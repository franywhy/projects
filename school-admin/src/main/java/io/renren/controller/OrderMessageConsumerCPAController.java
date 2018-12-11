package io.renren.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import io.renren.entity.MallOrderEntity;
import io.renren.entity.MallOrderSyncEntity;
import io.renren.entity.MallOrderSyncXEntity;
import io.renren.entity.OrderMessageConsumerCPAEntity;
import io.renren.entity.SysDeptEntity;
import io.renren.entity.UsersEntity;
import io.renren.pojo.order.OrderPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.MallAreaService;
import io.renren.service.MallClassService;
import io.renren.service.MallClassTypeService;
import io.renren.service.MallGoodsInfoService;
import io.renren.service.MallOrderService;
import io.renren.service.MallOrderSyncService;
import io.renren.service.MallOrderSyncXService;
import io.renren.service.SysDeptService;
import io.renren.service.UsersService;
import io.renren.utils.DateUtils;
import io.renren.utils.EncryptionUtils;
import sun.misc.BASE64Encoder;

@Controller
public class OrderMessageConsumerCPAController implements ChannelAwareMessageListener {
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
	private MallClassTypeService mallClassTypeService;
	@Autowired
	private MallClassService mallClassService;
	@Autowired
	private MallOrderSyncService mallOrderSyncService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private MallOrderSyncXService mallOrderSyncXService;

	@Autowired
	private DataSourceTransactionManager transactionManager;

	/** 默认密码 hqzk123456 */
	private static String PSW = "1c3f360330c442c3cc62d1608fe7a3a3";

