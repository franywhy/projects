package io.renren.utils.service.adaptive.pojo;

/**
 * 自适应算法服务接口返回值类型
 */
public class ASResponseModel {

    /*
        201 Created

        401 Unauthorized

        403 Forbidden

        404 Not Found
    */

    private String code;
    private Object data;
    private String message;



    @Override
    public String toString() {
        return "ASResponseModel{" +
                "code=" + code +
                ", data='" + data + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
}
