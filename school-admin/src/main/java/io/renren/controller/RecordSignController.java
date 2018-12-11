package io.renren.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import io.renren.entity.RecordSignEntity;
import io.renren.pojo.ExaminationResultPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseExamRecordDetailService;
import io.renren.service.CourseExamRecordService;
import io.renren.service.CourseUserplanDetailService;
import io.renren.service.CourseUserplanService;
import io.renren.service.CoursesService;
import io.renren.service.MallAreaService;
import io.renren.service.MallOrderService;
import io.renren.service.RecordSignService;
import io.renren.service.SysProductService;
import io.renren.service.UsersService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import net.sf.json.JSONObject;


/**
 * 报考登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-26 15:21:19
 */
@Controller
@RequestMapping("record/recordSign")
public class RecordSignController extends AbstractController {
	
	@Autowired
    RecordSignService recordSignService;
	 
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
//	@RequiresPermissions("examinationresult:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "name");
		stringQuery(map, request, "classId");
		map.put("dr", 0);//只取订单的dr=0或者没有订单的学员报读信息
		//查询列表数据
		List<RecordSignEntity> examinationResultList = recordSignService.queryList(map);
		int total = recordSignService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(examinationResultList, total , request);	
		return R.ok().put(pageUtil);
	}
 
}
