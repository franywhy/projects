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
import io.renren.entity.OrderMessageConsumerZYCDEntity;
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
import io.renren.service.SysDeptService;
import io.renren.service.UsersService;
import io.renren.utils.DateUtils;
import io.renren.utils.EncryptionUtils;
import sun.misc.BASE64Encoder;

@Controller
public class OrderMessageConsumerZYCDController implements ChannelAwareMessageListener {
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
		OrderMessageConsumerZYCDEntity orderMessageConsumerZYCDEntity = null; // 消费者实体类
		MallOrderSyncEntity mallOrderSync = new MallOrderSyncEntity(); // 异常消息实体类
		UsersEntity user = null; //学员实体类
		TransactionStatus status = null;
		// 开启事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务
		status = transactionManager.getTransaction(def); // 获得事务状态
		String body = new String(message.getBody(), "UTF-8");
		try {
			
			Gson gson = new Gson();
			orderMessageConsumerZYCDEntity = gson.fromJson(body, OrderMessageConsumerZYCDEntity.class);

			// 根据nc_commodity_id查找对应商品
			MallGoodsInfoEntity goodsInfo = mallGoodsInfoService.queryGoodsInfo(orderMessageConsumerZYCDEntity.getNc_commodity_id());
			// 根据nc_school_pk（对应部门表的nc_id）查找对应部门
			SysDeptEntity deptEntity = this.sysDeptService.queryObjectByNcId(orderMessageConsumerZYCDEntity.getNc_school_pk());
			logger.info("sync_rabbit_sign order={} goodsInfo=={}", orderMessageConsumerZYCDEntity, goodsInfo);
			if(null == goodsInfo){
				//生成学员信息
				// 根据同步订单的phone判断用户是否存在
				Map<String, Object> cmap = new HashMap<>();
				cmap.put("mobile", orderMessageConsumerZYCDEntity.getPhone());
				user = new UsersEntity();
				if (!this.usersService.mobileExist(cmap)) { //用户不存在则创建用户
					
					user.setUserId((long) userKGS.nextId());
					user.setMobile(orderMessageConsumerZYCDEntity.getPhone());
					user.setNickName(orderMessageConsumerZYCDEntity.getUser_name());
					user.setSex(orderMessageConsumerZYCDEntity.getSex());
					user.setPic("http://zikao.hqjy.com/Avatar.png");
					user.setEmail(null);
					user.setPassword(PSW);
					user.setCreator(null);
					user.setCreationTime(new Date());
					user.setModifier(null);
					user.setModifiedTime(null);
					user.setLastLoginIp(null);
					user.setLastLoginTime(null);
					user.setRemake(null);
					user.setNcId(orderMessageConsumerZYCDEntity.getNc_user_id());
					// TODO 部门id
					user.setDeptId(deptEntity.getDeptId());
					usersService.save(user);
				}
				//保存商品不存在异常
				mallOrderSync.setErrType(1);//错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常 5:无业务线
				saveMallOrderSync(mallOrderSync, orderMessageConsumerZYCDEntity, body);
				logger.info("sync_rabbit_sign goodsInfo == null>> mallOrderSync={}", mallOrderSync);
				return;
			}
			
			// 根据同步订单的商品GoodsId和province_pk查找对应省份
			Map<String, Object> map = new HashMap<>();
			map.put("ncProvinceId", orderMessageConsumerZYCDEntity.getProvince_pk());
			map.put("ncCommodityId", orderMessageConsumerZYCDEntity.getNc_commodity_id());
			Integer areaId = mallAreaService.queryAreaId(map);
			
			if (areaId == null || areaId < 0) {
				//保存省份不存在异常
				mallOrderSync.setErrType(2);//错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常 5:无业务线
				saveMallOrderSync(mallOrderSync, orderMessageConsumerZYCDEntity, body);
				logger.info("sync_rabbit_sign areaId == null>> mallOrderSync={}", mallOrderSync);
				return;
			}else{
				// 根据同步订单的phone判断用户是否存在
				Map<String, Object> cmap = new HashMap<>();
				cmap.put("mobile", orderMessageConsumerZYCDEntity.getPhone());
				user = new UsersEntity();
				if (!this.usersService.mobileExist(cmap)) { //用户不存在则创建用户
					
					user.setUserId((long) userKGS.nextId());
					user.setSchoolId(goodsInfo.getSchoolId());
					user.setMobile(orderMessageConsumerZYCDEntity.getPhone());
					user.setNickName(orderMessageConsumerZYCDEntity.getUser_name());
					user.setSex(orderMessageConsumerZYCDEntity.getSex());
					user.setPic("http://zikao.hqjy.com/Avatar.png");
					user.setEmail(null);
					user.setPassword(PSW);
					user.setCreator(null);
					user.setCreationTime(new Date());
					user.setModifier(null);
					user.setModifiedTime(null);
					user.setLastLoginIp(null);
					user.setLastLoginTime(null);
					user.setRemake(null);
					user.setNcId(orderMessageConsumerZYCDEntity.getNc_user_id());
					// TODO 部门id
					user.setDeptId(deptEntity.getDeptId());
					usersService.save(user);
				}
				// 根据同步订单报名表号code查询订单是否存在
				int orderExist = mallOrderService.queryOrderExist(null, orderMessageConsumerZYCDEntity.getCode(), null);
				if (orderExist == 0) {
					MallOrderEntity mallOrderEntity = new MallOrderEntity(); // 订单实体类
					
					// 判断用户是否存在
					Map<String, Object> usersMap = new HashMap<>();
					usersMap.put("mobile", orderMessageConsumerZYCDEntity.getPhone());
					int usersId = usersService.queryUserId(usersMap);
					if (usersId > 0) {
						// 用户id
						mallOrderEntity.setUserId((long) usersId);
					} else {
						// 用户id
						mallOrderEntity.setUserId(user.getUserId());
					}
					// 业务线
					mallOrderEntity.setBusinessId("kuaiji");
					// 支付状态 0.未支付 1.发起支付 ,2.支付成功
					mallOrderEntity.setPayStatus(2);
					// 订单号
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
					mallOrderEntity.setRemark(orderMessageConsumerZYCDEntity.getRemark());
					// 平台IP
					mallOrderEntity.setSchoolId(goodsInfo.getSchoolId());
					// 支付金额
					mallOrderEntity.setPayMoney(Double.valueOf(goodsInfo.getOriginalPrice()));
					// 支付時間 NC的支付时间--和有效期有关 
					mallOrderEntity.setPayTime(DateUtils.getDate(Long.valueOf(orderMessageConsumerZYCDEntity.getTs()), 1));
					// 订单有效期 date_validity
					mallOrderEntity.setDateValidity(DateUtils.orderDateValiditySync(DateUtils.getDate(Long.valueOf(orderMessageConsumerZYCDEntity.getTs()), 1), goodsInfo.getDayValidity()));
					// 产品线
					mallOrderEntity.setProductId(7L);
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
					mallOrderEntity.setCreationTime(new Date());
					// 最近修改用户
					mallOrderEntity.setModifier(null);
					// 最近修改时间
					mallOrderEntity.setModifiedTime(null);
					//// 来源 0.线上;1.NC;2測試
					mallOrderEntity.setSourceType(1);
					// NCID
					mallOrderEntity.setNcId(orderMessageConsumerZYCDEntity.getNc_id());
					// 省份ID
					mallOrderEntity.setAreaId(Long.valueOf(areaId));
					// nc同步code值
					mallOrderEntity.setNcCode(orderMessageConsumerZYCDEntity.getCode());
					// nc同步时间
					Date date = DateUtils.getDate(Long.valueOf(orderMessageConsumerZYCDEntity.getSyn_time()), 1);
					mallOrderEntity.setSynTime(date);
					// 层次ID
					mallOrderEntity.setLevelId(goodsInfo.getLevelId());
					
					// 根据省份（area_id），专业（profession_id），学历（level_id）查找对应班级ID
					Map<String, Object> classMap = new HashMap<>();
					classMap.put("areaId", areaId);
					classMap.put("professionId", goodsInfo.getProfessionId());
					classMap.put("levelId", goodsInfo.getLevelId());
					List<MallClassEntity> mallClassList = mallClassService.queryClassList(classMap);
					
					if(null != mallClassList && !mallClassList.isEmpty()){
//						// 班级ID
						mallOrderEntity.setClassId(mallClassList.get(0).getClassId());
					}
					// 班型ID
					mallOrderEntity.setClassTypeId(goodsInfo.getClassTypeId());
					// 专业ID
					mallOrderEntity.setProfessionId(goodsInfo.getProfessionId());
					// 状态
					mallOrderEntity.setStatus(1);
					// TODO 部门id
					mallOrderEntity.setDeptId(deptEntity.getDeptId());
					this.mallOrderService.saveOrder(mallOrderEntity);
				}
			}
			
		} catch (Exception e) {
			// 回滚
			transactionManager.rollback(status);
			e.printStackTrace();
			// 记录系统异常报错日志
			mallOrderSync.setErrType(4);//错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常 5:无业务线
			saveMallOrderSync(mallOrderSync, orderMessageConsumerZYCDEntity, body);
		} finally {
			try {
				// 提交事务
				transactionManager.commit(status);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} catch (Exception e) {
				// 记录系统异常报错日志
				mallOrderSync.setErrType(4);//错误类型 1:商品不存在 2：商品对应省份不存在3：没有该班级存在4：系统异常 5:无业务线
				saveMallOrderSync(mallOrderSync, orderMessageConsumerZYCDEntity, body);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		}
	}
	
