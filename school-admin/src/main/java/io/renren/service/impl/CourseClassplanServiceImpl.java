package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.entity.*;
import io.renren.service.manage.MessageProductor2KuaidaCourseClassplanService;
import io.renren.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.constant.DateTimeConstant;
import io.renren.dao.CourseClassplanDao;
import io.renren.pojo.classplan.ClassplanLivePOJO;
import io.renren.pojo.classplan.ClassplanPOJO;
import io.renren.utils.ClassplanLiveException;
import io.renren.utils.Constant;
import io.renren.utils.DateUtils;
import io.renren.utils.UUIDUtils;


@Transactional(readOnly = true)
@Service("courseClassplanService")
public class CourseClassplanServiceImpl implements CourseClassplanService {
    @Autowired
    private CourseClassplanDao courseClassplanDao;
    @Autowired
    private CourseClassplanLivesService courseClassplanLivesService;
    @Autowired
    private CourseLiveDetailsService courseLiveDetailsService;
    @Autowired
    private TimeTableDetailService timeTableDetailService;
    @Autowired
    private MallLiveRoomService mallLiveRoomService;
    @Autowired
    private MallStudioService mallStudioService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CoursesService coursesService;
    @Autowired
    private MallGoodsInfoService mallGoodsInfoService;
    @Autowired
    private MallGoodsDetailsService mallGoodsDetailsService;
    @Autowired
    private MessageProductor2KuaidaCourseClassplanService msgPro2KdCourseClassplanService;

//	@Override
//	public CourseClassplanEntity queryObject(Map<String, Object> map){
//		return courseClassplanDao.queryObject(map);
//	}

    @Autowired
    private CourseClassplanChangeRecordService courseClassplanChangeRecordService;

    public ClassplanPOJO queryObject1(Map<String, Object> map) {
        return courseClassplanDao.queryObject1(map);
    }

    @Override
    public List<CourseClassplanEntity> queryList(Map<String, Object> map) {
        return courseClassplanDao.queryList(map);
    }

    public List<Map<String, Object>> queryListMap(Map<String, Object> map) {
        return courseClassplanDao.queryListMap(map);
    }


