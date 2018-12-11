package com.hqjy.msg.model;

import java.util.List;

/**
 * Created by baobao on 2017/12/19 0019.
 * 限制IP的实体类
 */
public class IpListEntity {

    //是否开启访问IP的限制
    private Boolean filterSwitch;

    //IP地址列表
    private List<String> list;

    public IpListEntity(){}

    public IpListEntity(Boolean filterSwitch,List<String> list){
        this.filterSwitch = filterSwitch;
        this.list = list;
    }

    public Boolean getFilterSwitch() {
        return filterSwitch;
    }

    public void setFilterSwitch(Boolean filterSwitch) {
        this.filterSwitch = filterSwitch;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
