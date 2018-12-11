package com.izhubo.credit.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.izhubo.admin.BaseController; 
import com.izhubo.credit.service.CreditDataExportService; 
import com.izhubo.credit.service.CreditDataModefyService;
import com.izhubo.credit.service.CreditDelSameBillService;
import com.izhubo.credit.service.impl.CreditDelSameBillServiceImpl;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.vo.HttpServiceResult;
import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecordReportTemp;
import com.mysqldb.model.Creditpercent;
 

 
/**
 * 学分数据导出控制器
 * lintf 2017年11月21日11:26:06
 * @param <CreditPercentDetail>
 *
 */
//@Controller

public class CreditDataExportController  extends BaseController{
	/*
	 @Resource
	 CreditDataExportService creditDataExportService;
	 @Resource
	 CreditDataModefyService creditDataModefyService;
	 *//**
	  * 升级 
	  * @param session
	  * @param request
	  * @param response
	  * @return
	  *//*
		@RequestMapping(value="update")
		@ResponseBody
		public String getUpdate(HttpSession session,HttpServletRequest request,HttpServletResponse response)  {
			 String path = request.getSession(true).getServletContext().getRealPath("/WEB-INF/data");
			 String filepath=path+File.separator+ "export.ini";
				String strLine;
				String strr="";
			
			 
			 File file = new File(filepath);
			 try {
				BufferedReader bre = new BufferedReader(new InputStreamReader(new FileInputStream( file), "UTF-8"));	 

				 
					while ((strLine = bre.readLine()) != null) {
						strr=strr +"\n"+strLine;
					}
					bre.close();
			 		return new String(strr.getBytes("UTF-8"),"iso-8859-1"); 
				//	return strr;
				} catch ( Exception e) {
					// TODO Auto-generated catch block
				 return null;
			}
		 
		}
		
	 
		@RequestMapping(value="billupdate")
		@ResponseBody
		public String getBillUpdate(HttpSession session,HttpServletRequest request,HttpServletResponse response,String billkey)  {
			 String path = request.getSession(true).getServletContext().getRealPath("/WEB-INF/data/bill");
			 String filepath=path+File.separator+ billkey+".ini";
				String strLine;
				String strr="";
			//	 Reader reader=new InputStreamReader(new FileInputStream( path), "UTF-8");
			 
			 File file = new File(filepath);
			 try {
			//	BufferedReader bre = new BufferedReader(new FileReader(file));
				  
					BufferedReader bre = new BufferedReader(new InputStreamReader(new FileInputStream( file), "UTF-8"));	 
					while ((strLine = bre.readLine()) != null) {
						strr=strr +"\n"+strLine;
					}
					bre.close();
					 
				
				 return new String(strr.getBytes("UTF-8"),"iso-8859-1"); 
				// return strr;
				 
				} catch ( Exception e) {
					// TODO Auto-generated catch block
				 return null;
			}
		 
		}
		
	 
	 
 
	*//**
	 * 
	 * @param request
	 * @param beginDate
	 * @param endDate
	 * @param exportKey 学分库数据导出key()
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/creditexport")
	@ResponseBody
	public HttpServiceResult GetCreditbyRegdate(HttpServletRequest request, String beginDate,String endDate,String exportKey ) throws Exception{
		 
		 
		 HttpServiceResult hsr = new HttpServiceResult();
		*//**
		 * 检查exportKey是否正确  exportKey=MD5(NcSyncConstant.getMyCreditkey()+年月日时+星期几)
		 *//*
 
		//if(!MD5Util.getMD5Code( NcSyncConstant.getMyCreditkey()+DateUtil.DateToString(new Date()).substring(1,13)+DateUtil.GetWeekDay(new Date())).equals(exportKey)){
		//	hsr.setCode(HttpServiceResult.STATUS_FAILURE_VERIFY);
		///	hsr.setMsg("验签失败！");
		//}else {
		 List<CreditRecordReportTemp>  list=creditDataExportService.getRecordbyRegdate(beginDate,endDate);
 
			//hsr.setCode(HttpServiceResult.STATUS_SUCCESSFUL);
		 	hsr.setData(list);
		 	hsr.setMsg("操作成功！");
		//}
		}else if(!keyRignt){
			hsr.setCode(HttpServiceResult.STATUS_FAILURE_VERIFY);
			hsr.setMsg("验签失败！");
		}else if(!converSuccess){
			hsr.setCode(HttpServiceResult.STATUS_FAILURE);
			hsr.setMsg("参数验证失败！");
		}
		 return hsr;
	}
	
	
	*//**
	 * 检测NC与
	 * @param request
	 * @param beginDate
	 * @param endDate
	 * @param exportKey
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/checkNcDateByRegdate")
	@ResponseBody
	public HttpServiceResult  GetCheckNcDateByRegdate(HttpServletRequest request, String beginDate,String endDate   ) throws Exception{
		 
		 HttpServiceResult hsr = new HttpServiceResult();
	  
		 Date startday=DateUtil.StringToDate("2017-01-01 00:00:00");
		Date endday=new Date();
		 Map<String, String> mon=DateUtil.getMonthList(startday, endday);
		 CreditDelSameBillService service= new CreditDelSameBillServiceImpl();
			//service.checkNCdate(session, tx, beginDate, endDate);
		for (Entry<String, String> s:mon.entrySet()){
			
			Date date=DateUtil.StringToDate(s.getKey()+"-01 00:00:00");
			beginDate =DateUtil.getMonthFirstDay(date).substring(0,10);
			endDate=DateUtil.getMonthLastDay(date).substring(0,10);
			//creditDataExportService.CheckNcDateByRegdate(beginDate, endDate);
		//	service.checkNCdate(session, tx, beginDate, endDate);
		}
		
		
		// List<CreditRecord> list=	creditDataExportService.CheckNcDateByRegdate(beginDate, endDate);
		
		 
		 
		// hsr.setData(list);
	 	hsr.setMsg("操作成功！");
 
		 
		 return hsr;
	}
	*//**
	 * 导出报表明细
	 * @param request
	 * @param dbilldate
	 * @param beginDate
	 * @param endDate
	 * @param exportKey
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/exportdetail")
	@ResponseBody
	public HttpServiceResult GetExportDetail(HttpServletRequest request, String dbilldate,String beginDate,String endDate,String exportKey ) throws Exception{
		 
		 
		 HttpServiceResult hsr = new HttpServiceResult();
		*//**
		 * 检查exportKey是否正确  exportKey=MD5(NcSyncConstant.getMyCreditkey()+年月日时+星期几)
		 *//*
 
		//if(!MD5Util.getMD5Code( NcSyncConstant.getMyCreditkey()+DateUtil.DateToString(new Date()).substring(1,13)+DateUtil.GetWeekDay(new Date())).equals(exportKey)){
		//	hsr.setCode(HttpServiceResult.STATUS_FAILURE_VERIFY);
		///	hsr.setMsg("验签失败！");
		//}else {
		  List<CreditPercentDetail>  list= creditDataExportService.getPercentDetailbyDbilldate (dbilldate,beginDate,endDate);
 
			//hsr.setCode(HttpServiceResult.STATUS_SUCCESSFUL);
		 	hsr.setData(list);
		 	hsr.setMsg("操作成功！");
		//}
		}else if(!keyRignt){
			hsr.setCode(HttpServiceResult.STATUS_FAILURE_VERIFY);
			hsr.setMsg("验签失败！");
		}else if(!converSuccess){
			hsr.setCode(HttpServiceResult.STATUS_FAILURE);
			hsr.setMsg("参数验证失败！");
		}
		 return hsr;
	}
	*//**
	 * 导出报表数据
	 * @param request
	 * @param dbilldate
	 * @param beginDate
	 * @param endDate
	 * @param exportKey
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/exportreport")
	@ResponseBody
	public HttpServiceResult GetExportReport(HttpServletRequest request, String dbilldate,String beginDate,String endDate,String exportKey ) throws Exception{
		 
		 
		 HttpServiceResult hsr = new HttpServiceResult();
		*//**
		 * 检查exportKey是否正确  exportKey=MD5(NcSyncConstant.getMyCreditkey()+年月日时+星期几)
		 *//*
 
		//if(!MD5Util.getMD5Code( NcSyncConstant.getMyCreditkey()+DateUtil.DateToString(new Date()).substring(1,13)+DateUtil.GetWeekDay(new Date())).equals(exportKey)){
		//	hsr.setCode(HttpServiceResult.STATUS_FAILURE_VERIFY);
		///	hsr.setMsg("验签失败！");
		//}else {
		  List<Creditpercent>  list=creditDataExportService.getPercentReport(dbilldate, beginDate, endDate);
 
			//hsr.setCode(HttpServiceResult.STATUS_SUCCESSFUL);
		 	hsr.setData(list);
		 	hsr.setMsg("操作成功！");
		//}
		}else if(!keyRignt){
			hsr.setCode(HttpServiceResult.STATUS_FAILURE_VERIFY);
			hsr.setMsg("验签失败！");
		}else if(!converSuccess){
			hsr.setCode(HttpServiceResult.STATUS_FAILURE);
			hsr.setMsg("参数验证失败！");
		}
		 return hsr;
	}
	
	
	 
	@RequestMapping("/test")
	@ResponseBody
	public HttpServiceResult GetTest(HttpServletRequest request, String dbilldate,String beginDate,String endDate,String exportKey ) throws Exception{
		
	   
		 creditDataExportService.getTest(beginDate, endDate);
		 //creditDataModefyService.SetRecordInfo();
		return null;
	}
	
	*/
	
}
