package io.renren.service.impl;

import io.renren.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import io.renren.common.doc.ParamKey;
import io.renren.dao.CourseAbnormalAbandonExamDao;
import io.renren.dao.CourseAbnormalRegistrationDao;
import io.renren.dao.MallAreaDao;
import io.renren.dao.MallExamScheduleDao;
import io.renren.dao.MallOrderDao;
import io.renren.entity.CourseAbnormalAbandonExamEntity;
import io.renren.entity.CourseAbnormalRegistrationEntity;
import io.renren.entity.MallExamScheduleEntity;
import io.renren.entity.MallOrderEntity;
import io.renren.entity.SysUserEntity;
import io.renren.enums.AuditStatusEnum;
import io.renren.enums.CourseAbnormalAbandonExamExcelEnum;
import io.renren.enums.CourseAbnormalFreeAssessmentExcelEnum;
import io.renren.enums.CourseAbnormalRegistrationExcelEnum;
import io.renren.pojo.CourseAbnormalAbandonExamPOJO;
import io.renren.pojo.CourseAbnormalRegistrationPOJO;
import io.renren.pojo.CourseAbnormalRegistrationPOJO1;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseAbnormalAbandonExamService;
import io.renren.ws.chapterinfo.GetUsers;



@Service("courseAbnormalAbandonExamService")
public class CourseAbnormalAbandonExamServiceImpl implements CourseAbnormalAbandonExamService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CourseAbnormalAbandonExamDao courseAbnormalAbandonExamDao;	
	@Autowired
	private MallOrderDao mallOrderDao;
	@Autowired
	private MallAreaDao mallAreaDao;
	@Autowired
    private CourseUserplanServiceImpl courseUserplanService;	
	@Autowired
	private CourseUserplanDetailServiceImpl courseUserplanDetailService;
	@Autowired
	private MallExamScheduleDao mallExamScheduleDao;	
	@Resource
	KGS invoicesNumberKGS;	
	private static final String INVOICESNUMBER_HEAD = "HQQK";
	private static String DOWN_EXCEL_STRING = "";
	
	
	@Override
	public CourseAbnormalAbandonExamEntity queryObject(Long id){
		return courseAbnormalAbandonExamDao.queryObject(id);
	}
				
	@Override
	public List<CourseAbnormalAbandonExamEntity> queryList(Map<String, Object> map){
		return courseAbnormalAbandonExamDao.queryList(map);
	}
		
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseAbnormalAbandonExamDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam) {
		//验证状态
		verifySave(courseAbnormalAbandonExam);
		verifySave(courseAbnormalAbandonExam);
		courseAbnormalAbandonExamDao.save(courseAbnormalAbandonExam);
		
	}

	@Override
	public String saveExamEntity(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam){				
		//验证状态
		//System.out.println(courseAbnormalAbandonExam.toString());
		String msg = "保存成功";
			try {
				//加入去除前后空格操作
				BeanHelper.beanAttributeValueTrim(courseAbnormalAbandonExam);
				verifySave(courseAbnormalAbandonExam);
				courseAbnormalAbandonExamDao.save(courseAbnormalAbandonExam);
			} catch (RRException e) {
				e.printStackTrace();				
				msg = e.getMsg();
			}		
			return msg;
	}
	
	private void verifySave(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam) {
		if(null != courseAbnormalAbandonExam){
			//待审核
			CourseAbnormalAbandonExamEntity dshEntity = courseAbnormalAbandonExamDao.queryEntityByRegistrationId(courseAbnormalAbandonExam.getRegistrationId(), AuditStatusEnum.daishenhe.getValue());
			if(null != dshEntity){
				throw new RRException("弃考单已经存在，单号:【"+dshEntity.getInvoicesNumber()+"】" + AuditStatusEnum.getText(dshEntity.getStatus()));
			}
			//审核通过
			CourseAbnormalAbandonExamEntity tgEntity = courseAbnormalAbandonExamDao.queryEntityByRegistrationId(courseAbnormalAbandonExam.getRegistrationId(), AuditStatusEnum.tongguo.getValue());
			if(null != tgEntity){
				throw new RRException("弃考单已经存在，单号:【"+tgEntity.getInvoicesNumber()+"】" + AuditStatusEnum.getText(tgEntity.getStatus()));
			}

		}
	}
	@Override
	public void update(CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam){
		courseAbnormalAbandonExamDao.update(courseAbnormalAbandonExam);
	}
				
	//查询弃考数据
	@Override
	public List<CourseAbnormalAbandonExamPOJO> queryPOJOList(Map<String, Object> map) {		
		return courseAbnormalAbandonExamDao.queryPOJOList(map);
	}
	
	//批量保存弃考数据
	@Override
	public void saveBatch(List<CourseAbnormalAbandonExamEntity> list) {
		courseAbnormalAbandonExamDao.saveBatch(list);		
	}
	//报考订单数据
	@Override
	public CourseAbnormalRegistrationPOJO1 queryByRegistrationNo(Map<String, Object> map) {	
		return courseAbnormalAbandonExamDao.queryByRegistrationNo(map);
	}	
	
	//根据订单号查询
	@Override
	public CourseAbnormalRegistrationPOJO1 queryByOrderNo(String orderNo) {		
		return courseAbnormalAbandonExamDao.queryByOrderNo(orderNo);
	}
	
	//查询报考单数据
	@Override
	public CourseAbnormalRegistrationPOJO1 queryByNo(Map<String, Object> map) {		
		return courseAbnormalAbandonExamDao.queryByNo(map);
	}
		
	//作废
	@Override
	public void cancel(Integer status, Long[] ids, Long userId, Date date) {
		Map<String,Object> map = new HashMap<>();		
		map.put("status", status);
		map.put("list", ids);
		map.put("updatePerson",userId);
        map.put("updateTime",date);
		courseAbnormalAbandonExamDao.updateCancelStatus(map);	
		
	}
	//审核
	@Override
	public void pass(Integer status, Long[] ids, Long userId, Date date) {
		Map<String,Object> map = new HashMap<>();		
		map.put("status", status);
		map.put("list", ids);
		map.put("updatePerson",userId);
        map.put("updateTime",date);
		courseAbnormalAbandonExamDao.updatePassStatus(map);	
		
	}
	
	
	@Override
	public String downExcel() {
		if(StringUtils.isBlank(DOWN_EXCEL_STRING)) {
			StringBuffer sbf = new StringBuffer();
			//顶部
			sbf.append("0,0,0,0,订单编号&0,1,0,0,课程名称&0,2,0,0,报考省份&0,3,0,0,考试时段&0,4,0,0,原因&0,5,0,0,备注");
			//1
			sbf.append("&1,0,0,0,import_1472454998177&1,1,0,0,公共关系学&1,2,0,0,北京&1,3,0,0,2017年4月自学考试时间&1,4,0,0,原来是这样的&1,5,0,0,太简单了");
			DOWN_EXCEL_STRING = sbf.toString();
		}
		return DOWN_EXCEL_STRING;
		
	}

	@Override
	public String importExcel(Long userId, MultipartFile file) {
		   if(null != file) {
	            StringBuffer errorMsg = new StringBuffer();
	            try {
	                FileInputStream fio = (FileInputStream) file.getInputStream();
	                List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
	                String[] header = dataList.get(0);
	                if(null == dataList || dataList.isEmpty() || dataList.size() <= 1) {
	                    throw new RRException("文件或内容有问题，请重新导入！");
	                }
	                int columnLength = 6;
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
							String orderNo = dataItem[CourseAbnormalAbandonExamExcelEnum.orderNo.getExcelIndex()];
							String courseName = dataItem[CourseAbnormalAbandonExamExcelEnum.courseName.getExcelIndex()];
							String registerProvince = dataItem[CourseAbnormalAbandonExamExcelEnum.registerProvince.getExcelIndex()];
							String scheduleName = dataItem[CourseAbnormalAbandonExamExcelEnum.scheduleName.getExcelIndex()];
							String reason = dataItem[CourseAbnormalAbandonExamExcelEnum.reason.getExcelIndex()];
							String remark = dataItem[CourseAbnormalAbandonExamExcelEnum.remark.getExcelIndex()];	                   
							String saveExcelColumnString = saveExcelColumn(userId,orderNo,courseName, registerProvince,scheduleName, reason,remark);
							if(StringUtils.isNotBlank(saveExcelColumnString)) {
							    errorMsg.append("第" + line + "行提示：" + saveExcelColumnString +"<br/>");
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

	private String saveExcelColumn(Long userId, String orderNo, String courseName, String registerProvince,
			String scheduleName, String reason, String remark) {
				StringBuffer sb = new StringBuffer();
				//非空校验
				if(StringUtils.isBlank(orderNo)) sb.append(CourseAbnormalAbandonExamExcelEnum.orderNo.getColumnName()+"不能为空;");				 
				if(StringUtils.isBlank(courseName)) sb.append(CourseAbnormalAbandonExamExcelEnum.courseName.getColumnName()+"不能为空;");
				if(StringUtils.isBlank(registerProvince)) sb.append(CourseAbnormalAbandonExamExcelEnum.registerProvince.getColumnName()+"不能为空;");
		        if(StringUtils.isBlank(scheduleName)) sb.append(CourseAbnormalAbandonExamExcelEnum.scheduleName.getColumnName()+"不能为空;");		       
		        try{		        			        	 
		        	//验证订单号：
		        	Map orderNoMap = new HashMap();
		        	orderNoMap.put("orderNo",orderNo.trim());
		        	//TODO 优化 学员规划ID 获取课程信息
		            MallOrderEntity mallOrderEntity  = mallOrderDao.queryObject(orderNoMap);
		            if(mallOrderEntity == null){		            	
		            	sb.append("订单号不存在;");
		            	//return "订单号不存在";		            			            	
		             }else{		            	
		            	//验证课程
		            	Long courseId = getCourseId(mallOrderEntity.getOrderId(),courseName.trim());
		            	if(courseId == null){		             
		            		sb.append("该订单不存在此课程;");		            	
		            		//return "该订单不存在此课程！";
		            	}
		            }
		        	//验证报考省份
		            Map queryAreaMap = new HashMap();
		            queryAreaMap.put("examProvinceName",registerProvince.trim());
		            Long bkAreaId = mallAreaDao.getAreaIdByExamProvinceName(queryAreaMap);
		            if(bkAreaId == null){
		            	sb.append("不存在该省份;");
		            	//return"不存在该省份";
		            }
		        	//验证 报考时间段
		            Map queryExamMap = new HashMap();
		            queryExamMap.put("scheduleName", scheduleName.trim());
//		            queryExamMap.put("productId",mallOrderEntity.getProductId());
		            MallExamScheduleEntity examScheduleEntity = mallExamScheduleDao.queryPojoByParam(queryExamMap);
		            if(examScheduleEntity == null){
		            	sb.append("不存在该考试时段;");
		            	//return "不存在该考试时段";
		            }
		            CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam = new CourseAbnormalAbandonExamEntity();
		        	courseAbnormalAbandonExam.setCreater(userId);
		        	Map<String,Object> queryMap = new HashMap<String,Object>();
		            queryMap.put("orderNo",orderNo.trim());
		            queryMap.put("courseName",courseName.trim());
		            queryMap.put("registerProvince",registerProvince.trim());
		            queryMap.put("scheduleName",scheduleName.trim());		            
		            CourseAbnormalRegistrationPOJO1 queryByNo = courseAbnormalAbandonExamDao.queryByNo(queryMap);		           
		            if(queryByNo != null){
		            	courseAbnormalAbandonExam.setRegistrationId(queryByNo.getId());
		            	courseAbnormalAbandonExam.setProductId(queryByNo.getProductId());		            	
		            }else{
		            	sb.append("订单不存在;");
		            		//return "订单不存在";
		            }		           		           		            		            	
		            	
		            if(sb.length() != 0){
		                return sb.toString();
		            }else{
		            courseAbnormalAbandonExam.setStatus(AuditStatusEnum.tongguo.getValue());
		            	courseAbnormalAbandonExam.setInvoicesNumber(INVOICESNUMBER_HEAD+invoicesNumberKGS.nextId());
		            	courseAbnormalAbandonExam.setCreateTime(new Date());
		            	courseAbnormalAbandonExam.setUpdateTime(new Date());
		            	courseAbnormalAbandonExam.setCreater(userId);
		            	courseAbnormalAbandonExam.setUpdatePerson(userId);
		            	courseAbnormalAbandonExam.setReason(reason);
		            	courseAbnormalAbandonExam.setRemark(remark);
		            	return this.saveExamEntity(courseAbnormalAbandonExam);
		            }   
		        }catch(Exception e){
		        	logger.error("e.getMessageis{ }",e);
				    return e.getMessage();
		        }			
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

	
	
	

	

	

	
	
	
	
}
