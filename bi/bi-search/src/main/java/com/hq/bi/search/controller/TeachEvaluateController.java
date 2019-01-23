package com.hq.bi.search.controller;

import com.hq.bi.search.service.TeachEvaluateService;
import com.hq.bi.search.util.PageInfo;
import com.hq.bi.search.util.PageQuery;
import com.hq.bi.search.util.R;
import com.hq.bi.search.vo.LiveEvaluateVo;
import com.hq.bi.search.vo.RecordEvaluateVo;
import com.hq.bi.search.vo.TeachEvaluateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 评价 前端控制器
 * </p>
 *
 * @author hzr
 * @since 2018-12-05
 */
@RestController
@RequestMapping("/teachEvaluate")
public class TeachEvaluateController {
    @Autowired
    private TeachEvaluateService teachEvaluateService;


    /**
     * 获取评价详情-完成可以参考这
     * @param map
     * @return
     */
    @GetMapping(value = "/listEvaluate",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R listEvaluate(@RequestParam Map<String, Object> map){
        HashMap<String, Object> pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("page"));
        //pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("limit"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        pageParam.put("startTime",map.get("startTime"));
        pageParam.put("endTime",map.get("endTime"));
        pageParam.put("topicId",map.get("topicId"));
        pageParam.put("topicType",map.get("topicType"));
        PageInfo<TeachEvaluateVo> list = new PageInfo<>(teachEvaluateService.listEvaluate(PageQuery.build(pageParam, map)));
        return new R<>(list);
    }



    /**
     * 直播评价接口-写这
     * @param map
     * @return
     */
    @GetMapping(value = "/listLiveEvaluate",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R listLiveEvaluate(@RequestParam Map<String, Object> map){
        HashMap<String, Object> pageParam = new HashMap<>();
        /*当前页*/
        pageParam.put("pageNum",map.get("page"));
        /*查询条数*/
        pageParam.put("pageSize",map.get("limit"));
        /*排序字段名*/
        pageParam.put("sidx",map.get("sidx"));
        /*升降序*/
        pageParam.put("sort",map.get("sort"));
        /*开始日期*/
        pageParam.put("startTime",map.get("startTime"));
        /*结束日期*/
        pageParam.put("endTime",map.get("endTime"));
        /*课次id*/
        pageParam.put("courseClassplanLivesId",map.get("courseClassplanLivesId"));
        PageInfo<LiveEvaluateVo> list = new PageInfo<>(teachEvaluateService.listLiveEvaluate(PageQuery.build(pageParam, map)));
        return new R<>(list);
    }


    /**
     * 录播评价接口-写这
     * @param map
     * @return
     */
    @GetMapping(value = "/listRecordEvaluate",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R listRecordEvaluate(@RequestParam Map<String, Object> map){
        HashMap<String, Object> pageParam = new HashMap<>();
        /*当前页*/
        pageParam.put("pageNum",map.get("page"));
        /*查询条数*/
        pageParam.put("pageSize",map.get("limit"));
        /*排序字段名*/
        pageParam.put("sidx",map.get("sidx"));
        /*升降序*/
        pageParam.put("sort",map.get("sort"));
        /*开始日期*/
        pageParam.put("startTime",map.get("startTime"));
        /*结束日期*/
        pageParam.put("endTime",map.get("endTime"));
        /*录播课次id*/
        pageParam.put("recordId",map.get("recordId"));
        PageInfo<RecordEvaluateVo> list = new PageInfo<>(teachEvaluateService.listRecordEvaluate(PageQuery.build(pageParam, map)));
        return new R<>(list);
    }

}
