package com.izhubo.credit.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
 


import com.izhubo.credit.service.CreditForNcSYNCService; 
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.vo.HttpServiceResult;
import com.izhubo.credit.vo.RegistrationSYNCVO;
import com.izhubo.credit.vo.TiKuScoreVO;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditStandard;
/**
 * 学分中同步报名表数据接口 
 * @author lintf 
 *
 */
public interface CreditForNcSYNCService {
	/**
	 * 从接口处取数据
	 * @param SYDATE
	 * @param secretkey
	 * @return
	 */
 public  List<RegistrationSYNCVO> getRegistrationSYNCVO (String SYDATE,String SYEDATE, String secretkey);
	/**
	 * 处理同步取过来的数据 分别存在CVO 
	 * @param sourcedate 从同步接口中取来的数据
	 * @param Sourcecvo 从接口处同步来的未处理的CVO
	 * @param Sourcehvo 从接口处同步来的未处理的HVO 
	 * @param hvoidlist 本次hvo的主键
	 * @param cvoidlist 本次CVO的主键
	 */
  public void  ComputeSourcedate(List<RegistrationSYNCVO> sourcedate,Map<String,CreditRecordSignCVO> Sourcecvo,
		  Map<String,CreditRecordSign> Sourcehvo, List<String> hvoidist,List<String> cvoidlist,	Map<String,CreditStandard> creditStandardmap,
		  Map<String,String> notcheckmap
		  );
  
  /**
   * 取得学分中对应的HVO和CVO
   * @param session  
   * @param studentidlist
   * @param hvoidist 
   * @param cvoidlist
   * @param Localcvo 学分中的CVO<cid,CVO>
   * @param Localhvo 学分中的HVO<hid,HVO>
 * @throws Exception 
   */
  public void getLocaldateforNC(Session session,
		  List<String> studentidlist,List<String> hvoidist,List<String> cvoidlist,
		  Map<String,CreditRecordSignCVO> Localcvo,
		  Map<String,CreditRecordSign> Localhvo 
		  ) throws Exception;
  /**
   * 比较HVO
   * @param Sourcehvo 从接口处来的源HVO
   * @param Localhvo 从学分中取的本地HVO
   * @param Addhvo HVO新增
   * @param Updatehvo HVO修改
   * @param Localhvo  本次有变更的学员id ，key为studentid
   */
  public void ProcessHVO(
		  Map<String,CreditRecordSign> Sourcehvo,
		  Map<String,CreditRecordSign> Localhvo ,
		  Map<String,CreditRecordSign> Addhvo, 
		  Map<String,CreditRecordSign> Updatehvo,
		  Map<String,String> studentid
		  );
 public void ProcessCVO(
		 Map<String,CreditRecordSignCVO> Sourcecvo,
		 Map<String,CreditRecordSignCVO> Localcvo,
		 Map<String,CreditRecordSignCVO> Addcvo,
		 Map<String,CreditRecordSignCVO> Updatecvo
		  );
  /**
   * 持久化本次数据
   * @param session
   * @param cvo
   * @param hvo
   * @param studentidlist
   */
  public void  PersistenceVO(Session session,
		  Map<String,CreditRecordSign> Addhvo, 
		  Map<String,CreditRecordSign> Updatehvo,
		  Map<String,CreditRecordSignCVO> Addcvo,
		  Map<String,CreditRecordSignCVO> Updatecvo

		  );
  /**
   * 变更学员档案
   * @param studentid
   */
  public void ComputeCreditRecodeBystudntid(Session session,Map<String,String> studentid) ;
  
}
