package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.common.util.SchoolIdUtil;
import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.entity.AppGuideEntity;
import com.hq.learningapi.service.AppGuideService;
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
 * Created by Glenn on 2017/5/2 0002.
 */
@Controller
@RequestMapping("/api")
public class AppGuideController extends AbstractRestController {

    @Autowired
    private AppGuideService appGuideService;

    @Autowired
    private LocalConfigEntity config;

    @ApiOperation(value = "获取流程指南列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "areaId", value = "地区 ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "professionId", value = "专业 ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "levelId", value = "层次 ID", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/appGuide", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<List<AppGuideEntity>>> getAppGuide(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long areaId = ServletRequestUtils.getLongParameter(request, "areaId", -1);
            Long professionId = ServletRequestUtils.getLongParameter(request, "professionId", -1);
            Long levelId = ServletRequestUtils.getLongParameter(request, "levelId", -1);
            String schoolId = SchoolIdUtil.getSchoolId(request);
            TRACER.info(String.format("\nRequest Token:%s,\nRequest Area ID:%d,\nRequest Profession ID:%d,\nRequest Level ID:%d", token, areaId, professionId, levelId));
            if (areaId == -1 || professionId == -1 || levelId == -1) {
                return this.error("提交内容不可为空");
            } else {
                List<AppGuideEntity> list = appGuideService.queryList(professionId, areaId, levelId, config.getSchoolId());
                if (list == null) {
                    list = new ArrayList<AppGuideEntity>();
                } else {
                    for (AppGuideEntity entity : list) {
                        if (entity.getGuideUrl().indexOf("%s") != -1) {
                            entity.setGuideUrl(String.format(entity.getGuideUrl(), token));
                        }
                    }
                }
                return this.success(list);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取流程指南列表V1.2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "areaId", value = "地区 ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "professionId", value = "专业 ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "levelId", value = "层次 ID", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/appGuideV1_2", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<List<AppGuideEntity>>> getAppGuideV1_2(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long areaId = ServletRequestUtils.getLongParameter(request, "areaId", -1);
            Long professionId = ServletRequestUtils.getLongParameter(request, "professionId", -1);
            Long levelId = ServletRequestUtils.getLongParameter(request, "levelId", -1);
            String schoolId = SchoolIdUtil.getSchoolId(request);
            TRACER.info(String.format("\nRequest Token:%s,\nRequest Area ID:%d,\nRequest Profession ID:%d,\nRequest Level ID:%d", token, areaId, professionId, levelId));
            List<AppGuideEntity> list;
            if (areaId == -1 || professionId == -1 || levelId == -1) {
                list = appGuideService.queryList(schoolId);
            } else {
                list = appGuideService.queryList(professionId, areaId, levelId, config.getSchoolId());
            }
            if (list == null) {
                list = new ArrayList<AppGuideEntity>();
            } else {
                for (AppGuideEntity entity : list) {
                    if (entity.getGuideUrl().indexOf("%s") != -1) {
                        entity.setGuideUrl(String.format(entity.getGuideUrl(), token));
                    }
                }
            }
            return this.success(list);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

}