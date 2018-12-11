package com.school.dao;

import com.school.entity.MallExpcertificateEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Created by DL on 2017/12/19.
 */
public interface MallExpcertificateDao {

    MallExpcertificateEntity queryExpcertificateByMobile(Long userId);

    MallExpcertificateEntity queryExpcertificateByIDCard(@Param("IDCard") String IDCard, @Param("userName") String userName);
}
