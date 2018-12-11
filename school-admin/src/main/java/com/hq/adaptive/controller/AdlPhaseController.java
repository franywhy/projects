package com.hq.adaptive.controller;

import com.hq.adaptive.pojo.AdlPhasePOJO;
import com.hq.adaptive.pojo.AdlPhaseSectionPOJO;
import com.hq.adaptive.pojo.query.AdlPhaseQuery;
import com.hq.adaptive.service.AdlPhaseService;
import io.renren.common.doc.SysLog;
import io.renren.common.validator.Assert;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/adaptive/adlphase")
public class AdlPhaseController {
    @Autowired
    private AdlPhaseService adlPhaseService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("adaptive:adlphase:list")
    public R list(HttpServletRequest request) throws ServletRequestBindingException {
        //课程ID
        Long courseId = ServletRequestUtils.getLongParameter(request, "courseId" , 0);
        if(courseId <= 0){
            throw new RRException("课程ID不能为空!");
        }
        AdlPhaseQuery adlPhaseQuery = new AdlPhaseQuery();
        adlPhaseQuery.setCourseId(courseId);
        adlPhaseQuery.initPage(request);
        //考点
        String keyPointString = ServletRequestUtils.getStringParameter(request, "keyPoint");
        if(StringUtils.isNotBlank(keyPointString)) {
            adlPhaseQuery.setKeyPoint(Integer.valueOf(keyPointString));
        }
        //名称
        String phaseName = ServletRequestUtils.getStringParameter(request, "phaseName");
        if(StringUtils.isNotBlank(phaseName)) {
            adlPhaseQuery.setPhaseName(phaseName);
        }
        //编号
        String phaseNo = ServletRequestUtils.getStringParameter(request, "phaseNo");
        if(StringUtils.isNotBlank(phaseNo)) {
            adlPhaseQuery.setPhaseNo(phaseNo);
        }

        List<AdlPhasePOJO> adlPhasePOJOList = this.adlPhaseService.queryList(adlPhaseQuery);
        int total = this.adlPhaseService.queryTotal(adlPhaseQuery);
        PageUtils pageUtil = new PageUtils(adlPhasePOJOList, total , request);
        return R.ok().put("data", pageUtil);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{phaseId}")
    public R info(@PathVariable("phaseId") Long phaseId){
        AdlPhasePOJO adlPhasePOJO = adlPhaseService.queryObject(phaseId);
        return R.ok().put(adlPhasePOJO);
    }

    @RequiresPermissions("courses:knowledge:edit")
    @SysLog("adl:新增阶段")
    @RequestMapping("/save")
    public R save(@RequestBody AdlPhasePOJO adlPhasePOJO ,HttpServletRequest request){
        this.verifyForm(adlPhasePOJO);
        this.adlPhaseService.save(adlPhasePOJO);
        return R.ok();
    }

    @RequiresPermissions("courses:knowledge:edit")
    @SysLog("adl:编辑阶段")
    @RequestMapping("/edit")
    public R edit(@RequestBody AdlPhasePOJO adlPhasePOJO ,HttpServletRequest request){
        this.verifyForm(adlPhasePOJO);
        this.adlPhaseService.update(adlPhasePOJO);
        return R.ok();
    }
    /**
     * 验证参数是否正确
     */
    private void verifyForm(AdlPhasePOJO adlPhasePOJO){
        Assert.isNull(adlPhasePOJO, "对象不能为空！");
        Assert.isBlank(adlPhasePOJO.getPhaseName(), "名称不能为空！");
        Assert.isBlank(adlPhasePOJO.getPhaseNo(), "编码不能为空！");
        Assert.isNull(adlPhasePOJO.getCourseId() , "课程不能为空!");
        Assert.isNull(adlPhasePOJO.getKeyPoint() , "考点不能为空!");
        if(CollectionUtils.isEmpty(adlPhasePOJO.getKnowledgeIdList())){
            throw new RRException("请选择阶段包含的知识点!");
        }
    }

    @RequiresPermissions("courses:knowledge:edit")
    @SysLog("adl:删除阶段")
    @RequestMapping("/delete/{phaseId}")
    public R delete(@PathVariable("phaseId") Long phaseId ,HttpServletRequest request){
        Assert.isNull(phaseId , "请选择阶段!");
        this.adlPhaseService.delete(phaseId);
        return R.ok();
    }

    @RequiresPermissions("courses:knowledge:edit")
    @SysLog("adl:启用阶段")
    @RequestMapping("/enablePhase/{phaseId}")
    public R enablePhase(@PathVariable("phaseId") Long phaseId ,HttpServletRequest request){
        Assert.isNull(phaseId , "请选择阶段!");
        this.adlPhaseService.enablePhase(phaseId);
        return R.ok();
    }

    @RequiresPermissions("courses:knowledge:edit")
    @SysLog("adl:禁用阶段")
    @RequestMapping("/disablePhase/{phaseId}")
    public R disablePhase(@PathVariable("phaseId") Long phaseId ,HttpServletRequest request){
        Assert.isNull(phaseId , "请选择阶段!");
        this.adlPhaseService.disablePhase(phaseId);
        return R.ok();
    }

    /**
     * 校验名称重复性
     * @param request
     * @return
     * @throws ServletRequestBindingException
     */
    @RequestMapping("/checkPhaseName")
    public R checkPhaseName(HttpServletRequest request) throws ServletRequestBindingException {
        //参数
        //阶段名称
        String phaseName = ServletRequestUtils.getStringParameter(request, "phaseName");
        //课程iD
        String courseIdString = ServletRequestUtils.getStringParameter(request, "courseId");
        //阶段ID
        String phaseIdString = ServletRequestUtils.getStringParameter(request, "phaseId");

        //校验参数
        Assert.isBlank(phaseName , "请输入名称!");
        Assert.isBlank(courseIdString , "请选择课程!");

        //课程ID
        Long courseId = Long.valueOf(courseIdString);
        Long phaseId = null;
        if(StringUtils.isNotBlank(phaseIdString)){
            phaseId = Long.valueOf(phaseIdString);
        }
        boolean result = this.adlPhaseService.checkPhaseName(courseId , phaseId , phaseName);
        return R.ok().put(result);
    }

    /**
     * 校验编号重复性
     * @param request
     * @return
     * @throws ServletRequestBindingException
     */
    @RequestMapping("/checkPhaseNo")
    public R checkPhaseNo(HttpServletRequest request) throws ServletRequestBindingException {
        //参数
        //阶段编号
        String phaseNo = ServletRequestUtils.getStringParameter(request, "phaseNo");
        //阶段ID
        String phaseIdString = ServletRequestUtils.getStringParameter(request, "phaseId");

        //校验参数
        Assert.isBlank(phaseNo , "请输入编号!");

        Long phaseId = null;
        if(StringUtils.isNotBlank(phaseIdString)){
            phaseId = Long.valueOf(phaseIdString);
        }
        boolean result = this.adlPhaseService.checkPhaseNo(phaseId , phaseNo);
        return R.ok().put(result);
    }

    @RequiresPermissions("courses:knowledge:edit")
    @SysLog("adl:阶段版本更新")
    @RequestMapping("/updatePhaceKnowledgeVersion/{phaseId}")
    public R updatePhaceKnowledgeVersion(@PathVariable("phaseId") Long phaseId ,HttpServletRequest request){
        boolean update = ServletRequestUtils.getBooleanParameter(request, "update", false);
        String s = this.adlPhaseService.updatePhaceKnowledgeVersion(phaseId , update);
        if(StringUtils.isNotBlank(s)){
            return R.error(s);
        }
        return R.ok();
    }

}
