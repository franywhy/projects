package io.renren.controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseClassplanChangeRecordEntity;
import io.renren.entity.CourseClassplanEntity;
import io.renren.entity.CourseClassplanLivesChangeRecordEntity;
import io.renren.pojo.classplan.ClassplanLivePOJO;
import io.renren.pojo.classplan.ClassplanPOJO;
import io.renren.service.CourseClassplanChangeRecordService;
import io.renren.service.CourseClassplanLivesChangeRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/8/23.
 */
@Controller
@RequestMapping("/course/classplanChangeRecord")
public class CourseClassplanChangeRecordController extends AbstractController{

    @Autowired
    private CourseClassplanChangeRecordService courseClassplanChangeRecordService;
    @Autowired
    private CourseClassplanLivesChangeRecordService courseClassplanLivesChangeRecordService;
    /**
     * 主表列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("course:courseclassplanChangeRecord:list")
    public R list(String classplanName, Integer page, Integer limit , HttpServletRequest request){
        String schoolId = SchoolIdUtils.getSchoolId(request);
        Map<String, Object> map = new HashMap<>();
        map.put("classplanName", classplanName);
        map.put("schoolId", schoolId);
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);
        //查询列表数据
        List<CourseClassplanChangeRecordEntity> courseClassplanChangeRecordList = courseClassplanChangeRecordService.queryList(map);
        int total = courseClassplanChangeRecordService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(courseClassplanChangeRecordList, total, limit, page);
        return R.ok().put("page", pageUtil);
    }

    /**
     * 信息
     */
    @ResponseBody
    @RequestMapping("/info/{classplanId}/{versionNo}")
    @RequiresPermissions("course:courseclassplanChangeRecord:info")
    public R info(@PathVariable("classplanId") String classplanId,@PathVariable("versionNo")String versionNo, HttpServletRequest request){
        String schoolId = SchoolIdUtils.getSchoolId(request);
        //查询主表
        Map<String , Object> map1 = new HashMap<String , Object>();
        map1.put("classplanId", classplanId);
        map1.put("schoolId", schoolId);
        map1.put("versionNo", versionNo);
        CourseClassplanChangeRecordEntity changeRecordEntity = courseClassplanChangeRecordService.queryObject1(map1);
        //查询子表
        Map<String , Object> map2 = new HashMap<String , Object>();
        map2.put("classplanId", classplanId);
        map2.put("schoolId", schoolId);
        if (changeRecordEntity != null){
            map2.put("versionNo", changeRecordEntity.getVersionNo());
        }
        List<CourseClassplanLivesChangeRecordEntity> detailList = courseClassplanLivesChangeRecordService.queryPojoList(map2);
        return R.ok().put("courseClassplan", changeRecordEntity).put("detailList", detailList);
    }

    /**
     * 审核通过
     * */
    @SysLog("审核通过排课计划")
    @ResponseBody
    @RequestMapping("/accept/{classplanId}/{versionNo}")
    //@RequiresPermissions("course:classplan:accept")
    public R accept(@PathVariable("classplanId") String classplanId,@PathVariable("versionNo")String versionNo){
        Long userId = getUserId();
        courseClassplanChangeRecordService.accept(classplanId,Integer.parseInt(versionNo),userId);
        return R.ok();
    }
    /**
     * 审核未过
     * */
    @SysLog("审核未过排课计划")
    @ResponseBody
    @RequestMapping("/reject/{classplanId}/{versionNo}")
    //@RequiresPermissions("course:classplan:reject")
    public R reject(@PathVariable("classplanId") String classplanId,@PathVariable("versionNo")String versionNo){
        Long userId = getUserId();
        courseClassplanChangeRecordService.reject(classplanId,Integer.parseInt(versionNo),userId);
        return R.ok();
    }
}
