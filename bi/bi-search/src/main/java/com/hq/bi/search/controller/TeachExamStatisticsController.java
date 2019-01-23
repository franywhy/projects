package com.hq.bi.search.controller;

import com.hq.bi.search.mapper.TeachExamStatisticsMapper;
import com.hq.bi.search.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TeachExamStatisticsController 考试统计控制器
 *
 * @author Tobias
 * @date 2018/12/13
 */

@RestController
@RequestMapping("/teachExamStatistics")
@Slf4j
@Api(tags = "考试统计-TeachExamStatisticsController")
public class TeachExamStatisticsController {

  @Resource
  private TeachExamStatisticsMapper teachExamStatisticsMapper;

  @GetMapping("getTeachExamStatistics")
  @ApiOperation(value = "获取考试统计数据")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "page", value = "当前页", required = true),
      @ApiImplicitParam(name = "limit", value = "每页条数大小", required = true),
      @ApiImplicitParam(name = "course", value = "课程"),
      @ApiImplicitParam(name = "examinationPaperType", value = "试卷类型"),
      @ApiImplicitParam(name = "examinationPaper", value = "试卷"),
      @ApiImplicitParam(name = "submitStatus", value = "提交状态"),
      @ApiImplicitParam(name = "startTime", value = "开始时间"),
      @ApiImplicitParam(name = "endTime", value = "结束时间"),
      @ApiImplicitParam(name = "examinationPaperName", value = "试卷名称"),
      @ApiImplicitParam(name = "profession", value = "专业"),
      @ApiImplicitParam(name = "studentName", value = "学生名称")
  })
  public R<List> getTeachExamStatistics(@RequestParam("page") Integer page,
      @RequestParam("limit") Integer limit,
      String course, Integer examinationPaperType, String examinationPaper, String submitStatus,
      String startTime, String endTime, String examinationPaperName, String profession,
      String studentName) {
    return new R<>(teachExamStatisticsMapper
        .select(page, limit, course, examinationPaper, examinationPaperType, submitStatus,
            startTime, endTime, profession, examinationPaperName, studentName));
  }

  @ApiOperation("获取考试统计数据总条数")
  @GetMapping("getTotal")
  public R<Integer> getTotal(String course, Integer examinationPaperType, String examinationPaper, String submitStatus,
      String startTime, String endTime, String examinationPaperName, String profession,
      String studentName) {
    return new R<>(teachExamStatisticsMapper.getTotla(course.trim(), examinationPaper.trim(), examinationPaperType, submitStatus.trim(),
        startTime.trim(), endTime.trim(), profession.trim(), examinationPaperName.trim(), studentName.trim()));
  }

}