package com.hq.answerapi.util;

import com.hq.common.util.CommonUtil;
import com.hq.common.util.EncryptionUtils;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by Glenn on 2017/7/4 0004.
 * @author hq
 */
public class PassWordUtil {

    private static final String PASS_WORD_REGEX = "^[a-zA-Z0-9]{4,20}$";

    private static final String MD5_KEY = "%^\\$AF>.12*******zK";

    /**
     * Check the regular expression of password
     * */
    public static Boolean isValid(String passWord){
        String plainStr = new String(Base64.decodeBase64(passWord));
        return CommonUtil.checkRegular(PASS_WORD_REGEX,plainStr);
    }

    public static Boolean isCorrect(String dstPassWord,String srcPassWord){
            return EncryptionUtils.md5Check(dstPassWord+MD5_KEY, srcPassWord);
    }

    public static String encrypted(String passWord){
        return  EncryptionUtils.md5Hex(passWord+MD5_KEY);
    }
}
