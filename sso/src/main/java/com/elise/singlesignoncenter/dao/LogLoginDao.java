package com.elise.singlesignoncenter.dao;


import com.elise.singlesignoncenter.entity.LogLoginEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Repository
public interface LogLoginDao {

    Integer insertLoginLog(LogLoginEntity entity);

}
