package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.CommentDao;
import com.hq.learningapi.entity.Comment;
import com.hq.learningapi.pojo.CommentPOJO;
import com.hq.learningapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public void save(Comment comment) {
        comment.setLikeNumber(0);
        comment.setAppStatus(0);
        comment.setCreationTime(new Date());
        comment.setModifiedTime(new Date());
        comment.setDr(0);
        commentDao.insert(comment);
    }

    @Override
    public List<CommentPOJO> queryPojoList(Map<String, Object> map) {
        return commentDao.queryPojoList(map);
    }
	
	@Override
	public void addLikeNumber(Long commentId) {
		 commentDao.addLikeNumber(commentId);
	}
	
	@Override
	public void delLikeNumber(Long commentId) {
		commentDao.delLikeNumber(commentId);
	}

}
