package com.hq.learningcenter.school.controller.pc;

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import com.hq.learningcenter.school.service.RecordCourseFileService;
import com.hq.learningcenter.utils.SSOTokenUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/learningCenter/web")
public class RecordCourseFileController extends AbstractBaseController {

    @Autowired
    private RecordCourseFileService recordCourseFileService;

    @ApiOperation(value = "录播课资料下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "recordId", value = "录播课程的ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "fileType", value = "资料类型", required = true, dataType = "int", paramType = "query"),
    })
    @RequestMapping(value = "/getRecordCourseFile", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> getRecordFileList(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            int recordId = ServletRequestUtils.getIntParameter(request, "recordId", -1);
            int fileType = ServletRequestUtils.getIntParameter(request, "fileType", -1);
            logger.info(String.format("\nRequest Token:%s", token));
            Long userId;
            if (StringUtils.isNotBlank(token)) {
                UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
                userId = userInfo.getUserId() == null ? null : userInfo.getUserId();
            } else {
                userId = 0L;
            }
            if (null == userId) {
                return this.error("登陆信息失效，请用户重新登陆！:401");
            }
            List<Map<String, Object>> fileList = recordCourseFileService.getRecordFileList(recordId, fileType);
            return this.success(fileList);
        } catch (Throwable t) {
            logger.error("\nUnKnown Error", t);
            return this.error("服务器内部错误！");
        }
    }

    }

