package com.izhubo.credit.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.izhubo.credit.vo.ClassEndStudentVO;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditStandard;

/**
 * 学分每月运算类
 * 1. 取得当月完课的学员 
 * 2.完课学员取得NC成绩单
 * 3.完课学员取得考勤单
 * 4.完课学员取得题库成绩单
 *
 *
 *
 */
public interface CreditServiceComputeRule {
	/**
	 * 第一步取得当月结课的学员
	 * @param beginDate
	 * @param endDate
	 * @param secretkey
	 * @param creditStandardmap
	 * @throws Exception 
	 */
public Map<String,ClassEndStudentVO> getClassEndStudentlist(String beginDate, String endDate, String secretkey,Map<String, CreditStandard> creditStandardmap) throws Exception;
/**
 * 取得最近考试通过的学员
 * @param beginDate
 * @param endDate
 * @param secretkey
 * @param creditStandardmap
 */
public  void getExamPassStudentlistByLastTime(String beginDate, String endDate, String secretkey,Map<String, CreditStandard> creditStandardmap);
/**
 * 运算当月完课学员的考勤
 * @param session
 * @param sourcemap
 * @param localmap
 * @throws Exception 
 */
public  void ComputeAttendanceScore(Session session,Map<String,ClassEndStudentVO> sourcemap,Map<String,CreditRecord> localmap) throws Exception;
/**
 * 运算题库成 绩单 将结课的学员分别取得作业完成率和实操结课考试 通过的话直接修得学分
 * @param session
 * @param sourcemap
 * @param localmap
 * @throws Exception 
 */
public  void ComputeScore_tiku(Session session,Map<String,ClassEndStudentVO> sourcemap,Map<String,CreditRecord> localmap) throws Exception;
/**
 * 运算NC成绩单 将结课的学员的 作业完成率和NC中的从业中级初级成绩单重新从ncscore单中取数
 * @param session
 * @param sourcemap
 * @param localmap
 * @throws Exception 
 */
public  void ComputeScore_NC(Session session,Map<String,ClassEndStudentVO> sourcemap,Map<String,CreditRecord> localmap) throws Exception;
 
public void ComputeExamScore_tiku();
 
public void SaveCreditRecord(Session session, Map<String, CreditRecord> localmap) throws Exception;

}
