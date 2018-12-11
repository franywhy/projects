package com.hq.learningapi.pojo;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25 0025.
 */
public class PushTag {

    private Integer id;
    private List<String> tagList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}
