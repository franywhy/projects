package io.renren.service.impl.manage;

import io.renren.dao.manage.CommentDao;
import io.renren.pojo.manage.CommentPOJO;
import io.renren.service.manage.CommentService;
import io.renren.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public int queryTotal(Map<String, Object> map) {
        return commentDao.queryTotal(map);
    }

    @Override
    public List<CommentPOJO> queryPojoList(Map<String, Object> map) {
        return commentDao.queryPojoList(map);
    }

    @Override
    public void deleteBatch(Map<String, Object> map) {
        commentDao.deleteBatch(map);
    }

    @Override
    public void pause(Long[] commentIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", commentIds);
        map.put("appStatus", Constant.Status.PAUSE.getValue());
        commentDao.updateBatch(map);
    }

    @Override
    public void resume(Long[] commentIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", commentIds);
        map.put("appStatus", Constant.Status.RESUME.getValue());
        commentDao.updateBatch(map);
    }
}
