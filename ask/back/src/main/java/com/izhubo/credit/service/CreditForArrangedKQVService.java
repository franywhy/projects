package com.izhubo.credit.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.izhubo.credit.vo.ArrangedSYNCVO;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditStandard;

 
 
 


/**
 * 学分中同步档案单中中科目对应的学员的出勤率、教室、老师</p>
 * 
 * 根据昨天修改的考勤的学员根据 班级名+老师ID+科目ID+商机ID 取得应上课次、签到课次、出勤率</p>
 * 取得学分科目 过滤学分中不取的科目的更新源,并取类课程类型
 * 同步源相同学员操作过滤逻辑
 *
 * 1.dr不同时取最小的 dr相同时 取最大出勤率的 最终存到 map<科目类型+商机,更新源VO>
 * 根据  科目ID+商机ID  取得档案单</p>
 * 更新逻辑</p>
 *  1.当学员已经修满考勤学分的 不再更新</p>
 *  除1之后 直接更新上面的档案单
 *  档案单中如果找不到的不用更新
 *   
 * 
 * @author lintf 
 *
 */
public interface  CreditForArrangedKQVService {
	 /**
	  * 从同步接口中取得每天的考勤有变更的学员 
	  * @param SYDATE
	  * @param SYEDATE
	  * @param secretkey
	  * @return
	  */
	 public  List<ArrangedSYNCVO> getArrangedSYNCVO (String SYSDATE,String SYEDATE, String secretkey);
	/**
	 * 和每天变动的报名表中2017年报名且2016年有报名表的学员的2017年以前的考勤
	 * @param startTime
	 * @param endTime
	 * @param secretkey
	 * @return
	 */
	 public List<ArrangedSYNCVO> getArrangedSYNCVOOld(String startTime,
				String endTime, String secretkey);
	 /**
	  * 取得当前更新源的map 
	  * </p>
	  * @param sourcedate
	  * @param creditStandardmap
	  * @return
	  */
	 public  Map<String,ArrangedSYNCVO> ComputeArrangedSYNCVO(List<ArrangedSYNCVO> sourcedate ,
				Map<String,CreditStandard> creditStandardmap,Map<String,String> student_list
			 );
	 /**
	  * 根据来源map取得学分制中的map
	  * @param session 数据库seseion
	  * @param sourcedate
	  * @return
	  */
	 public  	 Map<String,CreditRecord> getLocalRecordMap(Session session,  Map<String,String> student_list);
	 /**
	  * 比对更新源与学分档案  只取考勤分没有修完的档案，如果来源是1则直接全null 否则更新老师和班级 
	  * @param sourcemap
	  * @param localmap
	  * @param updatemap
	  */
	 public void ComputeRecord( Map<String,ArrangedSYNCVO> sourcemap,Map<String,ArrangedSYNCVO> sourcemap_pass, Map<String,CreditRecord> localmap,Map<String,CreditRecord> updatemap);
	 /**
	  * 保存当前修改好的 学分档案中的考勤和老师信息
	  * @param session
	  * @param updatemap
	  */
	 public void SaveRecord(Session session,Map<String,CreditRecord> updatemap);
	

}
