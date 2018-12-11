package com.contract.service.impl;


import com.contract.dao.ContractDetailDao;
import com.contract.dao.ContractRecordDao;
import com.contract.pojo.ContractDetailPOJO;
import com.contract.pojo.ContractRecordPOJO;
import com.contract.service.ContractDetailService;
import com.contract.service.ContractRecordService;
import com.contract.utils.HttpClientUtil4_3;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
