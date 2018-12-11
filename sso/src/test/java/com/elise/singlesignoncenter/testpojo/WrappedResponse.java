package com.elise.singlesignoncenter.testpojo;

/**
 * Services have the opportunity to return both HTTP status codes along with a body in the response.
 * @author XnOU
 */
public class WrappedResponse<T> implements java.io.Serializable {

    private static final long serialVersionUID = 2271521171625442431L;

    /**
     * Contains the HTTP response status code as an integer.
     */
    private int code;

    /**
     * Contains the text for HTTP status response values
     */
    private String message;

    /**
     * Contains the response body. In the case of "error" or "fail" statuses, this contains the cause, or exception name.
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
