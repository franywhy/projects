package com.school.service.impl;

import com.school.dao.MallExpcertificateDao;
import com.school.entity.MallExpcertificateEntity;
import com.school.service.MallExpcertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DL on 2017/12/19.
 */
@Service("mallExpcertificateService")
public class MallExpcertificateServiceImpl implements MallExpcertificateService {
    @Autowired
    private MallExpcertificateDao mallExpcertificateDao;

    @Override
    public MallExpcertificateEntity queryExpcertificateByMobile(Long userId) {
        return mallExpcertificateDao.queryExpcertificateByMobile(userId);
    }

    @Override
    public MallExpcertificateEntity queryExpcertificateByIDCard(String IDCard, String userName) {
        return mallExpcertificateDao.queryExpcertificateByIDCard(IDCard, userName);
    }
}
