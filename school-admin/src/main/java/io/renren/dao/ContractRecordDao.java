package io.renren.dao;

import io.renren.entity.ContractRecord;
import io.renren.entity.SysLogEntity;
import java.util.Map;
 
/**
 * 系统日志
 *
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-29 09:57:44
 */
public interface ContractRecordDao extends BaseMDao<ContractRecord> {


    int setStatus(Long contractId);

    long savebackId(ContractRecord cd);

    void setDr(ContractRecord cd);

    void updateByContract(ContractRecord cd);
}
