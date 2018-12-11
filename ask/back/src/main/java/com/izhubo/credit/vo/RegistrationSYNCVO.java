package com.izhubo.credit.vo;

 
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;


/**
 * 报名表中每天同步信息专用 
 * @author lintf 
 * @date 2017年9月15日20:23:38
 *
 */
 
 
public class RegistrationSYNCVO {
	@SerializedName("kCTYPE") private String KCTYPE;
	@SerializedName("cLASS_ID") private String CLASS_ID;
	@SerializedName("cLASS_NAME") private String CLASS_NAME;
	@SerializedName("cLASS_CODE") private String CLASS_CODE;
	@SerializedName("sTUDENT_ID") private String STUDENT_ID;
	@SerializedName("pHONE") private String PHONE;
	@SerializedName("xUELI") private Integer XUELI;
	@SerializedName("sTUDENT_NAME") private String STUDENT_NAME;
	@SerializedName("iD_CARD") private String ID_CARD;
	@SerializedName("oRG_NAME") private String ORG_NAME;
	@SerializedName("oRG_CODE") private String ORG_CODE;
	@SerializedName("oRG_ID") private String ORG_ID;
	@SerializedName("nC_SUBJECT_ID") private String NC_SUBJECT_ID;
	@SerializedName("sUBJECT_NAME") private String SUBJECT_NAME;
	@SerializedName("sUBJECT_CODE") private String SUBJECT_CODE;
	@SerializedName("sIGN_CODE") private String SIGN_CODE;
	@SerializedName("sIGN_DATE") private String SIGN_DATE;
	@SerializedName("rEGISTSTATUS") private Integer REGISTSTATUS;
	@SerializedName("vBILLSTATUS") private Integer VBILLSTATUS;
	@SerializedName("rH_DR") private Integer RH_DR;
	@SerializedName("fULLPAY") private String FULLPAY;
	@SerializedName("iS_PAIKE") private String IS_PAIKE;
	@SerializedName("sIGN_ID") private String SIGN_ID;
	@SerializedName("nC_REG_C") private String NC_REG_C;
	@SerializedName("kC_STATUS") private Integer KC_STATUS;
	@SerializedName("kC_KS") private Integer KC_KS;
	@SerializedName("eNABLE") private Integer ENABLE;
	@SerializedName("rEPLACESUBJECT") private String REPLACESUBJECT;
	@SerializedName("rC_DR") private Integer RC_DR;
	@SerializedName("sYTS") private Date SYTS;

