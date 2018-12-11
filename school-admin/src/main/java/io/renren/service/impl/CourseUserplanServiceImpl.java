package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.CourseUserplanDao;
import io.renren.dao.CourseUserplanDetailDao;
import io.renren.entity.CourseUserplanDetailEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.entity.MallGoodsDetailsEntity;
import io.renren.entity.MallOrderEntity;
import io.renren.pojo.CourseUserplanPOJO;
import io.renren.service.CourseUserplanService;
import io.renren.service.MallGoodsDetailsService;
import io.renren.service.MallOrderService;
import io.renren.utils.Constant;

@Service("courseUserplanService")
public class CourseUserplanServiceImpl implements CourseUserplanService {
	@Autowired
	private CourseUserplanDao courseUserplanDao;
	@Autowired
	private CourseUserplanDetailDao courseUserplanDetailDao;
	@Autowired
	private MallGoodsDetailsService mallGoodsDetailsService;
	@Autowired
	private MallOrderService mallOrderService;
	
	@Override
	public CourseUserplanEntity queryObject(Long userPlanId) {
		return courseUserplanDao.queryObject(userPlanId);
	}

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		return courseUserplanDao.queryListMap(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return courseUserplanDao.queryTotal(map);
	}

	/**
	 * 批量生成某一商品下的所有订单
	 * @param  commodityId 商品ID
	 * @return  操作信息
	 */
	public synchronized String saveUserplanBatch(Long commodityId,Long classId){
		StringBuffer sbf = new StringBuffer();
		if(null != commodityId){
			List<MallOrderEntity> orderList = this.mallOrderService.queryListByCommodityId(commodityId,classId);
			if(null != orderList && !orderList.isEmpty()){
				sbf.append("操作订单总数:"+orderList.size()+"。");
				for (MallOrderEntity mallOrderEntity : orderList) {
					boolean saveByOrderIdBoolean = this.saveByOrderId(mallOrderEntity.getOrderId());
					if(!saveByOrderIdBoolean){
						sbf.append("~订单号:"+mallOrderEntity.getOrderNo() + "操作失败!");
					}
				}
			}else{
				sbf.append("该商品下的订单已经全部生成学员规划！");
			}
		}else{
			sbf.append("参数错误！");
		}
		return sbf.toString();
	}
	
