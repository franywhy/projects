package com.elise.singlesignoncenter.token;


import com.hq.common.enumeration.ClientTypeEnum;
import com.hq.common.util.HexUtil;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by Glenn on 2017/4/21 0021.
 */
public class UserTokenGenerator {

    private static final Random random = new Random();
    public static String generate(Byte clientType, Integer versionCode, Integer userId,Boolean isOneTime) {
        /**
           [ClientStamp生成算法]
           ClientType:8 bits(第八位始终为0,第七位标识是否为一次性token)
           ClientVersion:24 bits
         */
        if(isOneTime){
            clientType = (byte)(clientType | (byte)0x40);
        }
        Integer clientStamp = (clientType.intValue() << 24) ^ versionCode;

        /***
         * [TimeStamp 算法]
         * */
        Long timeStamp = System.currentTimeMillis();

        /**
         [Token生成算法]
         UserId(32 bits)+TimeStamp(64 bits)+ClientStamp(32 bits)
         UserId与0x80000000做异或运算
         TimeStamp与0x8000000000000000L做异或运算
         ClientStamp与0x80000000做异或运算
         */
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(userId ^ Integer.MIN_VALUE));
        sb.append(Long.toHexString(timeStamp ^ Long.MIN_VALUE));
        sb.append(Integer.toHexString(clientStamp ^ Integer.MIN_VALUE));
        return sb.toString();
    }

    public static boolean isOneTimeToken(String userToken){
        UserToken  token = parse(userToken);
        return token.getOneTimeToken();
    }

    public static boolean isValid(String userToken){
          try {
              if (userToken.length() != 32) {
                  return false;
              } else {
                  UserToken  token = parse(userToken);
                  return true;
              }
          } catch(Throwable t){
              t.printStackTrace();
              return false;
          }
    }

    public static boolean isExpired(String userToken,long maxTimeBucket){
        UserToken  token = parse(userToken);
        Long usedTime = System.currentTimeMillis() - token.getTimeStamp();
        return usedTime >= maxTimeBucket ? true : false;
    }

    public static UserToken parse(String userToken) {
        /**
         * 解析算法原理
         * 详见生成算法
         * */
        ByteBuffer userTokenBuffer = ByteBuffer.wrap(HexUtil.hexStr2Bytes(userToken));
        Integer userId = userTokenBuffer.getInt() ^ Integer.MIN_VALUE;
        Long timeStamp = userTokenBuffer.getLong() ^ Long.MIN_VALUE;
        Integer clientStamp = userTokenBuffer.getInt() ^ Integer.MIN_VALUE;
        //
        Integer versionCode = clientStamp & 0x00ffffff;
        Integer IntegerClientType = clientStamp >> 24;
        Boolean isOneTimeToken;
        Byte clientType;
        //
        if((IntegerClientType >> 6)==1){
            isOneTimeToken = true;
            clientType = (byte)(0xbf & IntegerClientType.byteValue());
        }else{
            isOneTimeToken = false;
            clientType = IntegerClientType.byteValue();
        }

        UserToken token = new UserToken(userId,versionCode, ClientTypeEnum.convertByte2ClientType(clientType),timeStamp,isOneTimeToken);
        return token;
    }

}
