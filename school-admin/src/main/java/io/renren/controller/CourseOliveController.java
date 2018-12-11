package io.renren.controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseOliveEntity;
import io.renren.pojo.CourseOlivePOJO;
import io.renren.service.CourseOliveService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 公开课
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 15:01:23
 */
@Controller
@RequestMapping("courseolive")
public class CourseOliveController extends AbstractController {
	@Autowired
	private CourseOliveService courseOliveService;
	
	@RequestMapping("/courseolive.html")
	public String list(){
		return "courseolive/courseolive.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseolive:list")
	public R list(String oliveTitle, HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		map.put("oliveTitle", oliveTitle);
		//查询列表数据
		List<CourseOlivePOJO> courseOliveList = courseOliveService.queryPojoList(map);
		int total = courseOliveService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseOliveList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{oliveId}")
	@RequiresPermissions("courseolive:info")
	public R info(@PathVariable("oliveId") Long oliveId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("oliveId", oliveId);
		CourseOlivePOJO courseOlive = courseOliveService.queryPojoObject(map);
		return R.ok().put(courseOlive);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存公开课")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courseolive:save")
	public R save(@RequestBody CourseOliveEntity courseOlive , HttpServletRequest request){
		if(StringUtils.isBlank(courseOlive.getOliveTitle())){
			return R.error("[标题]不能为空！！！");
		}
		if(StringUtils.isBlank(courseOlive.getOliveIntroduction())){
			return R.error("[公开课简介]不能为空！！！");
		}
		if(StringUtils.isBlank(courseOlive.getCardTitle())){
			return R.error("[卡片标题]不能为空！！！");
		}
		if(StringUtils.isBlank(courseOlive.getCardSubtitle())){
			return R.error("[卡片副标题]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getTeacherName())){
			return R.error("[讲师姓名]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getTeacherAvatar())){
			return R.error("[讲师头像]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getTeacherIntroduction())){
			return R.error("[讲师简介]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getSuitable())){
			return R.error("[适用对象]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getOliveStartTime())){
			return R.error("[开始时间]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getOliveEndTime())){
			return R.error("[结束时间]不能为空！！！");
		}
		if(StringUtils.isBlank(courseOlive.getOlivePic())){
			return R.error("[封面]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getLiveRoomId())){
			return R.error("[直播间]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getContent())){
			return R.error("[课程内容]不能为空！！！");
		}
		if(!StringUtils.isBlank(courseOlive.getOliveTitle()) && courseOlive.getOliveTitle().length() > 100){
			return R.error("[标题]不能超过100个字！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getAuthorityId())){
			return R.error("[公开课类型]不能为空！！！");
		}
	    //平台ID
	    courseOlive.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseOlive.setCreatePerson(getUserId());
		//修改用户
		courseOlive.setModifyPerson(courseOlive.getCreatePerson());
		//保存
		courseOliveService.save(courseOlive);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改公开课")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courseolive:update")
	public R update(@RequestBody CourseOliveEntity courseOlive , HttpServletRequest request){
		if(StringUtils.isBlank(courseOlive.getOliveTitle())){
			return R.error("[标题]不能为空！！！");
		}
		if(StringUtils.isBlank(courseOlive.getOliveIntroduction())){
			return R.error("[公开课简介]不能为空！！！");
		}
		if(StringUtils.isBlank(courseOlive.getCardTitle())){
			return R.error("[卡片标题]不能为空！！！");
		}
		if(StringUtils.isBlank(courseOlive.getCardSubtitle())){
			return R.error("[卡片副标题]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getTeacherName())){
			return R.error("[讲师姓名]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getTeacherAvatar())){
			return R.error("[讲师头像]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getTeacherIntroduction())){
			return R.error("[讲师简介]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getSuitable())){
			return R.error("[适用对象]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getOliveStartTime())){
			return R.error("[开始时间]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getOliveEndTime())){
			return R.error("[结束时间]不能为空！！！");
		}
		if(StringUtils.isBlank(courseOlive.getOlivePic())){
			return R.error("[封面]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getLiveRoomId())){
			return R.error("[直播间]不能为空！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getContent())){
			return R.error("[课程内容]不能为空！！！");
		}
		if(!StringUtils.isBlank(courseOlive.getOliveTitle()) && courseOlive.getOliveTitle().length() > 100){
			return R.error("[标题]不能超过100个字！！！");
		}
		if(StringUtils.isBlank(""+courseOlive.getAuthorityId())){
			return R.error("[公开课类型]不能为空！！！");
		}
		//平台ID
	    courseOlive.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseOlive.setModifyPerson(getUserId());
		//修改时间
		courseOlive.setModifiedTime(new Date());
		//修改
		courseOliveService.update(courseOlive);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除公开课")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("courseolive:delete")
	public R delete(@RequestBody Long[] oliveIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("list",oliveIds);
		courseOliveService.deleteBatch(map);	
		return R.ok();
	}

	/**
	 * 下架
	 */
	@SysLog("下架公开课")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("courseolive:pause")
	public R pause(@RequestBody Long[] oliveIds){
		courseOliveService.pause(oliveIds);
		return R.ok();
	}
	
	/**
	 * 上架
	 */
	@SysLog("上架公开课")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("courseolive:resume")
	public R resume(@RequestBody Long[] oliveIds){
		courseOliveService.resume(oliveIds);
		return R.ok();
	}
}
