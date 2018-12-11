package io.renren.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import io.renren.entity.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.SyncDao;
import io.renren.mongo.entity.AreaEntity;
import io.renren.mongo.sync.dao.IClassplanDao;
import io.renren.mongo.sync.dao.ICourseTimesDao;
import io.renren.mongo.sync.entity.ClassplanEntity;
import io.renren.mongo.sync.entity.ClassplanLiveEntity;
import io.renren.mongo.sync.entity.CommodityEntity;
import io.renren.mongo.sync.entity.CourseTimeEntity;
import io.renren.mongo.sync.entity.CourseUserPlanClassEntity;
import io.renren.mongo.sync.entity.LearningRecords;
import io.renren.mongo.sync.entity.LiveRoomEntity;
import io.renren.mongo.sync.entity.PeriodEntity;
import io.renren.mongo.sync.entity.QuserEntity;
import io.renren.mongo.sync.entity.SyncUsersEntity;
import io.renren.mongo.sync.entity.TeacherEntity;
import io.renren.mongo.sync.entity.UsersEntity;
import io.renren.pojo.MallGoodsInfoPOJO;
import io.renren.pojo.SyncCustomerPOJO;
import io.renren.pojo.classplan.ClassplanPOJO;
import io.renren.pojo.liveRoom.LiveRoomPOJO;
import io.renren.pojo.order.OrderPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.CourseClassplanService;
import io.renren.service.CourseUserplanClassService;
import io.renren.service.CourseUserplanDetailService;
import io.renren.service.CourseUserplanService;
import io.renren.service.CoursesService;
import io.renren.service.LiveLogDetailService;
import io.renren.service.LiveLogService;
import io.renren.service.LogGenseeWatchService;
import io.renren.service.LogWatchService;
import io.renren.service.MallAreaService;
import io.renren.service.MallClassTypeService;
import io.renren.service.MallGoodsDetailsService;
import io.renren.service.MallGoodsInfoService;
import io.renren.service.MallLiveRoomService;
import io.renren.service.MallOrderService;
import io.renren.service.MallProfessionService;
import io.renren.service.SyncService;
import io.renren.service.SysUserService;
import io.renren.service.UsersService;
import io.renren.service.VideoLogDetailService;
import io.renren.service.VideoLogService;
import io.renren.utils.BackUtils;
import io.renren.utils.ClassTypeUtils;
import io.renren.utils.DateUtils;
import io.renren.utils.SendUdeskUtil;
import io.renren.utils.SpringContextUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//@Transactional(readOnly = true)
@Service("syncService")
public class SyncServiceImpl implements SyncService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MallGoodsDetailsService mallGoodsDetailsService;
	@Autowired
	private CoursesService coursesService;
	@Autowired
	private CourseUserplanDetailService courseUserplanDetail;
//	@Autowired
	private ICourseTimesDao zkCourseTimesDao;
	@Autowired
	private MallProfessionService mallProfessionService;
	@Autowired
	private LogWatchService logWatchService;
//	@Autowired
	private IClassplanDao classplanDao;
	@Autowired
	private MallClassTypeService classTypeService;
	@Autowired
	private MallGoodsInfoService mallGoodsInfoService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private LiveLogService liveLogService;
	@Autowired
	private VideoLogService videoLogService;
