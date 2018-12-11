package com.hq.answerapi.model;

/**
 * Created by Administrator on 2018/9/20 0020.
 * @author hq
 */
public enum Priv {

    Teacher(2), Student(3);

    private int value;

    Priv(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
