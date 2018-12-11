package com.izhubo.credit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.izhubo.credit.dao.CreditRecordDao;
import com.izhubo.credit.dao.CreditRecordSignCDao;
import com.izhubo.credit.dao.CreditRecordSignDao;
import com.izhubo.credit.service.CreditDataModefyService;
import com.izhubo.credit.util.DateUtil;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
 
 
 
 
 

 
 
@Service("creditDataModefyService")
@Transactional
public class CreditDataModefyServiceImpl implements CreditDataModefyService{
   @Resource
	CreditRecordDao creditRecordDao;
	@Resource
	CreditRecordSignDao creditRecordSignDao;
	@Resource
	CreditRecordSignCDao creditRecordSignCDao;  
	 
	@Override
	public void SetRecordInfo() {
		  System.out.println("进入了修改"+DateUtil.DateToString(new Date()));

		 List<CreditRecordSign> hvos= creditRecordSignDao.getAllEntitys();
		 Map<String,CreditRecordSign> Student_map= new HashMap<String,CreditRecordSign>(); 
		  for (CreditRecordSign s:hvos){
			  if (s!=null&&s.getStudentId()!=null&&s.getStudentName()!=null){ //不为空的话才会赋值
				  Student_map.put(s.getStudentId(), s);
			  }		  		
		  }
		 
		  List<String> studentids= new ArrayList<String>(Student_map.keySet());
		  List<String> tempid= new   ArrayList<String>();
		  int i=0;
		  for (String sub:studentids){
			  i++;
			  tempid.add(sub);
			  if (tempid.size()==5000||i==studentids.size()){
				  getStudentRecord(Student_map,tempid);
				  System.out.println(i+"/"+studentids.size()+":"+DateUtil.DateToString(new Date()));
				  tempid.clear();
			  }
		  }
		  
		  
		    System.out.println(" 修改完成"+DateUtil.DateToString(new Date()));
		
	}

	@Override
	public void getStudentRecord(Map<String,CreditRecordSign> Student_map,List<String> studentids) {
		  List<CreditRecord> r_list=  creditRecordDao.findEntitysByHQLS("from CreditRecord where studentId in (:list)", new Object[]{studentids});
		  List<CreditRecordSignCVO> c_list=  creditRecordSignCDao.findEntitysByHQLS("from CreditRecordSignCVO where studentId in (:list)", new Object[]{studentids});
		  List<CreditRecord> rr=new ArrayList<CreditRecord>();
		  List<CreditRecordSignCVO> cc= new  ArrayList<CreditRecordSignCVO>();
		  
		  
		  
		    for (CreditRecord d:r_list){
		    	CreditRecordSign s=Student_map.get(d.getStudentId());
		    	if (s!=null){
		    		d.setStudentName(s.getStudentName());
		    		d.setIdCard(s.getIdCard());
		    		d.setPhone(s.getPhone());
		    		d.setSignCode(s.getSignCode());
		    		 rr.add(d);
		    	}
		    }
		  
		    for (CreditRecordSignCVO c:c_list){
		    	CreditRecordSign s=Student_map.get(c.getStudentId());
		    	if (s!=null){
		    		c.setStudentName(s.getStudentName());
		    		c.setIdCard(s.getIdCard());
		    		c.setPhone(s.getPhone());
		    		c.setSignCode(s.getSignCode());
		    		cc.add(c);
		    	}
		    }
		    
		    if (rr!=null&&rr.size()>0){
		    	creditRecordDao.UpdateEntitBylist(rr);
		    }
		    if (cc!=null&&cc.size()>0){
		    	creditRecordSignCDao.UpdateEntitBylist(cc);
		    }
		    

	}

}
