/**
 * SynchroMemberLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.izhubo.webservice;

public class SynchroMemberLocator extends org.apache.axis.client.Service implements com.izhubo.webservice.SynchroMember {

    public SynchroMemberLocator() {
    }


    public SynchroMemberLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SynchroMemberLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SynchroMemberSoap
    private java.lang.String SynchroMemberSoap_address = "http://my.kjcity.com/WebService/SynchroMember.asmx";

    public java.lang.String getSynchroMemberSoapAddress() {
        return SynchroMemberSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SynchroMemberSoapWSDDServiceName = "SynchroMemberSoap";

    public java.lang.String getSynchroMemberSoapWSDDServiceName() {
        return SynchroMemberSoapWSDDServiceName;
    }

    public void setSynchroMemberSoapWSDDServiceName(java.lang.String name) {
        SynchroMemberSoapWSDDServiceName = name;
    }

    public com.izhubo.webservice.SynchroMemberSoap getSynchroMemberSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SynchroMemberSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSynchroMemberSoap(endpoint);
    }

    public com.izhubo.webservice.SynchroMemberSoap getSynchroMemberSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.izhubo.webservice.SynchroMemberSoapStub _stub = new com.izhubo.webservice.SynchroMemberSoapStub(portAddress, this);
            _stub.setPortName(getSynchroMemberSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSynchroMemberSoapEndpointAddress(java.lang.String address) {
        SynchroMemberSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.izhubo.webservice.SynchroMemberSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.izhubo.webservice.SynchroMemberSoapStub _stub = new com.izhubo.webservice.SynchroMemberSoapStub(new java.net.URL(SynchroMemberSoap_address), this);
                _stub.setPortName(getSynchroMemberSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SynchroMemberSoap".equals(inputPortName)) {
            return getSynchroMemberSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "SynchroMember");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "SynchroMemberSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SynchroMemberSoap".equals(portName)) {
            setSynchroMemberSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}