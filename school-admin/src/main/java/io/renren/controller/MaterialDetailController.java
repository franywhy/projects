package io.renren.controller;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.MaterialDetailEntity;
import io.renren.mongo.entity.ChapterEntity;
import io.renren.mongo.entity.SectionEntity;
import io.renren.service.MaterialDetailService;
import io.renren.service.QuestionBankService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 
 * 
 * @author Yuanjp
 * @date 2017-05-12 11:16:41
 */
@Controller
@RequestMapping("/materialdetail")
public class MaterialDetailController extends AbstractController {
	@Autowired
	private MaterialDetailService materialDetailService;
	
	@Autowired
	private QuestionBankService questionBankService;
	
	@RequestMapping("/materialdetail.html")
	public String list(){
		return "materialdetail/materialdetail.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("materialdetail:list")
	public R list(MaterialDetailEntity materialDetailEntity,Integer page, Integer limit){
		
		logger.info("materialDetailEntity:"+materialDetailEntity.toString());
		
		logger.info("page:"+page+" limit:"+limit);
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);	
		map.put("materialDetailEntity", materialDetailEntity);
		
		if(materialDetailEntity.getIsRelevance()==null||"".equals(materialDetailEntity.getIsRelevance().trim())){ // 没有选择关联类型 查询
			//查询列表数据
			List<MaterialDetailEntity> materialDetailEntities=materialDetailService.queryDetailEntities( materialDetailService.queryList(map));
			int total = materialDetailService.queryTotal(map);
			PageUtils pageUtil = new PageUtils(materialDetailEntities, total, limit, page);	
			return R.ok().put("page", pageUtil);
		}else{
			
			if("是".equals(materialDetailEntity.getIsRelevance())){ //按关联 查询
				
				List<MaterialDetailEntity> materialDetailYesList=materialDetailService.queryMaterialDetailYesList(map);
				int total = materialDetailService.queryYesTotal(map);
				PageUtils pageUtil = new PageUtils(materialDetailYesList, total, limit, page);	
				return R.ok().put("page", pageUtil);
			}else{ //按非关联查询
				List<MaterialDetailEntity> materialDetailNoList=materialDetailService.queryMaterialDetailNoList(map);
				int total = materialDetailService.queryNoTotal(map);
				PageUtils pageUtil = new PageUtils(materialDetailNoList, total, limit, page);	
				return R.ok().put("page", pageUtil);
			}
			
		}
		
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("materialdetail:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
//		longQuery(map, request, "id");
		map.put("id", id);
		MaterialDetailEntity materialDetail = materialDetailService.queryObject(map);
		
		return R.ok().put("materialDetail",materialDetail);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存资料管理")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("materialdetail:save")
	public R save(@RequestBody MaterialDetailEntity materialDetail , HttpServletRequest request){
		
		logger.info("materialDetailEntity:"+materialDetail.toString());
		
	/*	if("".equals(materialDetail.getName())||null==materialDetail.getName()){
			return R.error(200,"资料名称不能为空");
		}*/
		
		//默认状态
	    materialDetail.setStatus(1);
	    //平台ID
//	    materialDetail.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		materialDetail.setCreateBy(String.valueOf(getUserId()));
	    //创建时间
		materialDetail.setCreateTime(new Date());
		//保存
		logger.info("materialDetail="+materialDetail.toString());
		materialDetailService.save(materialDetail);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改资料管理")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("materialdetail:update")
	public R update(@RequestBody MaterialDetailEntity materialDetail , HttpServletRequest request){
		
		logger.info("materialDetailEntity:"+materialDetail.toString());
		
		 //平台ID
//	    materialDetail.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		materialDetail.setUpdateBy(String.valueOf(getUserId()));
		//修改时间
		materialDetail.setUpdateTime(new Date());
		System.out.println("materialDetail="+materialDetail.toString());
		//修改
		materialDetailService.update(materialDetail);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除资料管理")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("materialdetail:delete")
	public R delete(@RequestBody Integer[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		//判断删除的ID中是否含有已关联的ID号 ,有则不能删除
		if(materialDetailService.judgeIds(map)>0){
			
			return R.error("已选资料中有被知识点关联，不能删除！");
		}
		
		materialDetailService.deleteBatch(map);	
		return R.ok();
	}
	
	
	/**
	 * 获取题库的课程信息
	 */
	@ResponseBody
	@RequestMapping("/qbCourseInit")
	public R qbCourseInit(HttpServletRequest request){
		List list = this.questionBankService.queryCourse();
		return R.ok().put(list);
	}
	
	/**
	 * 获取章的下拉信息
	 */
	@ResponseBody
	@RequestMapping(value="/qbCourseSelect")
	public R qbCourseSelect(
			@RequestBody String result,
			HttpServletRequest request){
		
		//根据字符串生成JSON对象
		JSONObject jb = JSONObject.parseObject(result);
		Map<String, Object> map = (Map<String, Object>)jb;
		String courseId = (String) map.get("courseId");
		Map m = new HashMap();
		m.put("course_code", courseId);
		List list = this.questionBankService.queryChapter(m);
		return R.ok().put(list);
	}
	
	/**
	 * 获取节的下拉信息
	 */
	@ResponseBody
	@RequestMapping("/qbChapterSelect")
	public R qbChapterSelect(@RequestBody String result
			,HttpServletRequest request){
		//根据字符串生成JSON对象
				JSONObject jb = JSONObject.parseObject(result);
				Map<String, Object> map = (Map<String, Object>)jb;
				String courseId = (String) map.get("courseId");
				String chapterId = (String) map.get("chapterId");
				
				Map m = new HashMap();
				m.put("course_code", courseId);
				m.put("chapter_code", chapterId);
				
		List<SectionEntity> list = this.questionBankService.queryVerse(m);
		return R.ok().put(list);
	}
	
	
}
