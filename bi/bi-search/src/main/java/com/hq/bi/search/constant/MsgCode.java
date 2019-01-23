package com.hq.bi.search.constant;

/**
 * 消息状态码统一定义
 *
 * @author chenzeke
 * @date 2018/11/22 15:45
 */
public enum MsgCode {
    /**
     * 状态码与提示语对应
     */
    SUCCESS(200, "成功"),
    ILLEGAL_ARGUMENT(400, "参数异常"),
    NO_AUTH(401, "没有权限"),
    NOT_FOUND(404, "无法找到资源"),
    NOT_LOGIN(403, "未登录"),
    SERVER_EXCEPTION(500, "服务器繁忙"),
    NOT_NULL(1000, "参数不能为空"),
    FORM_EXCEPTION(1001, "表单验证异常");

    /**
     * 状态码
     */
    private int code;
    /**
     * 提示语
     */
    private String message;

    MsgCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
