package com.hq.courses.util;

import io.renren.utils.RRException;
import org.apache.commons.lang.StringUtils;

/**
 * 编码截取和拼接工具类
 * @author: shihongjie
 * @email: shihongjie@hengqijy.com
 * @date: 2018/3/1 10:36
 */
public final class CodeSplitUtils {

    public static void main(String[] args) {
        System.out.println(chapterEndCode("kZk1mZ01ZA0"));
        System.out.println(sectionEndCode("kckm01ZA0J01" ));
        System.out.println(knowledgeEndCode("kckm01ZA0J01K001"));
        System.out.println(chapterFullCode("kZk1mZ01ZA0" , "kZk1mZ01"));
        System.out.println(chapterFullCode("kZk1mZ01JA0" , "kZk2mZ01"));
    }

    public static final String splitCode(final String code , final String split){
        String string = null;
        if(StringUtils.isNotBlank(code) && code.indexOf(split) > -1){
            string = code.substring( code.lastIndexOf(split) , code.length());
        }
        return string;
    }

    /**
     * 获取本级根编码
     * <p>1.校验code是否合法 不合法返回null<p/>
     * <p>2.截取本级根code<p/>
     * @param code          章编码
     * @return
     */
    public static final String chapterEndCode(final String code)throws RRException {
        CodeValidateUtils.chapterCodeValidate(code);
        return splitCode(code , CodeValidateUtils.CSCHAPTER_TYPE);
    }

    /**
     * 获取本级根编码
     * <p>1.校验code是否合法 不合法返回null<p/>
     * <p>2.截取本级根code<p/>
     * @param code          章编码
     * @return
     * @throws RRException 校验编码code异常错误
     */
    public static final String sectionEndCode(final String code)throws RRException{
        CodeValidateUtils.sectionCodeValidate(code);
        return splitCode(code , CodeValidateUtils.CSSECTION_TYPE);
    }
    /**
     * 获取本级根编码
     * <p>1.校验code是否合法 不合法返回null<p/>
     * <p>2.截取本级根code<p/>
     * @param code          章编码
     * @return
     * @throws RRException 校验编码code异常错误
     */
    public static final String knowledgeEndCode(final String code)throws RRException{
        CodeValidateUtils.knowledgeCodeValidate(code);
        return splitCode(code , CodeValidateUtils.CSKNOWLEDGE_TYPE);
    }

    /**
     * 获取本级完整编码
     * <p>1.校验code是否合法 不合法返回null<p/>
     * <p>2.截取本级根code<p/>
     * <p>3.拼接编码(上级+本级根编码)<p/>
     * @param code          章编码
     * @param parentCode    上级编码
     * @return
     */
    public static final String chapterFullCode(final String code , final String parentCode){
        if(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(parentCode)){
            String endCode = chapterEndCode(code);
            if(StringUtils.isNotBlank(endCode)){
                return parentCode + endCode;
            }
        }
        return null;
    }
    /**
     * 获取本级完整编码
     * <p>1.校验code是否合法 不合法返回null<p/>
     * <p>2.截取本级根code<p/>
     * <p>3.拼接编码(上级+本级根编码)<p/>
     * @param code          章编码
     * @param parentCode    上级编码
     * @return
     */
    public static final String sectionFullCode(final String code , final String parentCode){
        if(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(parentCode)){
            String endCode = sectionEndCode(code);
            if(StringUtils.isNotBlank(endCode)){
                return parentCode + endCode;
            }
        }
        return null;
    }
    /**
     * 获取本级完整编码
     * <p>1.校验code是否合法 不合法返回null<p/>
     * <p>2.截取本级根code<p/>
     * <p>3.拼接编码(上级+本级根编码)<p/>
     * @param code          章编码
     * @param parentCode    上级编码
     * @return
     */
    public static final String knowledgeFullCode(final String code , final String parentCode){
        if(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(parentCode)){
            String endCode = knowledgeEndCode(code);
            if(StringUtils.isNotBlank(endCode)){
                return parentCode + endCode;
            }
        }
        return null;
    }

}
