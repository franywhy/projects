package io.renren.controller.manage;

import io.renren.common.doc.SysLog;
import io.renren.controller.AbstractController;
import io.renren.service.manage.ActivityJoinInfoService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/7 0007.
 * @author Evan
 */
@Controller
@RequestMapping("/activityjoininfo")
public class ActivityJoinInfoController extends AbstractController {

    @Autowired
    private ActivityJoinInfoService activityJoinInfoService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("activityjoininfo:list")
    public R list(HttpServletRequest request){
        PageUtils pageUtil = activityJoinInfoService.findPage(request);
        return R.ok().put(pageUtil);
    }

    /**
     * 修改
     */
    @SysLog("修改活动参与信息")
    @ResponseBody
    @RequestMapping("/updateStatus")
    @RequiresPermissions("activityjoininfo:update")
    public R updateStatus(@RequestBody int[] ids, int status, HttpServletRequest request){
        activityJoinInfoService.updateStatusById(ids, status);
        return R.ok();
    }
}
