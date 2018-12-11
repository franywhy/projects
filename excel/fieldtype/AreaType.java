/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.accentrix.lifetouch.common.utils.excel.fieldtype;

import org.apache.commons.lang3.StringUtils;

import com.accentrix.lifetouch.entity.sys.Area;
import com.accentrix.lifetouch.util.UserUtils;

/**
 * 字段类型转换
 * 
 * @author ThinkGem
 * @version 2013-03-10
 */
public class AreaType {

    /**
     * 获取对象值（导入）
     */
    public static Object getValue(String val) {
        for (Area e : UserUtils.getAreaList()) {
            if (StringUtils.trimToEmpty(val).equals(e.getName())) {
                return e;
            }
        }
        return null;
    }

    /**
     * 获取对象值（导出）
     */
    public static String setValue(Object val) {
        if (val != null && ((Area) val).getName() != null) {
            return ((Area) val).getName();
        }
        return "";
    }
}
