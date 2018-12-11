package com.hqjy.msg.util;

import java.util.List;

public class PageUtils<T> {
    //已知数据
    private int pageNum;    //当前页,从请求那边传过来。
    private int pageSize;    //每页显示的数据条数。
    private int totalRecord;    //总的记录条数。查询数据库得到的数据
    //需要计算得来
    private int totalPage;    //总页数，通过totalRecord和pageSize计算可以得来
    //将每页要显示的数据放在list集合中
    private List<T> list;

    public PageUtils(){}
    //通过pageNum，pageSize，totalRecord计算得来tatalPage和startIndex
    //构造方法中将pageNum，pageSize，totalRecord获得
    public PageUtils(int pageNum, int pageSize, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalRecord = list.size();
        //this.list = list;
        //totalPage 总页数
        if(totalRecord%pageSize==0){
            //说明整除，正好每页显示pageSize条数据，没有多余一页要显示少于pageSize条数据的
            this.totalPage = totalRecord / pageSize;
        }else{
            //不整除，就要在加一页，来显示多余的数据。
            this.totalPage = totalRecord / pageSize +1;
        }

        //this.list = (List<T>) ListUtils.listsToArray(list,pageSize).get(pageNum-1);
        int ends = (pageNum)*pageSize>totalRecord?totalRecord:(pageNum)*pageSize;
        int starts = (pageNum-1)*pageSize;
        this.list = list.subList(starts,ends);
    }
    //get、set方法。
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


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }




}