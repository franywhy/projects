package io.renren.service;

import java.util.List;
import java.util.Map;

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
public interface MallOrderService {
	
	List<MallOrderEntity> queryListByCommodityId(Long commodityId, Long classId);
	
	List<MallOrderEntity> queryListByCommodityIdAndUserId(Long commodityId, Long userId);
	
	MallOrderEntity queryObject(Map<String,Object> map);
	
	List<OrderPOJO> queryList(Map<String, Object> map);
	
	int queryUserOneOrder(String userId);
	
	String queryOrderId(String userId);
	
	int queryTotal(Map<String, Object> map);
	
	void save(OrderPOJO mallOrder);
	
	void update(OrderPOJO mallOrder);

	//void updateBatchClass(Map<String , Object> map);

	void update(MallOrderEntity mallOrder);
	
	void updateValidate(OrderPOJO mallOrder);
	
	void delete(Long orderId);
	
	void deleteBatch(Long[] orderIds);
	
	void pause(Long[] orderIds);
	
	void resume(Long[] orderIds);
	
	//判断订单是否存在
//	int queryOrderExist(Map<String,Object> map);
	/**
	 * 校验订单是否存在
	 * @param nc_id
	 * @param nc_code
	 * @param order_no
	 * @return
	 */
	int queryOrderExist(String nc_id , String nc_code , String order_no);
	//如果存在订单就删除
	void updateDr(OrderPOJO mallOrder);
	/**
	 * 查询订单--部分简易数据
	 * @param map[orderNo]=订单号
	 * @param map[userMobile]=手机号码
	 * @param map[classId]=班级ID
	 * @param map[sourceType]=来源
	 * @param map[schoolId]=平台ID
	 * @param map[classId]=班级ID
	 * @return	订单集合
	 */
	List<Map<String , Object>> queryListGrid(Map<String , Object> map);
	
	 /**
	 * 查询订单--部分简易数据
	 * @param map[orderNo]=订单号
	 * @param map[userMobile]=手机号码
	 * @param map[classId]=班级ID
	 * @param map[sourceType]=来源
	 * @param map[schoolId]=平台ID
	 * @param map[classId]=班级ID
	 * @return	订单集合
	 */
	int queryListGridTotal(Map<String , Object> map);
	
	/**
	 * 查询学员全部未生成学员规划的订单
	 * @param map
	 * @return
	 */
	List<Map<String , Object>> queryOrderForChangeUserplan(Map<String , Object> map);
	int queryOrderForChangeUserplanTotal(Map<String , Object> map);
	
	/**
	 * 查询可生成学员规划的订单详情
	 */
	MallOrderEntity queryOrderForUserplan(Map<String , Object> map);
	
	// 判断是否有班型的引用
	int checkClassType(long id);

	// 判断是否有专业的引用
	int checkProfession(long id);

	// 根据
	long getUserplanDetailId(Map<String, Object> map);

	// 根据
	int countUserplanDetailId(Map<String, Object> map);
	
	List<io.renren.pojo.order.OrderPOJO> queryPOJO(Map<String, Object> map);
	
     //	判断订单下存在某个用户的数量
	int countUsersMobile(Map<String, Object> map);

	// 保存同步订单
	void saveOrder(MallOrderEntity mallOrderEntity);
	
	// 转专业
	void updateChange(Map<String, Object> map);
	
	// 转省份
	void updateChangeArea(Map<String, Object> map);
    //根据nc状态码查询退款订单
    List<MallOrderEntity> queryObjectByNC(String userPhone, String ncId);
    //推送消息告诉题库关掉题库权限
    Map<String,Object> queryKJClasstoTK(Long orderId);

    void updateChangeByOrderNo(Long orderId);

    //根据nc推送消息关闭蓝鲸订单
    void closeOrderByNC(List<MallOrderEntity> mallOrderList);
    //查询题库课程编号的同一个学员的是否有其他的商品订单
    int queryOrderExistByTkCode(Long userId, Long commodityId);

    /*删除订单发送消息队列关闭题库权限
    @Description:
    @Author:DL
    @Date:16:03 2017/12/18
    @params:
     * @param orderId 订单id
     * @param openNum 是否开通题库权限,1是开,0是关
    */
    void sendMsgToTK (Long orderId,int openNum);

    /**
     * nc冲减订单过来时判断蓝鲸是否存在相同用户商品订单<br>
     * 根据订单NCID，手机号，商品班型PK确定唯一订单
     * @param ncId
     * @param userPhone
     * @param commodityNCPK
     * @return
     */
    List<MallOrderEntity> isExistByUserId(String ncId, String userPhone, String commodityNCPK);

	int updateIsTeacher(Map<String,Object> map);

	int queryTotalCustomers(Integer startOrderId, Integer endOrderId, List<String> teacherMobileList, List<String> orderNoList);

	List<SyncCustomerPOJO> queryMapList(int offset, Integer startOrderId, Integer endOrderId, List<String> teacherMobileList, List<String> orderNoList);

	void saveGiveOrder(OrderPOJO mallOrder);
	
	String queryPhoneByOrderId(Long orderId);

	//根据nc过来的排课查询有没有订单
    MallOrderEntity queryObjectByNcIdAndCommodityId(String ncId, String ncCommodityId);
    //根据nc传过来查询有没有有没有手动生成的订单
    MallOrderEntity queryObjectByMobileAndComodityId(String mobile, String ncCommodityId);
    /**
     * 设置订单的NCid  当ncid为null 则更新订单的ncid为null
     *@param orderId
     *@param ncId
     * @author lintf
     * 2018年9月20日
     */
    void setNcId(Long orderId,String ncId);
    
    
}
