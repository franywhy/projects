package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.LikeUserDao;
import com.hq.learningapi.entity.LikeUser;
import com.hq.learningapi.service.LikeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/16 0016.
 */
@Service
public class LikeUserServiceImpl implements LikeUserService {

    @Autowired
    private LikeUserDao likeUserDao;

    @Override
    public int save(LikeUser likeUser) {
        return likeUserDao.save(likeUser);
    }

    @Override
	public int remove(Map<String, Object> map) {	 	
		return likeUserDao.remove(map);
	}

    /**
     * 判断用户点赞表是否存在数据
     * @param userId
     * @param likeObject
     * @param likeType
     * @return
     */
    @Override
    public boolean isLikeUserExist(Long likeObject,Long userId,Integer likeType){
         Map<String,Object> map = new HashMap<>();
         map.put("userId", userId);
         map.put("likeObject", likeObject);
         map.put("likeType", likeType);
         Long count = likeUserDao.isLike(map);
         if (count > 0) {
        	  return true;
         }
         return false;
    }  

    @Override
    public Long queryTotal(Long likeObject, int likeType) {
        return likeUserDao.queryTotal(likeObject, likeType);
    }
}
