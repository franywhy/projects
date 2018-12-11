package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.entity.Comment;
import com.hq.learningapi.pojo.CommentPOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.CommentService;
import com.hq.learningapi.service.HeadlineService;
import com.hq.learningapi.util.SSOTokenUtils;
import com.hq.learningapi.util.SensitivewordEngine;
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * Created by Administrator on 2017/12/28 0028.
 * @author Evan
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractRestController {

    @Autowired
    private LocalConfigEntity config;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HeadlineService headlineService;
    
    @ApiOperation(value = "发表评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "commentObject", value = "评论对象ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "commentType", value = "评论类型  0：会计头条", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "评论内容", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> addComment(HttpServletRequest request) throws Exception {
    	String token = ServletRequestUtils.getStringParameter(request, "token", "");
        Long commentObject = ServletRequestUtils.getLongParameter(request, "commentObject", -1);
        int commentType = ServletRequestUtils.getIntParameter(request,"commentType",-1);
        String content = ServletRequestUtils.getStringParameter(request, "content", "");

        if (commentObject == -1) {
            return this.error("评论对象ID不可为空");
        }
        if(commentType == -1) {
            return this.error("评论类型不可为空");
        }
        if(StringUtils.isBlank(content)) {
            return this.error("评论内容不可为空");
        }
        UserInfoPOJO user = SSOTokenUtils.getUserInfo(request, token);
        //敏感词过滤
        content = SensitivewordEngine.replaceSensitiveWord(content,SensitivewordEngine.maxMatchType,"*");
        Comment comment = new Comment();
        comment.setCommentObject(commentObject);
        comment.setCommentType(commentType);
        comment.setContent(content);
        comment.setUserId(user.getUserId());
        comment.setName(user.getNickName());
        comment.setAvatar(user.getAvatar());
        commentService.save(comment);
        if(0 == commentType) {
            headlineService.addCommentNumber(commentObject);
        }
        return this.success("发表成功");
    }

    @ApiOperation(value = "获取评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "commentObject", value = "评论对象ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "commentType", value = "评论类型  0：会计头条", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", defaultValue="1", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "int", defaultValue="20", paramType = "query")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<List<CommentPOJO>>> list(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Long commentObject = ServletRequestUtils.getLongParameter(request, "commentObject", -1);
            int commentType = ServletRequestUtils.getIntParameter(request,"commentType",-1);
            int page = ServletRequestUtils.getIntParameter(request, "page", 1);
            int size = ServletRequestUtils.getIntParameter(request, "size", 20);

            if (commentObject == -1) {
                return this.error("评论对象ID不可为空");
            }
            if(commentType == -1) {
                return this.error("评论类型不可为空");
            }
            Long userId = -1L;
            if(StringUtils.isNotBlank(token)) {
                UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request,token);
                userId = userInfo.getUserId();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("userId",userId);
            map.put("commentObject", commentObject);
            map.put("commentType", commentType);
            map.put("limit", size);
            map.put("offset", (page - 1) * size);
            List<CommentPOJO> list = commentService.queryPojoList(map);

            if (list == null) {
                list = new ArrayList<>();
            }
            return this.success(list);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateSensitiveWord", method = RequestMethod.GET)
    public String updateSensitiveWord() throws Exception{
        URL url = new URL(config.getSensitiveWordUrl());
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"GBK"));

        Set<String> keyWordSet = new HashSet<>();
        String s;
        while ((s = reader.readLine()) != null) {
            keyWordSet.add(s.trim());
        }
        reader.close();
        return SensitivewordEngine.updateSensitiveWord(keyWordSet);
    }
}
