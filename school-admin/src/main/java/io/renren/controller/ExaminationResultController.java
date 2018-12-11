package io.renren.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.renren.entity.ExaminationResultEntity;
import io.renren.entity.SysUserEntity;
import io.renren.pojo.ExaminationResultPOJO;
import io.renren.service.ExaminationResultService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 统考成绩
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-07 09:21:13
 */
@Controller
@RequestMapping("examinationresult")
public class ExaminationResultController extends AbstractController {
	@Autowired
	private ExaminationResultService examinationResultService;
	
	@RequestMapping("/examinationresult.html")
	public String list(){
		return "examinationresult/examinationresult.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
//	@RequiresPermissions("examinationresult:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "id");
		stringQuery(map, request, "userId");
		stringQuery(map, request, "nickName");
		stringQuery(map, request, "mobile");
		stringQuery(map, request, "examType");
		stringQuery(map, request, "sysUserId");
		//查询列表数据
		List<ExaminationResultPOJO> examinationResultList = examinationResultService.queryList(map);
		int total = examinationResultService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(examinationResultList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("examinationresult:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		longQuery(map, request, "id");
		ExaminationResultPOJO examinationResult = examinationResultService.queryObject(map);
		return R.ok().put(examinationResult);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("examinationresult:save")
	public R save(@RequestBody ExaminationResultEntity examinationResult , HttpServletRequest request){
		SysUserEntity userInfo = getUser();
		if(userInfo.getUserId() != null){
			examinationResult.setCreater(userInfo.getUserId());
		}
		Map<String,Object> map = new HashMap<>();
		map.put("registrationId", examinationResult.getRegistrationId());
		int num = examinationResultService.getRegistrationNum(map);
		if(num == 1){
			examinationResult.setExamType(0);
		}else if(num > 1){
			examinationResult.setExamType(1);
		}
		 
		//保存
		examinationResultService.save(examinationResult);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("examinationresult:update")
	public R update(@RequestBody ExaminationResultEntity examinationResult , HttpServletRequest request){
//		 //平台ID
//	    examinationResult.setSchoolId(SchoolIdUtils.getSchoolId(request));  
		//修改
		examinationResultService.update(examinationResult);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("examinationresult:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		examinationResultService.deleteBatch(map);	
		return R.ok();
	}
	
	/**
	 * 导入
	 */
	@ResponseBody
	@RequestMapping(value = "/importData" , method = RequestMethod.POST)
	@RequiresPermissions("examinationresult:import")
	@Transactional
	public R importData(HttpServletRequest request){
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		int result = 0;
		try {
			result = examinationResultService.importData(file,getUser());
		} catch (Exception e) { 
			e.printStackTrace();
			return R.ok(e.getMessage());
		}
		return R.ok("成功导入 " + result + " 条数据"); 
	} 
}
