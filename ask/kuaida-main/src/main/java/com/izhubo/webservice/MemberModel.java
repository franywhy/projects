/**
 * MemberModel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.izhubo.webservice;

public class MemberModel  extends com.izhubo.webservice.IEntityOfmemberModel  implements java.io.Serializable {
    private int memberid;

    private java.lang.String username;

    private java.lang.String userpass;

    private java.lang.String nickname;

    private int membertype;

    private java.lang.String mobile;

    private java.lang.String email;

    private int registertype;

    private int status;

    private int channelid;

    private java.util.Calendar regdatetime;

    private java.util.Calendar updatetime;

    private int dr;

    private java.lang.String openid;

    private java.lang.String opentype;

    private java.lang.String wxopenid;

    private java.lang.String sessionid;

    private java.lang.String nc_id;

    public MemberModel() {
    }

    public MemberModel(
           int memberid,
           java.lang.String username,
           java.lang.String userpass,
           java.lang.String nickname,
           int membertype,
           java.lang.String mobile,
           java.lang.String email,
           int registertype,
           int status,
           int channelid,
           java.util.Calendar regdatetime,
           java.util.Calendar updatetime,
           int dr,
           java.lang.String openid,
           java.lang.String opentype,
           java.lang.String wxopenid,
           java.lang.String sessionid,
           java.lang.String nc_id) {
        this.memberid = memberid;
        this.username = username;
        this.userpass = userpass;
        this.nickname = nickname;
        this.membertype = membertype;
        this.mobile = mobile;
        this.email = email;
        this.registertype = registertype;
        this.status = status;
        this.channelid = channelid;
        this.regdatetime = regdatetime;
        this.updatetime = updatetime;
        this.dr = dr;
        this.openid = openid;
        this.opentype = opentype;
        this.wxopenid = wxopenid;
        this.sessionid = sessionid;
        this.nc_id = nc_id;
    }


    /**
     * Gets the memberid value for this MemberModel.
     * 
     * @return memberid
     */
    public int getMemberid() {
        return memberid;
    }


    /**
     * Sets the memberid value for this MemberModel.
     * 
     * @param memberid
     */
    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }


    /**
     * Gets the username value for this MemberModel.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this MemberModel.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the userpass value for this MemberModel.
     * 
     * @return userpass
     */
    public java.lang.String getUserpass() {
        return userpass;
    }


    /**
     * Sets the userpass value for this MemberModel.
     * 
     * @param userpass
     */
    public void setUserpass(java.lang.String userpass) {
        this.userpass = userpass;
    }


    /**
     * Gets the nickname value for this MemberModel.
     * 
     * @return nickname
     */
    public java.lang.String getNickname() {
        return nickname;
    }


    /**
     * Sets the nickname value for this MemberModel.
     * 
     * @param nickname
     */
    public void setNickname(java.lang.String nickname) {
        this.nickname = nickname;
    }


    /**
     * Gets the membertype value for this MemberModel.
     * 
     * @return membertype
     */
    public int getMembertype() {
        return membertype;
    }


    /**
     * Sets the membertype value for this MemberModel.
     * 
     * @param membertype
     */
    public void setMembertype(int membertype) {
        this.membertype = membertype;
    }


    /**
     * Gets the mobile value for this MemberModel.
     * 
     * @return mobile
     */
    public java.lang.String getMobile() {
        return mobile;
    }


    /**
     * Sets the mobile value for this MemberModel.
     * 
     * @param mobile
     */
    public void setMobile(java.lang.String mobile) {
        this.mobile = mobile;
    }


    /**
     * Gets the email value for this MemberModel.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this MemberModel.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the registertype value for this MemberModel.
     * 
     * @return registertype
     */
    public int getRegistertype() {
        return registertype;
    }


    /**
     * Sets the registertype value for this MemberModel.
     * 
     * @param registertype
     */
    public void setRegistertype(int registertype) {
        this.registertype = registertype;
    }


    /**
     * Gets the status value for this MemberModel.
     * 
     * @return status
     */
    public int getStatus() {
        return status;
    }


    /**
     * Sets the status value for this MemberModel.
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Gets the channelid value for this MemberModel.
     * 
     * @return channelid
     */
    public int getChannelid() {
        return channelid;
    }


    /**
     * Sets the channelid value for this MemberModel.
     * 
     * @param channelid
     */
    public void setChannelid(int channelid) {
        this.channelid = channelid;
    }


    /**
     * Gets the regdatetime value for this MemberModel.
     * 
     * @return regdatetime
     */
    public java.util.Calendar getRegdatetime() {
        return regdatetime;
    }


    /**
     * Sets the regdatetime value for this MemberModel.
     * 
     * @param regdatetime
     */
    public void setRegdatetime(java.util.Calendar regdatetime) {
        this.regdatetime = regdatetime;
    }


    /**
     * Gets the updatetime value for this MemberModel.
     * 
     * @return updatetime
     */
    public java.util.Calendar getUpdatetime() {
        return updatetime;
    }


    /**
     * Sets the updatetime value for this MemberModel.
     * 
     * @param updatetime
     */
    public void setUpdatetime(java.util.Calendar updatetime) {
        this.updatetime = updatetime;
    }


    /**
     * Gets the dr value for this MemberModel.
     * 
     * @return dr
     */
    public int getDr() {
        return dr;
    }


    /**
     * Sets the dr value for this MemberModel.
     * 
     * @param dr
     */
    public void setDr(int dr) {
        this.dr = dr;
    }


    /**
     * Gets the openid value for this MemberModel.
     * 
     * @return openid
     */
    public java.lang.String getOpenid() {
        return openid;
    }


    /**
     * Sets the openid value for this MemberModel.
     * 
     * @param openid
     */
    public void setOpenid(java.lang.String openid) {
        this.openid = openid;
    }


    /**
     * Gets the opentype value for this MemberModel.
     * 
     * @return opentype
     */
    public java.lang.String getOpentype() {
        return opentype;
    }


    /**
     * Sets the opentype value for this MemberModel.
     * 
     * @param opentype
     */
    public void setOpentype(java.lang.String opentype) {
        this.opentype = opentype;
    }


    /**
     * Gets the wxopenid value for this MemberModel.
     * 
     * @return wxopenid
     */
    public java.lang.String getWxopenid() {
        return wxopenid;
    }


    /**
     * Sets the wxopenid value for this MemberModel.
     * 
     * @param wxopenid
     */
    public void setWxopenid(java.lang.String wxopenid) {
        this.wxopenid = wxopenid;
    }


    /**
     * Gets the sessionid value for this MemberModel.
     * 
     * @return sessionid
     */
    public java.lang.String getSessionid() {
        return sessionid;
    }


    /**
     * Sets the sessionid value for this MemberModel.
     * 
     * @param sessionid
     */
    public void setSessionid(java.lang.String sessionid) {
        this.sessionid = sessionid;
    }


    /**
     * Gets the nc_id value for this MemberModel.
     * 
     * @return nc_id
     */
    public java.lang.String getNc_id() {
        return nc_id;
    }


    /**
     * Sets the nc_id value for this MemberModel.
     * 
     * @param nc_id
     */
    public void setNc_id(java.lang.String nc_id) {
        this.nc_id = nc_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MemberModel)) return false;
        MemberModel other = (MemberModel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.memberid == other.getMemberid() &&
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.userpass==null && other.getUserpass()==null) || 
             (this.userpass!=null &&
              this.userpass.equals(other.getUserpass()))) &&
            ((this.nickname==null && other.getNickname()==null) || 
             (this.nickname!=null &&
              this.nickname.equals(other.getNickname()))) &&
            this.membertype == other.getMembertype() &&
            ((this.mobile==null && other.getMobile()==null) || 
             (this.mobile!=null &&
              this.mobile.equals(other.getMobile()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            this.registertype == other.getRegistertype() &&
            this.status == other.getStatus() &&
            this.channelid == other.getChannelid() &&
            ((this.regdatetime==null && other.getRegdatetime()==null) || 
             (this.regdatetime!=null &&
              this.regdatetime.equals(other.getRegdatetime()))) &&
            ((this.updatetime==null && other.getUpdatetime()==null) || 
             (this.updatetime!=null &&
              this.updatetime.equals(other.getUpdatetime()))) &&
            this.dr == other.getDr() &&
            ((this.openid==null && other.getOpenid()==null) || 
             (this.openid!=null &&
              this.openid.equals(other.getOpenid()))) &&
            ((this.opentype==null && other.getOpentype()==null) || 
             (this.opentype!=null &&
              this.opentype.equals(other.getOpentype()))) &&
            ((this.wxopenid==null && other.getWxopenid()==null) || 
             (this.wxopenid!=null &&
              this.wxopenid.equals(other.getWxopenid()))) &&
            ((this.sessionid==null && other.getSessionid()==null) || 
             (this.sessionid!=null &&
              this.sessionid.equals(other.getSessionid()))) &&
            ((this.nc_id==null && other.getNc_id()==null) || 
             (this.nc_id!=null &&
              this.nc_id.equals(other.getNc_id())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        _hashCode += getMemberid();
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getUserpass() != null) {
            _hashCode += getUserpass().hashCode();
        }
        if (getNickname() != null) {
            _hashCode += getNickname().hashCode();
        }
        _hashCode += getMembertype();
        if (getMobile() != null) {
            _hashCode += getMobile().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        _hashCode += getRegistertype();
        _hashCode += getStatus();
        _hashCode += getChannelid();
        if (getRegdatetime() != null) {
            _hashCode += getRegdatetime().hashCode();
        }
        if (getUpdatetime() != null) {
            _hashCode += getUpdatetime().hashCode();
        }
        _hashCode += getDr();
        if (getOpenid() != null) {
            _hashCode += getOpenid().hashCode();
        }
        if (getOpentype() != null) {
            _hashCode += getOpentype().hashCode();
        }
        if (getWxopenid() != null) {
            _hashCode += getWxopenid().hashCode();
        }
        if (getSessionid() != null) {
            _hashCode += getSessionid().hashCode();
        }
        if (getNc_id() != null) {
            _hashCode += getNc_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MemberModel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "memberModel"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("memberid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "memberid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userpass");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "userpass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nickname");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nickname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("membertype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "membertype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "mobile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registertype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "registertype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("channelid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "channelid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regdatetime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "regdatetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updatetime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "updatetime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "dr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("openid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "openid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opentype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "opentype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wxopenid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "wxopenid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "sessionid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nc_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "nc_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
