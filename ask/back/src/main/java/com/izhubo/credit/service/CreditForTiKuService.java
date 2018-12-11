package com.izhubo.credit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.izhubo.credit.vo.TiKuScoreVO;
import com.mysqldb.model.CreditDef;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordTiKuScore;
import com.mysqldb.model.CreditStandard;
  

/**
 * 学分与题库数据交互接口
 *
 */
public interface CreditForTiKuService {
/**
 * 取得完课在题库中做作业的成绩
 * @return
 */
	public  Map<String,TiKuScoreVO>  getGetStudentOfClassEndingWork(String beginDate, String endDate, String secretkey,Map<String, CreditStandard> creditStandardmap)  throws Exception ;
/**
 * 根据题库单据取得学员学分数据
 * @param billkeyList
 * @return  <billkey,billvo>
 * @throws Exception
 */
	public  Map<String,CreditRecordTiKuScore>  getLocaldate(Session session,Map<String,TiKuScoreVO> sourcedate) throws Exception;

	/**
	 * 按billkeylist取得学分制库中的题库单的信息 ，QueryAll如果是true则取得全部的 
	 * @param billkeylist
	 * @param queryall
	 * @return
	 */
	public List<TiKuScoreVO> getTiKuScoreByBillKeyList(List<String> billkeylist,boolean QueryAll);
    /**
     * 学分制中插入题库成绩单
     * @param addTiKuScoreList
     * @return 
     * @return
     */
	public   void addTiKuScore(List<CreditRecordTiKuScore> addTiKuScoreList,Session session);
	/**
	 * 学分制中修改题库成绩单
	 * @param updateTiKuScoreList
	 * @return 
	 * @return
	 */
	public   void UpdateTiKuScore(List<CreditRecordTiKuScore> updateTiKuScoreList,Session session);
	/**
	 * 取得作业成绩之后与学分中的分数运算
	 * @param Sourcedate
	 * @param localdate
	 * @throws Exception	 */

	 
	public   void SaveRecord(  Map<String,String> workPassMap,Map<String,String> examPassMasp,
			Session session);
	
	/**
	 * 题库作业单运算
	 * @param Sourcedate
	 * @param localdate
	 * @param addTiKuScoreList
	 * @param updateTiKuScoreList
	 * @param passmap 题库单通过列表
	 * @param type 类型  W：作业 E:实操考试
	 * @throws Exception
	 */
	public void ComputeWorkScoreRule(Map<String,TiKuScoreVO> Sourcedate,Map<String,CreditRecordTiKuScore> localdate,	List<CreditRecordTiKuScore> addTiKuScoreList,
			List<CreditRecordTiKuScore> updateTiKuScoreList,Map<String,String> passmap,String type) throws Exception ;
	/**
	 * 取得完课在题库中考试的成绩
	 * @return
	 */
	public	Map<String,TiKuScoreVO>   getGetStudentOfClassEndingExan(String beginDate, String endDate, String secretkey,Map<String, CreditStandard> creditStandardmap)  throws Exception; 
/**
 * 取得题库成绩类中的成绩单中取得班级类型
 * @param tikuvo
 * @param creditStandardmap
 */
	public void getSubjectTypebyNC(List<TiKuScoreVO> tikuvo,Map<String,CreditStandard> creditStandardmap) throws Exception;
	/**
	 * 特殊情况中NC中要设置成满分的  会计基础只有6节课的
	 * @param tikuvo
	 * @param creditStandardmap
	 * @throws Exception
	 */
	public void getEndClassPassAct(Map<String,TiKuScoreVO> tikuMap,Map<String,CreditStandard> creditStandardmap,String beginDate,String endDate) throws Exception;
	
	/**
	 *  取到往月阶段考试没有及格的学员
	 * @param session
	 * @param failStudents 往期
	 */
	public void getFailExamStudent(Session session,Map<String,CreditRecordTiKuScore> failStudents);
	 /**
	  * 根据学员和排课取得补考成绩并运算出修过的成绩单和通过的档案
	  * @param beginDate
	  * @param endDate
	  * @param secretkey
	  * @param creditStandardmap
	  * @param failStudents
	  * @param updateTiKuScoreList
	  * @param examPassMasp
	  * @return
	  * @throws Exception
	  */
	public  void  getGetStudentOfFailExam(String beginDate, String endDate,
			String secretkey,Map<String, CreditStandard> creditStandardmap,
			Map<String,CreditRecordTiKuScore> failStudents,
			List<CreditRecordTiKuScore> updateTiKuScoreList,
			Map<String,String> examPassMasp
			)  throws Exception ;

	
	
	/**
	 * 根据学员主键和科目取得实操阶段考试分数
	 * @param dh
	 * @param coursecode
	 * @param result
	 * @throws Exception
	 */
 
	public double getExamComplianceRateByStudentIdAndCoursecode(String studentID,
			String coursecode) throws Exception; 
	
	
	
	/**
	 * 根据学员电话和科目取得学员作业达标率
	 * @param dh
	 * @param coursecode
	 * @param result
	 * @throws Exception
	 */
 
	public double getHomeWorkCompletionByDhandCoursecode(String dh,
			String coursecode) throws Exception; 
	
	/**
	 * 根据学员学分档案取得学员作业达标率
	 * @param Record
	 * @throws Exception
	 */
	public void getHomeWorkCompletionByRecord(
			List< CreditRecord> Record,Map<String, String> defmap,Session session 
			)throws Exception; 
	

}
