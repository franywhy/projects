package io.renren.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.entity.MallProfessionEntity;
import io.renren.service.AppBannerService;
import io.renren.service.CourseGuideService;
import io.renren.service.CourseUserplanService;
import io.renren.service.MallGoodsInfoService;
import io.renren.service.MallOrderService;
import io.renren.service.MallProfessionService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 专业档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-21 16:15:19
 */
@Controller
@RequestMapping("mallprofession")
public class MallProfessionController extends AbstractController {
	@Autowired
	private MallProfessionService mallProfessionService;
	@Autowired
	private MallGoodsInfoService mallGoodsInfoService;
	@Autowired
	private MallOrderService mallOrderService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private CourseGuideService courseGuideService;
	@Autowired
	private AppBannerService appBannerService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallprofession:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "professionName");
		//查询列表数据
		List<MallProfessionEntity> mallProfessionList = mallProfessionService.queryList(map);
		int total = mallProfessionService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallProfessionList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info")
	@RequiresPermissions("mallprofession:info")
	public R info(HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		longQuery(map, request, "professionId");
		MallProfessionEntity mallProfession = mallProfessionService.queryObject(map);
		return R.ok().put(mallProfession);
	}
	/**
	 * 保存
	 */
	@SysLog("保存专业")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mallprofession:save")
	public R save(@RequestBody MallProfessionEntity mallProfession , HttpServletRequest request){
		if(StringUtils.isBlank(mallProfession.getProfessionName())){
			return R.error("[专业名称]不能为空！！！");
		}
//		if(StringUtils.isBlank(mallProfession.getPic())){
//			return R.error("[专业图片]不能为空！！！");
//		}
//		if(StringUtils.isBlank(mallProfession.getAppPic())){
//			return R.error("[APP图片]不能为空！！！");
//		}
//		if(StringUtils.isBlank(mallProfession.getBrandPic())){
//			return R.error("[品牌封面]不能为空！！！");
//		}
//		if(StringUtils.isBlank(mallProfession.getRemark())){
//			return R.error("[专业简介]不能为空！！！");
//		}
//		if(StringUtils.isBlank(mallProfession.getAuditionUrl())){
//			return R.error("[试听地址]不能为空！！！");
//		}
		if(null == mallProfession.getProductId()){
			return R.error("[产品线]不能为空！！！");
		}
		if(StringUtils.isBlank(mallProfession.getOrderNum()+"")){
			return R.error("[排序]不能为空！！！");
		}
		if(!StringUtils.isBlank(mallProfession.getRemark()) && mallProfession.getRemark().length() > 100){
			return R.error("[专业简介]不得超过100个字！！！");
		}
		//默认状态
	    mallProfession.setStatus(1);
	    //平台ID
	    mallProfession.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		mallProfession.setCreatePerson(getUserId());
		mallProfession.setCreationTime(new Date());
		//保存
		mallProfessionService.save(mallProfession);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改专业")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mallprofession:update")
	public R update(@RequestBody MallProfessionEntity mallProfession , HttpServletRequest request){
		if(StringUtils.isBlank(mallProfession.getProfessionName())){
			return R.error("[专业名称]不能为空！！！");
		}
//		if(StringUtils.isBlank(mallProfession.getPic())){
//			return R.error("[专业图片]不能为空！！！");
//		}
//		if(StringUtils.isBlank(mallProfession.getAppPic())){
//			return R.error("[APP图片]不能为空！！！");
//		}
//		if(StringUtils.isBlank(mallProfession.getBrandPic())){
//			return R.error("[品牌封面]不能为空！！！");
//		}
//		if(StringUtils.isBlank(mallProfession.getRemark())){
//			return R.error("[专业简介]不能为空！！！");
//		}
//		if(StringUtils.isBlank(mallProfession.getAuditionUrl())){
//			return R.error("[试听地址]不能为空！！！");
//		}
		if(StringUtils.isBlank(mallProfession.getOrderNum()+"")){
			return R.error("[排序]不能为空！！！");
		}
		if(null == mallProfession.getProductId()){
			return R.error("[产品线]不能为空！！！");
		}
		if(!StringUtils.isBlank(mallProfession.getRemark()) && mallProfession.getRemark().length() > 100){
			return R.error("[专业简介]不得超过100个字！！！");
		}
		 //平台ID
	    mallProfession.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		mallProfession.setModifyPerson(getUserId());
		mallProfession.setModifiedTime(new Date());
		//修改
		mallProfessionService.update(mallProfession);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除专业")
	@ResponseBody
	@RequestMapping("/delete/{professionId}")
	@RequiresPermissions("mallprofession:delete")
	public R delete(@PathVariable("professionId") Long professionId,HttpServletRequest request){
		Map<String, Object> map = getMap(request); 
		Map<String, Object> map1 = getMap(request);
		map.put("professionId", professionId);
		map1.put("value", professionId);
		ArrayList<String> exceptList = new ArrayList<String>();
		String exceptMsg = "删除失败的具体原因如下:";
		String errMsg="";
		if(mallGoodsInfoService.checkProfession(professionId)>0){
			exceptMsg="商品中有引用！";
			exceptList.add(exceptMsg);
		}
		if(courseUserplanService.checkProfession(professionId)>0){
				exceptMsg="学员规划有引用！";
				exceptList.add(exceptMsg);
		}
		if(sysCheckQuoteService.checkQuote(map1 , QuoteConstant.mall_class_profession_id)){
			exceptMsg="班级有引用！";
			exceptList.add(exceptMsg);
		}
		/*if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_userplan_profession_id)){
			exceptMsg="学员规划表有引用！";
			exceptList.add(exceptMsg);
	    }*/
		if(mallOrderService.checkProfession(professionId)>0){
			exceptMsg="订单有引用！";
			exceptList.add(exceptMsg);
	    }
		if(courseGuideService.checkProfession(professionId)>0){
			exceptMsg="移动端banner表有引用！";
			exceptList.add(exceptMsg);
	    }
		if(appBannerService.checkProfession(professionId)>0){
			exceptMsg="流程指南表有引用！";
			exceptList.add(exceptMsg);
	    }
		for (int i = 0; i < exceptList.size(); i++)
		{
			errMsg += exceptList.get(i) + "<br>";
		}
		if(errMsg.equals("")){
			mallProfessionService.delete(map);	
			return R.ok();
		}
		else{
			return R.error(errMsg);
		}
	}
	/**
	 * 禁用
	 */
	@SysLog("禁用专业")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mallprofession:pause")
	public R pause(@RequestBody Long[] professionIds){
		mallProfessionService.pause(professionIds);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用专业")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mallprofession:resume")
	public R resume(@RequestBody Long[] professionIds){
		mallProfessionService.resume(professionIds);
		return R.ok();
	}
}
