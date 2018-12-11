package com.izhubo.credit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;

import com.google.gson.reflect.TypeToken;
 
import com.izhubo.credit.service.CreditForNcSYNCService; 
import com.izhubo.credit.service.CreditForRecordCheckService;
 
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.vo.HttpServiceResult;
import com.izhubo.credit.vo.TiKuScoreVO;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO; 
import com.mysqldb.model.CreditStandard;
 

 

public class CreditForRecordCheckServiceImpl implements CreditForRecordCheckService {

	@Override
	public List<CreditRecordSignCVO> getCreditCVOByStudentidlist(Session session,
			List<String> studentidlist) {
	 
		 List<CreditRecordSignCVO>  cvolist=CreditDaoUtil.getLocalDateSign_cByUserIWhere(session, studentidlist, 10000, " and isEnable=0");  
	 		 return cvolist;
	}

	@Override
	public void ComputerCVO(List<CreditRecordSignCVO> cvolists,
			Map<String, CreditRecordSignCVO> Newmap) {
		 for(CreditRecordSignCVO cvo:cvolists){
			 if( cvo.getSubjectId()!=null){
				String subject_type= cvo.getSubjectType()==null?cvo.getSubjectId(): cvo.getSubjectType();
				  CreditRecordSignCVO  tmp= Newmap.get(cvo.getStudentId()+"_"+subject_type);
				  if (tmp!=null){//如果有重复科目的要取报名最旧的那一个
					  
					  if(cvo.getSignDate()==null||tmp.getSignDate()==null){
						  Newmap.put(cvo.getStudentId()+"_"+subject_type, cvo);
					  }else {
						 String a =cvo.getSignDate()==null?"2017-01-01":cvo.getSignDate();
						 String b =tmp.getSignDate()==null?"2017-01-01":tmp.getSignDate();
						if ( CreditUtil.Compare_date(b,a)){ //当b>a就是a在b前面时
							  Newmap.put(cvo.getStudentId()+"_"+subject_type, cvo);
						}
					  }
					  
					  
				  }else {
					  Newmap.put(cvo.getStudentId()+"_"+subject_type, cvo)  ;
				  }
			 }
		 }
		
	}

	@Override
	public void getLocalRecordByStudentidlist(
			Session session,List<String> studentidlist,
			List<CreditRecordSignCVO> newlist,
			Map<String,CreditRecordSignCVO> typemap,
			Map<String, CreditRecord> Localmap ) {
		 //  从根据USERID查到的全部订单中取出cid存在keymap中，用来后面判断某个档案是否作废了
		Map<String,CreditRecordSignCVO> keymap=new HashMap<String,CreditRecordSignCVO>();
		//科目类型+学员ID存的在typemap中
		  
		
		for (CreditRecordSignCVO cvo : newlist) {

			if (cvo != null && cvo.getStudentId() != null
					&& cvo.getIsEnable() == 0 && cvo.getSignIdc() != null) {
				keymap.put(cvo.getSignIdc(), cvo);

				String subject_type = cvo.getSubjectType() == null ? cvo
						.getSubjectId() : cvo.getSubjectType();
				CreditRecordSignCVO tmp = typemap.get(subject_type
						+ cvo.getStudentId());
				if (tmp != null) {// 如果有重复科目的要取报名最旧的那一个

					if (cvo.getSignDate() == null
							|| tmp.getSignDate() == null) {
						typemap.put(cvo.getStudentId() + subject_type, cvo);
					} else {
						String a = cvo.getSignDate() == null ? "2017-01-01"
								: cvo.getSignDate();
						String b = tmp.getSignDate() == null ? "2017-01-01"
								: tmp.getSignDate();
						if (CreditUtil.Compare_date(b, a)) { // 当b>a就是a在b前面时
							typemap.put(subject_type + cvo.getStudentId(), cvo);
						}
					}

				} else {
					typemap.put(subject_type + cvo.getStudentId(), cvo);
				}

			}

		}
		
		//这一步是为了对比现在的档案单，所以先要所全部USERID 取得全部档案单，然后先根据CID匹配 再根据subjecttype+userid匹配
		//从学分库中取得当前学员的档案信息 取得全部的档案 不管enable
		//
	 
		 List<CreditRecord>  cvolist= CreditDaoUtil.getLocalDateRecordByUserID(session, studentidlist, 10000, 3);
				 
		 //遍历当前档案单 将没有订单的档案 标示为作废enable=1
		 for (CreditRecord dvo:cvolist){
			 if (dvo.getSubjectId()!=null){
				 
				 
				 
				 String subject_type= dvo.getSubjectType()==null?dvo.getSubjectId(): dvo.getSubjectType();
			   
				 CreditRecordSignCVO inuse =keymap.get(dvo.getSignId());//从上面的keymap中取数
				 CreditRecordSignCVO intype=typemap.get(subject_type+dvo.getStudentId());
			       //档案单中的修改逻辑
				  //1.先判断CID是否存在  如果存在则赋值 则enable=0 
				  //2.当CID不存在时则判断科目类型和USEriD,存在则赋值 且启用。否 则不启用:enable=1
				 
				 
				 
				  if (inuse==null){ //cid不存在
					   if (intype==null){ //两个都不存在的 则enable=1
						  dvo.setIsEnable(1);
					   }else { //当CID不存在 但是科目和学员ID却能取得的 enable=0 且更新信息
						      dvo.setIsEnable(0);
					    	  dvo.setSignDate(intype.getSignDate());
					    	  dvo.setSignId (intype.getSignId());
					    	  dvo.setSignIdc(intype.getSignIdc());
					    	  dvo.setSubjectName(intype.getSubjectName());
					    	  dvo.setSubjectId(intype.getSubjectId());
					    	  dvo.setSubjectCode(intype.getSubjectCode());
					    	  dvo.setorgId(intype.getOrgId());
					    	  dvo.setIsCheck(intype.getIsCheck());
					    	  dvo.setClassId(intype.getClassId());
					    	  dvo.setSignCode(intype.getSignCode());
					    	  dvo.setPhone(intype.getPhone());
					    	  dvo.setIdCard(intype.getIdCard());
					    	  dvo.setStudentName(intype.getStudentName());
					   }
					   Localmap.put( subject_type+dvo.getStudentId(), dvo);
				  }else { //当CID存在时 
					  if (dvo.getIsEnable()==1){//当enable=1时说明是从无效转成有效的
						  dvo.setIsEnable(0);
						  Localmap.put(subject_type+dvo.getStudentId(), dvo);
					  }else{  
						  Localmap.put(subject_type+dvo.getStudentId(), dvo);
						   
						
					  }
					  
					  
				  }
				 
				 
				 
				 
		 
				 
				 
			 }
			 
			 
			 
			 
		 }
		 
		
		 
		 
		 
		 
	}

