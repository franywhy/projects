package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.CourseUserplanClassDao;
import io.renren.entity.CourseClassplanEntity;
import io.renren.entity.CourseUserplanClassDetailEntity;
import io.renren.entity.CourseUserplanClassEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.pojo.CourseUserplanClassDetailPOJO;
import io.renren.pojo.CourseUserplanClassPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseClassplanService;
import io.renren.service.CourseUserplanClassDetailService;
import io.renren.service.CourseUserplanClassService;
import io.renren.service.CourseUserplanService;
import io.renren.utils.Constant;
import io.renren.utils.R;
import io.renren.utils.UserPlanClassDetailException;


//@Transactional(readOnly = true)
@Service("courseUserplanClassService")
public class CourseUserplanClassServiceImpl implements CourseUserplanClassService {
	@Autowired
	private CourseUserplanClassDao courseUserplanClassDao;
	@Autowired
	private CourseUserplanClassDetailService courseUserplanClassDetailService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private CourseClassplanService courseClassplanService;
	
	@Resource
	KGS studyplanKGS;
	private static final String HEAD = "XXJH_";
	
	
	public String getCode(){
		String userplanClassNo = HEAD + studyplanKGS.nextId();
		return userplanClassNo;
	}
	
	@Override
	public CourseUserplanClassEntity queryObject(Map<String, Object> map){
		return courseUserplanClassDao.queryObject(map);
	}
	
	@Override
	public List<CourseUserplanClassEntity> queryList(Map<String, Object> map){
		return courseUserplanClassDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseUserplanClassDao.queryTotal(map);
	}
	
