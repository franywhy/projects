package io.renren.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseUserplanDetailEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.service.CourseUserplanDetailService;
import io.renren.service.CourseUserplanService;
import io.renren.service.MallGoodsDetailsService;
import io.renren.service.MallGoodsInfoService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.Constant;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 学员规划
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:16
 */
@Controller
@RequestMapping("course/userplan")
public class CourseUserplanController extends AbstractController {
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private CourseUserplanDetailService courseUserplanDetailService;
	@Autowired
	private MallGoodsInfoService mallGoodsInfoService;
	@Autowired
	private MallGoodsDetailsService mallGoodsDetailsService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	
	/**
	 * 保存
	 */
	@SysLog("批量生成学员规划")
	@ResponseBody
	@RequestMapping("/saveBatch/{commodityId}/{classId}")
	@RequiresPermissions("course:userplan:saveBatch")
	public R saveBatch(@PathVariable("commodityId") Long commodityId,@PathVariable("classId")Long classId){
		String string = this.courseUserplanService.saveUserplanBatch(commodityId,classId);
		return R.ok().put(string);
	}
	
	/**
	 * 根据学员规划id查询课程,排课计划
	 */
	@ResponseBody
	@RequestMapping("/userplanDetailList")
	public R userplanDetailList(String userPlanId,String classplanIds,String classplanName,String courseName,String courseNo,Integer page,  Integer limit , HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		map.put("userPlanId", userPlanId);
		map.put("classplanName", classplanName);
		map.put("courseName", courseName);
		map.put("courseNo", courseNo);
		if(null != classplanIds && classplanIds.length() > 0){
			String[] classplanIdsStr = new String[]{};
			classplanIdsStr = classplanIds.split(",");
			map.put("classplanIds", classplanIdsStr);
		}
		//longQuery(map, request, "userPlanId");
		List<Map<String , Object>> list = courseUserplanDetailService.queryListByUserplanId(map);
//		int total = list.size();
		int total = courseUserplanDetailService.queryTotalByUserplanId(map);
		PageUtils pageUtil = new PageUtils(list, total, limit, page);
		return R.ok().put(pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/courseListByCommodityId")
	public R courseListByCommodityId(
			Integer isRep , Integer isMatch ,
			Integer page,  Integer limit , 
			HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		longQuery(map, request, "commodityId");
		longQuery(map, request, "areaId");
		//考英语二
		boolean examEn2  = true;
		//专业对口
		boolean targetGrade  = true;
		if(1 == isRep){
			examEn2 = false;
		}
		if(1 == isMatch){
			targetGrade = false;
		}
		map.put("examEn2", examEn2);
		map.put("targetGrade", targetGrade);
		List<Map<String , Object>> list = this.courseUserplanDetailService.courseListByCommodityId(map);
		int total = courseUserplanDetailService.courseTotalByCommodityId(map);
		PageUtils pageUtil = new PageUtils(list, total, limit, page);	
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/courseListByUserPlanId")
	public R courseListByUserPlanId(
			Integer isRep , Integer isMatch ,
			HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		longQuery(map, request, "userPlanId");
		//考英语二
		boolean examEn2  = true;
		//专业对口
		boolean targetGrade  = true;
		if(1 == isRep){
			examEn2 = false;
		}
		if(1 == isMatch){
			targetGrade = false;
		}
		map.put("examEn2", examEn2);
		map.put("targetGrade", targetGrade);
//		Long[] userPlanDetailIds=this.courseUserplanDetailService.getUserDetailPlanIds(map);
		List<Map<String , Object>> list = this.courseUserplanDetailService.courseListByUserPlanId(map);
		int total = courseUserplanDetailService.courseTotalByUserPlanId(map);
		PageUtils pageUtil = new PageUtils(list, total, request);	
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("course:userplan:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//用户ID
		longQuery(map, request, "userId");
		//手机号码
		stringQuery(map, request, "mobile");
		//用户昵称
		stringQuery(map, request, "nickName");
		//班级PK
		longQuery(map, request, "classId");
		//地区PK
		longQuery(map, request, "areaId");
		//班型PK
		longQuery(map, request, "classTypeId");
		//层次PK
		longQuery(map, request, "levelId");
		//订单号
		stringQuery(map, request, "orderNo");
		//专业PK
		stringQuery(map, request, "professionId");
		
		//班主任
		longQuery(map, request, "classTeacherId");
		//是否学习代替课程
		intQuery(map, request, "isRep");
		//专业对口
		intQuery(map, request, "isMatch");
		//学员规划状态
		intQuery(map, request, "userplanStatus");
		//部门
		longQuery(map, request, "deptId");
        stringQuery(map,request,"deptIdList");
        String deptIdList = (String) map.get("deptIdList");
        if (deptIdList!= null && !"".equals(deptIdList.trim())){
            String[] split = deptIdList.split(",");
            map.put("deptIdList", Arrays.asList(split));
        }
		//查询列表数据
		List<Map<String,Object>> courseUserplanList = courseUserplanService.queryList(map);
		int total = courseUserplanService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseUserplanList, total, request);	
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{userPlanId}")
	@RequiresPermissions("course:userplan:info")
	public R info(@PathVariable("userPlanId") Long userPlanId , HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		Map<String , Object> map = new HashMap<>();
		map.put("userPlanId", userPlanId);
		map.put("schoolId", schoolId);
		Map<String , Object> courseUserplan = courseUserplanService.queryObjectMap(map);
		return R.ok().put("courseUserplan", courseUserplan);
	}
	
	
	/**
	 * 保存
	 */
	@SysLog("保存学员规划")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("course:userplan:save")
	public R save(@RequestBody CourseUserplanEntity courseUserplan , HttpServletRequest request){
		if(null == courseUserplan.getOrderId()){
			return R.error("请选择订单");
		}
		if(null == courseUserplan.getClassId()){
			return R.error("请选择班级");
		}
		if(null==courseUserplan.getIsMatch()){
			return R.error("请确认专业是否对口");
		}
		if(null==courseUserplan.getIsRep()){
			return R.error("请确认是否学习可代替课程");
		}
		String schoolId = SchoolIdUtils.getSchoolId(request);
		courseUserplan.setCreatePerson(getUserId());
		courseUserplan.setSchoolId(schoolId);
		courseUserplanService.save(courseUserplan);	
		return R.ok();
	}
	/**
	 * 保存子表信息
	 */
	@ResponseBody
	@RequestMapping("/saveDetail")
	@RequiresPermissions("course:userplandetail:save")
	public R saveDetail(@RequestBody CourseUserplanDetailEntity courseUserplanDetailEntity , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("areaId", courseUserplanDetailEntity.getAreaId());
		map.put("courseId", courseUserplanDetailEntity.getCourseId());
		map.put("userplanId", courseUserplanDetailEntity.getUserplanId());
		
		if(null == courseUserplanDetailEntity.getAreaId()){
			return R.error("请选择区域");
		}
		if(null == courseUserplanDetailEntity.getCourseId()){
			return R.error("请选择课程");
		}
		if(courseUserplanDetailService.checkAreaAndCourse(map)>0){
			return R.error("添加失败，该区域下的课程已经存在");
		}
		String schoolId = SchoolIdUtils.getSchoolId(request);
		courseUserplanDetailEntity.setSchoolId(schoolId);
		courseUserplanDetailService.save(courseUserplanDetailEntity);	
		return R.ok();
	}
	/**
	 * 修改 修改可代替课程和专业对口
	 */
	@SysLog("修改可代替课程和专业对口")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("course:userplan:update")
	public R update(@RequestBody CourseUserplanEntity courseUserplan , HttpServletRequest request){
		
		if(null == courseUserplan.getUserPlanId()){
			return R.error("请选择一条记录");
		}
		if(null==courseUserplan.getIsMatch()){
			return R.error("请确认专业是否对口");
		}
		if(null==courseUserplan.getIsRep()){
			return R.error("请确认是否学习可代替课程");
		}
		if(null==courseUserplan.getClassId()){
			return R.error("请选择班级");
		}
		String schoolId = SchoolIdUtils.getSchoolId(request);
		courseUserplan.setSchoolId(schoolId);
		courseUserplan.setModifyPerson(getUserId());
		this.courseUserplanService.updateMap(courseUserplan);
		return R.ok();
	}
	
/*	
 	/**
	 * 转省转专业旧方案
	 *//*
	@SysLog("转省转专业")
	@ResponseBody
	@RequestMapping("/updateChange")
	@RequiresPermissions("course:userplan:updateChange")
	public R updateChange(HttpServletRequest request){
		Map<String , Object> map = getMap(request);
		longQuery(map, request, "userPlanId");
		longQuery(map, request, "orderId");
		longQuery(map, request, "commodityId");//商品
		longQuery(map, request, "areaId");//省份
		longQuery(map, request, "classId");
		intQuery(map, request, "isRep");
		intQuery(map, request, "isMatch");
		map.put("modifyPerson", getUserId());
		this.courseUserplanService.updateChange(map);
		return R.ok();
	}
*/
	/**
	 * 转省转专业新方案
	 */
	@SysLog("转省转专业")
	@ResponseBody
	@RequestMapping("/updateChange")
	@RequiresPermissions("course:userplan:updateChange")
	public R updateChange(HttpServletRequest request){
		Map<String , Object> map = getMap(request);
		longQuery(map, request, "userPlanId");
		longQuery(map, request, "commodityId");//商品
		longQuery(map, request, "areaId");//省份
		longQuery(map, request, "classTypeId");//班型
		longQuery(map, request, "levelId");//层次
		longQuery(map, request, "professionId");//专业
		map.put("modifyPerson", getUserId());
		
		this.courseUserplanService.updateChange1(map);
		return R.ok();
	}
	
	/**
	 * 商品列表
	 */
	@ResponseBody
	@RequestMapping("/goodList")
	public R goodList(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//上架
		map.put("status", Constant.Status.RESUME.getValue());
//		//专业
//		longQuery(map, request, "professionId");
//		//层次
//		longQuery(map, request, "levelId");
//		//名称
//		stringQuery(map, request, "name");
		List<Map<String, Object>> data = mallGoodsInfoService.simpleQueryList(map);
//		int total = mallGoodsInfoService.simpleQueryTotal(map);
//		PageUtils pageUtil = new PageUtils(data, total, request);	
		return R.ok().put("data", data);
	}
	
	/**
	 * 订单列表 需要匹配学员所以不用分页加载
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/orderChangeList")
	public R orderChangeList(HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		Long userPlanId = ServletRequestUtils.getLongParameter(request, "userPlanId" , 0);
		if(userPlanId == 0){
			return R.error("请选择一条记录");
		}
		map.put("userPlanId", userPlanId);
		
		List<Map<String, Object>> data = this.courseUserplanService.queryOrderForChangeUserplan(map);
		return R.ok().put("data", data);
	}
	
	/**
	 * 获取商品销售的地区
	 */
	@ResponseBody
	@RequestMapping("/queryAreaByCommodityId")
	public R queryAreaByCommodityId(HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		Long commodityId = ServletRequestUtils.getLongParameter(request, "commodityId" , 0);
		map.put("commodityId", commodityId);
		List<Map<String, Object>> data = null;
		if(commodityId > 0){
			data = this.mallGoodsDetailsService.queryAreaByGoodId(map);
		}
		return R.ok().put("data", data);
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除学员规划")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("course:userplan:delete")
	public R delete(@RequestBody Long[] userPlanIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids", userPlanIds);
		courseUserplanService.deleteBatch(map);	
		return R.ok();
	}
	/**
	 * 删除明细
	 */
	@SysLog("删除学员规划明细")
	@ResponseBody
	@RequestMapping("/deleteDetail/{userPlanDetailId}")
	@RequiresPermissions("course:userplandetail:delete")
	public R deleteDetail(@PathVariable("userPlanDetailId") Long userPlanDetailId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("value", userPlanDetailId);
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_userplan_class_detail_userplan_detail_id)){
			return R.error("排课计划明细有引用，删除失败");
		}
		courseUserplanDetailService.delete(map);	
		return R.ok();
	}
}
