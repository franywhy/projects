package org.tempuri;

public class SynchroMemberSoapProxy implements org.tempuri.SynchroMemberSoap {
  private String _endpoint = null;
  private org.tempuri.SynchroMemberSoap synchroMemberSoap = null;
  
  public SynchroMemberSoapProxy() {
    _initSynchroMemberSoapProxy();
  }
  
  public SynchroMemberSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSynchroMemberSoapProxy();
  }
  
  private void _initSynchroMemberSoapProxy() {
    try {
      synchroMemberSoap = (new org.tempuri.SynchroMemberLocator()).getSynchroMemberSoap();
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
  
  public org.tempuri.SynchroMemberSoap getSynchroMemberSoap() {
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap;
  }
  
  public boolean addUser(java.lang.String website, java.lang.String loginName, java.lang.String loginPwd, java.lang.String nc_id, int registertype, java.lang.String key, javax.xml.rpc.holders.StringHolder retCode) throws java.rmi.RemoteException{
    if (synchroMemberSoap == null)
      _initSynchroMemberSoapProxy();
    return synchroMemberSoap.addUser(website, loginName, loginPwd, nc_id, registertype, key, retCode);
  }
  
  
}