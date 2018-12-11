package io.renren.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.entity.NcCommodityOpenCourseInfoEntity;
import io.renren.entity.SysUserEntity;
import io.renren.pojo.NcCommodityOpenCourseInfoPOJO;
import io.renren.service.NcCommodityOpenCourseInfoService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 报读班型与公开课权限关系表
 *
 * @date 2018-10-30 15:54:24
 */
@RestController
@RequestMapping("ncCommodityOpenCourseInfo")
public class NcCommodityOpenCourseInfoController extends AbstractController{
    @Autowired
    private NcCommodityOpenCourseInfoService ncCommodityOpenCourseInfoService;

    protected SysUserEntity getUser() {
        return ShiroUtils.getUserEntity();
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ncCommodityOpenCourseInfo:list")
    public R list(@RequestParam Map<String, Object> params,HttpServletRequest request){
        Map<String, Object> map = getMapPage(request);
        //按照条件查询
        stringQuery(map, request, "ncCommodityId");
        stringQuery(map, request, "authorityId");
        List<NcCommodityOpenCourseInfoPOJO>  ncCommodityOpenCourseInfoList= ncCommodityOpenCourseInfoService.queryList(map);
        int total = ncCommodityOpenCourseInfoService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(ncCommodityOpenCourseInfoList, total,request);
        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ncCommodityOpenCourseInfo:info")
    public R info(@PathVariable("id") Integer id){
		NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfo = ncCommodityOpenCourseInfoService.queryObject(id);
        return R.ok().put("ncCommodityOpenCourseInfo", ncCommodityOpenCourseInfo);
    }

    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/infoPOJO/{id}")
    @RequiresPermissions("ncCommodityOpenCourseInfo:infoPOJO")
    public R infoPOJO(@PathVariable("id") Integer id){
        NcCommodityOpenCourseInfoPOJO ncCommodityOpenCourseInfo = ncCommodityOpenCourseInfoService.queryPOJOObject(id);
        return R.ok().put("ncCommodityOpenCourseInfo", ncCommodityOpenCourseInfo);
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions("ncCommodityOpenCourseInfo:save")
    public R save(@RequestBody NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfo){
        if(StringUtils.isNotBlank(ncCommodityOpenCourseInfo.getNcCommodityId())){
            if(ncCommodityOpenCourseInfo.getNcCommodityId().length()>80){
                return R.error("NC报读班型长度超过限制");
            }
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("authorityId",ncCommodityOpenCourseInfo.getAuthorityId());
        params.put("ncCommodityId",ncCommodityOpenCourseInfo.getNcCommodityId());
        int total = ncCommodityOpenCourseInfoService.queryTotal(params);
        if(total > 0){
            return R.error("该报读班型已有该权限");
        }
        SysUserEntity sysUserEntity = getUser();
        ncCommodityOpenCourseInfo.setCreator(sysUserEntity.getUserId());
        ncCommodityOpenCourseInfo.setModifier(sysUserEntity.getUserId());
        ncCommodityOpenCourseInfoService.insert(ncCommodityOpenCourseInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ncCommodityOpenCourseInfo:update")
    public R update(@RequestBody NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfo){
        if(StringUtils.isNotBlank(ncCommodityOpenCourseInfo.getNcCommodityId())){
            if(ncCommodityOpenCourseInfo.getNcCommodityId().length()>80){
                return R.error("NC报读班型长度超过限制");
            }
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("authorityId",ncCommodityOpenCourseInfo.getAuthorityId());
        params.put("ncCommodityId",ncCommodityOpenCourseInfo.getNcCommodityId());
        int total = ncCommodityOpenCourseInfoService.queryTotal(params);
        if(total > 0){
            return R.error("该报读班型已有该权限");
        }
        SysUserEntity sysUserEntity = getUser();
        ncCommodityOpenCourseInfo.setModifier(sysUserEntity.getUserId());
		ncCommodityOpenCourseInfoService.update(ncCommodityOpenCourseInfo);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ncCommodityOpenCourseInfo:delete")
    public R delete(@RequestBody Long[] ids,HttpServletRequest request){
        Map<String, Object> map = getMap(request);
        map.put("ids",ids);
		ncCommodityOpenCourseInfoService.deleteBatch(map);
        return R.ok();
    }

}
