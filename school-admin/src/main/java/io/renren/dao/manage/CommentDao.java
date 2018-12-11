package io.renren.dao.manage;

import io.renren.pojo.manage.CommentPOJO;

import java.util.List;
import java.util.Map;

public interface CommentDao {
    /**
     * 批量更新状态
     */
    int updateBatch(Map<String, Object> map);

    List<CommentPOJO> queryPojoList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    int deleteBatch(Map<String, Object> map);
}