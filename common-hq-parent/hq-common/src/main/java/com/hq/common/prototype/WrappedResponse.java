package com.hq.common.prototype;

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

    /**
     * 成功处理，响应空字符串作为结果。
     */
    public static WrappedResponse success() {
        return generate(200,"",null);
    }
    /**
     * 成功处理，回应提示语字符串作为结果
     * */
    public static WrappedResponse success(String message){
        return generate(200,message,null);
    }
    /**
     * 成功处理，回应data数据作为结果
     * */
    public static WrappedResponse success(String message, Object data){
        return generate(200,message,data);
    }
    /**
     * 失败处理，回应Code以及Message作为结果
     * */
    public static WrappedResponse fail(Integer code, String message){
        return generate(code,message,null);
    }

    /**
     * Response生成器
     * @param code the response body
     * @param message the response body
     * @param data the response body
     */
    public static WrappedResponse generate(Integer code, String message, Object data) {
        WrappedResponse response = new WrappedResponse();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    private void setData(T data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + code;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
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
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append( "WrappedResponse [code=");
        sb.append(code);
        sb.append(", message=");
        sb.append(message);
        sb.append(", data=");
        sb.append(data);
        sb.append("]");
        return sb.toString();
    }

}