	@Override
	public void ComputerRecordCheck( 
			 Map<String,CreditRecord> Addlist, 
			 Map<String,CreditRecord> Updatelist,
			 Map<String,String> dvoSchme,
			 Map<String,CreditRecordSignCVO> typemap,
		     Map<String, CreditStandard> creditstandard
			
			) {
		
		 for (Entry<String, String> key:dvoSchme.entrySet()){
			 
			 if (key.getKey()!=null&&Updatelist.get(key.getKey())==null ){//在数据库中没有这个科目的档案 说明是新档案 要从typemap中取
				 
					 CreditRecordSignCVO dvo= typemap.get(key.getKey());
                     CreditStandard st = creditstandard.get(dvo.getSubjectId());
						if (dvo!=null&& st!=null){
							//根据CVO和标准学分单构建档案
							CreditRecord newd=new CreditRecord(dvo,st);
							newd.setIsEnable(0);
							newd.setAttendanceActualScore(0); 
							newd.setWorkActualScore(0); 
							newd.setExamActualScore(0);
							newd.setTotalScore(0);							
							newd.setTeacherId(null);
							newd.setClassName(null);
							newd.setCreateDate(DateUtil.DateToString( new Date()));
							newd.setCreater("admin");
							Addlist.put(key.getKey(), newd);
						}
				 
			 }
		 }
	
		 
 
		 
		 
	 
		
		
	}

