package com.hq.learningapi.entity;

/**
 * Created by Administrator on 2018/1/16 0016.
 */
public class LikeUser {

    private Long id;

    private Long likeObject;

    private Long userId;

    private Integer likeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLikeObject() {
        return likeObject;
    }

    public void setLikeObject(Long likeObject) {
        this.likeObject = likeObject;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getLikeType() {
        return likeType;
    }

    public void setLikeType(Integer likeType) {
        this.likeType = likeType;
    }
}
