package com.hq.bi.search.controller;

import com.hq.bi.search.entity.*;
import com.hq.bi.search.service.PKIndexManagerService;
import com.hq.bi.search.util.PageInfo;
import com.hq.bi.search.util.PageQuery;
import com.hq.bi.search.util.R;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Autor: hutao
 * Date: 2018-12-13-15:20
 */
@RestController
@RequestMapping("/pKIndex")
@Slf4j
@Api(tags = "PKIndexEvaluationController")
public class PKIndexEvaluationController {

    @Autowired
    private PKIndexManagerService pkIndexManagerService;

    /**
     * 学习笔记管理查询报表接口
     * @param pageParam
     * @param map
     * @return
     */
    @PostMapping(value = "/teachNoteList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "学习笔记列表数据", notes = "获取全部学习笔记列表数据\n" +
            "{\n" +
            "    \"[当前页] pageNum\":\"1\",\n" +
            "    \"[分页总数] total\":\"\",\n" +
            "    \"[分页大小] pageSize\":\"10\",\n" +
            "    \"[排序字段] sidx\":\"courseId\",\n" +
            "    \"[排序方式] sort\":\"desc\",\n" +
            "    \"[商品ID] mallGoodsId\":\"166\"\n" +
            "    \"[专业ID] professionId\":\"1\"\n" +
            "    \"[课程ID] courseId\":\"521\"\n" +
            "    \"[教师名称] userName\":李老师\"10\"\n" +
            "    \"[手机号] mobile\":\"19924429748\"\n" +
            "}")
    @ApiModelProperty(value = "pageNum", name = "map", dataType = "Integer", example = "1")
    public R getTeachNoteList(@RequestBody Map<String, Object> map){
        HashMap<String, Object>pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("pageNum"));
        pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("pageSize"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        PageInfo<TeachNoteEvaluationEntity> getTeachNoteList = new PageInfo<>(pkIndexManagerService.getTeachNoteList(PageQuery.build(pageParam, map)));
        return new R<>(getTeachNoteList);
    }

    /**
     * 高频错题报表统计查询
     * @param map
     * @return
     */
    @PostMapping(value = "/errorHistoryList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "高频错题列表", notes = "获取高频错题列表\n" +
            "{\n" +
            "    \"[当前页] pageNum\":\"1\",\n" +
            "    \"[分页总数] total\":\"\",\n" +
            "    \"[分页大小] pageSize\":\"10\",\n" +
            "    \"[排序字段] sidx\":\"courseId\",\n" +
            "    \"[排序方式] sort\":\"desc\",\n" +
            "    \"[开始时间] startTime\":\"2018-01-10\"\n" +
            "    \"[结束时间] endTime\":\"2019-01-10\"\n" +
            "    \"[课程ID] courseId\":\"kckm105\"\n" +
            "    \"[章id] chapterId\":\"kckm105ZA0\"\n" +
            "    \"[节id] sectionId\":\"kckm105ZA0J01\"\n" +
            "    \"[题目id] subjectId\":\"90961\"\n" +
            "    \"[题目名称] subjectName\":\"下列电算化会计核算工作由计算机自动完成的是（   ）\"\n" +
            "}")
    @ApiModelProperty(value = "pageNum", name = "map", dataType = "Integer", example = "1")
    public R getErrorHistoryList(@RequestBody Map<String, Object> map){
        HashMap<String, Object>pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("pageNum"));
        pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("pageSize"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        PageInfo<ErrorHistoryReportEntity> getTeachNoteList = new PageInfo<>(pkIndexManagerService.getErrorHistoryList(PageQuery.build(pageParam, map)));
        return new R<>(getTeachNoteList);
    }

