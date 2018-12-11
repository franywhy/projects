package io.renren.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.renren.common.doc.SysLog;
import io.renren.entity.KnowledgePointEntity;
import io.renren.mongo.dao.ICourseDao;
import io.renren.mongo.entity.ChapterEntity;
import io.renren.mongo.entity.SectionEntity;
import io.renren.service.KnowledgePointService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 知识点管理档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
//@Controller
//@RequestMapping("kpmanagement")
public class KnowledgePointController extends AbstractController {
	@Autowired
	private KnowledgePointService knowledgePointService;
	
	@Resource
	private ICourseDao courseDao;
	
	@RequestMapping("kbm/kpmanagement.html")
	public String list(){
		return "kbm/kpmanagement.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("kpmanagement:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		
		String course = request.getParameter("course");
		String verse = request.getParameter("verse");
		String chapter = request.getParameter("chapter");
		String name = request.getParameter("name");
		String sTime = request.getParameter("sTime");
		
		map.put("course", course);
		map.put("verse", verse);
		map.put("chapter", chapter);
		map.put("name", name);
		map.put("sTime", sTime);
		
		
		//查询列表数据
		List knowledgePointList = knowledgePointService.queryList1(map);
		int total = knowledgePointService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(knowledgePointList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("kpmanagement:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		//longQuery(map, request, "id");
		if (null==id) {
			id = Long.valueOf(request.getParameter("id").toString());
		}
		map.put("id", id);
		KnowledgePointEntity knowledgePoint = knowledgePointService.queryObject(map);
		if (null!=knowledgePoint) {
			Map m= this.courseDao.findCourseInfoById(knowledgePoint.getCourse(), knowledgePoint.getChapter(), knowledgePoint.getVerse());
			knowledgePoint.setVerseName(null==m.get("verseName")?"":m.get("verseName").toString());
			knowledgePoint.setChapterName(null==m.get("chapterName")?"":m.get("chapterName").toString());
			knowledgePoint.setCourseName(null==m.get("courseName")?"":m.get("courseName").toString());
		}
		
		return R.ok().put(knowledgePoint);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存知识点")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("kpmanagement:save")
	public R save(@RequestBody KnowledgePointEntity knowledgePoint
			, HttpServletRequest request){
		
		if (null==knowledgePoint || null==knowledgePoint.getName() 
				||knowledgePoint.getName().trim().toString().equals("")) {
			return R.error("名称不能为空");
		}
	    //创建用户
		knowledgePoint.setCreateBy(getUserId());
	    //创建时间
		knowledgePoint.setCreateTime(new Date());
		//保存
		knowledgePointService.save(knowledgePoint);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改知识点")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("kpmanagement:update")
	public R update(@RequestBody KnowledgePointEntity knowledgePoint , HttpServletRequest request){
		if (null==knowledgePoint || null==knowledgePoint.getName() 
				||knowledgePoint.getName().trim().toString().equals("")) {
			return R.error("名称不能为空");
		}
	    //修改用户
		knowledgePoint.setUpdateBy(getUserId());
		//修改时间
		knowledgePoint.setUpdateTime(new Date());
		//修改
		knowledgePointService.update(knowledgePoint);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除知识点")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("kpmanagement:delete")
	public R delete(@RequestBody Integer[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		knowledgePointService.deleteBatch(map);	
		return R.ok();
	}
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/detailInfo")
	@RequiresPermissions("kpmanagement:detailInfo")
	public R detailInfo(@RequestParam("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		if (null==id) {
			id = Long.valueOf(request.getParameter("id").toString());
		}
		map.put("point_id", id);
		List materialDetails = this.knowledgePointService.queryListForDetail(map);
		
		int total = knowledgePointService.queryTotalForDetail(map);
		PageUtils pageUtil = new PageUtils(materialDetails, total , request);	
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 获取节的下拉信息
	 */
	@ResponseBody
	@RequestMapping("/chapterSelect")
	public R chapterSelect(@RequestBody String result
			,HttpServletRequest request){
		//根据字符串生成JSON对象
				JSONObject jb = JSONObject.parseObject(result);
				Map<String, Object> map = (Map<String, Object>)jb;
				String courseId = (String) map.get("courseId");
				String chapterId = (String) map.get("chapterId");
		List<SectionEntity> list = this.courseDao.findVerseById(courseId, chapterId);
		return R.ok().put(list);
	}
	
	/**
	 * 获取章的下拉信息
	 */
	@ResponseBody
	@RequestMapping(value="/courseSelect")
	public R courseSelect(
			@RequestBody String result,
			HttpServletRequest request){
		
		//根据字符串生成JSON对象
		JSONObject jb = JSONObject.parseObject(result);
		Map<String, Object> map = (Map<String, Object>)jb;
		String courseId = (String) map.get("courseId");
		
		List<ChapterEntity> list = this.courseDao.findChapterByCourseId(courseId);
		return R.ok().put(list);
	}

	
}
