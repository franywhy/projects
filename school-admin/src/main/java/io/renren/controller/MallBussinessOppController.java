package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.MallBussinessOppEntity;
import io.renren.service.MallBussinessOppService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 商机记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-28 15:14:18
 */
@Controller
@RequestMapping("mallbussinessopp")
public class MallBussinessOppController extends AbstractController {
	@Autowired
	private MallBussinessOppService mallBussinessOppService;
	
	@RequestMapping("/mallbussinessopp.html")
	public String list(){
		return "mallbussinessopp/mallbussinessopp.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallbussinessopp:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "nickName");
		stringQuery(map, request, "userMobile");
		//查询列表数据
		List<MallBussinessOppEntity> mallBussinessOppList = mallBussinessOppService.queryList(map);
		int total = mallBussinessOppService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallBussinessOppList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("mallbussinessopp:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		longQuery(map, request, "id");
		MallBussinessOppEntity mallBussinessOpp = mallBussinessOppService.queryObject(map);
		return R.ok().put(mallBussinessOpp);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存商机记录")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mallbussinessopp:save")
	public R save(@RequestBody MallBussinessOppEntity mallBussinessOpp , HttpServletRequest request){
		/*//默认状态
	    mallBussinessOpp.setStatus(1);
	    //平台ID
	    mallBussinessOpp.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		mallBussinessOpp.setCreatePerson(getUserId());
	    //创建时间
		mallBussinessOpp.setCreationTime(new Date());
		//保存
		mallBussinessOppService.save(mallBussinessOpp);*/	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改商机记录")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mallbussinessopp:update")
	public R update(@RequestBody MallBussinessOppEntity mallBussinessOpp , HttpServletRequest request){
		/* //平台ID
	    mallBussinessOpp.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		mallBussinessOpp.setModifyPerson(getUserId());
		//修改时间
		mallBussinessOpp.setModifiedTime(new Date());
		//修改
		mallBussinessOppService.update(mallBussinessOpp);*/
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除商机记录")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mallbussinessopp:delete")
	public R delete(@RequestBody Integer[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		mallBussinessOppService.deleteBatch(map);	
		return R.ok();
	}
	
}
