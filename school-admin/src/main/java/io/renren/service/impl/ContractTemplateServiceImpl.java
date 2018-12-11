package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.ContractTemplateDao;
import io.renren.entity.ContractTemplateEntity;
import io.renren.service.ContractTemplateService;

@Service("ContractTemplateService")
public class ContractTemplateServiceImpl implements ContractTemplateService {

	@Autowired
	private ContractTemplateDao contractTemplateDao;
	 
	@Override
	public ContractTemplateEntity queryObject(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return contractTemplateDao.queryObject(map);
	}
	
	@Override
	public List<ContractTemplateEntity> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return contractTemplateDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return contractTemplateDao.queryTotal(map);
	}

	@Override
	public List<Map<String, Object>> queryListMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return contractTemplateDao.queryListMap(map);
	}

	@Override
	public void save(ContractTemplateEntity contractTemplate) {
		contractTemplateDao.save(contractTemplate);
	}

	@Override
	public int update(ContractTemplateEntity contractTemplate) {
		return contractTemplateDao.update(contractTemplate);
	}

	@Override
	public int delete(Map<String, Object> map) {
		return contractTemplateDao.delete(map); 
	}

}
