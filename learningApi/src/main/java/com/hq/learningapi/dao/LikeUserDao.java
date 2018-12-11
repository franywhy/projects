package com.hq.learningapi.dao;

import com.hq.learningapi.entity.LikeUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/16 0016.
 */
@Repository
public interface LikeUserDao {

    Long isLike(Map<String, Object> map);
        
    int save(LikeUser likeUser);

    int remove(Map<String, Object> map);

    Long queryTotal(@Param("likeObject") Long likeObject, @Param("likeType") Integer likeType);
}
