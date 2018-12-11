/**
 * SynchroMemberSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.usercenter.webservice;

public interface SynchroMemberSoap extends java.rmi.Remote {
    public com.usercenter.webservice.MemberModel getLoginName(java.lang.String loginName, java.lang.String key) throws java.rmi.RemoteException;
    public com.usercenter.webservice.MemberModel getUserInfo(java.lang.String sessionid, java.lang.String key) throws java.rmi.RemoteException;
    public com.usercenter.webservice.MemberModel getUserInfoByName(java.lang.String loginName, java.lang.String loginPwd, java.lang.String key) throws java.rmi.RemoteException;
    public boolean addUserLogin(java.lang.String website, java.lang.String loginName, java.lang.String sessionId, java.lang.String key) throws java.rmi.RemoteException;
    public boolean addUser(java.lang.String website, java.lang.String loginName, java.lang.String loginPwd, java.lang.String email, int registertype, java.lang.String key, javax.xml.rpc.holders.StringHolder retCode) throws java.rmi.RemoteException;
    public boolean addOpenUser(java.lang.String website, java.lang.String loginName, java.lang.String nickname, java.lang.String loginPwd, java.lang.String email, int registertype, java.lang.String openid, java.lang.String opentype, javax.xml.rpc.holders.StringHolder retCode) throws java.rmi.RemoteException;
    public boolean updateUserPwd(java.lang.String website, java.lang.String loginName, java.lang.String oldPwd, java.lang.String newPwd, java.lang.String key) throws java.rmi.RemoteException;
    public boolean deleteUser(java.lang.String loginName, java.lang.String pwd) throws java.rmi.RemoteException;
    public com.usercenter.webservice.MemberaccountModel updateUserPoint(java.lang.String website, java.lang.String loginName, int point, int optype, java.lang.String memo, java.lang.String key) throws java.rmi.RemoteException;
    public com.usercenter.webservice.MemberaccountModel updateUserCash(java.lang.String website, java.lang.String loginName, double cash, int optype, java.lang.String memo, java.lang.String key) throws java.rmi.RemoteException;
}
