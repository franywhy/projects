package io.renren.dao;

import io.renren.entity.UserOldclassLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 记录学员转班前推送给题库的队列班级信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-02-26 14:37:05
 */
@Mapper
public interface UserOldclassLogDao extends BaseDao<UserOldclassLogEntity> {

    List<Map<String,Object>> queryMessage(@Param("ts") String millisecond);
}
