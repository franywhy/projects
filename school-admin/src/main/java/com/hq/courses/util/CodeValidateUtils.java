package com.hq.courses.util;

import com.hq.courses.entity.CsChapterEntity;
import io.renren.utils.RRException;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class  CodeValidateUtils {



    public static String CSCOURSE_PATTERN = "^[a-zA-Z][0-9a-zA-Z]{5,100}$";
    public static String CSCHAPTER_PATTERN = "^.+Z[A-Za-z0-9][0-9]{1,100}$";
    public static String CSSECTION_PATTERN = "^.+J[0-9]{2,100}$";
    public static String CSKNOWLEDGE_PATTERN = "^.+[K|Z][0-9]{2,100}$";

    public static String CSCOURSE_TYPE = "KC";
    public static String CSCHAPTER_TYPE = "Z";
    public static String CSSECTION_TYPE = "J";
    public static String CSKNOWLEDGE_TYPE = "K";

    private static String COURSE_MSG = "课程编码错误；需以字母开头，由数字和字母组成，不小于六位。例如：kckm01。";
    private static String CSCHAPTER_MSG = "章编码错误；需由数字和字母组成，不区分大小写；\n" +
            "章编码由两部分组成，前半部分是课程编码，后半部分是章编号；\n" +
            "章编号不得小于3位，以Z开头。\n" +
            "         例如：kckm01Z01。前半部分kckm01是课程编码，后半部分是Z01是区分章的编号。";
    private static String CSSECTION_MSG = "节编码错误；需由数字和字母组成，不区分大小写；\n" +
            "节编码由两部分组成，前半部分是章编码，后半部分是节编号；\n" +
            "节编号不得小于3位，以J开头，J后面均为数字。\n" +
            "例如：kckm01Z01J01。前半部分kckm01Z01是章编码，后半部分是J01是区分节的编号。";
    private static String CSKNOWLEDGE_MSG = "知识点编码错误；需由数字和字母组成，不区分大小写;\n" +
            "知识点编码由两部分组成，前半部分是节编码，后半部分是知识点编号;\n" +
            "知识点编号不得小于4位,以K开头，K后面均为数字。\n" +
            "例如：kckm01ZA0J01K001。前半部分kckm01ZA0J01是节编码，后半部分是K001是区分知识点的编号。";

    public static void main(String[] args) {

        System.out.println(codeValidate("kckm906Z01", CodeValidateUtils.CSCHAPTER_PATTERN,"kckm906",CodeValidateUtils.CSCHAPTER_TYPE));
    }

    /**
     * 编码验证
     * @param str 编码
     * @param pattern 正则表达式：静态常量
     * @param parent 父级编码，课程父级为null
     * @param type 编码类型：静态常量，课程类型为null
     */
	public static String codeValidate(String str, String pattern, String parent,String type)throws RRException{
	    if(!(excelRegexMatches(str,pattern,type) && parentMatches(str,parent,type))){
            try {
                throwException(type);
            } catch (RRException e) {
                return e.getMsg();
            }
        }
        return null;
	}

    public static void regexMatches(String str,String pattern,String type)throws RRException{
        if(!excelRegexMatches(str,pattern,type)){
            throwException(type);
        }
    }

	public static boolean excelRegexMatches(String str,String pattern,String type){
	    Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		return m.matches();
	}

	public static boolean parentMatches(String str,String parent,String type){
	    return str.contains(parent + type);
    }

    /**
     * 校验code
     * @param code
     * @throws RRException
     */
    public static final void courseCodeValidate(final String code)throws RRException{
        regexMatches(code , CSCOURSE_PATTERN, CSCOURSE_TYPE);
    }
    public static final void chapterCodeValidate(final String code )throws RRException{
        regexMatches(code, CSCHAPTER_PATTERN, CSCHAPTER_TYPE);
    }
    public static final void sectionCodeValidate(final String code )throws RRException{
        regexMatches(code , CSSECTION_PATTERN  , CSSECTION_TYPE);
    }
    public static final void knowledgeCodeValidate(final String code )throws RRException{
        regexMatches(code , CSKNOWLEDGE_PATTERN  , CSKNOWLEDGE_TYPE);
    }

    public static void throwException(String type){
        if(CSCOURSE_TYPE.equals(type)){
            throw new RRException(COURSE_MSG);
        }
        if(CSCHAPTER_TYPE.equals(type)){
            throw new RRException(CSCHAPTER_MSG);
        }
        if(CSSECTION_TYPE.equals(type)){
            throw new RRException(CSSECTION_MSG);
        }
        if(CSKNOWLEDGE_TYPE.equals(type)){
            throw new RRException(CSKNOWLEDGE_MSG);
        }
    }

}