	@Value("#{application['user.psw']}")
	private void setPSW(String str) {
		PSW = str;
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		logger.info("收到队列信息" + " [x] Received '" + message.getBody() + "'");
		// TODO
		OrderMessageConsumerCPAEntity orderMessageConsumerCPAEntity = null; //CPA同步消费者实体类
		UsersEntity user=null;
		
		TransactionStatus status = null;
		// 开启事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
		status = transactionManager.getTransaction(def); // 获得事务状态
		String body = null;
		try {
			body = new String(message.getBody(), "UTF-8");
			Gson gson = new Gson();
			orderMessageConsumerCPAEntity = gson.fromJson(body, OrderMessageConsumerCPAEntity.class);
			// 不存在相同的报名表
			if(0 == mallOrderService.queryOrderExist(null, orderMessageConsumerCPAEntity.getSign_code(), null)){
				Map<String, Object> areaMap = new HashMap<>();
				areaMap.put("areaName", orderMessageConsumerCPAEntity.getArea_name());
				//省份id
				Long areaId = this.mallAreaService.queryAreaIdByName(areaMap);
				
				Map<String, Object> commodityMap = new HashMap<>();
				commodityMap.put("id", orderMessageConsumerCPAEntity.getCommodity_id());
				// 根据commodity_id查找对应商品
				MallGoodsInfoEntity goodsInfo = mallGoodsInfoService.queryObject(commodityMap);
				if(null != goodsInfo){//商品存在
					if(null != areaId){//省份存在
						// 查询默认班级 ==>根据省份（area_id），专业（profession_id），学历（level_id）查找对应班级ID
//						Map<String, Object> classMap = new HashMap<>();
//						classMap.put("areaId", areaId);
//						classMap.put("professionId", goodsInfo.getProfessionId());
//						classMap.put("levelId", goodsInfo.getLevelId());
//						MallClassEntity mallClass = mallClassService.queryClassId(classMap);
						//班级ID
//						Long classId = null;
//						if(mallClass != null){//班级存在
//							classId = mallClass.getClassId();
//						}
						Map<String, Object> checkUserMap = new HashMap<>();
						checkUserMap.put("schoolId", goodsInfo.getSchoolId());
						checkUserMap.put("mobile", orderMessageConsumerCPAEntity.getUser_mobile());
						Long user_id = null;
						MallOrderEntity mallOrderEntity = new MallOrderEntity(); // 订单实体类
						// 判断号码是否存在
						if (!this.usersService.checkMobile(checkUserMap)) {//用户不存在
							// 如果是生成订单就插入用户信息
							user = new UsersEntity();
							user.setUserId((long) userKGS.nextId());
							user.setSchoolId(goodsInfo.getSchoolId());
							user.setMobile(orderMessageConsumerCPAEntity.getUser_mobile());
							user.setNickName(orderMessageConsumerCPAEntity.getUser_name());
							user.setPic("http://zikao.hqjy.com/Avatar.png");
							user.setSex(2);
							user.setEmail(null);
							user.setPassword(PSW);
							user.setCreator(null);
							user.setCreationTime(new Date());
							user.setModifier(null);
							user.setModifiedTime(null);
							user.setLastLoginIp(null);
							user.setLastLoginTime(null);
							user.setRemake(null);
							user.setDeptId(725L);
							usersService.save(user);
							//用户ID
							mallOrderEntity.setUserId(user.getUserId());
						}else{//用户存在
							Map<String, Object> usersMap = new HashMap<>();
							usersMap.put("mobile", orderMessageConsumerCPAEntity.getUser_mobile());
							// 判断用户是否存在
							user_id = Long.valueOf(usersService.queryUserId(usersMap));
							//用户ID
							mallOrderEntity.setUserId(user_id);
						}
						
						//班型
						mallOrderEntity.setClassTypeId(goodsInfo.getClassTypeId());
						//班级
						mallOrderEntity.setClassId(orderMessageConsumerCPAEntity.getClass_id());
						//支付状态
						mallOrderEntity.setPayStatus(2);
						//订单号
						mallOrderEntity.setOrderNo(ORDERNO_HEAD + orderNoKGS.nextId());
						// 商品名称
						mallOrderEntity.setCommodityName(goodsInfo.getName());
						// 商品ID
						mallOrderEntity.setCommodityId(goodsInfo.getId());
						// 订单名称
						mallOrderEntity.setOrderName(goodsInfo.getName());
						// 订单描述
						mallOrderEntity.setOrderDescribe(goodsInfo.getName());
						// 订单总额
						mallOrderEntity.setTotalMoney(Double.valueOf(goodsInfo.getOriginalPrice()));
						// 备注
						mallOrderEntity.setRemark(null);
						// 平台IP
						mallOrderEntity.setSchoolId(goodsInfo.getSchoolId());
						// 支付金額
						mallOrderEntity.setPayMoney(Double.valueOf(goodsInfo.getOriginalPrice()));
						// 支付時間
						mallOrderEntity.setPayTime(DateUtils.parse(orderMessageConsumerCPAEntity.getSign_time()));
						// 订单有效期
						mallOrderEntity.setDateValidity(DateUtils.orderDateValiditySync(DateUtils.parse(orderMessageConsumerCPAEntity.getSign_time()), goodsInfo.getDayValidity()));
						// 优惠金额
						mallOrderEntity.setFavorableMoney(0.0);
						// 优惠券ID
						mallOrderEntity.setDiscountId(null);
						// 第三方支付回调时间
						mallOrderEntity.setPayCallblackTime(new Date());
						// 第三方支付回调信息
						mallOrderEntity.setPayCallblackMsg(null);
						// 支付宝交易号
						mallOrderEntity.setAlipayTradeNo(null);
						// 支付IP
						mallOrderEntity.setPayip(null);
						// 生成支付地址
						mallOrderEntity.setPayUrl(null);
						// 商品图片
						mallOrderEntity.setPic(goodsInfo.getOriginPath());
						// 商品小图
						mallOrderEntity.setSpic(goodsInfo.getThumbPath());
						// dr 0.正常 1.删除
						mallOrderEntity.setDr(0);
						// 用户操作状态 0.正常 1.取消 2.删除
						mallOrderEntity.setUstatus(0);
						// 微信开放ID
						mallOrderEntity.setWxOpenId(null);
						// 0.未选择 1.支付宝 2.微信
						mallOrderEntity.setPayType(0);
						// 银行编码
						mallOrderEntity.setBankCode(null);
						// 银行名称
						mallOrderEntity.setBankName(null);
						// 創建用戶
						mallOrderEntity.setCreator(null);
						// 创建时间
						mallOrderEntity.setCreationTime(DateUtils.parse(orderMessageConsumerCPAEntity.getSign_time()));
						// 最近修改用户
						mallOrderEntity.setModifier(null);
						// 最近修改时间
						mallOrderEntity.setModifiedTime(null);
						//// 来源 0.线上;1.NC;2測試
						mallOrderEntity.setSourceType(1);
						// NCID
						mallOrderEntity.setNcId(null);
						// 省份ID
						mallOrderEntity.setAreaId(areaId);
						// nc同步code值
						mallOrderEntity.setNcCode(orderMessageConsumerCPAEntity.getSign_code());
						// nc同步时间
						mallOrderEntity.setSynTime(DateUtils.parse(orderMessageConsumerCPAEntity.getSign_time().toString()));
						// 层次ID
						mallOrderEntity.setLevelId(goodsInfo.getLevelId());
						//专业ID
						mallOrderEntity.setProfessionId(goodsInfo.getProfessionId());
						//产品线
						mallOrderEntity.setProductId(7L);
						//部门
						mallOrderEntity.setDeptId(725L);
						//业务线
						mallOrderEntity.setBusinessId("kuaiji");
						mallOrderEntity.setNcTs(new Date());
						mallOrderEntity.setStatus(1);
						//保存
						this.mallOrderService.saveOrder(mallOrderEntity);
					}else{//省份不存在
						saveErr(body, orderMessageConsumerCPAEntity, 2);
					}
				}else{//商品不存在
					saveErr(body, orderMessageConsumerCPAEntity, 1);
				}
			}
			
		} catch (Exception e) {
			// 回滚
			transactionManager.rollback(status);
			e.printStackTrace();
			saveErr(body, orderMessageConsumerCPAEntity, 4);
		} finally {
			try {
				// 提交事务
				transactionManager.commit(status);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} catch (Exception e) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		}
	}
	
	/**
	 * 保存错误信息
	 * @param json	json
	 * @param ms	转换对象
	 * @param type	错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常
	 */
	private void saveErr(String json , OrderMessageConsumerCPAEntity ms , int type){
		MallOrderSyncXEntity entity = new MallOrderSyncXEntity();
		entity.setCreateTime(new Date());
		entity.setErrType(type);
		entity.setJson(json);
		try {
			if(null != ms){
				entity.setNcSignCode(ms.getSign_code());
				entity.setNcSignTime(DateUtils.parse(ms.getSign_time()));
				entity.setState(0);
				entity.setSynTime(new Date());
				entity.setUserMobile(ms.getUser_mobile());
				entity.setUserName(ms.getUser_name());
				entity.setZkCommodityId(Integer.valueOf(ms.getCommodity_id()));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}finally{
			mallOrderSyncXService.save(entity);
		}
	}
}
