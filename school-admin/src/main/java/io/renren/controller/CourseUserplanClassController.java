package io.renren.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.pojo.CourseUserplanClassDetailPOJO;
import io.renren.pojo.CourseUserplanClassPOJO;
import io.renren.service.CourseUserplanClassDetailService;
import io.renren.service.CourseUserplanClassService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import io.renren.utils.UserPlanClassDetailException;


/**
 * 学习计划
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-27 09:55:36
 */
@Controller
@RequestMapping("courseuserplanclass")
public class CourseUserplanClassController extends AbstractController {
	@Autowired
	private CourseUserplanClassService courseUserplanClassService;
	@Autowired
	private CourseUserplanClassDetailService courseUserplanClassDetailService;
//	@Resource
//	KGS studyplanKGS;
//	private static final String HEAD = "XXJH";
	
	@SysLog("批量生成学习计划-会计")
	@ResponseBody
	@RequestMapping("/addBatchByClassplanId")
	@RequiresPermissions("courseuserplanclass:addBatchByClassplanId")
	public R addBatchByClassplanId(HttpServletRequest request) throws ServletRequestBindingException {
        String classplanId = ServletRequestUtils.getStringParameter(request, "classplanId");
        String classId = ServletRequestUtils.getStringParameter(request, "classId");
        String string = this.courseUserplanClassService.addBatchByClassplanId(classplanId, classId,SchoolIdUtils.getSchoolId(request), getUserId());
		return R.ok(string).put(string);
	}
	@SysLog("批量生成学习计划-自考")
	@ResponseBody
	@RequestMapping("/addBatchByClassplanIdAndClassId/{classplanId}/{classId}/{examScheduleId}")
	@RequiresPermissions("courseuserplanclass:addBatchByClassplanIdAndClassId")
	public R addBatchByClassplanIdAndClassId(
			@PathVariable("classplanId")String classplanId ,
			@PathVariable("classId")String classId,
			@PathVariable("examScheduleId")Long examScheduleId,
			HttpServletRequest request)
	{
		String string = this.courseUserplanClassService.addBatchByClassplanIdAndClassId(examScheduleId, classId, classplanId, SchoolIdUtils.getSchoolId(request), getUserId());
		return R.ok(string).put(string);
	}
	
	@ResponseBody
	@RequestMapping("/getCode")
	public R getCode(HttpServletRequest request){
//		String userplanClassNo = HEAD + studyplanKGS.nextId();
		return R.ok().put(this.courseUserplanClassService.getCode());
	}
	
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseuserplanclass:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "userplanClassNo");
		stringQuery(map, request, "userName");
		stringQuery(map, request, "mobile");
		longQuery(map, request, "deptId");
		stringQuery(map,request,"deptIdList");
        String deptIdList = (String) map.get("deptIdList");
        if (deptIdList!= null && !"".equals(deptIdList.trim())){
            String[] split = deptIdList.split(",");
            map.put("deptIdList", Arrays.asList(split));
        }
		//查询列表数据
		List<CourseUserplanClassPOJO> courseUserplanClassList = courseUserplanClassService.queryPojoList(map);
		int total = courseUserplanClassService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseUserplanClassList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{userplanClassId}")
	@RequiresPermissions("courseuserplanclass:info")
	public R info(@PathVariable("userplanClassId") Long userplanClassId,HttpServletRequest request){
		//根据userplanClassId查询主表信息
		Map<String, Object> map = getMap(request);
		map.put("userplanClassId", userplanClassId);
		CourseUserplanClassPOJO courseUserplanClass = courseUserplanClassService.queryPojoObject(map);
		
		//根据userplanClassId查询子表列表信息
		//查询子表
		Map<String , Object> map2 = new HashMap<String , Object>();
		map2.put("userplanClassId", userplanClassId);
		map2.put("schoolId", SchoolIdUtils.getSchoolId(request));
		List<CourseUserplanClassDetailPOJO> detailList = courseUserplanClassDetailService.queryListByUserplanClassId(map2);
		return R.ok().put("courseUserplanClass",courseUserplanClass).put("detailList", detailList);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存学习计划")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courseuserplanclass:save")
	public R save(@RequestBody CourseUserplanClassPOJO courseUserplanClass , HttpServletRequest request){
		if(StringUtils.isBlank(""+courseUserplanClass.getExamScheduleId())){
			return R.error("[考试时间段]不能为空");
		}
		if(StringUtils.isBlank(""+courseUserplanClass.getUserplanId())){
			return R.error("[订单编号]不能为空");
		}
		if(StringUtils.isNotBlank(courseUserplanClass.getRemark()) && courseUserplanClass.getRemark().length() > 50){
			return R.error("[备注信息]不能超过50个字");
		}
		//平台ID
	    courseUserplanClass.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseUserplanClass.setCreatePerson(getUserId());
		//修改用户
		courseUserplanClass.setModifyPerson(courseUserplanClass.getCreatePerson());
		//学习计划单号
//		courseUserplanClass.setUserplanClassNo("XXJH"+studyplanKGS.nextId());
		courseUserplanClass.setUserplanClassNo(courseUserplanClass.getUserplanClassNo());
		//保存主表
		try {
			courseUserplanClassService.save(courseUserplanClass);
		} catch (UserPlanClassDetailException e) {
//			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改学习计划")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courseuserplanclass:update")
	public R update(@RequestBody CourseUserplanClassPOJO courseUserplanClass , HttpServletRequest request){
		if(StringUtils.isBlank(""+courseUserplanClass.getExamScheduleId())){
			return R.error("[考试时间段]不能为空");
		}
		if(StringUtils.isBlank(""+courseUserplanClass.getUserplanId())){
			return R.error("[订单编号]不能为空");
		}
		if(StringUtils.isNotBlank(courseUserplanClass.getRemark()) && courseUserplanClass.getRemark().length() > 50){
			return R.error("[备注信息]不能超过50个字");
		}
		//平台ID
	    courseUserplanClass.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseUserplanClass.setModifyPerson(getUserId());
		//修改
		try {
			courseUserplanClassService.update(courseUserplanClass);
		} catch (UserPlanClassDetailException e) {
//			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除学习计划")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("courseuserplanclass:delete")
	public R delete(@RequestBody Long[] userplanClassIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",userplanClassIds);
		
		courseUserplanClassService.deleteBatch(map);	
		return R.ok();
	}
	
	
	/**
	 * 审核通过
	 * */
	@SysLog("审核通过学习计划")
	@ResponseBody
	@RequestMapping("/accept")
	//@RequiresPermissions("courseuserplanclass:accept")
	public R accept(@RequestBody Long  userplanClassId){
		courseUserplanClassService.accept(userplanClassId);
		return R.ok();
	}
	/**
	 * 审核未过
	 * */
	@SysLog("审核未通过学习计划")
	@ResponseBody
	@RequestMapping("/reject")
	//@RequiresPermissions("courseuserplanclass:reject")
	public R reject(@RequestBody Long userplanClassId){
		courseUserplanClassService.reject(userplanClassId);
		return R.ok();
	}
}