	@Transactional
	@Override
	public void save(CourseUserplanClassPOJO courseUserplanClass) throws UserPlanClassDetailException{
		courseUserplanClass.setDr(0);
		courseUserplanClass.setStatus(0);
		courseUserplanClass.setCreateTime(new Date());
		courseUserplanClass.setModifyTime(courseUserplanClass.getCreateTime());
		//en
		CourseUserplanClassEntity en = CourseUserplanClassPOJO.getEntity(courseUserplanClass);
		// update 2017-10-12 默认审核通过
		en.setStatus(1);
		CourseUserplanEntity courseUserplanEntity = this.courseUserplanService.queryObject(en.getUserplanId());
		if(courseUserplanEntity != null){
			en.setDeptId(courseUserplanEntity.getDeptId());
		}
		//保存主表
		courseUserplanClassDao.save(en);
		
		//子表
		List<CourseUserplanClassDetailPOJO> detailList = courseUserplanClass.getDetailList();
		//子表保存
		if(null != detailList && detailList.size() > 0){
			for(int i=0;i<detailList.size();i++){
				//pojo
				CourseUserplanClassDetailPOJO cucdp = detailList.get(i);
				//entity
				CourseUserplanClassDetailEntity cucde = CourseUserplanClassDetailPOJO.getEntity(cucdp);
				//set 主表id
				cucde.setUserplanClassId(en.getUserplanClassId());
				//set 学员规划子表id
				cucde.setUserplanDetailId(cucdp.getUserplanDetailId());
				//set 排课计划id
				cucde.setClassplanId(cucdp.getClassplanId());
				//set 入课时间
				cucde.setTimestamp(cucdp.getTimestamp());
				//set 备注
				cucde.setRemark(cucdp.getRemark());
				//set dr
				cucde.setDr(0);
				//set schoolId
				cucde.setSchoolId(en.getSchoolId());
				//排序
				cucde.setOrderNum(i);
				
				//保存子表
				courseUserplanClassDetailService.save(cucde);
			}
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(CourseUserplanClassPOJO courseUserplanClass) throws UserPlanClassDetailException{
		//修改时间
		courseUserplanClass.setModifyTime(new Date());
		//en
		CourseUserplanClassEntity en = CourseUserplanClassPOJO.getEntity(courseUserplanClass);
		CourseUserplanEntity courseUserplanEntity = this.courseUserplanService.queryObject(en.getUserplanId());
		if(courseUserplanEntity != null){
			en.setDeptId(courseUserplanEntity.getDeptId());
		}
		//保存主表修改
		courseUserplanClassDao.update(en);
		//遍历子表
		List<CourseUserplanClassDetailPOJO> detailList = courseUserplanClass.getDetailList();
		
		List<Long> delIds = new ArrayList<Long>();
		if(null != detailList && detailList.size() > 0){
			for(int i=0;i<detailList.size();i++){
				//pojo
				CourseUserplanClassDetailPOJO cucdp = detailList.get(i);
				//entity
				CourseUserplanClassDetailEntity cucde = CourseUserplanClassDetailPOJO.getEntity(cucdp);
				//set 主表id
				cucde.setUserplanClassId(en.getUserplanClassId());
				//set 学员规划子表id
				cucde.setUserplanDetailId(cucdp.getUserplanDetailId());
				//set 排课计划id
				cucde.setClassplanId(cucdp.getClassplanId());
				//set 入课时间
				cucde.setTimestamp(cucdp.getTimestamp());
				//set 备注
				cucde.setRemark(cucdp.getRemark());
				//set dr
				cucde.setDr(0);
				//set schoolId
				cucde.setSchoolId(en.getSchoolId());
				//排序
				cucde.setOrderNum(i);
				if(null == cucde.getUserplanClassDetailId()){
					courseUserplanClassDetailService.save(cucde);
				}else{
					courseUserplanClassDetailService.update(cucde);
				}
				delIds.add(cucde.getUserplanClassDetailId());
			}
		}
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("userplanClassDetailIds", delIds);
		map.put("userplanClassId", en.getUserplanClassId());
		courseUserplanClassDetailService.deleteBatchNotIn(map);
	}
	
	@Override
	public void update(CourseUserplanClassEntity courseUserplanClassEntity) throws UserPlanClassDetailException{
		//保存主表修改
		courseUserplanClassDao.update(courseUserplanClassEntity);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(Map<String, Object> map){
		courseUserplanClassDao.delete(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseUserplanClassDetailService.deleteBatch(map);
		courseUserplanClassDao.deleteBatch(map);
	}

	/**
	 * 查询列表
	 */
	@Override
	public List<CourseUserplanClassPOJO> queryPojoList(Map<String, Object> map) {
		return this.courseUserplanClassDao.queryPojoList(map);
	}

	@Override
	public CourseUserplanClassPOJO queryPojoObject(Map<String, Object> map) {
		return this.courseUserplanClassDao.queryPojoObject(map);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] userplanClassIds) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", userplanClassIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
    	courseUserplanClassDao.updateBatch(map);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] userplanClassIds) {
		 Map<String, Object> map = new HashMap<String, Object>();
	     map.put("list", userplanClassIds);
	     map.put("status", Constant.Status.RESUME.getValue());
	     //map.put("modifiedTime", new Date());
	     courseUserplanClassDao.updateBatch(map);
	}

	/**
	 * 审核通过
	 * */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void accept(Long userplanClassId) {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("userplanClassId", userplanClassId);
	    map.put("status", Constant.Status.RESUME.getValue());
	    courseUserplanClassDao.audited(map);
	}
	
	/**
	 * 审核未过
	 * */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void reject(Long userplanClassId) {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("userplanClassId", userplanClassId);
	    map.put("status", Constant.Status.PAUSE.getValue());
	    courseUserplanClassDao.audited(map);
	}

	@Override
	public Integer queryUserClassPlanMid(String mId) {
		return courseUserplanClassDao.queryUserClassPlanMid(mId);
	}

    @Override
    public void updateChangeByOrderNo(Long userplanId) {
        courseUserplanClassDao.updateChangeByOrderNo(userplanId);
    }

    @Override
    public List<Long> queryCourseUserplanClass(Long userPlanId) {
        return courseUserplanClassDao.queryCourseUserplanClass(userPlanId);
    }

    @Override
	public synchronized String addBatchByClassplanId(String classplanId , String classId,String schoolId,Long userId) {
		StringBuffer sbf = new StringBuffer();
        String[] classIds = null;
        if (StringUtils.isNotBlank(classId)){
            classIds = classId.split(",");
        }
		if(StringUtils.isNotBlank(classplanId)){
//			this.courseClassplanService.queryObject1(map)
			//排课
			CourseClassplanEntity classplanEntity = this.courseClassplanService.queryObjectByClassplanId(classplanId);
			if(null != classplanEntity){
				//课程ID
				Long courseId = classplanEntity.getCourseId();
//				Map<String, Object> userplanMap = 
				List<Map<String, Object>> list = this.courseUserplanService.queryUserplanInfoNotClassplanByCourseId(courseId,classIds);
				if(null != list && !list.isEmpty()){
					sbf.append("本次处理单据数量:"+list.size());
					for(Map<String, Object> userplanMap : list){
						if(null != userplanMap){
							//学员规划子表ID
							Long userplanDetailId = (Long) userplanMap.get("userplanDetailId");
							//学员规划ID
							Long userplanId = (Long) userplanMap.get("userplanId");
							CourseUserplanClassPOJO courseUserplanClass = new CourseUserplanClassPOJO();
							courseUserplanClass.setUserplanId(userplanId);
							courseUserplanClass.setUserplanClassNo(getCode());
							courseUserplanClass.setExamScheduleId(null);
							courseUserplanClass.setRemark("");
							courseUserplanClass.setSchoolId(schoolId);
							courseUserplanClass.setCreateTime(new Date());
							courseUserplanClass.setModifyTime(new Date());
							courseUserplanClass.setCreatePerson(userId);
							courseUserplanClass.setModifyPerson(userId);
							courseUserplanClass.setDr(0);
							courseUserplanClass.setStatus(1);
							courseUserplanClass.setDeptId((Long)userplanMap.get("deptId"));
							List<CourseUserplanClassDetailPOJO> detailList = new ArrayList<>();
							CourseUserplanClassDetailPOJO classDetailPOJO = new CourseUserplanClassDetailPOJO();
							classDetailPOJO.setUserplanDetailId(userplanDetailId);
							classDetailPOJO.setClassplanId(classplanId);
							classDetailPOJO.setTimestamp(new Date());
							classDetailPOJO.setRemark("");
							classDetailPOJO.setDr(0);
							classDetailPOJO.setSchoolId(schoolId);
							classDetailPOJO.setOrderNum(1);
							courseUserplanClass.setDetailList(detailList);
							detailList.add(classDetailPOJO);
							try {
								this.save(courseUserplanClass);
							} catch (UserPlanClassDetailException e) {
								sbf.append("~学员规划ID:"+userplanId + "学员规划子表ID:"+userplanDetailId+"生成学习计划时异常!");
//								e.printStackTrace();
							}
						}
					}
				}else{
					sbf.append("该排课下的学员已经全部生成学习规划！");
				}
				
			}
//			courseUserplanClass.setUserplanId(userplanId);
		}else{
			sbf.append("参数异常!");
		}
		return sbf.toString();
	}

	@Override
	public synchronized String addBatchByClassplanIdAndClassId(Long examScheduleId, String classId, String classplanId, String schoolId, Long userId) {
		StringBuffer sbf = new StringBuffer();
        String[] classIds = null;
        if (StringUtils.isNotBlank(classId)){
            classIds = classId.split(",");
        }
		if(StringUtils.isNotBlank(classplanId)){
			//排课
			CourseClassplanEntity classplanEntity = this.courseClassplanService.queryObjectByClassplanId(classplanId);
			if(null != classplanEntity){
				//课程ID
				Long courseId = classplanEntity.getCourseId();
				List<Map<String, Object>> list = this.courseUserplanService.queryUserplanInfoNotClassplanByCourseIdAndClassId(courseId, classIds);
				if(null != list && !list.isEmpty()){
					sbf.append("本次处理单据数量:"+list.size()+"。");
					for(Map<String, Object> userplanMap : list){
						if(null != userplanMap){
							//学员规划子表ID
							Long userplanDetailId = (Long) userplanMap.get("userplanDetailId");
							//学员规划ID
							Long userplanId = (Long) userplanMap.get("userplanId");
							CourseUserplanClassPOJO courseUserplanClass = new CourseUserplanClassPOJO();
							courseUserplanClass.setUserplanId(userplanId);
							courseUserplanClass.setUserplanClassNo(getCode());
							courseUserplanClass.setExamScheduleId(examScheduleId);
							courseUserplanClass.setRemark("");
							courseUserplanClass.setSchoolId(schoolId);
							courseUserplanClass.setCreateTime(new Date());
							courseUserplanClass.setModifyTime(new Date());
							courseUserplanClass.setCreatePerson(userId);
							courseUserplanClass.setModifyPerson(userId);
							courseUserplanClass.setDr(0);
							courseUserplanClass.setStatus(1);
							courseUserplanClass.setDeptId((Long)userplanMap.get("deptId"));
							List<CourseUserplanClassDetailPOJO> detailList = new ArrayList<>();
							CourseUserplanClassDetailPOJO classDetailPOJO = new CourseUserplanClassDetailPOJO();
							classDetailPOJO.setUserplanDetailId(userplanDetailId);
							classDetailPOJO.setClassplanId(classplanId);
							classDetailPOJO.setTimestamp(new Date());
							classDetailPOJO.setRemark("");
							classDetailPOJO.setDr(0);
							classDetailPOJO.setSchoolId(schoolId);
							classDetailPOJO.setOrderNum(1);
							courseUserplanClass.setDetailList(detailList);
							detailList.add(classDetailPOJO);
							try {
								this.save(courseUserplanClass);
							} catch (UserPlanClassDetailException e) {
								sbf.append("~学员规划ID:"+userplanId + "学员规划子表ID:"+userplanDetailId+"生成学习计划时异常!");
//								e.printStackTrace();
							}
						}
					}
				}else{
					sbf.append("该排课下并且选中班级的学员已经全部生成学习规划！");
				}
				
			}
		}else{
			sbf.append("参数异常!");
		}
		return sbf.toString();
	}

	
	
}