//	@Autowired
	private SyncDao syncDao;
	@Autowired
	private MallClassTypeService mallClassTypeService;
	@Autowired
	private MallAreaService mallAreaService;
	@Autowired
	private MallLiveRoomService mallLiveRoomService;
	@Autowired
	private CourseClassplanService courseClassplanService;
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;
	@Autowired
	private MallOrderService mallOrderService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private CourseUserplanClassService courseUserplanClassService;
	@Autowired
	private LiveLogDetailService liveLogDetailService;
	@Autowired
	private VideoLogDetailService videoLogDetailService;
	@Autowired
	private LogGenseeWatchService logGenseeWatchService;
	/** 默认密码 hqzk123456 */
	private static String PSW = "1c3f360330c442c3cc62d1608fe7a3a3";
	// 2小时毫秒数
	private static long MIAOSHU = 7200L;

	@Value("#{application['user.psw']}")
	private void setPSW(String str) {
		PSW = str;
	}

	/** 班型数组 */
	private static String classTypeIds = null;
	@Resource
	KGS studyplanKGS;
	private static final String HEAD = "XXJH";
	// String userplanClassNo = HEAD + studyplanKGS.nextId();
	// 事务
	// 后台默认密码
	private static String def_psw = "123456";
	// private static String def_psw =
	// "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
	// private static String def_psw = new Sha256Hash("123456").toHex();
	private static Map<String, String> commondity_map = new HashMap<>();
	static {
		commondity_map.put("546", "1001A5100000002FE23P");
		commondity_map.put("166", "1001A510000000270YDO");
		commondity_map.put("167", "1001A510000000270YEH");
		commondity_map.put("169", "1001A510000000270YGS");
		commondity_map.put("343", "1001A510000000270YIX");
		commondity_map.put("326", "1001A510000000270YOK");
		commondity_map.put("558", "1001A5100000002FE2H1");
		commondity_map.put("556", "1001A5100000002FE843");
		commondity_map.put("176", "1001A510000000270YE3");
		commondity_map.put("168", "1001A510000000270YFL");
		commondity_map.put("336", "1001A510000000270YRK");
		commondity_map.put("327", "1001A510000000270YSS");
		commondity_map.put("337", "1001A510000000270YTE");
		commondity_map.put("338", "1001A510000000270YU1");
		commondity_map.put("329", "1001A510000000270YU2");
		commondity_map.put("387", "1001A510000000270YV4");
		commondity_map.put("356", "1001A510000000271DOA");
		commondity_map.put("347", "1001A510000000271DOH");
		commondity_map.put("348", "1001A510000000271DR0");
		commondity_map.put("358", "1001A510000000271DSA");
		commondity_map.put("349", "1001A510000000271DST");
		commondity_map.put("359", "1001A510000000271DTL");
		commondity_map.put("388", "1001A510000000271DTW");
		commondity_map.put("366", "1001A510000000271DUZ");
		commondity_map.put("376", "1001A510000000271DVA");
		commondity_map.put("367", "1001A510000000271DVL");
		commondity_map.put("377", "1001A510000000271DVU");
		commondity_map.put("368", "1001A510000000271DVV");
		commondity_map.put("378", "1001A510000000271DVW");
		commondity_map.put("369", "1001A510000000271DW1");
		commondity_map.put("379", "1001A510000000271DW3");
		commondity_map.put("341", "1001A510000000270YEL");
		commondity_map.put("342", "1001A510000000270YFN");
		commondity_map.put("386", "1001A510000000270YJG");
		commondity_map.put("328", "1001A510000000270YTX");
		commondity_map.put("559", "1001A5100000002FE28V");
		commondity_map.put("547", "1001A5100000002FE2F7");
		commondity_map.put("548", "1001A5100000002FE7XK");
		commondity_map.put("557", "1001A5100000002FE7Y4");
		commondity_map.put("389", "1001A510000000271DW6");
		commondity_map.put("339", "1001A510000000270YUD");
		commondity_map.put("346", "1001A510000000270YYJ");
		commondity_map.put("357", "1001A510000000271DPD");
		commondity_map.put("549", "1001A5100000002FE82C");

		commondity_map.put("604", "1001A5100000002MA3Y3");
		commondity_map.put("601", "1001A5100000002MA3X7");
		commondity_map.put("602", "1001A5100000002MA3Y5");
		commondity_map.put("603", "1001A5100000002MA3YL");
	}

	// 事务
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void syncSaveCourse(CoursesEntity coursesEntity, List<CourseLiveDetailsEntity> courseLiveDetailsEntitys) {
		if (null != coursesEntity) {
			Map<String, Object> map = new HashMap<>();
			// map.put("schoolId", coursesEntity.getSchoolId());
			map.put("mId", coursesEntity.getmId());
			// 按照mongodbID查询课程是否已经同步过
			int syncQueryCourseByMid = syncDao.syncQueryCourseByMid(map);
			// 课程未同步则重新同步
			if (0 == syncQueryCourseByMid) {
				// 保存课程
				syncDao.syncCourseSave(coursesEntity);
				// 保存基础课时
				if (null != courseLiveDetailsEntitys && courseLiveDetailsEntitys.size() > 0) {
					for (CourseLiveDetailsEntity courseLiveDetailsEntity2 : courseLiveDetailsEntitys) {
						// 主表主键
						courseLiveDetailsEntity2.setCourseId(coursesEntity.getCourseId());
						// 保存基础课时
						syncDao.syncCourseLiveDetailSave(courseLiveDetailsEntity2);
					}
				}
			}
		}
	}

	@Override
	public void syncCourse(String schoolId) {
		List<CourseTimeEntity> findCourseTime = zkCourseTimesDao.findCourseTime();
		if (null != findCourseTime && findCourseTime.size() > 0) {
			for (CourseTimeEntity courseTimeEntity : findCourseTime) {
				// 构造课程对象
				CoursesEntity coursesEntity = new CoursesEntity();
				String oldname = courseTimeEntity.getName();
				String code = "";
				String name = oldname;
				// 处理课程名称和课程编号
				// "1001 语文" => code = "1001" name= "语文"
				// "数学" => code = "" name= "数学"
				if (oldname.indexOf(" ") > 0) {
					String[] nameArray = oldname.split(" ");
					code = nameArray[0];
					// name = nameArray[1];
				}
				// 课程名称
				coursesEntity.setCourseName(name);
				// 课程编号
				coursesEntity.setCourseNo(code);
				// 未删除
				coursesEntity.setDr(0);
				coursesEntity.setCourseLb(courseTimeEntity.getCourse_prop());
				coursesEntity.setCourseType(courseTimeEntity.getCourse_prop());
				coursesEntity.setExamType(courseTimeEntity.getExam_type());
				// 可以冲突
				coursesEntity.setCourseEq(0);
				// 试听地址
				coursesEntity.setListenUrl(courseTimeEntity.getUrl());
				// 校区id
				coursesEntity.setSchoolId(schoolId);
				// 主键
				coursesEntity.setmId(courseTimeEntity.get_id());

				// 构造课时对象
				List<CourseLiveDetailsEntity> courseLiveDetailsEntitys = null;
				// 查询基础课时信息
				List<String> period_list = courseTimeEntity.getPeriod_list();
				if (null != period_list && period_list.size() > 0) {
					courseLiveDetailsEntitys = new ArrayList<>();
					int i = 0;
					for (String periodId : period_list) {
						// 查询mongodb 的课时详情
						PeriodEntity findPeriodByPeriod = zkCourseTimesDao.findPeriodByPeriodId(periodId);
						if (null != findPeriodByPeriod) {
							// 创建mysql的课时对象
							CourseLiveDetailsEntity courseLiveDetailsEntity = new CourseLiveDetailsEntity();
							// 课时名称
							courseLiveDetailsEntity.setLiveName(findPeriodByPeriod.getName());
							// 班型
							courseLiveDetailsEntity.setLiveClassTypeIds(getClassTypeIds(schoolId));
							// 平台ID
							courseLiveDetailsEntity.setSchoolId(schoolId);
							// 排序
							courseLiveDetailsEntity.setOrderNum(i++);

							courseLiveDetailsEntity.setmId(findPeriodByPeriod.get_id());
							courseLiveDetailsEntitys.add(courseLiveDetailsEntity);
						}
					}
				}
				// 保存
				syncSaveCourse(coursesEntity, courseLiveDetailsEntitys);
			}
		}
	}

	/**
	 * 获取全部班型
	 * 
	 * @param schoolId
	 *            平台ID
	 * @return 班型入库格式
	 */
	private String getClassTypeIds(String schoolId) {
		if (StringUtils.isBlank(classTypeIds)) {
			Map<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("schoolId", schoolId);
			hashMap.put("status", 1);
			// 查询班型列表
			List<MallClassTypeEntity> queryList = classTypeService.queryList(hashMap);
			if (null != queryList && queryList.size() > 0) {
				List<Long> classTypeList = new ArrayList<Long>();
				// 班型列表id存入数组
				for (MallClassTypeEntity mallClassTypeEntity : queryList) {
					classTypeList.add(mallClassTypeEntity.getClasstypeId());
				}
				// 调用班型工具类组装入库格式
				classTypeIds = ClassTypeUtils.inArr(classTypeList);
			}
		}
		return classTypeIds;
	}

	@Override
	public void syncCommodity(String schoolId) {
		logger.info("sync_commodity strat:{}", new Date());
		try {
			List<CommodityEntity> findCommodityList = zkCourseTimesDao.findCommodity();
			MallGoodsInfoPOJO goodsInfo = new MallGoodsInfoPOJO();
			for (int j = 0; j < findCommodityList.size(); j++) {
				CommodityEntity comm = findCommodityList.get(j);
				// System.out.println("sync_commodity:"+comm);
				logger.info("sync_commodity:{}", comm.toString());
				System.out.println("sync_commodity:{}" + comm.toString());
				// 判断商品是否存在
				Map goodsMap = new HashMap();
				goodsMap.put("id", comm.get_id());
				MallGoodsInfoEntity info = mallGoodsInfoService.queryGoodsInfoId(goodsMap);
				if (info != null) {
					logger.info("sync_commodity_exite:{}", comm.toString());
					System.out.println("sync_commodity_exite:{}" + comm.toString());
					continue;
				}
				if (findCommodityList.size() > 0 && findCommodityList != null) {
					// 商品ID
					goodsInfo.setId((long) comm.get_id());
					// 商品名称
					goodsInfo.setName(comm.getName());
					// 判断班型是否存在，如果存在就插入商品信息，否则就不插入商品信息
					MallClassTypeEntity classType = mallClassTypeService.queryClassId(comm.getPage_name());
					if (classType == null) {
						logger.info("sync_commodity classType is null:{}", comm.toString());
//						System.out.println("sync_commodity classType is null:{}" + comm.toString());
						continue;
					}
					// 班型
					goodsInfo.setClassTypeId(classType.getClasstypeId());
					// 小图
					goodsInfo.setThumbPath(comm.getThumbnail());
					// 大图
					goodsInfo.setOriginPath(comm.getPhoto());
					// 售价
					goodsInfo.setPresentPrice((int) comm.getPrice());
					// 原价
					goodsInfo.setOriginalPrice((int) comm.getOld_price());
					// 适用对象
					goodsInfo.setSuitableUser(comm.getReader());
					// 学习周期
					goodsInfo.setLearningTime(comm.getLearntime());
					// 上课方式
					goodsInfo.setPattern(comm.getClassmode());
					// 上架状态0：下架 1：上架
					goodsInfo.setStatus(0);
					// 判断nongoDB表中type是否存在MySQL表专业表（mall_profession）中m_id，如果存在就插入，否则。。。
					Map map = new HashMap<>();
					map.put("type", comm.getType());
					MallProfessionEntity profession = mallProfessionService.queryMID(map);
					if (profession == null) {
						logger.info("sync_commodity profession is null:{}", comm.toString());
//						System.out.println("sync_commodity profession is null:{}" + comm.toString());
						continue;
					}
					// 专业
					goodsInfo.setProfessionId(profession.getProfessionId());
					// 层次
					goodsInfo.setLevelId((long) comm.getLevel_type() + 1);
					// 审核状态0：不通过 1：通过
					goodsInfo.setIsAudited(0);
					// 创建时间
					goodsInfo.setCreateTime(new Date());
					// 业务分类
					goodsInfo.setSchoolId(schoolId);
					// dr :0为未删除，1为软删除
					goodsInfo.setDr(comm.getDr());
					// nc_id
					goodsInfo.setNcId(comm.getNc_id());
					// m_id
					goodsInfo.setmId(comm.get_id());

					goodsInfo.setNcId(commondity_map.get("" + comm.get_id()));
					mallGoodsInfoService.save(goodsInfo);
					for (int i = 0; i < comm.getCourse_time_list().size(); i++) {
						// 保存商品详细表子表
						MallGoodsDetailsEntity mgde = new MallGoodsDetailsEntity();
						Map courseMap = new HashMap<>();
						courseMap.put("mId", comm.getCourse_time_list().get(i));
						// 根据mongo表查找Course_time_list值来对应MySQL中course表中m_id，如果能匹配就插入省份areaId
						CoursesEntity courser = coursesService.queryMid(courseMap);
						if (courser != null) {
							// 根据固定省份（新疆，广东省，河南省，云南省，四川省，重庆，甘肃省 ）查找AreaId
							// 根据固定省份（新疆，广东省，云南省，四川省，重庆，甘肃省 , 湖北省）查找AreaId
							AreaEntity area = new AreaEntity();
							List<String> areaId = mallAreaService.queryAreaIdList(area);
							for (int k = 0; k < areaId.size(); k++) {
								// set 省份id
								mgde.setMallAreaId(Long.valueOf(areaId.get(k)));
								// set 课程id
								mgde.setCourseId(courser.getCourseId());
								// set 主表id
								mgde.setMallGoodsId((long) comm.get_id());
								// set 被替代课程
								mgde.setIsSubstituted(0);
								// set 代替课程
								mgde.setIsSubstitute(0);
								// set 是否统考
								mgde.setIsUnitedExam(1);
								// set 专业不对口课程
								mgde.setIsSuitable(0);
								// set 平台ID
								mgde.setSchoolId(schoolId);
								// 排序
								mgde.setOrderNum(i);
								// 保存子表
								mallGoodsDetailsService.save(mgde);
							}
						} /*
							 * else{ goodsDetails.setCourseId(null); }
							 */

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 同步排课
	 */
	@Override
	public void syncClassplan(String schoolId) {
		try {
			// 获取mongodb排课信息
			List<ClassplanEntity> classplanList = this.classplanDao.findClassplanList();
			CoursesEntity courser = null;
			if (null != classplanList && classplanList.size() > 0) {
				ClassplanPOJO courseClassplan = new ClassplanPOJO();
				for (int i = 0; i < classplanList.size(); i++) {
					boolean flag = false;
					ClassplanEntity classplanEntity = classplanList.get(i);
					// 判断排课是否存在数据库中，如果不存在就保存，否则不保存
					Map ClassPlan = new HashMap();
					ClassPlan.put("classPlanId", classplanEntity.get_id());
					int count = courseClassplanService.findCId(ClassPlan);
					if (count == 0) {
						if (null != classplanEntity) {
							// 排课计划名称
							courseClassplan.setClassplanName(classplanEntity.getName());
							// 排课计划ID
							courseClassplan.setClassplanId(classplanEntity.get_id());
							// 直播时间说明
							courseClassplan.setClassplanLiveDetail(classplanEntity.getLive_detail());
							// 关联Mysql课程表courses 的m_id 获取course_id
							Map courseMap = new HashMap<>();
							courseMap.put("mId", classplanEntity.getCourse_id());
							courser = coursesService.queryMid(courseMap);
							if (courser == null) {
								continue;
							}
							// 课程PK
							courseClassplan.setCourseId(courser.getCourseId());
							// 查询mongodb表classplan_live(按照live_start_time排序
							// 升序)再limit查找第二条记录
							List<ClassplanLiveEntity> classPlanList = this.classplanDao
									.findClassplanLiveList(classplanEntity.get_id(), flag);
							for (ClassplanLiveEntity live : classPlanList) {
								//// 关联mysql mall_live_room 的m_id查找Live_room_id
								Map roomMap = new HashMap<>();
								roomMap.put("mId", live.getLive_room_id());
								MallLiveRoomEntity room = mallLiveRoomService.queryMid(roomMap);
								if (room == null) {
									continue;
								}
								// 直播间PK
								courseClassplan.setLiveRoomId(Long.valueOf(room.getLiveRoomId()));
								// 授课老师PK
								Map map = new HashMap();
								map.put("mId", live.getLive_teacher_id());
								// 关联mysql表sys_user的m_id
								SysUserEntity sys = sysUserService.queryMid(map);
								if (sys == null) {
									continue;
								}
								courseClassplan.setTeacherId(sys.getUserId());
								// 开课日期
								long startTime = Long.valueOf(live.getLive_start_time());
								Date date = DateUtils.getDate(startTime, 1);
								courseClassplan.setStartTime(date);
							}
							// 备注
							courseClassplan.setRemark(null);
							// 授课老师PK
							courseClassplan.setTimetableId(null);
							// 直播室PK
							courseClassplan.setStudioId(null);
							// 创建人
							courseClassplan.setCreator(null);
							// 创建时间
							courseClassplan.setCreationTime(new Date());
							// 修改人
							courseClassplan.setModifier(null);
							// 修改时间
							courseClassplan.setModifiedTime(null);
							// 平台PK
							courseClassplan.setSchoolId(schoolId);
							// 删除标志0正常1删除
							courseClassplan.setDr(classplanEntity.getDr());
							// 状态0：作废 1：正常 2：结课
							courseClassplan.setStatus(1);
							// 审核状态0：不通过 1：通过
							courseClassplan.setIsAudited(1);
							// 是否公开课 0：是 1：否
							courseClassplan.setIsOpen(1);
							// 资料PK
							courseClassplan.setMaterialId(null);
							syncDao.sysnCourseClassplanSave(courseClassplan);

							// 保存子表
							List<ClassplanLiveEntity> classPlanLiveList = this.classplanDao
									.findClassplanLiveList(classplanEntity.get_id(), true);
							logger.info("syncClassplan classPlanLiveList:{}", classPlanLiveList);
							if (classPlanLiveList.size() > 0 && classPlanLiveList != null) {
								logger.info("syncClassplan classPlanLiveList.size():{}", classPlanLiveList.size());
								for (int j = 0; j < classPlanLiveList.size(); j++) {
									ClassplanLiveEntity classPlanLive = classPlanLiveList.get(j);
									logger.info("syncClassplan item classPlanLive:{}", classPlanLive);

									CourseClassplanLivesEntity entity = new CourseClassplanLivesEntity();
									// 主键
									entity.setClassplanLiveId(classPlanLive.get_id());
									// 排课计划PK
									entity.setClassplanId(courseClassplan.getClassplanId());
									// 直播课程名称
									entity.setClassplanLiveName(classPlanLive.getName());
									Date dayTime = DateUtils.getDate(Long.valueOf(classPlanLive.getLive_start_time()),
											2);
									// 直播日期
									entity.setDayTime(dayTime);
									Date startTime = DateUtils.getDate(Long.valueOf(classPlanLive.getLive_start_time()),
											1);
									// 直播开始时间
									entity.setStartTime(startTime);
									Date endTime = DateUtils.getDate(Long.valueOf(classPlanLive.getLive_end_time()), 1);

									// 结束时间
									entity.setEndTime(endTime);

									// 默认格式2017-01-01 19:30-21:30
									// 根据（live_start_time和live_end_time组合）
									SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
									String ymdhm = sdf1.format(startTime);

									Date MMSS = DateUtils.getDate(Long.valueOf(classPlanLive.getLive_end_time()), 3);
									SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
									String hs = sdf2.format(MMSS);
									// 直播时间说明
									entity.setClassplanLiveTimeDetail(ymdhm + "-" + hs);
									/**
									 * 0-12:00 上午 12-18:00 下午 18-24:00 晚上
									 * live_start_time 时段0.上午;1.下午;2.晚上
									 */
									Date HH = DateUtils.getDate(Long.valueOf(classPlanLive.getLive_end_time()), 4);
									SimpleDateFormat sdf = new SimpleDateFormat("HH");
									int hh = Integer.valueOf(sdf.format(HH));
									if (hh == 0 || hh <= 12) {
										entity.setTimeBucket(0);
									} else if (hh == 12 || hh <= 18) {
										entity.setTimeBucket(1);
									} else if (hh == 18 || hh <= 24) {
										entity.setTimeBucket(2);
									}
									//// 关联mysql mall_live_room
									//// 的m_id查找Live_room_id
									Map map = new HashMap<>();
									map.put("mId", classPlanLive.getLive_room_id());
									MallLiveRoomEntity room = mallLiveRoomService.queryMid(map);
									if (room == null) {
										// 新增直播间
										room = new MallLiveRoomEntity();
										room.setSchoolId(schoolId);
										room.setDr(0);
										room.setLiveRoomName("直播间_" + classPlanLive.getLive_room_id());
										room.setLiveRoomRemake("直播间_" + classPlanLive.getLive_room_id());
										room.setGenseeLiveId(classPlanLive.getLive_room_id());
										// room.setGenseeLiveNum(classPlanLive.getLive_room_id());
										this.mallLiveRoomService.save(room);
										// continue;
									}
									// 直播间PK
									entity.setLiveroomId(room.getLiveRoomId());
									// 直播室PK
									entity.setStudioId(null);
									// 回放地址
									String liveResUrl = classPlanLive.getLive_res_url();
									entity.setBackUrl(liveResUrl);
									//设置回放地址
									BackUtils.setBackId(entity);
//									// 使用正则表达式判断是否包含http、paly-字符串
//									String reg = ".*http.*play-.*";
//									boolean contain = liveResUrl.matches(reg);
//									// 如果是否包含http、paly-字符串
//									if (contain) {
//										// 获取到字符串中-符号后面字符串
//										String a[] = liveResUrl.split("-");
//										String backId = a[1];
//										// 回放ID
//										entity.setBackId(backId);
//										// 回放类型0.CC 1.展视互动
//										entity.setBackType(1);
//									} else {
//										// 回放ID
//										entity.setBackId(null);
//										// 回放类型0.CC 1.展视互动
//										entity.setBackType(0);
//									}
									// 关联mysql表sys_user的m_id
									// 授课老师PK
									Map tMap = new HashMap();
									tMap.put("mId", classPlanLive.getLive_teacher_id());
									// 关联mysql表sys_user的m_id
									SysUserEntity sysUser = sysUserService.queryMid(tMap);
									if (sysUser == null) {
										continue;
									}
									// 教师pk
									entity.setTeacherId(sysUser.getUserId());
									// 班型
									entity.setLiveClassTypeIds(getClassTypeIds(schoolId));
									// 排序
									entity.setOrderNum(j);
									// ------------->
									entity.setCourseLiveDetailId(null);
									// 创建时间
									entity.setCreationTime(new Date());
									// 创建人
									entity.setCreatePerson(null);
									// 修改时间
									entity.setModifiedTime(null);
									// 修改人
									entity.setModifyPerson(null);
									// 平台PK
									entity.setSchoolId(schoolId);
									// 软删除0：正常 1：删除
									entity.setDr(classPlanLive.getDr());
									// 课程PK
									entity.setCourseId(courser.getCourseId());
									// 文件上传
									entity.setFileUrl(classPlanLive.getCourse_files());
									// mongoDB中m_id
									entity.setmId(classPlanLive.get_id());
									syncDao.sysnCourseClassplanLivesSave(entity);
								}

							}
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同步授课老师
	 */
	@Override
	public String syncTeacher(String schoolId) {
		List<TeacherEntity> teacherList = this.classplanDao.findTeacherList();
		if (null != teacherList && 0 < teacherList.size()) {
			List<SysUserEntity> sysUserList = new ArrayList<>();
			int i = 0;
			for (TeacherEntity teacherEntity : teacherList) {
				if (null != teacherEntity) {
					SysUserEntity sysUserEntity = new SysUserEntity();
					// mongodb_ID
					sysUserEntity.setMId(teacherEntity.get_id());
					// 登录账号
					String username = teacherEntity.getTelephone();
					if (StringUtils.isBlank(username)) {
						username = "teacher" + i;
					}
					i++;
					sysUserEntity.setUsername(username);
					// 密码
					sysUserEntity.setPassword(def_psw);
					// 邮箱
					sysUserEntity.setEmail(teacherEntity.getEmail());
					// 手机号码
					sysUserEntity.setMobile(teacherEntity.getTelephone());
					// 微信号
					sysUserEntity.setWxCode(null);
					// 状态
					sysUserEntity.setStatus(1);
					// 班主任
					sysUserEntity.setClassTeacher(0);
					// 授课老师
					sysUserEntity.setTeacher(1);
					// 平台ID
					sysUserEntity.setSchoolId(schoolId);
					// 昵称
					sysUserEntity.setNickName(teacherEntity.getNick_name());
					// 身份证号码
					sysUserEntity.setIdCode(teacherEntity.getId_code());

					sysUserList.add(sysUserEntity);

					sysUserService.saveList(sysUserList);
				}
			}

		}
		return null;
	}

	@Override
	public void syncliveRoom(String schoolId) {
		try {
			LiveRoomPOJO mallLiveRoom = new LiveRoomPOJO();
			List<LiveRoomEntity> mallLiveRoomList = this.classplanDao.findLiveRoomList();
			if (mallLiveRoomList.size() > 0 && mallLiveRoomList != null) {
				for (int i = 0; i < mallLiveRoomList.size(); i++) {
					LiveRoomEntity mgLiveRoom = mallLiveRoomList.get(i);
					// 判断是否存在直接间记录
					Map map = new HashMap<>();
					map.put("mId", mgLiveRoom.get_id());
					MallLiveRoomEntity room = mallLiveRoomService.queryMid(map);
					if (room != null) {
						continue;
					}
					// 直播间名称
					mallLiveRoom.setLiveRoomName(mgLiveRoom.getLive_name());
					// 直播间beiz
					mallLiveRoom.setLiveRoomRemake(mgLiveRoom.getLive_info_text());
					// 互动id
					mallLiveRoom.setGenseeLiveId(mgLiveRoom.getLive_id());
					// 机构id
					mallLiveRoom.setSchoolId(schoolId);
					// 是否使用 1.正常 0.停用
					mallLiveRoom.setStatus(1);
					// 直播间频道密码
					mallLiveRoom.setLiveRoomChannelPassword(null);
					// 创建用户
					mallLiveRoom.setCreator(null);
					// 修改用户
					mallLiveRoom.setModifier(null);
					//// 是否删除 0.未删除 1.删除 用于软删除
					mallLiveRoom.setDr(mgLiveRoom.getDr());
					// 创建时间
					mallLiveRoom.setCreationTime(new Date());
					// 修改时间
					mallLiveRoom.setModifiedTime(null);
					// m_id
					mallLiveRoom.setmId(mgLiveRoom.get_id());
					mallLiveRoomService.save(mallLiveRoom);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量生成学员规划-订单未生成学员规划的
	 * 
	 * @param schoolId
	 *            平台ID
	 */
	@Override
	public void saveUserplanBatch(String schoolId) {
		Map<String, Object> queryOrderMap = new HashMap<>();
		// 平台ID
		queryOrderMap.put("schoolId", schoolId);
		// 支付状态 0.未支付 1.发起支付 ,2.支付成功
		queryOrderMap.put("payStatus", 2);
		// 用户操作状态 0.正常 1.取消 2.删除
		queryOrderMap.put("ustatus", 0);
		// 未生成学员规划
		queryOrderMap.put("userplanType", 0);
		List<OrderPOJO> orderList = mallOrderService.queryList(queryOrderMap);
		if (null != orderList && 0 < orderList.size()) {
			for (OrderPOJO order : orderList) {
				CourseUserplanEntity courseUserplan = new CourseUserplanEntity();
				// 学员PK
				courseUserplan.setUserId(order.getUserId());
				// 商品PK
				courseUserplan.setCommodityId(order.getCommodityId());
				// 省份ID
				courseUserplan.setAreaId(order.getAreaId());
				// 班型
				courseUserplan.setClassId(order.getClassId());
				// 是有学习代替课程 0.否1.正常
				courseUserplan.setIsRep(0);
				// 专业对口0.对口;1不对口
				courseUserplan.setIsMatch(0);
				// 班型PK
				courseUserplan.setClassTypeId(order.getClassTypeId());
				// 状态
				courseUserplan.setStatus(1);
				// 创建用户
				courseUserplan.setCreatePerson(null);
				// 创建时间
				courseUserplan.setCreationTime(new Date());
				//
				courseUserplan.setModifyPerson(null);
				//
				courseUserplan.setModifiedTime(null);
				//
				courseUserplan.setSchoolId(schoolId);
				//
				courseUserplan.setDr(0);
				// 订单ID
				courseUserplan.setOrderId(order.getOrderId());
				// 订单编号
				courseUserplan.setOrderNo(order.getOrderNo());
				// 学员规划状态:0.正常;1.毕业;2.休学
				courseUserplan.setUserplanStatus(0);
				// 最近考试时段PK
				courseUserplan.setGraduateTime(null);
				// 层次PK
				courseUserplan.setLevelId(order.getLevelId());
				// 专业
				courseUserplan.setProfessionId(order.getProfessionId());
				// 保存
				courseUserplanService.save(courseUserplan);
			}
		}
	}

	/**
	 * 同步学习计划
	 */
	@Override
	public void syncCourseUserPlanClass(String schoolId) {
		try {
			// 查询mongoDB学习计划所以信息
			List<CourseUserPlanClassEntity> list = this.classplanDao.findCourseUserPlanClassList();
			logger.info("syncCourseUserPlanClass method start==>list.size():{}", list.size());
			for (int i = 0; i < list.size(); i++) {
				CourseUserPlanClassEntity cupce = list.get(i);
				logger.info("syncCourseUserPlanClass method start==>item:{}", cupce);
				// 判断是否存在记录
				Integer userClassPlanCount = courseUserplanClassService.queryUserClassPlanMid(cupce.get_id());
				logger.info("syncCourseUserPlanClass method userClassPlanCount:{}", userClassPlanCount);
				if (userClassPlanCount == 0) {
					// 根据mongoDB中查找所有学习计划信息，然后根据users查询是否存在用户，如果不存在就break查询
					UsersEntity users = (UsersEntity) this.classplanDao
							.findUsersTuId(Integer.valueOf(cupce.getUser_id()));
					logger.info("syncCourseUserPlanClass method users:{}", users);
					if (users == null) {
						continue;
					}
					// 根据tuid查找mobile号码
					QuserEntity qUser = this.classplanDao.findQUsersUserName(users.getTuid());
					logger.info("syncCourseUserPlanClass method qUser:{}", qUser);
					if (qUser == null) {
						continue;
					}
					// 根据mobile查找userId
					Map uMap = new HashMap();
					uMap.put("mobile", qUser.getUsername());
					Integer userId = usersService.queryUserId(uMap);
					logger.info("syncCourseUserPlanClass method userId:{}", userId);
					if (userId == null) {
						continue;
					}
					// 判断用户是否存在订单记录，只有一条订单记录，否则就不执行插入学习计划保存
					int count = mallOrderService.queryUserOneOrder(String.valueOf(userId));
					logger.info("syncCourseUserPlanClass method count:{}", count);
					if (count == 1) {
						String orderId = mallOrderService.queryOrderId(String.valueOf(userId));
						String userPlanId = courseUserplanService.queryUserPlanId(orderId);
						if (StringUtils.isBlank(userPlanId)) {
							continue;
						}
						// 保存学习计划主表
						CourseUserplanClassEntity courseUserplanClass = new CourseUserplanClassEntity();
						// 学员规划PK
						courseUserplanClass.setUserplanId(Long.valueOf(userPlanId));
						// 计划单号
						courseUserplanClass.setUserplanClassNo(HEAD + HEAD + studyplanKGS.nextId());
						// 考试时段PK
						courseUserplanClass.setExamScheduleId(null);
						// 备注
						courseUserplanClass.setRemark(null);
						// 平台id
						courseUserplanClass.setSchoolId(schoolId);
						// 创建时间
						Date createTime = DateUtils.getDate(Long.valueOf(cupce.getCreate_user_time()), 1);
						courseUserplanClass.setCreateTime(createTime);
						// 修改时间
						courseUserplanClass.setModifyTime(null);
						// 创建人
						courseUserplanClass.setCreatePerson(null);
						// 修改人
						courseUserplanClass.setModifyPerson(null);
						// dr
						courseUserplanClass.setDr(0);
						// 审核0：不通过 1：通过
						courseUserplanClass.setStatus(1);
						courseUserplanClass.setmId(cupce.get_id());
						syncDao.courseUserplanClassSave(courseUserplanClass);

						// 保存学习计划子表
						CourseUserplanClassDetailEntity courseUserplanClassDetail = new CourseUserplanClassDetailEntity();
						// 学习计划PK
						courseUserplanClassDetail.setUserplanClassId(courseUserplanClass.getUserplanClassId());

						// 查找course_id
						ClassplanEntity mClassPlan = this.classplanDao.findCourseId(cupce.getClassplan_id());
						Map<String, Object> cMap = new HashMap<>();
						cMap.put("mId", mClassPlan.getCourse_id());
						CoursesEntity courses = coursesService.queryMid(cMap);
						if (null == courses) {
							logger.info("SyncServiceImpl syncCourseUserPlanClass courses is null! mClassPlan:{}",
									mClassPlan);
							continue;
						}
						// 查找userPlanDetailId
						Map<String, Object> updMap = new HashMap<>();
						updMap.put("userPlanId", userPlanId);
						updMap.put("courseId", courses.getCourseId());
						String userPlanDetailId = courseUserplanDetail.queryUserplanDetailId(updMap);
						logger.info("syncCourseUserPlanClass method userPlanDetailId:{}", userPlanDetailId);
						if (userPlanDetailId == null) {
							Map<String, Object> queryOneByUserPlanIdMap = new HashMap<>();
							;
							queryOneByUserPlanIdMap.put("userplanId", userPlanId);
							CourseUserplanDetailEntity queryOneByUserPlanId = this.courseUserplanDetail
									.queryOneByUserPlanId(queryOneByUserPlanIdMap);
							if (null == queryOneByUserPlanId) {// 如果学员规划下没有对应的课程
																// 则给学员对应的学员规划新增该课程
								continue;
							}
							CourseUserplanDetailEntity newCourseUserplanDetailEntity = new CourseUserplanDetailEntity();
							newCourseUserplanDetailEntity.setAreaId(queryOneByUserPlanId.getAreaId());
							newCourseUserplanDetailEntity.setCourseId(courses.getCourseId());
							newCourseUserplanDetailEntity.setIsPass(0);
							newCourseUserplanDetailEntity.setIsSubstitute(0);
							newCourseUserplanDetailEntity.setIsSubstituted(0);
							// is_suitable
							newCourseUserplanDetailEntity.setIsSuitable(0);
							newCourseUserplanDetailEntity.setIsUnitedExam(1);
							newCourseUserplanDetailEntity.setSchoolId(schoolId);
							// newCourseUserplanDetailEntity.setUserplanDetailId(userplanDetailId);
							newCourseUserplanDetailEntity.setUserplanId(queryOneByUserPlanId.getUserplanId());
							this.courseUserplanDetail.save(newCourseUserplanDetailEntity);
							// 新增成功后获取ID
							userPlanDetailId = String.valueOf(newCourseUserplanDetailEntity.getUserplanDetailId());
						}
						if (StringUtils.isBlank(userPlanDetailId)) {
							continue;
						}
						// 学员规划课程子表PK
						courseUserplanClassDetail.setUserplanDetailId(Long.valueOf(userPlanDetailId));
						// 排课计划PK
						courseUserplanClassDetail.setClassplanId(cupce.getClassplan_id());
						// 入课时间
						courseUserplanClassDetail.setTimestamp(createTime);
						// 备注
						courseUserplanClassDetail.setRemark(null);
						// dr
						courseUserplanClassDetail.setDr(0);
						// 平台id
						courseUserplanClassDetail.setSchoolId(schoolId);
						// 排序
						courseUserplanClassDetail.setOrderNum(0);
						syncDao.courseUserplanClassDetailSave(courseUserplanClassDetail);
					} else {
						continue;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void syncUsers(String schoolId) {
		List<SyncUsersEntity> findUsers = this.classplanDao.findUsers();
		if (null != findUsers) {
			for (SyncUsersEntity syncUsersEntity : findUsers) {
				if (null != syncUsersEntity) {
					// 用户手机号码
					QuserEntity qquser = this.classplanDao.findQUsersUserName(syncUsersEntity.getTuid());
					// 用户对应手机号码存在
					if (null != qquser && StringUtils.isNotBlank(qquser.getUsername())) {
						// 手机号码
						String mobile = qquser.getUsername();
						// 校验手机号码是否存在
						Map<String, Object> cmap = new HashMap<>();
						cmap.put("schoolId", schoolId);
						cmap.put("mobile", mobile);
						boolean checkMobile = usersService.checkMobile(cmap);
						// 不存在
						if (!checkMobile) {
							io.renren.entity.UsersEntity user = new io.renren.entity.UsersEntity();
							user.setUserId(syncUsersEntity.get_id());
							user.setSchoolId(schoolId);
							user.setMobile(mobile);
							user.setNickName(syncUsersEntity.getNick_name());
							user.setPic(syncUsersEntity.getPic());
							user.setSex(syncUsersEntity.getSex());
							user.setEmail(syncUsersEntity.getEmail());
							user.setPassword(PSW);
							user.setCreator(null);
							user.setCreationTime(new Date());
							user.setModifier(null);
							user.setModifiedTime(null);
							user.setLastLoginIp(null);
							user.setLastLoginTime(null);
							user.setRemake(null);
							usersService.save(user);
						}
					}
				}
			}
		}
	}

	@Override
	public void updateCourseClassPlanLives() {
		List<String> list = syncDao.queryBackUrlInfo();
		CourseClassplanLivesEntity entity = new CourseClassplanLivesEntity();
		for (String s : list) {
			String backId = BackUtils.getBackId(s);
//			entity.setBackId(backId);
			// 回放ID
			entity.setBackId(backId);
			// 回放类型0.CC 1.展视互动
			entity.setBackType(StringUtils.isNotBlank(backId) ? 0: 1);
			
//			// 使用正则表达式判断是否包含http、paly-字符串
//			String reg = ".*http.*play-.*";
//			boolean contain = s.matches(reg);
//			// 如果是否包含http、paly-字符串
//			if (contain) {
//				// 获取到字符串中-符号后面字符串
//				String a[] = s.split("-");
//				String backId = a[1];
//				// 回放ID
//				entity.setBackId(backId);
//				// 回放类型0.CC 1.展视互动
//				entity.setBackType(1);
//				entity.setBackUrl(s);
//				syncDao.updateCourseClassPlanLives(entity);
//			} else {
//				// 回放ID
//				entity.setBackId(null);
//				// 回放类型0.CC 1.展视互动
//				entity.setBackType(0);
//				entity.setBackUrl(s);
//				syncDao.updateCourseClassPlanLives(entity);
//			}
		}

	}

	/**
	 * 同步直播和录播信息
	 */
	@Override
	public void syncLiveLog(Integer _type,String schoolId) {
		logger.info("SyncServiceImpl syncLiveLog start _type={}  schoolId={}",_type,schoolId );
		// 查询mongodb数据库中learning_records表所有信息
		List<LearningRecords> recordsList = this.classplanDao.findLearningRecords(_type);
		if (recordsList != null && recordsList.size() > 0) {
			for (LearningRecords records : recordsList) {
				// type = 0 直播 type = 1 回放
				int type = records.getType();
				// 根据mongoDB中查找所有学习计划信息，然后根据users查询是否存在用户，如果不存在就break查询
				UsersEntity users = (UsersEntity) this.classplanDao.findUsersTuId(records.getUser_id());
				if (users == null) {
					continue;
				}
				// 根据tuid查找mobile号码
				QuserEntity qUser = this.classplanDao.findQUsersUserName(users.getTuid());
				if (qUser == null) {
					continue;
				}
				Map<String , Object> userMap = new HashMap<>();
				userMap.put("mobile", qUser.getUsername());
				// 根据mobile号码查找user_id
				Integer userId = usersService.queryUserId(userMap);
				if (userId == null) {
					continue;
				}
				// 查找Live_id值
				ClassplanLiveEntity classplanLive = this.classplanDao.findLiveId(records.getLive_id());
//				logger.info("SyncServiceImpl syncLiveLog records={}  type={}",records , type);
				// 如果type=0就保存直播信息，否则保存录播信息
				//if (type == 0) {
					// 判断是否存在这条记录
					int logWatchCount = logWatchService.queryExistCount(records.get_id());
					if (logWatchCount == 0) {
						LogWatchEntity logWatch = new LogWatchEntity();
						String liveId = null;
						if (classplanLive == null) {
							continue;
						}
						liveId = classplanLive.getLive_id();
						if(StringUtils.isBlank(liveId)){
							continue;
						}
						logWatch.setLvId(liveId);
						//String genseeLiveNum = mallLiveRoomService.queryGenseeLiveNum(liveId);
						//logWatch.setLiveNum(genseeLiveNum);
						// 用户ID
						logWatch.setUserId(Long.valueOf(userId));
						// 根据liveId查找排课明细主键
						String classplanLiveId = courseClassplanLivesService.findClassplanliveId(records.getLive_id());
						// 如果不存在就不操作
						if (StringUtils.isBlank(classplanLiveId)) {
							continue;
						}
						// 业务ID
						logWatch.setBusinessId(classplanLiveId);
						// 观看时长
						logWatch.setWatchDuration(MIAOSHU);
						// 开始时间
						Date startTime = DateUtils.getDate(Long.valueOf(classplanLive.getLive_start_time()), 1);
						logWatch.setLiveStartTime(startTime);
						// 结束时间
						Date endTime = DateUtils.getDate(Long.valueOf(classplanLive.getLive_end_time()), 1);
						logWatch.setLiveEndTime(endTime);
						logWatch.setLvDuration(MIAOSHU);
						logWatch.setAttend30(1);
						logWatch.setVersionCode(1);
						//logWatch.setBusinessType(0);
						logWatch.setLogType(type);
						logWatch.setSchoolId(schoolId);
						logWatch.setMId(records.get_id());
						logWatch.setCreateTime(new Date());
						logWatchService.save(logWatch);
					}
				//} 
					/*else {
					// 保存录播信息
					int videoCount = videoLogService.videoLogExist(records.get_id());
					if (videoCount == 0) {
						VideoLogEntity videoLog = new VideoLogEntity();
//						logger.info("SyncServiceImpl syncLiveLog classplanLive={}",classplanLive);
						if(classplanLive==null){
							continue;
						}
						videoLog.setVideoId(classplanLive.getLive_res_url());
						videoLog.setUserId(Long.valueOf(userId));
						videoLog.setVideoDuration(MIAOSHU);
						videoLog.setWatchDuration(MIAOSHU);
						videoLog.setAttend30(1);
						videoLog.setVersionCode(1);
						videoLog.setSchoolId(schoolId);
						videoLog.setmId(records.get_id());
						videoLogService.save(videoLog);
					}
				}*/
			}
		}

	}

	/**
	 * 更新排课明细回放
	 * 
	 * @param schoolId
	 *            平台ID
	 */
	@Override
	public void UpdatecourseClassplanLives() {
		// 查询所有mongoDB中classplan_live表所有数据
		List<ClassplanLiveEntity> list = this.classplanDao.findAllClassplanLive();
		if (list != null && list.size() > 0) {
			for (ClassplanLiveEntity Entity : list) {
				String _id = Entity.get_id();
				// 根据mongoBb中_id来查找MySQL中m_id,
				int count = courseClassplanLivesService.findMid(_id);
				// 判断是否存在，如果存在就执行更新操作，否则。。。
				if (count > 0) {
					String backUrl = Entity.getLive_res_url();
					Map map = new HashMap();
					map.put("mId", Entity.get_id());
					map.put("backUrl", backUrl);
					// 根据mongoDB中_id更新MySQL数据中back_url字段
					courseClassplanLivesService.updateBackUrl(map);
				}
			}

		}
	}


	@Override
	public void syncKSLogMongodb() {
		int countMongodbLog = this.logWatchService.queryCountMongodbLog();
		if(countMongodbLog > 0){
			//每页1W
			int limit = 10000;
//			int maxPage = 1;
			int maxPage = countMongodbLog / limit + (countMongodbLog % limit == 0 ? 0 : 1);
			//分页查询
			for (int page = 1; page <= maxPage; page++) {
				int offset = (page - 1) * limit;
				List<LogWatchEntity> listMongodbLog = this.logWatchService.queryListMongodbLog(offset, limit);
				if(null != listMongodbLog && !listMongodbLog.isEmpty()){
					//迭代处理
					for (LogWatchEntity logWatchEntity : listMongodbLog) {
						saveMongodbWatchLog(logWatchEntity);
					}
				}
			}
		}
	}
	@Transactional
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	private void saveMongodbWatchLog(LogWatchEntity logWatchEntity){
		if(null != logWatchEntity){
			//日志类型 0:直播  1:录播
			Integer logType = logWatchEntity.getLogType();
			LogGenseeWatchEntity logGenseeWatchEntity = null;
			if(0 == logType){//0:直播
				if(this.liveLogDetailService.selectDetailCount(logWatchEntity.getLvId(), null, logWatchEntity.getUserId(), logWatchEntity.getLiveStartTime().getTime(), logWatchEntity.getLiveEndTime().getTime())){
					return;
				}
				
				LiveLogDetailEntity liveLogDetailEntity = new LiveLogDetailEntity();
				//直播间ID
				liveLogDetailEntity.setLiveId(logWatchEntity.getLvId());
				//直播间号
				liveLogDetailEntity.setLiveNum("0");
				//用户ID
				liveLogDetailEntity.setUserId(logWatchEntity.getUserId());
				//进入直播间时间范围
				liveLogDetailEntity.setJoinTime(logWatchEntity.getLiveStartTime().getTime());
				liveLogDetailEntity.setLeaveTime(logWatchEntity.getLiveEndTime().getTime());
				liveLogDetailEntity.setJoinType(1);
				liveLogDetailEntity.setPlatformCode(1);
				liveLogDetailEntity.setCreateTime(new Date());
				liveLogDetailEntity.setProductId(1l);
				//保存明细记录
				this.liveLogDetailService.save(liveLogDetailEntity);
				//
				logGenseeWatchEntity = new LogGenseeWatchEntity();
				logGenseeWatchEntity.setLiveId(logWatchEntity.getLvId());
				logGenseeWatchEntity.setLiveDur(logWatchEntity.getLiveEndTime().getTime() - logWatchEntity.getLiveStartTime().getTime());
				logGenseeWatchEntity.setVideoDur(0l);
			}else if(1 == logType){// 1:录播
				if(!this.videoLogDetailService.checkAddAble(
						logWatchEntity.getLiveStartTime().getTime(), 
						logWatchEntity.getLiveEndTime().getTime(), 0, logWatchEntity.getLvId(), logWatchEntity.getUserId())
						){
					return;
				}
				VideoLogDetailEntity detailEntity = new VideoLogDetailEntity();
				//视频ID
				detailEntity.setVideoId(logWatchEntity.getLvId());
				//学员
				detailEntity.setUserId(logWatchEntity.getUserId());
				//开始时间
				detailEntity.setStartTime(logWatchEntity.getLiveStartTime().getTime());
				//开始时间
				detailEntity.setLeaveTime(logWatchEntity.getLiveEndTime().getTime());
				//开始时间
				detailEntity.setDuration(logWatchEntity.getLiveEndTime().getTime() - logWatchEntity.getLiveStartTime().getTime());
				//设备
				detailEntity.setDevice(0);
				//平台代号
				detailEntity.setPlatformCode(1);
				detailEntity.setCreateTime(new Date());
				detailEntity.setProductId(1l);
				this.videoLogDetailService.save(detailEntity);
				
				logGenseeWatchEntity = new LogGenseeWatchEntity();
				//处理求和信息
				logGenseeWatchEntity.setVideoId(logWatchEntity.getLvId());
				logGenseeWatchEntity.setVideoDur(logWatchEntity.getLiveEndTime().getTime() - logWatchEntity.getLiveStartTime().getTime());
				logGenseeWatchEntity.setLiveDur(0l);
			}
			if(null != logGenseeWatchEntity){
				logGenseeWatchEntity.setUserId(logWatchEntity.getUserId());
				logGenseeWatchEntity.setBusinessId(logWatchEntity.getBusinessId());
				logGenseeWatchEntity.setFullDur(logWatchEntity.getLiveEndTime().getTime() - logWatchEntity.getLiveStartTime().getTime());
				logGenseeWatchEntity.setWatchDur(logWatchEntity.getLiveEndTime().getTime() - logWatchEntity.getLiveStartTime().getTime());
				logGenseeWatchEntity.setAttendPer(new BigDecimal(1));
				logGenseeWatchEntity.setCreateTime(new Date());
				logGenseeWatchEntity.setProductId(1l);
				this.logGenseeWatchService.saveOrUpdate(logGenseeWatchEntity);
			}
		}
	}

	@Override
	public void syncAgent() {
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cachedThreadPool.execute(new Runnable() {
			
			SysUserService sysUserService = (SysUserService) SpringContextUtils.getBean("sysUserService");
			
			public synchronized void run() {
				List<SysUserEntity> agentTeacherList = this.sysUserService.queryAllAgentTeacher();
				for (SysUserEntity agentTeacher : agentTeacherList) {
					//发送Udesk创建客服
					if(StringUtils.isNotBlank(agentTeacher.getMobile())){
						Integer ownerId = SendUdeskUtil.creatUdeskAgent(agentTeacher.getMobile(), agentTeacher.getNickName());
						if(null != ownerId){
							agentTeacher.setOwnerId(ownerId);
							this.sysUserService.update(agentTeacher);
						}
					}else{
						Integer ownerId = SendUdeskUtil.creatUdeskAgent(agentTeacher.getUsername(), agentTeacher.getNickName());
						if(null != ownerId){
							agentTeacher.setOwnerId(ownerId);
							this.sysUserService.update(agentTeacher);
						}
						
					}
					try {
						Thread.sleep(20000);//延迟1s
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		});
	}


	/*@Override
	public void syncCustomers() {
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		final int num = this.mallOrderService.queryTotalCustomers();
		
		cachedThreadPool.execute(new Runnable() {
			int offset = 0;//从第一条数据开始
			MallOrderService mallOrderService = (MallOrderService) SpringContextUtils.getBean("mallOrderService");
			
			public synchronized void run() {
				while(offset < num){
					JSONObject json = new JSONObject();
					JSONArray customers=new JSONArray();
					List<SyncCustomerPOJO> customerPOJOList = this.mallOrderService.queryMapList(offset);
					for (SyncCustomerPOJO customerPOJO : customerPOJOList) {
						
						String[] arr = {customerPOJO.getMobile()+"-"+customerPOJO.getOrderId()};
						
						JSONObject customer = new JSONObject();
						customer.put("nick_name", customerPOJO.getNickName());//客户名称
						customer.put("owner_id", customerPOJO.getOwnerId());//客服id
						customer.put("owner_group_id", 65540);//客服组id
						customer.put("cellphones", arr);//客户电话
						JSONObject customerField = new JSONObject();
						customerField.put("TextField_21507", DateUtils.format(customerPOJO.getPayTime(), DateUtils.DATE_PATTERN));//支付时间
						customerField.put("TextField_21508", customerPOJO.getAreaName());//省份名称
						customerField.put("TextField_21509", customerPOJO.getLevelName());//层次
						customerField.put("TextField_21510", customerPOJO.getClassName());//班级名称
						customerField.put("TextField_21511", customerPOJO.getDeptName());//校区名称
						customerField.put("TextField_21512", customerPOJO.getProfessionName());//专业名称
						customerField.put("TextField_21513", customerPOJO.getCommodityName());//商品名称
						customer.put("custom_fields", customerField);
						customer.put("tags", customerPOJO.getTeacherName());//标签
						
						customers.add(customer);
						
						
					}
					json.put("customers", customers.toString());
					SendUdeskUtil.batchImportCustomers(json.toString());
					offset+=100;
					try {
						Thread.sleep(60000);//延迟60s
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		
	}*/
	@Override
	public void syncCustomers(final Integer startOrderId, final Integer endOrderId, final String teacherMobile, final String orderNos) {
		try {
			ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
			List<String> teacherMobileListTmp = new ArrayList<String>();
			if(StringUtils.isNotBlank(teacherMobile)){
				String[] splitstr = teacherMobile.split(",");
				for (String string : splitstr) {
					teacherMobileListTmp.add(string.trim());
				}
			}
			final List<String> teacherMobileList = teacherMobileListTmp;
			
			/**订单号*/
			List<String> orderNoListTmp = new ArrayList<String>();
			if(StringUtils.isNotBlank(orderNos)){
				String[] splitstr = orderNos.split(",");
				for (String string : splitstr) {
					orderNoListTmp.add(string.trim());
				}
			}
			final List<String> orderNoList = orderNoListTmp;
			
			logger.error("syncCustomers params: startOrderId={},endOrderId={},teacherMobile={},teacherMobileList.size={},orderNoList.size{}",startOrderId,endOrderId,teacherMobile,teacherMobileList.size(),orderNoList.size());
			final int num = this.mallOrderService.queryTotalCustomers(startOrderId,endOrderId,teacherMobileList,orderNoList);
			logger.error("syncCustomers num={}",num);
			cachedThreadPool.execute(new Runnable() {
				int offset = 0;//从第一条数据开始
				int i = 0;
				MallOrderService mallOrderService = (MallOrderService) SpringContextUtils.getBean("mallOrderService");
				
				public synchronized void run() {
					while(offset < num){
						JSONObject json = new JSONObject();
						JSONArray customers=new JSONArray();
						List<SyncCustomerPOJO> customerPOJOList = this.mallOrderService.queryMapList(offset,startOrderId,endOrderId,teacherMobileList,orderNoList);
						logger.error("syncCustomers customerPOJOList.size={}",customerPOJOList.size());
						for (SyncCustomerPOJO customerPOJO : customerPOJOList) {
							i++;
							String phone = customerPOJO.getMobile().trim()+"-"+customerPOJO.getOrderId();							
							//同步到Udesk
							try {
								SendUdeskUtil.creatUdeskCustomer(
																customerPOJO.getNickName(), 
											    				phone, 
											    				customerPOJO.getTeacherName(), 
											    				customerPOJO.getOwnerId(), 
											    				DateUtils.format(customerPOJO.getPayTime(), DateUtils.DATE_PATTERN), 
											    				customerPOJO.getAreaName(), 
											    				customerPOJO.getLevelName(), 
											    				customerPOJO.getClassName(), 
											    				customerPOJO.getDeptName(), 
											    				customerPOJO.getProfessionName(), 
											    				customerPOJO.getCommodityName()
											    				);
							} catch (Exception e1) {
								logger.error("syncCustomers creatUdeskCustomer error... Message={},customerPOJO={}",e1.getMessage(),customerPOJO);
								e1.printStackTrace();
							}
							try {
								SendUdeskUtil.updateUdeskCustomer(
																customerPOJO.getNickName(), 
											    				phone, 
											    				customerPOJO.getTeacherName(), 
											    				customerPOJO.getOwnerId(), 
											    				DateUtils.format(customerPOJO.getPayTime(), DateUtils.DATE_PATTERN), 
											    				customerPOJO.getAreaName(), 
											    				customerPOJO.getLevelName(), 
											    				customerPOJO.getClassName(), 
											    				customerPOJO.getDeptName(), 
											    				customerPOJO.getProfessionName(), 
											    				customerPOJO.getCommodityName()
											    				);
							} catch (Exception e1) {
								logger.error("syncCustomers updateUdeskCustomer error... Message={},customerPOJO={}",e1,customerPOJO);
								e1.printStackTrace();
							}
							//保存日志
							logger.error("curr={},num={},message body=[{}]",i, num, customerPOJO);
							try {
								Thread.sleep(1000L);//延迟1s
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						offset+=100;
					}
				}
			});
			
		} catch (Exception e) {
			logger.error("syncCustomers error... Message={}",e);
			e.printStackTrace();
		}
	}
	

}