    /**
     * 班级学籍评比查询报表接口
     * @param pageParam
     * @param map
     * @return
     */
    @PostMapping(value = "/schoolRollList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "班级学籍评比列表数据", notes = "获取班级学籍评比列表数据\n" +
            "{\n" +
            "    \"[当前页] pageNum\":\"1\",\n" +
            "    \"[分页总数] total\":\"\",\n" +
            "    \"[分页大小] pageSize\":\"10\",\n" +
            "    \"[排序字段] sidx\":\"courseId\",\n" +
            "    \"[排序方式] sort\":\"desc\",\n" +
            "    \"[开始时间] startTime\":\"2018-01-10\"\n" +
            "    \"[结束时间] endTime\":\"2019-01-10\"\n" +
            "    \"[专业ID] professionId\":\"3\"\n" +
            "    \"[班主任姓名] classTeacherName\":\"黄敏\"\n" +
            "    \"[班级名称] className\":\"重庆会计本科默认班级\"\n" +
            "}")
    @ApiModelProperty(value = "pageNum", name = "map", dataType = "Integer", example = "1")
    public R getSchoolRollList(@RequestBody Map<String, Object> map){
         HashMap<String, Object>pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("pageNum"));
        pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("pageSize"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        PageInfo<SchoolRollEvaluationEntity> getSchoolRollList = new PageInfo<>(pkIndexManagerService.getSchoolRollList(PageQuery.build(pageParam, map)));
        return new R<>(getSchoolRollList);
    }

    /**
     * 教学质量评比查询报表接口
     * @param pageParam
     * @param map
     * @return
     */
    @PostMapping(value = "/teachingEvaluationList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "教学质量评比列表数据", notes = "获取教学质量评比列表数据\n" +
            "{\n" +
            "    \"[当前页] pageNum\":\"1\",\n" +
            "    \"[分页总数] total\":\"\",\n" +
            "    \"[分页大小] pageSize\":\"10\",\n" +
            "    \"[排序字段] sidx\":\"courseId\",\n" +
            "    \"[排序方式] sort\":\"desc\",\n" +
            "    \"[开始时间] startTime\":\"2018-01-10\"\n" +
            "    \"[结束时间] endTime\":\"2019-01-10\"\n" +
            "    \"[排课计划ID] classPlanId\":6ce28c51-1bc7-43c8-b582-be54e8ff1e9f\"3\"\n" +
            "    \"[课程ID] courseId\":\"521\"\n" +
            "    \"[讲师名称] teacherName\":\"黄敏\"\n" +
            "}")
    @ApiModelProperty(value = "pageNum", name = "map", dataType = "Integer", example = "1")
    public R getTeachingEvaluationList(@RequestBody Map<String, Object> map){
        HashMap<String, Object>pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("pageNum"));
        pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("pageSize"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        PageInfo<PKIndexEvaluationEntity> getTeachingEvaluationList = new PageInfo<>(pkIndexManagerService.getTeachingEvaluationList(PageQuery.build(pageParam, map)));
        return new R<>(getTeachingEvaluationList);
    }

    /**
     * 班级学习评比查询报表接口
     * @param pageParam
     * @param map
     * @return
     */
    @PostMapping(value = "/classStudyList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "班级学习评比列表数据", notes = "获取班级学习评比列表数据\n" +
            "{\n" +
            "    \"[当前页] pageNum\":\"1\",\n" +
            "    \"[分页总数] total\":\"\",\n" +
            "    \"[分页大小] pageSize\":\"10\",\n" +
            "    \"[排序字段] sidx\":\"courseId\",\n" +
            "    \"[排序方式] sort\":\"desc\",\n" +
            "    \"[开始时间] startTime\":\"2018-01-10\"\n" +
            "    \"[结束时间] endTime\":\"2019-01-10\"\n" +
            "    \"[专业ID] professionId\":\"3\"\n" +
            "    \"[班主任姓名] classTeacherName\":\"黄敏\"\n" +
            "    \"[班级名称] className\":\"重庆会计本科默认班级\"\n" +
            "}")
    @ApiModelProperty(value = "pageNum", name = "map", dataType = "Integer", example = "1")
    public R getClassStudyList(@RequestBody Map<String, Object> map){
        HashMap<String, Object>pageParam = new HashMap<>();
        pageParam.put("pageNum",map.get("pageNum"));
        pageParam.put("total",map.get("total"));
        pageParam.put("pageSize",map.get("pageSize"));
        pageParam.put("sidx",map.get("sidx"));
        pageParam.put("sort",map.get("sort"));
        PageInfo<PKIndexEvaluationEntity> getClassStudyList = new PageInfo<>(pkIndexManagerService.getClassStudyList(PageQuery.build(pageParam, map)));
        return new R<>(getClassStudyList);
    }

}
