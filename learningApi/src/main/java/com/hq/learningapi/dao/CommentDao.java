package com.hq.learningapi.dao;

import com.hq.learningapi.entity.Comment;
import com.hq.learningapi.pojo.CommentPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentDao {   

    int insert(Comment comment);  

    List<CommentPOJO> queryPojoList(Map<String, Object> map);

    void addLikeNumber(@Param("commentId") Long commentId);
    
    void delLikeNumber(@Param("commentId") Long commentId);
    
}