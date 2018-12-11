package com.school.web.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Services have the opportunity to return both HTTP status codes along with a body in the response.
 * @author XnOU
 */
public class WrappedResponse implements java.io.Serializable {

    private static final long serialVersionUID = 2271521171625442431L;

    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAIL = "fail";
    private static final String STATUS_ERROR = "error";
    private static final String STATUS_NOT_FOUND = "not_found";
    private static final String UPDATE_VERSION = "update_version";

    /**
     * Contains the HTTP response status code as an integer.
     */
    private int code;

    /**
     * Contains the text: "success", "fail", or "error". Where "fail" is for HTTP status response values from 500-599, "error" is for
     * statuses 400-499, and "success" is for everything else (e.g. 1XX, 2XX and 3XX responses).
     */
    private String status;

    /**
     * Only used for "fail" and "error" statuses to contain the error message.
     * For internationalization (i18n) purpose, this could contain a message number or code, either alone or contained within delimiters.
     */
    private String message;

    /**
     * Contains the response body. In the case of "error" or "fail" statuses, this contains the cause, or exception name.
     */
    private Object data;

    /**
     * 成功处理，响应空字符串作为结果。
     */
    public static WrappedResponse success() {
        return success("");
    }

    /**
     * 成功处理。
     * @param data the response body
     */
    public static WrappedResponse success(Object data) {
        WrappedResponse response = new WrappedResponse();
        response.setCode(200);
        response.setStatus(STATUS_SUCCESS);
        response.setMessage("");
        response.setData(data);
        return response;
    }

    /**
     * 处理失败，抛出异常。
     * @param message 失败代码或提示
     * @param cause 导致失败的异常
     */
    public static WrappedResponse fail(String message, Object cause) {
        WrappedResponse response = new WrappedResponse();
        response.setCode(500);
        response.setStatus(STATUS_FAIL);
        response.setMessage(message);
        response.setData(cause);
        return response;
    }

    /**
     * 发生错误，由用户提交的参数非法而引起。
     * @param message 错误代码或提示
     */
    public static WrappedResponse error(String message) {
        return error(message, "");
    }

    /**
     * 发生错误，由用户提交的参数非法而引起。
     * @param message 错误代码或提示
     * @param cause 错误原因
     */
    public static WrappedResponse error(String message, Object cause) {
        WrappedResponse response = new WrappedResponse();
        response.setCode(400);
        response.setStatus(STATUS_ERROR);
        response.setMessage(message);
        response.setData(cause);
        return response;
    }
    
    /**
     * token没找到,token错误或token过期引起
     * @param message 错误代码或提示
     * @param cause 错误原因
     */
    public static WrappedResponse notFound(String message) {
        WrappedResponse response = new WrappedResponse();
        response.setCode(401);
        response.setStatus(STATUS_NOT_FOUND);
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    /**
     * 旧版app返回提示更新信息
     * @param message 错误代码或提示
     * @param cause 错误原因
     */
    public static WrappedResponse updateVersion(String message,Object cause) {
        WrappedResponse response = new WrappedResponse();
        response.setCode(205);
        response.setStatus(UPDATE_VERSION);
        response.setMessage(message);
        response.setData(cause);
        return response;
    }

    /**
     * 自定义成功返回
     * @param message 错误提示
     */
    public static Map<Object,Object> success(Object data,Map<Object,Object> map) {
        map.put("data", data);
        return map;
    }
    

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + code;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WrappedResponse other = (WrappedResponse) obj;
        if (code != other.code)
            return false;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "WrappedResponse [code=" + code + ", status=" + status + ", message=" + message + ", data=" + data + "]";
    }

}