    @Override
    public int queryTotal(Map<String, Object> map) {
        return courseClassplanDao.queryTotal(map);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void save(ClassplanPOJO courseClassplan) throws ClassplanLiveException {
        // 2018-05-19 去掉排课冲突校验
        //排课冲突信息抛出
//		String errMsg = checkLive(courseClassplan);
//		if(StringUtils.isNotBlank(errMsg)){
//			throw new ClassplanLiveException(errMsg);
//		}
        //审核初始值
        courseClassplan.setIsAudited(2);
        //dr
        courseClassplan.setDr(0);
        //创建时间
        courseClassplan.setCreationTime(new Date());
        //修改时间
        courseClassplan.setModifiedTime(courseClassplan.getCreationTime());
        //en
        CourseClassplanEntity en = ClassplanPOJO.getEntity(courseClassplan);
        //生成ID
        en.setClassplanId(UUIDUtils.formatter());
//		en.setClassplanId(UUID.randomUUID().toString());
        //保存主表
        courseClassplanDao.save(en);
        //排课发送到MQ
        msgPro2KdCourseClassplanService.pushToMessageQueue(en);

        //子表
        List<ClassplanLivePOJO> detailList = courseClassplan.getDetailList();
        //子表保存
        if (null != detailList && detailList.size() > 0) {
            for (int i = 0; i < detailList.size(); i++) {
                //pojo
                ClassplanLivePOJO clp = detailList.get(i);
                //entity
                CourseClassplanLivesEntity ccle = ClassplanLivePOJO.getEntity(clp);
                //set classplanLiveName
                ccle.setClassplanLiveName(clp.getClassplanLiveName());
                //school_id
                ccle.setSchoolId(en.getSchoolId());
                //主表pk
                ccle.setClassplanId(en.getClassplanId());
                //开课时间
                ccle.setStartTime(clp.getStartTime());
                ccle.setEndTime(clp.getEndTime());
                //直播室
                ccle.setStudioId(clp.getStudioId());
                //直播间
                ccle.setLiveroomId(clp.getLiveroomId());
                //授课老师
                ccle.setTeacherId(clp.getTeacherId());
                //创建人
                ccle.setCreatePerson(en.getCreator());
                //修改人
                ccle.setModifyPerson(ccle.getCreatePerson());
                //课程pk
                ccle.setCourseId(en.getCourseId());
                //排序
                ccle.setOrderNum(i);

                ccle.setDayTime(DateUtils.dateClear(ccle.getStartTime()));
                //上传文件的文件名
                ccle.setFileName(clp.getFileName());
                //上期复习文件的文件名
                ccle.setReviewName(clp.getReviewName());
                //本期预习文件的文件名
                ccle.setPrepareName(clp.getPrepareName());
                //课堂资料文件的文件名
                ccle.setCoursewareName(clp.getCoursewareName());
//				ccle.setClassplanLiveTimeDetail(classplanLiveTimeDetailFormate(ccle.getStartTime(), ccle.getEndTime()));

                //保存子表
                courseClassplanLivesService.save(ccle);
            }
        }
    }

    public String checkLive(ClassplanPOJO courseClassplan) {
        StringBuffer stb = new StringBuffer();
        if (null == courseClassplan || null == courseClassplan.getCheckType() || !courseClassplan.getCheckType()) {
            return stb.toString();
        }
        // 子表
        List<ClassplanLivePOJO> detailList = courseClassplan.getDetailList();
        // 子表保存
        if (null != detailList && detailList.size() > 0) {
            for (int i = 0, j = 0; i < detailList.size(); i++) {
                // pojo
                ClassplanLivePOJO clp = detailList.get(i);
                // entity
                CourseClassplanLivesEntity ccle = ClassplanLivePOJO.getEntity(clp);
                // school_id
                ccle.setSchoolId(courseClassplan.getSchoolId());
                // 主表pk
                ccle.setClassplanId(courseClassplan.getClassplanId());
                // 开课时间
                ccle.setStartTime(clp.getStartTime());
                ccle.setEndTime(clp.getEndTime());
                // 直播室
                ccle.setStudioId(clp.getStudioId());
                // 直播间
                ccle.setLiveroomId(clp.getLiveroomId());
                // 授课老师
                ccle.setTeacherId(clp.getTeacherId());
                // 课程pk
                ccle.setCourseId(courseClassplan.getCourseId());

                ccle.setDayTime(DateUtils.dateClear(ccle.getStartTime()));

                //校验授课老师冲突
                Map<String, Object> checkTeacherMap = this.courseClassplanLivesService.checkTeacher(ccle);
                if (null != checkTeacherMap && null != checkTeacherMap.get("classplanLiveId")) {
                    String errorMsg = ++j + ".授课老师时间冲突！" + checkTeacherMap.get("dayTime") + " "
                            + DateTimeConstant.TIME_BUCKET[(Integer) checkTeacherMap.get("timeBucket")] + " 的"
                            + ccle.getClassplanLiveName() + "与排课计划【" + checkTeacherMap.get("classplanName") + "】的直播课【"
                            + checkTeacherMap.get("classplanLiveName") + "】冲突！";
//					throw new ClassplanLiveException(errorMsg);
                    stb.append(errorMsg + "<br/>");
                }
                //校验直播室冲突
                Map<String, Object> checkStudioMap = this.courseClassplanLivesService.checkStudio(ccle);
                if (null != checkStudioMap && null != checkStudioMap.get("classplanLiveId")) {
                    String errorMsg = ++j + ".直播室冲突！" + checkStudioMap.get("dayTime") + " "
                            + DateTimeConstant.TIME_BUCKET[(Integer) checkStudioMap.get("timeBucket")] + " 的"
                            + ccle.getClassplanLiveName() + "与排课计划【" + checkStudioMap.get("classplanName") + "】的直播课【"
                            + checkStudioMap.get("classplanLiveName") + "】冲突！";
//					throw new ClassplanLiveException(errorMsg);
                    stb.append(errorMsg + "<br/>");
                }
                //校验直播间冲突
                Map<String, Object> checkLiveRoomMap = this.courseClassplanLivesService.checkLiveRoom(ccle);
                if (null != checkLiveRoomMap && null != checkLiveRoomMap.get("classplanLiveId")) {
                    String errorMsg = ++j + ".直播间冲突！" + checkLiveRoomMap.get("dayTime") + " "
                            + DateTimeConstant.TIME_BUCKET[(Integer) checkLiveRoomMap.get("timeBucket")] + " 的"
                            + ccle.getClassplanLiveName() + "与排课计划【" + checkLiveRoomMap.get("classplanName") + "】的直播课【"
                            + checkLiveRoomMap.get("classplanLiveName") + "】冲突！";
//					throw new ClassplanLiveException(errorMsg);
                    stb.append(errorMsg + "<br/>");
                }
                //校验同一课程多次排课冲突
                Map<String, Object> checkCourseLiveMap = this.courseClassplanLivesService.checkCourseLive(ccle);
                if (null != checkCourseLiveMap && null != checkCourseLiveMap.get("classplanLiveId")) {
                    String errorMsg = ++j + ".同一课程下的排课课次冲突！" + checkCourseLiveMap.get("dayTime") + " "
                            + DateTimeConstant.TIME_BUCKET[(Integer) checkCourseLiveMap.get("timeBucket")] + " 的"
                            + ccle.getClassplanLiveName() + "与排课计划【" + checkCourseLiveMap.get("classplanName") + "】的直播课【"
                            + checkCourseLiveMap.get("classplanLiveName") + "】冲突！";
//					throw new ClassplanLiveException(errorMsg);
                    stb.append(errorMsg + "<br/>");
                }

                //TODO 校验同一商品下的不同课程的排课课次冲突
                //*******************************************************************************//
                //通过排课id获取课程信息
                CoursesEntity coursesEntity = new CoursesEntity();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("courseId", ccle.getCourseId());
                map.put("schoolId", ccle.getSchoolId());
                coursesEntity = coursesService.queryObject(map);

                /*List<String> classplanLiveIds = new ArrayList<String>();*/


                if (coursesEntity.getCourseEq() == 0) {//课程不可冲突
                    //确定该课程在哪些商品下
                    List<Long> goodsInfoIds = goodsInfoListByCourseId(ccle.getCourseId(), ccle.getSchoolId());
                    //遍历商品
                    for (Long goodsInfoId : goodsInfoIds) {
                        //确定某个商品下的课程
                        List<Long> courseIds = courseListByGoodsInfoId(goodsInfoId, ccle.getSchoolId());
                        for (Long courseId : courseIds) {
							/*//确定某个课程排课下的课次
							List<String> courseLiveIds = courseLiveListByCourseId(courseId, ccle.getSchoolId());
							if(!courseLiveIds.isEmpty()){
								for (String courseLiveId : courseLiveIds) {
									//将包含该课程的所有商品下的所有课程的排课下的课次放入List集合
									classplanLiveIds.add(courseLiveId);
								}
							}*/
                            ccle.setCourseId(courseId);
                            Map<String, Object> checkOneCommodityCourseLiveMap = this.courseClassplanLivesService.checkOneCommodityCourseLive(ccle);
                            if (null != checkOneCommodityCourseLiveMap && null != checkOneCommodityCourseLiveMap.get("classplanLiveId")) {
                                String errorMsg = ++j + ".同一商品下的不同课程的排课课次冲突！" + checkOneCommodityCourseLiveMap.get("dayTime") + " "
                                        + DateTimeConstant.TIME_BUCKET[(Integer) checkOneCommodityCourseLiveMap.get("timeBucket")] + " 的"
                                        + ccle.getClassplanLiveName() + "与排课计划【" + checkOneCommodityCourseLiveMap.get("classplanName") + "】的直播课【"
                                        + checkOneCommodityCourseLiveMap.get("classplanLiveName") + "】冲突！";
                                //			throw new ClassplanLiveException(errorMsg);
                                stb.append(errorMsg + "<br/>");
                            }
                        }
                    }
					/*if(!classplanLiveIds.isEmpty()){
						String[] classplanLiveIdArray = StringUtils.strip(classplanLiveIds.toString(), "[]").split(",");
						Map<String, Object> checkOneCommodityCourseLiveMap = this.courseClassplanLivesService.checkOneCommodityCourseLive(ccle, classplanLiveIdArray);
						if(null != checkOneCommodityCourseLiveMap && null != checkOneCommodityCourseLiveMap.get("classplanLiveId")){
							String errorMsg = ++j+".同一商品下的不同课程的课次冲突！"+ checkOneCommodityCourseLiveMap.get("dayTime") + " " 
									+ DateTimeConstant.TIME_BUCKET[(Integer)checkOneCommodityCourseLiveMap.get("timeBucket")] + " 的"
									+ ccle.getClassplanLiveName() + "与排课计划【" + checkOneCommodityCourseLiveMap.get("classplanName") + "】的直播课【" 
									+ checkOneCommodityCourseLiveMap.get("classplanLiveName") + "】冲突！";
				//			throw new ClassplanLiveException(errorMsg);
							stb.append(errorMsg + "<br/>");		
						}
					}*/
                }
                if (coursesEntity.getCourseEq() == 1) {//课程可冲突
                    //确定该课程在哪些商品下
                    List<Long> goodsInfoIds = goodsInfoListByCourseId(ccle.getCourseId(), ccle.getSchoolId());
                    //遍历商品
                    for (Long goodsInfoId : goodsInfoIds) {
                        //确定某个商品下的课程
                        List<Long> courseIds = courseListByGoodsInfoId(goodsInfoId, ccle.getSchoolId());
                        for (Long courseId : courseIds) {
                            map.put("courseId", courseId);
                            map.put("schoolId", ccle.getSchoolId());
                            coursesEntity = coursesService.queryObject(map);
                            if (coursesEntity.getCourseEq() == 0) {
                                ccle.setCourseId(courseId);
                                Map<String, Object> checkOneCommodityCourseLiveMap = this.courseClassplanLivesService.checkOneCommodityCourseLive(ccle);
                                if (null != checkOneCommodityCourseLiveMap && null != checkOneCommodityCourseLiveMap.get("classplanLiveId")) {
                                    String errorMsg = ++j + ".同一商品下的不同课程的课次冲突！" + checkOneCommodityCourseLiveMap.get("dayTime") + " "
                                            + DateTimeConstant.TIME_BUCKET[(Integer) checkOneCommodityCourseLiveMap.get("timeBucket")] + " 的"
                                            + ccle.getClassplanLiveName() + "与排课计划【" + checkOneCommodityCourseLiveMap.get("classplanName") + "】的直播课【"
                                            + checkOneCommodityCourseLiveMap.get("classplanLiveName") + "】冲突！";
                                    //			throw new ClassplanLiveException(errorMsg);
                                    stb.append(errorMsg + "<br/>");
                                }
                            }
                        }
                    }
                }


            }
        }
        return stb.toString();
    }

    public String checkLive2(ClassplanPOJO courseClassplan) {
        StringBuffer stb = new StringBuffer();
        //校验同一商品下的不同课程的课次冲突
        return null;

    }


    //修改资料库
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateMaterial(ClassplanPOJO courseClassplan) {
        if (null != courseClassplan) {
            //资料库ID
            Long materialId = courseClassplan.getMaterialId();
            //资料库ids
            String materialIds = courseClassplan.getMaterialIds();
            //平台ID
            String schoolId = courseClassplan.getSchoolId();
            //排课ID
            String classplanId = courseClassplan.getClassplanId();

            if (StringUtils.isNotBlank(schoolId) && StringUtils.isNotBlank(classplanId)) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("materialId", materialId);
                map.put("materialIds", materialIds);
                map.put("schoolId", schoolId);
                map.put("classplanId", classplanId);
                this.courseClassplanDao.updateMaterial(map);
            }
        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void update(ClassplanPOJO courseClassplan) {
        //排课冲突信息抛出   2018-05-19 去掉排课冲突校验
		/*String errMsg = checkLive(courseClassplan);
		if(StringUtils.isNotBlank(errMsg)){
			throw new ClassplanLiveException(errMsg);
		}*/
        //修改时间
        courseClassplan.setModifiedTime(new Date());
        //en
        CourseClassplanEntity en = ClassplanPOJO.getEntity(courseClassplan);
        //保存主表修改
        courseClassplanDao.update(en);
        //排课发送到MQ
        msgPro2KdCourseClassplanService.pushToMessageQueue(en);
        //遍历子表
        List<ClassplanLivePOJO> detatilList = courseClassplan.getDetailList();
        //用于存放被删除的子表id集合
        List<String> delIds = new ArrayList<String>();
        if (null != detatilList && detatilList.size() > 0) {
            for (int i = 0; i < detatilList.size(); i++) {
                //pojo
                ClassplanLivePOJO clp = detatilList.get(i);
                //entity
                CourseClassplanLivesEntity ccle = ClassplanLivePOJO.getEntity(clp);
                //pk
                ccle.setClassplanId(en.getClassplanId());
                //schoolId
                ccle.setSchoolId(en.getSchoolId());
                //创建人
                ccle.setCreatePerson(en.getCreator());
                //修改人
                ccle.setModifyPerson(en.getModifier());
                //course_id
                ccle.setCourseId(en.getCourseId());
                //上传的文件名
                ccle.setFileName(clp.getFileName());
                //上期复习文件名
                ccle.setReviewName(clp.getReviewName());
                //本期预习文件名
                ccle.setPrepareName(clp.getPrepareName());
                //课程资料文件名
                ccle.setCoursewareName(clp.getCoursewareName());
                //ccle.setClassplanLiveId(UUID.randomUUID().toString());
                //排序
                ccle.setOrderNum(i);
                ccle.setDayTime(DateUtils.dateClear(ccle.getStartTime()));
                if(StringUtils.isBlank(ccle.getClassplanLiveId())){//保存
                    courseClassplanLivesService.save(ccle);
                }else{//更新
                    //判断课次是否有变动再更新
                    if (checkClassLiveDetail(ccle)) {
                        courseClassplanLivesService.update(ccle);
                    }
                }
                delIds.add(ccle.getClassplanLiveId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classplanLiveIds", delIds);
        map.put("classplanId", en.getClassplanId());
//		System.out.println("==========删除了：" + map);
        courseClassplanLivesService.deleteBatchNotIn(map);
    }

    @Override
    public void update(CourseClassplanEntity en, List<CourseClassplanLivesEntity> detatilList) {
        //保存主表修改
        courseClassplanDao.update(en);
        //用于存放被删除的子表id集合
        List<String> delIds = new ArrayList<String>();
        if (null != detatilList && detatilList.size() > 0) {
            for (int i = 0; i < detatilList.size(); i++) {
                //entity
                CourseClassplanLivesEntity ccle = detatilList.get(i);
                if (StringUtils.isBlank(ccle.getClassplanLiveId())) {//保存
                    courseClassplanLivesService.save(ccle);
                } else {//更新
                    //判断课次是否有变动再更新
                    if (checkClassLiveDetail(ccle)) {
                        ccle.setReview(null);
                        ccle.setPrepare(null);
                        ccle.setCourseware(null);
                        ccle.setFileUrl(null);
                        courseClassplanLivesService.update(ccle);
                    }
                }
                delIds.add(ccle.getClassplanLiveId());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classplanLiveIds", delIds);
        map.put("classplanId", en.getClassplanId());
        courseClassplanLivesService.deleteBatchNotIn(map);
    }

    private boolean checkClassLiveDetail(CourseClassplanLivesEntity ccle) {
        boolean flag = false;
        Map<String, Object> map = new HashMap<>();
        map.put("classplanLiveId", ccle.getClassplanLiveId());
        CourseClassplanLivesEntity oldEntity = courseClassplanLivesService.queryObject(map);
        //直播课名称
        if (!ccle.getClassplanLiveName().equals(oldEntity.getClassplanLiveName())) {
            flag = true;
        }
        //即将开始时间
        if (!ccle.getReadyTime().equals(oldEntity.getReadyTime())) {
            flag = true;
        }
        //开始时间
        if (!ccle.getStartTime().equals(oldEntity.getStartTime())) {
            flag = true;
        }
        //结束时间
        if (!ccle.getEndTime().equals(oldEntity.getEndTime())) {
            flag = true;
        }
        //进入直播结束时间
        if (!ccle.getCloseTime().equals(oldEntity.getCloseTime())) {
            flag = true;
        }
        //上课时段
        if (!ccle.getTimeBucket().equals(oldEntity.getTimeBucket())) {
            flag = true;
        }
        //直播室
        if (ccle.getStudioId() == null && oldEntity.getStudioId() == null) {

        } else if (!ccle.getStudioId().equals(oldEntity.getStudioId())) {
            flag = true;
        }
        //直播间
        if (ccle.getLiveroomId() == null && oldEntity.getLiveroomId() == null) {

        } else if (!ccle.getLiveroomId().equals(oldEntity.getLiveroomId())) {
            flag = true;
        }
        //回放地址
        if (StringUtils.isBlank(ccle.getBackUrl()) && StringUtils.isBlank(oldEntity.getBackUrl())) {

        } else if (!ccle.getBackUrl().equals(oldEntity.getBackUrl())) {
            flag = true;
        }
        //授课老师
        if (!ccle.getTeacherId().equals(oldEntity.getTeacherId())) {
            flag = true;
        }
        //文件上传
        if (StringUtils.isBlank(ccle.getFileUrl()) && StringUtils.isBlank(oldEntity.getFileUrl())) {

        } else if (!ccle.getFileUrl().equals(oldEntity.getFileUrl())) {
            flag = true;
        }
        //考勤
        if (ccle.getAttendance() != (oldEntity.getAttendance())) {
            flag = true;
        }


        //app4.0.1撤销内容
        //上期复习
        if (StringUtils.isBlank(ccle.getReview()) && StringUtils.isBlank(oldEntity.getReview())) {

        } else if (!ccle.getReview().equals(oldEntity.getReview())) {
            flag = true;
        }
        //本次预习
        if (StringUtils.isBlank(ccle.getPrepare()) && StringUtils.isBlank(oldEntity.getPrepare())) {

        } else if (!ccle.getPrepare().equals(oldEntity.getPrepare())) {
            flag = true;
        }
        //课堂资料
        if (StringUtils.isBlank(ccle.getCourseware()) && StringUtils.isBlank(oldEntity.getCourseware())) {

        } else if (!ccle.getCourseware().equals(oldEntity.getCourseware())) {
            flag = true;
        }
        //阶段
        if (StringUtils.isBlank(ccle.getExamStageId()) && StringUtils.isBlank(oldEntity.getExamStageId())) {

        } else if (!ccle.getExamStageId().equals(oldEntity.getExamStageId())) {
            flag = true;
        }

        // 禁言
        if (ccle.getBanSpeaking() != (oldEntity.getBanSpeaking())) {
            flag = true;
        }
        // 禁止问答
        if (ccle.getBanAsking() != (oldEntity.getBanAsking())) {
            flag = true;
        }
        // 隐藏讨论模块
        if (ccle.getHideDiscussion() != (oldEntity.getHideDiscussion())) {
            flag = true;
        }
        // 隐藏问答模块
        if (ccle.getHideAsking() != (oldEntity.getHideAsking())) {
            flag = true;
        }

        //班型权限
        if (StringUtils.isBlank(ccle.getLiveClassTypeIds()) && StringUtils.isBlank(oldEntity.getLiveClassTypeIds())) {

        } else if (!("," + ccle.getLiveClassTypeIds() + ",").equals(oldEntity.getLiveClassTypeIds())) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void delete(Long classplanId) {
        courseClassplanDao.delete(classplanId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deleteBatch(Map<String, Object> map) {
        courseClassplanDao.deleteBatch(map);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void pause(String[] classplanIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", classplanIds);
        map.put("status", Constant.Status.PAUSE.getValue());
        courseClassplanDao.updateBatch(map);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void resume(String[] classplanIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", classplanIds);
        map.put("status", Constant.Status.RESUME.getValue());
        courseClassplanDao.updateBatch(map);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void end(String[] classplanIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", classplanIds);
        map.put("status", Constant.Status.END.getValue());
        courseClassplanDao.updateBatch(map);
    }

    @Override
    public List<Map<String, Object>> addItem(Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<>();
        //将主表中相同的字段传递给子表
        Long classplanId = (Long) map.get("classplanId");//主表排课计划id
        Long courseId = (Long) map.get("courseId");//主表课程id
        Long timetableId = (Long) map.get("timetableId");//主表上课时点id
        String startTime = (String) map.get("startTime");//主表开课日期
        Long studioId = (Long) map.get("studioId");//主表直播室id
        Long liveRoomId = (Long) map.get("liveRoomId");//主表直播间id
        Long teacherId = (Long) map.get("teacherId");//主表授课老师id
        String schoolId = (String) map.get("schoolId");//主表schoolId

        String readyTimeStr = (String) map.get("readyTime");//即将开始时间
        int readyTime = Integer.parseInt(readyTimeStr);//即将开始时间转为int类型
        String closeTimeStr = (String) map.get("closeTime");//进入直播间结束时间
        int closeTime = Integer.parseInt(closeTimeStr);//进入直播间结束时间转为int类型

        //课程子表
        Map<String, Object> corseQueryMap = new HashMap<>();
        corseQueryMap.put("schoolId", schoolId);
        corseQueryMap.put("courseId", courseId);
        List<CourseLiveDetailsEntity> courseLiveDetailsEntities = courseLiveDetailsService.queryListByCouresId(corseQueryMap);
        //判断该课程的子表list集合是否为空
        if (null != courseLiveDetailsEntities && !courseLiveDetailsEntities.isEmpty()) {
            List<TimeTableDetailEntity> tableDetailEntities = timeTableDetailService.queryObject(timetableId);
            //查询直播室名称
            Map<String, Object> studioMap = new HashMap<String, Object>();
            studioMap.put("schoolId", schoolId);
            studioMap.put("studioId", studioId);
            MallStudioEntity mallStudioEntity = this.mallStudioService.queryObject(studioMap);
            String studioName = "";
            if (null != studioName) studioName = mallStudioEntity.getStudioName();

            //查询直播间名称
            Map<String, Object> liveRoomSMap = new HashMap<String, Object>();
            liveRoomSMap.put("schoolId", schoolId);
            liveRoomSMap.put("liveRoomId", liveRoomId);
            MallLiveRoomEntity mallLiveRoomEntity = this.mallLiveRoomService.queryObject(liveRoomSMap);
            String liveRoomName = "";
            if (null != liveRoomName) liveRoomName = mallLiveRoomEntity.getLiveRoomName();

            //查询授课老师
            Map<String, Object> teacherMap = new HashMap<String, Object>();
            teacherMap.put("teacherId", teacherId);
            teacherMap.put("schoolId", schoolId);
            SysUserEntity sysUserEntity = this.sysUserService.queryTeacherById(teacherMap);
            String teacherName = "";
            if (null != teacherName) teacherName = sysUserEntity.getUsername();

            for (int i = 0; i < courseLiveDetailsEntities.size(); i++) {
                CourseLiveDetailsEntity courseLiveDetailsEntity = courseLiveDetailsEntities.get(i);
                Map<String, Object> liveMap = new HashMap<>();
                liveMap.put("courseLiveDetailId", courseLiveDetailsEntity.getLiveId());//课程子表直播课次id
                liveMap.put("classplanLiveName", courseLiveDetailsEntity.getLiveName());//直播课程名称
                liveMap.put("liveClassTypeIds", courseLiveDetailsEntity.getLiveClassTypeIds());//班型权限
                //加载上期复习,阶段等字段
                liveMap.put("reviewName",courseLiveDetailsEntity.getReviewName());
                liveMap.put("prepareName",courseLiveDetailsEntity.getPrepareName());
                liveMap.put("coursewareName",courseLiveDetailsEntity.getCoursewareName());
                liveMap.put("review",courseLiveDetailsEntity.getReviewUrl());
                liveMap.put("prepare",courseLiveDetailsEntity.getPrepareUrl());
                liveMap.put("review", courseLiveDetailsEntity.getReviewUrl());
                liveMap.put("prepare", courseLiveDetailsEntity.getPrepareUrl());
                liveMap.put("courseware", courseLiveDetailsEntity.getCoursewareUrl());
                liveMap.put("examStageId", courseLiveDetailsEntity.getExamStageId());
                liveMap.put("phaseName", courseLiveDetailsEntity.getExamStageName());
                liveMap.put("courseware",courseLiveDetailsEntity.getCoursewareUrl());
                liveMap.put("examStageId",courseLiveDetailsEntity.getExamStageId());
                liveMap.put("phaseName",courseLiveDetailsEntity.getExamStageName());
                liveMap.put("studioId", studioId);//直播室PK
                liveMap.put("studioName", studioName);//直播室
                liveMap.put("liveroomId", liveRoomId);//直播间PK
                liveMap.put("liveRoomName", liveRoomName);//直播间
                liveMap.put("teacherId", teacherId);//授课老师PK
                liveMap.put("teacherName", teacherName);//授课老师
                liveMap.put("classplanId", classplanId);//主表id

                liveMap.put("classplanLiveTimeDetail",null);
                liveMap.put("startTime", null);
                liveMap.put("endTime", null);
                liveMap.put("timeBucket", null);//时段0.上午;1.下午;2.晚上
                //TODO
                liveMap.put("readyTime", null);
                liveMap.put("closeTime", null);

                list.add(liveMap);
            }
            //判断上课时点子表list集合是否为空
            if(null != tableDetailEntities && !tableDetailEntities.isEmpty()){
                List<Map<String, Object>> dateList = new ArrayList<>(courseLiveDetailsEntities.size());
                Date today = DateUtils.parse(startTime);//将主表开课日期转换成Date类型
                today = DateUtils.getDateBefore(today, 1);//将Date类型的开课日期减一天
                do {
                    today = DateUtils.getDateAfter(today, 1);//将Date类型的开课日期加一天
                    for(TimeTableDetailEntity timeTableDetailEntity : tableDetailEntities){
                        if(timeTableDetailEntity.getWeek() == DateUtils.getWeek(today)){//判断排课主表的开课日期对应的星期是否与遍历后的上课时点子表的上课时间（星期）对应
                            Map<String, Object> dateMap = new HashMap<String, Object>();
                            dateMap.put("date", today);//上课时间（星期）
                            dateMap.put("data", timeTableDetailEntity);//上课时点子表
                            dateList.add(dateMap);
                        }
                    }
                } while (dateList.size() < list.size());

                for(int i = 0 ; i< list.size() ;i++){
                    Map<String, Object> liveMap = list.get(i);
                    Map<String, Object> dateMap = dateList.get(i);
                    Date date = (Date) dateMap.get("date");
                    TimeTableDetailEntity timeTableDetailEntity = (TimeTableDetailEntity) dateMap.get("data");
                    Integer startHHMM[] = timetableTimeFormate(timeTableDetailEntity.getStartTime());//将开始时间String类型（HH:mm）按“:”拆分
                    Integer endHHMM[] = timetableTimeFormate(timeTableDetailEntity.getEndTime());//将结束时间String类型（HH:mm）按“:”拆分

                    Date startDate = DateUtils.setHour(startHHMM[0], DateUtils.setMinute(startHHMM[1], date));//将开始时间String类型（HH:mm）转为Date类型（yyyy-MM-dd HH:mm）
                    Date endDate = DateUtils.setHour(endHHMM[0], DateUtils.setMinute(endHHMM[1], date));//将结束时间String类型（HH:mm）转为Date类型（yyyy-MM-dd HH:mm）
                    Date readyDate = DateUtils.addMinute(-readyTime, startDate);//
                    Date closeDate = DateUtils.addMinute(closeTime, endDate);
//					liveMap.put("classplanLiveTimeDetail",classplanLiveTimeDetailFormate(startDate, endDate));

                    liveMap.put("readyTime", readyDate);//即将开始时间
                    liveMap.put("closeTime", closeDate);//进入直播间结束时间
                    liveMap.put("startTime", startDate);//直播开始时间
                    liveMap.put("endTime", endDate);//直播结束时间
                    liveMap.put("timeBucket", timeTableDetailEntity.getTimeBucket());//时段0.上午;1.下午;2.晚上
                    liveMap.put("timeBucketName", DateTimeConstant.TIME_BUCKET[timeTableDetailEntity.getTimeBucket()]);//时段0.上午;1.下午;2.晚上
                }
            }

        }

        return list;
    }

//	public static String classplanLiveTimeDetailFormate(Date sdate , Date eDate){
//		String formate = "[开始时间]-[结束时间]";
//		String startFormate = "yyyy-MM-dd HH:mm" ;
//		String endFormate = "HH:mm" ;
//		
//		return formate.replace("[开始时间]", DateUtils.format(sdate , startFormate)).replace("[结束时间]", DateUtils.format(sdate , endFormate));
//	}

    public static Integer[] timetableTimeFormate(String time) {
        Integer[] times = {0, 0};
        if (StringUtils.isNotBlank(time)) {
            String s[] = time.split(":");
            times[0] = Integer.valueOf(s[0]);
            times[1] = Integer.valueOf(s[1]);
        }
        return times;
    }

    /**
     * 根据学员规划子表id查询排课计划下拉列表
     */
    @Override
    public List<Map<String, Object>> queryListByUserplanDetailId(Map<String, Object> map) {
        return this.courseClassplanDao.queryListByUserplanDetailId(map);
    }

    /**
     * 审核通过
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void accept(String classplanId, Long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classplanId", classplanId);
        map.put("isAudited", Constant.Status.RESUME.getValue());
        courseClassplanDao.audited(map);
        courseClassplanChangeRecordService.saveAuait(classplanId, userId);
    }

    /**
     * 审核未过
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void reject(String classplanId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classplanId", classplanId);
        map.put("isAudited", Constant.Status.PAUSE.getValue());
        courseClassplanDao.audited(map);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void unAudit(String classplanId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classplanId", classplanId);
        map.put("isAudited", Constant.Status.END.getValue());
        courseClassplanDao.audited(map);
    }

    @Override
    public int checkClassType(long id) {
        return this.courseClassplanDao.checkClassType(id);
    }

    @Override
    public List<CourseClassplanEntity> queryList1(Map<String, Object> map) {
        return this.courseClassplanDao.queryList1(map);
    }

    @Override
    public int queryTotal1(Map<String, Object> map) {
        return this.courseClassplanDao.queryTotal1(map);
    }

    @Override
    public List<Object> queryUserList(Map<String, Object> map) {
        return this.courseClassplanDao.queryUserList(map);
    }

    @Override
    public int queryUserListTotal(Map<String, Object> map) {
        return this.courseClassplanDao.queryUserListTotal(map);
    }

    @Override
    public int findCId(Map<String, Object> map) {
        return this.courseClassplanDao.findCId(map);
    }


    /**
     * 根据课程id确定该课程在哪些商品下
     *
     * @param courseId 课程id
     * @param schoolId 平台id
     * @return
     */
    private List<Long> goodsInfoListByCourseId(Long courseId, String schoolId) {
        List<Long> goodsInfoList = null;
        goodsInfoList = mallGoodsDetailsService.getGoods(courseId, schoolId);
        return goodsInfoList;
    }

    /**
     * 根据商品id获取该商品下的课程
     *
     * @param goodsInfoId 商品id
     * @param schoolId    平台id
     * @return
     */
    private List<Long> courseListByGoodsInfoId(Long goodsInfoId, String schoolId) {
        List<Long> courseList = null;
        courseList = mallGoodsDetailsService.getCourses(goodsInfoId, schoolId);
        return courseList;
    }

    /**
     * 根据课程id获取该课程下的课次
     *
     * @param courseId 课程id
     * @param schoolId 平台id
     * @return
     */
    private List<String> courseLiveListByCourseId(Long courseId, String schoolId) {
        List<String> courseLiveList = null;
        courseLiveList = courseClassplanLivesService.getCourseLives(courseId, schoolId);
        return courseLiveList;
    }
//	public static void main(String[] args) {
//		System.out.println(classplanLiveTimeDetailFormate(new Date(), new Date()));;
//	}

    @Override
    public List<Map<String, Object>> queryClassPlanForQueue(Map<String, Object> map) {
        List<Map<String, Object>> list = courseClassplanDao.queryListMap(map);

//		List<Map<String, Object>> list = this.courseUserplanDetailDao.courseListByUserPlanId(map);
        String schoolId = (String) map.get("schoolId");
        String millisecond = (String) map.get("millisecond");
        if (null != list && list.size() > 0) {
            for (Map<String, Object> map2 : list) {
                Map<String, Object> mapp = new HashMap<String, Object>();
                String id = (String) map2.get("id");
                mapp.put("id", id);
                mapp.put("schoolId", schoolId);
                mapp.put("millisecond", millisecond);
                Map<String, Object> map3 = this.courseClassplanDao.queryOtherById(mapp);
                if (null != map3) {
                    map2.putAll(map3);
                }
            }
        }
        return list;
    }

    /**
     * 查询排课 根据排课ID
     */
    @Override
    public CourseClassplanEntity queryObjectByClassplanId(String classplanId) {
        return this.courseClassplanDao.queryObjectByClassplanId(classplanId);
    }

}
