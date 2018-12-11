package com.hq.common.enumeration;



/**
 * Created by Glenn on 2017/7/21 0011.
 */
public enum GenderEnum {

    FEMALE(0),
    MALE(1),
    UNKNOWN(2);

    private Integer value;

    GenderEnum(Integer value){
        this.value =value;
    }
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
