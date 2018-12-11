package io.renren.controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.FreeAssessmentEntity;
import io.renren.service.FreeAssessmentService;
import io.renren.service.UsersService;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isNumeric;


/**
 * 免考核档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-26 10:31:23
 */
@Controller
@RequestMapping("freeassessment")
public class FreeAssessmentController extends AbstractController {

	@Autowired
	private FreeAssessmentService freeAssessmentService;

	@Autowired
	private UsersService usersService;
	
	@RequestMapping("/freeassessment.html")
	public String list(){
		return "freeassessment/freeassessment.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("freeassessment:list")
	public R list(HttpServletRequest request) {
		Map<String, Object> map = getMapPage(request);
		stringQueryDecodeUTF8(map, request, "teacherName");
		stringQueryDecodeUTF8(map, request, "course");
		stringQueryDecodeUTF8(map, request, "studentMobile");
		intQuery(map, request, "applicationStatus");
		//查询列表数据
		List<FreeAssessmentEntity> freeAssessmentList = freeAssessmentService.queryList(map);
		int total = freeAssessmentService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(freeAssessmentList, total, request);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("freeassessment:info")
	public R info(@PathVariable("id") Long id){
		FreeAssessmentEntity freeAssessment = freeAssessmentService.queryObject(id);
		
		return R.ok().put("freeAssessment", freeAssessment);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("freeassessment:save")
	public R save(@RequestBody FreeAssessmentEntity freeAssessment){
		freeAssessmentService.save(freeAssessment);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("freeassessment:update")
	public R update(@RequestBody FreeAssessmentEntity freeAssessment){
		freeAssessmentService.update(freeAssessment);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("freeassessment:delete")
	public R delete(@RequestBody Long[] ids){
		freeAssessmentService.deleteBatch(ids);
		
		return R.ok();
	}

	/**
	 * 通过
	 */
	@ResponseBody
	@RequestMapping("/pass")
	public R pass(@RequestBody Long[] ids){
		//申请状态:0.审核中,1.通过,2.取消
		Map<String, Object> map = new HashMap<>();
		map.put("applicationStatus",1);
		map.put("list",ids);
		freeAssessmentService.updateStatusBatch(map);
		return R.ok();
	}

	/**
	 * 取消
	 */
	@ResponseBody
	@RequestMapping("/cancel")
	public R cancel(@RequestBody Long[] ids){
		//申请状态:0.审核中,1.通过,2.取消
		Map<String, Object> map = new HashMap<>();
		map.put("applicationStatus",2);
		map.put("list",ids);
		freeAssessmentService.updateStatusBatch(map);
		return R.ok();
	}

	/**
	 * 免考核导入模板
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/downTemplate")
	public void downTemplate(HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		String arrStr = "0,0,0,0,学员名称&0,1,0,0,手机号&0,2,0,0,班级名称&0,3,0,0,班主任&0,4,0,0,免考核课程&0,5,0,0,申请时间(例:1991/02/30)&0,6,0,0,截止时间(例:1991/02/30)&0,7,0,0,申请原因";
		String title = "免考核";
		String cellsStr = new String(arrStr.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = title+"档案模板导出-" + Calendar.getInstance().getTimeInMillis() + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		res.setContentType("text/html; charset=UTF-8");
		res.addHeader("Content-Disposition",
				(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
		OutputStream toClient = new BufferedOutputStream(res.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(filename, title+"档案", cells, toClient);
		toClient.flush();
		toClient.close();
	}

	/**
	 * EXCEL批量导入
	 *
	 * @throws Exception
	 */
	@SysLog("EXCEL批量导入")
	@ResponseBody
	@RequestMapping("/batchImport")
	public R batchImport(HttpServletRequest request) throws Exception {

		String studentName;
		String studentMobile;
		String className;
		String teacherName;
		String course;
		String applicationDate;
		String deadlineDate;
		String reason;

		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		List<FreeAssessmentEntity> detailList = new ArrayList<>();
		FreeAssessmentEntity freeAssessmentEntity = null;
		FileInputStream fio = (FileInputStream) file.getInputStream();
		List<String[]> dataList = ExcelReaderJXL.readExcelSDF(fio,"yyyy-MM-dd HH:mm:ss");

		String exceptMsg = "";
		String[] header = dataList.get(0);
		ArrayList<String> exceptList = new ArrayList<>();

		//String arrStr = "0,0,0,0,学员名称&0,1,0,0,手机号&0,2,0,0,班级名称&0,3,0,0,班主任&0,4,0,0,免考核课程&0,5,0,0,申请时间(例:1991/02/30)&0,6,0,0,截止时间(例:1991/02/30)&0,7,0,0,申请原因";
		if (header.length < 8) {
			exceptMsg = "总列数不正确，请核对一下列数；";
			exceptList.add(exceptMsg);
		} else if (!"学员名称".equals(header[0].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【学员名称】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"手机号".equals(header[1].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【手机号】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"班级名称".equals(header[2].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【班级名称】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"班主任".equals(header[3].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【班主任】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"免考核课程".equals(header[4].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【免考核课程】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"申请时间(例:1991/02/30)".equals(header[5].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【申请时间(例:1991/02/30)】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"截止时间(例:1991/02/30)".equals(header[6].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【截止时间(例:1991/02/30)】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"申请原因".equals(header[7].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【申请原因】匹配;";
			exceptList.add(exceptMsg);
		}
		else if (header.length >= 8 && "学员名称".equals(header[0].trim()) && "手机号".equals(header[1].trim())
				&& "班级名称".equals(header[2].trim()) && "班主任".equals(header[3].trim())
				&& "免考核课程".equals(header[4].trim()) && "申请时间(例:1991/02/30)".equals(header[5].trim())
				&& "截止时间(例:1991/02/30)".equals(header[6].trim()) && "申请原因".equals(header[7].trim())) {
			dataList.remove(0);
			for (int i = 0; i < dataList.size(); i++) {

				String[] dataArr = dataList.get(i);
				if (dataArr.length==8) {

					Map<String, Object> map = getMapPage(request);
					boolean flag = false;
					exceptMsg = "第" + new Integer(i + 2).toString() + "行数据不能导入原因是：";

					studentName = dataArr[0];
					studentMobile = dataArr[1];
					className = dataArr[2];
					teacherName = dataArr[3];
					course = dataArr[4];
					applicationDate = dataArr[5];
					deadlineDate = dataArr[6];
					reason = dataArr[7];

					map.put("mobile", studentMobile);

					if (studentMobile == null || "".equals(studentMobile)) {
						exceptMsg += "手机号不能为空";
						flag = true;
					} else {
						if (!isMobile(studentMobile)) {
							if (flag == true) {
								exceptMsg += ",手机号的格式不正确（必须为11位的数字）";
							} else {
								exceptMsg += "手机号的格式不正确（必须为11位的数字）";
								flag = true;
							}
						} else {
							boolean isExists = this.usersService.checkMobile(map);
							if (!isExists) {
								if (flag == true) {
									exceptMsg += ",手机号不存在";
								} else {
									exceptMsg += "手机号不存在";
									flag = true;
								}
							} else{
								Map<String,Object> queryMap = new HashMap();
								queryMap.put("mobilePhoneNo",studentMobile);
								studentName = this.usersService.getNickNameByMobilePhoneNo(queryMap);
							}
						}
					}

					if (className == null || "".equals(className)) {
						if(flag==true){
							exceptMsg += ",班级名称不能为空";
						}else {
							exceptMsg += "班级名称不能为空";
							flag = true;
						}
					} else {
						if (StringUtils.length(className) > 60) {
							if (flag == true) {
								exceptMsg += ",班级名称长度不能大于60个字符";
							} else {
								exceptMsg += "班级名称长度不能大于60个字符";
								flag = true;
							}
						}
					}

					if (teacherName == null || "".equals(teacherName)) {
						if(flag==true){
							exceptMsg += ",班主任名称不能为空";
						}else {
							exceptMsg += "班主任名称不能为空";
							flag = true;
						}
					} else {
						if (StringUtils.length(teacherName) > 50) {
							if (flag == true) {
								exceptMsg += ",班主任名称长度不能大于50个字符";
							} else {
								exceptMsg += "班主任名称长度不能大于50个字符";
								flag = true;
							}
						}
					}

					if (course == null || "".equals(course)) {
						if(flag==true){
							exceptMsg += ",免考核课程不能为空";
						}else {
							exceptMsg += "免考核课程不能为空";
							flag = true;
						}
					} else {
						if (StringUtils.length(course) > 200) {
							if (flag == true) {
								exceptMsg += ",免考核课程长度不能大于200个字符";
							} else {
								exceptMsg += "免考核课程长度不能大于200个字符";
								flag = true;
							}
						}
					}

					Date applicationTime = null;
					if (applicationDate == null || "".equals(applicationDate)) {
						if(flag==true){
							exceptMsg += ",申请时间不能为空";
						}else {
							exceptMsg += "申请时间不能为空";
							flag = true;
						}
					} else {
						applicationTime = DateUtils.parse(applicationDate, "yyyy-MM-dd HH:mm:ss");
						if (null == applicationTime) {
							if (flag == true) {
								exceptMsg += ",申请时间格式不正确";
							} else {
								exceptMsg += "申请时间格式不正确";
								flag = true;
							}
						}
					}

					Date deadlineTime = null;
					if (deadlineDate == null || deadlineDate.equals("")) {
						if(flag==true){
							exceptMsg += ",截止时间不能为空";
						}else {
							exceptMsg += "截止时间不能为空";
							flag = true;
						}
					} else {
						deadlineTime = DateUtils.parse(deadlineDate, "yyyy-MM-dd HH:mm:ss");
						if (null == deadlineTime) {
							if (flag == true) {
								exceptMsg += ",截止时间格式不正确";
							} else {
								exceptMsg += "截止时间格式不正确";
								flag = true;
							}
						}
					}

					if (reason == null || "".equals(reason)) {
						if(flag==true){
							exceptMsg += ",申请原因不能为空";
						}else {
							exceptMsg += "申请原因不能为空";
							flag = true;
						}
					} else {
						if (StringUtils.length(reason) > 200) {
							if (flag == true) {
								exceptMsg += ",报读课程的长度要小于200字符";
							} else {
								exceptMsg += "报读课程的长度要小于200字符";
								flag = true;
							}
						}
					}

					if (flag) {
						exceptList.add(exceptMsg);
						continue;
					}

					freeAssessmentEntity = new FreeAssessmentEntity();
					freeAssessmentEntity.setStudentName(studentName);
					freeAssessmentEntity.setStudentMobile(studentMobile);
					freeAssessmentEntity.setClassName(className);
					freeAssessmentEntity.setTeacherName(teacherName);
					freeAssessmentEntity.setCourse(course);
					freeAssessmentEntity.setApplicationTime(applicationTime);
					freeAssessmentEntity.setDeadlineTime(deadlineTime);
					freeAssessmentEntity.setReason(reason);
					//申请状态:0.审核中,1.通过,2.取消
					freeAssessmentEntity.setApplicationStatus(0);
					// 创建用户
					freeAssessmentEntity.setCreator(getUserId());
					// 创建时间
					freeAssessmentEntity.setCreationTime(new Date());
					// 修改用户
					freeAssessmentEntity.setModifier(freeAssessmentEntity.getCreator());
					// 修改时间
					freeAssessmentEntity.setModifiedTime(freeAssessmentEntity.getCreationTime());
					freeAssessmentEntity.setDr(0);

					detailList.add(freeAssessmentEntity);
				}
			}
		}

		String errMsgErr = "";
		for (int i = 0; i < exceptList.size(); i++) {
			errMsgErr += exceptList.get(i) + "<br>";
		}

		if (errMsgErr.equals("")) {
			// 保存
			if (!detailList.isEmpty()) {
				this.freeAssessmentService.saveBatch(detailList);
			}
			return R.ok("");
		} else {
			return R.error(1,errMsgErr);
		}
	}

	private static boolean isMobile(String str) {
		if (null != str && isNumeric(str.trim()) && str.trim().length() == 11) {
			return true;
		}
		return false;
	}
}
