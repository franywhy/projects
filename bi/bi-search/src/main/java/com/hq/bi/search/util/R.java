package com.hq.bi.search.util;

import com.hq.bi.search.constant.MsgCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author chenzeke
 * @date 2018/12/6 17:44
 * 响应实体，映射成JSON
 */
@ApiModel(value = "响应实体")
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {
    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码", name = "code")
    private Integer code;
    /**
     * 提示消息
     */
    @ApiModelProperty(value = "提示消息", name = "msg")
    private String msg;
    /**
     * 实体数据
     */
    @ApiModelProperty(value = "实体数据", name = "data")
    private T data;

    public R() {
        this.code = MsgCode.SUCCESS.getCode();
        this.msg = MsgCode.SUCCESS.getMessage();
    }

    public R(T data) {
        this.data = data;
        this.code = MsgCode.SUCCESS.getCode();
        this.msg = MsgCode.SUCCESS.getMessage();
    }

    public R(T data, String msg) {
        this.data = data;
        this.code = MsgCode.SUCCESS.getCode();
        this.msg = msg;
    }

    public R(MsgCode msgCode, String msg) {
        this.code = msgCode.getCode();
        this.msg = msg;
    }

    public R(T data, MsgCode msgCode) {
        this.data = data;
        this.code = msgCode.getCode();
        this.msg = msgCode.getMessage();
    }

    public R(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public R(MsgCode msgCode) {
        this.code = msgCode.getCode();
        this.msg = msgCode.getMessage();
    }
}
