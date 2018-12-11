package com.hqjy.pay.utils;

import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;

public class CustomBigDecimalEditor extends PropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            setValue(NumberUtils.getBigDecimal(text));
        }
    }

}
