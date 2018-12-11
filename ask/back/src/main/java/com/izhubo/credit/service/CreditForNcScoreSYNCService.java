package com.izhubo.credit.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
 



import com.izhubo.credit.service.CreditForNcScoreSYNCService; 
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.vo.HttpServiceResult;
import com.izhubo.credit.vo.NCscoreSyncVO;
import com.izhubo.credit.vo.TiKuScoreVO;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordNcSore;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditStandard;
/**
 * 学分中同步NC中的全部成绩单
 * @author lintf 
 *
 */
public interface CreditForNcScoreSYNCService {
	/**
	 * 从接口处取数据
	 * @param SYDATE
	 * @param secretkey
	 * @return
	 */
 public  List<NCscoreSyncVO> getNCscoreSyncVO (String SYSDATE,String SYEDATE, String secretkey);
	/**
	 * 将来源list转成 map<bid,NCscoreSyncVO>，有在学分科目中的 则取得科目类型，没有在的 则dr=1
	 * @param sourcedate 来原的list
	 * @param sourcemap 要转后的map
	 * @param creditStandardmap 学分科目表 
	 */
  public void  ComputeSourcedate(List<NCscoreSyncVO> sourcedate,Map<String,NCscoreSyncVO> sourcemap,Map<String, CreditStandard> creditStandardmap
		  );
  
   /**
    * 根据更新源的key取得当前系统中的CreditRecordNcSore
    * @param session
    * @param sourcemap
    * @param localmap
    */
  public void getLocaldateforNCscore(Session session,
		  Map<String,NCscoreSyncVO> sourcemap,
		  Map<String,CreditRecordNcSore> localmap 
		  );
   
   /**
    * 比对更新源和本地 </p>
    * 比对规则：</p>
    * 1.当本地有这个更新源时，判断syts 更新的syts要大于本地的syts 添加到updatevo</p>
    * 
    * 2.当本地没有这个更新源时 且dr不等于1时  添加到AddVO
    * @param sourcemap
    * @param localmap
    * @param Addhvo
    * @param Updatehvo
    */
  public void ProcessLocalmapTosourcemap(
		  Map<String,NCscoreSyncVO> sourcemap,
		  Map<String,CreditRecordNcSore> localmap,
		  Map<String,CreditRecordNcSore> Addvo, 
		  Map<String,CreditRecordNcSore> Updatevo
	 
		  );
  
  
  
  
 
  /**
   * 持久化本次数据 保存NC成绩单,并将除了学分单据之外的全部的成绩单直接写到学分单。
   * @param session
   * @param cvo
   * @param hvo
   * @param studentidlist
   */
  public void  PersistenceVO(Session session,
		  Map<String,CreditRecordNcSore> Addvo, 
		  Map<String,CreditRecordNcSore> Updatevo,
		  String noteinfo

		  );
  
  
}
