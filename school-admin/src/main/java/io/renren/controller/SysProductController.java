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
import io.renren.entity.SysProductEntity;
import io.renren.service.SysProductService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 产品线
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 17:12:41
 */
@Controller
@RequestMapping("sysproduct")
public class SysProductController extends AbstractController {
	@Autowired
	private SysProductService sysProductService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sysproduct:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "productName");
		//查询列表数据
		List<SysProductEntity> sysProductList = sysProductService.queryList(map);
		int total = sysProductService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(sysProductList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 产品列表
	 */
	@ResponseBody
	@RequestMapping("/select")
	public R select(){
		Map<String, Object> map = new HashMap<String, Object>();
		//查询列表数据
		List<SysProductEntity> list = this.sysProductService.queryList1(map);
		
		return R.ok().put("list", list);
	}
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{productId}")
	@RequiresPermissions("sysproduct:info")
	public R info(@PathVariable("productId") Long productId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("productId", productId);
		SysProductEntity sysProduct = sysProductService.queryObject(map);
		return R.ok().put(sysProduct);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存产品线")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("sysproduct:save")
	public R save(@RequestBody SysProductEntity sysProduct , HttpServletRequest request){
		if(StringUtils.isBlank(sysProduct.getProductName())){
			return R.error("[名称]不能为空！！！");
		}
		//默认状态
	    sysProduct.setStatus(1);
		//保存
		sysProductService.save(sysProduct);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改产品线")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sysproduct:update")
	public R update(@RequestBody SysProductEntity sysProduct , HttpServletRequest request){
		if(StringUtils.isBlank(sysProduct.getProductName())){
			return R.error("[名称]不能为空！！！");
		}
		//修改
		sysProductService.update(sysProduct);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除产品线")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("sysproduct:delete")
	public R delete(@RequestBody Long[] productIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",productIds);
		sysProductService.deleteBatch(map);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用产品线")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("sysproduct:pause")
	public R pause(@RequestBody Long[] productIds){
		sysProductService.pause(productIds);
		return R.ok();
	}
	/**
	 * 启用
	 */
	@SysLog("启用产品线")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("sysproduct:resume")
	public R resume(@RequestBody Long[] productIds){
		sysProductService.resume(productIds);
		return R.ok();
	}
	
}
