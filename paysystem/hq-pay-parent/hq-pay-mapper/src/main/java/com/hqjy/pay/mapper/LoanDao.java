package com.hqjy.pay.mapper;

import com.hqjy.pay.LoanEntity;

public interface LoanDao extends BaseDao<LoanEntity> {

    /**
     * 保存
     * @param loanEntity
     * @return
     */
    Integer saveLoan(LoanEntity loanEntity);

    /**
     * 获取
     * @param id
     * @return
     */
    LoanEntity getLoan(String id);

}
