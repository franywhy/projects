package com.elise.datacenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Glenn on 2017/5/19 0019.
 */
@ConfigurationProperties(prefix="ip-white-list")
@Component
public class IPWhiteListEntity {
    private Boolean filterSwitch;
    private List<String> list;

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