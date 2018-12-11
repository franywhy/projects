package com.izhubo.webservice;

public class SynchroMemberSoapProxy implements com.izhubo.webservice.SynchroMemberSoap {
  private String _endpoint = null;
  private com.izhubo.webservice.SynchroMemberSoap synchroMemberSoap = null;
  
  public SynchroMemberSoapProxy() {
    _initSynchroMemberSoapProxy();
  }
  
  public SynchroMemberSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSynchroMemberSoapProxy();
  }
  
  private void _initSynchroMemberSoapProxy() {
    try {
      synchroMemberSoap = (new com.izhubo.webservice.SynchroMemberLocator()).getSynchroMemberSoap();
      if (synchroMemberSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)synchroMemberSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)synchroMemberSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (synchroMemberSoap != null)
      ((javax.xml.rpc.Stub)synchroMemberSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.izhubo.webservice.SynchroMemberSoap getSynchroMemberSoap() {
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap;
  }
  
  public com.izhubo.webservice.MemberModel getLoginName(java.lang.String loginName, java.lang.String key) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.getLoginName(loginName, key);
  }
  
  public com.izhubo.webservice.MemberModel getUserInfo(java.lang.String sessionid, java.lang.String key) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.getUserInfo(sessionid, key);
  }
  
  public com.izhubo.webservice.MemberModel getUserInfoByName(java.lang.String loginName, java.lang.String loginPwd, java.lang.String key) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.getUserInfoByName(loginName, loginPwd, key);
  }
  
  public boolean addUserLogin(java.lang.String website, java.lang.String loginName, java.lang.String sessionId, java.lang.String key) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.addUserLogin(website, loginName, sessionId, key);
  }
  
  public boolean addUser(java.lang.String website, java.lang.String loginName, java.lang.String loginPwd, java.lang.String email, int registertype, java.lang.String key, javax.xml.rpc.holders.StringHolder retCode) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.addUser(website, loginName, loginPwd, email, registertype, key, retCode);
  }
  
  public boolean addOpenUser(java.lang.String website, java.lang.String loginName, java.lang.String nickname, java.lang.String loginPwd, java.lang.String email, int registertype, java.lang.String openid, java.lang.String opentype, javax.xml.rpc.holders.StringHolder retCode) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.addOpenUser(website, loginName, nickname, loginPwd, email, registertype, openid, opentype, retCode);
  }
  
  public boolean updateUserPwd(java.lang.String website, java.lang.String loginName, java.lang.String oldPwd, java.lang.String newPwd, java.lang.String key) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.updateUserPwd(website, loginName, oldPwd, newPwd, key);
  }
  
  public boolean deleteUser(java.lang.String loginName, java.lang.String pwd) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.deleteUser(loginName, pwd);
  }
  
  public com.izhubo.webservice.MemberaccountModel updateUserPoint(java.lang.String website, java.lang.String loginName, int point, int optype, java.lang.String memo, java.lang.String key) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.updateUserPoint(website, loginName, point, optype, memo, key);
  }
  
  public com.izhubo.webservice.MemberaccountModel updateUserCash(java.lang.String website, java.lang.String loginName, java.lang.String cash, int optype, java.lang.String memo, java.lang.String number, java.lang.String key) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.updateUserCash(website, loginName, cash, optype, memo, number, key);
  }
  
  public com.izhubo.webservice.MemberinfoModel addUpdateUserInfo(java.lang.String website, java.lang.String loginName, java.lang.String mobile, java.lang.String key) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.addUpdateUserInfo(website, loginName, mobile, key);
  }
  
  
}