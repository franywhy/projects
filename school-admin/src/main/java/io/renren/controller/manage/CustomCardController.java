package io.renren.controller.manage;

import io.renren.common.doc.SysLog;
import io.renren.controller.AbstractController;
import io.renren.entity.manage.CustomCard;
import io.renren.pojo.manage.CustomCardPOJO;
import io.renren.service.manage.CustomCardService;
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
 */
@Controller
@RequestMapping("/customcard")
public class CustomCardController extends AbstractController {

    @Autowired
    private CustomCardService customCardService;

    @RequestMapping("/customcard.html")
    public String list(){
        return "customcard/customcard.html";
    }

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("customcard:list")
    public R list(String title, HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        map.put("title", title);
        //查询列表数据
        List<CustomCardPOJO> customCardList = customCardService.queryPojoList(map);
        int total = customCardService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(customCardList, total , request);
        return R.ok().put(pageUtil);
    }


    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{cardId}")
    @RequiresPermissions("customcard:info")
    public R info(@PathVariable("cardId") Long cardId , HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        map.put("cardId", cardId);
        CustomCardPOJO customCard = customCardService.queryPojoObject(map);
        return R.ok().put(customCard);
    }

    /**
     * 保存
     */
    @SysLog("保存自定义卡片")
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions("customcard:save")
    public R save(@RequestBody CustomCard customCard , HttpServletRequest request){
        if(StringUtils.isBlank(customCard.getTitle())){
            return R.error("[标题]不能为空！！！");
        }
        if(StringUtils.isBlank(customCard.getSubtitle())){
            return R.error("[副标题]不能为空！！！");
        }
        if(StringUtils.isBlank(customCard.getCardUrl())){
            return R.error("[卡片地址]不能为空！！！");
        }
        if(StringUtils.isBlank(customCard.getCardBannerUrl())){
            return R.error("[封面]不能为空！！！");
        }
        if(StringUtils.isBlank(""+customCard.getProductId())){
            return R.error("[产品线]不能为空！！！");
        }
        if(StringUtils.isBlank(""+customCard.getCanShare())){
            return R.error("[是否可分享]不能为空！！！");
        }
        if(!StringUtils.isBlank(customCard.getTitle()) && customCard.getTitle().length() > 100){
            return R.error("[标题]不能超过100个字！！！");
        }
        //平台ID
        customCard.setSchoolId(SchoolIdUtils.getSchoolId(request));
        //创建用户
        customCard.setCreator(getUserId());
        //修改用户
        customCard.setModifier(customCard.getCreator());
        //保存
        customCardService.save(customCard);
        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改自定义卡片")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("customcard:update")
    public R update(@RequestBody CustomCard customCard , HttpServletRequest request){
        if(StringUtils.isBlank(customCard.getTitle())){
            return R.error("[标题]不能为空！！！");
        }
        if(StringUtils.isBlank(customCard.getSubtitle())){
            return R.error("[副标题]不能为空！！！");
        }
        if(StringUtils.isBlank(customCard.getCardUrl())){
            return R.error("[卡片地址]不能为空！！！");
        }
        if(StringUtils.isBlank(customCard.getCardBannerUrl())){
            return R.error("[封面]不能为空！！！");
        }
        if(StringUtils.isBlank(""+customCard.getProductId())){
            return R.error("[产品线]不能为空！！！");
        }
        if(StringUtils.isBlank(""+customCard.getCanShare())){
            return R.error("[是否可分享]不能为空！！！");
        }
        if(!StringUtils.isBlank(customCard.getTitle()) && customCard.getTitle().length() > 100){
            return R.error("[标题]不能超过100个字！！！");
        }
        //平台ID
        customCard.setSchoolId(SchoolIdUtils.getSchoolId(request));
        //修改用户
        customCard.setModifier(getUserId());
        //修改时间
        customCard.setModifiedTime(new Date());
        //修改
        customCardService.update(customCard);
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除自定义卡片")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("customcard:delete")
    public R delete(@RequestBody Long[] cardIds , HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        map.put("list",cardIds);
        customCardService.deleteBatch(map);
        return R.ok();
    }

    /**
     * 下架
     */
    @SysLog("下架自定义卡片")
    @ResponseBody
    @RequestMapping("/pause")
    @RequiresPermissions("customcard:pause")
    public R pause(@RequestBody Long[] cardIds){
        customCardService.pause(cardIds);
        return R.ok();
    }

    /**
     * 上架
     */
    @SysLog("上架自定义卡片")
    @ResponseBody
    @RequestMapping("/resume")
    @RequiresPermissions("customcard:resume")
    public R resume(@RequestBody Long[] cardIds){
        customCardService.resume(cardIds);
        return R.ok();
    }
    
}
