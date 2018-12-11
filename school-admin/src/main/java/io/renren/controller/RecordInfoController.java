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
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseExamRecordDetailService;
import io.renren.service.CourseExamRecordService;
import io.renren.service.CourseUserplanDetailService;
import io.renren.service.CourseUserplanService;
import io.renren.service.CoursesService;
import io.renren.service.MallAreaService;
import io.renren.service.MallOrderService;
import io.renren.service.RecordInfoService;
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
@RequestMapping("record/recordInfo")
public class RecordInfoController extends AbstractController {
	/**
	 * 导入和导出时用到的字段
	 */
	private static final String[] ExcelTitleName =new String [] {"序号","姓名","性别","身份证","电话号码","年龄","学历","QQ","是否结婚","是否生育","每天可学习时间(min)","现工作岗位","会计类证书"};
	private static final String[] ExcelTitleKey =new String [] {"rowNo","name","sex","idCard","mobile","age","record","qq","marriageStatus","fertilityStatus","studyTimeOfDay","postName","accountingCertificates"};
	/**
	 * 判断不能为空的项目
	 */
	private static final String[] ExcelValueNotnull =new String [] {"name","idCard","record","mobile"};
	@Autowired
	private RecordInfoService recordInfoService;
	
	
	
	/**
	 * 根据id取得基础信息 
	 *@param recordInfoId
	 *@param request
	 *@return
	 * @author lintf
	 * 2018年8月13日
	 */
	@ResponseBody
	@RequestMapping("/info/{recordId}")
	public R info(@PathVariable("recordId") Long recordInfoId, HttpServletRequest request) {	
		
		Map<String, Object> queryMap= new HashMap<String,Object>();
		queryMap.put("recordId", recordInfoId);
		 RecordInfoEntity  recordInfo = recordInfoService.queryObject(queryMap);
		return R.ok().put("recordInfo", recordInfo);
	} 
	
	
	/**
	 * 更新基础信息表
	 */
	@SysLog("页面更新学员档案基础信息")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("record:recordInfo:update")
	public R  update(@RequestBody RecordInfoEntity e) {
		 e.setModifyPerson(getUserId());
		recordInfoService.upDateRecordInfo(e);
		return R.ok();
		
	} 
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping(value="/list",produces="text/html;charset=UTF-8")
   @RequiresPermissions("record:recordInfo:list")
	public R list( HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "name");
		stringQuery(map, request, "mobile");
		stringQuery(map, request, "idCard"); 
		longQuery(map, request, "recordId"); 
		longQuery(map, request, "teacherId"); 
	 
