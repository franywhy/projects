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

import io.renren.common.doc.SysLog;
import io.renren.entity.MallClassTypeEntity;
import io.renren.pojo.classType.ClassTypePOJO;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.CourseClassplanService;
import io.renren.service.CourseLiveDetailsService;
import io.renren.service.CourseUserplanService;
import io.renren.service.MallClassTypeService;
import io.renren.service.MallGoodsInfoService;
import io.renren.service.MallOrderService;
import io.renren.service.MsgContentService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 班型档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-21 14:56:53
 */
@Controller
@RequestMapping("/mall/classtype")
public class MallClassTypeController extends AbstractController {
	@Autowired
	private MallClassTypeService mallClassTypeService;
	@Autowired
	private MallGoodsInfoService mallGoodsInfoService;
	@Autowired
	private MallOrderService mallOrderService;
	@Autowired
	private CourseLiveDetailsService courseLiveDetailsService;
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	/*@Autowired
	private MsgContentService msgContentService;*/
	@ResponseBody
	@RequestMapping("/select")
	@RequiresPermissions("mall:classtype:select")
	public R selectList(HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        longQuery(map,request,"productId");
        List<Map<String, Object>> list = this.mallClassTypeService.querySelectList(map);
		logger.debug(list.toString());
		return R.ok().put("data", list);
	}
	
	/**
	 * 班型列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mall:classtype:list")
	public R list(String classtypeName, String productName, Integer page, Integer limit, HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", schoolId);
		map.put("classtypeName", classtypeName);
		map.put("productName", productName);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		//查询列表数据
		List<ClassTypePOJO> mallClassTypeList = mallClassTypeService.queryPojoList(map);
		//List<MallClassTypeEntity> mallClassTypeList = mallClassTypeService.queryList(map);
		int total = mallClassTypeService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallClassTypeList, total, limit, page);
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 班型信息
	 */
	@ResponseBody
	@RequestMapping("/info/{classtypeId}")
	@RequiresPermissions("mall:classtype:info")
	public R info(@PathVariable("classtypeId") Long classtypeId, HttpServletRequest requset){
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", SchoolIdUtils.getSchoolId(requset));
		map.put("classtypeId", classtypeId);
		ClassTypePOJO mallClassType = mallClassTypeService.queryPojoObject(map);
		return R.ok().put("mallClassType", mallClassType);
	}
	
	/**
	 * 新增班型
	 */
	@SysLog("保存班型")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mall:classtype:save")
	public R save(@RequestBody MallClassTypeEntity mallClassType,HttpServletRequest request){
		if(StringUtils.isBlank(mallClassType.getClasstypeName())){
			return R.error("[班型名称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+mallClassType.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		if(!StringUtils.isBlank(mallClassType.getRemake()) && mallClassType.getRemake().length() > 50){
			return R.error("[备注]不能超过50个字！！！");
		}
		mallClassType.setSchoolId(SchoolIdUtils.getSchoolId(request));
		mallClassType.setCreator(getUserId());
		mallClassType.setModifier(mallClassType.getCreator());
		mallClassTypeService.save(mallClassType);	
		return R.ok();
	}
	
	/**
	 * 修改班型信息
	 */
	@SysLog("修改班型")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mall:classtype:update")
	public R update(@RequestBody MallClassTypeEntity mallClassType){
		if(StringUtils.isBlank(mallClassType.getClasstypeName())){
			return R.error("[班型名称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+mallClassType.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		if(!StringUtils.isBlank(mallClassType.getRemake()) && mallClassType.getRemake().length() > 50){
			return R.error("[备注]不能超过50个字！！！");
		}
		mallClassType.setModifier(getUserId());
		mallClassTypeService.update(mallClassType);
		return R.ok();
	}
	
	/**
	 * 删除班型信息
	 *//*
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mall:classtype:delete")
	public R delete(@RequestBody Long[] classtypeIds){
		
		mallClassTypeService.deleteBatch(classtypeIds);	
		if(goodsService.checkClassType(classTypeId)){
			return R.error("删除失败：商品引用！");
		}
		if(goodsService.checkClassType(classTypeId)){
			return R.error("删除失败：商品引用！");
		}
		return R.ok();
	}*/
	/**
	 * 单个删除班型信息
	 */
	@SysLog("删除班型")
	@ResponseBody
	@RequestMapping("/delete/{classtypeId}")
	@RequiresPermissions("mall:classtype:delete")
	public R delete(@PathVariable("classtypeId") Long classtypeId){
		Map<String, Object> map = new HashMap<>();
		map.put("classtypeId", classtypeId);
		ArrayList<String> exceptList = new ArrayList<String>();
		String exceptMsg = "删除失败的具体原因如下:";
		String errMsg="";
		if(mallGoodsInfoService.checkClassType(classtypeId)>0){
			exceptMsg="商品中有引用！";
			exceptList.add(exceptMsg);
		}
		if(courseUserplanService.checkClassType(classtypeId)>0){
				exceptMsg="学员规划有引用！";
				exceptList.add(exceptMsg);
		}
		/*if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_userplan_class_type_id)){
			exceptMsg="学员规划表有引用！";
			exceptList.add(exceptMsg);
	    }*/
		if(mallOrderService.checkClassType(classtypeId)>0){
			exceptMsg="订单有引用！";
			exceptList.add(exceptMsg);
	    }
		if(courseLiveDetailsService.checkClassType(classtypeId)>0){
			exceptMsg="课程直播明细表有引用！";
			exceptList.add(exceptMsg);
	    }
		if(courseClassplanLivesService.checkClassType(classtypeId)>0){
			exceptMsg="排课计划明细表有引用！";
			exceptList.add(exceptMsg);
	    }
		if(msgContentService.checkClassType(classtypeId)>0){
			exceptMsg="消息表有引用！";
			exceptList.add(exceptMsg);
	    }
		for (int i = 0; i < exceptList.size(); i++)
		{
			errMsg += exceptList.get(i) + "<br>";
		}
		if(errMsg.equals("")){
			mallClassTypeService.deleteClassType(map);
			return R.ok();
		}
		else{
			return R.error(errMsg);
		}
	}
	/**
	 * 禁用
	 */
	@SysLog("禁用班型")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mall:classtype:pause")
	public R pause(@RequestBody Long[] classtypeIds){
		mallClassTypeService.pause(classtypeIds);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用班型")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mall:classtype:resume")
	public R resume(@RequestBody Long[] classtypeIds){
		mallClassTypeService.resume(classtypeIds);
		return R.ok();
	}
	
}
