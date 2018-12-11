package com.elise.userinfocenter.entity;

import java.io.Serializable;

/**
 * Created by DL on 2017/12/29.
 */
public class LabelEntity implements Serializable {
    //id
    private Long id;
    //用户id
    private Long userId;
    //标签id
    private Long labelId;

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

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    @Override
    public String toString() {
        return "LabelEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", labelId=" + labelId +
                '}';
    }
}
