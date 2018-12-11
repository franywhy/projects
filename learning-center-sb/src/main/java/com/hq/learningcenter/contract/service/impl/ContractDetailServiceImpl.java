package com.hq.learningcenter.contract.service.impl;


import com.hq.learningcenter.contract.dao.ContractDetailDao;
import com.hq.learningcenter.contract.pojo.ContractDetailPOJO;
import com.hq.learningcenter.contract.service.ContractDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("contractDetailService")
public class ContractDetailServiceImpl implements ContractDetailService {
    @Autowired
    private ContractDetailDao contractDetailDao;

    @Override
    public List<ContractDetailPOJO> queryPojoList(Long contractRecordId) {
        System.out.println(contractDetailDao);
        return contractDetailDao.queryPojoList(contractRecordId);
    }
}
