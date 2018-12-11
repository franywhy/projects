package io.renren.entity.manage;

public class AppCommodity {
    private Integer id;

    private String ncCommodityId;

    private Integer itemType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNcCommodityId() {
        return ncCommodityId;
    }

    public void setNcCommodityId(String ncCommodityId) {
        this.ncCommodityId = ncCommodityId == null ? null : ncCommodityId.trim();
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }
}