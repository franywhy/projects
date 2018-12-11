package io.renren.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.SysRoleProductDao;
import io.renren.service.SysRoleProductService;

@Service("sysRoleProductService")
public class SysRoleProductServiceImpl implements SysRoleProductService {
	@Autowired
	private SysRoleProductDao sysRoleProductDao;
	
	@Override
	public void saveOrUpdate(Long roleId, List<Long> productIdList) {
		if(productIdList.size() == 0){
			return ;
		}
		
		//先删除角色与产品线关系
		sysRoleProductDao.delete(roleId);
		
		//保存角色与产品的关系
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("productIdList", productIdList);
		sysRoleProductDao.save(map);
	}

	@Override
	public List<Long> queryProductIdList(Long roleId) {
		return sysRoleProductDao.queryProductIdList(roleId);
	}

}
