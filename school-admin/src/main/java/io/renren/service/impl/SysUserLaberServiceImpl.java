package io.renren.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.SysUserDao;
import io.renren.dao.SysUserLaberDao;
import io.renren.entity.SysUserEntity;
import io.renren.entity.SysUserLaberEntity;
import io.renren.pojo.SysUserLaberPOJO;
import io.renren.service.SysUserLaberService;
import io.renren.utils.ShiroUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Service("sysUserLaberService")
public class SysUserLaberServiceImpl implements SysUserLaberService {
	@Autowired
	private SysUserLaberDao sysUserLaberDao;
	
	@Autowired
	private SysUserDao sysUserDao;
	
	protected SysUserEntity getUser() {
		return ShiroUtils.getUserEntity();
	}
	
	@Override
	public SysUserLaberEntity queryObject(Long id){
		return sysUserLaberDao.queryObject(id);
	}
	
	@Override
	public List<SysUserLaberEntity> queryList(Map<String, Object> map){
		return sysUserLaberDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysUserLaberDao.queryTotal(map);
	}
	
	@Override
	public void save(SysUserLaberEntity sysUserLaber){
		if(null == sysUserLaber.getStatus() || 0 == sysUserLaber.getStatus()){
			sysUserLaber.setStatus(1);
		}
		
		sysUserLaberDao.save(sysUserLaber);
	}
	
	@Override
	public void update(SysUserLaberEntity sysUserLaber){
		sysUserLaberDao.update(sysUserLaber);
	}

	@Override
	public String queryLaberByUserId(Long userId) {
		return sysUserLaberDao.queryLaberByUserId(userId);
	}

	@Override
	public List<SysUserLaberPOJO> queryUserLaberList(Map<String, Object> map) {

		List<Long> ids = new ArrayList<Long>();
		List<Long> moIds = new ArrayList<Long>();
		//使用名字模糊匹配
		String name = (String) map.get("nickName");
		if (StringUtils.isNotBlank(name)) {
			List<Long> userIds = sysUserDao.queryUserIdByName(name);
			if(userIds.size()==0){
				userIds.add(0L);
			}
			ids.addAll(userIds);
		}
		// 使用手机号码查出用户ID
		String mobile = (String) map.get("mobile");
		if (StringUtils.isNotBlank(mobile)) {
			SysUserEntity sysUser = sysUserDao.queryMobile(map);
			sysUser.getUserId();
			moIds.add(sysUser.getUserId());
		}
		map.put("sysUserIds", ids);
		map.put("sysUserMoIds", moIds);
		return sysUserLaberDao.queryUserLaberList(map);
	}

	@Override
	public SysUserLaberPOJO queryPOJO(Long id) {
		return sysUserLaberDao.queryPOJO(id);
	}

	@Override
	public void stopUse(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", 2);
    	map.put("modifier", getUser().getUserId());
    	sysUserLaberDao.updateBatch(map);
	}
	
	@Override
	public void startUse(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", 1);
    	map.put("modifier", getUser().getUserId());
    	sysUserLaberDao.updateBatch(map);
	}
	
	@Override
	public void del(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", ids);
		map.put("dr", 1);
		map.put("modifier", getUser().getUserId());
		sysUserLaberDao.del(map);
	}
	
	
}
