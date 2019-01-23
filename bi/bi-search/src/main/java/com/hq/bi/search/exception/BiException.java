package com.hq.bi.search.exception;

import com.hq.bi.search.constant.MsgCode;

/**
 * @author chenzeke
 * @date 2018/12/13 11:40
 * Bi业务异常类
 */
public class BiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private MsgCode msgCode;

    public BiException() {
        this.msgCode = MsgCode.SERVER_EXCEPTION;
    }

    public BiException(MsgCode msgCode) {
        super(msgCode.getMessage());
        this.msgCode = msgCode;
    }

    public BiException(MsgCode msgCode, String message) {
        super(message);
        this.msgCode = msgCode;
    }

    public MsgCode getMsgCode() {
        return msgCode;
    }
}