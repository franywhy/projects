package com.hq.learningapi.pojo;

import java.io.Serializable;
import java.util.List;

public class UserMsgPagePOJO implements Serializable{
    private int pageNum;
    private int pageSize;
    private int totalRecord;
    private int totalPage;
    private List<UserMsgContextPOJO> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<UserMsgContextPOJO> getList() {
        return list;
    }

    public void setList(List<UserMsgContextPOJO> list) {
        this.list = list;
    }
}
