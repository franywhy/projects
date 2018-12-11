package io.renren.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.renren.entity.CourseLiveDetailsEntity;
import io.renren.pojo.course.CourseLiveDetailsPOJO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseRecordDetailEntity;
import io.renren.entity.CoursesEntity;
import io.renren.pojo.CourseRecordDetailPOJO;
import io.renren.pojo.course.CoursesPOJO;
import io.renren.service.CourseLiveDetailsService;
import io.renren.service.CourseMaterialService;
import io.renren.service.CourseRecordDetailService;
import io.renren.service.CoursesService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 课程档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-22 14:06:37
 */
@Controller
@RequestMapping("/mall/courses")
public class CoursesController extends AbstractController {
	@Autowired
	private CoursesService coursesService;
	@Autowired
	private CourseLiveDetailsService courseLiveDetailsService;
	@Autowired
	private CourseRecordDetailService courseRecordDetailService;
	@Autowired
	private CourseMaterialService courseMaterialService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	
	//课程弹框
	@ResponseBody
	@RequestMapping("/listGrid")
	public R listGrid(
			String courseName, 
			Integer page, Integer limit , HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "courseName");
		List<Map<String, Object>> list = coursesService.queryListGrid(map);
		int total = coursesService.queryListGridTotal(map);
		PageUtils pageUtil = new PageUtils(list, total, request);	
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courses:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "courseName");
		stringQuery(map, request, "courseNo");
		longQuery(map, request, "productId");
		//查询列表数据
		List<CoursesPOJO> coursesList = coursesService.queryPOJOList(map);
		int total = coursesService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(coursesList, total, request);	
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/demo")
	public R demo(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//查询列表数据
		List<CoursesPOJO> coursesList = coursesService.queryPOJOList(map);
		return R.ok().put("data", coursesList);
	}
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{courseId}")
	@RequiresPermissions("courses:info")
	public R info(@PathVariable("courseId") Long courseId,HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		//查询主表
		Map<String , Object> map1 = new HashMap<String , Object>();
		map1.put("courseId", courseId);
		map1.put("schoolId", schoolId);
		CoursesEntity courses = coursesService.queryObject(map1);
		//查询子表
		Map<String , Object> map2 = new HashMap<String , Object>();
		map2.put("courseId", courseId);
		map2.put("schoolId", schoolId);
		List<CourseLiveDetailsEntity> detailList = courseLiveDetailsService.queryAll(map2);
		return R.ok().put("courses", courses).put("detailList", detailList);
	}
	
	
	/**
	 * 保存
	 */
	@SysLog("保存课程")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courses:save")
	public R save(@RequestBody CoursesPOJO courses , HttpServletRequest request ){
		if(StringUtils.isBlank(courses.getCourseName())){
			return R.error("[课程名称]不能为空");
		}
		if(StringUtils.isBlank(courses.getCourseNo())){
			return R.error("[课程编号]不能为空");
		}
		if(StringUtils.isBlank(""+courses.getProductId())){
			return R.error("[产品线]不能为空");
		}
		if(courses.getCourseEq() == null){
			return R.error("[排课是否可冲]突不能为空");
		}
		String schoolId = SchoolIdUtils.getSchoolId(request);
		//创建用户
		courses.setCreator(getUserId());
		//平台ID
		courses.setSchoolId(schoolId);
		//保存主表
		coursesService.save(courses);
		
		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改课程")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courses:update")
	public R update(@RequestBody CoursesPOJO courses,HttpServletRequest request ){
		if(StringUtils.isBlank(courses.getCourseName())){
			return R.error("[课程名称]不能为空");
		}
		if(StringUtils.isBlank(courses.getCourseNo())){
			return R.error("[课程编号]不能为空");
		}
		if(StringUtils.isBlank(""+courses.getProductId())){
			return R.error("[产品线]不能为空");
		}
		if(courses.getCourseEq() == null){
			return R.error("[排课是否可冲突]不能为空");
		}
		courses.setModifier(getUserId());
		coursesService.update(courses);
		return R.ok();
	}
	@ResponseBody
	@RequestMapping("/del/{courseId}")
	public R del(@PathVariable("courseId") Long courseId,HttpServletRequest request){
		Map<String, Object> map = getMap(request); 
		map.put("courseId", courseId);
		Integer l = courseMaterialService.checkCourses(map);
		return R.ok().put(l);
	}
	@ResponseBody
	@RequestMapping("/del2/{courseId}")
	public R del2(@PathVariable("courseId") Long courseId,HttpServletRequest request){
		Map<String, Object> map = getMap(request); 
		map.put("value", courseId);
		return R.ok().put(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_material_course_id));
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除课程")
	@ResponseBody
	@RequestMapping("/delete/{courseId}")
	@RequiresPermissions("courses:delete")
	public R delete(@PathVariable("courseId") Long courseId,HttpServletRequest request){
		Map<String, Object> map = getMap(request); 
		map.put("value", courseId);
		ArrayList<String> exceptList = new ArrayList<String>();
		String exceptMsg = "删除失败的具体原因如下:";
		String errMsg="";
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_material_course_id)){
			exceptMsg="资料库有引用！";
			exceptList.add(exceptMsg);
		}
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_book_course_id)){
				exceptMsg="教材档案有引用！";
				exceptList.add(exceptMsg);
		}
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_classplan_course_id)){
			exceptMsg="排课计划有引用！";
			exceptList.add(exceptMsg);
	    }
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_exam_schedule_course_id)){
			exceptMsg="课程考试时段表有引用！";
			exceptList.add(exceptMsg);
	    }
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_record_detail_course_id)){
			exceptMsg="课程录播有引用！";
			exceptList.add(exceptMsg);
	    }
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_textbook_course_id)){
			exceptMsg="教材档案有引用！";
			exceptList.add(exceptMsg);
	    }
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_userplan_detail_course_id)){
			exceptMsg="学员规划子表有引用！";
			exceptList.add(exceptMsg);
	    }
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.mall_goods_detail_course_id)){
			exceptMsg="商品子表有引用！";
			exceptList.add(exceptMsg);
	    }
		for (int i = 0; i < exceptList.size(); i++)
		{
			errMsg += exceptList.get(i) + "<br>";
		}
		if(errMsg.equals("")){
			coursesService.delete(map);	
			return R.ok();
		}
		else{
			return R.error(errMsg);
		}
		
	}
	
	/**
	 * 学历层次映射列表
	 * */
	
	@ResponseBody
	@RequestMapping("/getSelectionList")
	public R getSelectionList(HttpServletRequest request){
		return R.ok().put("courseSelections", coursesService.querySelectionList(SchoolIdUtils.getSchoolId(request)));
	}
	
	
	
	//////////////////////////////////////////////////////////直播课基础课次///////////////////////////////////////////////
	/**
	 * 课程:直播课基础课次列表
	 */
	@ResponseBody
	@RequestMapping("/liveList")
	@RequiresPermissions("courses:liveList")
	public R liveList(HttpServletRequest request){
		//查询子表
		Map<String , Object> queryMap = getMapPage(request);
		longQuery(queryMap, request, "courseId");
		List<CourseLiveDetailsEntity> liveList = courseLiveDetailsService.queryPageList(queryMap);
		int total = courseLiveDetailsService.queryPageTotal(queryMap);
		PageUtils pageUtil = new PageUtils(liveList, total, request);	
		return R.ok().put(pageUtil);
	}
	/**
	 * 新增直播课基础课次
	 */
	@SysLog("新增直播课基础课次")
	@ResponseBody
	@RequestMapping("/liveSave")
	@RequiresPermissions("courses:liveList")
	public R liveSave(@RequestBody CourseLiveDetailsPOJO courseLiveDetailsPOJO , HttpServletRequest request ){
		//直播课基础课次
		if(StringUtils.isBlank(courseLiveDetailsPOJO.getLiveName())){
			return R.error("直播课基础课次必填!");
		}
		//排序
		if(null == courseLiveDetailsPOJO.getOrderNum()){
			return R.error("排序必填!");
		}
		//课程主键
		if(null == courseLiveDetailsPOJO.getCourseId() || courseLiveDetailsPOJO.getCourseId() <= 0){
			return R.error("课程选择失败!");
		}
		
		courseLiveDetailsPOJO.setSchoolId(SchoolIdUtils.getSchoolId(request));
		//entity
		CourseLiveDetailsEntity liveEn = CourseLiveDetailsPOJO.getEntity(courseLiveDetailsPOJO);
		courseLiveDetailsService.save(liveEn);
		return R.ok();
	}
	/**
	 * 修改直播课基础课次
	 */
	@SysLog("修改直播课基础课次")
	@ResponseBody
	@RequestMapping("/liveUpdate")
	@RequiresPermissions("courses:liveList")
	public R liveUpdate(@RequestBody CourseLiveDetailsPOJO courseLiveDetailsPOJO , HttpServletRequest request ){
		//主键
		if(null == courseLiveDetailsPOJO.getLiveId() || courseLiveDetailsPOJO.getLiveId() <= 0){
			return R.error("课程选择失败!");
		}
		//直播课基础课次
		if(StringUtils.isBlank(courseLiveDetailsPOJO.getLiveName())){
			return R.error("直播课基础课次必填!");
		}
		//排序
		if(null == courseLiveDetailsPOJO.getOrderNum()){
			return R.error("排序必填!");
		}
		//课程主键
		if(null == courseLiveDetailsPOJO.getCourseId() || courseLiveDetailsPOJO.getCourseId() <= 0){
			return R.error("课程选择失败!");
		}
		
		courseLiveDetailsPOJO.setSchoolId(SchoolIdUtils.getSchoolId(request));
		//entity
		CourseLiveDetailsEntity liveEn = CourseLiveDetailsPOJO.getEntity(courseLiveDetailsPOJO);
		courseLiveDetailsService.update(liveEn);
		return R.ok();
	}
	/**
	 * 删除直播课基础课次
	 */
	@SysLog("删除直播课基础课次")
	@ResponseBody
	@RequestMapping("/liveDelete/{liveId}")
	@RequiresPermissions("courses:liveList")
	public R liveDelete(@PathVariable("liveId") Long liveId , HttpServletRequest request ){
		courseLiveDetailsService.delete(liveId);
		return R.ok();
	}
	
	//////////////////////////////////////////////////////////录播课///////////////////////////////////////////////
	/**
	 * 课程:录播课列表
	 */
	@ResponseBody
	@RequestMapping("/recordList/{courseId}")
	@RequiresPermissions("courses:recordList")
	public List<CourseRecordDetailPOJO> recordList(@PathVariable("courseId") Long courseId,HttpServletRequest request){
		List<CourseRecordDetailPOJO> pojoList = courseRecordDetailService.queryPOJOList(courseId);
		return pojoList;
	}
	/**
	 * 课程:录播课详情
	 */
	@ResponseBody
	@RequestMapping("/recordInfo/{recordId}")
	@RequiresPermissions("courses:recordList")
	public R recordInfo(@PathVariable("recordId") Long recordId,HttpServletRequest request){
		//查询子表
		CourseRecordDetailPOJO courseRecordDetailPOJO = courseRecordDetailService.queryPOJO(recordId);
		return R.ok().put(courseRecordDetailPOJO);
	}
	/**
	 * 录播课 保存
	 * @param courseRecordDetailEntity
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/recordSave")
	@RequiresPermissions("courses:recordList")
	public R recordSave(@RequestBody CourseRecordDetailEntity courseRecordDetailEntity,HttpServletRequest request){
		if(StringUtils.isBlank(courseRecordDetailEntity.getName())){
			return R.error("请输入名称!");
		}
		if(null == courseRecordDetailEntity.getParentId()){
			return R.error("请选择上一级!");
		}
		if(null == courseRecordDetailEntity.getCourseId() || courseRecordDetailEntity.getCourseId() <= 0){
			return R.error("请选择课程!");
		}
		if(null == courseRecordDetailEntity.getType()){
			return R.error("请选择章节类型!");
		}
		if(null == courseRecordDetailEntity.getCourseId()){
			return R.error("请输入排序!");
		}
		
		courseRecordDetailEntity.setCreatePerson(getUserId());
		courseRecordDetailEntity.setCreationTime(new Date());
		courseRecordDetailEntity.setModifiedTime(courseRecordDetailEntity.getCreationTime());
		
		this.courseRecordDetailService.save(courseRecordDetailEntity);
		return R.ok();
	}
	/**
	 * 录播课 更新
	 * @param courseRecordDetailEntity
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/recordUpdate")
	@RequiresPermissions("courses:recordList")
	public R recordUpdate(@RequestBody CourseRecordDetailEntity courseRecordDetailEntity,HttpServletRequest request){
		if(StringUtils.isBlank(courseRecordDetailEntity.getName())){
			return R.error("请输入名称!");
		}
		if(null == courseRecordDetailEntity.getParentId()){
			return R.error("请选择上一级!");
		}
		if(null == courseRecordDetailEntity.getCourseId() || courseRecordDetailEntity.getCourseId() <= 0){
			return R.error("请选择课程!");
		}
		if(null == courseRecordDetailEntity.getType()){
			return R.error("请选择章节类型!");
		}
		if(null == courseRecordDetailEntity.getCourseId()){
			return R.error("请输入排序!");
		}
		
		courseRecordDetailEntity.setCreationTime(new Date());
		courseRecordDetailEntity.setModifiedTime(courseRecordDetailEntity.getCreationTime());
		
		this.courseRecordDetailService.update(courseRecordDetailEntity);
		return R.ok();
	}
	/**
	 * 录播课 删除
	 * @param courseRecordDetailEntity
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/recordDelete/{recordId}")
	@RequiresPermissions("courses:recordList")
	public R recordDelete(@PathVariable("recordId") Long recordId,HttpServletRequest request){
		
		Map<String , Object> map = new HashMap<>();
		map.put("recordId" , recordId);
		this.courseRecordDetailService.delete(map);
		return R.ok();
	}
	
}