package com.hq.bi.search.controller;

import com.hq.bi.search.entity.ClassExamQualityEntity;
import com.hq.bi.search.service.ClassExamQualityService;
import com.hq.bi.search.util.PageInfo;
import com.hq.bi.search.util.PageQuery;
import com.hq.bi.search.util.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:班级考试质量评比报表控制器
 * Autor: wl
 * Date: 2018-12-15
 */
@RestController
@RequestMapping("/classExamQuality")
@Slf4j
@Api(tags = "报表查询-ClassExamQualityController")
public class ClassExamQualityController {

    @Autowired
    private ClassExamQualityService classExamQualityService;

    /**
     * 班级考试质量评比报表接口
     * @param map
     * @return
     */
    @PostMapping(value = "/getClassExamQualityList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R getClassExamQualityList(@RequestBody Map<String, Object> map){
        HashMap<String, Object>pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("pageNum"));
        pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("pageSize"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        PageInfo<ClassExamQualityEntity> classExamQualityList = new PageInfo<>(classExamQualityService.getClassExamQualityList(PageQuery.build(pageParam, map)));
        return new R<>(classExamQualityList);
    }
}
