package io.renren.service.impl;

import io.renren.common.doc.ParamKey;
import io.renren.dao.CourseAbnormalFreeAssessmentDao;
import io.renren.dao.MallOrderDao;
import io.renren.entity.CourseAbnormalFreeAssessmentEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.entity.MallOrderEntity;
import io.renren.enums.AuditStatusEnum;
import io.renren.enums.CourseAbnormalFreeAssessmentExcelEnum;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.pojo.CourseAbnormalFreeAssessmentPOJO;
import io.renren.pojo.CourseAbnormalOrderPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseAbnormalFreeAssessmentService;
import io.renren.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("courseAbnormalFreeAssessmentService")
public class CourseAbnormalFreeAssessmentServiceImpl implements CourseAbnormalFreeAssessmentService {

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CourseAbnormalFreeAssessmentDao courseAbnormalFreeAssessmentDao;
	@Autowired
	private MallOrderDao mallOrderDao;
    @Autowired
    private CourseUserplanServiceImpl courseUserplanService;

    @Autowired
    private CourseUserplanDetailServiceImpl courseUserplanDetailService;

	@Resource
	KGS courseAbnormalFreeAssessmentNoKGS;

	private static String DOWN_EXCEL_STRING = "";

	@Override
	public CourseAbnormalFreeAssessmentPOJO queryObject(Long id){
		return courseAbnormalFreeAssessmentDao.queryPojoObject(id);
	}
	
	@Override
	public List<CourseAbnormalFreeAssessmentPOJO> queryList(Map<String, Object> map){
		return courseAbnormalFreeAssessmentDao.queryPojoList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseAbnormalFreeAssessmentDao.queryTotal(map);
	}

	@Override
	public void save(CourseAbnormalFreeAssessmentEntity courseAbnormalFreeAssessment){
        verifyStatus(courseAbnormalFreeAssessment.getOrderId(),courseAbnormalFreeAssessment.getCourseId(),courseAbnormalFreeAssessment.getStartTime(),courseAbnormalFreeAssessment.getEndTime());
		final String orderNo = "AF" + courseAbnormalFreeAssessmentNoKGS.nextId();
		courseAbnormalFreeAssessment.setOrderno(orderNo);
		courseAbnormalFreeAssessmentDao.save(courseAbnormalFreeAssessment);
	}

    @Override
	public void updateStatus(Integer auditStatus,Long id){
	    Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("auditStatus",auditStatus);
	    courseAbnormalFreeAssessmentDao.updateStatus(map);
	}

	@Override
	public void updateCancelBatch(Integer auditStatus,Long[] ids,Long userId,Date date){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("ids",ids);
        map.put("auditStatus",auditStatus);
        map.put("updatePerson",userId);
        map.put("updateTime",date);
	    courseAbnormalFreeAssessmentDao.updateCancelBatch(map);
	}

    @Override
    public void updateAuditPassBatch(Integer auditStatus,Long[] ids,Long userId,Date date){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("ids",ids);
        map.put("auditStatus",auditStatus);
        map.put("modifyPerson",userId);
        map.put("modifiedTime",date);
        courseAbnormalFreeAssessmentDao.updateAuditPassBatch(map);
    }
	@Override
	public String downExcel() {
		if(StringUtils.isBlank(DOWN_EXCEL_STRING)) {
			StringBuffer sbf = new StringBuffer();
			//顶部
			sbf.append("0,0,0,0,学员姓名&0,1,0,0,手机号码&0,2,0,0,订单编号&0,3,0,0,课程名称&0,4,0,0,开始时间&0,5,0,0,结束时间&0,6,0,0,原因&0,7,0,0,备注");
			//1
			sbf.append("&1,0,0,0,缪耀莹&1,1,0,0,15398619409&1,2,0,0, import_1472434590740&1,3,0,0,开学典礼&1,4,0,0,2017/12/23&1,5,0,0,2018/03/20&1,6,0,0,太简单了");
            sbf.append("&2,0,0,0,尹家英&2,1,0,0,15108749407&2,2,0,0, import_1472269701297&2,3,0,0,考霸训练营&2,4,0,0,2017/12/23&2,5,0,0,2018/03/20&2,6,0,0,太简单了");
			DOWN_EXCEL_STRING = sbf.toString();
		}
		return DOWN_EXCEL_STRING;
	}