	public String getKCTYPE() {
		return KCTYPE;
	}
	public String getCLASS_ID() {
		return CLASS_ID;
	}
	public String getCLASS_NAME() {
		return CLASS_NAME;
	}
	public String getCLASS_CODE() {
		return CLASS_CODE;
	}
	public String getSTUDENT_ID() {
		return STUDENT_ID;
	}
	public String getPHONE() {
		return PHONE;
	}
	public Integer getXUELI() {
		return XUELI;
	}
	public String getSTUDENT_NAME() {
		return STUDENT_NAME;
	}
	public String getID_CARD() {
		return ID_CARD;
	}
	public String getORG_NAME() {
		return ORG_NAME;
	}
	public String getORG_CODE() {
		return ORG_CODE;
	}
	public String getORG_ID() {
		return ORG_ID;
	}
	public String getNC_SUBJECT_ID() {
		return NC_SUBJECT_ID;
	}
	public String getSUBJECT_NAME() {
		return SUBJECT_NAME;
	}
	public String getSUBJECT_CODE() {
		return SUBJECT_CODE;
	}
	public String getSIGN_CODE() {
		return SIGN_CODE;
	}
	public String getSIGN_DATE() {
		return SIGN_DATE;
	}
	public Integer getREGISTSTATUS() {
		return REGISTSTATUS;
	}
	public Integer getVBILLSTATUS() {
		return VBILLSTATUS;
	}
	public Integer getRH_DR() {
		return RH_DR;
	}
	public String getFULLPAY() {
		return FULLPAY;
	}
	public String getIS_PAIKE() {
		return IS_PAIKE;
	}
	public String getSIGN_ID() {
		return SIGN_ID;
	}
	public String getNC_REG_C() {
		return NC_REG_C;
	}
	public Integer getKC_STATUS() {
		return KC_STATUS;
	}
	public Integer getKC_KS() {
		return KC_KS;
	}
	public Integer getENABLE() {
		return ENABLE;
	}
	public String getREPLACESUBJECT() {
		return REPLACESUBJECT;
	}
	public Integer getRC_DR() {
		return RC_DR;
	}
	public Date getSYTS() {
		return SYTS;
	}
	public void setKCTYPE(String kCTYPE) {
		KCTYPE = kCTYPE;
	}
	public void setCLASS_ID(String cLASS_ID) {
		CLASS_ID = cLASS_ID;
	}
	public void setCLASS_NAME(String cLASS_NAME) {
		CLASS_NAME = cLASS_NAME;
	}
	public void setCLASS_CODE(String cLASS_CODE) {
		CLASS_CODE = cLASS_CODE;
	}
	public void setSTUDENT_ID(String sTUDENT_ID) {
		STUDENT_ID = sTUDENT_ID;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public void setXUELI(Integer xUELI) {
		XUELI = xUELI;
	}
	public void setSTUDENT_NAME(String sTUDENT_NAME) {
		STUDENT_NAME = sTUDENT_NAME;
	}
	public void setID_CARD(String iD_CARD) {
		ID_CARD = iD_CARD;
	}
	public void setORG_NAME(String oRG_NAME) {
		ORG_NAME = oRG_NAME;
	}
	public void setORG_CODE(String oRG_CODE) {
		ORG_CODE = oRG_CODE;
	}
	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}
	public void setNC_SUBJECT_ID(String nC_SUBJECT_ID) {
		NC_SUBJECT_ID = nC_SUBJECT_ID;
	}
	public void setSUBJECT_NAME(String sUBJECT_NAME) {
		SUBJECT_NAME = sUBJECT_NAME;
	}
	public void setSUBJECT_CODE(String sUBJECT_CODE) {
		SUBJECT_CODE = sUBJECT_CODE;
	}
	public void setSIGN_CODE(String sIGN_CODE) {
		SIGN_CODE = sIGN_CODE;
	}
	public void setSIGN_DATE(String sIGN_DATE) {
		SIGN_DATE = sIGN_DATE;
	}
	public void setREGISTSTATUS(Integer rEGISTSTATUS) {
		REGISTSTATUS = rEGISTSTATUS;
	}
	public void setVBILLSTATUS(Integer vBILLSTATUS) {
		VBILLSTATUS = vBILLSTATUS;
	}
	public void setRH_DR(Integer rH_DR) {
		RH_DR = rH_DR;
	}
	public void setFULLPAY(String fULLPAY) {
		FULLPAY = fULLPAY;
	}
	public void setIS_PAIKE(String iS_PAIKE) {
		IS_PAIKE = iS_PAIKE;
	}
	public void setSIGN_ID(String sIGN_ID) {
		SIGN_ID = sIGN_ID;
	}
	public void setNC_REG_C(String nC_REG_C) {
		NC_REG_C = nC_REG_C;
	}
	public void setKC_STATUS(Integer kC_STATUS) {
		KC_STATUS = kC_STATUS;
	}
	public void setKC_KS(Integer kC_KS) {
		KC_KS = kC_KS;
	}
	public void setENABLE(Integer eNABLE) {
		ENABLE = eNABLE;
	}
	public void setREPLACESUBJECT(String rEPLACESUBJECT) {
		REPLACESUBJECT = rEPLACESUBJECT;
	}
	public void setRC_DR(Integer rC_DR) {
		RC_DR = rC_DR;
	}
	public void setSYTS(Date sYTS) {
		SYTS = sYTS;
	}
	
	
}
