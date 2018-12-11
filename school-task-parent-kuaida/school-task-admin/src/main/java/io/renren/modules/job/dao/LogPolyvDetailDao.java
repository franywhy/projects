package io.renren.modules.job.dao;


import io.renren.modules.job.entity.LogPolyvDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-16 14:16:36
 */
@Mapper
public interface LogPolyvDetailDao extends BaseMDao<LogPolyvDetailEntity> {

    int checkDetail(@Param("userId") String userId, @Param("recordId")String recordId, @Param("createDate")Date createDate);
}
