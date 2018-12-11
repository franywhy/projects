package com.hq.learningcenter.contract.dao;

import com.hq.learningcenter.contract.pojo.ContractDetailPOJO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractDetailDao {
    List<ContractDetailPOJO> queryPojoList(Long contractRecordId);
}
