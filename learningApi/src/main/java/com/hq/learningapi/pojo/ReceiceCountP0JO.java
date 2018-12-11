package com.hq.learningapi.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DL on 2018/3/9.
 */
public class ReceiceCountP0JO extends ArrayList {
    private String date;
    private int count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
