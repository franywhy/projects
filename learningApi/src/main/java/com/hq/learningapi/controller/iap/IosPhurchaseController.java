package com.hq.learningapi.controller.iap;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.service.PhurchaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * ios购买商品
 * Created by DL on 2018/9/11.
 */
@Controller
@RequestMapping("/buy")
@Api(description = "ios内购购买接口")
public class IosPhurchaseController extends AbstractRestController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PhurchaseService phurchaseService;

    @ApiOperation(value = "ios内购点击购买接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodId", value = "商品id", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "ncId", value = "商品ncId", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "商品价格", required = true, dataType = "dubble", paramType = "query")
    })
    @RequestMapping(value = "/phurchaseOrder", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> getMallGoodList(HttpServletRequest request) {
        String token = ServletRequestUtils.getStringParameter(request, "token", "");
        String ncId = ServletRequestUtils.getStringParameter(request, "ncId", "");
        Long goodId = ServletRequestUtils.getLongParameter(request, "goodId", 0L);
        double price = ServletRequestUtils.getDoubleParameter(request, "price", 0d);
        try {
            String message = phurchaseService.phurchase(request,token,ncId,goodId,price);
            if ( message != null){
                return this.error(message);
            }
            return this.success(TransactionStatus.OK);
        } catch (Exception e) {
            logger.error("订单生成失败,cause:{}",e.toString());
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
