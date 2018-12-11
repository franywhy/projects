package io.renren.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import io.renren.utils.NumberUtils;

/**
 * 学员档案基础信息
 * @author lintf
 *
 */
public class RecordInfoEntity {
	/**
	 * 学员档案主键
	 */
	private Long recordId;
	/**
	 * 用户id
	 * 
	 */
	private Long userId;
	
	
	/**
	 * 学员名称
	 */
	private String name;
	/**
	 * 性别
	 */
	private  Integer sex;
	/**
	 * 身份证
	 */
	private String idCard;
	/**
	 * 年龄
	 */
	private Integer age;
	
	
	private String ageStr;
	/**
	 * 学历
	 */
	private String record;
	/**
	 *  电话号码
	 */
	private String mobile;
	private  String qq;
	/**
	 * 是否已婚
	 */
	private Integer marriageStatus;
	/**
	 * 是否生育
	 */
	private Integer fertilityStatus;
	/**
	 * 每天学习时间
	 */
	private Integer studyTimeOfDay;
	/**
	 * 现在岗位
	 */
	private String postName;
	private String accountingCertificates;
	/**
	 * NC中学员id
	 */
	private String ncStudentId;
	private Date createTime;
	private Date ts;
	private Long syncTime;
	/**
	 * 是否从NC过来的 (true 是)
	 * 
	 */
	private boolean inNCSync;
	
 
	//修改时间
	private Date modifyTime;
	//创建人
	private Long createPerson;
	//创建人名
	private String createdName;
	//修改人
	private Long modifyPerson;
	//修改人名
	private String modifiedName;
	
	
	
	