		List<RecordInfoEntity> infoList = recordInfoService.queryList(map);
		int total = recordInfoService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(infoList, total , request);
		return R.ok().put(  pageUtil);
	}
	
	
	/**
	 * 学员档案基础信息EXCEL批量导入
	 * @throws Exception 
	 * */
	@SysLog("学员档案基础信息EXCEL批量导入")
	@ResponseBody
	@RequestMapping("/getExcelRecordInfoImportData")
	 @RequiresPermissions("record:recordInfo:importData")
	
	
	public R getRecordInfoImport(HttpServletRequest request) throws Exception {

		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data"); 
		List<RecordInfoEntity> detailList = new ArrayList<RecordInfoEntity>();
		  
		FileInputStream fio = (FileInputStream) file.getInputStream();
		List<String[]> dataList = ExcelReaderJXL.readExcel(fio);

		String exceptMsg = "";
		String notMatchMsg="";
		String[] header = dataList.get(0);
		ArrayList<String> exceptList = new ArrayList<String>();

		// 判断列名是否对应得上

		boolean headerCheck = true;// 当列数和列名是否对得上
		if (header.length < this.ExcelTitleName.length) {
			exceptMsg = "总列数不正确，请核对一下列数；";
			headerCheck = false;
			exceptList.add(exceptMsg);
		} else {
			for (int i = 0; i < header.length; i++) {

				if (i < this.ExcelTitleName.length) {
					if (!header[i].trim().equals(ExcelTitleName[i])) {
						exceptMsg = "第" + (i + 1) + "列的数据与模板不一致,应该是" + ExcelTitleName[i] + ",不应该是" + header[i].trim()
								+ ",请认真核对。";
						exceptList.add(exceptMsg);
						headerCheck = false;
					}
				}

			}
		}

		// 判断列名完成

		if (headerCheck) {

			for (int i = 1; i < dataList.size(); i++) {
				notMatchMsg="";
				boolean hasError = false; // 是否有错误 有错误的不导入只加入错误信息 并提示是哪一行的错误
				exceptMsg ="";
				Map<String, Object> map = getMapPage(request);
				String[] dataArr = dataList.get(i);
				int count=0;
				for (int n = 0; n < ExcelTitleKey.length; n++) {
					if (dataArr[n]==null||"".equals(dataArr[n])) {
						count++;
					}
					map.put(ExcelTitleKey[n], dataArr[n]);

				}
				/**
				 * 如果有一行是全空的则不会再做判断这一行
				 */
				if (count==ExcelTitleKey.length) {
					continue;
				}
				
				
				
				
				for (String notNul : this.ExcelValueNotnull) {
					if (map.get(notNul) == null || map.get(notNul).toString().equals("")) {
						
						exceptMsg += notNul + " ";
					//	exceptList.add(exceptMsg);
						hasError = true;
					}
					if ("mobile".equals(notNul)) {
						String mobile=map.get(notNul).toString().trim();//.replaceAll("\\D+", "");
						if (mobile.length()==11&&mobile.matches("^(1)\\d{10}$")) {
							map.put(notNul, mobile);
						}else {
							notMatchMsg+= " 电话号码不合法!";
							hasError = true;
						}
						
					}
					if ("idCard".equals(notNul)) {
						String idcard=map.get(notNul).toString().trim();
						if (idcard.length()==18||idcard.length()==15) {
							if(	idcard.matches("^\\d{17}([0-9]|x|X){1}$")||
								
								idcard.matches("^\\d{14}([0-9]|x|X){1}$") ) {
							 
							}else {
								notMatchMsg+= " 身份证不合法!";
								hasError = true;
							}
								
								
							}else {
							
							
							  if (idcard.matches("^[a-zA-Z][0-9]{9}$") || // 台湾
								 idcard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$") ||// 澳门
						          idcard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) // 香港
						           {
								  
						           } {
						        		notMatchMsg+= " 身份证不合法!";
										hasError = true;
						           }
						         
							}
						
					}
					
					
					
					
				}

				if (hasError) { // 如果有错误的 则加入提示列表 不加到导入列表
					exceptMsg= exceptMsg.trim().length()>0? exceptMsg+ "字段不能为空。":"";
					exceptList.add("第" + new Integer(i + 1).toString() + "行数据："+exceptMsg+notMatchMsg);
					continue;
				} else {
					RecordInfoEntity info = new RecordInfoEntity(map);

					// 创建用户
					info.setCreatePerson(getUserId());
					// 修改用户
					info.setModifyPerson(getUserId());
					// 创建时间
					info.setCreateTime(new Date());
					// 修改时间
					info.setModifyTime(new Date());
					// 默认状态

					detailList.add(info);
				}

			}

		} 
		String errMsgErr = "";
		for (int i = 0; i < exceptList.size(); i++) {
			errMsgErr += exceptList.get(i) + "<br>"; 
		}
		// 没有错误并的才保存
		if (errMsgErr.equals("") && detailList != null && detailList.size() > 0) {
			String seccMessage="导入完成:";
			int updateCount=0;
			int insertCount=0;
			int failCount=0;
			for (RecordInfoEntity e : detailList) {
				
				int result=recordInfoService.upDateOrSaveByMobile(e);//更新还是新增是以电话号进行判断  如果已经存在的则更新
				if (result==1) {
					insertCount++;
				}else if (result==2) {
					updateCount++;
				}else if (result==0) {
					failCount++;
				}
				
			}
			return R.ok();
			//return R.ok(seccMessage+"新增:"+insertCount+"条,更新:"+updateCount+"条, 错误:"+failCount+"条");
		} else {
			return R.ok(errMsgErr);
		}

	}
/**
 * 学员档案基础信息模板导出
 *@param request
 *@param response
 * @author lintf
 * 2018年8月13日
 */
	@ResponseBody
	@RequestMapping("/exportExcelRecordInfoTemplate")
	public void   DownloadRecordInfoImportTemplate(HttpServletRequest request,HttpServletResponse response) {
		
		 	String[] cells = new String[ExcelTitleName.length];
		for (int i=0;i<ExcelTitleName.length;i++) {
			cells[i]="0,"+i+",0,0,"+ExcelTitleName[i];
		}
		
		// String arrStr = "0,0,0,0,手机号&0,1,0,0,报名省份&0,2,0,0,报考省份&0,3,0,0,课程编号&0,4,0,0,订单号&0,5,0,0,备注&0,6,0,0,产品线(自考产品线、会计产品线、双师产品线)";
		    /*String result=areaService.getDataByCondition(conditions);*/
			/*arrStr+=result;*/
		   // String cellsStr = new String(arrStr.getBytes("GBK"), "GBK");
		//	String[] cells = cellsStr.split("&");
		try {
			String filename = "基础信息导入模板-" + Calendar.getInstance().getTimeInMillis() + ".xls";
			filename = new String(filename.getBytes("GBK"), "iso-8859-1");
			response.setContentType("text/html; charset=UTF-8");
			response.addHeader("Content-Disposition", (new StringBuilder())
	                .append("attachment;filename=")
	                .append(filename+";").toString());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			ExcelReaderJXL.exportToJxlExcel(filename, "基础信息导入", cells, toClient);
			toClient.flush();
			toClient.close();
		}catch(Exception es) {
			logger.error("exportExcelCourseExamRecordTemplate has erros,message is {}",es);
		}
			/* return R.ok();*/
	}
}
