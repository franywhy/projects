package io.renren.controller.manage;

import io.renren.common.doc.SysLog;
import io.renren.controller.AbstractController;
import io.renren.entity.manage.AppMessage;
import io.renren.pojo.manage.AppMessagePOJO;
import io.renren.service.manage.AppMessageService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 * @author Evan
 */
@Controller
@RequestMapping("/appmessage")
public class AppMessageController extends AbstractController {

    @Autowired
    private AppMessageService appMessageService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("appmessage:list")
    public R list(String title, HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        map.put("title", title);
        //查询列表数据
        List<AppMessagePOJO> list = appMessageService.queryPojoList(map);
        int total = appMessageService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(list, total , request);
        return R.ok().put(pageUtil);
    }


    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{messageId}")
    @RequiresPermissions("appmessage:info")
    public R info(@PathVariable("messageId") Long messageId , HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        map.put("messageId", messageId);
        AppMessagePOJO appMessage = appMessageService.queryPojoObject(map);
        return R.ok().put(appMessage);
    }

    /**
     * 保存
     */
    @SysLog("保存系统消息")
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions("appmessage:save")
    public R save(@RequestBody AppMessage appMessage , HttpServletRequest request){
        if(StringUtils.isBlank(appMessage.getTitle())){
            return R.error("[标题]不能为空！！！");
        }
       /* if(StringUtils.isBlank(appMessage.getPic())){
            return R.error("[封面]不能为空！！！");
        }*/
        if(StringUtils.isBlank(appMessage.getContentHtml())){
            return R.error("[内容]不能为空！！！");
        }   
        if(StringUtils.isBlank(""+appMessage.getProductId())){
            return R.error("[产品线]不能为空！！！");
        }
        if(!StringUtils.isBlank(appMessage.getTitle()) && appMessage.getTitle().length() > 100){
            return R.error("[标题]不能超过100个字！！！");
        }
        //平台ID
        appMessage.setSchoolId(SchoolIdUtils.getSchoolId(request));
        //创建用户
        appMessage.setCreatePerson(getUserId());
        //修改用户
        appMessage.setModifyPerson(appMessage.getCreatePerson());
        //保存
        appMessageService.save(appMessage);
        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改系统消息")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("appmessage:update")
    public R update(@RequestBody AppMessage appMessage , HttpServletRequest request){
        if(StringUtils.isBlank(appMessage.getTitle())){
            return R.error("[标题]不能为空！！！");
        }
        /*if(StringUtils.isBlank(appMessage.getPic())){
            return R.error("[封面]不能为空！！！");
        }*/
        if(StringUtils.isBlank(appMessage.getContentHtml())){
            return R.error("[内容]不能为空！！！");
        }
        if(StringUtils.isBlank(""+appMessage.getProductId())){
            return R.error("[产品线]不能为空！！！");
        }
        if(!StringUtils.isBlank(appMessage.getTitle()) && appMessage.getTitle().length() > 100){
            return R.error("[标题]不能超过100个字！！！");
        }
        //平台ID
        appMessage.setSchoolId(SchoolIdUtils.getSchoolId(request));
        //修改用户
        appMessage.setModifyPerson(getUserId());
        //修改时间
        appMessage.setModifyTime(new Date());
        //修改
        appMessageService.update(appMessage);
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除系统消息")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("appmessage:delete")
    public R delete(@RequestBody Long[] messageIds , HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        map.put("list",messageIds);
        appMessageService.deleteBatch(map);
        return R.ok();
    }
}
