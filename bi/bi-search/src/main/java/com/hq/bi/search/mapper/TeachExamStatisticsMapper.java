package com.hq.bi.search.mapper;


import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hzr
 * @since 2018-12-07
 */
@Mapper
public interface TeachExamStatisticsMapper {


  List<Map<String, Object>> select(@Param("page") Integer page, @Param("limit") Integer limit,
      @Param("course") String course,
      @Param("examinationPaper") String examinationPaper,
      @Param("examinationPaperType") Integer examinationPaperType,
      @Param("submitStatus") String submitStatus, @Param("startTime") String startTime,
      @Param("endTime") String endTime,
      @Param("profession") String profession,
      @Param("examinationPaperName") String examinationPaperName,
      @Param("studentName") String studentName);

  Integer getTotla(
      @Param("course") String course,
      @Param("examinationPaper") String examinationPaper,
      @Param("examinationPaperType") Integer examinationPaperType,
      @Param("submitStatus") String submitStatus, @Param("startTime") String startTime,
      @Param("endTime") String endTime,
      @Param("profession") String profession,
      @Param("examinationPaperName") String examinationPaperName,
      @Param("studentName") String studentName);
}
