package com.hq.learningapi.controller.iap;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.pojo.MallGoodDetailPOJO;
import com.hq.learningapi.pojo.MallGoodPOJO;
import com.hq.learningapi.service.MallGoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/9/10.
 */
@Controller
@RequestMapping("/buy")
@Api(description = "ios内购获取商品信息")
public class MallGoodController extends AbstractRestController {
   private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MallGoodService mallGoodService;

    @ApiOperation(value = "ios内购获取商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getMallGoodList", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getMallGoodList(HttpServletRequest request) {
        Map<String,Object> map = new HashedMap();
        try {
            List<MallGoodPOJO> list = mallGoodService.getMallGoodList();
            if (list != null && list.size() > 0){
                map.put("goodList",list);
            }
            return this.success(map);
        } catch (Exception e) {
            logger.error("获取商品列表失败,cause:{}",e.toString());
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "ios内购获取商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodId", value = "商品id", required = true, dataType = "long", paramType = "query"),
    })
    @RequestMapping(value = "/getMallGoodDetail", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getMallGoodDetail(HttpServletRequest request) {
        long goodId = ServletRequestUtils.getLongParameter(request, "goodId", 0L);
        String token = ServletRequestUtils.getStringParameter(request, "token", "");
        try {
            MallGoodDetailPOJO detailPOJO = mallGoodService.getMallGoodDetail(request,token,goodId);
            return this.success(detailPOJO);
        } catch (Exception e) {
            logger.error("获取商品详情内容失败,cause:{}",e.toString());
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
