package com.izhubo.web.qqbase;

import javax.servlet.http.HttpSession;

import com.izhubo.userSystem.mongo.qquser.QQUser;

public class QQUserInSession {

	
	private String openId;
	private String openKey;
	private String pf;
	private QQUser qqUser;
	
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getOpenKey() {
		return openKey;
	}
	public void setOpenKey(String openKey) {
		this.openKey = openKey;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	
	
	public QQUser getQqUser() {
		return qqUser;
	}
	
	public void setQqUser(QQUser qqUser) {
		this.qqUser = qqUser;
	}
	
	
	private static final String Key = "QQUserInSession_";
	public static QQUserInSession getUserInSession(HttpSession session, String openId, String openKey){
		QQUserInSession qqUserInSession = null;
		
		if ((qqUserInSession = (QQUserInSession)session.getAttribute(Key + openId)) != null
				&& qqUserInSession.openKey.equals(openKey)) {
			return qqUserInSession;
			
		}
		return qqUserInSession;
	}
	
	public void putUserInSession(HttpSession session){
		session.setAttribute(Key + openId, this);
	}
}
