package com.izhubo.credit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;

import com.google.gson.reflect.TypeToken;
 
import com.izhubo.credit.service.CreditForNcSYNCService; 
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.vo.HttpServiceResult;
import com.izhubo.credit.vo.RegistrationSYNCVO;
import com.izhubo.credit.vo.TiKuScoreVO;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditStandard;
 
 

public class CreditForNcSYNCServiceImpl implements CreditForNcSYNCService {

	@Override
	public List<RegistrationSYNCVO> getRegistrationSYNCVO(String SYSDATE,String SYEDATE,
			String secretkey) {

	 
		 
				String result;
				try {
	 					
					result = HttpRequest.sendPost( NcSyncConstant.getRegistrationSYNCUrl(), "syncSDate="+SYSDATE+"&syncEDate="+SYEDATE+"&secretKey="+secretkey,null);
				} catch (Exception e) {
					return null;
				}
				HttpServiceResult s = JsonUtil.fromJson(result, new TypeToken<HttpServiceResult>() {}.getType());
				 
				List<RegistrationSYNCVO> voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<RegistrationSYNCVO>>() {}.getType());
 	 
 	
		         return voList;
		
		
	}

	
/**
* 在取数据时会进入逻辑判断 </p>
* 学分单据 CreditRecordSign的enable 为是否启动 1 为不启动 0 为启动
* 每次同步报名表时都要做一下判断 
*当报名表H表 中的</p>
*if(同步的科目不在学分的科目列表时 报名表) Dr=1
*if(registstatic in (4,5,6) or vbillstatus!=1 or kctype in (2,13) or dr=1){enable =1 } *
* else if  (isfullpay=Y or isPaike=Y) {   enable=0}
*
*</p>
*子表中的enable源自报名表c表中的def5 [不同步到学分制]
*当H表为enable =1 时 子表的也会为enable也为1
*子表中 dr=1 ,时enable=1 

* 		
*/
	@Override
	public void ComputeSourcedate(List<RegistrationSYNCVO> sourcedate,
			Map<String, CreditRecordSignCVO> Sourcecvo,
			Map<String, CreditRecordSign> Sourcehvo, List<String> hvoidist,
			List<String> cvoidlist,Map<String,CreditStandard> creditStandardmap,Map<String,String> notcheckmap) {

int c_enable=1;//是否启动  1 为不启用，0为启用
int h_enable=1;//h表是否启用 1为不启用，0为启用

boolean isdr =false;//是否删除，当报名表的班型的类型是2，13时 标志这个报名表为删除状态
boolean isnotuse =false;//是否在学分中有这个科目 ，当这个科目是学分中没有的 也标志报名表为删除状态 子表也为删除状态
String subject_type=null;
for (int i=0;i<sourcedate.size();i++){
	isdr=false;
	isnotuse=false;
	subject_type=null;
	c_enable=1;
	h_enable=1;
	RegistrationSYNCVO  sync= sourcedate.get(i);
	CreditRecordSign h = Sourcehvo.get(sync.getSIGN_ID());
	//判断当前科目是否是学分中的科目 如果 不是的话则isnotuse 为true;
	CreditStandard st = creditStandardmap.get(sync.getNC_SUBJECT_ID());
	if (st==null){
		isnotuse=true; 
	}else {
		isnotuse=false;//如果是有这个科目的 则为false 且取得科目类型 科目类型没有的 则取为科目
		 
			subject_type=st.getSubject_type()==null?st.getNc_id():st.getSubject_type();
		 
	}
	

	String kctype=sync.getKCTYPE()==null?"2":sync.getKCTYPE();//班型类型
	int registstatic=sync.getREGISTSTATUS()==null?4:sync.getREGISTSTATUS(); //报名表状态
	int vbillstatus=sync.getVBILLSTATUS()==null?0:sync.getVBILLSTATUS();//报名表单据状态
	int rh_dr=sync.getRH_DR()==null?1:sync.getRH_DR(); //报名表是否删除 1为删除，0为正常
	String isfullpay=sync.getFULLPAY()==null?"N":sync.getFULLPAY();//是否交齐款
	String ispaike=sync.getIS_PAIKE()==null?"N":sync.getIS_PAIKE();//是否已经排课
	//先判断h_enable 是否启用
	if (kctype.equals("2")||kctype.equals("13")){
		h_enable=1;		
	}else if (vbillstatus!=1||registstatic==4||registstatic==5||registstatic==6||rh_dr!=0){
		h_enable=1;	
	}else if (isfullpay.equals("Y")||ispaike.equals("Y")){
		h_enable=0;
	}
	//h_enable和c_enable的判断是一样的，区别在于 当Cvo中的科目不在学分制中的话 那么就不启用CVO并且
	if (h_enable==1||isnotuse){
		c_enable=1;
	}else {
		c_enable=h_enable;
	}
	
	
	
	if (h==null){
		/**
		 * 从接口处过来的转成hvo的转换已经调整到实体类构造了 
		 */
		CreditRecordSign newhvo= new CreditRecordSign(sync);
		newhvo.setIsEnable(h_enable);
		
		/*if (h_enable==1) {
			newhvo.setDR(1);
			
		}
*/
 
  
  
		Sourcehvo.put(sync.getSIGN_ID(), newhvo);
	 
		
		hvoidist.add(sync.getSIGN_ID());
		
		//如果来源中已经有这个学员的 判断一下enable是1且本次的是0 那才会更新
	}else if (h.getIsEnable()!=null&&h.getIsEnable()==1&&h_enable==0) { 
		h.setIsEnable(0);
		h.setDR(0);
		Sourcehvo.put(sync.getSIGN_ID(),h);
	}
	
	
	
	
	CreditRecordSignCVO c = Sourcecvo.get(sync.getNC_REG_C());
	if (c==null){
		/**
		 * 从接口处过来的转成Cvo的转换已经调整到实体类构造了 
		 */
		CreditRecordSignCVO newcvo= new CreditRecordSignCVO(sync);
		newcvo.setSubjectType(subject_type);
		if (notcheckmap.get(sync.getCLASS_ID()+"_"+subject_type)!=null){
			newcvo.setIsCheck(1);
		}else {
			newcvo.setIsCheck(0);
		}
		
		
		
		
	/**
	 * 当enable=1 时则说明报名表不属于学分的，那么对应的科目也就不是学分 
	 */
		newcvo.setIsEnable(sync.getENABLE());
		if (c_enable==1||sync.getENABLE()==1|| sync.getRC_DR()==1){
			newcvo.setIsEnable(1);
			newcvo.setDR(1);
		}
		
		Sourcecvo.put(sync.getNC_REG_C(), newcvo);
		cvoidlist.add(sync.getNC_REG_C());
		
	}

	
}
 
		 
	}

	@Override
	public void getLocaldateforNC(Session session, List<String> studentidlist,
			List<String> hvoidist, List<String> cvoidlist,
			Map<String, CreditRecordSignCVO> Localcvo,
			Map<String, CreditRecordSign> Localhvo) throws Exception{
		//先以hvolist和cvolist 取得学分数据库的数据
		
		if (cvoidlist!=null&&cvoidlist.size()>0){
			 List<CreditRecordSignCVO>  cvolist= CreditDaoUtil.getLocalDateSign_c(session, cvoidlist,1000);
					 
				//	 session.createQuery("from CreditRecordSignCVO where  SIGN_ID_C in (:alist) ").setParameterList("alist",cvoidlist).list();;

			  /**
			  * 开始将list中的CVO转成map形式
			  */
			  if (cvolist!=null&&cvolist.size()>0){
				  
				  for (int i=0;i<cvolist.size();i++){
					  CreditRecordSignCVO cvo=cvolist.get(i);
					  CreditRecordSignCVO subc=Localcvo.get(cvo.getSignId());
					  if (subc==null){//当从学分库中取出来的c表没有存在本次的c表中时，存到本地的c表中
						  Localcvo.put(cvo.getSignId(), cvo);
					  }else {//如果本地中已经有这个学员了 这种情况是由于存了两个相同CID,取最新的一个CID的
						  if(subc.getSYTS().before(cvo.getSYTS())){
							  
							  Localcvo.put(cvo.getSignId(), cvo);
						  }
						  
					  }
				  }
				  
			 
		}
		if (hvoidist!=null&&hvoidist.size()>0){
			  List<CreditRecordSign>  hvolist= CreditDaoUtil.getLocalDateSign_h(session, hvoidist, 1000);
				//	  session.createQuery("from CreditRecordSign where  signId in (:alist) ").setParameterList("alist",hvoidist).list();;
			  /**
				  * 开始将list中的hVO转成map形式
				  */
			  if (hvolist!=null&&hvolist.size()>0){
				  for (int i=0;i<hvolist.size();i++){
					  CreditRecordSign hvo=hvolist.get(i);
					  CreditRecordSign subh=Localhvo.get(hvo.getSignId());
					  if (subh==null){//当从学分库中取出来的h表没有存在本次的h表中时，存到本地的h表中
						  Localhvo.put(hvo.getSignId(), hvo);
					  }else {//如果本地中已经有这个学员了 这种情况是由于存了两个相同hID,取最新的一个hID的
						  if(subh.getSYTS().before(hvo.getSYTS())){
							  
							  Localhvo.put(hvo.getSignId(), hvo);
						  }
						  
					  }
				  }
			  }
		}
		}
		 
		
				 
	 
	 
		  
		  
	
		  
		  
		  
		  
		  
	}

	@Override
	public void ProcessHVO(Map<String, CreditRecordSign> Sourcehvo,
			Map<String, CreditRecordSign> Localhvo,
			Map<String, CreditRecordSign> Addhvo,
			Map<String, CreditRecordSign> Updatehvo,
			Map<String, String> studentid) {
		 
		for (Entry<String, CreditRecordSign> entry : Sourcehvo.entrySet()) {
			CreditRecordSign hvo=Localhvo.get(entry.getKey());//hvo为学分中的
			if (hvo!=null){
				
				
				if(entry.getValue().getSYTS()==null||hvo.getSYTS()==null){ //当来源的比学分中的还要后的 更新学分中的
					entry.getValue().setId(hvo.getId());						
					Updatehvo.put(entry.getKey(), entry.getValue());	
					studentid.put(entry.getValue().getStudentId(),entry.getValue().getSYTS().toString() );
				}else  if(entry.getValue().getSYTS().after(hvo.getSYTS())){ //当来源的比学分中的还要后的 更新学分中的
					entry.getValue().setId(hvo.getId());						
					Updatehvo.put(entry.getKey(), entry.getValue());	
					studentid.put(entry.getValue().getStudentId(),entry.getValue().getSYTS().toString() );
				}else if (entry.getValue().getIsEnable()!=hvo.getIsEnable()){
					entry.getValue().setId(hvo.getId());						
					Updatehvo.put(entry.getKey(), entry.getValue());	
					studentid.put(entry.getValue().getStudentId(),entry.getValue().getSYTS().toString() );
		
				}
			}else { //如果学分中没有存在的判断是否enable为0 是的话 才会加到列表中
				 
				
				if (entry.getValue().getDR()!=null&&entry.getValue().getIsEnable()==0){
				Addhvo.put(entry.getKey(), entry.getValue());
				studentid.put(entry.getValue().getStudentId(),entry.getValue().getSYTS().toString() );
			//	System.out.println(entry.getValue().getSignCode()+"的"+entry.getValue().getDR()+" 所以存");
				}else {
			//		System.out.println(entry.getValue().getSignCode()+"的"+entry.getValue().getDR()+" 所以不存");
				}
			}
			
		}
		
		
		
		
	}

	@Override
	public void ProcessCVO(Map<String, CreditRecordSignCVO> Sourcecvo,
			Map<String, CreditRecordSignCVO> Localcvo,
			Map<String, CreditRecordSignCVO> Addcvo,
			Map<String, CreditRecordSignCVO> Updatecvo) {
		for (Entry<String, CreditRecordSignCVO> entry : Sourcecvo.entrySet()) {
			CreditRecordSignCVO cvo=Localcvo.get(entry.getKey());//hvo为学分中的
			if (cvo!=null){
				if (entry.getValue().getSYTS()==null||cvo.getSYTS()==null){
					entry.getValue().setId(cvo.getId());					
					Updatecvo.put(entry.getKey(), entry.getValue());
				}else if(entry.getValue().getSYTS().after(cvo.getSYTS())){ //当来源的比学分中的还要后的 更新学分中的
					entry.getValue().setId(cvo.getId());					
					Updatecvo.put(entry.getKey(), entry.getValue());	
				}
			}else { //如果学分中没有存在的 enable为0的 直接加到增加列表中 
				
				
				if (entry.getValue().getDR()!=null&&entry.getValue().getIsEnable()==0){
				Addcvo.put(entry.getKey(), entry.getValue());
				}
			}
			
		}
			
	}

	@Override
	public void PersistenceVO(Session session,
			Map<String, CreditRecordSign> Addhvo,
			Map<String, CreditRecordSign> Updatehvo,
			Map<String, CreditRecordSignCVO> Addcvo,
			Map<String, CreditRecordSignCVO> Updatecvo) {
		//插入HVO
		
		 
		
		
		if (Addhvo!=null&&Addhvo.size()>0){
		for (Entry<String, CreditRecordSign> entry : Addhvo.entrySet()) {
			CreditRecordSign hvo= entry.getValue();
		     session.save(hvo);		}
		}
		
		//更新HVO
		if (Updatehvo!=null&&Updatehvo.size()>0){
        for (Entry<String, CreditRecordSign> entry : Updatehvo.entrySet()) {
        	CreditRecordSign hvo= entry.getValue();
        	 
			hvo=(CreditRecordSign) session.merge(hvo);
             session.update(hvo);
			 
        		 
        		 
        	
		}
		}
		
		if (Addcvo!=null&&Addcvo.size()>0){
			for (Entry<String, CreditRecordSignCVO> entry : Addcvo.entrySet()) {
				CreditRecordSignCVO cvo= entry.getValue();
				session.save(cvo);
			}
			}
			//更新HVO
			if (Updatecvo!=null&&Updatecvo.size()>0){
	        for (Entry<String, CreditRecordSignCVO> entry : Updatecvo.entrySet()) {
	        	CreditRecordSignCVO cvo= entry.getValue();
	        	 session.merge(cvo);
			}
			}
		
		
		
	}

	@Override
	public void ComputeCreditRecodeBystudntid(Session session,
			Map<String, String> studentid) {
		// TODO Auto-generated method stub
		
	}

	 
}
