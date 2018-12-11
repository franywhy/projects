package com.hq.learningapi.pojo;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public class OrderPOJO {

    private Long orderId;
    private Long areaId;
    private Long levelId;
    private String title;
    private String classTypeName;
    private Long classId;
    private Long professionId;
    private Long commodityId;
    private Long classTypeId;
    private String wxCode;
    private String backGroundPic;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Long professionId) {
        this.professionId = professionId;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public Long getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(Long classTypeId) {
        this.classTypeId = classTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWxCode() {
        return wxCode;
    }

    public void setWxCode(String wxCode) {
        this.wxCode = wxCode;
    }

    public String getBackGroundPic() {
        return backGroundPic;
    }

    public void setBackGroundPic(String backGroundPic) {
        this.backGroundPic = backGroundPic;
    }

    public String getClassTypeName() {
        return classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }


}
