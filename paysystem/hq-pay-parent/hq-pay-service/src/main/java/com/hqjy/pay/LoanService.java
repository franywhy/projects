package com.hqjy.pay;

/**
 * 贷款
 * @author zhaozhiguang
 */
public interface LoanService {

    /**
     * 保存
     * @param loanEntity
     * @return
     */
    Boolean saveLoan(LoanEntity loanEntity);

    /**
     * 获取
     * @param id
     * @return
     */
    LoanEntity getLoan(String id);

}
