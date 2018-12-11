package io.renren.modules.job.dao;


import io.renren.modules.job.entity.PushClassplanDetailRemindEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 课次通知提醒详情表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 10:54:14
 */
@Repository
public interface PushClassplanDetailRemindDao extends BaseMDao<PushClassplanDetailRemindEntity> {

     List<PushClassplanDetailRemindEntity> queryListByTs(String ts);

}
