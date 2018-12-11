package io.renren.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javax.servlet.http.HttpServletRequest;

import io.renren.service.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.AbstractNestablePropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.serializer.SerializerFeature;

import io.renren.common.doc.ParamKey;
import io.renren.constant.MaterialProperty;
import io.renren.constant.MaterialType;
import io.renren.entity.*;
import io.renren.entity.ContractTemplateEntity;import io.renren.mongo.dao.ICourseDao;
import io.renren.mongo.entity.ChapterEntity;
import io.renren.mongo.entity.CourseEntity;
import io.renren.mongo.entity.MongoPagination;
import io.renren.mongo.entity.SectionEntity;
import io.renren.pojo.CourseUserplanPOJO;
import io.renren.pojo.PhasePOJO;
import io.renren.pojo.classplan.ClassplanLivePOJO;
import io.renren.pojo.examschedule.ExamSchedulePOJO;
import io.renren.pojo.timetable.TimeTablePOJO;
import io.renren.service.ContractTemplateService;import io.renren.utils.EncryptionUtils;
import io.renren.utils.HttpUtils;
import io.renren.utils.JSONUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


/**
 * 弹窗基础数据
 * @class io.renren.controller.CommonController.java
 * @Description:
 * @author shihongjie
 * @dete 2017年4月8日
 */
@Controller
@RequestMapping("/layerdata")
public class LayerDataController extends AbstractController {

    @Autowired
    private MallAreaService mallAreaService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private MallProfessionService mallProfessionService;
    @Autowired
    private MallLevelService mallLevelService;
    @Autowired
    private TimetableService timetableService;
    @Autowired
    private MallStudioService mallStudioService;
    @Autowired
    private MallLiveRoomService mallLiveRoomService;
    @Autowired
    private MallClassTypeService mallClassTypeService;
    @Autowired
    private MallExamScheduleService mallExamScheduleService;
    @Autowired
    private CoursesService coursesService;
    @Autowired
    private CourseUserplanService courseUserplanService;
    @Autowired
    private MallClassService mallClassService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private MallGoodsInfoService mallGoodsInfoService;
    @Autowired
    private CourseClassplanService courseClassplanService;
    @Autowired
    private CourseClassplanLivesService courseClassplanLivesService;
    //	@Autowired
    private ICourseDao courseDao;

    @Autowired
    private ContractTemplateService contractTemplateService;
    @Autowired
    private MaterialDetailService materialDetailService;
    @Autowired
    private CourseMaterialService courseMaterialService;
    @Autowired
    private CourseMaterialTypeService courseMaterialTypeService;

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SysProductService sysProductService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private AppCityService appCityService;
    @Autowired
    private AppLabelService appLabelService;

    @Autowired
    private CourseAbnormalRegistrationService courseAbnormalRegistrationService;
    @Autowired
    private PushTimeTemplateService pushTimeTemplateService;

    @Autowired
    private CourseOliveAuthorityService courseOliveAuthorityService;


    @Value("#{application['adaptive.learning.domain']}")
    private String adaptiveLearningDomain = "";

