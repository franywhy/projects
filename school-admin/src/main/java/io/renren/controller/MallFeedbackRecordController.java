package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import io.renren.entity.MallFeedbackRecordEntity;
import io.renren.pojo.feedbackrecord.FeedbackDisplayPOJO;
import io.renren.pojo.feedbackrecord.FeedbackUpdatePOJO;
import io.renren.service.MallFeedbackRecordService;
import io.renren.service.SysUserService;
import io.renren.utils.Constant;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import io.renren.utils.ShiroUtils;


/**
 * 反馈类型文档档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-24 16:39:34
 */
@Controller
@RequestMapping("/mall/mallfeedbackrecord")
public class MallFeedbackRecordController extends AbstractController{
	@Autowired
	private MallFeedbackRecordService mallFeedbackRecordService;
	@Autowired
	private SysUserService sysUserService;
	
	
	@RequestMapping("/mallfeedbackrecord.html")
	public String list(){
		return "mallfeedbackrecord/mallfeedbackrecord.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallfeedbackrecord:list")
	public R list(Integer page, Integer limit,HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "feedbackType");
//		Map<String, Object> map = new HashMap<>();
//		map.put("offset", (page - 1) * limit);
//		map.put("limit", limit);
//		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		//查询列表数据
		List<MallFeedbackRecordEntity> mallFeedbackRecordList = mallFeedbackRecordService.queryList(map);
		/*List<FeedbackDisplayPOJO> pojoList = new LinkedList<FeedbackDisplayPOJO>();
		for(MallFeedbackRecordEntity entity:mallFeedbackRecordList){
			FeedbackDisplayPOJO pojo = new FeedbackDisplayPOJO();
			pojo.setId(entity.getId());
			pojo.setFeedbackType(entity.getFeedbackType());
			pojo.setFeedbackContent(entity.getFeedbackContent());
			pojo.setCreatePerson(sysUserService.queryObject(entity.getCreatePerson()).getUsername());
			pojo.setModifyPerson(sysUserService.queryObject(entity.getModifyPerson()).getUsername());
			pojo.setCreateTime(entity.getCreateTime());
			pojo.setModifyTime(entity.getModifyTime());
			pojo.setStatus(entity.getStatus());
			pojoList.add(pojo);
		}*/
		int total = mallFeedbackRecordService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallFeedbackRecordList, total, request);	
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("mallfeedbackrecord:info")
	public R info(@PathVariable("id") Long id){
		MallFeedbackRecordEntity feedback = mallFeedbackRecordService.queryObject(id);
		FeedbackUpdatePOJO pojo  = new FeedbackUpdatePOJO();
		pojo.setId(id);
		pojo.setFeedbackType(feedback.getFeedbackType());
		pojo.setFeedbackContent(feedback.getFeedbackContent());
		pojo.setCreatePerson(sysUserService.queryObject(feedback.getCreatePerson()).getUsername());
		pojo.setModifyPerson(sysUserService.queryObject(feedback.getModifyPerson()).getUsername());
		pojo.setCreateTime(feedback.getCreateTime());
		pojo.setModifyTime(feedback.getModifyTime());	
		return R.ok().put("mallFeedbackRecord", pojo);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存反馈类型文档")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mallfeedbackrecord:save")
	public R save(@RequestBody FeedbackUpdatePOJO pojo,HttpServletRequest request){
//		反馈类型
		if(StringUtils.isBlank(pojo.getFeedbackType())){
			return R.error("反馈类型不能为空!");
		}
		//反馈内容
		if(StringUtils.isBlank(pojo.getFeedbackContent())){
			return R.error("反馈内容不能为空!");
		}
		MallFeedbackRecordEntity feedback = new MallFeedbackRecordEntity();
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		feedback.setFeedbackType(pojo.getFeedbackType());
		feedback.setFeedbackContent(pojo.getFeedbackContent());
		feedback.setCreatePerson(ShiroUtils.getUserId());
		feedback.setModifyPerson(ShiroUtils.getUserId());
		feedback.setCreateTime(date);
		feedback.setModifyTime(date);
		feedback.setSchoolId(SchoolIdUtils.getSchoolId(request));
		feedback.setStatus((int)Constant.Status.RESUME.getValue());
		mallFeedbackRecordService.save(feedback);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改反馈类型文档")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mallfeedbackrecord:update")
	public R update(@RequestBody FeedbackUpdatePOJO pojo){
		//反馈类型
		if(StringUtils.isBlank(pojo.getFeedbackType())){
			return R.error("反馈类型不能为空!");
		}
		//反馈内容
		if(StringUtils.isBlank(pojo.getFeedbackContent())){
			return R.error("反馈内容不能为空!");
		}
		MallFeedbackRecordEntity feedback = new MallFeedbackRecordEntity();
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		feedback.setId(pojo.getId());
		feedback.setFeedbackType(pojo.getFeedbackType());
		feedback.setFeedbackContent(pojo.getFeedbackContent());
		feedback.setModifyPerson(ShiroUtils.getUserId());
		feedback.setModifyTime(pojo.getModifyTime());
		mallFeedbackRecordService.update(feedback);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("修改反馈类型文档")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mallfeedbackrecord:delete")
	public R delete(@RequestBody Long[] ids){
		mallFeedbackRecordService.deleteBatch(ids);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用反馈类型文档")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mallfeedbackrecord:pause")
	public R pause(@RequestBody Long[] ids){
		mallFeedbackRecordService.pause(ids);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用反馈类型文档")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mallfeedbackrecord:resume")
	public R resume(@RequestBody Long[] ids){
		mallFeedbackRecordService.resume(ids);
		return R.ok();
	}
	
}
