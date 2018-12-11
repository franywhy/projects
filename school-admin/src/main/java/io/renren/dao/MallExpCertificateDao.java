package io.renren.dao;

import java.util.List;

import io.renren.entity.MallExpCertificateEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-11-04 10:20:59
 */
public interface MallExpCertificateDao extends BaseDao<MallExpCertificateEntity> {

	void updateStatus(Integer[] ids);
	
	void batchSave(List<MallExpCertificateEntity> exps);
	
}
