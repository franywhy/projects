package com.elise.singlesignoncenter.dao;


import com.elise.singlesignoncenter.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Repository
public interface UsersDao {

    Integer insertUserInfo(UserInfoEntity entity);

    String queryPassWord(@Param("userId")Integer userId,
                         @Param("dr")Integer dr,
                         @Param("limit")Integer limit);

    UserInfoEntity queryUserInfo(@Param("mobileNo")String mobileNo,
                                 @Param("dr")Integer dr,
                                 @Param("limit")Integer limit);

    UserInfoEntity queryUserInfoById(@Param("userId")Integer userId,
                                 @Param("dr")Integer dr,
                                 @Param("limit")Integer limit);

    Integer updateUserInfo(UserInfoEntity entity);

    Integer modifyPassWord(UserInfoEntity entity);

    Integer updateUserAvatar(UserInfoEntity entity);

    Integer checkMobileNo(@Param("mobileNo")String mobileNo);

    String queryNcId(@Param("userId") Integer userId,
                     @Param("dr") Integer dr,
                     @Param("limit") Integer limit);
}
