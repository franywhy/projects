package com.hq.bi.search.controller;

import com.hq.bi.search.constant.MsgCode;
import com.hq.bi.search.exception.BiException;
import com.hq.bi.search.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenzeke
 * @date 2018/12/13 11:47
 */
@RestController
@RequestMapping("/test")
@Slf4j
@Api(tags = "测试控制器-TestController")
public class TestController {

    @GetMapping("/testException")
    @ApiOperation(value = "测试")
    @ApiImplicitParam(name = "i", value = "根据i值调用同异常", example = "0", type = "path")
    public R testException(int i) {
        //异常捕获demo
        if (i == 0) {
            //不捕获，根据抛出的异常类找对应的异常处理类，ArithmeticException找不到使用通用异常类处理
            int a = 10 / 0;
        } else if (i == 1) {
            //捕获异常
            try {
                int b = 10 / 0;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                //抛出业务异常，参数不能为空
                throw new BiException(MsgCode.NOT_NULL);
            }
        } else if (i == 2) {
            //捕获异常，自定义提示语与code，以ServerException为例子
            try {
                int c = 10 / 0;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BiException(MsgCode.SERVER_EXCEPTION, "除0异常");
            }
        } else if (i == 3) {
            //捕获异常，自定义提示语与code，以SEND_MAX_100为例子,code不一样，msg不一样
            try {
                int d = 10 / 0;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BiException(MsgCode.FORM_EXCEPTION, "除0异常");
            }
        } else if (i == 4) {
            //也可以不抛出异常，直接返回异常信息,以SERVER_EXCEPTION为例
        }
        return new R<>(MsgCode.SERVER_EXCEPTION);
    }


}
