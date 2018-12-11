package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.entity.GivingCoursesEntity;
import io.renren.pojo.GivingCoursesPOJO;
import io.renren.service.GivingCoursesService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-31 15:08:46
 */
@Controller
@RequestMapping("givingcourses")
public class GivingCoursesController extends AbstractController {
	@Autowired
	private GivingCoursesService givingCoursesService;
	
	@RequestMapping("/givingcourses.html")
	public String list(){
		return "givingcourses/givingcourses.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
//	@RequiresPermissions("givingcourses:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		map.put("givingType", 0); //只查询赠送类型为0的
		//查询列表数据
		List<GivingCoursesPOJO> givingCoursesList = givingCoursesService.queryPojoList(map);
		
		int total = givingCoursesService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(givingCoursesList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/grouplist")
//	@RequiresPermissions("givingcourses:list")
	public R grouplist(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//查询列表数据
		map.put("givingType", 1); //只查询赠送类型为1的
		List<GivingCoursesPOJO> givingCoursesList = givingCoursesService.queryPojoList(map);
		int total = givingCoursesService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(givingCoursesList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("givingcourses:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id", id);
		//longQuery(map, request, "id");
		GivingCoursesEntity givingCourses = givingCoursesService.queryObject(map);
		return R.ok().put(givingCourses);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
//	@RequiresPermissions("givingcourses:save")
	public R save(@RequestBody GivingCoursesEntity givingCourses , HttpServletRequest request){
		
		/*if(StringUtils.isBlank(givingCourses.getMallGoodsId())){
			return R.error("商品名称必填!");
		}*/
		R check_R= this.CourseCheck(givingCourses); 
		 if (!check_R.isOK()) {
			 return check_R;
		 }
/*		if(null == givingCourses.getMallGoodsId() || givingCourses.getMallGoodsId() <= 0){
			if (givingCourses.getGivingType()!=null&&givingCourses.getGivingType()==1) {
				return R.error("组合班型的商品ID不能为空！");
			}
			return R.error("赠送课程的商品ID必选!");
		}
		if(StringUtils.isBlank(givingCourses.getNcCommodityId())){
			return R.error("NC商品ID必填!");
		}else {
			
			   String regEx = "[ `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		        Pattern p = Pattern.compile(regEx);
		        if (p.matcher(givingCourses.getNcCommodityId()).find()) {
		        	return R.error("NC商品ID不能包含特殊符号！只能包含字母、数字、和_!");
		        }
			
			
		}
			
			
		if(StringUtils.isBlank(givingCourses.getNcCommodityName())){
			return R.error("NC商品名称必填!");
		}
		if(null == givingCourses.getProductId() || givingCourses.getProductId() <= 0){
			return R.error("产品线必选!");
		}*/
		
		
		//默认状态
//	    givingCourses.setStatus(1);
	    //平台ID
//	    givingCourses.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		givingCourses.setCreatePerson(getUserId());
	    //创建时间
		givingCourses.setCreateTime(new Date());
		//保存
		givingCoursesService.save(givingCourses);	
		//删除组合班型对照关系 lintf 2018-09-25
		givingCoursesService.DeleteGroupGoodFromRedis();
		return R.ok();
	}
	
	
	
	private R CourseCheck( GivingCoursesEntity givingCourses) {
		if(null == givingCourses.getMallGoodsId() || givingCourses.getMallGoodsId() <= 0){
			if (givingCourses.getGivingType()!=null&&givingCourses.getGivingType()==1) {
				return R.error("组合班型的商品ID不能为空！");
			}
			return R.error("赠送课程的商品ID必选!");
		}
		if(StringUtils.isBlank(givingCourses.getNcCommodityId())){
			return R.error("NC商品ID必填!");
		}else {
			
			   String regEx = "[ `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		        Pattern p = Pattern.compile(regEx);
		        if (p.matcher(givingCourses.getNcCommodityId()).find()) {
		        	return R.error("NC商品ID不能包含特殊符号！只能包含字母、数字、和_!");
		        }
			
			
		}
			
			
		if(StringUtils.isBlank(givingCourses.getNcCommodityName())){
			return R.error("NC商品名称必填!");
		}
		if(null == givingCourses.getProductId() || givingCourses.getProductId() <= 0){
			return R.error("产品线必选!");
		}
		
		
		
		
		return R.ok();
	}
	
	
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
//	@RequiresPermissions("givingcourses:update")
	public R update(@RequestBody GivingCoursesEntity givingCourses , HttpServletRequest request){
		R check_R= this.CourseCheck(givingCourses); 
		 if (!check_R.isOK()) {
			 return check_R;
		 }
		 //平台ID
//	    givingCourses.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		givingCourses.setModifyPerson(getUserId());
		//修改时间
		givingCourses.setModifyTime(new Date());
		//修改
		givingCoursesService.update(givingCourses);
		//删除组合班型对照关系 lintf 2018-09-25
		givingCoursesService.DeleteGroupGoodFromRedis();
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
//	@RequiresPermissions("givingcourses:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		givingCoursesService.deleteBatch(map);	
		//删除组合班型对照关系 lintf 2018-09-25
		givingCoursesService.DeleteGroupGoodFromRedis();
		return R.ok();
	}
	
	
	
}
