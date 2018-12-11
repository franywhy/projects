package io.renren.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseClassplanEntity;
import io.renren.entity.CourseGuideEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseGuideService;
import io.renren.utils.PageUtils;
import io.renren.utils.PropertiesUtil;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import net.sf.json.JSONObject;

/**
 * 流程指南
 * 
 * @author xiechaojun
 * @date 2017-05-09 09:58:40
 */
@Controller
@RequestMapping("courseguide")
public class CourseGuideController extends AbstractController { 
	@Autowired
	private CourseGuideService courseGuideService;
	@Resource
	KGS courseGuideKGS;
	@Value("#{application['course.guide.url']}")
	private String course_guide_url = "";
	//文件上传服务
	private static String fileUploadUrl = "";
	//初始化文件服务
	@Value("#{application['file.domain']}")
	private void setFileUploadUrl(String str){
		fileUploadUrl = str + "/file/singleDirectUpload?sourceId=1";
		logger.info("CourseGuideController setFileUploadUrl init fileUploadUrl:{}" , fileUploadUrl);
	}
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseguide:list")
	public R list(Integer page, Integer limit,HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", schoolId);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
	    /*longQuery(map, request, "guideId");*/
		stringQuery(map, request, "guideName");
		longQuery(map, request, "levelId");
		longQuery(map, request, "professionId");
		longQuery(map, request, "areaId");
//		longQuery(map, request, "area_id");
		//查询列表数据
		List<Map<String, Object>> courseGuideEntity = courseGuideService.queryListMap(map);
		int total = courseGuideService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseGuideEntity, total, limit, page);	
		return R.ok().put(pageUtil);
		/*Map<String, Object> map = getMapPage(request);
		//查询列表数据
		List<CourseGuideEntity> courseGuideList = courseGuideService.queryList(map);
		int total = courseGuideService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseGuideList, total , request);	
		return R.ok().put(pageUtil);*/
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{guideId}")
	@RequiresPermissions("courseguide:info")
	public R info(@PathVariable("guideId") Long guideId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
//		longQuery(map, request, "guideId");
		map.put("guideId", guideId);
		Map<String, Object> courseGuide = courseGuideService.queryMap(map);
		return R.ok().put(courseGuide);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@SysLog("保存流程指南")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courseguide:save")
	public R save(@RequestBody CourseGuideEntity courseGuide , HttpServletRequest request) throws Exception{
		//默认状态
	    courseGuide.setStatus(1);
	    //平台ID
	    courseGuide.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseGuide.setCreatePerson(getUserId());
		courseGuide.setModifyPerson(getUserId());
		Long id = (long)courseGuideKGS.nextId();
        //	 自动生成ID
		courseGuide.setGuideId(id);
        //URL处理
		String url=courseGuide.getGuideUrl();
		if(StringUtils.isBlank(url)){
			String uil=PropertiesUtil.loadInterfaceConfigProperties().getMaterialDetailUrl();
			String urll=course_guide_url.replace("{id}", id.toString());
			courseGuide.setGuideUrl(urll);
		}
		courseGuideService.save(courseGuide);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改流程指南")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courseguide:update")
	public R update(@RequestBody CourseGuideEntity courseGuide , HttpServletRequest request){
		 //平台ID
	    courseGuide.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseGuide.setModifyPerson(getUserId());
		 //URL处理
		Long id=courseGuide.getGuideId();
		String url=courseGuide.getGuideUrl();
		if(StringUtils.isBlank(url)){
			String urll=course_guide_url.replace("{id}", id.toString());
			courseGuide.setGuideUrl(urll);
		}
		courseGuideService.update(courseGuide);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除流程指南")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("courseguide:delete")
	public R delete(@RequestBody Long[] guideIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",guideIds);
		courseGuideService.deleteBatch(map);	
		return R.ok();
	}
	/**
	 * 编辑器上传图片
	 *//*
	@ResponseBody
	@RequestMapping("/uploadCourseGuide")
	@RequiresPermissions("courseguide:delete")
	public R uploadCourseGuide(HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("value", "123");
		return R.ok();
	}*/
	/**
	 * 编辑器上传图片
	 * @throws Exception 
	 */
	@RequestMapping(value = "uploadCourseGuide", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) mpReq.getFile("imgFile");
		InputStream fis = file.getInputStream();
		String boundary = "admin-html-pic"; // Could be any string
		String Enter = "\r\n";
		URL url = new URL(fileUploadUrl);
		logger.info("CourseGuideController upload fileUploadUrl:{}" , fileUploadUrl);
		// URL url = new
		// URL("http://177.77.83.132:8081/file/singleDirectUpload?sourceId=1");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		conn.connect();
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		// part 1
		String part1 = "--" + boundary + Enter + "Content-Type: application/octet-stream" + Enter + "Content-Disposition: form-data; filename=\""
				+ file.getOriginalFilename() + "\"; name=\"file\"" + Enter + Enter;
		// part 2
		String part2 = Enter + "--" + boundary + Enter + "Content-Type: text/plain" + Enter + "Content-Disposition: form-data; name=\"dataFormat\""
				+ Enter + Enter + "hk" + Enter + "--" + boundary + "--";
		byte[] xmlBytes = new byte[fis.available()];
		fis.read(xmlBytes);
		dos.writeBytes(part1);
		dos.write(xmlBytes);
		dos.writeBytes(part2);
		dos.flush();
		dos.close();
		fis.close();
		// 获得响应状态
		int resultCode = conn.getResponseCode();
		logger.info("CourseGuideController upload resultCode:{}" , resultCode);
		StringBuffer sb = new StringBuffer();
		if (HttpURLConnection.HTTP_OK == resultCode) {
			String readLine = new String();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine).append("\n");
			}
			responseReader.close();
		}
		conn.disconnect();
		String allUrl1 = sb.toString().substring(sb.toString().indexOf(":", 28), sb.toString().length() - 1);
		String allUrl = allUrl1.substring(2, allUrl1.length() - 2);
		JSONObject jsStr1 = new JSONObject();
		jsStr1.put("error", 0);
		jsStr1.put("url", allUrl);
		logger.info("CourseGuideController upload jsStr1:{}" , jsStr1);
		response.getWriter().write(jsStr1.toString());
	}
	/**
	 * 禁用
	 */
	@SysLog("禁用流程指南")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("courseguide:resume")
	public R pause(@RequestBody Long[] numbers) {
		/*timetableService.pause(numbers);*/
		courseGuideService.pause(numbers);
		return R.ok();
	}

	/**
	 * 启用
	 */
	@SysLog("启用流程指南")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("courseguide:pause")
	public R resume(@RequestBody Long[] numbers) {
		/*timetableService.resume(numbers);*/
		courseGuideService.resume(numbers);
		return R.ok();
	}
}