	/**
	 * 通过订单号生成学员规划
	 * @param orderId
	 */
	public synchronized boolean saveByOrderId(Long orderId){
		//订单ID不为空 && 订单ID对应的学员规划不存在
		if(null != orderId && queryUserplanByOrderId(orderId) == 0){
			Map<String , Object> queryOrderMap = new HashMap<>();
			queryOrderMap.put("orderId", orderId);//订单ID
			MallOrderEntity mallOrderEntity = this.mallOrderService.queryObject(queryOrderMap);
			if (null != mallOrderEntity && null != mallOrderEntity.getClassId() && null != mallOrderEntity.getDeptId()) {
				CourseUserplanEntity courseUserplan = new CourseUserplanEntity();
				courseUserplan.setOrderId(orderId);
				courseUserplan.setSchoolId(mallOrderEntity.getSchoolId());
				courseUserplan.setIsMatch(0);
				courseUserplan.setIsRep(0);
				courseUserplan.setClassId(mallOrderEntity.getClassId());
				courseUserplan.setDeptId(mallOrderEntity.getDeptId());
				this.save(courseUserplan);
				return true;
			}
		}
		return false;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(CourseUserplanEntity courseUserplan) {
		//判断nc同步订单后是否同步生成学员规划开关
//		if (nc_Order_courseUserplan) {
			// 订单查询条件
			Map<String, Object> queryOrder = new HashMap<>();
			queryOrder.put("orderId", courseUserplan.getOrderId());
//			queryOrder.put("schoolId", courseUserplan.getSchoolId());
			// 查询订单
			MallOrderEntity order = mallOrderService.queryOrderForUserplan(queryOrder);
			
			List<Map<String, Object>> courseDetail = null;
			// 存在可以生成学员规划的订单
			if (order != null) {
				//产品线ID
				courseUserplan.setProductId(order.getProductId());
				// 订单ID
				courseUserplan.setOrderId(order.getOrderId());
				// 订单号
				courseUserplan.setOrderNo(order.getOrderNo());
				// 省份
				courseUserplan.setAreaId(order.getAreaId());
				// 班级
				// courseUserplan.setClassId(order.getClassId());
				courseUserplan.setClassId(courseUserplan.getClassId());
				// 班型
				courseUserplan.setClassTypeId(order.getClassTypeId());
				// 商品ID
				courseUserplan.setCommodityId(order.getCommodityId());
				// 创建时间
				courseUserplan.setCreationTime(new Date());
				// dr
				courseUserplan.setDr(Constant.DR.NORMAL.getValue());
				// 最近考试时段
				courseUserplan.setExamScheduleId(null);
				// 毕业时间
				courseUserplan.setGraduateTime(null);
				// 状态
				courseUserplan.setStatus((int) Constant.Status.RESUME.getValue());
				// 学员ID
				courseUserplan.setUserId(order.getUserId());
				// 排课状态
				courseUserplan.setUserplanStatus(Constant.UserplanStatus.NORMAL.getValue());
				// 层次PK
				courseUserplan.setLevelId(order.getLevelId());
				// 专业
				courseUserplan.setProfessionId(order.getProfessionId());
				// 部门
				courseUserplan.setDeptId(order.getDeptId());
				// 保存主表
				courseUserplanDao.save(courseUserplan);
				// 子表查询条件
				Map<String, Object> queryCourseMap = new HashMap<>();
				// 商品ID
				queryCourseMap.put("commodityId", courseUserplan.getCommodityId());
				// 地区ID
				queryCourseMap.put("areaId", courseUserplan.getAreaId());
				// 平台ID
				queryCourseMap.put("schoolId", courseUserplan.getSchoolId());
				// 课程列表
				courseDetail = this.courseUserplanDetailDao.queryAllcourseListByCommodityId(queryCourseMap);
				if (null != courseDetail && !courseDetail.isEmpty()) {
					for (Map<String, Object> map : courseDetail) {
						// 子表
						CourseUserplanDetailEntity de = new CourseUserplanDetailEntity();
						// 课程ID
						de.setCourseId((Long) map.get("courseId"));
						// 考试通过状态
						de.setIsPass(Constant.PassStatus.UNPASS.getValue());
						// 代替课程
						de.setIsSubstitute((Integer) map.get("isSubstitute"));
						// 被替代课程
						de.setIsSubstituted((Integer) map.get("isSubstituted"));
						// 专业不对口课程
						de.setIsSuitable((Integer) map.get("isSuitable"));
						// 是否统考
						de.setIsUnitedExam((Integer) map.get("isUnitedExam"));
						// 学员规划PK
						de.setUserplanId(courseUserplan.getUserPlanId());
						// 省份ID
						de.setAreaId(courseUserplan.getAreaId());
						// shoolId
						de.setSchoolId(courseUserplan.getSchoolId());
						// 保存
						this.courseUserplanDetailDao.save(de);
					}
				}
			}
//		}
	}

	@Override
	public void update(CourseUserplanEntity courseUserplan) {
		courseUserplanDao.update(courseUserplan);
	}

	@Override
	public void delete(Map<String, Object> map) {
		courseUserplanDao.delete(map);
	}

	@Override
	public void deleteBatch(Map<String, Object> map) {
		courseUserplanDao.deleteBatch(map);
	}

	@Override
	public void pause(Long[] userPlanIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", userPlanIds);
		map.put("status", Constant.Status.PAUSE.getValue());
		courseUserplanDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] userPlanIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", userPlanIds);
		map.put("status", Constant.Status.RESUME.getValue());
		courseUserplanDao.updateBatch(map);
	}

	@Override
	public Map<String, Object> queryObjectMap(Map<String, Object> map) {
		return this.courseUserplanDao.queryObjectMap(map);
	}

	@Override
	public void updateMap(CourseUserplanEntity entity) {
		Map<String, Object> map = new HashMap<>();
		map.put("userPlanId", entity.getUserPlanId());
		// 班级
		map.put("classId", entity.getClassId());
		// 修改学习代替课程
		map.put("isRep", entity.getIsRep());
		// 修改专业对口
		map.put("isMatch", entity.getIsMatch());
		map.put("schoolId", entity.getSchoolId());
		map.put("modifyPerson", entity.getModifyPerson());
		map.put("modifiedTime", new Date());
		this.courseUserplanDao.updateCommon(map);
	}

