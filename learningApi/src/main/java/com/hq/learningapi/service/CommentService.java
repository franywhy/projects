package com.hq.learningapi.service;

import com.hq.learningapi.entity.Comment;
import com.hq.learningapi.pojo.CommentPOJO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface CommentService {

    void save(Comment comment);

    List<CommentPOJO> queryPojoList(Map<String, Object> map);

    void addLikeNumber(Long commentId);

    void delLikeNumber(Long commentId);

}
