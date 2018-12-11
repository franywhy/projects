package com.izhubo.admin

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.izhubo.admin.answer.TeacherOrderReportController
import com.izhubo.rest.anno.RestWithSession;

@RestWithSession
class TimerController extends BaseController {
	
	@Resource
	TeacherOrderReportController teacherOrderReportController;
	
	private static List REPORT_LIST = new ArrayList();;
	static{
		
		REPORT_LIST.add(["name" : "会答首页教师展示每日定时器" , "url" : "/timer/teacherOrderDay" , "isAsyn" : true]);
		
	}
	
	def list(){
		return ["code" : 1 , "data" : REPORT_LIST];
	}
	
	def teacherOrderDay(HttpServletRequest req){
		req.setAttribute("channle", 1);
		teacherOrderReportController.teacherOrderDay(req);
		return OK();
	}
}
