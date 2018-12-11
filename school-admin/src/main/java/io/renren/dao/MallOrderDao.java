package io.renren.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.entity.MallOrderEntity;
import io.renren.pojo.SyncCustomerPOJO;
import io.renren.pojo.order.OrderPOJO;

/**
 * 订单
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 14:30:47
 */
public interface MallOrderDao extends BaseDao<MallOrderEntity> {
	
	/**
	 * 查询未生成学员规划的订单列表-根据商品ID
	 * @param commodityId	商品ID
	 * @param classId 
	 * @return	未生成学员规划的订单列表
	 */
	List<MallOrderEntity> queryListByCommodityId(@Param(value="commodityId")Long commodityId, @Param(value="classId")Long classId);
	
	/**
	 * 查询未生成学员规划的订单列表-根据商品ID
	 * @param commodityId	商品ID
	 * @return	未生成学员规划的订单列表
	 */
	List<io.renren.entity.MallOrderEntity> queryListByCommodityIdAndUserId(@Param(value="commodityId")Long commodityId, @Param(value="userId")Long userId);
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	/**
	 * 批量更换班级
	 */
	int updateBatchClass(Map<String, Object> map);

	/**
	 * 查询订单--部分简易数据
	 * 
	 * @param map[orderNo]=订单号
	 * @param map[userMobile]=手机号码
	 * @param map[classId]=班级ID
	 * @param map[sourceType]=来源
	 * @param map[schoolId]=平台ID
	 * @param map[classId]=班级ID
	 * @return 订单集合
	 */
	List<Map<String, Object>> queryListGrid(Map<String, Object> map);

	/**
	 * 查询订单--部分简易数据
	 * 
	 * @param map[orderNo]=订单号
	 * @param map[userMobile]=手机号码
	 * @param map[classId]=班级ID
	 * @param map[sourceType]=来源
	 * @param map[schoolId]=平台ID
	 * @param map[classId]=班级ID
	 * @return 订单集合
	 */
	int queryListGridTotal(Map<String, Object> map);

	/**
	 * 查询可生成学员规划的订单详情
	 */
	MallOrderEntity queryOrderForUserplan(Map<String, Object> map);

	/**
	 * 查询学员全部未生成学员规划的订单
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryOrderForChangeUserplan(Map<String, Object> map);

	int queryOrderForChangeUserplanTotal(Map<String, Object> map);

	// 判断是否有班型的引用
	int checkClassType(long id);

	// 判断是否有专业的引用
	int checkProfession(long id);

	// 判断订单是否存在
//	int queryOrderExist(Map<String, Object> map);
	/**
	 * 校验订单是否存在 根据nc_id
	 * @param ncId
	 * @return
	 */
	int queryOrderExistByNcId(String ncId);
	/**
	 * 校验订单是否存在 根据nc_code
	 * @param ncCode
	 * @return
	 */
	int queryOrderExistByNcCode(String ncCode);
	/**
	 * 校验订单是否存在 根据order_no
	 * @param orderNo
	 * @return
	 */
	int queryOrderExistByOrderNo(String orderNo);

	// 如果存在订单就删除
	void updateDr(OrderPOJO mallOrder);

	// 根据
	long getUserplanDetailId(Map<String, Object> map);

	// 根据
	int countUserplanDetailId(Map<String, Object> map);

	int queryUserOneOrder(String userId);

	String queryOrderId(String userId);
	
	List<io.renren.pojo.order.OrderPOJO> queryPOJO(Map<String, Object> map);
	
	  //	判断订单下存在某个用户的数量
    int countUsersMobile(Map<String, Object> map);
    
    //
    void saveLogOrder(MallOrderEntity mallOrderEntity);

    // 保存同步订单
	void saveOrder(MallOrderEntity mallOrderEntity);

	// 根据订单ids修改dr
	void updateDrByOrderIds(Long[] orderIds);
	
	//查询录播课订单
	List<Map<String,Object>> queryVideoCourse(Map<String,Object> map);
    //查询退款订单
    List<MallOrderEntity> queryObjectByNC(@Param("mobile") String userPhone,@Param("ncId") String ncId);
    //问题订单告诉题库关闭权限
   Map<String,Object> queryKJClasstoTK(Long orderId);

    void updateChangeByOrderNo(Long orderId);
    //查询同一学员是否有其他订单
    int queryOrderExistByTkCode(@Param("userId")Long userId, @Param("commodityId")Long commodityId);

    //查询蓝鲸是否存在相同用户的商品
    List<MallOrderEntity> isExistByUserId(@Param("ncId")String ncId, @Param("userPhone")String userPhone, @Param("commodityNCPK")String commodityNCPK);
	
	OrderPOJO queryPOJOByOrderId(@Param("orderId")Long orderId);

	int queryTotalCustomers(@Param("startOrderId")Integer startOrderId, @Param("endOrderId")Integer endOrderId, @Param("teacherMobileList")List<String> teacherMobileList, @Param("orderNoList")List<String> orderNoList);

	List<SyncCustomerPOJO> queryMapList(@Param("offset")int offset, @Param("startOrderId")Integer startOrderId, @Param("endOrderId")Integer endOrderId, @Param("teacherMobileList")List<String> teacherMobileList, @Param("orderNoList")List<String> orderNoList);
	
	/**
	 * 修改教师角色
	 * @param map
	 * @return
	 */
	int updateIsTeacher(Map<String,Object> map);

	List<Map<String, Object>> queryDivideClassesMessage(Map<String, Object> mapQuery);

	String queryPhoneByOrderId(@Param("orderId")Long orderId);

    MallOrderEntity queryObjectByNcIdAndCommodityId(@Param("ncId")String ncId, @Param("ncCommodityId")String ncCommodityId);

    MallOrderEntity queryObjectByMobileAndComodityId(@Param("mobile")String mobile, @Param("ncCommodityId")String ncCommodityId);
    
    /**
     * 设置订单的Ncid,当为空时设置为空
     *@param orderId
     *@param ncId
     * @author lintf
     * 2018年9月20日
     */
    void setNcId(@Param("orderId")Long orderId,@Param("ncId")String ncId);
}
