package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.MallGoodsInfoEntity;
import io.renren.entity.MallGoodsInfoLayEntity;
import io.renren.pojo.MallGoodsInfoPOJO;

/**
 * 商品档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-29 10:11:55
 */
public interface MallGoodsInfoService {
	
	MallGoodsInfoEntity queryGoodsInfoId(Map<String, Object> map);
	MallGoodsInfoEntity queryObject(Map<String, Object> map);
	Map<String, Object> queryMap(Map<String, Object> map);
	 
	MallGoodsInfoEntity queryGoodsInfo(String commodityId);
	
	/**
	 * 将组合班型和商品表合并取得可开通的商品列表 
	 *@param NcCommodityId nc中的班型
	 *@param  hasDr 为FALSE则只取dr=0的  
	 *@return
	 * @author lintf
	 * 2018年9月25日
	 */

	List<MallGoodsInfoEntity> groupGoodInfo(String NcCommodityId,boolean hasDr);
	public List<Map<String, Object>> queryListMap(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	List<MallGoodsInfoEntity> queryList(Map<String,Object> map);
	/**
	 * 查询dr=0,status=1,is_audited=1的商品列表
	 * @param map
	 * @return
	 */
	List<MallGoodsInfoLayEntity> queryLayList(Map<String,Object> map);
	void save(MallGoodsInfoPOJO mallGoodsInfo);
	
	void update(MallGoodsInfoPOJO mallGoodsInfo);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	void pause(Long[] ids);
	
	void resume(Long[] ids);
	
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
	List<Map<String, Object>> simpleQueryList(Map<String, Object> map);
	//判断是否有班型的引用
	int checkClassType(long id);
	//判断是否有专业的引用
	 int checkProfession(long id);
	/**
	 * 查询dr=0,status=1,is_audited=1的商品总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);
	//判断该商品是否存在
	boolean checkExist(Map<String, Object> map);
	/**
	 * 拷贝省份
	 * @param id  		商品ID
	 * @param areaId1	原省份ID
	 * @param areaId2	新省份ID
	 * @return			处理是否成功
	 */
	boolean copyArea(Long id, Long areaId1, Long areaId2 , String schoolId);

	//根据ncid查询商品内容
    MallGoodsInfoEntity queryGoodsInfoByNcId(String ncId);
}
