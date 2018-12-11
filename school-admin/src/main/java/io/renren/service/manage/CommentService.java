package io.renren.service.manage;


import io.renren.pojo.manage.CommentPOJO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface CommentService {

    int queryTotal(Map<String, Object> map);

    List<CommentPOJO> queryPojoList(Map<String, Object> map);

    void deleteBatch(Map<String, Object> map);

    void pause(Long[] commentIds);

    void resume(Long[] commentIds);
}