	/**
	 * 转省转专业 1.原有课程匹配的保留;不匹配的删除 2.新课程不重复的新增
	 */
	// TODO 转省转专业日志记录
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public boolean updateChange(Map<String, Object> map) {
		// 参数
		Long userPlanId = (Long) map.get("userPlanId");
		Long orderId = (Long) map.get("orderId");
		Long areaId = (Long) map.get("areaId");
		Long commodityId = (Long) map.get("commodityId");
		Long classId = (Long) map.get("classId");
		Long modifyPerson = (Long) map.get("modifyPerson");
		Integer isRep = (Integer) map.get("isRep");
		Integer isMatch = (Integer) map.get("isMatch");
		String schoolId = (String) map.get("schoolId");
		// 原学员规划
		CourseUserplanEntity oldUserPlan = this.queryObject(userPlanId);
		// 参数校验
		if (null != oldUserPlan && null != orderId && null != areaId && null != commodityId && null != classId
				&& null != isRep && null != isMatch) {
			// 修改的学员规划信息
			CourseUserplanEntity updateUserPlan = new CourseUserplanEntity();
			updateUserPlan.setUserPlanId(oldUserPlan.getUserPlanId());
			if (orderId != updateUserPlan.getOrderId()) {
				Map<String, Object> orderMao = new HashMap<>();
				orderMao.put("orderId", orderId);
				orderMao.put("schoolId", schoolId);
				MallOrderEntity oldOrder = mallOrderService.queryObject(orderMao);
				if (null != oldOrder) {
					// 订单ID
					updateUserPlan.setOrderId(oldOrder.getOrderId());
					// 订单号
					updateUserPlan.setOrderNo(oldOrder.getOrderNo());
					// 班型
					updateUserPlan.setClassTypeId(oldOrder.getClassTypeId());
					// 层次PK
					updateUserPlan.setLevelId(oldOrder.getLevelId());
					// 专业
					updateUserPlan.setProfessionId(oldOrder.getProfessionId());
				}
				// 省份
				updateUserPlan.setAreaId(areaId);
				// 商品ID
				updateUserPlan.setCommodityId(commodityId);
				// 班级ID
				updateUserPlan.setClassId(classId);
				// 学习替代课程
				updateUserPlan.setIsRep(isRep);
				// 专业对口
				updateUserPlan.setIsMatch(isMatch);

				// 修改信息
				updateUserPlan.setModifyPerson(modifyPerson);
				updateUserPlan.setModifiedTime(new Date());
				// 保存
				this.courseUserplanDao.update(updateUserPlan);
				// 商品ID或者省份ID有变动 修改子表课程
				if (commodityId != oldUserPlan.getCommodityId() || areaId != oldUserPlan.getAreaId()) {
					// 根据商品ID和省份ID 查询课程
					Map<String, Object> goodDetailMap = new HashMap<>();
					goodDetailMap.put("mallGoodsId", commodityId);
					goodDetailMap.put("schoolId", oldOrder.getSchoolId());
					List<MallGoodsDetailsEntity> goodDetailList = mallGoodsDetailsService.queryList(goodDetailMap);

					// 根据排课主键查询课程列表
					Map<String, Object> userplanMap = new HashMap<>();
					userplanMap.put("userPlanId", oldUserPlan.getUserPlanId());
					userplanMap.put("schoolId", oldOrder.getSchoolId());
					List<CourseUserplanDetailEntity> oldUserPlandDetailList = courseUserplanDetailDao
							.courseUserPlanEntityLisrt(userplanMap);

					// 转省转专业后 原课程不符合条件的
					List<Long> delUserPlanDetailIdList = new ArrayList<>();
					// 转省转专业后 原课程还需要学习的
					List<Long> oldUserPlanDetailIdList = new ArrayList<>();
					// 转省转专业后 需要新增加的课程
					List<MallGoodsDetailsEntity> newGoodsDetailIdList = new ArrayList<>();
					// 原课程 区分出来需要保留的和 删除的
					if (null != oldUserPlandDetailList && oldUserPlandDetailList.size() > 0) {
						for (CourseUserplanDetailEntity oldUserPlandDetail : oldUserPlandDetailList) {
							Long oldAreaId = oldUserPlandDetail.getAreaId();
							Long oldCourseId = oldUserPlandDetail.getCourseId();
							Long oldUserPlanDetailId = oldUserPlandDetail.getUserplanDetailId();
							// 旧课程可以保留标志
							boolean tag = false;
							// 判断旧课程是否可保留
							for (MallGoodsDetailsEntity newCourse : goodDetailList) {
								if (oldCourseId == newCourse.getCourseId() && oldAreaId == newCourse.getMallAreaId()) {
									tag = true;
									break;
								}
							}
							if (tag) {
								oldUserPlanDetailIdList.add(oldUserPlanDetailId);
							} else {
								delUserPlanDetailIdList.add(oldUserPlanDetailId);
							}
						}
					}
					// 新增原来没有的课程
					if (null != goodDetailList && !goodDetailList.isEmpty()) {
						for (MallGoodsDetailsEntity newCourse : goodDetailList) {
							Long newAreaId = newCourse.getMallAreaId();
							Long newCourseId = newCourse.getCourseId();
							// 新添加课程的标志
							boolean tag = false;
							for (CourseUserplanDetailEntity oldUserPlandDetail : oldUserPlandDetailList) {
								if (newCourseId == oldUserPlandDetail.getCourseId()
										&& newAreaId == oldUserPlandDetail.getAreaId()) {
									tag = true;
									break;
								}
							}
							if (tag) {
								newGoodsDetailIdList.add(newCourse);
							}

						}
					}

					// TODO 报考登记
					// 删除原来不匹配的课程
					if (!delUserPlanDetailIdList.isEmpty()) {
						Map<String, Object> deleteUserPlanDetailMap = new HashMap<>();
						deleteUserPlanDetailMap.put("userplanDetailId", delUserPlanDetailIdList);
						deleteUserPlanDetailMap.put("schoolId", oldOrder.getSchoolId());
						this.courseUserplanDetailDao.deleteBatch(deleteUserPlanDetailMap);
					}

					// 新增原来没有的课程
					if (!newGoodsDetailIdList.isEmpty()) {
						for (MallGoodsDetailsEntity newCourse : newGoodsDetailIdList) {
							// 子表
							CourseUserplanDetailEntity de = new CourseUserplanDetailEntity();
							// 课程ID
							de.setCourseId(newCourse.getCourseId());
							// 考试通过状态
							de.setIsPass(Constant.PassStatus.UNPASS.getValue());
							// 代替课程
							de.setIsSubstitute(newCourse.getIsSubstitute());
							// 被替代课程
							de.setIsSubstituted(newCourse.getIsSubstituted());
							// 专业不对口课程
							de.setIsSuitable(newCourse.getIsSuitable());
							// 是否统考
							de.setIsUnitedExam(newCourse.getIsUnitedExam());
							// 学员规划PK
							de.setUserplanId(oldUserPlan.getUserPlanId());
							// 省份ID
							de.setAreaId(areaId);
							// shoolId
							de.setSchoolId(oldUserPlan.getSchoolId());
							// 保存
							this.courseUserplanDetailDao.save(de);
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> queryOrderForChangeUserplan(Map<String, Object> map) {
		CourseUserplanEntity courseUserplanEntity = this.courseUserplanDao.queryObject(map.get("userPlanId"));
		if (courseUserplanEntity != null) {
			map.put("orderId", courseUserplanEntity.getOrderId());
			map.put("userId", courseUserplanEntity.getUserId());
			return this.mallOrderService.queryOrderForChangeUserplan(map);
		}
		return null;
	}

	/**
	 * 弹框列表
	 */
	@Override
	public List<CourseUserplanPOJO> queryList2(Map<String, Object> map) {
		return this.courseUserplanDao.queryList2(map);
	}

	@Override
	public List<Long> getUsers(String classplanId, String classtypeIds[], String schoolId) {
		return this.courseUserplanDao.getUsers(classplanId, classtypeIds, schoolId);
	}

	@Override
	public int checkClassType(long id) {
		return this.courseUserplanDao.checkClassType(id);
	}

	@Override
	public int checkProfession(long id) {
		return this.courseUserplanDao.checkProfession(id);
	}

	@Override
	public void updateByOrderId(CourseUserplanEntity courseUserplan) {
		this.courseUserplanDao.updateByOrderId(courseUserplan);
	}

	@Override
	public String queryUserPlanId(String orderId) {
		return this.courseUserplanDao.queryUserPlanId(orderId);
	}

	@Override
	public List<Long> getAllUsers(String schoolId) {
		return this.courseUserplanDao.getAllUsers(schoolId);
	}
	/**
	 * 弹框总数
	 */
	@Override
	public int queryTotal2(Map<String, Object> map) {
		return this.courseUserplanDao.queryTotal2(map);
	}

	@Override
	public void updateChange1(Map<String, Object> map) {
		Long userPlanId = (Long) map.get("userPlanId");
		Long commodityId = (Long) map.get("commodityId");
		Long areaId = (Long) map.get("areaId");
		Long classTypeId = (Long) map.get("classTypeId");
		Long levelId = (Long) map.get("levelId");
		Long professionId = (Long) map.get("professionId");
		Long modifyPerson = (Long) map.get("modifyPerson");
		
		CourseUserplanEntity courseUserplan = courseUserplanDao.queryObject(userPlanId);
		
		courseUserplan.setUserPlanId(userPlanId);
		courseUserplan.setCommodityId(commodityId);
		courseUserplan.setAreaId(areaId);
		courseUserplan.setClassTypeId(classTypeId);
		courseUserplan.setLevelId(levelId);
		courseUserplan.setProfessionId(professionId);
		courseUserplan.setModifyPerson(modifyPerson);
		
		this.courseUserplanDao.update(courseUserplan);
	}

	@Override
	public int queryUserplanByOrderId(Long orderId) {
		return this.courseUserplanDao.queryUserplanByOrderId(orderId);
	}

	@Override
	public void updateUserplanByOrderId(CourseUserplanEntity courseUserplan) {
		this.courseUserplanDao.updateUserplanByOrderId(courseUserplan);
	}

	@Override
	public CourseUserplanEntity queryUserplanObjectByOrderId(Long orderId) {
		return this.courseUserplanDao.queryUserplanObjectByOrderId(orderId);
	}

	@Override
	public void deleteByOrderId(Long orderId) {
		this.courseUserplanDao.deleteByOrderId(orderId);
	}

	/**
	 * 查询 新的学员规划（会计产品线）
	 * @param ts
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryKJClassMessage(String ts) {
		return this.courseUserplanDao.queryKJClassMessage(ts);
	}
	/**
	 * 查询 新的学员规划（学来学往产品线）
	 * @param ts
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryXLXWClassMessage(String ts) {
		return this.courseUserplanDao.queryXLXWClassMessage(ts);
	}
	@Override
	public List<Map<String,Object>> queryUserplanInfoNotClassplanByCourseId(Long courseId, String[] classIds) {
		return this.courseUserplanDao.queryUserplanInfoNotClassplanByCourseId(courseId,classIds);
	}
	@Override
	public List<Map<String, Object>> queryUserplanInfoNotClassplanByCourseIdAndClassId(Long courseId, String[] classIds) {
		return this.courseUserplanDao.queryUserplanInfoNotClassplanByCourseIdAndClassId(courseId, classIds);
	}

	@Override
	public List<String> queryCodeListByCommodityId(Object object) {
		return this.courseUserplanDao.queryCodeListByCommodityId(object);
	}

    @Override
    public void updateChangeByOrderNo(Long orderId) {
        courseUserplanDao.deleteByOrderId(orderId);
    }

    @Override
    public List<Long> queryComnodityListByTkCode(String tkCode) {
        return courseUserplanDao.queryComnodityListByTkCode(tkCode);
    }

    @Override
    public List<Map<String, Object>> queryKJClassMessage(Map<String, Object> mapQuery) {
        return courseUserplanDao.queryKJClassMessageByGoods(mapQuery);
    }

    @Override
    public List<Map<String, Object>> queryUserplanDetailIdByOrderId(Long orderId, Long courseId) {
        return courseUserplanDao.queryUserplanDetailIdByOrderId(orderId,courseId);
    }


}
