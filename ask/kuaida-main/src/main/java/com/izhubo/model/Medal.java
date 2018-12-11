package com.izhubo.model;

/**
 * 签到100天， 羽毛365片
 * 系统徽章
 */
public enum Medal {

    羽毛徽章(999,365),签到徽章(998, 100),;


    Medal(Integer id,Integer count){
        this.id = id;
        this.count = count;
    }
    private final Integer id;

    private final Integer count;

    public Integer getId() {
        return id;
    }

    public Integer getCount() {
        return count;
    }
}
