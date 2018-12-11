package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.RelatedCommodityDetailDao;
import io.renren.entity.RelatedCommodityDetailEntity;
import io.renren.service.RelatedCommodityDetailService;
import io.renren.utils.Constant;



@Service("relatedCommodityDetailService")
public class RelatedCommodityDetailServiceImpl implements RelatedCommodityDetailService {
	@Autowired
	private RelatedCommodityDetailDao relatedCommodityDetailDao;
	
	@Override
	public List<RelatedCommodityDetailEntity> queryObject(Long relatedCommodityId){
		return relatedCommodityDetailDao.queryList(relatedCommodityId);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return relatedCommodityDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(RelatedCommodityDetailEntity relatedCommodityDetail){
		relatedCommodityDetailDao.save(relatedCommodityDetail);
	}
	
	@Override
	public void update(RelatedCommodityDetailEntity relatedCommodityDetail){
		relatedCommodityDetailDao.update(relatedCommodityDetail);
	}
	
	@Override
	public void delete(Long id){
		relatedCommodityDetailDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		relatedCommodityDetailDao.deleteBatch(ids);
	}
	@Override
	public void pause(Long[] ids){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
		relatedCommodityDetailDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] ids){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
		relatedCommodityDetailDao.updateBatch(map);
	}

	/**
	 * 删除ID不等于ids的数据
	 * @param ids = id数组
	 * @param relatedCommodityId = PK
	 */
	@Override
	public void deleteBatchNotIn(Map<String, Object> map) {
		// TODO Auto-generated method stub
		relatedCommodityDetailDao.deleteBatchNotIn(map);
	}
}
