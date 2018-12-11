package com.bluewhale.http;


import com.bluewhale.common.enumeration.TransactionStatus;

public class HttpResultDetail<T> {

    private Boolean isOK;
    private Boolean isClientError;
    private Boolean isServerError;
    private String responseMessage;
    private Integer responseStatus;
    private T result ;


    public Boolean isOK() {
        return isOK;
    }

    protected void setOK(Boolean OK) {
        isOK = OK;
    }

    public Boolean isClientError() {
        return isClientError;
    }

    protected void setClientError(Boolean clientError) {
        isClientError = clientError;
    }

    public Boolean isServerError() {
        return isServerError;
    }

    protected void setServerError(Boolean serverError) {
        isServerError = serverError;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    protected void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public TransactionStatus getResponseStatus() {
        return TransactionStatus.valueOf(responseStatus);
    }

    protected void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public T getResult() {
        return result;
    }

    protected void setResult(T result) {
        this.result = result;
    }



}
