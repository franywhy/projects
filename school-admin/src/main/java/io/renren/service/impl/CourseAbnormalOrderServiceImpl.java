package io.renren.service.impl;

import io.renren.dao.MallOrderDao;
import io.renren.entity.MallOrderEntity;
import io.renren.enums.AuditStatusEnum;
import io.renren.enums.CourseAbnormalFreeAssessmentExcelEnum;
import io.renren.enums.CourseAbnormalOrderExcelEnum;
import io.renren.enums.CourseAbormalTypeEnum;
import io.renren.pojo.CourseAbnormalOrderPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.utils.BeanHelper;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.RRException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.dao.CourseAbnormalOrderDao;
import io.renren.entity.CourseAbnormalOrderEntity;
import io.renren.service.CourseAbnormalOrderService;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@Service("courseAbnormalOrderService")
public class CourseAbnormalOrderServiceImpl implements CourseAbnormalOrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CourseAbnormalOrderDao courseAbnormalOrderDao;
	@Autowired
	private MallOrderDao mallOrderDao;


	@Resource
	KGS courseAbnormalOrderNoKGS;

	private static String DOWN_EXCEL_STRING = "";

	@Override
	public CourseAbnormalOrderPOJO queryObject(Long id){
		return courseAbnormalOrderDao.queryPojoObject(id);
	}
	
	@Override
	public List<CourseAbnormalOrderPOJO> queryList(Map<String, Object> map){
		return courseAbnormalOrderDao.queryPojoList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseAbnormalOrderDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseAbnormalOrderEntity courseAbnormalOrder){
		final String orderNo = "AO" + courseAbnormalOrderNoKGS.nextId();
		courseAbnormalOrder.setOrderno(orderNo);
        verifyStatus(courseAbnormalOrder.getOrderId(),courseAbnormalOrder.getStartTime(),courseAbnormalOrder.getEndTime());
        BeanHelper.beanAttributeValueTrim(courseAbnormalOrder);
		courseAbnormalOrderDao.save(courseAbnormalOrder);
	}
	
	@Override
	public void update(CourseAbnormalOrderEntity courseAbnormalOrder){
        verifyStatus(courseAbnormalOrder.getId(),courseAbnormalOrder.getOrderId(),courseAbnormalOrder.getStartTime(),courseAbnormalOrder.getEndTime());
		courseAbnormalOrderDao.update(courseAbnormalOrder);
	}

    @Override
    public void auditPass(CourseAbnormalOrderEntity courseAbnormalOrder) {
        verifyStatus(courseAbnormalOrder.getOrderId(),courseAbnormalOrder.getStartTime(),courseAbnormalOrder.getEndTime());
        courseAbnormalOrderDao.update(courseAbnormalOrder);
    }

    @Override
    public void saveResumeClasses(CourseAbnormalOrderEntity courseAbnormalOrder) {
        courseAbnormalOrderDao.update(courseAbnormalOrder);
    }

    @Override
	public void updateCancelBatch(Integer status,Long[] ids){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("ids",ids);
        map.put("auditStatus",status);
		courseAbnormalOrderDao.updateCancelBatch(map);
	}

    @Override
    public void updateCancelBatch(Integer auditStatus,Long[] ids,Long userId,Date date){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("ids",ids);
        map.put("auditStatus",auditStatus);
        map.put("updatePerson",userId);
        map.put("updateTime",date);
        courseAbnormalOrderDao.updateCancelBatch(map);
    }

	@Override
	public void deleteBatch(Map<String, Object> map){
		courseAbnormalOrderDao.deleteBatch(map);
	}

	@Override
	public String downExcel() {
		if(StringUtils.isBlank(DOWN_EXCEL_STRING)) {
			StringBuffer sbf = new StringBuffer();
			//顶部
			sbf.append("0,0,0,0,类型&0,1,0,0,学员姓名&0,2,0,0,学员号码&0,3,0,0,订单编号&0,4,0,0,预计开始时间&0,5,0,0,预计结束时间&0,6,0,0,异常原因&0,7,0,0,备注");
			//1
			sbf.append("&1,0,0,0,休学&1,1,0,0,曾利华&1,2,0,0,18320238051&1,3,0,0,import_1489820429742&1,4,0,0,2017/12/23&1,5,0,0,2018/03/20&1,6,0,0,学校考试");

			sbf.append("&2,0,0,0,失联&2,1,0,0,方平&2,2,0,0,13795648566&2,3,0,0,import_1490259054870&2,4,0,0,2018/03/25&2,5,0,0,2018/05/20&2,6,0,0,号码更换");
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
                int columnLength = 5;
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
                    String abnormalTypeStr = dataItem[CourseAbnormalOrderExcelEnum.abnormalTypeStr.getExcelIndex()];
                    String mallOrderNo = dataItem[CourseAbnormalOrderExcelEnum.mallOrderNo.getExcelIndex()];
                    String startTime = dataItem[CourseAbnormalOrderExcelEnum.startTime.getExcelIndex()];
                    String expectEndTime = dataItem[CourseAbnormalOrderExcelEnum.expectEndTime.getExcelIndex()];
                    String abnormalReason = dataItem[CourseAbnormalOrderExcelEnum.abnormalReason.getExcelIndex()];
                    String remark = dataItem[CourseAbnormalOrderExcelEnum.remark.getExcelIndex()];
                    String saveExcelColumnString = saveExcelColumn(userId,abnormalTypeStr ,mallOrderNo, startTime,expectEndTime, abnormalReason,remark);
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

    @Override
    public Integer verifyStatusCount(Long orderId, Date startTime, Date endTime) {
        Map queryMap = new HashMap();
        queryMap.put("orderId",orderId);
        queryMap.put("startTime",startTime);
        CourseAbnormalOrderPOJO courseAbnormalOrderPOJO = courseAbnormalOrderDao.verifyStatus(queryMap);
        if(courseAbnormalOrderPOJO != null){
            try {
                throw new RRException("存在类似单据！单据单号为：" + courseAbnormalOrderPOJO.getOrderno());
            }catch (Exception e){
                return courseAbnormalOrderPOJO.getAbnormalType();
            }
        }
        return null;
    }

    public void verifyStatus(Long orderId, Date startTime, Date endTime) {
        Map queryMap = new HashMap();
        queryMap.put("orderId",orderId);
        queryMap.put("startTime",startTime);
        CourseAbnormalOrderPOJO courseAbnormalOrderPOJO = courseAbnormalOrderDao.verifyStatus(queryMap);
        if(courseAbnormalOrderPOJO != null){
            throw new RRException("存在类似单号：" + courseAbnormalOrderPOJO.getOrderno());
        }
    }

    public void verifyStatus(Long id,Long orderId, Date startTime, Date endTime) {
        Map queryMap = new HashMap();
        queryMap.put("id",id);
        queryMap.put("orderId",orderId);
        queryMap.put("startTime",startTime);
        CourseAbnormalOrderPOJO courseAbnormalOrderPOJO = courseAbnormalOrderDao.verifyStatus(queryMap);
        if(courseAbnormalOrderPOJO != null){
            throw new RRException("存在类似单号：" + courseAbnormalOrderPOJO.getOrderno());
        }
    }

    @Override
    public CourseAbormalTypeEnum verifyXiuxueOrShilian(Long orderId, Date startTime) {
        Integer result = verifyStatusCount(orderId,startTime,null);
        if(result != null){
            return CourseAbormalTypeEnum.getE(result);
        }
        return null;
    }

    private String saveExcelColumn(Long userId,String abnormalTypeStr,String mallOrderNo,String startTime,String expectEndTime,String abnormalReason,String remark){
		StringBuffer sb = new StringBuffer();
	    //非空校验
		if(StringUtils.isBlank(abnormalTypeStr)) sb.append(CourseAbnormalOrderExcelEnum.abnormalTypeStr.getColumnName()+"不能为空;");
		if(StringUtils.isBlank(mallOrderNo)) sb.append(CourseAbnormalOrderExcelEnum.mallOrderNo.getColumnName()+"不能为空;");
		if(StringUtils.isBlank(startTime)) sb.append(CourseAbnormalOrderExcelEnum.startTime.getColumnName()+"不能为空;") ;
		if(StringUtils.isBlank(expectEndTime)) sb.append(CourseAbnormalOrderExcelEnum.expectEndTime.getColumnName()+"不能为空;") ;
        if(!DateUtils.checkDateString(startTime)){
            sb.append(CourseAbnormalOrderExcelEnum.startTime.getColumnName()+"格式不正确;");
        }
        if(!DateUtils.checkDateString(expectEndTime)){
            sb.append(CourseAbnormalOrderExcelEnum.expectEndTime.getColumnName()+"格式不正确;");
        }
        if(DateUtils.checkDateString(startTime) && DateUtils.checkDateString(expectEndTime)){
            if(startTime.equals(expectEndTime) || new Date(startTime).after(new Date(expectEndTime))){
                sb.append("预计开始时间不能大于等于预计结束时间;");
            }
        }
        try{
            CourseAbnormalOrderEntity courseAbnormalOrder = new CourseAbnormalOrderEntity();
            courseAbnormalOrder.setCreatePerson(userId);
            Integer abnormalType = CourseAbormalTypeEnum.getValue(abnormalTypeStr);
            if(abnormalType != null){
                courseAbnormalOrder.setAbnormalType(CourseAbormalTypeEnum.getValue(abnormalTypeStr.trim()));
            }else{
                sb.append("异常类型填写错误;");
            }
            Map queryMap = new HashMap();
            queryMap.put("orderNo",mallOrderNo);
            MallOrderEntity mallOrderEntity  = mallOrderDao.queryObject(queryMap);
            if(mallOrderEntity != null){
                courseAbnormalOrder.setOrderId(mallOrderEntity.getOrderId());
                courseAbnormalOrder.setProductId(mallOrderEntity.getProductId());
            }else{
                sb.append("订单不存在;");
            }
            if(sb.length() != 0){
                return sb.toString();
            }else{
                courseAbnormalOrder.setStartTime(new Date(startTime));
                courseAbnormalOrder.setExpectEndTime(new Date(expectEndTime));
                courseAbnormalOrder.setAbnormalReason(abnormalReason);
                courseAbnormalOrder.setAuditStatus(AuditStatusEnum.tongguo.getValue());
                courseAbnormalOrder.setRemark(remark);
                BeanHelper.beanAttributeValueTrim(courseAbnormalOrder);
                save(courseAbnormalOrder);
            }
        }catch(Exception e){
            logger.error("e.getMessageis{ }",e);
		    return e.getMessage();
        }
		return null;
	}
}
