package io.renren.service;

import io.renren.entity.MallExpCertificateEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-11-04 10:20:59
 */
public interface MallExpCertificateService {
	
	MallExpCertificateEntity queryObject(Integer id);
	
	List<MallExpCertificateEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallExpCertificateEntity mallExpCertificate);
	
	void update(MallExpCertificateEntity mallExpCertificate);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	void updateStatus(Integer[] ids);
	
	void batchSave(List<MallExpCertificateEntity> exps);
	
}
