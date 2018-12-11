package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.entity.ColdStartingEntity;
import com.hq.learningapi.service.ColdStartingService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 冷启动广告页
 * Created by DL on 2018/1/2.
 */

@Controller
@RequestMapping("/api")
public class ColdStartingController extends AbstractRestController {

    @Autowired
    private ColdStartingService coldStartingService;

    /**
     * 获取所有广告页列表
     * @date 2017年8月22日
     * @param @param request
     */
    @ApiOperation(value = "获取所有广告页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "getColdStartingList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<WrappedResponse<List<ColdStartingEntity>>> getColdStartingList(HttpServletRequest request){
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            List<ColdStartingEntity> list = coldStartingService.getColdStartingList();
            return this.success(list);
        }catch (Throwable t){
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 获取最新广告页面
     * @date 2017年8月22日
     * @param @param request
     */
    @ApiOperation(value = "获取最新广告页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "getColdStartingLatest", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<WrappedResponse<ColdStartingEntity>> getLatestColdStarting(HttpServletRequest request){
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            ColdStartingEntity coldStarting = coldStartingService.getLatestColdStarting();
            if (coldStarting != null){
                String url = coldStarting.getUrl();
                if (url != null && url.contains("?")){
                    url = coldStarting.getUrl()+"&token="+token;
                    coldStarting.setUrl(url);
                }
                if (url != null && !url.contains("?")){
                    url = coldStarting.getUrl()+"?token="+token;
                    coldStarting.setUrl(url);
                }
                return this.success(coldStarting);
            }
           return this.success(coldStarting);
            
        }catch (Throwable t){
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
