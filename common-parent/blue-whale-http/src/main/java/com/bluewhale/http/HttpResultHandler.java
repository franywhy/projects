package com.bluewhale.http;

import com.bluewhale.common.enumeration.TransactionStatus;
import com.bluewhale.common.prototype.WrappedResponse;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpResultHandler {

    private static final Logger TRACER = LoggerFactory.getLogger(HttpResultHandler.class);

    private  static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> HttpResultDetail<T> handle(HttpPlainResult plainResult, Class<? super T> rawType) throws IOException {
        Boolean isOK  = false;
        Boolean isClientError = false;
        Boolean isServerError = false;
        String  responseMessage = null;
        Integer responseCode = null;
        T result = null;
        if (plainResult.getCode() == HttpResultType.OK) {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(WrappedResponse.class,rawType);
            WrappedResponse<T> InnerResponse = objectMapper.readValue(plainResult.getResult(), type);
            TransactionStatus status = TransactionStatus.valueOf(InnerResponse.getCode());
            if (status.is2xxSuccessful()) {
                isOK = true;
                result = InnerResponse.getData();
            } else if (status.is4xxClientError()) {
                isClientError = true;

            } else if (status.is5xxServerError()) {
                isServerError = true;

            }
            responseMessage = InnerResponse.getMessage();
            responseCode = InnerResponse.getCode();
        }else{
            TRACER.error("Invoke Remote Service Failed");
            isServerError = true;
        }
        HttpResultDetail entry = new HttpResultDetail<T>();
        entry.setOK(isOK);
        entry.setClientError(isClientError);
        entry.setServerError(isServerError);
        entry.setResponseStatus(responseCode);
        entry.setResponseMessage(responseMessage);
        entry.setResult(result);
        return entry;
    }

}
