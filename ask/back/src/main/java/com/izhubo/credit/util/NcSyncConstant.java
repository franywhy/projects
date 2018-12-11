package com.izhubo.credit.util;

import com.izhubo.rest.AppProperties;

/**
 * NC学分数据的URL
 * @author 严志城
 *
 */
public class NcSyncConstant {
	/**
	 * 获取学分秘钥
	 */
	public static String getMyCreditkey() {
		return AppProperties.get("credit.mycredit.key") ;
	}


	/**
	 * 查询NC数据秘钥
	 */
	public static String getNcSecretkey() {
		return AppProperties.get("credit.ncsecret.key") ;
	}

	/**
	 * NC服务器地址
	 */
	public static String getSyncUrl() {
		return AppProperties.get("credit.sync.url") ;
	}
	
	
	/**
	 * 题库webservice服务器地址
	 */
	public static String getWebserviceUrl() {
		return AppProperties.get("credit.webservice.url") ;
	}
	/**
	 * 恒企在线SYNC地址
	 */
	public static String getHqonlinesyncUrl() {
		return AppProperties.get("credit.hqonlinesync.url") ;
	}
	/**
	 * 恒企在线SYNCkey
	 *@return
	 * @author lintf
	 * 2018年6月26日
	 */
    public static String getHqonlinesyncKey() {
		return AppProperties.get("credit.hqonlinesync.key") ;
	} 
	
	
	
	
	/**
	 * NC获取结课学员考勤率URL
	 */
	public static String getAttendanceUrl(){
		return getSyncUrl()+"GetClassEndingAttendance";
		
	}
	
	/**
	 * NC获取结课学员实操作业分URL
	 */
	public static String getBusyworkUrl(){
		return getSyncUrl()+"GetClassEndingBusywork";
		
	}
	
	/**
	 * NC获取学员会计证成绩分数URL
	 */
	public static String getAccountingCertificateUrl(){
		return getSyncUrl()+"GetAccountingCertificateScore";
	}
	/**
	 * NC获取初级职称成绩分数URL
	 */
	public static String getJuniorLevelAccountantUrl(){
		return getSyncUrl()+"GetJuniorLevelAccountantScore";
	}
	/**
	 * NC获取中级职称成绩分数URL
	 */
	public static String getMediumLevelAccountantUrl(){
		return getSyncUrl()+"GetMediumLevelAccountantScore";
	}
	
	
	/**
	 * NC获取排课计划数据URL
	 */
	public static String getArrangedPlanUrl(){
		return getSyncUrl()+"GetArrangedPlan";
		
	}
	
	/**
	 * NC获取报名表数据URL
	 */
	public static String getRegistrationInfoUrl(){
		return getSyncUrl()+"GetRegistrationInfo";
		
	}
	
	/**
	 * 通过日期获取NC报名表数据URL
	 */
	public static String getRegistrationInfoByDateUrl(){
		return getSyncUrl()+"GetRegistrationInfoByDate";
		
	}
	
	
	/**
	 * 通过学员主键获取排课计划的授课老师数据URL
	 */
	public static String getTeacherFromArrangedPlanUrl(){
		return getSyncUrl()+"GetTeacherFromArrangedPlan";
		
	}
	
	/**以下是2017年10月更新的Nc中与题库同步的新接口**/
	
	/**
	 * 从NC中获取每天TS变动的报名表
	 * 
	 * @return http://*.8889/ncws/GetRegistrationSYNC
	 */
	public static String getRegistrationSYNCUrl(){
		return getSyncUrl()+"GetRegistrationSYNC";
		
	}
	/**
	 * 从考勤有变动的学员的全部排课的考勤情况
	 * @return http://*.8889/ncws/GetArrangedKQVSYNC
	 */
	public static String getArrangedKQVSYNCUrl(){
		return getSyncUrl()+"GetArrangedKQVSYNC";
		
	}
	/**
	 * 从报名表有变的2017年的学员取2016年以前的考勤的
	 * @return http://*.8889/ncws/GetArrangedKQVSYNC
	 */
	public static String getArrangedKQVSYNCUrlOld(){
		return getSyncUrl()+"GetArrangedKQVSYNCOld";
		
	}
	/**
	 * 取得NC中的成绩单 包括学分单和初级、中级、的成绩单
	 * @return http://*.8889/ncws/GetNCscoreSYNC
	 */
	public static String getNCscoreSYNCUrl(){
		return getSyncUrl()+"GetNCscoreSYNC";
		
	}
	/**
	 * 取得NC中结课的学员 这个是精简的取法 不取考勤
	 * @return http://*.8889/ncws/GetClassEndStudent
	 */
	public static String getClassEndStudentUrl(){
		return getSyncUrl()+"GetClassEndStudent";
		
	}
	/**
	 * 取得NC中结课的学员的精简取法 特殊情况
	 * @return http://*.8889/ncws/GetClassEndStudentAct
	 */
	public static String getClassEndStudentActUrl(){
		return getSyncUrl()+"GetClassEndStudentAct";
		
	}
	
	
	/**
	 * 取得NC中的校区信息 大区名 省份名 城市名  
	 * @return http://*.8889/ncws/GetNcOrgMain
	 */
	public static String getNcOrgMainUrl(){
		return getSyncUrl()+"GetNcOrgMain";
		
	}
	
	/**
	 * 取得NC中的老师名称
	 * @return http://*.8889/ncws/GetTeacherNameMain
	 */
	public static String getNcTeacherNameMainUrl(){
		return getSyncUrl()+"GetTeacherNameMain";
		
	}
	
}
