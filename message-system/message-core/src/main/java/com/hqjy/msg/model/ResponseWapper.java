package com.hqjy.msg.model;


import java.io.Serializable;

/**
 * Created by baobao on 2017/8/21 0021.
 * 接口返回的格式类
 */
public class ResponseWapper implements Serializable {
    private int code;
    private Object data;
    private String message;

    public ResponseWapper() {
    }

    public ResponseWapper(int code, Object data, String message) {

        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseWapper success(Object data){
        this.code = 200;
        this.data = data;
        this.message = "success";
        return this;
    }

    public ResponseWapper success(){
        this.code = 200;
        this.data = null;
        this.message = "success";
        return this;
    }

    public ResponseWapper fail(){
        this.code = -200;
        this.message = "fail";
        return this;
    }
}
