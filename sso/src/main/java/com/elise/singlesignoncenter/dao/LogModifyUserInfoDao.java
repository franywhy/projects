package com.elise.singlesignoncenter.dao;


import com.elise.singlesignoncenter.entity.LogModifyUserInfoEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Repository
public interface LogModifyUserInfoDao {

    Integer insertModifyLog(LogModifyUserInfoEntity entity);



}
