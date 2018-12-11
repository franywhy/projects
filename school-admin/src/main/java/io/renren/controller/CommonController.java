package io.renren.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.ParamKey;
import io.renren.entity.SysUserEntity;
import io.renren.pojo.SelectionItem;
import io.renren.service.CourseClassplanService;
import io.renren.service.CourseUserplanDetailService;
import io.renren.service.CoursesService;
import io.renren.service.MallAreaService;
import io.renren.service.MallClassTypeService;
import io.renren.service.MallExamScheduleService;
import io.renren.service.MallFeedbackRecordService;
import io.renren.service.MallGoodsDetailsService;
import io.renren.service.MallLevelService;
import io.renren.service.MallLiveRoomService;
import io.renren.service.MallProfessionService;
//import io.renren.service.MallProfessionService;
import io.renren.service.MallServiceRecordService;
import io.renren.service.MallStudioService;
import io.renren.service.SysUserService;
import io.renren.service.TimeTableDetailService;
import io.renren.service.TimetableService;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;

/**
 * 基础档案列表
 * @class io.renren.controller.CommonController.java
 * @Description:
 * @author shihongjie
 * @dete 2017年4月8日
 */
@Controller
@RequestMapping("/common")
public class CommonController extends AbstractController {
	
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
	private TimeTableDetailService timetableDetailService;
	@Autowired
	private MallStudioService mallStudioService;
	@Autowired
	private MallLiveRoomService mallLiveRoomService;
	@Autowired
	private MallClassTypeService mallClassTypeService;
	@Autowired
	private MallGoodsDetailsService mallGoodsDetailsService;
	@Autowired
	private MallServiceRecordService mallServiceRecordService;
	@Autowired
	private MallFeedbackRecordService mallFeedbackRecordService;
	@Autowired
	private MallExamScheduleService mallExamScheduleService; 
	@Autowired
	private CourseUserplanDetailService courseUserplanDetailService;
	@Autowired
	private CourseClassplanService courseClassplanService;
	@Autowired
	private CoursesService coursesService;
	/**
	 * 服务类型
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/serviceRecordList")
	public R serviceRecordList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data , mallServiceRecordService.queryList(map));
	}
	/**
	 *  反馈类型文档
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/feedbackRecordList")
	public R feedbackRecordList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data , mallFeedbackRecordService.queryList(map));
	}
	
	/**
	 * 班型列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/classTypeList")
	public R classTypeList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, mallClassTypeService.querySelectList(map));
	}
	
	
	/**
	 * 班主任列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/classTeacherList")
	public R classTeacherList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, sysUserService.findClassTeacherList(map));
	}
	/**
	 * 课程列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/courseList")
	public R CourseList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		List<SelectionItem> list=coursesService.querySelectionList(SchoolIdUtils.getSchoolId(request));
		return R.ok().put(ParamKey.Out.data,list);
	}
	
	/**
	 * 授课老师列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/teacherList")
	public R teacherList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		String nickName = null;
		Long classTeacher = null;
		Long teacher = null;
		try {
			nickName = ServletRequestUtils.getStringParameter(request, "nickName", null);
			classTeacher = ServletRequestUtils.getLongParameter(request, "classTeacher");
			teacher = ServletRequestUtils.getLongParameter(request, "teacher");
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		
		List<SysUserEntity> list = this.sysUserService.findTeacherList(
				nickName, teacher, classTeacher,
				(Integer)map.get(ParamKey.In.OFFSET), (Integer)map.get(ParamKey.In.LIMIT)
				);
		return R.ok().put(ParamKey.Out.data, list);
	}
	
	/**
	 * 上课时点列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/timetableList")
	public R timetableList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, timetableService.findTimetableList(map));
	}
	
	/**
	 * 直播室列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/studioList")
	public R sudioList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, mallStudioService.findStudioList(map));
	}
	
	/**
	 * 直播间列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/liveRoomList")
	public R liveRoomList(HttpServletRequest request ){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, mallLiveRoomService.findLiveRoomList(map));
	}
	
	/**
	 * 地区列表
	 * */
	@ResponseBody
	@RequestMapping("/areaList")
	public R areaList(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, mallAreaService.querySelectionList(map));
	}
	
	/**
	 * 地区列表
	 * */
	@ResponseBody
	@RequestMapping("/courseAreaList")
	public R AreaListByCommodityId(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		Integer commodityId = ServletRequestUtils.getIntParameter(request, "commodity_id", 0);
		if(0 == commodityId){
			return R.error("请选择一个商品");
		}
		map.put("commodityId", commodityId);
		List<Map<String, Object>> data = mallGoodsDetailsService.queryAreaByGoodId(map);
		return R.ok().put(ParamKey.Out.data, data);
	}
	
	/**
	 * 专业列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/professionList")
	public R professionList(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, mallProfessionService.queryList1(map));
	}
	/**
	 * 层次列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/levelList")
	public R levelList(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, mallLevelService.findLevelList(map));
	}
	/**
	 * 考试时间段列表
	 */
	@ResponseBody
	@RequestMapping("/examTimeList")
	public R examTimeList(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, mallExamScheduleService.queryList(map));
	}
	
	
	
}
