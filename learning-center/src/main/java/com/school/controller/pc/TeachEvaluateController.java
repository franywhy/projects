package com.school.controller.pc;

import com.school.controller.AbstractBaseController;
import com.school.entity.TeachEvaluate;
import com.school.pojo.UserInfoPOJO;
import com.school.service.TeachEvaluateService;
import com.school.web.model.WrappedResponse;
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
import javax.servlet.http.HttpServletResponse;

/**
 * @author hq
 */
@Controller
@RequestMapping("/learningCenter/teachEvaluate")
public class TeachEvaluateController extends AbstractBaseController {

	@Autowired
	private TeachEvaluateService teachEvaluateService;

	@ApiOperation(value = "直播，录播，回放评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "score", value = "综合评分", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "materialScore", value = "课程资料评分", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "contentScore", value = "教学内容评分", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "teachStyleScore", value = "教学风格评分", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "content", value = "评价内容", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "fileKey", value = "交付件关联KEY", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "stageCode", value = "阶段ID", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "topicId", value = "目标ID", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "topicType", value = "主题类型（直播、录播、课件、作业、预习视频）", required = true, dataType = "int", paramType = "query")
    })
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> add(HttpServletRequest request, HttpServletResponse response) {
		UserInfoPOJO userInfo = this.getUserInfo(request,response);
		int score = ServletRequestUtils.getIntParameter(request,"score",0);
		int materialScore = ServletRequestUtils.getIntParameter(request,"materialScore",0);
		int contentScore = ServletRequestUtils.getIntParameter(request,"contentScore",0);
		int teachStyleScore = ServletRequestUtils.getIntParameter(request,"teachStyleScore",0);
		String content = ServletRequestUtils.getStringParameter(request,"content","");
		String fileKey = ServletRequestUtils.getStringParameter(request,"fileKey","");
		int stageCode = ServletRequestUtils.getIntParameter(request,"stageCode",0);
		String topicId = ServletRequestUtils.getStringParameter(request,"topicId","");
		long topicType = ServletRequestUtils.getLongParameter(request,"topicType",0L);

		TeachEvaluate te = new TeachEvaluate();
		te.setScore(score);
		te.setContent(content);
		te.setUserId(userInfo.getUserId());
		te.setTopicId(topicId);
		te.setTopicType(topicType);
		te.setStageCode(stageCode);
		te.setFileKey(fileKey);
		te.setMaterialScore(materialScore);
		te.setContentScore(contentScore);
		te.setTeachStyleScore(teachStyleScore);

		teachEvaluateService.save(te);
		return this.success();
    }
}
