package com.elise.userinfocenter.entity;

import java.io.Serializable;

/**
 * Created by DL on 2017/12/29.
 */
public class CityEntity  implements Serializable{
    private  Long id;
    //用户id
    private Long userId;
    //城市编码
    private String cityCode;
    //城市名称
    private String cityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "CityEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", cityCode='" + cityCode + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
