package com.elise.singlesignoncenter.dao;


import com.elise.singlesignoncenter.entity.LogFirstActivateEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Repository
public interface LogFirstActivateDao {

    Integer insertOnRegister(LogFirstActivateEntity entity);

    Integer insertOnLogin(LogFirstActivateEntity entity);
}
