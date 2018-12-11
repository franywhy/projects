package com.elise.singlesignoncenter.service.impl;

import com.elise.singlesignoncenter.dao.UsersDao;
import com.elise.singlesignoncenter.entity.UserInfoEntity;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.hq.common.enumeration.GenderEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UsersDao usersDao;

    @Override
    public UserInfoEntity getUserInfoItem(String mobileNo) {

        return usersDao.queryUserInfo(mobileNo, 0, 1);
    }

    @Override
    public UserInfoEntity getUserInfoItem(Integer userId) {

        return usersDao.queryUserInfoById(userId, 0, 1);
    }

    @Override
    public Integer updateUserInfo(String avatar, String nickName, String email, Integer gender, Integer userId) {
        UserInfoEntity entity = new UserInfoEntity();
        entity.setNickName(nickName);
        entity.setEmail(email);
        entity.setSex(gender);
        entity.setUserId(userId);
        entity.setAvatar(avatar);
        entity.setDr(0);
        return usersDao.updateUserInfo(entity);
    }


    @Override
    public Integer insertUserInfo(Integer userId, String businessId, String mobileNo, String nickName, String passWord, String avatar) {
        UserInfoEntity entity = new UserInfoEntity();
        entity.setUserId(userId);
        entity.setSex(GenderEnum.UNKNOWN.getValue());
        entity.setEmail(null);
        entity.setNickName(nickName);
        entity.setDr(0);
        entity.setChannel(0);
        entity.setMobile(mobileNo);
        entity.setPassWord(passWord);
        entity.setSchoolId(businessId);
        entity.setStatus(1);
        return usersDao.insertUserInfo(entity);

    }

    @Override
    public Integer modifyUserPassWord(Integer userId, String passWord) {
        UserInfoEntity entity = new UserInfoEntity();
        entity.setUserId(userId);
        entity.setPassWord(passWord);
        entity.setDr(0);
        return usersDao.modifyPassWord(entity);
    }

    @Override
    public String queryUserPassWord(Integer userId) {
        return usersDao.queryPassWord(userId, 0, 1);
    }

    @Override
    public String getNcId(Integer userId) {
        return usersDao.queryNcId(userId, 0, 1);
    }

    @Override
    public Boolean checkMobileNo(String mobileNo) {
        Integer flag = usersDao.checkMobileNo(mobileNo);
        return flag == 0 ? false : true;
    }


}
