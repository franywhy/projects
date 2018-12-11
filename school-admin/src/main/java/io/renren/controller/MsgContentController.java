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
import io.renren.entity.MsgContentEntity;
import io.renren.pojo.MsgContentPOJO;
import io.renren.service.MsgContentService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 消息表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-15 17:00:41
 */
@Controller
@RequestMapping("msgcontent")
public class MsgContentController extends AbstractController {
	@Autowired
	private MsgContentService msgContentService;
	
	@RequestMapping("/msgcontent.html")
	public String list(){
		return "msgcontent/msgcontent.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("msgcontent:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "contentTitle");
		//查询列表数据
		List<MsgContentPOJO> msgContentList = msgContentService.queryPojoList(map);
		int total = msgContentService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(msgContentList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{contentId}")
	@RequiresPermissions("msgcontent:info")
	public R info(@PathVariable("contentId") Long contentId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		//longQuery(map, request, "contentId");
		map.put("contentId", contentId);
		MsgContentPOJO msgContent = msgContentService.queryPojoObject(map);
		return R.ok().put(msgContent);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存消息")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("msgcontent:save")
	public R save(@RequestBody MsgContentEntity msgContent , HttpServletRequest request){
		//非空校验
		if(StringUtils.isBlank(msgContent.getContentTitle())){
			return R.error("[标题]不能为空！！！");
		}
		if(msgContent.getContentTitle().length() < 10 || msgContent.getContentTitle().length() > 100 ){
			return R.error("[标题]少于10个字或者大于100个字！！！");
		}
		if(msgContent.getType() == 1){
			if(StringUtils.isBlank(msgContent.getClassplanId())){
				return R.error("[排课]不能为空！！！");
			}
			if(StringUtils.isBlank(msgContent.getClasstypeIds())){
				return R.error("[班型]不能为空！！！");
			}
		}
		if(msgContent.getType() == 0){
			msgContent.setClassplanId("");
			msgContent.setClasstypeIds("");;
		}
		if(msgContent.getSendType() == 2){
			if(StringUtils.isBlank(""+msgContent.getTimestamp())){
				return R.error("[发送时间]不能为空！！！");
			}
		}
	    //平台ID
	    msgContent.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		msgContent.setCreatePerson(getUserId());
		//修改用户
		msgContent.setModifyPerson(msgContent.getCreatePerson());
		//保存
		msgContentService.save(msgContent);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改消息")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("msgcontent:update")
	public R update(@RequestBody MsgContentEntity msgContent , HttpServletRequest request){
		//非空校验
		if(StringUtils.isBlank(msgContent.getContentTitle())){
			return R.error("[标题]不能为空！！！");
		}
		if(msgContent.getContentTitle().length() < 10 || msgContent.getContentTitle().length() > 100 ){
			return R.error("[标题]少于10个字或者大于100个字！！！");
		}
		if(msgContent.getType() == 1){
			if(StringUtils.isBlank(msgContent.getClassplanId())){
				return R.error("[排课]不能为空！！！");
			}
			if(StringUtils.isBlank(msgContent.getClasstypeIds())){
				return R.error("[班型]不能为空！！！");
			}
		}
		if(msgContent.getType() == 0){
			msgContent.setClassplanId("");
			msgContent.setClasstypeIds("");
		}
		if(msgContent.getSendType() == 2){
			if(StringUtils.isBlank(""+msgContent.getTimestamp())){
				return R.error("[发送时间]不能为空！！！");
			}
		}
		//平台ID
	    msgContent.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		msgContent.setModifyPerson(getUserId());
		//修改
		msgContentService.update(msgContent);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除消息")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("msgcontent:delete")
	public R delete(@RequestBody Long[] contentIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",contentIds);
		msgContentService.deleteBatch(map);	
		return R.ok();
	}
	
}
