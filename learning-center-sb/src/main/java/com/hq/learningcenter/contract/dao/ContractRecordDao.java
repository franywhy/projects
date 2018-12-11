package com.hq.learningcenter.contract.dao;

import com.hq.learningcenter.contract.pojo.ContractRecordPOJO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ContractRecordDao {
    Long querySignerId(Long userId);

    void updateStatus(Long contractId);

    List<ContractRecordPOJO> queryPojoList(Map<String,Object> map);

    void update(Map<String,Object> map);
    
    public int getContractSignNum(Map<String,Object> map);
}