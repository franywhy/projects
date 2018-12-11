package com.hq.learningapi.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CommentPOJO {

    private Long commentId;

    private String content;

    private String name;

    private String avatar;

    private Integer likeNumber;

    private Date creationTime;

    private Integer isLike;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
  
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    @DateTimeFormat(pattern = "MM-dd HH:mm")
    @JsonFormat(pattern = "MM-dd HH:mm",timezone = "GMT+8")
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }
}