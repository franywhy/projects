package io.renren.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import io.renren.utils.BeanHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import io.renren.enums.CourseAbnormalRegistrationExcelEnum;
import io.renren.pojo.CourseAbnormalRegistrationPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseAbnormalRegistrationService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.RRException;
import io.renren.utils.ShiroUtils;




@Service("courseAbnormalRegistrationService")
public class CourseAbnormalRegistrationServiceImpl implements CourseAbnormalRegistrationService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CourseAbnormalRegistrationDao courseAbnormalRegistrationDao;
	
	@Autowired
	private MallOrderDao mallOrderDao;
	@Autowired
	private MallExamScheduleDao mallExamScheduleDao;
//	@Autowired
//	private CourseUserplanDao courseUserplanDao;
	@Autowired
	private MallAreaDao mallAreaDao;
	@Autowired
	private CourseAbnormalAbandonExamDao courseAbnormalAbandonExamDao;
	
	@Autowired
    private CourseUserplanServiceImpl courseUserplanService;
	
	@Autowired
	private CourseUserplanDetailServiceImpl courseUserplanDetailService;
	
	@Resource
	private KGS baoKaoNoKGS;
	
	private static String DOWN_EXCEL_STRING = "";
	
	protected SysUserEntity getUser() {
		return ShiroUtils.getUserEntity();
	}
	
	@Override
	public CourseAbnormalRegistrationEntity queryObject(Long id){
		return courseAbnormalRegistrationDao.queryObject(id);
	}
	
	@Override
	public List<CourseAbnormalRegistrationEntity> queryList(Map<String, Object> map){
		return courseAbnormalRegistrationDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseAbnormalRegistrationDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseAbnormalRegistrationEntity courseAbnormalRegistration){
		
		courseAbnormalRegistration.setRegistrationNo("HQBK"+this.baoKaoNoKGS.nextId());
		courseAbnormalRegistration.setRegistrationTime(new Date());
		courseAbnormalRegistration.setDr(0);
		String userPlanId = courseUserplanService.queryUserPlanId(courseAbnormalRegistration.getOrderId());
		courseAbnormalRegistration.setUserPlanId(userPlanId);
		
		Map queryMap = new HashMap();
        queryMap.put("orderNo",courseAbnormalRegistration.getOrderNo());
        MallOrderEntity mallOrderEntity  = mallOrderDao.queryObject(queryMap);
        if(mallOrderEntity != null){
        	courseAbnormalRegistration.setProductId(mallOrderEntity.getProductId());
        }
        if(StringUtils.isNotBlank(courseAbnormalRegistration.getRegistrationNumber())){
        	//报考登记号验证
        	if(!verRegistrationEntity(courseAbnormalRegistration.getProductId(),courseAbnormalRegistration.getRegistrationNumber())){
        		courseAbnormalRegistrationDao.save(courseAbnormalRegistration);
        	}
        }
		
//		System.out.println("实体：------------"+courseAbnormalRegistration);
		
	}
	
	
	/**
	 * 保存实体
	 * @param courseAbnormalRegistration
	 * @return
	 */
	@Override
	public String saveEntity(CourseAbnormalRegistrationEntity courseAbnormalRegistration){
		courseAbnormalRegistration.setRegistrationNo("HQBK"+this.baoKaoNoKGS.nextId());
		courseAbnormalRegistration.setRegistrationTime(new Date());
		courseAbnormalRegistration.setDr(0);
		String userPlanId = courseUserplanService.queryUserPlanId(courseAbnormalRegistration.getOrderId());
		courseAbnormalRegistration.setUserPlanId(userPlanId);
		//查询是否重复
		Map queryRegistrationMap = new HashMap();
		queryRegistrationMap.put("statusNo", 1);
		queryRegistrationMap.put("orderNo", courseAbnormalRegistration.getOrderNo());
		queryRegistrationMap.put("courseId", courseAbnormalRegistration.getCourseId());
		queryRegistrationMap.put("bkAreaId", courseAbnormalRegistration.getBkAreaId());
		queryRegistrationMap.put("examScheduleId", courseAbnormalRegistration.getExamScheduleId());
		
		List<CourseAbnormalRegistrationEntity> registrationEntity = courseAbnormalRegistrationDao.queryRegistrationEntity(queryRegistrationMap);
		if (null != registrationEntity && registrationEntity.size()>0) {
			return "你有一个相同报考时间的课程报考单正在审核或者已经通过,请勿重复提交";
		}
		Map queryMap = new HashMap();
        queryMap.put("orderNo",courseAbnormalRegistration.getOrderNo());
        MallOrderEntity mallOrderEntity  = mallOrderDao.queryObject(queryMap);
        if(mallOrderEntity != null){
        	courseAbnormalRegistration.setProductId(mallOrderEntity.getProductId());
        }
        if(StringUtils.isNotBlank(courseAbnormalRegistration.getRegistrationNumber())){
        	//报考登记号验证
        	if(!verRegistrationEntity(courseAbnormalRegistration.getProductId(),courseAbnormalRegistration.getRegistrationNumber())){
        		courseAbnormalRegistrationDao.save(courseAbnormalRegistration);
        		return "保存成功";
        	}else{
        		return "已存在报考登记号";
        	}
        }else{
        	courseAbnormalRegistrationDao.save(courseAbnormalRegistration);
        	return "保存成功";
        }
	}
	
	@Override
	public void update(CourseAbnormalRegistrationEntity courseAbnormalRegistration){
		courseAbnormalRegistrationDao.update(courseAbnormalRegistration);
	}

	@Override
	public List<CourseAbnormalRegistrationPOJO> queryRegistrationList(Map<String, Object> map) {
		return courseAbnormalRegistrationDao.queryRegistrationList(map);
	}

	@Override
	public void cancellation(Long[] registrationIds) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", registrationIds);
    	map.put("status", 1);
    	map.put("updatePerson", getUser().getUserId());
    	courseAbnormalRegistrationDao.updateBatch(map);
	}
	
	@Override
	public String audit(Long[] registrationIds) {
		/*for(int i = 0 ; i < registrationIds.length ; i++){
			
			CourseAbnormalRegistrationEntity registrationEntity=courseAbnormalRegistrationDao.queryObject(registrationIds[i]);
			if(StringUtils.isNotBlank(registrationEntity.getRegistrationNumber())){
				if(registrationEntity.getStatus()==2){
	        		return "该报考单已审核，无需重复审核";
	        	}
				//报考登记号验证
	        	if(verRegistrationEntity(registrationEntity.getProductId(),registrationEntity.getRegistrationNumber())){
	        		return "已存在通过报考登记号无法通过审核";
	        	}
	        }
		}*/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", registrationIds);
		map.put("status", 2);
		map.put("updatePerson", getUser().getUserId());
		courseAbnormalRegistrationDao.updateBatch(map);
		
		return "操作成功";
	}
	
	@Override
	public String opposeAudit(Long[] registrationIds) {
		
		for(int i = 0 ; i < registrationIds.length ; i++){
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("registrationId", registrationIds[i]);
			queryMap.put("statusNo", 1);
			List<CourseAbnormalAbandonExamEntity> abandonExamList = courseAbnormalAbandonExamDao.queryByRegistrationId(queryMap);
		if(null != abandonExamList && abandonExamList.size()>0){
        		return "学员弃考单状态为待审核或者审核通过，报考登记单不可以反审核";
        }
	}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", registrationIds);
		map.put("status", 0);
		map.put("updatePerson", getUser().getUserId());
		courseAbnormalRegistrationDao.updateBatch(map);
		return "操作成功";
	}

	@Override
	public List<CourseAbnormalRegistrationPOJO> queryRegistrationLayList(Map<String, Object> map) {
		return courseAbnormalRegistrationDao.queryRegistrationLayList(map);
	}
	
	@Override
	public String downExcel() {
		if(StringUtils.isBlank(DOWN_EXCEL_STRING)) {
			StringBuffer sbf = new StringBuffer();
			//顶部
			sbf.append("0,0,0,0,订单号&0,1,0,0,课程&0,2,0,0,报考省份&0,3,0,0,报考时间段名称&0,4,0,0,报考单号（非必填）");
			//1
			sbf.append("&1,0,0,0,HQDD01&1,1,0,0,语文&1,2,0,0, 广东省&1,3,0,0,CPA本期考试时间&1,4,0,0,2017001200");
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
                    String mallOrderNo = dataItem[CourseAbnormalRegistrationExcelEnum.mallOrderNo.getExcelIndex()];
                    String coursesName = dataItem[CourseAbnormalRegistrationExcelEnum.courseName.getExcelIndex()];
                    String bkArea = dataItem[CourseAbnormalRegistrationExcelEnum.bkArea.getExcelIndex()];
                    String scheduleName = dataItem[CourseAbnormalRegistrationExcelEnum.scheduleName.getExcelIndex()];
                    String registrationNo = dataItem[CourseAbnormalRegistrationExcelEnum.registrationNo.getExcelIndex()];
                    /*String remark = dataItem[CourseAbnormalFreeAssessmentExcelEnum.remark.getExcelIndex()];*/
                    String saveExcelColumnString = saveExcelColumn(userId,mallOrderNo,coursesName,bkArea,scheduleName,registrationNo);
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
	
	
	private String saveExcelColumn(Long userId,String mallOrderNo,String courseName,String bkArea,String scheduleName,String registrationNo){
		StringBuffer sb = new StringBuffer();
		//非空校验
		if(StringUtils.isBlank(mallOrderNo)) return CourseAbnormalRegistrationExcelEnum.mallOrderNo.getColumnName()+"不能为空！";
		if(StringUtils.isBlank(courseName)) return CourseAbnormalRegistrationExcelEnum.courseName.getColumnName()+"不能为空！";
		if(StringUtils.isBlank(bkArea)) return CourseAbnormalRegistrationExcelEnum.bkArea.getColumnName()+"不能为空！";
        if(StringUtils.isBlank(scheduleName)) return CourseAbnormalRegistrationExcelEnum.scheduleName.getColumnName()+"不能为空！";
        try{
        	CourseAbnormalRegistrationEntity courseAbnormalRegistration = new CourseAbnormalRegistrationEntity();
        	courseAbnormalRegistration.setCreater(userId);
            Map queryMap = new HashMap();
            queryMap.put("orderNo",mallOrderNo.trim());
            MallOrderEntity mallOrderEntity  = mallOrderDao.queryObject(queryMap);
            if(mallOrderEntity != null){
            	courseAbnormalRegistration.setOrderNo(mallOrderEntity.getOrderNo());
            	courseAbnormalRegistration.setProductId(mallOrderEntity.getProductId());
	        	
            	Long courseId = getCourseId(mallOrderEntity.getOrderId(),courseName.trim());
	            if(courseId == null){
	            	sb.append("该订单不存在此课程；");
	            }else{
	            	courseAbnormalRegistration.setCourseId(courseId);
	            }
	            
	            //学员规划
	            String userPlanId = courseUserplanService.queryUserPlanId(mallOrderEntity.getOrderId().toString());
	            if(null != userPlanId){
	            }else{
	            	sb.append("该订单不存在学员规划；");
//	            	return "该订单不存在学员规划";
	            }
            }else{
            	sb.append("订单不存在；");
            }
           
            
           
            //报考时间段
            Map queryExamMap = new HashMap();
            queryExamMap.put("scheduleName",scheduleName);
//            queryExamMap.put("productId",mallOrderEntity.getProductId());暂时去掉产品线
            MallExamScheduleEntity examScheduleEntity = mallExamScheduleDao.queryPojoByParam(queryExamMap);
            if(null != examScheduleEntity){
            	courseAbnormalRegistration.setExamScheduleId(examScheduleEntity.getId().toString());
            }else{
            	sb.append("不存在考试阶段；");
//            	return "订单产品线与考试阶段不对应";
            }
            //报考省份验证
            Map queryAreaMap = new HashMap();
            queryAreaMap.put("examProvinceName",bkArea);
            Long bkAreaId = mallAreaDao.getAreaIdByExamProvinceName(queryAreaMap);
            if(null != bkAreaId){
            	courseAbnormalRegistration.setBkAreaId(bkAreaId);
            }else{
            	sb.append("不存在该省份；");
//            	return "不存在该省份";
            }
            if(StringUtils.isNotBlank(registrationNo)){
            	//报考登记号验证
            	if(verRegistrationEntity(courseAbnormalRegistration.getProductId(),registrationNo)){
            		sb.append("已存在报考登记号；");
//            		return "已存在报考登记号";
            	}else{
            		courseAbnormalRegistration.setRegistrationNumber(registrationNo);
            	}
            }
            if(sb.length() != 0){
                return sb.toString();
            }else{
	            courseAbnormalRegistration.setStatus(2);
	            courseAbnormalRegistration.setOrderId(mallOrderEntity.getOrderId().toString());
	            courseAbnormalRegistration.setCreater(userId);
	            courseAbnormalRegistration.setUpdatePerson(userId);
	            //加入去除前后空格操作
                BeanHelper.beanAttributeValueTrim(courseAbnormalRegistration);
                return this.saveEntity(courseAbnormalRegistration);
            }
        }catch(Exception e){
			logger.error("e.getMessageis{ }",e);
		    return e.getMessage();
        }
//		return null;
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
	
	/**
	 * 验证该登记号是否存在
	 * @param productId
	 * @param registrationNumber
	 * @return
	 */
	private Boolean verRegistrationEntity(Long productId, String registrationNumber) {
		// 报考登记号验证
		Map queryRegistrationMap = new HashMap();
		queryRegistrationMap.put("statusNo", 1);
		queryRegistrationMap.put("productId", productId);
		queryRegistrationMap.put("registrationNumber", registrationNumber);
		List<CourseAbnormalRegistrationEntity> registrationEntity = courseAbnormalRegistrationDao.queryRegistrationEntity(queryRegistrationMap);
		if (null != registrationEntity && registrationEntity.size()>0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int queryLayTotal(Map<String, Object> map) {
		return courseAbnormalRegistrationDao.queryLayTotal(map);
	}
	
	
	
}
