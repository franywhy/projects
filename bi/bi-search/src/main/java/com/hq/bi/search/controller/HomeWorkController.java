package com.hq.bi.search.controller;

import com.hq.bi.search.entity.HomeWorkStaticEntity;
import com.hq.bi.search.service.HomeWorkService;
import com.hq.bi.search.util.PageInfo;
import com.hq.bi.search.util.PageQuery;
import com.hq.bi.search.util.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:班级考试质量评比报表控制器
 * Autor: wl
 * Date: 2018-12-15
 */
@RestController
@RequestMapping("/homeWork")
@Slf4j
@Api(tags = "报表查询-ClassExamQualityController")
public class HomeWorkController {

    @Autowired
    private HomeWorkService homeWorkService;

    /**
     * 预习作业统计报表接口
     * @param map
     * @return
     */
    @PostMapping(value = "/getPreHomeWorkStaticList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R getPreHomeWorkStaticList(@RequestBody Map<String, Object> map){
        HashMap<String, Object>pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("pageNum"));
        pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("pageSize"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        PageInfo<HomeWorkStaticEntity> preHomeWorkStaticList = new PageInfo<>(homeWorkService.getPreHomeWorkStaticList(PageQuery.build(pageParam, map)));
        return new R<>(preHomeWorkStaticList);
    }
    /**
     * 课后作业统计报表接口
     * @param map
     * @return
     */
    @PostMapping(value = "/getAfterClassStaticList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R getAfterClassStaticList(@RequestBody Map<String, Object> map){
        HashMap<String, Object>pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("pageNum"));
        pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("pageSize"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        PageInfo<HomeWorkStaticEntity> afterClassStaticList = new PageInfo<>(homeWorkService.getAfterClassStaticList(PageQuery.build(pageParam, map)));
        return new R<>(afterClassStaticList);
    }

}