    @Override
    public String importExcel(Long userId,MultipartFile file) {
        if(null != file) {
            StringBuffer errorMsg = new StringBuffer();
            try {
                FileInputStream fio = (FileInputStream) file.getInputStream();
                List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
                String[] header = dataList.get(0);
                if(null == dataList || dataList.isEmpty() || dataList.size() <= 1) {
                    throw new RRException("文件或内容有问题，请重新导入！");
                }
                int columnLength = 8;
                //总共六列数据
                if(header.length < columnLength){
                    throw new RRException("总列数不正确，请核对一下列数或重新下载导入模板！");
                }
                for(int i = 1 , length = dataList.size() ; i < length ; i++) {
                    int line = i+1;
                    String[] dataItem = dataList.get(i);
                    //列数校验
                    if(dataItem.length < columnLength) {
                        errorMsg.append("第" + line + "行错误：" + "列数不正确！<br/>");
                        continue;
                    }
                    //列
                    String mallOrderNo = dataItem[CourseAbnormalFreeAssessmentExcelEnum.mallOrderNo.getExcelIndex()];
                    String coursesName = dataItem[CourseAbnormalFreeAssessmentExcelEnum.courseName.getExcelIndex()];
                    String startTime = dataItem[CourseAbnormalFreeAssessmentExcelEnum.startTime.getExcelIndex()];
                    String endTime = dataItem[CourseAbnormalFreeAssessmentExcelEnum.endTime.getExcelIndex()];
                    String abnormalReason = dataItem[CourseAbnormalFreeAssessmentExcelEnum.abnormalReason.getExcelIndex()];
                    String remark = dataItem[CourseAbnormalFreeAssessmentExcelEnum.remark.getExcelIndex()];
                    String saveExcelColumnString = saveExcelColumn(userId,mallOrderNo,coursesName, startTime,endTime, abnormalReason,remark);
                    if(StringUtils.isNotBlank(saveExcelColumnString)) {
                        errorMsg.append("第" + line + "行错误：" + saveExcelColumnString +"<br/>");
                    }else{
                        errorMsg.append("第" + line + "行提示：保存成功<br/>");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return errorMsg.toString();
        }
        return null;
    }

    private String saveExcelColumn(Long userId,String mallOrderNo,String courseName,String startTime,String endTime,String abnormalReason,String remark){
        StringBuffer sb = new StringBuffer();
	    //非空校验
		if(StringUtils.isBlank(mallOrderNo)) sb.append(CourseAbnormalFreeAssessmentExcelEnum.mallOrderNo.getColumnName()+"不能为空；");
		if(StringUtils.isBlank(courseName)) sb.append(CourseAbnormalFreeAssessmentExcelEnum.courseName.getColumnName()+"不能为空；");
		if(StringUtils.isBlank(startTime)) sb.append(CourseAbnormalFreeAssessmentExcelEnum.startTime.getColumnName()+"不能为空；");
        if(StringUtils.isBlank(endTime)) sb.append(CourseAbnormalFreeAssessmentExcelEnum.endTime.getColumnName()+"不能为空；");
        if(!DateUtils.checkDateString(startTime)){
            sb.append(CourseAbnormalFreeAssessmentExcelEnum.startTime.getColumnName()+"格式不正确；");
        }
        if(!DateUtils.checkDateString(endTime)){
            sb.append(CourseAbnormalFreeAssessmentExcelEnum.endTime.getColumnName()+"格式不正确；");
        }
        if(DateUtils.checkDateString(startTime) && DateUtils.checkDateString(endTime)){
            if(startTime.equals(endTime) || new Date(startTime).after(new Date(endTime))){
                sb.append("开始时间不能大于等于结束时间;");
            }
        }
        try{
            CourseAbnormalFreeAssessmentEntity courseAbnormalFreeAssessment = new CourseAbnormalFreeAssessmentEntity();
            courseAbnormalFreeAssessment.setCreatePerson(userId);
            Map queryMap = new HashMap();
            queryMap.put("orderNo",mallOrderNo.trim());
            MallOrderEntity mallOrderEntity  = mallOrderDao.queryObject(queryMap);
            if(mallOrderEntity != null){
                courseAbnormalFreeAssessment.setOrderId(mallOrderEntity.getOrderId());
                courseAbnormalFreeAssessment.setProductId(mallOrderEntity.getProductId());
                Long courseId = getCourseId(mallOrderEntity.getOrderId(),courseName.trim());
                if(courseId == null){
                    sb.append("该订单不存在此课程；");
                }else{
                    courseAbnormalFreeAssessment.setCourseId(courseId);
                }
            }else{
                sb.append("订单不存在；");
            }
            if(sb.length() != 0){
                return sb.toString();
            }else{
                courseAbnormalFreeAssessment.setStartTime(new Date(startTime));
                courseAbnormalFreeAssessment.setEndTime(new Date(endTime));
                courseAbnormalFreeAssessment.setAbnormalReason(abnormalReason);
                courseAbnormalFreeAssessment.setAuditStatus(AuditStatusEnum.tongguo.getValue());
                courseAbnormalFreeAssessment.setRemark(remark);
                BeanHelper.beanAttributeValueTrim(courseAbnormalFreeAssessment);
                save(courseAbnormalFreeAssessment);
            }
        }catch(Exception e){
            logger.error("e.getMessageis{ }",e);
		    return e.getMessage();
        }
		return null;
	}

	Long getCourseId(Long orderId,String courseName){
        String userPlanId = courseUserplanService.queryUserPlanId(orderId.toString());
        if(userPlanId != null){
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("userPlanId",userPlanId);
            map.put("offset",0);
            map.put("schoolId","zikao");
            map.put("limit", ParamKey.In.DEFAULT_MAX_LIMIT);
            map.put("targetGrade",true);
            map.put("examEn2",true);
            List<Map<String , Object>> list = this.courseUserplanDetailService.courseListByUserPlanId(map);
            for(Map listMap : list){
                if(listMap.get("courseName").toString().trim().equals(courseName)){
                   return (Long)listMap.get("courseId");
               }
            }
        }
        return null;
    }

    public void verifyStatus(Long orderId, Long courseId,Date startTime, Date endTime) {
        Map queryMap = new HashMap();
        queryMap.put("orderId",orderId);
        queryMap.put("courseId",courseId);
        queryMap.put("startTime",startTime);
        CourseAbnormalFreeAssessmentPOJO courseAbnormalFreeAssessmentPOJO = courseAbnormalFreeAssessmentDao.verifyStatus(queryMap);
        if(courseAbnormalFreeAssessmentPOJO != null){
            throw new RRException("存在类似单号：" + courseAbnormalFreeAssessmentPOJO.getOrderno());
        }
    }
}
