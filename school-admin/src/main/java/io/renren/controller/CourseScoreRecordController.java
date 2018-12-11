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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseExamRecordDetailEntity;
import io.renren.entity.CourseExamRecordEntity;
import io.renren.entity.CourseScoreRecordDetailEntity;
import io.renren.entity.CourseScoreRecordEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseExamRecordDetailService;
import io.renren.service.CourseExamRecordService;
import io.renren.service.CourseScoreRecordDetailService;
import io.renren.service.CourseScoreRecordService;
import io.renren.service.CoursesService;
import io.renren.service.MallAreaService;
import io.renren.service.MallOrderService;
import io.renren.service.UsersService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 分数登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-04 09:27:06
 */
@Controller
@RequestMapping("course/coursescorerecord")
public class CourseScoreRecordController extends AbstractController {
	@Autowired
	private CourseScoreRecordService courseScoreRecordService;
	@Autowired
	private CourseScoreRecordDetailService courseScoreRecordDetailService;
	@Autowired
	private CourseExamRecordDetailService courseExamRecordDetailService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private MallAreaService mallAreaService;
	@Autowired
	private CoursesService coursesService;
	@Autowired
	private MallOrderService mallOrderService;
	@Autowired
	private CourseExamRecordService courseExamRecordService;
	@Resource
	KGS scoreRecordKGS;
	