	/**
	 * 空白的构造
	 */
	public RecordInfoEntity() {
		 
	}
	/**
	 * 从队列信息构造学员档案
	 * @param message
	 */
	public RecordInfoEntity(OrderMessageConsumerEntity order,MallOrderEntity m) {
		this.inNCSync=true;
		this.studyTimeOfDay=0;
		this.fertilityStatus=0;
		this.marriageStatus=0;
		this.userId=m.getUserId();
		if (order!=null) {
			this.name=order.getUser_name();
			 this.ncStudentId=order.getNc_user_id();
			  this.mobile=order.getPhone();
			 this.record=order.getRecord();
			 this.sex=order.getSex();
			 this.qq=order.getQq();
			 this.idCard=order.getidCard();
		}
		  
		 
				 
		 
	}
	
	
	
	
	/**
	 * 根据map转成bean,由于用json转的 总是有一些格式不对 所以改为直接手动指定的
	 * @param map
	 */
	public RecordInfoEntity(Map<String, Object> map) {


		//"name","sex","idCard","mobile","age","record","qq","marriageStatus","fertilityStatus","postName","accountingCertificates" 
		
		this.sex=2;
		this.marriageStatus=0;
		this.fertilityStatus=0;
		
		
		if (map.get("name")!=null) {this.name=map.get("name").toString();} 
	
		//0女1男2保密
	
		if (map.get("sex")!=null) {
			
			String str=map.get("sex").toString();
			if ("女".equals(str.trim())||"0".equals(str.trim())) {
				this.sex=0;
			}else if ("男".equals(str.trim())||"1".equals(str.trim())) {
				this.sex=1;
			}else {
				this.sex=2;
			}
			
		
		} 
		if (map.get("idCard")!=null) {this.idCard=map.get("idCard").toString();} 
		if (map.get("mobile")!=null) {this.mobile=map.get("mobile").toString();} 
		if (map.get("age")!=null) {this.age= NumberUtils.stringToInt(map.get("age").toString(), 0);} 
		if (map.get("record")!=null) {this.record=map.get("record").toString();} 
		if (map.get("qq")!=null) {this.qq=map.get("qq").toString();} 
		if (map.get("marriageStatus")!=null) {this.marriageStatus=NumberUtils.stringToInt(map.get("marriageStatus").toString(), 0);} 
		if (map.get("fertilityStatus")!=null) {this.fertilityStatus=NumberUtils.stringToInt(map.get("fertilityStatus").toString(), 0);} 
		if (map.get("postName")!=null) {this.postName=map.get("postName").toString();}
		if (map.get("studyTimeOfDay")!=null) {
			try {
			this.studyTimeOfDay=NumberUtils.stringToInt(map.get("studyTimeOfDay").toString(), 0);
			}catch(Exception es) {
				this.studyTimeOfDay=0;
			}
		}
		
		if (map.get("accountingCertificates")!=null) {this.accountingCertificates=map.get("accountingCertificates").toString();} 
		
			 
	 	
		
	}
	
	
	/**
	 * 是否从NC过来的 (true 是)
	 *@return
	 * @author lintf
	 * 2018年8月24日
	 */
	public boolean isInNCSync() {
		return inNCSync;
	}
	/**
	 * 是否从NC过来的 (true 是)
	 *@return
	 * @author lintf
	 * 2018年8月24日
	 */
	public void setInNCSync(boolean inNCSync) {
		this.inNCSync = inNCSync;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Long getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	public String getCreatedName() {
		return createdName;
	}
	public void setCreatedName(String createdName) {
		this.createdName = createdName;
	}
	public Long getModifyPerson() {
		return modifyPerson;
	}
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	public String getModifiedName() {
		return modifiedName;
	}
	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Integer getAge() {
		if(age==null) {
			try {
			return Integer.parseInt( this.getAgeStr());
			}catch(Exception es) {
				
			}
		}
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	 
	
	
	public Integer getMarriageStatus() {
		return marriageStatus;
	}
	public void setMarriageStatus(Integer marriageStatus) {
		this.marriageStatus = marriageStatus;
	}
	public Integer getFertilityStatus() {
		return fertilityStatus;
	}
	public void setFertilityStatus(Integer fertilityStatus) {
		this.fertilityStatus = fertilityStatus;
	}
	public Integer getStudyTimeOfDay() {
		return studyTimeOfDay;
	}
	public void setStudyTimeOfDay(Integer studyTimeOfDay) {
		this.studyTimeOfDay = studyTimeOfDay;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getAccountingCertificates() {
		return accountingCertificates;
	}
	public void setAccountingCertificates(String accountingCertificates) {
		this.accountingCertificates = accountingCertificates;
	}
	public String getNcStudentId() {
		return ncStudentId;
	}
	public void setNcStudentId(String ncStudentId) {
		this.ncStudentId = ncStudentId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public   void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public Long getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(Long syncTime) {
		this.syncTime = syncTime;
	}
	
	
	
	
	
	
	public String getAgeStr() {
		if (this.age!=null) {
			return age+"";
		}else if (this.idCard!=null) {
			return this.getAgeByCertId(this.idCard);
		}
		return "";
		
		 
	}
	public void setAgeStr(String ageStr) {
		this.ageStr = ageStr;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 *  将新的信息赋给旧的档案 
	 *@param old_r 旧档案
	 *@param new_r 新档案
	 * @author lintf
	 * 2018年7月30日
	 */
	public void infoUpdate(RecordInfoEntity old_r, RecordInfoEntity new_r) {
		
		if (new_r.getUserId()!=null&&new_r.getUserId().longValue()>0) {
			old_r.setUserId(new_r.getUserId());
		}
		//最后修改时间会修改
		old_r.setModifyTime( new_r.getModifyTime()==null?new Date():new_r.getModifyTime());
		
		
	//	old_r.setName(new_r.getName());
	//	old_r.setMobile(new_r.getMobile());
		old_r.setSex(new_r.getSex());
		old_r.setIdCard(new_r.getIdCard());
		old_r.setRecord(new_r.getRecord());
		if (!new_r.inNCSync) { //当不是从NC过来的 才更新这些  因为有些数据不是NC中的值 如果直接更新的话会数据不对

			 if (new_r.getPostName()!=null) {
			old_r.setPostName(new_r.getPostName());
			 }
			 if (new_r.getMarriageStatus()!=null) {
		old_r.setMarriageStatus(new_r.getMarriageStatus());
			 }
			 if (new_r.getFertilityStatus()!=null) {
				old_r.setFertilityStatus(new_r.getFertilityStatus());
			 } 
			 if (new_r.getAccountingCertificates()!=null&&new_r.getAccountingCertificates().trim().length()>0) {
		old_r.setAccountingCertificates(new_r.getAccountingCertificates());
			 }
			 if (new_r.getStudyTimeOfDay()!=null&&new_r.getStudyTimeOfDay()  >=0) {
					old_r.setStudyTimeOfDay(new_r.getStudyTimeOfDay());
						 }
			 
			 
		}
		
		old_r.setQq(new_r.getQq());
	}
	 
	/**
     * 根据身份证号获取年龄
     * @param certId
     * @return
     */
    public  String getAgeByCertId(String certId) {
    	try {
        String birthday = "";
        if (certId.length() == 18) {
           birthday = certId.substring(6, 10) + "/"
                   + certId.substring(10, 12) + "/"
                   + certId.substring(12, 14);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
        Date now = new Date();
        Date birth = new Date();
  
            birth = sdf.parse(birthday);
         
        long intervalMilli = now.getTime() - birth.getTime();
        int age = (int) (intervalMilli/(24 * 60 * 60 * 1000))/365;
 
        return age +"";
    	}catch (Exception es) {
    		return " ";
    	}
    }
	
	
	
	

}
