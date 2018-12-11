package com.bluewhale.common.enumeration;

import java.util.HashMap;

/**
 * Created by Glenn on 2017/5/5 0005.
 */
public enum ClientTypeEnum {

    WEB((byte)0x00,"web"),
    ANDROID((byte)0x01,"android"),
    IOS((byte)0x02,"ios");

    private static HashMap<Byte,ClientTypeEnum> type2Client = new HashMap<Byte,ClientTypeEnum>();
    private static HashMap<String,ClientTypeEnum> str2Client = new HashMap<String,ClientTypeEnum>();

    static {
        for (ClientTypeEnum type : ClientTypeEnum.values()) {
            type2Client.put(type.type,type);
            str2Client.put(type.strType,type);
        }
    }

    private Byte type;
    private String strType;
    ClientTypeEnum(Byte type,String strType){
           this.strType = strType;
           this.type = type;
    }

    public Byte getType(){
        return type;
    }

    public String getStrType(){
        return strType.toLowerCase();
    }

    public static ClientTypeEnum convertByte2ClientType(Byte type){
        return type2Client.get(type);
    }

    public static ClientTypeEnum convertString2ClientType(String strType){
        return str2Client.get(strType.toLowerCase());
    }

}