	@RequestMapping("/coursescorerecord.html")
	public String list(){
		return "coursescorerecord/coursescorerecord.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("coursescorerecord:list")
	public R list(String scoreRecordNo, Integer page, Integer limit, HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		
		Map<String, Object> map = getMapPage(request);
		//查询列表数据
		map.put("schoolId", schoolId);
		map.put("scoreRecordNo", scoreRecordNo);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		List<Map<String, Object>> courseScoreRecordList = courseScoreRecordService.queryAll(map);
		int total = courseScoreRecordService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseScoreRecordList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{scoreRecordId}")
	@RequiresPermissions("coursescorerecord:info")
	public R info(@PathVariable("scoreRecordId") Long scoreRecordId , HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		//查询主表
		Map<String, Object> map = getMap(request);
		map.put("scoreRecordId", scoreRecordId);
		map.put("schoolId", schoolId);
		Map<String, Object> courseScoreRecord = courseScoreRecordService.queryOne(map);
		//查询子表
		Map<String, Object> mapDetail = getMap(request);
		mapDetail.put("scoreRecordId", scoreRecordId);
		mapDetail.put("schoolId", schoolId);
		List<Map<String, Object>> courseScoreRecordDetail = courseScoreRecordDetailService.queryAllByPK(mapDetail);
		
		return R.ok().put("scoreRecord",courseScoreRecord).put("scoreRecordDetail", courseScoreRecordDetail);
	}
	
	/**
	 * 获取系统生成的单号
	 */
	@ResponseBody
	@RequestMapping("/getNo")
	@RequiresPermissions("coursescorerecord:save")
	public R getNo(HttpServletRequest request){
		String no = "FS" + (long)scoreRecordKGS.nextId();
		CourseScoreRecordEntity courseScoreRecord = new CourseScoreRecordEntity();
		courseScoreRecord.setScoreRecordNo(no);
		return R.ok().put(courseScoreRecord);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存分数登记")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("coursescorerecord:save")
	public R save(@RequestBody CourseScoreRecordEntity courseScoreRecord , HttpServletRequest request){
	    //平台ID
	    courseScoreRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseScoreRecord.setCreatePerson(getUserId());
	    //创建时间
		courseScoreRecord.setCreateTime(new Date());
		//默认状态
		courseScoreRecord.setDr(0);
		//审核状态
		courseScoreRecord.setAudited(0);
		//保存
		courseScoreRecordService.save(courseScoreRecord);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改分数登记")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("coursescorerecord:update")
	public R update(@RequestBody CourseScoreRecordEntity courseScoreRecord , HttpServletRequest request){
		 //平台ID
	    courseScoreRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseScoreRecord.setModifyPerson(getUserId());
		//修改时间
		courseScoreRecord.setModifyTime(new Date());
		//修改
		courseScoreRecordService.update(courseScoreRecord);
		return R.ok();
	}
	
	/**
	 * 审核
	 */
	@SysLog("审核分数登记")
	@ResponseBody
	@RequestMapping("/accept")
	@RequiresPermissions("coursescorerecord:accept")
	public R accept(@RequestBody CourseScoreRecordEntity courseScoreRecord , HttpServletRequest request){
		 //平台ID
		courseScoreRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseScoreRecord.setModifyPerson(getUserId());
		//修改时间
		courseScoreRecord.setModifyTime(new Date());
		//审核通过
		courseScoreRecord.setAudited(1);
		//修改
		courseScoreRecordService.accept(courseScoreRecord);
		return R.ok();
	}
	
	/**
	 * 反审核
	 */
	@SysLog("反审核分数登记")
	@ResponseBody
	@RequestMapping("/reaccept")
	@RequiresPermissions("coursescorerecord:reaccept")
	public R reaccept(@RequestBody CourseScoreRecordEntity courseScoreRecord , HttpServletRequest request){
		 //平台ID
		courseScoreRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseScoreRecord.setModifyPerson(getUserId());
		//修改时间
		courseScoreRecord.setModifyTime(new Date());
		//审核不通过
		courseScoreRecord.setAudited(0);
		//修改
		courseScoreRecordService.accept(courseScoreRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除分数登记")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("coursescorerecord:delete")
	public R delete(@RequestBody Long[] scoreRecordIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",scoreRecordIds);
		courseScoreRecordService.deleteBatch(map);
		courseScoreRecordDetailService.deleteBatch(map);
		return R.ok();
	}
	
	@ResponseBody
	@RequestMapping("/examrecordlist")
	public R examRecordList(Integer page, Integer limit, HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		
		Map<String, Object> map = getMapPage(request);
		//查询列表数据
		map.put("schoolId", schoolId);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		List<CourseExamRecordEntity> courseExamRecordList = courseScoreRecordService.queryExamRecordList(map);
		int total = courseScoreRecordService.queryExamRecordTotal(map);
		PageUtils pageUtil = new PageUtils(courseExamRecordList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/examRecordDetail/{examRecordId}")
	public R examRecordDetail(@PathVariable("examRecordId") Long examRecordId, HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		
		Map<String, Object> map = getMapPage(request);
		//查询列表数据
		map.put("examRecordId", examRecordId);
		map.put("schoolId", schoolId);
		List<Map<String, Object>> courseExamRecordDetail = courseExamRecordDetailService.queryAll(map);
		return R.ok().put(courseExamRecordDetail);
	}
	/**
	 * 列表
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping("/exportExcelCourseScoreRecordTemplate")
	public void exportExcelCourseScoreRecordTemplate(long examRecordId,HttpServletRequest request,HttpServletResponse res) throws IOException{
		Map<String, Object> map = getMapPage(request);
		map.put("examRecordId", examRecordId);
	    String arrStr = "0,0,0,0,报考登记单号&0,1,0,0,课程编号&0,2,0,0,手机号&0,3,0,0,分数";	
	    List<CourseExamRecordEntity> courseExamRecordList = courseScoreRecordService.queryExamRecordListForExport(map);
	    
	    String data="";
		int count=0;
		boolean flag=true;
	    for (int i = 0; i < courseExamRecordList.size(); i++)
		{
	    	 count++;
	    	 CourseExamRecordEntity co=new CourseExamRecordEntity();
			 co=courseExamRecordList.get(i);
			 String examRecordNo=co.getExamRecordNo();
			 if(flag){
			 data+="&"+count+",0,3,0,"+examRecordNo;
			 flag=false;
			 }else{
			 data+="&"+count+",0,3,0,"+examRecordNo;
			 }
			 String courseNo=co.getCourseNo();
			 data+="&"+count+",1,0,0,"+courseNo;
			 String mobile=co.getMobile();
			 data+="&"+count+",2,0,0,"+mobile;
			 String score="0000";
			 data+="&"+count+",3,0,0,"+score;
		}
		arrStr+=data;
	    String cellsStr = new String(arrStr.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = "分数登记模板导出-" + Calendar.getInstance().getTimeInMillis() + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		res.setContentType("text/html; charset=UTF-8");
		res.addHeader(
			"Content-Disposition", (new StringBuilder()).append(
				"attachment;filename=").append(
				filename+";").toString());
		OutputStream toClient = new BufferedOutputStream(res.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(
			filename, "分数登记", cells, toClient);
		toClient.flush();
		toClient.close();
		/* return R.ok();*/
	}
	/**
	 * 分数登记EXCEL批量导入
	 * @throws Exception 
	 * */
	@SysLog("分数登记EXCEL批量导入")
	@ResponseBody
	@RequestMapping("/getExcelCourseScoreRecordData")
	public R getExcelCourseScoreRecordData(HttpServletRequest request) throws Exception
		{
		    String mobilePhoneNo=null;
		    Long  user_id=null;
		    String examRecordNo=null;
		    Long  examRecordId=null;
		    String courseNo=null;
		    Long course_id=null;
		    Double score=0.0;
		    String sc=null;
			MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
			MultipartFile file = mpReq.getFile("file_data");
			/*String fileName = file.getOriginalFilename();*/
			/*List<String> warningMesages=null;*/
			List<CourseScoreRecordDetailEntity> detailList=new ArrayList();
			CourseScoreRecordEntity courseScoreRecord=new CourseScoreRecordEntity();
				FileInputStream fio = (FileInputStream) file.getInputStream();
				List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
				 //平台ID
				courseScoreRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
			    //创建用户
				courseScoreRecord.setCreatePerson(getUserId());
				  //修改用户
				courseScoreRecord.setModifyPerson(getUserId());
			    //创建时间
				courseScoreRecord.setCreateTime(new Date());
				 //修改时间
				courseScoreRecord.setModifyTime(new Date());
				//默认状态
				courseScoreRecord.setDr(0);
				//默认审核状态
				courseScoreRecord.setAudited(0);
                //自动生成单号
				String scoreRecordNo = "FS" + (long)scoreRecordKGS.nextId();
				courseScoreRecord.setScoreRecordNo(scoreRecordNo);
				String exceptMsg = "";
				String[] header=dataList.get(0);
				ArrayList<String> exceptList = new ArrayList<String>();
				if(header.length<4){
					exceptMsg="总列数不正确，请核对一下列数；";
					 exceptList.add(exceptMsg);
				}
				else if(!header[0].trim().equals("报考登记单号")){
					exceptMsg="对应的列名不对，请核对一下是否与【报考登记单号】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[1].trim().equals("课程编号")){
					exceptMsg="对应的列名不对，请核对一下是否与【课程编号】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[2].trim().equals("手机号")){
					exceptMsg="对应的列名不对，请核对一下是否与【手机号】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(!header[3].trim().equals("分数")){
					exceptMsg="对应的列名不对，请核对一下是否与【分数】匹配;";
					exceptList.add(exceptMsg);
				}
				else if(header.length>=4&&header[0].trim().equals("报考登记单号")&&header[1].trim().equals("课程编号")&&header[2].trim().equals("手机号")
					&&header[3].trim().equals("分数")) {
				    dataList.remove(0);
				   for (int i = 0; i < dataList.size(); i++)
				{
					   CourseScoreRecordDetailEntity courseScoreRecordDetail=new CourseScoreRecordDetailEntity();
					   String[] dataArr=dataList.get(i);
					      Map<String, Object> map = getMapPage(request);
					       boolean flag = false;
					       exceptMsg="第" + new Integer(i+2).toString() + "行数据不能导入原因是：";
					       
					       courseNo=dataArr[1];
					       mobilePhoneNo=dataArr[2];
						   sc=dataArr[3];
						   map.put("mobilePhoneNo", mobilePhoneNo);
						   map.put("courseNo", courseNo);
						 /*  map.put("examRecordNo", examRecordNo);*/
						   map.put("sc", sc);
						   if (mobilePhoneNo == null || mobilePhoneNo.equals(""))
						   {
							   exceptMsg += "手机号不能为空";
							   exceptList.add(exceptMsg);
							   flag=true;
						   }else{
							int count=usersService.countUserIdByMobilePhoneNo(map);
						   if(count<=0){
						   if(flag==true){
							   exceptMsg += ",手机号不存在";
						   }else{
							   exceptMsg += "手机号不存在"; 
							   flag=true;
						   }
					   }else{
						 //根据手机号获取 用户的ID
						   user_id =usersService.getUserIdByMobilePhoneNo(map);
					   }
				   }
						   if (courseNo == null || courseNo.equals("")){
							   if(flag==true){
							   exceptMsg += ",课程编号不能为空";
							   }else{
							   exceptMsg += "课程编号不能为空";
							   flag=true;
							   }
						   }else{
							   int count=coursesService.countCourseIdBycourseNo(map);
						   if(count<=0){
						   if(flag==true){
							   exceptMsg += ",课程编号不存在";
						   }else{
							   exceptMsg += "课程编号不存在"; 
							   flag=true;
						   }
					   }
						   else if(count>=2){
							   if(flag==true){
								   exceptMsg += "系统数据紊乱";
							   }else{
								   exceptMsg += ",系统数据紊乱";
								   flag=true;
							   }
						  } 
						   else{
						 //根据课程名获取课程的ID		   
							  course_id=coursesService.getCourseIdBycourseNo(map);
					   }
				   }
						   if (sc == null || sc.equals("")){
							   if(flag==true){
							   exceptMsg += ",分数不能为空";
							   }else{
							   exceptMsg += "分数不能为空";
							   flag=true;
							   }
						   }else{
							   if(Double.parseDouble(sc)<=0.0){
								   if(flag==true){
									   exceptMsg += ",分数必须不能小于等于零";
								   }else{
									   exceptMsg += "分数不能小于等于零"; 
									   flag=true;
								   }
							   }
						   }
						   score=Double.parseDouble(sc);
						   
						   if (flag)
						   {
							   exceptList.add(exceptMsg);
							   continue;
						   }
						  
						   courseScoreRecordDetail.setCourseId(course_id);
						   courseScoreRecordDetail.setUserId(user_id);
						   courseScoreRecordDetail.setScore(score);
						   if(score>=0){
							   courseScoreRecordDetail.setPassed(1);
						   }
						   detailList.add(courseScoreRecordDetail);
						   if(i==0){
						       examRecordNo=dataArr[0];
						       continue;
						       }
					}
				}
				/*return R.ok();*/
				String errMsgErr = "";
				for (int i = 0; i < exceptList.size(); i++)
				{
					errMsgErr += exceptList.get(i) + "<br>";
					/* System.out.println(warningMesages.get(i)); */
				}
				if (errMsgErr.equals(""))
				{
					courseScoreRecord.setDetailList(detailList);
					Map<String, Object> map = getMapPage(request);
					map.put("examRecordNo", examRecordNo);
					courseScoreRecord.setExamRecordId(courseExamRecordService.getExamRecordIdByNo(map).getExamRecordId());
					courseScoreRecord.setProductId(courseExamRecordService.getExamRecordIdByNo(map).getProductId());
					//保存
					courseScoreRecordService.save(courseScoreRecord);	
					return R.ok();
				}
				else
				{
					return R.error(1,errMsgErr);
			}
		}
}
