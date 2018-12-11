package com.hqjy.pay.impl;

import com.hqjy.pay.LoanEntity;
import com.hqjy.pay.LoanService;
import com.hqjy.pay.mapper.LoanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanDao loanDao;

    @Override
    public Boolean saveLoan(LoanEntity loanEntity) {
        return loanDao.saveLoan(loanEntity) == 1 ? true: false;
    }

    @Override
    public LoanEntity getLoan(String id) {
        return loanDao.getLoan(id);
    }
}
