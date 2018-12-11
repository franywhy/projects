package com.contract.dao;

import com.contract.pojo.ContractDetailPOJO;
import com.contract.pojo.ContractRecordPOJO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ContractDetailDao {
    List<ContractDetailPOJO> queryPojoList(Long contractRecordId);
}
