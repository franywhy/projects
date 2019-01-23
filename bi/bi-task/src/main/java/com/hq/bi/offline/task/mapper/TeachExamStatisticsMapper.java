package com.hq.bi.offline.task.mapper;


import com.hq.bi.offline.task.entity.TeachExamStatisticsEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Tobias
 * @since 2018-12-07
 */

public interface TeachExamStatisticsMapper {

  List<TeachExamStatisticsEntity> getExamStatistics(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

  void delete();

  void insert(List<TeachExamStatisticsEntity> examStatistic);

  Integer getTotal();

}
