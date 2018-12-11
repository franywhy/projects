package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.common.util.SchoolIdUtil;
import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.entity.AppBannerEntity;
import com.hq.learningapi.service.AppBannerService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glenn on 2017/4/27 0027.
 */

@Controller
@RequestMapping("/api")
public class AppBannerController extends AbstractRestController {

    @Autowired
    private AppBannerService appBannerService;

    @Autowired
    private LocalConfigEntity config;

    @ApiOperation(value = "获取banner列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "levelId", value = "层次 ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "professionId", value = "专业 ID", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/banner", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<List<AppBannerEntity>>> getBanner(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long levelId = ServletRequestUtils.getLongParameter(request, "levelId", -1);
            Long professionId = ServletRequestUtils.getLongParameter(request, "professionId", -1);
            TRACER.info(String.format("\nRequest Token:%s,\nRequest Level ID:%d,\nRequest Profession ID:%d", token, levelId, professionId));
            if (levelId == -1 || professionId == -1) {
                return this.error("提交内容不可为空");
            } else {
                String schoolId = SchoolIdUtil.getSchoolId(request);
                List<AppBannerEntity> list = appBannerService.queryList(levelId, professionId, config.getSchoolId());
                if (list == null) {
                    list = new ArrayList<AppBannerEntity>();
                }
                return this.success(list);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取banner列表V1.2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "levelId", value = "层次 ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "professionId", value = "专业 ID", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/bannerV1_2", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<List<AppBannerEntity>>> getBannerV1_2(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long levelId = ServletRequestUtils.getLongParameter(request, "levelId", -1);
            Long professionId = ServletRequestUtils.getLongParameter(request, "professionId", -1);
            TRACER.info(String.format("\nRequest Token:%s,\nRequest Level ID:%d,\nRequest Profession ID:%d", token, levelId, professionId));
            List<AppBannerEntity> list;
            String schoolId = SchoolIdUtil.getSchoolId(request);
            if (levelId == -1 || professionId == -1) {
                list = appBannerService.queryList(0L, 0L, config.getSchoolId());
            } else {
                list = appBannerService.queryList(levelId, professionId, config.getSchoolId());
            }
            if (list == null) {
                list = new ArrayList<>();
            }
            return this.success(list);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
