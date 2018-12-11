package io.renren.controller;

import com.alibaba.fastjson.JSONObject;
import io.renren.entity.LogLabelBonusRelationRecordEntity;
import io.renren.entity.SysUserEntity;
import io.renren.entity.SysUserLaberEntity;
import io.renren.pojo.SysUserLaberPOJO;
import io.renren.pojo.manage.TipBonusRelationPOJO;
import io.renren.service.LogLabelBonusRelationRecordService;
import io.renren.service.SysUserLaberService;
import io.renren.service.TipBonusRelationService;
import io.renren.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 标签红包管理
 *
 * @author vince
 * @date 2018-08-27 17:01:37
 */
@RestController
@RequestMapping("/manage/tipBonusRelation")
public class TipBonusRelationController extends AbstractController {
    @Autowired
    private TipBonusRelationService tipBonusRelationService;


    protected SysUserEntity getUser() {
        return ShiroUtils.getUserEntity();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("tipBonusRelation:list")
    public R list(HttpServletRequest request) {

        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "labelName");

        List<Object> tipBonusRelationList = tipBonusRelationService.queryListObject(map);

        int total = tipBonusRelationService.queryTotal(map);;
        PageUtils pageUtil = new PageUtils(tipBonusRelationList, total, request);

        return R.ok().put("data", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("tipBonusRelation:info")
    public R info(@PathVariable("id") Long id) {
        Object tipBonusRelation = tipBonusRelationService.queryObject(id);

        return R.ok().put("tipBonusRelation", tipBonusRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("tipBonusRelation:save")
    public R save(@RequestBody Object tipBonusRelationPOJO) {
        Object laberId=((JSONObject) tipBonusRelationPOJO).get("laberId");
        Long laberIdL=Long.valueOf(String.valueOf(laberId));
        if(!tipBonusRelationService.queryPower(laberIdL)){
            return R.ok().put("data", "很抱歉，你无权限新增该标签的红包金额");
        }
        if(null==tipBonusRelationService.queryObject(laberIdL) || tipBonusRelationService.queryObject(laberIdL)==""){}else {
            return R.ok().put("data", "已有标签无法重复添加");
        }

        Map<String, Object> map=JSONUtil.beanToMap(tipBonusRelationPOJO);

        tipBonusRelationService.save(map);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("tipBonusRelation:update")
    public R update(@RequestBody Object tipBonusRelationPOJO) {


        Object modifyReason=((JSONObject) tipBonusRelationPOJO).get("modifyReason");
        Object laberId=((JSONObject) tipBonusRelationPOJO).get("laberId");
        Long laberIdL=Long.valueOf(String.valueOf(laberId));
        if(!tipBonusRelationService.queryPower(laberIdL)){
            return R.ok().put("data", "很抱歉，你无权限修改该标签的红包金额");
        }
        if(null==modifyReason || modifyReason==""){
            return R.ok().put("data", "修改原因不能为空");
        }
        Map<String, Object> map=JSONUtil.beanToMap(tipBonusRelationPOJO);

        tipBonusRelationService.update(map);
        return R.ok();
    }


}
