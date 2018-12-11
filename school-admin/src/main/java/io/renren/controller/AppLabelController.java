package io.renren.controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.AppLabel;
import io.renren.service.AppLabelService;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * app用户----标签
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-07-05 11:45:59
 */
@Controller
@RequestMapping("applabel")
public class AppLabelController extends AbstractController {

	public static final String SMALLPICURL = "http://answerimg.kjcity.com/7/7/1534814873991.jpg";
	public static final String BIGPICURL = "http://answerimg.kjcity.com/9/1/1534814858377.jpg";

	@Autowired
	private AppLabelService appLabelService;
	
	@RequestMapping("/applabel.html")
	public String list(){
		return "applabel/applabel.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("applabel:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "labelName");
		intQuery(map, request, "productId");
		intQuery(map, request, "dr");
		//查询列表数据
		List<AppLabel> appLabelList = appLabelService.queryList(map);
		int total = appLabelService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(appLabelList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("applabel:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id",id);
		AppLabel appLabel = appLabelService.queryObject(map);
		return R.ok().put(appLabel);
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改标签")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("applabel:update")
	public R update(@RequestBody AppLabel appLabel , HttpServletRequest request){
		//修改
		if(StringUtils.isBlank(appLabel.getLabelName())){
			return R.error("名称不能为空!!!");
		}
		Map<String,Object> map = new HashMap<>();
		if(appLabel.getParentId() == 0){//修改专业
			map.put("labelName", appLabel.getLabelName());
			map.put("parentId", appLabel.getParentId());
			map.put("productId", appLabel.getProductId());
			int count = appLabelService.queryCountByLabelNameAndParentId(map);
			if(count > 0){
				return R.error("["+appLabel.getLabelName()+"]"+"该产品线的专业已存在!!!");
			}
		}else if(StringUtils.isBlank(appLabel.getCourseCode())){
			return R.error("[课程代码]不能为空!!!");
		}
		appLabelService.update(appLabel);
		return R.ok();
	}
	/**
	 * 新增
	 */
	@SysLog("新增标签")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("applabel:save")
	public R save(@RequestBody AppLabel appLabel , HttpServletRequest request){
		//新增
		if(StringUtils.isBlank(appLabel.getLabelName())){
			return R.error("[名称]不能为空!!!");
		}
		Map<String,Object> map = new HashMap<>();
		if(0 == appLabel.getType()){//新增专业
			map.put("labelName", appLabel.getLabelName());
			map.put("parentId", appLabel.getParentId());
			map.put("productId", appLabel.getProductId());
			int count = appLabelService.queryCountByLabelNameAndParentId(map);
			if(count > 0){
				return R.error("["+appLabel.getLabelName()+"]"+"该产品线的专业已存在!!!");
			}
		}else{//新增标签
			if(null == appLabel.getParentId()){
				return R.error("[父级]不能为空!!!");
			}
			
			Map<String,Object> mapParent = new HashMap<>();
			mapParent.put("id", appLabel.getParentId());
			AppLabel parent = appLabelService.queryObject(mapParent);
			appLabel.setProductId(parent.getProductId());
			
			if(appLabel.getProductId() == 0){
				if(StringUtils.isBlank(appLabel.getSmallPicUrl())){
					appLabel.setSmallPicUrl(SMALLPICURL);
				}
				if(StringUtils.isBlank(appLabel.getBigPicUrl())){
					appLabel.setBigPicUrl(BIGPICURL);
				}
			}
			if(StringUtils.isBlank(appLabel.getCourseCode())){
				return R.error("[课程代码]不能为空!!!");
			}
			map.put("labelName", appLabel.getLabelName());
			map.put("parentId", appLabel.getParentId());
			map.put("productId", appLabel.getProductId());
			int count = appLabelService.queryCountByLabelNameAndParentId(map);
			if(count > 0){
				return R.error("["+appLabel.getLabelName()+"]"+"该产品线的标签已存在!!!");
			}
		}
		appLabelService.save(appLabel);
		return R.ok();
	}
	/**
	 * 启用
	 */
	@SysLog("启用标签")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("applabel:resume")
	public R resume(@RequestBody Long id){
		appLabelService.resume(id);
		return R.ok();
	}
	/**
	 * 禁用
	 */
	@SysLog("禁用标签")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("applabel:pause")
	public R pause(@RequestBody Long id){
		appLabelService.pause(id);
		return R.ok();
	}
	
	/**
	 * 下载会计标签导入模板
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/downKJExcelTemplate")
	@RequiresPermissions("applabel:downKJExcelTemplate")
	public void downLabelKJExcelTemplate(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String downExcel = this.appLabelService.downKJExcel();
		String cellsStr = new String(downExcel.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String fileName = "批量导入会计标签模板-" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
		fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(fileName + ";").toString());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(fileName, "批量导入会计标签模板", cells, toClient);
		toClient.flush();
		toClient.close();
	}
	/**
	 * 下载自考标签导入模板
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/downZKExcelTemplate")
	@RequiresPermissions("applabel:downZKExcelTemplate")
	public void downLabelZKExcelTemplate(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String downExcel = this.appLabelService.downZKExcel();
		String cellsStr = new String(downExcel.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String fileName = "批量导入自考标签模板-" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
		fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(fileName + ";").toString());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(fileName, "批量导入自考标签模板", cells, toClient);
		toClient.flush();
		toClient.close();
	}
	/**
	 * 批量导入会计标签
	 * @param request
	 * @param response
	 * @return
	 */
	@SysLog("批量导入会计标签")
	@ResponseBody
	@RequestMapping("/importKJExcel")
	@RequiresPermissions("applabel:importKJExcel")
	public R importKJExcel(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		String importExcel = this.appLabelService.importKJExcel(file);
		return R.ok().put(importExcel);
	}
	/**
	 * 批量导入自考标签
	 * @param request
	 * @param response
	 * @return
	 */
	@SysLog("批量导入自考标签")
	@ResponseBody
	@RequestMapping("/importZKExcel")
	@RequiresPermissions("applabel:importZKExcel")
	public R importZKExcel(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		String importExcel = this.appLabelService.importZKExcel(file);
		return R.ok().put(importExcel);
	}
}
