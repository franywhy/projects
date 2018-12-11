package io.renren.controller.manage;

import io.renren.common.doc.SysLog;
import io.renren.controller.AbstractController;
import io.renren.entity.manage.Headline;
import io.renren.pojo.manage.HeadlinePOJO;
import io.renren.service.manage.HeadlineService;
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
@RequestMapping("/headline")
public class HeadlineController extends AbstractController {

    @Autowired
    private HeadlineService headlineService;

    @RequestMapping("/headline.html")
    public String list(){
        return "headline/headline.html";
    }

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("headline:list")
    public R list(String title, HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        map.put("title", title);
        //查询列表数据
        List<HeadlinePOJO> headlineList = headlineService.queryPojoList(map);
        int total = headlineService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(headlineList, total , request);
        return R.ok().put(pageUtil);
    }


    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{headlineId}")
    @RequiresPermissions("headline:info")
    public R info(@PathVariable("headlineId") Long headlineId , HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        map.put("headlineId", headlineId);
        HeadlinePOJO headline = headlineService.queryPojoObject(map);
        return R.ok().put(headline);
    }

    /**
     * 保存
     */
    @SysLog("保存自定义卡片")
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions("headline:save")
    public R save(@RequestBody Headline headline , HttpServletRequest request){
        if(StringUtils.isBlank(headline.getTitle())){
            return R.error("[标题]不能为空！！！");
        }
        if(StringUtils.isBlank(headline.getSubtitle())){
            return R.error("[副标题]不能为空！！！");
        }
        if(StringUtils.isBlank(headline.getCardBannerUrl())){
            return R.error("[封面]不能为空！！！");
        }
        if(StringUtils.isBlank(headline.getLabels())){
            return R.error("[标签]不能为空！！！");
        }
        if(StringUtils.isBlank(""+headline.getProductId())){
            return R.error("[产品线]不能为空！！！");
        }
        if(StringUtils.isBlank(headline.getContentType()+"")){
            return R.error("[标签]不能为空！！！");
        }
        if(!StringUtils.isBlank(headline.getTitle()) && headline.getTitle().length() > 100){
            return R.error("[标题]不能超过100个字！！！");
        }
        //平台ID
        headline.setSchoolId(SchoolIdUtils.getSchoolId(request));
        //创建用户
        headline.setCreator(getUserId());
        //修改用户
        headline.setModifier(headline.getCreator());
        //保存
        headlineService.save(headline);
        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改自定义卡片")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("headline:update")
    public R update(@RequestBody Headline headline , HttpServletRequest request){
        if(StringUtils.isBlank(headline.getTitle())){
            return R.error("[标题]不能为空！！！");
        }
        if(StringUtils.isBlank(headline.getSubtitle())){
            return R.error("[副标题]不能为空！！！");
        }
        if(StringUtils.isBlank(headline.getCardBannerUrl())){
            return R.error("[封面]不能为空！！！");
        }
        if(StringUtils.isBlank(headline.getLabels())){
            return R.error("[标签]不能为空！！！");
        }
        if(StringUtils.isBlank(""+headline.getProductId())){
            return R.error("[产品线]不能为空！！！");
        }
        if(StringUtils.isBlank(headline.getContentType()+"")){
            return R.error("[标签]不能为空！！！");
        }
        if(!StringUtils.isBlank(headline.getTitle()) && headline.getTitle().length() > 100){
            return R.error("[标题]不能超过100个字！！！");
        }
        //平台ID
        headline.setSchoolId(SchoolIdUtils.getSchoolId(request));
        //修改用户
        headline.setModifier(getUserId());
        //修改时间
        headline.setModifiedTime(new Date());
        //修改
        headlineService.update(headline);
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除自定义卡片")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("headline:delete")
    public R delete(@RequestBody Long[] headlineIds , HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        map.put("list",headlineIds);
        headlineService.deleteBatch(map);
        return R.ok();
    }

    /**
     * 下架
     */
    @SysLog("下架自定义卡片")
    @ResponseBody
    @RequestMapping("/pause")
    @RequiresPermissions("headline:pause")
    public R pause(@RequestBody Long[] headlineIds){
        headlineService.pause(headlineIds);
        return R.ok();
    }

    /**
     * 上架
     */
    @SysLog("上架自定义卡片")
    @ResponseBody
    @RequestMapping("/resume")
    @RequiresPermissions("headline:resume")
    public R resume(@RequestBody Long[] headlineIds){
        headlineService.resume(headlineIds);
        return R.ok();
    }

}
