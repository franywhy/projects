package com.izhubo.credit.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

 







import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditStandard;
 

/**
 * 学分中学分档案运算接口
 * <p>说明</p>
 * 1.取得当前userid对应的订单全部列表CVO
 * 2.取得当前档案列表,以userid+subject_type为key,value为cvo组成localmap，关联订单 判断是否有些已经失效
 * 3.分析1 以userid+subject_type为key,value为cvo组成Newmap,过滤相同KEY的 取最旧报名的
 * 4.for 当前档案列表localmap，value为空的 从newmap中取数，取不到的则判断这个学分档案失效，进入update 列表
 * 5.for newmap,在localmap中取数，取不到的为新加入的科目，进入add 列表。
 * 6.将上面的add列表和update 列表存入到数据库中。
 * @author lintf 
 *
 */
public interface CreditForRecordCheckService {
	 /**
	  * 根据学员userid列表取得学分制中的全部订单信息CVO ,只取enable=0的
	  * @param studentidlist
	  * @return
	  */
 public List<CreditRecordSignCVO>  getCreditCVOByStudentidlist(Session session,List<String> studentidlist) ;
 /**
  * 根据Studentid取得没有CVO的HVO 并删除这些HVO
  * 业务规则:1.先根据userid取得全部的HVO，2.遍历全部的CVO，取得enable=0的CVO的HVO 3.将HVO中没有CVO的 删除
  * @param session
  * @param studentidlist
  * @return
  */
 public  void getDelSingHVO(Session session,List<String> studentidlist,List<CreditRecordSignCVO> localcvo);

 /**
  * 根据全部enable=0的CVO 组装成档案单模板DVO,key:userid+subject_type
  * @param session
  * @param cvolists 根据全部enable=0的CVO
  * @param dvoSchme
  */
 public void getRecordSchme(Session session,List<CreditRecordSignCVO> cvolists,Map<String,String> dvoSchme);
 /**
  *  根据学员订单拼成当前档案map
  * @param cvolists 订单CVO
  * @param Newmap 以全部订单拼成的档案map
  */
 
 public void ComputerCVO(List<CreditRecordSignCVO> cvolists,
		 Map<String,CreditRecordSignCVO> Newmap);
 /**
  * 根据全部学员userid取得当前库中的档案map
  * @param studentidlist
  * @param newlist 本地是
  * @param Localmap 当前库中存的档案map
  */
 public  void getLocalRecordByStudentidlist(
		 Session session,List<String> studentidlist,
		 List<CreditRecordSignCVO> newlist,
		 Map<String,CreditRecordSignCVO> typemap,
		 Map<String,CreditRecord> Localmap );
 /**
  * 比较当前库和新档案 分别存到update 列表和add列表
  * @param Newmap 当前订单按科目拼成的档案
  * @param Addlist 档案增加列表
  * @param Localmap 本地档案
  * @param Updatelist 档案更新列表
  * @param creditstandard 当前库中的学分科目列表
  */
 public void ComputerRecordCheck( 
		 Map<String,CreditRecord> Addlist, 
		 Map<String,CreditRecord> Updatelist,
		 Map<String,String> dvoSchme,
		 Map<String,CreditRecordSignCVO> typemap,
		 Map<String, CreditStandard> creditstandard
			);
 /**
  * 保存update列表和add列表
  * @param session
  * @param Addlist
  * @param Updatelist
  */
 public void SaveRecord (Session session, 
		 Map<String,CreditRecord> Addlist,
		 Map<String,CreditRecord> Updatelist);
 /**
  * 删除没有档案单的HVO </p>
  * 1.先取得全部的HVO </p>
  * 2.遍历全部的addlist和 updatelist 取得enable=0的HVO</p>
  * 3.对比1和2将没有HVO的1的enable=1</p>
  * @param session 
  * @param studentidlist 全部学员的userid
  * @param Addlist 要增加的学员
  * @param Updatelist 要修改的学员 
  */
 public void DelCreditRecordSignforNullRecord(Session session, 
		 List<String> studentidlist,
		 Map<String,CreditRecord> Addlist,
		 Map<String,CreditRecord> Updatelist);
 
}