	@Override
	public void SaveRecord(Session session,
			Map<String, CreditRecord> Addlist,
			Map<String, CreditRecord> Updatelist) {
		
		int innum=0;
		int updatenum=0;
		String createDate=DateUtil.DateToString(new Date());
		for (Entry<String, CreditRecord> entry:Addlist.entrySet()){
			if (entry.getValue()!=null){
				CreditRecord dvo=entry.getValue();
				if (dvo.getCreateDate()==null){					
					dvo.setCreateDate(createDate);
					dvo.setCreater("admin");
				}
				session.save (dvo);
				innum++;
			}
		}
		for (Entry<String, CreditRecord> entry:Updatelist.entrySet()){
			if (entry.getValue()!=null){
				CreditRecord dvo=entry.getValue();
				session.merge(dvo);
				updatenum++;
			}
		}
		
		
		
 
	}
/**
 * 根据USERid分别对比HVO和CVO将没有CVO的HVO 的enable=1
 * 此方法作废 已经转为下面的com.izhubo.credit.service.impl.CreditForRecordCheckServiceImpl.DelCreditRecordSignforNullRecord 
 */
	@Override
	public void getDelSingHVO(Session session, List<String> studentidlist,List<CreditRecordSignCVO> localcvo) {
	/*	Map<String,CreditRecordSign> hids= new HashMap<String,CreditRecordSign>();  //这个是根据USERID查到的全部的HVoid 
		Map<String,String> L_hids= new HashMap<String,String>();//这个是根据USERID查到的全部的CVO中的HID
	//	System.out.println("学分制运算学分档案 ");
		
		
		if (studentidlist!=null&&studentidlist.size()>0){
			  List<CreditRecordSign> hvolist= CreditUtil.getLocalDateSignHvoByUserID(session, studentidlist, 1000);
			 
			   if (hvolist!=null&&hvolist.size()>0){//当本地查到的HVO不为空时  
				   for(CreditRecordSign s:hvolist){
					   if (s!=null&&s.getSignId()!=null){
						   hids.put(s.getSignId(), s);
						   
					   }
				   }
			   }
			  
			  
			  if (localcvo!=null&&localcvo.size()>0){//当本次取得的cvo不为0时才做判断 
				  for (CreditRecordSignCVO c:localcvo){
					  if (c!=null&&c.getId()!=null&&c.getIsEnable()==0){ 
						  L_hids.put(c.getId(), "0");
					  }
				  }
				  
			  }
			   
			  int delhid=0;
			  if (hids!=null&&hids.size()>0&&L_hids!=null&&L_hids.size()>0){
				  for (Entry<String, CreditRecordSign> entry:hids.entrySet()){
					  if (entry.getValue()!=null ){
						  CreditRecordSign s=entry.getValue();
						  String temp=L_hids.get(s.getSignId());
						  if (temp==null){ //当这个HVO在CVO列表中没有找到的话 直接enable=1
							  s.setIsEnable(1);
							  session.merge (s);
							  delhid++;
						  }
						 
					  }
					  
				  }
			  }
	//	System.out.println(hids.size()+" "+L_hids.size());	  
	 	System.out.println("学分制运算学分档案 删除HVO "+delhid+" 个。");
		
		
		
		
		
		
		
		  }
 */
		
	}

@Override
public void DelCreditRecordSignforNullRecord(Session session,
		List<String> studentidlist, Map<String, CreditRecord> Addlist,
		Map<String, CreditRecord> Updatelist) {
	Map<String,CreditRecordSign> hids= new HashMap<String,CreditRecordSign>();  //这个是根据USERID查到的全部的HVoid 
	Map<String,String> L_hids= new HashMap<String,String>(); //用来存全部的update和add的全部档案中enable=0的key
	Map<String,CreditRecordSign> hvoupdate= new HashMap<String,CreditRecordSign>(); 
	if (studentidlist!=null&&studentidlist.size()>0){
		  List<CreditRecordSign> hvolist= CreditDaoUtil.getLocalDateSignHvoByUserID(session, studentidlist, 1000);
		  if (hvolist!=null&&hvolist.size()>0){//当本地查到的HVO不为空时  
			   
			  //全部hids
			  for(CreditRecordSign s:hvolist){
				   if (s!=null&&s.getSignId()!=null){
					   hids.put(s.getSignId(), s);
					   
				   }
			   }
			   
			   //当前存在的hid
			   List<String> s =
			   CreditUtil.getCreditSignHid(Addlist);
			   s.addAll( CreditUtil.getCreditSignHid(Updatelist));
			   for (String key:s){
				   L_hids.put(key, "");
				   
			   }
			   
			   //对比,假如enable=0的档案中有这个HVO的 那么这个HVO为启用状态，否则为enable=1
			   for (Entry<String,CreditRecordSign> entry:hids.entrySet()){
				   if (entry.getValue()!=null&&entry.getValue().getSignId()!=null){
					   if (L_hids.get(entry.getValue().getSignId())==null){
						      entry.getValue().setIsEnable(1);
						      hvoupdate.put(entry.getKey(), entry.getValue());//没有的 直接为1
					   }else {
						   if (entry.getValue().getIsEnable()!=0){ //不是0的 直接变为0
							   entry.getValue().setIsEnable(0);
							   hvoupdate.put(entry.getKey(), entry.getValue());
						   }
					   }
				   }
				   
			   }
			   
			   //保存数据 
			   for (Entry<String,CreditRecordSign> entry:hvoupdate.entrySet()){
				   if(entry.getValue()!=null&&entry.getValue().getSignId()!=null){
					   session.merge (entry.getValue());
				   }
			   }
			   
			   
		  
		  }
		  
		  
		  
		  
		  
		  
	} 
	
	
 
	
}

@Override
public void getRecordSchme(Session session, List<CreditRecordSignCVO> cvolists,
		Map<String, String> dvoSchme) {
	 if(cvolists!=null&&cvolists.size()>0){
		 for (CreditRecordSignCVO c:cvolists){
			 if (c!=null&&c.getStudentId()!=null){
				 String subject_type=c.getSubjectType()==null?c.getSubjectId():c.getSubjectType();
				 dvoSchme.put(subject_type+c.getStudentId(), "");
			 }
			 
		 }
	 }
	
}

	 
	 
}
