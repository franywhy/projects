package com.bluewhale.common.enumeration;

/**
 * Created by Glenn on 2017/5/9 0009.
 */
public enum TransactionStatus {
//    CONTINUE(100, "Continue"),
//    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
//    PROCESSING(102, "Processing"),
//    CHECKPOINT(103, "Checkpoint"),


      OK(200, "操作成功"),
      LATEST_VERSION(201, "当前客户端最新版本"),
//    ACCEPTED(202, "Accepted"),
//    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
      NO_CONTENT(204, "无内容"),
//    RESET_CONTENT(205, "Reset Content"),
//    PARTIAL_CONTENT(206, "Partial Content"),
//    MULTI_STATUS(207, "Multi-Status"),
//    ALREADY_REPORTED(208, "Already Reported"),
//    IM_USED(226, "IM Used"),


//    MULTIPLE_CHOICES(300, "Multiple Choices"),
//    MOVED_PERMANENTLY(301, "Moved Permanently"),
//    FOUND(302, "Found"),
//    MOVED_TEMPORARILY(302, "Moved Temporarily"),
//    SEE_OTHER(303, "See Other"),
//    NOT_MODIFIED(304, "Not Modified"),
//    USE_PROXY(305, "Use Proxy"),
//    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
//    PERMANENT_REDIRECT(308, "Permanent Redirect"),


    BAD_REQUEST(400, "错误的请求"),
    USER_TOKEN_NOT_FOUND(401, "登录信息失效，请重新登录"),
    USER_NOT_FOUND(402, "用户不存在"),
    PASSWORD_ERROR(403, "密码输入错误"),
    INVALID_MOBILE_NUMBER(404,"错误手机号码"),
    INVALID_PASS_WORD(405, "密码格式非法"),
    NOT_FOUND(406, "请求资源未找到"),
    REQUEST_REJECT(407, "服务被拒绝"),
    UPLOAD_FAILED(408,"上传文件失败"),
    DAMAGE_FILE(409, "文件已经被损坏"),
    FILE_TOO_LARGE(410, "文件过大"),
    FROZEN_USER(411,"用户被冻结"),
    DUPLICATE_MOBILE_NUMBER(412,"号码重复注册"),
    UNKNOWN_BUSINESS_ID(413,"未知的业务ID"),

//    LENGTH_REQUIRED(411, "Length Required"),
//    PRECONDITION_FAILED(412, "Precondition Failed"),
//    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
//    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
//    URI_TOO_LONG(414, "URI Too Long"),
//    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
//    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
//    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),
//    EXPECTATION_FAILED(417, "Expectation Failed"),
//    I_AM_A_TEAPOT(418, "I'm a teapot"),
//    INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"),
//    METHOD_FAILURE(420, "Method Failure"),
//    DESTINATION_LOCKED(421, "Destination Locked"),
//    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
//    LOCKED(423, "Locked"),
//    FAILED_DEPENDENCY(424, "Failed Dependency"),
//    UPGRADE_REQUIRED(426, "Upgrade Required"),
//    PRECONDITION_REQUIRED(428, "Precondition Required"),
//    TOO_MANY_REQUESTS(429, "Too Many Requests"),
//    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
//    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),


    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    CHECK_VERSION_FAILED(501, "更新检查失败"),
//    BAD_GATEWAY(502, "Bad Gateway"),
//    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
//    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
//    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
//    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
//    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
//    LOOP_DETECTED(508, "Loop Detected"),
//    BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),
//    NOT_EXTENDED(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

    private final int value;
    private final String reasonPhrase;

    private TransactionStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public boolean is1xxInformational() {
        return Series.INFORMATIONAL.equals(this.series());
    }

    public boolean is2xxSuccessful() {
        return Series.SUCCESSFUL.equals(this.series());
    }

    public boolean is3xxRedirection() {
        return Series.REDIRECTION.equals(this.series());
    }

    public boolean is4xxClientError() {
        return Series.CLIENT_ERROR.equals(this.series());
    }

    public boolean is5xxServerError() {
        return Series.SERVER_ERROR.equals(this.series());
    }

    public Series series() {
        return Series.valueOf(this);
    }

    public String toString() {
        return Integer.toString(this.value);
    }

    public static TransactionStatus valueOf(int statusCode) {
        TransactionStatus[] var1 = values();
        int var2 = var1.length;
        for(int var3 = 0; var3 < var2; ++var3) {
            TransactionStatus status = var1[var3];
            if(status.value == statusCode) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }

    public static enum Series {
        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        private Series(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Series valueOf(int status) {
            int seriesCode = status / 100;
            Series[] var2 = values();
            int var3 = var2.length;
            for(int var4 = 0; var4 < var3; ++var4) {
                Series series = var2[var4];
                if(series.value == seriesCode) {
                    return series;
                }
            }
            throw new IllegalArgumentException("No matching constant for [" + status + "]");
        }
        public static Series valueOf(TransactionStatus status) {
            return valueOf(status.value);
        }
    }
}