package com.hq.learningapi.pojo;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by DL on 2018/9/10.
 */
public class MallGoodPOJO implements Serializable{
    //商品id
    private Long goodId;
    //商品名称
    private String goodName;
    //商品ncId
    private String ncId;
    //商品图片
    private String picture;
    //商品价格
    private BigDecimal price;

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getNcId() {
        return ncId;
    }

    public void setNcId(String ncId) {
        this.ncId = ncId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        if (StringUtils.isBlank(picture)){
            picture = "http://download.hqjy.com/staic/course/1-min.jpg";
        }
        this.picture = picture;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        BigDecimal multiply = price.multiply(new BigDecimal(1.43));
        this.price = multiply.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return "MallGoodPOJO{" +
                "goodId=" + goodId +
                ", goodName='" + goodName + '\'' +
                ", ncId='" + ncId + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                '}';
    }
}
