package io.renren.modules.job.dao;


import io.renren.modules.job.entity.LogPolyvWatchEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-16 11:02:21
 */
@Mapper
public interface LogPolyvWatchDao extends BaseMDao<LogPolyvWatchEntity> {

    LogPolyvWatchEntity queryObjectByUserId(@Param("userId") String userId, @Param("recordId") String recordId);
}
