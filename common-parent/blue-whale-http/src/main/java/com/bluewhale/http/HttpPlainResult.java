package com.bluewhale.http;

public class HttpPlainResult {

    private String result;
    private HttpResultType type;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HttpResultType getCode() {
        return type;
    }

    public void setCode(HttpResultType type) {
        this.type = type;
    }
}
