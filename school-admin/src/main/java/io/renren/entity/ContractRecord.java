package io.renren.entity;

import java.util.Date;
import java.util.List;

public class ContractRecord {

    private Long id;//pk
    private Long orderId;//订单pk
    private Long userId;//学员pk
    private String realName;//学员姓名
    private String idCard;//学员身份证
    private String mobile;//手机
     private int status;//状态(0-未签订 1-已签订 2-已驳回)
    private Long contractId;//在线协议id
    private Long templateId;//模板id
    private Long signerId;//在线用户pk
    private String teacherName;//课程顾问老师
    private String ncId;//订单主键
    private int sex;//性别
    private String bdyx;//报读院校
    private String companyName;//签协议的公司名称
    private int companyId;//在线协议的公司id

    private String record;//学历
    private String zy;//专业
    private String qq;//QQ
    private String emergencyPro;//紧急联系人
    private String emergencyCall;//紧急联系电话
    private String provinceName;//报考省份
    private String vbillCode;//报名表号
    private String className;//班级名称
    private String courseName;//班型名称
    private String payName;//支付方式
    private String regdate;//报读日期
    private int dr;//是否删除0 为未删除, 1 为删除
    private Date createTime;
    private Date ts;
    private Double regMoney;//报名表金额

    private int idcardLocation;
    private int mobileLocation;
    //产品线PK
    private Long productId;
    private List<ContractDetail> contractDetail;
    /**
     * NC中的Ts
     */
    private long syncTime;

    public ContractRecord() {
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public int getCompanyId() {
        return companyId;
    }


    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }


    public long getSyncTime() {
        return syncTime;
    }


    public void setSyncTime(long syncTime) {
        this.syncTime = syncTime;
    }


    public ContractRecord getContractRecord(OrderMessageConsumerEntity or, MallOrderEntity mallOrderEntity, Long tempid) {
        ContractRecord cd = new ContractRecord();
        if (or.getContractHead() != null) {
            cd = or.getContractHead();
            cd.setProductId(mallOrderEntity.getProductId());
            cd.setOrderId(mallOrderEntity.getOrderId());
            cd.setUserId(mallOrderEntity.getUserId());
            cd.setTemplateId(tempid);
            cd.setCompanyName(or.getContractCompanyName());
        }

        if (or.getContractBody() != null) {
            cd.setContractDetail(or.getContractBody());
        }
        return cd;

    }

    public void setNew(ContractRecord oldOne, ContractRecord newOne) {

        oldOne.setBdyx(newOne.getBdyx());
        oldOne.setClassName(newOne.getClassName());
        //	oldOne.setContractDetail(newOne.getContractDetail());
        oldOne.setContractId(newOne.getContractId());
        oldOne.setCourseName(newOne.getCourseName());
        oldOne.setCreateTime(newOne.getCreateTime());
        oldOne.setDr(newOne.getDr());
        oldOne.setEmergencyCall(newOne.getEmergencyCall());
        oldOne.setEmergencyPro(newOne.getEmergencyPro());
        //oldOne.setId(newOne.getId());
        oldOne.setIdCard(newOne.getIdCard());
        //	oldOne.setIdcardLocation(newOne.getIdcardLocation());
        oldOne.setMobile(newOne.getMobile());
        //oldOne.setMobileLocation(newOne.getMobileLocation());
        //oldOne.setNcId(newOne.getNcId());

        //	oldOne.setOrderId(newOne.getOrderId());
        oldOne.setPayName(newOne.getPayName());
        //	oldOne.setProductId(newOne.getProductId());
        oldOne.setProvinceName(newOne.getProvinceName());
        oldOne.setQq(newOne.getQq());
        oldOne.setRealName(newOne.getRealName());
        oldOne.setRecord(newOne.getRecord());
        oldOne.setRegdate(newOne.getRegdate());
        oldOne.setRegMoney(newOne.getRegMoney());
        oldOne.setSex(newOne.getSex());
        //	oldOne.setSignerId(newOne.getSignerId());
        //	oldOne.setStatus(newOne.getStatus());
        oldOne.setSyncTime(newOne.getSyncTime());
        oldOne.setTeacherName(newOne.getTeacherName());
        //	oldOne.setTemplateId(newOne.getTemplateId());
        //	oldOne.setTs(newOne.getTs());
        //	oldOne.setUserId(newOne.getUserId());
        oldOne.setVbillCode(newOne.getVbillCode());
        oldOne.setZy(newOne.getZy());

        oldOne.setCompanyId(newOne.getCompanyId());
        oldOne.setDr(newOne.getDr());


    }


    public String getRecord() {
        return record;
    }


    public void setRecord(String record) {
        this.record = record;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }


    public Double getRegMoney() {
        return regMoney;
    }

    public void setRegMoney(Double regMoney) {
        this.regMoney = regMoney;
    }

    public List<ContractDetail> getContractDetail() {
        return contractDetail;
    }

    public void setContractDetail(List<ContractDetail> contractDetail) {
        this.contractDetail = contractDetail;
    }

    public String getNcId() {
        return ncId;
    }

    public void setNcId(String ncId) {
        this.ncId = ncId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getIdcardLocation() {
        return idcardLocation;
    }

    public void setIdcardLocation(int idcardLocation) {
        this.idcardLocation = idcardLocation;
    }

    public int getMobileLocation() {
        return mobileLocation;
    }

    public void setMobileLocation(int mobileLocation) {
        this.mobileLocation = mobileLocation;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getDr() {
        return dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getStatus() {
        return status;

    }

    public void setStatus(int status) {
        this.status = status;

    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getSignerId() {
        return signerId;
    }

    public void setSignerId(Long signerId) {
        this.signerId = signerId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBdyx() {
        return bdyx;
    }

    public void setBdyx(String bdyx) {
        this.bdyx = bdyx;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmergencyPro() {
        return emergencyPro;
    }

    public void setEmergencyPro(String emergencyPro) {
        this.emergencyPro = emergencyPro;
    }

    public String getEmergencyCall() {
        return emergencyCall;
    }

    public void setEmergencyCall(String emergencyCall) {
        this.emergencyCall = emergencyCall;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getVbillCode() {
        return vbillCode;
    }

    public void setVbillCode(String vbillCode) {
        this.vbillCode = vbillCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }


    @Override
    public String toString() {
        return "ContractRecord [id=" + id + ", orderId=" + orderId + ", userId=" + userId + ", realName=" + realName
                + ", idCard=" + idCard + ", mobile=" + mobile + ", status=" + status + ", contractId=" + contractId
                + ", templateId=" + templateId + ", signerId=" + signerId + ", teacherName=" + teacherName + ", ncId="
                + ncId + ", sex=" + sex + ", bdyx=" + bdyx + ", companyName=" + companyName + ", companyId=" + companyId
                + ", record=" + record + ", zy=" + zy + ", qq=" + qq + ", emergencyPro=" + emergencyPro
                + ", emergencyCall=" + emergencyCall + ", provinceName=" + provinceName + ", vbillCode=" + vbillCode
                + ", className=" + className + ", courseName=" + courseName + ", payName=" + payName + ", regdate="
                + regdate + ", dr=" + dr + ", createTime=" + createTime + ", ts=" + ts + ", regMoney=" + regMoney
                + ", idcardLocation=" + idcardLocation + ", mobileLocation=" + mobileLocation + ", productId="
                + productId + ", contractDetail=" + contractDetail + ", syncTime=" + syncTime + "]";
    }


}