    /**
     * 地区（ID和地区名称）
     * */
    @ResponseBody
    @RequestMapping("/appCityList")
    public List<AppCity> appCityList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        List<AppCity> appCities = appCityService.queryList(map);
        return appCities;
    }

    /**
     * 用户标签（ID和标签名称）
     * */
    @ResponseBody
    @RequestMapping("/appLabelList")
    public R appLabelList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQueryDecodeUTF8(map, request, "labelName");
        longQuery(map, request, "productId");
        PageUtils pageUtil = new PageUtils(
                appLabelService.queryList(map),
                appLabelService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 用户信息（ID和用户姓名）
     * */
    @ResponseBody
    @RequestMapping("/userInfoList")
    public R userInfoList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "mobile");
        PageUtils pageUtil = new PageUtils(
                usersService.queryList(map),
                usersService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 商品信息
     * */
    @ResponseBody
    @RequestMapping("/goodsInfoList")
    public R goodsInfoList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "commodityName");
        longQuery(map, request, "commodityId");
        PageUtils pageUtil = new PageUtils(
                mallGoodsInfoService.queryLayList(map),
                mallGoodsInfoService.queryTotal1(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 商品ID查詢省份信息
     * */
    @ResponseBody
    @RequestMapping("/areaGoodsList")
    public R areaGoodsList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
//		map.put("goodsId",goodsId);
        longQuery(map, request, "goodsId");
        PageUtils pageUtil = new PageUtils(
                mallAreaService.findAreaGoodsList(map),
                mallAreaService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 班级列表
     * */
    @ResponseBody
    @RequestMapping("/classList")
    public R classList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "className");
        stringQuery(map, request, "teacherName");
        PageUtils pageUtil = new PageUtils(
                mallClassService.queryPojoList(map),
//				mallClassService.queryList(map), 
                mallClassService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 班型列表
     * */
    @ResponseBody
    @RequestMapping("/classTypeList")
    public R classTypeList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        PageUtils pageUtil = new PageUtils(
                mallClassTypeService.queryList(map),
                mallClassTypeService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 地区列表
     * */
    @ResponseBody
    @RequestMapping("/areaList")
    public R areaList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        PageUtils pageUtil = new PageUtils(
                mallAreaService.queryList(map),
                mallAreaService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 地区列表 包含全部【id=0，name=全部】
     * */
    @ResponseBody
    @RequestMapping("/areaAllList")
    public R areaAllList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        List<MallAreaEntity> queryList = mallAreaService.queryList(map);
        if(null != queryList){
            MallAreaEntity area = new MallAreaEntity();
            area.setAreaId(0l);
            area.setAreaName("全部");
            queryList.add(0,area);
        }
        int queryTotal = mallAreaService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(
                queryList,
                queryTotal+1,
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 专业列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/professionList")
    public R professionList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        PageUtils pageUtil = new PageUtils(
                mallProfessionService.queryList(map),
                mallProfessionService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 专业列表 包含全部【id=0，name=全部】
     * @return
     */
    @ResponseBody
    @RequestMapping("/professionAllList")
    public R professionAllList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        List<MallProfessionEntity> pofessionList=this.mallProfessionService.queryList1(map);
        if(null != pofessionList){
            MallProfessionEntity profession = new MallProfessionEntity();
            profession.setProfessionId(0l);
            profession.setProfessionName("全部");
            pofessionList.add(0,profession);
        }
        int total = this.mallProfessionService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(
                pofessionList,
                total+1,
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 层次列表 包含全部【id=0，name=全部】
     * @return
     */
    @ResponseBody
    @RequestMapping("/levelAllList")
    public R levelAllList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        List<MallLevelEntity> list = this.mallLevelService.queryList(map);
        if(null != list){
            MallLevelEntity level = new MallLevelEntity();
            level.setLevelId(0l);
            level.setLevelName("全部");
            list.add(0,level);
        }
        int total = this.mallLevelService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(list, total+1, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 层次列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/levelList")
    public R levelList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        List<MallLevelEntity> list = this.mallLevelService.queryList(map);
        int total = this.mallLevelService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 合同模板列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/contractTemplateList")
    public R contractTemplateList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        List<ContractTemplateEntity> list = this.contractTemplateService.queryList(map);
        int total = this.contractTemplateService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 课程列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/courseList")
    public R courseList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "courseName");
        List<CoursesEntity> list = this.coursesService.queryList(map);

        int total = this.coursesService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 考试时间
     * @return
     */
    @ResponseBody
    @RequestMapping("/examScheduleList")
    public R examScheduleList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "scheduleName");
        List<ExamSchedulePOJO> list = this.mallExamScheduleService.queryPojoList1(map);
        int total = this.mallExamScheduleService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 学员规划列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/userplanList")
    public R userplanList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "orderNo");
        stringQuery(map, request, "userMobile");
        stringQuery(map, request, "className");
        stringQuery(map, request, "teacherName");
        longQuery(map, request, "examScheduleId");
        List<CourseUserplanPOJO> list = this.courseUserplanService.queryList2(map);
        int total = this.courseUserplanService.queryTotal2(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 课程信息列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/coursesList")
    public R coursesList(HttpServletRequest request){
//		Map<String, Object> map = getMapPage(request);
        String name = request.getParameter("courseName");
        MongoPagination pageUtil = null;
        int total = 0;
        Integer pageNo = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
        Integer pageSize = ServletRequestUtils.getIntParameter(request,  ParamKey.In.LIMIT, ParamKey.In.DEFAULT_MAX_LIMIT);

        if (null!=name && !name.trim().equals("")) {
            pageUtil = this.courseDao.findCourseByName(pageNo, pageSize,name);
            total = this.courseDao.queryTotalByName(name);
        }else{
            pageUtil = this.courseDao.findAllCourse(pageNo, pageSize);
            total = this.courseDao.queryTotal();
        }

        //PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 知识点课程树状信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/perms")
    public R perms(HttpServletRequest request){
//		Map<String, Object> map = getMapPage(request);
        String id = request.getParameter("id");
        //id = "N_1001A5100000000DYMHJ";
        CourseEntity cen = this.courseDao.findCourseById(id);
        List<SysMenuEntity> menuList = new ArrayList<SysMenuEntity>();
        if (null!=cen) {
            //转换操作

            change(menuList,cen);
        }



        return R.ok().put(ParamKey.Out.data, menuList);
    }

    /**
     * 课程菜单 转换操作
     * @param menuList
     * @param cen
     */
    private void change(List<SysMenuEntity> menuList, CourseEntity cen) {
        // TODO Auto-generated method stub

        SysMenuEntity sysMenu = new SysMenuEntity();
        sysMenu.setName(cen.getName());
        Long idx = new Long(1);
        sysMenu.setParentId(new Long(0));
        sysMenu.setMenuId(idx);
        sysMenu.setPerms(cen.getNc_id());

        menuList.add(sysMenu);
        idx++;
        if (cen.getChapter_number()>0) {
            SysMenuEntity sysMenuEntity = null;
            for (int i = 0; i < cen.getChapter().size(); i++) {
                ChapterEntity cr = cen.getChapter().get(i);
                sysMenuEntity = new SysMenuEntity();
                sysMenuEntity.setName(cr.getName());
                sysMenuEntity.setPerms(cr.getNc_course_id()+"#"+cr.getNc_id());

                sysMenuEntity.setParentId(sysMenu.getMenuId());
                sysMenuEntity.setMenuId(idx);
                menuList.add(sysMenuEntity);
                idx++;

                //
                if(null!=cr.getSection()){
                    SysMenuEntity sme = null;
                    for(int z=0;z<cr.getSection().size();z++){
                        SectionEntity se = cr.getSection().get(z);
                        sme = new SysMenuEntity();
                        sme.setName(se.getName());
                        sme.setPerms(se.getNc_course_id()+"#"+se.getNc_chapter_id()+"#"+se.getNc_id());
                        sme.setParentId(sysMenuEntity.getMenuId());
                        sme.setMenuId(idx);
                        menuList.add(sme);

                        idx++;

                    }
                }
            }
        }

    }

    /**
     * 直播间列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/liveroomList")
    public R liveroomList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "liveRoomName");
        List<MallLiveRoomEntity> list = this.mallLiveRoomService.queryListForCourseOLive(map);
        int total = this.mallLiveRoomService.queryTotal1(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 授课老师列表
     */
    @ResponseBody
    @RequestMapping("/teacherList")
    public R teacherList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        String nickName = null;
        Long classTeacher = null;
        Long teacher = null;
        try {
            if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "nickName",null))){
                nickName = ServletRequestUtils.getStringParameter(request, "nickName", null);
            }
            if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "classTeacher",null))){
                classTeacher = ServletRequestUtils.getLongParameter(request, "classTeacher");
            }
            if(StringUtils.isNotBlank(ServletRequestUtils.getStringParameter(request, "teacher",null))){
                teacher = ServletRequestUtils.getLongParameter(request, "teacher");
            }
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }

        List<SysUserEntity> list = this.sysUserService.findTeacherList(
                nickName, teacher, classTeacher,
                (Integer)map.get(ParamKey.In.OFFSET), (Integer)map.get(ParamKey.In.LIMIT)
        );
        int total = this.sysUserService.findTeacherCount(nickName, teacher, classTeacher);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 直播室列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/studioList")
    public R sudioList(HttpServletRequest request ){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "studioName");
        List<MallStudioEntity> list = this.mallStudioService.queryList1(map);
        int total = this.mallStudioService.queryTotal1(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 阶段列表
     * @return
     * @throws ServletRequestBindingException
     */
    @ResponseBody
    @RequestMapping("/phaseList")
    public R phaseList(HttpServletRequest request ) throws ServletRequestBindingException{

        String classPlanId = ServletRequestUtils.getStringParameter(request, "classPlanId","");
        Map<String, Object> map = getMapPage(request);
        Integer page= (((Integer)map.get("offset"))+10)/10;

        if(StringUtils.isBlank(classPlanId)){
            return R.error("不存在排课ID");
        }
        CoursesEntity courseEntity = new CoursesEntity();
        //根据classPlanId的长度判断是courseId还是classPlanId,courseId纯数字
        this.logger.error("请求的参数classPlan的值是:{}",classPlanId);
        if (classPlanId.length() < 9 ){
            Map<String,Object> courseIdParam= new HashMap<String,Object>();
            courseIdParam.put("courseId", Long.valueOf(classPlanId));
//		System.out.println("classplanEntity========================333"+classplanEntity.getCourseId());
            //获取课程
            courseEntity = coursesService.queryObjectbyCourseId(courseIdParam);
        }else {
            //获取排课
            CourseClassplanEntity classplanEntity = courseClassplanService.queryObjectByClassplanId(classPlanId);
            if (null == classplanEntity && "".equals(classplanEntity)) {
                return R.error("不存在排课");
            }
//		System.out.println("classplanEntity========================222"+classplanEntity);
            Map<String, Object> courseIdParam = new HashMap<String, Object>();
            courseIdParam.put("courseId", classplanEntity.getCourseId());
//		System.out.println("classplanEntity========================333"+classplanEntity.getCourseId());
            //获取课程
            courseEntity = coursesService.queryObjectbyCourseId(courseIdParam);
        }
        stringQuery(map, request, "phaseName");
//		System.out.println("值====================================================444map:" + map);
        //获取阶段列表
        String url = adaptiveLearningDomain+"/adlPhase/list?";
        String param = "courseId="+courseEntity.getCourseFk()+"&page="+page;
        String httpUrl = url + param;
//		System.out.println("http:"+httpUrl);
        this.logger.error("请求只适应获取阶段id的url:{}",httpUrl);
        String sr1 = HttpUtils.httpRequest(httpUrl);

//		System.out.println(sr1);
        //下面的sr用于模拟测试例子
        //String sr = "{'code':0,'data':{'totalCount':3,'pageSize':10,'totalPage':2,'currPage':1,'list':[{'phaseId':5,'courseId':1,'phaseName':'第一阶段','phaseNo':'1001','keyPoint':1,'versionNo':1,'versionHash':'1','status':1,'dr':0,'updateTime':'2018-01-1211:11:46','createTime':'2018-01-1110:00:54'},{'phaseId':6,'courseId':1,'phaseName':'对付对付个','phaseNo':'噢噢噢噢','keyPoint':1,'versionNo':1,'versionHash':'1','status':1,'dr':0,'updateTime':'2018-01-1110:31:34','createTime':'2018-01-1110:31:34'},{'phaseId':14,'courseId':1,'phaseName':'u3','phaseNo':'u3','keyPoint':1,'versionNo':0,'versionHash':'0','status':1,'dr':0,'updateTime':'2018-01-1610:37:39','createTime':'2018-01-1610:37:39'}]}}";
        List<PhasePOJO> resultList =new ArrayList<PhasePOJO>();
        //数据格式化
        Map<String, Object> map1=(Map<String, Object>) JSONUtil.jsonToMap(sr1).get("data");
        List<Map<String, String>> list=(List<Map<String, String>>) map1.get("list");
        for (Map<String, String> m : list)
        {
            String json1=JSONUtil.hashMapToJson(m);
            PhasePOJO pojo = JSONUtil.jsonToBean(json1,PhasePOJO.class);
            resultList.add(pojo);
        }

        int total = (int) map1.get("totalCount");
        PageUtils pageUtil = new PageUtils(resultList, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
	
	/*
	*//**
     * 阶段列表
     * @return
     *//*
	@ResponseBody
	@RequestMapping("/huoqu")
	public R phaseList1(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "phaseName");
		
		String url="http://localhost:8074/adlPhase/list?courseId=1";
//		String sr = HttpUtils.httpRequest(url);
		String sr = "{'code':0,'data':{'totalCount':3,'pageSize':10,'totalPage':2,'currPage':1,'list':[{'phaseId':5,'courseId':1,'phaseName':'第一阶段','phaseNo':'1001','keyPoint':1,'versionNo':1,'versionHash':'1','status':1,'dr':0,'updateTime':'2018-01-1211:11:46','createTime':'2018-01-1110:00:54'},{'phaseId':6,'courseId':1,'phaseName':'对付对付个','phaseNo':'噢噢噢噢','keyPoint':1,'versionNo':1,'versionHash':'1','status':1,'dr':0,'updateTime':'2018-01-1110:31:34','createTime':'2018-01-1110:31:34'},{'phaseId':14,'courseId':1,'phaseName':'u3','phaseNo':'u3','keyPoint':1,'versionNo':0,'versionHash':'0','status':1,'dr':0,'updateTime':'2018-01-1610:37:39','createTime':'2018-01-1610:37:39'}]}}";
		List<PhasePOJO> resultList =new ArrayList<PhasePOJO>();
        //数据格式化
		Map<String, Object> map1=(Map<String, Object>) JSONUtil.jsonToMap(sr).get("data");
        List<Map<String, String>> list=(List<Map<String, String>>) map1.get("list");
        for (Map<String, String> m : list)
        {
          String json1=JSONUtil.hashMapToJson(m);
          PhasePOJO pojo = JSONUtil.jsonToBean(json1,PhasePOJO.class);
          resultList.add(pojo);
        }
        
		int total = (int) map1.get("totalCount");
		PageUtils pageUtil = new PageUtils(resultList, total, request);
		return R.ok().put(ParamKey.Out.data, pageUtil);
	}*/

    /**
     * 上课时点列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/timetableList")
    public R timetableList(HttpServletRequest request ){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "name");
        List<TimeTablePOJO> list = this.timetableService.queryPojoList1(map);
        int total = this.timetableService.queryTotal1(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 排课列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/classplanList")
    public R classplanList(HttpServletRequest request ){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "classplanName");
        List<CourseClassplanEntity> list = this.courseClassplanService.queryList1(map);
        int total = this.courseClassplanService.queryTotal1(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 推送模板
     */
    @ResponseBody
    @RequestMapping("/pushTemplateList")
    public R pushTemplateList(HttpServletRequest request ){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "pushTemplateName");
        List<PushTimeTemplateEntity> list = this.pushTimeTemplateService.queryList(map);
        int total = this.pushTimeTemplateService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 排课明细列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/classplanLivesList")
    public R classplanLiveList(String classplanId, HttpServletRequest request ){
        Map<String, Object> map = new HashMap<>();
        if(null != classplanId) map.put("classplanId", classplanId);
        List<ClassplanLivePOJO> list = this.courseClassplanLivesService.queryPojoList(map);
        //int total = list != null?list.size():0;
        PageUtils pageUtil = new PageUtils(list, 0, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /** 资料列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/materialDetailList")
    public R materialDetailList(HttpServletRequest request ){
        Integer page = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
        Integer limit = ServletRequestUtils.getIntParameter(request,  "limit", 1);
        Map<String, Object> map = getMapPage(request,limit);
        String name = request.getParameter("name");
        String point_id = request.getParameter("point_id");

        String property = request.getParameter("property");
        String isRelevance = request.getParameter("isRelevance");
        String type = request.getParameter("type");
        String sTime = request.getParameter("sTime");


        map.put("name", name);
        map.put("point_id", point_id);
        if (null!=property && !property.trim().equals("")) {
            map.put("property", MaterialProperty.isMaterialProperty(property).getValue());
        }

        map.put("isRelevance", isRelevance);
        map.put("sTime", sTime);
        if (null!=type && !type.trim().equals("")) {
            map.put("type", MaterialType.isMaterialType(type).getValue());

        }

        List<MaterialDetailEntity> list = this.materialDetailService.queryListForLay(map);
        int total = this.materialDetailService.queryTotalForLay(map);
        PageUtils pageUtil = new PageUtils(list, total, limit,page);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 资料库类型
     * */
    @ResponseBody
    @RequestMapping("/materialTypeList")
    public R materialTypeList(HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        Long materialId = null;
        try {
            materialId = ServletRequestUtils.getLongParameter(request,  "materialId");
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        if(null == materialId){
            return R.ok();
        }
        map.put("materialId", materialId);
        PageUtils pageUtil = new PageUtils(
                courseMaterialTypeService.queryObject(materialId),
                courseMaterialTypeService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 资料库
     * */
    @ResponseBody
    @RequestMapping("/materialList")
    public R materialList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        PageUtils pageUtil = new PageUtils(
                courseMaterialService
                        .queryListMap(map),
                courseMaterialService.queryTotal(map),
                request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    /**
     * 请求保利威视的点播视频信息
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/polyvVideo")
    public R videoInfo(HttpServletRequest request){
        String schoolId = SchoolIdUtils.getSchoolId(request);

        //通过schoolId查询网校配置
        Map<String , Object> map = new HashMap<String , Object>();
        map.put("schoolId", schoolId);
        SchoolEntity school = schoolService.queryObject(map);

        String userid = school.getLiveuserid();//点播userId
        int numPerPage = 10000;//一页显示数量
        int pageNum = 1;//页码
        String secureKey = school.getPolyvsecretkey();//点播secretKey
        Long ptime = new Date().getTime();//时间戳
        //sign签名验证
	    /*String sign = new Sha1Utils().getDigestOfString(
	    		("numPerPage="+numPerPage+"&pageNum="+pageNum+"&ptime="+ptime+secureKey).getBytes()).toUpperCase();*/
        String sign = EncryptionUtils.shaHex("numPerPage="+numPerPage+"&pageNum="+pageNum+"&ptime="+ptime+secureKey).toUpperCase();
//	    YjAEjZz1PQ
        String url = "http://api.polyv.net/v2/video/"+userid+"/get-new-list";//请求地址
        String param = "userid"+userid+"&numPerPage="+numPerPage+"&pageNum="+pageNum+"&ptime="+ptime+"&sign="+sign;//请求参数

        //发送请求,获取返回数据
        try {
            String result = HttpUtils.sendGet(url, param);
            String encodeResult = URLEncoder.encode(result,"UTF-8");
            this.logger.info("polyv video log result:{}",result);
            this.logger.info("polyv video log result:{}",encodeResult);
            if(!result.equals("")){
                Map<String,Object> resultMap = JSONUtil.jsonToMap(result);

                //获得结果code
                Integer code = (Integer) resultMap.get("code");
                if(code == 200){
                    ArrayList<Object> data = (ArrayList<Object>) resultMap.get("data");
                    PageUtils pageUtil = new PageUtils(data, 0 , request);
                    return R.ok().put(pageUtil);
                    //return R.ok().put(data);
                }else{
                    return R.error(result);
                }
            }else{
                return R.error("获取数据失败");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return R.error("获取数据异常");
        }

    }

    /**
     * 产品线列表
     */
    @ResponseBody
    @RequestMapping("/productList")
    public R productList(HttpServletRequest request ){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "productName");
        List<SysProductEntity> list = this.sysProductService.queryList1(map);
        int total = this.sysProductService.queryTotal1(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 父级标签列表
     */
    @ResponseBody
    @RequestMapping("/labelParentList")
    public R labelParentList(HttpServletRequest request ){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "labelParentName");
        intQuery(map, request, "productId");
        List<AppLabel> list = this.appLabelService.queryParentList(map);
        int total = this.appLabelService.queryParentTotal(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
    /**
     * 部门组织列表
     */
    @ResponseBody
    @RequestMapping("/deptList")
    public R deptList(HttpServletRequest request ){
        Map<String, Object> map = getMapPage(request);
        List<SysDeptEntity> list = this.sysDeptService.queryList(map);
//		return list;
        return R.ok().put(list);
    }
    /**
     * 保利威视视频分类
     * @param request
     * @return
     * @throws ServletRequestBindingException
     */
    @ResponseBody
    @RequestMapping("/polyvVideoCataList")
    public R polyvVideoCataList(HttpServletRequest request ) throws ServletRequestBindingException{
        List<Map<String, Object>> dataList = null;
        Long productId = getVideoProductId(request);
        //课程
        if(null != productId){
            Map<String, Object> productSelectMap = new HashMap<>();
            //产品线获取保利威视参数
            productSelectMap.put("productId", productId);
            SysProductEntity productEntity = sysProductService.queryObject(productSelectMap);
            if(null != productEntity){
                //保利威视参数
                String userid = productEntity.getPolyvuserid();
                String secureKey = productEntity.getPolyvsecretkey();
                Long ptime = System.currentTimeMillis();
                String sign = EncryptionUtils.shaHex("ptime=" + ptime + "&userid=" + userid + secureKey).toUpperCase();
                String url = "http://api.polyv.net/v2/video/"+userid+"/cataJson";
                String param = "ptime=" + ptime + "&userid=" + userid + "&sign=" + sign;
                String result = HttpUtils.sendGet(url, param);
                logger.info("polyvVideoCataList result:{}" , result);
                if(StringUtils.isNotBlank(result)){
                    Map<String,Object> resultMap = JSONUtil.jsonToMap(result);
                    //获得结果code
                    Integer code = (Integer) resultMap.get("code");
                    if(code == 200){
                        dataList = (List<Map<String, Object>>) resultMap.get("data");
                    }else{
                        logger.error("polyvVideoCataList code:{},result:{}" , code,result);
                    }
                }
            }
        }
        return R.ok().put(dataList);
    }
    private Long getVideoProductId(HttpServletRequest request) {
        Long productId = null;
        try {
            productId = ServletRequestUtils.getLongParameter(request, "productId");
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        if(null == productId) {
            String courseId = null;
            try {
                courseId = ServletRequestUtils.getStringParameter(request, "courseId");
            } catch (ServletRequestBindingException e) {
                e.printStackTrace();
            }
            if(StringUtils.isNotBlank(courseId)) {
                Map<String, Object> courseSelectMap = getMap(request);
                courseSelectMap.put("courseId", courseId);
                CoursesEntity coursesEntity = coursesService.queryObject(courseSelectMap);
                if(null != coursesEntity) {
                    productId = coursesEntity.getProductId();
                }
            }
        }
        return productId;
    }
    /**
     * 保利威视视频列表
     * @param request
     * @param courseId	课程ID
     * @param catatree	树ID
     * @param page	第几页 默认1
     * @param limit	每页数目 默认10
     * @param total 总数 默认0
     * @return
     */
    @ResponseBody
    @RequestMapping("/polyvVideoList")
    public R polyvVideoList(HttpServletRequest request){
        List<Map<String, Object>> dataList = null;
        Long productId = getVideoProductId(request);
        if(null != productId){
            Map<String, Object> productSelectMap = new HashMap<>();
            //产品线获取保利威视参数
            productSelectMap.put("productId", productId);
            SysProductEntity productEntity = sysProductService.queryObject(productSelectMap);
            if(null != productEntity){
                //树ID
                String catatree = ServletRequestUtils.getStringParameter(request, "catatree", "1");
                //分页
                Integer page = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
                Integer limit = ServletRequestUtils.getIntParameter(request, "limit", 10);
                //保利威视参数
                String userid = productEntity.getPolyvuserid();
                String secureKey = productEntity.getPolyvsecretkey();
                Long ptime = new Date().getTime();


//				String param1 = "catatree="+"1,1494387689257,1495706226647"+"&numPerPage="+10+"&pageNum="+1+"&ptime="+ptime;
                String sign = EncryptionUtils.shaHex("catatree="+ catatree + "&numPerPage=" + limit  + "&pageNum=" + page + "&ptime=" + ptime + secureKey).toUpperCase();
                String url = "http://api.polyv.net/v2/video/" + userid + "/get-new-list";
                String param = "catatree="+ catatree + "&numPerPage=" + limit  + "&pageNum=" + page + "&ptime=" + ptime + "&sign=" + sign;
                String result = HttpUtils.sendGet(url, param);
                logger.info("polyvVideoList result:{}" , result);
                if(StringUtils.isNotBlank(result)){
                    Map<String,Object> resultMap = JSONUtil.jsonToMap(result);
                    //获得结果code
                    Integer code = (Integer) resultMap.get("code");
                    if(code == 200){
                        dataList = (List<Map<String, Object>>) resultMap.get("data");
//						List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("data");
//						List<Map<String, Object>> nodes = (List<Map<String, Object>>)dataList.get(0).get("nodes");
//						LinkedHashSet<String> set = new LinkedHashSet<String>();
//						writeInfo(nodes,set);
                    }else{
                        logger.error("polyvVideoList code:{},result:{}" , code,result);
                    }
                }
            }
        }
        PageUtils pageUtil = new PageUtils(dataList, ServletRequestUtils.getIntParameter(request,  "total", 0), request);
        return R.ok().put(pageUtil);
    }


    /**
     * 报考列表
     * */
    @ResponseBody
    @RequestMapping("/registrationList")
    public R registrationList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "registrationNo");
        map.put("status", 2);
        PageUtils pageUtil = new PageUtils(
                courseAbnormalRegistrationService.queryRegistrationLayList(map),
                courseAbnormalRegistrationService.queryLayTotal(map),
                request);

        return R.ok().put(ParamKey.Out.data, pageUtil);
    }

    //勿删 ---- by shanyaofeng
	/*public static void main(String[] args) {
		String userid = "a647f95e6e";
		String secureKey = "y9qdB0dV0I";//school.getPolyvsecretkey();//点播secretKey
		
		Long cata_ptime = new Date().getTime();
		String cata_sign = EncryptionUtils.shaHex("ptime=" + cata_ptime + "&userid=" + userid + secureKey).toUpperCase();
		
		String cata_url = "http://api.polyv.net/v2/video/"+userid+"/cataJson";
		String cata_param = "ptime=" + cata_ptime + "&userid=" + userid + "&sign=" + cata_sign;
		
		
		String cata_result = HttpUtils.sendGet(cata_url, cata_param);
		if(!cata_result.equals("")){
			Map<String,Object> resultMap = JSONUtil.jsonToMap(cata_result);
			
			//获得结果code
			Integer code = (Integer) resultMap.get("code");
			if(code == 200){
				List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("data");
				List<Map<String, Object>> nodes = (List<Map<String, Object>>)dataList.get(0).get("nodes");
				LinkedHashSet<String> set = new LinkedHashSet<String>();
				writeInfo(nodes,set);
			}else{
				System.out.println("");
			}
		}else{
			System.out.println("");
		}
	}

	public static void writeInfo(List<Map<String, Object>> list, LinkedHashSet<String> catanameSet){
		String userid = "a647f95e6e";
		String secureKey = "y9qdB0dV0I";//school.getPolyvsecretkey();//点播secretKey
		
		String catatree = null;
		int numPerPage = 10000;//一页显示数量
		int pageNum = 1;//页码
	    Long ptime = null;//时间戳
	    String sign = null;
	    
	    String paramStr = null;
	    
	    String url = "http://api.polyv.net/v2/video/"+userid+"/get-new-list";//请求地址
	    String param = "numPerPage="+numPerPage+"&pageNum="+pageNum+"&ptime="+ptime+"&sign="+sign;//请求参数
	    
		for(Map<String, Object> l : list){
			
			List<Map<String, Object>> nodes = (List<Map<String, Object>>)l.get("nodes");
			
			if(null == nodes){
				String cataname = l.get("cataname").toString();
				catanameSet.add(cataname);
				
				catatree = (String)l.get("catatree");
			    ptime = new Date().getTime();//时间戳
			    paramStr = "catatree="+catatree+"&numPerPage="+numPerPage+"&pageNum="+pageNum+"&ptime="+ptime;
			    sign = EncryptionUtils.shaHex(paramStr+secureKey).toUpperCase();
			    
			    param = paramStr+"&sign="+sign;//请求参数
			    
			    //发送请求,获取返回数据
				
				String result = HttpUtils.sendGet(url, param);
				if(!result.equals("")){
					Map<String,Object> resultMap = JSONUtil.jsonToMap(result);
					
					//获得结果code
					Integer code = (Integer) resultMap.get("code");
					if(code == 200){
						List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("data");
						for(Map<String, Object> data : dataList){
							for(String value : catanameSet){  
					            System.out.print(value + ",");  
					        }  
							System.out.print(data.get("title") + ",");
							System.out.println(data.get("vid"));
						}
						catanameSet.remove(cataname);
					}
				}
			}else{
				String cataname = l.get("cataname").toString();
				catanameSet.add(cataname);
				writeInfo(nodes,catanameSet);
				catanameSet.remove(cataname);
			}
		}
	}*/


    /**
     * 公开课权限列表
     */
    @ResponseBody
    @RequestMapping("/courseOliveAuthorityList")
    public R courseOliveAuthorityList(HttpServletRequest request ){
        Map<String, Object> map = getMapPage(request);
        List<CourseOliveAuthority> list = this.courseOliveAuthorityService.queryList(map);
        int total = this.courseOliveAuthorityService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(list, total, request);
        return R.ok().put(ParamKey.Out.data, pageUtil);
    }
}
