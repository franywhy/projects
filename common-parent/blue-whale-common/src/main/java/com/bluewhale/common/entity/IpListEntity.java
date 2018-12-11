package com.bluewhale.common.entity;

import java.util.List;


/**
 * Created by Glenn on 2017/8/7 0011.
 */
public class IpListEntity {
    private Boolean filterSwitch;
    private List<String> list;

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
