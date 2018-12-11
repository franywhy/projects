package io.renren.modules.job.pojo;

import io.renren.modules.job.pojo.log.LogPolyvDetailPOJO;

import java.util.List;

/**
 * Created by DL on 2018/10/16.
 */
public class PolyvResultPOJO {
    private int code;
    private String status;
    private String message;
    private List<LogPolyvDetailPOJO> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LogPolyvDetailPOJO> getData() {
        return data;
    }

    public void setData(List<LogPolyvDetailPOJO> data) {
        this.data = data;
    }
}
