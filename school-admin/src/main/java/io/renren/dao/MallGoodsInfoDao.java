package io.renren.dao;

import io.renren.entity.MallGoodsInfoEntity;
import io.renren.entity.MallGoodsInfoLayEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-29 10:11:55
 */
public interface MallGoodsInfoDao extends BaseMDao<MallGoodsInfoEntity> {
	public Map<String, Object> queryMap(Map<String, Object> map) ;
	
	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	
	List<MallGoodsInfoEntity> queryList(Map<String, Object> map);
	
	List<MallGoodsInfoLayEntity> queryLayList(Map<String,Object> map);
	
	MallGoodsInfoEntity queryGoodsInfo(String commodityId);
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	int accept(Long id);
	
	int reject(Long id);

	List<MallGoodsInfoEntity> findGoodsList();
	/**
	 * 简单查询
	 * @param map
	 * @param status 上架状态
	 * @param professionId 专业
	 * @param levelId 层次
	 * @param name 名称
	 * @return
	 */
	public List<Map<String, Object>> simpleQueryList(Map<String, Object> map);
	
	//判断是否有班型的引用
			int checkClassType(long id);
			//判断是否有专业的引用
			 int checkProfession(long id);
    /**
     * 查询dr=0,status=1,is_audited=1的商品总数
     * @param map
     * @return
     */
	public int queryTotal1(Map<String, Object> map);
	//判断该商品是否存在
	int checkExist(Map<String, Object> map);

    MallGoodsInfoEntity queryGoodsInfoByNcId(String ncId);
}
