package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;

import io.renren.entity.MallClassEntity;
import io.renren.entity.MallGoodsInfoEntity;
import io.renren.entity.MallOrderSyncXEntity;
import io.renren.entity.OrderMessageConsumer2Entity;
import io.renren.entity.UsersEntity;
import io.renren.pojo.order.OrderPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.MallAreaService;
import io.renren.service.MallClassService;
import io.renren.service.MallGoodsInfoService;
import io.renren.service.MallOrderService;
import io.renren.service.MallOrderSyncXService;
import io.renren.service.UsersService;
import io.renren.utils.DateUtils;

@Controller
public class OrderMessageConsumer2Controller implements ChannelAwareMessageListener {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static final String MD5_KEY = "%^\\$AF>.12*******zK";
	@Resource
	KGS orderNoKGS;
	@Resource
	KGS userKGS;
	private static final String ORDERNO_HEAD = "nc_";
	@Autowired
	private MallOrderService mallOrderService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private MallGoodsInfoService mallGoodsInfoService;
	@Autowired
	private MallAreaService mallAreaService;
	@Autowired
	private MallClassService mallClassService;
	@Autowired
	private MallOrderSyncXService mallOrderSyncXService;

	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	/** 默认密码 hqzk123456 */
	private static String PSW = "1c3f360330c442c3cc62d1608fe7a3a3";
	@Value("#{application['user.psw']}")
	private void setPSW(String str){
		PSW = str;
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		//System.out.println("收到队列信息" + " [x] Received '" + message + "'");
		TransactionStatus status = null;
		UsersEntity user=null;
		OrderMessageConsumer2Entity order = null;
		//开启事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
		status = transactionManager.getTransaction(def); // 获得事务状态
		
		String body = null;
		try {
			body = new String(message.getBody(), "UTF-8");
			Gson gson = new Gson();
			order = gson.fromJson(body, OrderMessageConsumer2Entity.class);
			//不存在相同的报名表
			if(0 == mallOrderService.queryOrderExist(null, order.getNc_sign_code(), order.getOrder_no())){
				if(StringUtils.isBlank(order.getOrder_no())){
					order.setOrder_no(ORDERNO_HEAD + orderNoKGS.nextId());
				}
				//省份
				Long areaId = Long.valueOf(order.getZk_area_id());
				
				Map<String, Object> queryGoodMap = new HashMap<>();
				queryGoodMap.put("id", order.getZk_commodity_id());
				// 根据nc_commodity_id查找对应商品
				MallGoodsInfoEntity goodsInfo = mallGoodsInfoService.queryObject(queryGoodMap );
				if(null != goodsInfo){//商品存在
					Map<String, Object> queryAreaMap = new HashMap<>();
					queryAreaMap.put("goodId", order.getZk_commodity_id());
					queryAreaMap.put("areaId", areaId);
					Integer queryAreaIdByZKAreaId = mallAreaService.queryAreaIdByZKAreaId(queryAreaMap );
					if(null != queryAreaIdByZKAreaId){//省份存在
						// 查询默认班级 ==>根据省份（area_id），专业（profession_id），学历（level_id）查找对应班级ID
						Map<String, Object> classMap = new HashMap<>();
						classMap.put("areaId", areaId);
						classMap.put("professionId", goodsInfo.getProfessionId());
						classMap.put("levelId", goodsInfo.getLevelId());
						MallClassEntity mallClass = mallClassService.queryClassId(classMap);
						//班级ID
						Long classId = null;
						if(mallClass != null){//班级存在
							classId = mallClass.getClassId();
						}
//						if(mallClass != null){//班级存在
							Map<String, Object> checkUserMap = new HashMap<>();
							checkUserMap.put("schoolId", goodsInfo.getSchoolId());
							checkUserMap.put("mobile", order.getUser_mobile());
							Long user_id = null;
							// 判断号码是否存在
							if (!this.usersService.checkMobile(checkUserMap)) {//用户不存在
								// 如果是生成订单就插入用户信息
								user = new UsersEntity();
								user_id = (long) userKGS.nextId();
								user.setUserId(user_id);
								user.setSchoolId(goodsInfo.getSchoolId());
								user.setMobile(order.getUser_mobile());
								user.setNickName(order.getUser_name());
								user.setPic(null);
								user.setSex(1);
								user.setEmail(null);
								
								user.setPassword(PSW);
								user.setCreator(null);
								user.setCreationTime(new Date());
								user.setModifier(null);
								user.setModifiedTime(null);
								user.setLastLoginIp(null);
								user.setLastLoginTime(null);
								user.setRemake(null);
								usersService.save(user);
							}else{//用户存在
								Map<String, Object> usersMap = new HashMap<>();
								usersMap.put("mobile", order.getUser_mobile());
								// 判断用户是否存在
								user_id = Long.valueOf(usersService.queryUserId(usersMap));
							}
							
							//创建订单对象
							OrderPOJO mallOrder = new OrderPOJO();
							//用户ID
							mallOrder.setUserId(user_id);
							//班型
							mallOrder.setClassTypeId(goodsInfo.getClassTypeId());
							//班级
							mallOrder.setClassId(classId);
//							mallOrder.setClassId(mallClass.getClassId());
							//支付状态
							mallOrder.setPayStatus(2);
							// 訂單號
							mallOrder.setOrderNo(order.getOrder_no());
							// 商品名称
							mallOrder.setCommodityName(goodsInfo.getName());
							// 商品ID
							mallOrder.setCommodityId(goodsInfo.getId());
							// 订单名称
							mallOrder.setOrderName(goodsInfo.getName());
							// 订单描述
							mallOrder.setOrderDescribe(goodsInfo.getName());
							// 订单总额
						    mallOrder.setTotalMoney(Double.valueOf(goodsInfo.getOriginalPrice()));
							//mallOrder.setTotalMoney(null);
							// 备注
							mallOrder.setRemark(null);
							// 平台IP
							mallOrder.setSchoolId(goodsInfo.getSchoolId());
							// 支付金額
							mallOrder.setPayMoney(Double.valueOf(goodsInfo.getOriginalPrice()));
							// 支付時間
							mallOrder.setPayTime(DateUtils.parse(order.getNc_sign_time()));
							// 优惠金额
							mallOrder.setFavorableMoney(0.0);
							// 优惠券ID
							mallOrder.setDiscountId(null);
							// 第三方支付回调时间
							mallOrder.setPayCallblackTime(new Date());
							// 第三方支付回调信息
							mallOrder.setPayCallblackMsg(null);
							// 支付宝交易号
							mallOrder.setAlipayTradeNo(null);
							// 支付IP
							mallOrder.setPayip(null);
							// 生成支付地址
							mallOrder.setPayUrl(null);
							// 商品图片
							mallOrder.setPic(goodsInfo.getOriginPath());

							// 商品小图
							mallOrder.setSpic(goodsInfo.getThumbPath());
							// dr 0.正常 1.删除
							mallOrder.setDr(0);
							// 用户操作状态 0.正常 1.取消 2.删除
							mallOrder.setUstatus(0);
							// 微信开放ID
							mallOrder.setWxOpenId(null);
							// 0.未选择 1.支付宝 2.微信
							mallOrder.setPayType(0);
							// 银行编码
							mallOrder.setBankCode(null);
							// 银行名称
							mallOrder.setBankName(null);
							// 創建用戶
							mallOrder.setCreator(null);
							// 创建时间
							mallOrder.setCreationTime(DateUtils.parse(order.getNc_sign_time()));
							// 最近修改用户
							mallOrder.setModifier(null);
							// 最近修改时间
							mallOrder.setModifiedTime(null);
							//// 来源 0.线上;1.NC;2測試
							mallOrder.setSourceType(1);
							// NCID
							mallOrder.setNcId(null);
							// 省份ID
							mallOrder.setAreaId(areaId);
							// nc同步code值
							mallOrder.setNcCode(order.getNc_sign_code());
							// nc同步时间
							mallOrder.setSynTime(DateUtils.parse(order.getNc_sign_time().toString()));
							// 层次ID
							mallOrder.setLevelId(goodsInfo.getLevelId());
							//专业ID
							mallOrder.setProfessionId(goodsInfo.getProfessionId());
							//保存
							mallOrderService.save(mallOrder);
//						}else{//班型=null
//							saveErr(body, order, 3);
//						}
					}else{//省份不存在
						saveErr(body, order, 2);
					}
				}else{//商品不存在
					saveErr(body, order, 1);
				}
			}else{//订单存在
				
			}
			
		}catch (Exception e) {
		    transactionManager.rollback(status);
			// 记录报错日志
			e.printStackTrace();
			saveErr(body, order, 4);
		} finally {
			try{
			//提交事务
			transactionManager.commit(status);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}catch(Exception e){}
		}
	}
	/**
	 * 保存错误信息
	 * @param json	json
	 * @param ms	转换对象
	 * @param type	错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常
	 */
	private void saveErr(String json , OrderMessageConsumer2Entity ms , int type){
		MallOrderSyncXEntity entity = new MallOrderSyncXEntity();
		entity.setCreateTime(new Date());
		entity.setErrType(type);
		entity.setJson(json);
		try {
			if(null != ms){
				entity.setNcSignCode(ms.getNc_sign_code());
				entity.setNcSignTime(DateUtils.parse(ms.getNc_sign_time()));
				entity.setOrderNo(ms.getOrder_no());
				entity.setState(0);
				entity.setSynTime(new Date());
				entity.setUserMobile(ms.getUser_mobile());
				entity.setUserName(ms.getUser_name());
				entity.setZkCommodityId(Integer.valueOf(ms.getZk_commodity_id()));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}finally{
			mallOrderSyncXService.save(entity);
		}
	}
}
