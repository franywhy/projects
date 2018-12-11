package com.elise.userinfocenter.pojo;

import java.io.PipedReader;
import java.io.Serializable;
import java.util.List;

/**
 * Created by DL on 2018/1/23.
 */
public class CityPOJO implements Serializable {
    //id
    private Long cityId;
    //父id
    private  Long parentId;
    //城市编码
    private String cityCode;
    //城市名称
    private String cityName;
    //状态,0启用,1不启用
    private Integer status;
    //子城市列表
    private List<CityPOJO> childrenCityList;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CityPOJO> getChildrenCityList() {
        return childrenCityList;
    }

    public void setChildrenCityList(List<CityPOJO> childrenCityList) {
        this.childrenCityList = childrenCityList;
    }
}
