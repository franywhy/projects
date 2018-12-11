package com.elise.singlesignoncenter.service;

import com.elise.singlesignoncenter.entity.UserInfoEntity;


/**
 * Created by Glenn on 2017/4/26 0026.
 */


public interface UserInfoService {

    UserInfoEntity getUserInfoItem(String mobileNo);

    UserInfoEntity getUserInfoItem(Integer userId);

    Integer updateUserInfo(String avatar,String nickName, String email, Integer gender, Integer userId);

    Integer insertUserInfo(Integer userId, String businessId, String  mobileNo, String nickName, String passWord,String avatar);

    Integer modifyUserPassWord(Integer userId,String passWord);

    String queryUserPassWord(Integer userId);

    String getNcId(Integer userId);

    Boolean checkMobileNo(String mobileNo);

}
