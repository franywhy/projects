package com.hq.learningapi.service;

import com.hq.learningapi.entity.LikeUser;

import java.util.Map;


/**
 * Created by Administrator on 2018/1/16 0016.
 */
public interface LikeUserService {

    int save(LikeUser likeUser);

    int remove(Map<String, Object> map);

    boolean isLikeUserExist(Long likeObject,Long userId,Integer likeType);

    Long queryTotal(Long likeObject, int likeType);
}
