package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.APPCourseLiveFileService;
import com.hq.learningapi.util.SSOTokenUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class APPCourseLiveFileConrtroller extends AbstractRestController {

    @Autowired
    private APPCourseLiveFileService appCourseLiveFileService;


    @ApiOperation(value = "获取今日卡片中的课程资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classplanLiveId", value = "直播课程的ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "fileType", value = "资料类型", required = true, dataType = "int", paramType = "query"),
    })
    @RequestMapping(value = "/courseLiveFile", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> getCourseLiveFile(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            String classplanLiveId = ServletRequestUtils.getStringParameter(request, "classplanLiveId", "");
            int fileType = ServletRequestUtils.getIntParameter(request, "fileType", -1);
            TRACER.info(String.format("\nRequest Token:%s", token));
            Long userId;
            if (StringUtils.isNotBlank(token)) {
                UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
                userId = userInfo.getUserId() == null ? null : userInfo.getUserId();
            } else {
                userId = 0L;
            }
            if (null == userId) {
                return this.error(TransactionStatus.USER_TOKEN_NOT_FOUND);
            }
            List<Map<String, Object>> fileList = appCourseLiveFileService.getFileList(classplanLiveId, fileType);
            return this.success(fileList);
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
            return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
