package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.ClassGoodsDeptsEntity;
import io.renren.pojo.ClassGoodsDeptsPOJO;
import io.renren.service.ClassGoodsDeptsService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 班级-商品-部门对照表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-11-29 15:45:01
 */
@Controller
@RequestMapping("classgoodsdepts")
public class ClassGoodsDeptsController extends AbstractController {
	@Autowired
	private ClassGoodsDeptsService classGoodsDeptsService;
	
	@RequestMapping("/classgoodsdepts.html")
	public String list(){
		return "classgoodsdepts/classgoodsdepts.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("classgoodsdepts:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "className");
		stringQuery(map, request, "teacherName");
		stringQuery(map, request, "goodName");
		stringQuery(map, request, "deptName");
		//查询列表数据
		List<ClassGoodsDeptsPOJO> classGoodsDeptsList = classGoodsDeptsService.queryListPOJO(map);
		int total = classGoodsDeptsService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(classGoodsDeptsList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("classgoodsdepts:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id", id);
		ClassGoodsDeptsPOJO classGoodsDepts = classGoodsDeptsService.queryObjectPOJO(map);
		return R.ok().put(classGoodsDepts);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存班级-商品-校区")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("classgoodsdepts:save")
	public R save(@RequestBody ClassGoodsDeptsEntity classGoodsDepts , HttpServletRequest request){
		
		if(null == classGoodsDepts.getClassId()){
			return R.error("[班级]不能为空");
		}
		if(null == classGoodsDepts.getGoodId()){
			return R.error("[商品]不能为空");
		}
		if(null == classGoodsDepts.getDeptId()){
			return R.error("[部门]不能为空");
		}
		int goodAndDeptNum = this.classGoodsDeptsService.queryNumBydeptIdAndGoodId(classGoodsDepts.getGoodId(),classGoodsDepts.getDeptId());
		if(goodAndDeptNum > 0){
			return R.error("你选择的商品和校区已有对应的默认班级存在！！！");
		}
		int classNum = this.classGoodsDeptsService.queryNumByGoodIdAndDeptId(classGoodsDepts.getClassId(),classGoodsDepts.getGoodId(),classGoodsDepts.getDeptId());
		if(classNum > 0){
			return R.error("你选择的班级已有对应的商品和校区存在！！！");
		}
		
	    //创建用户
		classGoodsDepts.setCreator(getUserId());
	    //创建时间
		classGoodsDepts.setCreationTime(new Date());
		//保存
		classGoodsDeptsService.save(classGoodsDepts);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改班级-商品-校区")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("classgoodsdepts:update")
	public R update(@RequestBody ClassGoodsDeptsEntity classGoodsDepts , HttpServletRequest request){
		if(null == classGoodsDepts.getClassId()){
			return R.error("[班级]不能为空");
		}
		if(null == classGoodsDepts.getGoodId()){
			return R.error("[商品]不能为空");
		}
		if(null == classGoodsDepts.getDeptId()){
			return R.error("[部门]不能为空");
		}
		int goodAndDeptNum = this.classGoodsDeptsService.queryNumBydeptIdAndGoodId(classGoodsDepts.getGoodId(),classGoodsDepts.getDeptId());
		if(goodAndDeptNum > 0){
			return R.error("你选择的商品和校区已有对应的默认班级存在，请返回删除选中的记录后再新增！！！");
		}
		int classNum = this.classGoodsDeptsService.queryNumByGoodIdAndDeptId(classGoodsDepts.getClassId(),classGoodsDepts.getGoodId(),classGoodsDepts.getDeptId());
		if(classNum > 0){
			return R.error("你选择的班级已有对应的商品和校区存在！！！");
		}
		
		//修改用户
		classGoodsDepts.setModifier(getUserId());
		//修改时间
		classGoodsDepts.setModifyTime(new Date());
		//修改
		classGoodsDeptsService.update(classGoodsDepts);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除班级-商品-校区")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("classgoodsdepts:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		classGoodsDeptsService.deleteBatch(map);	
		return R.ok();
	}
	
}
