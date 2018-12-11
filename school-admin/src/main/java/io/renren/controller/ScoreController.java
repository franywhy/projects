package io.renren.controller;

import static org.apache.commons.lang.StringUtils.isNumeric;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.RegistrationEntity;
import io.renren.entity.ScoreEntity;
import io.renren.service.ScoreService;
import io.renren.service.UsersService;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 成绩档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-15 14:34:23
 */
@Controller
@RequestMapping("score")
public class ScoreController extends AbstractController {
	
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping("/score.html")
	public String list(){
		return "score/score.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("score:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//查询列表数据
		stringQueryDecodeUTF8(map, request, "teacherName");
		stringQueryDecodeUTF8(map, request, "course");
		stringQueryDecodeUTF8(map, request, "studentMobile");
		stringQueryDecodeUTF8(map, request, "className");
		intQuery(map, request, "status");
		List<ScoreEntity> scoreList = scoreService.queryList(map);
		int total = scoreService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(scoreList, total , request);	
		return R.ok().put("page",pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("score:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		//Map<String, Object> map = getMap(request);
		//longQuery(map, request, "id");		
		ScoreEntity score = scoreService.queryObject(id);
		return R.ok().put("score",score);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("score:save")
	public R save(@RequestBody ScoreEntity score , HttpServletRequest request){
		
		if(StringUtils.isBlank(score.getStudentName())){
			return R.error("学员姓名必填！！！");
		}
		if(StringUtils.isBlank(score.getStudentMobile())){
			return R.error("手机号必填！！！");
		}
		if(StringUtils.isBlank(score.getClassName())){
			return R.error("班级名称必填！！！");
		}
		if(StringUtils.isBlank(score.getTeacherName())){
			return R.error("班主任必填！！！");
		}
		if(StringUtils.isBlank(score.getCourse())){
			return R.error("课程必填！！！");
		}
		if (null == score.getExaminationTime()
				|| StringUtils.isBlank(score.getExaminationTime().toString())) {
			return R.error("考试时间必填！！！");
		}
		if (null == score.getRegistrationTime()
				|| StringUtils.isBlank(score.getRegistrationTime().toString())) {
			return R.error("录入时间必填！！！");			
		}
		if(StringUtils.isBlank(score.getScore().toString())){
			return R.error("成绩必填！！！");
		}
		
		if (!isMobile(score.getStudentMobile())) {
			return R.error("手机号的格式不正确（必须为11位的数字）！！！");

		}
		if(!StringUtils.isBlank(score.getRemark()) && score.getRemark().length() > 100){
			return R.error("备注不能超过100个字！！！");
		}
		
		int checkUser = this.usersService.checkUser(score.getStudentMobile());
		if(checkUser <= 0){
			return R.error("用户不存在！！！");
		}
		
		/*boolean isExists = usersService.mobileExist(map);
		if (!isExists) {
			return R.error("用户不存在！！！");
		}*/
				
		//默认状态
	    score.setStatus(0);
	    //创建用户
		score.setCreator(getUserId());
	    //创建时间
		score.setCreationTime(new Date());
		//保存
		scoreService.save(score);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("score:update")
	public R update(@RequestBody ScoreEntity score , HttpServletRequest request){
		
	    //修改用户
		score.setModifier(getUserId());
		//修改时间
		score.setModifiedTime(new Date());
		//修改
		scoreService.update(score);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("score:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		scoreService.deleteBatch(map);	
		return R.ok();
	}
	
	/**
	 * 下载模版
	 * @param request
	 * @param res
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/downTemplate")
	public void downTemplate(HttpServletRequest request, HttpServletResponse res)
			throws IOException {
		
			String arrStr = "0,0,0,0,学员名称&0,1,0,0,手机号&0,2,0,0,班级名称&0,3,0,0,班主任&0,4,0,0,课程&0,5,0,0,考试时间(例:1991/02/30)&0,6,0,0,录入时间(例:1991/02/30)&0,7,0,0,成绩";
			String title = "成绩";
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

		String studentName;  //学员姓名
		String studentMobile;//学员手机号
		String className;    //班级名称
		String teacherName;  //班主任
		String course;       //课程	
		String examinationDate; //考试时间
		String registrationDate;//录入时间
		Integer score = null;   //成绩
				
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		//获取上传文件
		MultipartFile file = mpReq.getFile("file_data");
		List<ScoreEntity> detailList = new ArrayList<ScoreEntity>();
		ScoreEntity scoreEntity = null;
		//把文件以流的形式输入
		FileInputStream fio = (FileInputStream) file.getInputStream();
		List<String[]> dataList = ExcelReaderJXL.readExcelSDF(fio,"yyyy-MM-dd HH:mm:ss");

		String exceptMsg = "";
		//获取文件表头栏
		String[] header = dataList.get(0);
		ArrayList<String> exceptList = new ArrayList<>();

		if (header.length < 8) {
			exceptMsg = "总列数不正确，请核对一下列数;";
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
		} else if (!"课程".equals(header[4].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【课程】匹配;";
			exceptList.add(exceptMsg);
		
		} else if (!"考试时间(例:1991/02/30)".equals(header[5].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【考试时间(例:1991/02/30)】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"录入时间(例:1991/02/30)".equals(header[6].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【录入时间(例:1991/02/30)】匹配;";
			exceptList.add(exceptMsg);
		} else if (!"成绩".equals(header[7].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【成绩】匹配;";
			exceptList.add(exceptMsg);
		}	
		/*}else if (!"状态".equals(header[8].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【状态】匹配;";
			exceptList.add(exceptMsg);*/
		/*else if (!"备注".equals(header[9].trim())) {
			exceptMsg = "对应的列名不对，请核对一下是否与【备注】匹配;";
			exceptList.add(exceptMsg);
		}*/
		else if (header.length >= 8 && "学员名称".equals(header[0].trim()) && "手机号".equals(header[1].trim())
				&& "班级名称".equals(header[2].trim()) && "班主任".equals(header[3].trim())
				&& "课程".equals(header[4].trim()) && "考试时间(例:1991/02/30)".equals(header[5].trim())
				&& "录入时间(例:1991/02/30)".equals(header[6].trim()) && "成绩".equals(header[7].trim())){				 
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
					examinationDate = dataArr[5];
					registrationDate = dataArr[6];
					String sco = String.valueOf(score);										
					sco = dataArr[7];															
					
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
								Map<String,Object> queryMap = new HashMap<String,Object>();
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
							exceptMsg += ",课程不能为空";
						}else {
							exceptMsg += "课程不能为空";
							flag = true;
						}
					} else {
						if (StringUtils.length(course) > 200) {
							if (flag == true) {
								exceptMsg += ",课程长度不能大于200个字符";
							} else {
								exceptMsg += "课程长度不能大于200个字符";
								flag = true;
							}
						}
					}

					Date examinationTime = null;
					if (examinationDate == null || "".equals(examinationDate)) {
						if(flag==true){
							exceptMsg += ",考试时间不能为空";
						}else {
							exceptMsg += "考试时间不能为空";
							flag = true;
						}
					} else {
						examinationTime = DateUtils.parse(examinationDate, "yyyy-MM-dd HH:mm:ss");
						if (null == examinationTime) {
							if (flag == true) {
								exceptMsg += ",考试时间格式不正确";
							} else {
								exceptMsg += "考试时间格式不正确";
								flag = true;
							}
						}
					}

					Date registrationTime = null;
					if (registrationDate == null || registrationDate.equals("")) {
						if(flag==true){
							exceptMsg += ",录入时间不能为空";
						}else {
							exceptMsg += "录入时间不能为空";
							flag = true;
						}
					} else {
						registrationTime = DateUtils.parse(registrationDate, "yyyy-MM-dd HH:mm:ss");
						if (null == registrationTime) {
							if (flag == true) {
								exceptMsg += ",录入时间格式不正确";
							} else {
								exceptMsg += "录入时间格式不正确";
								flag = true;
							}
						}
					}

					if (sco == null || "".equals(sco)) {
						if(flag==true){
							exceptMsg += ",成绩不能为空";
						}else {
							exceptMsg += "成绩不能为空";
							flag = true;
						}
					} 
					/*if (status == null || "".equals(status)) {
						if(flag==true){
							exceptMsg += ",状态不能为空";
						}else {
							exceptMsg += "状态不能为空";
							flag = true;
						}
					} */


					if (flag) {
						exceptList.add(exceptMsg);
						continue;
					}

					scoreEntity = new ScoreEntity();
					scoreEntity.setStudentName(studentName);
					scoreEntity.setStudentMobile(studentMobile);
					scoreEntity.setClassName(className);
					scoreEntity.setTeacherName(teacherName);
					scoreEntity.setCourse(course);
					scoreEntity.setExaminationTime(examinationTime);
					scoreEntity.setRegistrationTime(registrationTime);					
					//成绩分数
					int sc = Integer.parseInt(sco);
					scoreEntity.setScore(sc);

																																						
					// 创建用户
					scoreEntity.setCreator(getUserId());										
					// 创建时间
					scoreEntity.setCreationTime(new Date());
					// 修改用户
					scoreEntity.setModifier(scoreEntity.getCreator());
					// 修改时间
					scoreEntity.setModifiedTime(scoreEntity.getCreationTime());
					//默认状态为 通过0
					scoreEntity.setStatus(0);
					// 删除标志
					scoreEntity.setDr(0);
					//添加到集合里
					detailList.add(scoreEntity);
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
				this.scoreService.saveBatch(detailList);
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
