package com.hq.learningcenter.school.controller.pc;

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.entity.LogWatchRecordEntity;
import com.hq.learningcenter.school.pojo.CourseRecordDetailPOJO;
import com.hq.learningcenter.school.service.LogWatchRecordService;
import com.hq.learningcenter.school.service.RecordService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping("/learningCenter/web")
public class recordController extends AbstractBaseController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private LogWatchRecordService logWatchRecordService;

    @ApiOperation(value = "获取录播课播放地址/记录录播课播放日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "recordId", value = "录播课Id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/record/play", method = RequestMethod.GET)
    public ModelAndView getRecordUrl(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        String recordIdStr = request.getParameter("recordId");
        Long recordId = Long.parseLong(recordIdStr);
        try {
            int logWatchRecordNum = this.logWatchRecordService.queryRecordNum(recordId, userInfo.getUserId());

            LogWatchRecordEntity logWatchRecordEntity = new LogWatchRecordEntity();
            if (logWatchRecordNum > 0) {
                logWatchRecordEntity.setUserId(userInfo.getUserId());
                logWatchRecordEntity.setRecordId(recordId);
                logWatchRecordEntity.setTs(new Date());
                logWatchRecordService.update(logWatchRecordEntity);
            } else {
                logWatchRecordEntity.setUserId(userInfo.getUserId());
                logWatchRecordEntity.setRecordId(recordId);
                logWatchRecordEntity.setAttend(1);
                logWatchRecordEntity.setTs(new Date());
                logWatchRecordService.save(logWatchRecordEntity);
            }
            ModelAndView mav = this.createModelAndView(request, response, true);
            CourseRecordDetailPOJO courseRecordDetail = this.recordService.getRecordInfo(recordId);

            if (null != courseRecordDetail) {
                if (null != courseRecordDetail.getVid() && !courseRecordDetail.getVid().equals("")) {
                    mav.setViewName("common/polyVideo");

                    if (null != courseRecordDetail) {
                        mav.addObject("redirectUrl", courseRecordDetail.getVid());
                        mav.addObject("param1", userInfo.getUserId());
                        mav.addObject("param2", courseRecordDetail.getRecordId());
                    }
                    Long endTime = System.currentTimeMillis();
                    logger.info("/learningCenter/web/record/play" + (endTime - startTime) + "ms");
                    if ((endTime - startTime) > 1000) {
                        logger.error("/learningCenter/web/record/play " + (endTime - startTime) + "ms");
                    }
                    return mav;
                } else if (null != courseRecordDetail.getCcId() && !courseRecordDetail.getCcId().equals("")) {
                    String ccUrl = "http://p.bokecc.com/flash/player.swf?vid=" + courseRecordDetail.getCcId() + "&siteid=07552B247EACAED4&playerid=55295D704B531A0D&playertype=1&amp;autoStart=true";
                    response.sendRedirect(ccUrl);
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return this.createModelAndView(request, response, true);
        }
    }

}
