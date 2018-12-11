package io.renren.dao;

import io.renren.entity.ContractDetail;
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
public interface ContractDetailDao extends BaseMDao<ContractDetail> {
	 
	void setDr(ContractDetail c);
}
