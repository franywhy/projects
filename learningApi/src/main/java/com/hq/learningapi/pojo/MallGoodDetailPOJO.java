package com.hq.learningapi.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DL on 2018/9/10.
 */
public class MallGoodDetailPOJO extends MallGoodPOJO implements Serializable {
    //商品适用对象
    private String suitableUser;
    //商品介绍
    private String goodRecomment;
    //用户是否已经购买商品: 0没有,1已购买
    private Integer isExistOrder;
    //商品购买须知
    private List<String> phurchaseNotes;

    public String getSuitableUser() {
        return suitableUser;
    }

    public void setSuitableUser(String suitableUser) {
        this.suitableUser = suitableUser;
    }

    public String getGoodRecomment() {
        return goodRecomment;
    }

    public void setGoodRecomment(String goodRecomment) {
        this.goodRecomment = goodRecomment;
    }

    public Integer getIsExistOrder() {
        return isExistOrder;
    }

    public void setIsExistOrder(Integer isExistOrder) {
        this.isExistOrder = isExistOrder;
    }

    public List<String> getPhurchaseNotes() {
        return phurchaseNotes;
    }

    public void setPhurchaseNotes(List<String> phurchaseNotes) {
        this.phurchaseNotes = phurchaseNotes;
    }

    @Override
    public String toString() {
        return "MallGoodDetailPOJO{" +
                "suitableUser='" + suitableUser + '\'' +
                ", goodRecomment='" + goodRecomment + '\'' +
                ", isExistOrder=" + isExistOrder +
                ", phurchaseNotes=" + phurchaseNotes +
                '}';
    }
}
