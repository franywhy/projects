package com.hq.bi.search.handler;

import com.hq.bi.search.constant.MsgCode;
import com.hq.bi.search.exception.BiException;
import com.hq.bi.search.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenzeke
 * @date 2018/12/13 11:38
 * BI公共异常处理类
 */
@ControllerAdvice
@Slf4j
public class BiExceptionHandler {
    /**
     * 全局异常捕捉处理
     *
     * @param e 通用异常
     * @return R
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return new R(MsgCode.SERVER_EXCEPTION);
    }

    /**
     * 业务处理类型异常
     *
     * @param e 业务异常
     * @return R
     */
    @ResponseBody
    @ExceptionHandler(value = BiException.class)
    public R hqImExceptionHandler(BiException e) {
        log.error(e.getMessage(), e);
        return new R<>(e.getMsgCode(), e.getMessage());
    }

    /**
     * 表单验证异常捕获
     *
     * @param e 表单验证异常
     * @return R
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public R jsonBindExceptionHandler(BindException e) {
        log.info(e.getMessage(), e);
        return new R(MsgCode.FORM_EXCEPTION.getCode(), e.getMessage());
    }
}