	private void saveMallOrderSync(MallOrderSyncEntity mallOrderSync, OrderMessageConsumerZYCDEntity orderMessageConsumerZYCDEntity, String body){
		mallOrderSync.setCreateTime(new Date(System.currentTimeMillis()));//创建时间
		mallOrderSync.setNcId(orderMessageConsumerZYCDEntity.getNc_id());//NC主键
		mallOrderSync.setNcCreateTime(new Date(orderMessageConsumerZYCDEntity.getCreate_time()));//NC创建时间
		mallOrderSync.setSchoolId(null);
		mallOrderSync.setNcClassTypeId("kuaiji");//NC业务平台
		mallOrderSync.setDr(0);//删除标志0：不删除 1：删除
		mallOrderSync.setState(0);//状态 0:已处理 1:未处理 2:过期不处理
		
		Date syncdate = DateUtils.getDate(Long.valueOf(orderMessageConsumerZYCDEntity.getSyn_time()), 1);
		mallOrderSync.setSynTime(syncdate);//NC同步时间
		
		mallOrderSync.setNcJson(body);//同步信息
		mallOrderSync.setMobile(orderMessageConsumerZYCDEntity.getPhone());//nc同步过来的手机号
		mallOrderSync.setNcCode(orderMessageConsumerZYCDEntity.getCode());//nc同步过来的报名表号
		
		this.mallOrderSyncService.save(mallOrderSync);
	}
	
}
