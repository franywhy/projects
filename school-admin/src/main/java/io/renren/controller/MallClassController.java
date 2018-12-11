package io.renren.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import io.renren.common.doc.ParamKey;
import io.renren.common.doc.SysLog;
import io.renren.entity.MallClassEntity;
//import io.renren.entity.MallProfessionEntity;
import io.renren.pojo.Class.ClassPOJO;
import io.renren.service.MallClassService;
import io.renren.service.SysCheckQuoteService;
//import io.renren.service.MallProfessionService;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 班级档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 10:24:52
 */
@Controller
@RequestMapping("/mall/class")
public class MallClassController extends AbstractController {
	@Autowired
	private MallClassService mallClassService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	/**
	 * 班级下拉列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectList")
	public R professionList(){
		return R.ok().put("data", mallClassService.findClassList());
	}
	
	/**
	 * 班级列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mall:class:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "className");
		stringQuery(map, request, "teacherName");
		longQuery(map, request, "levelId");
		longQuery(map, request, "areaId");
		longQuery(map, request, "professionId");
		longQuery(map, request, "userId");
		//查询列表数据
		List<ClassPOJO> mallClassList = mallClassService.queryPojoList(map);
		int total = mallClassService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(mallClassList, total, request);	
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 班级列表信息
	 */
	@ResponseBody
	@RequestMapping("/info/{classId}")
	@RequiresPermissions("mall:class:info")
	public R info(@PathVariable("classId") Long classId, HttpServletRequest requset){
		String schoolId = SchoolIdUtils.getSchoolId(requset);
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", schoolId);
		map.put("classId", classId);
		ClassPOJO mallClass = mallClassService.queryPojoObject(map);
		return R.ok().put("mallClass", mallClass);
	}
	
	/**
	 * 新增班级
	 */
	@SysLog("保存班级")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mall:class:save")
	public R save(@RequestBody MallClassEntity mallClass ,HttpServletRequest request){
		if(StringUtils.isBlank(mallClass.getAreaId()+"")){
			return R.error("[省份]不能为空");
		}
		if(StringUtils.isBlank(mallClass.getProfessionId()+"")){
			return R.error("[专业]不能为空");
		}
		if(StringUtils.isBlank(mallClass.getLevelId()+"")){
			return R.error("[学历]不能为空");
		}
		if(StringUtils.isBlank(mallClass.getClassName())){
			return R.error("[班級名称]不能为空");
		}
		if(StringUtils.isBlank(mallClass.getUserId()+"")){
			return R.error("[班主任]不能为空");
		}
		if(!StringUtils.isBlank(mallClass.getRemake()) && mallClass.getRemake().length() > 50){
			return R.error("[备注]不能超过50个字");
		}
		if(null == mallClass.getProductId()){
			return R.error("[产品线]不能超过50个字");
		}
		mallClass.setSchoolId(SchoolIdUtils.getSchoolId(request));
		mallClass.setCreator(getUserId());
		mallClass.setModifier(mallClass.getCreator());
		mallClassService.save(mallClass);	
		
		return R.ok();
	}
	
	/**
	 * 修改班级信息
	 */
	@SysLog("修改班级")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mall:class:update")
	public R update(@RequestBody MallClassEntity mallClass){
		if(StringUtils.isBlank(mallClass.getAreaId()+"")){
			return R.error("[省份]不能为空");
		}
		if(StringUtils.isBlank(mallClass.getProfessionId()+"")){
			return R.error("[专业]不能为空");
		}
		if(StringUtils.isBlank(mallClass.getLevelId()+"")){
			return R.error("[学历]不能为空");
		}
		if(StringUtils.isBlank(mallClass.getClassName())){
			return R.error("[班級名称]不能为空");
		}
		if(StringUtils.isBlank(mallClass.getUserId()+"")){
			return R.error("[班主任]不能为空");
		}
		if(!StringUtils.isBlank(mallClass.getRemake()) && mallClass.getRemake().length() > 50){
			return R.error("[备注]不能超过50个字");
		}
		if(null == mallClass.getProductId()){
			return R.error("[产品线]不能超过50个字");
		}
		mallClass.setModifier(getUserId());
		mallClassService.update(mallClass);
		return R.ok();
	}
	
	/**
	 * 删除班级信息
	 */
	@SysLog("删除班级")
	@ResponseBody
	@RequestMapping("/delete/{classId}")
	@RequiresPermissions("mall:class:delete")
	public R delete(@PathVariable("classId") Long classId,HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("value", classId);
		ArrayList<String> exceptList = new ArrayList<String>();
		String exceptMsg = "删除失败的具体原因如下:";
		String errMsg="";
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.mall_order_class_id)){
			exceptMsg="订单有引用！";
			exceptList.add(exceptMsg);
		}
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_userplan_class_id)){
				exceptMsg="班级有引用！";
				exceptList.add(exceptMsg);
		}
		for (int i = 0; i < exceptList.size(); i++)
		{
			errMsg += exceptList.get(i) + "<br>";
		}
		if(errMsg.equals("")){
			mallClassService.delete(map);	
			return R.ok();
		}
		else{
			return R.error(errMsg);
		}
		
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用班级")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mall:class:pause")
	public R pause(@RequestBody Long[] classIds){
		mallClassService.pause(classIds);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用班级")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mall:class:resume")
	public R resume(@RequestBody Long[] classIds){
		mallClassService.resume(classIds);
		return R.ok();
	}
	/**
	 * 指定默认班级
	 * @param request
	 * @return
	 */
	@SysLog("指定默认班级")
	@ResponseBody
	@RequestMapping("/updateDefaultClass")
	@RequiresPermissions("mall:class:updateDefaultClass")
	public R updateDefaultClass(HttpServletRequest request){
		Long classId = ServletRequestUtils.getLongParameter(request,  "classId", 0);
		if(classId == 0){
			return R.error("请选择班级!");
		}
		
		Map<String, Object> map = getMap(request);
		map.put("classId", classId);
		MallClassEntity mallClass = mallClassService.queryObject(map);
		if(mallClass.getProductId() != 1){
			this.mallClassService.updateDefaultClassNotZK(map);
			return R.ok();
		}else{
			this.mallClassService.updateDefaultClass(map);
			return R.ok();
		}
		
	}
	
}
