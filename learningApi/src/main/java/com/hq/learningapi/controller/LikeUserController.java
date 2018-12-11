package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.entity.LikeUser;
import com.hq.learningapi.pojo.HeadlinePOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.CommentService;
import com.hq.learningapi.service.LikeUserService;
import com.hq.learningapi.util.SSOTokenUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 * @author Evan
 */
@Controller
@RequestMapping("/like")
public class LikeUserController extends AbstractRestController {

    @Autowired
    private LikeUserService likeUserService;
   
    @Autowired
    private CommentService commentService;
    
    @ApiOperation(value = "点赞/取消")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "likeObject", value = "点赞对象ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "likeType", value = "点赞类型  0：评论，1：观点PK-支持，2：观点PK-反对", required = true, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/addCancel", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<List<HeadlinePOJO>>> addCancel(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request,"token","");
            Long likeObject = ServletRequestUtils.getLongParameter(request,"likeObject",-1);
            int likeType = ServletRequestUtils.getIntParameter(request, "likeType", -1);

            if (likeObject == -1) {
                return this.error("点赞对象ID不可为空");
            }
            if (likeType == -1) {
                return this.error("点赞类型不可为空");
            }
            Map<String,Object> result = new HashMap<>();
            UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
            Long userId = userInfo.getUserId();
            boolean likeUserExist = likeUserService.isLikeUserExist(likeObject,userId,likeType);
            if(!likeUserExist){
                LikeUser lu = new LikeUser();
                lu.setLikeObject(likeObject);
                lu.setUserId(userId);
                lu.setLikeType(likeType);
                likeUserService.save(lu);
                if(0 == likeType) {
                    commentService.addLikeNumber(likeObject);
                }
                result.put("isLike",1);
            }else{
                Map<String,Object> map = new HashMap<>();
                map.put("likeObject", likeObject);
                map.put("userId", userId);
                map.put("likeType", likeType);
                likeUserService.remove(map);
                if(0 == likeType) {
                    commentService.delLikeNumber(likeObject);
                }
                result.put("isLike",0);
            }
            result.put("total",likeUserService.queryTotal(likeObject,likeType));
            return this.success(result);
        } catch (Throwable t) {
            TRACER.error("服务器内部问题", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }		
    }

}
