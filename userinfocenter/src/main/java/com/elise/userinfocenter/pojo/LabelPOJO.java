package com.elise.userinfocenter.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DL on 2017/12/29.
 */
public class LabelPOJO implements Serializable {
    //id
    private Long id;
    //父id
    private Long parentId;
    //标签名称
    private String labelName;
    //产品线
    private Long productId;
    //状态码,0启用,1不启用
    private Integer dr;
    //子标签列表
    private List<LabelPOJO> childrenLabelList;

    public List<LabelPOJO> getChildrenLabelList() {
        return childrenLabelList;
    }

    public void setChildrenLabelList(List<LabelPOJO> childrenLabelList) {
        this.childrenLabelList = childrenLabelList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }


    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    @Override
    public String toString() {
        return "LabelPOJO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", labelName='" + labelName + '\'' +
                ", productId=" + productId +
                ", dr=" + dr +
                ", childrenLabelList=" + childrenLabelList +
                '}';
    }
}
