package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.dao.MallExpCertificateDao;
import io.renren.entity.MallExpCertificateEntity;
import io.renren.service.MallExpCertificateService;
import io.renren.utils.BeanHelper;



@Service("mallExpCertificateService")
public class MallExpCertificateServiceImpl implements MallExpCertificateService {
	@Autowired
	private MallExpCertificateDao mallExpCertificateDao;
	
	@Override
	public MallExpCertificateEntity queryObject(Integer id){
		return mallExpCertificateDao.queryObject(id);
	}
	
	@Override
	public List<MallExpCertificateEntity> queryList(Map<String, Object> map){
		return mallExpCertificateDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallExpCertificateDao.queryTotal(map);
	}
	
	@Override
	public void save(MallExpCertificateEntity mallExpCertificate){
		BeanHelper.beanAttributeValueTrim(mallExpCertificate);
		mallExpCertificateDao.save(mallExpCertificate);
	}
	
	@Override
	public void update(MallExpCertificateEntity mallExpCertificate){
		BeanHelper.beanAttributeValueTrim(mallExpCertificate);
		mallExpCertificateDao.update(mallExpCertificate);
	}
	
	@Override
	public void delete(Integer id){
		mallExpCertificateDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		mallExpCertificateDao.deleteBatch(ids);
	}

	@Override
	public void updateStatus(Integer[] ids) {
		// TODO Auto-generated method stub
		mallExpCertificateDao.updateStatus(ids);
	}

	@Override
	public void batchSave(List<MallExpCertificateEntity> exps) {
		// TODO Auto-generated method stub
		mallExpCertificateDao.batchSave(exps);
	}
	
}
