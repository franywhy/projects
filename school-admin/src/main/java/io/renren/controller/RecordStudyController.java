package io.renren.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseExamRecordDetailEntity;
import io.renren.entity.CourseExamRecordEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.pojo.RecordStudyPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseExamRecordDetailService;
import io.renren.service.CourseExamRecordService;
import io.renren.service.CourseUserplanDetailService;
import io.renren.service.CourseUserplanService;
import io.renren.service.CoursesService;
import io.renren.service.MallAreaService;
import io.renren.service.MallOrderService;
import io.renren.service.RecordInfoService;
import io.renren.service.RecordStudyService;
import io.renren.service.SysProductService;
import io.renren.service.UsersService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import net.sf.json.JSONObject;

 /**
  * 学员档案-基础信息
  * @author lintf 
  *
  */
@Controller
@RequestMapping("record/study")
public class RecordStudyController extends AbstractController {
 	@Autowired
	private RecordStudyService recordStudyService;
	
	
	
	 
 
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping(value="/list",produces="text/html;charset=UTF-8")
	//@RequiresPermissions("mallorder:list")
	public R list( HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "name");
		stringQuery(map, request, "mobile"); 
		longQuery(map, request, "classTeacherId"); 
		 
		List<RecordStudyPOJO> infoList = recordStudyService.queryList(map);
	 
		
		recordStudyService.ClassCompletionRateProcess(infoList);
		
	 
		recordStudyService.examinationResultProcess(infoList); 
		int total = recordStudyService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(infoList, total , request);
		return R.ok().put(  pageUtil);
	}
	
 
}
