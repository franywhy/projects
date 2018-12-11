package io.renren.modules.job.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.renren.modules.job.entity.SchoolReportEntity;

import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-19 09:17:36
 */
@Mapper
public interface SchoolReportDao extends BaseDao<SchoolReportEntity> {

    List<SchoolReportEntity> getDetailsByDate(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("type") int type);
}
