package com.bluewhale.http;

public enum HttpResultType {
    OK(200),
    FAIL(400),
    ERROR(500),
    TIMEOUT(600);

    private Integer code;
    HttpResultType(Integer code){
        this.code = code;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


}
