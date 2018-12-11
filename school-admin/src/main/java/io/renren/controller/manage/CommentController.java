package io.renren.controller.manage;

import io.renren.common.doc.SysLog;
import io.renren.controller.AbstractController;
import io.renren.pojo.manage.CommentPOJO;
import io.renren.service.manage.CommentService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

    @Autowired
    private CommentService commentService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("comment:list")
    public R list(String content, String appStatus, HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        map.put("content",content);
        map.put("appStatus",appStatus);
        //查询列表数据
        List<CommentPOJO> customCardList = commentService.queryPojoList(map);
        int total = commentService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(customCardList, total , request);
        return R.ok().put(pageUtil);
    }

    /**
     * 删除
     */
    @SysLog("删除评论")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("comment:delete")
    public R delete(@RequestBody Long[] commentId , HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        map.put("list",commentId);
        commentService.deleteBatch(map);
        return R.ok();
    }

    /**
     * 下架
     */
    @SysLog("下架评论")
    @ResponseBody
    @RequestMapping("/pause")
    @RequiresPermissions("comment:pause")
    public R pause(@RequestBody Long[] commentIds){
        commentService.pause(commentIds);
        return R.ok();
    }

    /**
     * 上架
     */
    @SysLog("上架评论")
    @ResponseBody
    @RequestMapping("/resume")
    @RequiresPermissions("comment:resume")
    public R resume(@RequestBody Long[] commentIds){
        commentService.resume(commentIds);
        return R.ok();
    }

}
