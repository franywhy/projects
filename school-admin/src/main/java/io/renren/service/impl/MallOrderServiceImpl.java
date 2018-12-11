package io.renren.service.impl;

import java.util.*;

import com.google.gson.Gson;
import io.renren.entity.ClassToTkLogEntity;
import io.renren.entity.UserOldclassLogEntity;
import io.renren.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.MallOrderDao;
import io.renren.entity.CourseUserplanEntity;
import io.renren.entity.MallOrderEntity;
import io.renren.pojo.SyncCustomerPOJO;
import io.renren.pojo.order.OrderPOJO;
import io.renren.service.MessageVideoCourseService;
import io.renren.utils.Constant;
import io.renren.utils.DateUtils;
import io.renren.utils.SendUdeskUtil;


@Transactional(readOnly = true)
@Service("mallOrderService")
public class MallOrderServiceImpl implements MallOrderService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
    //ThreadPoolExecutor poolExecutor = ThreadPoolExecutorUtils.getDefaultThreadPoolExecutor();
    /** 推送会计班级消息队列名 */
    private static String KJ_CLASS_MESSAGE = "";
    @Value("#{rabbitmq['kj.class.sync.tk']}")
    private void setKJ_CLASS_MESSAGE(String str){
        KJ_CLASS_MESSAGE = str;
        logger.info("MessageKJClassServiceImpl setKJ_CLASS_MESSAGE KJ_CLASS_MESSAGE={}",KJ_CLASS_MESSAGE);
    }
    /** 推送自考班级消息队列名 */
    private static String ZK_CLASS_MESSAGE = "";
    @Value("#{rabbitmq['divide.class.sync.tk']}")
    private void setZK_CLASS_MESSAGE(String str){
        ZK_CLASS_MESSAGE = str;
        logger.info("MessageVideoCourseServiceImpl setZK_CLASS_MESSAGE={}",ZK_CLASS_MESSAGE);
    }
    /** 同步学员信息到Udesk消息队列名 */
    private static String SYNC_UDESK_MESSAGE = "";
    @Value("#{rabbitmq['customer.sync.udesk']}")
    private void setDIVIDE_CLASS_MESSAGE(String str){
    	SYNC_UDESK_MESSAGE = str;
    }
    @Autowired
    private AmqpTemplate amqpTemplate;
	@Autowired
	private MallOrderDao mallOrderDao;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
	private CourseUserplanDetailService courseUserplanDetailService;
	@Autowired
	private MallGoodsInfoService mallGoodsInfoService;
	@Autowired
	private MessageVideoCourseService messageVideoCourseService;
    @Autowired
    private CourseUserplanClassService courseUserplanClassService;
    @Autowired
    private CourseUserplanClassDetailService courseUserplanClassDetailService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private UserOldclassLogService userOldclassLogService;
    @Autowired
    private ClassToTkLogService classToTkLogService;
    @Autowired
    private ContractRecordService contractRecordService;
    @Autowired
    private RecordInfoService recordInfoService;
    
	@Override
	public MallOrderEntity queryObject(Map<String,Object> map){
		return mallOrderDao.queryObject(map);
	}
	
	@Override
	public List<OrderPOJO> queryList(Map<String, Object> map){
        return mallOrderDao.queryPOJO(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallOrderDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(OrderPOJO mallOrder){
		
		mallOrder.setDr(0);
		mallOrder.setCreationTime(new Date());
		mallOrder.setModifiedTime(mallOrder.getCreationTime());
//		mallOrder.setTotalMoney(mallOrder.geto);
		MallOrderEntity en = OrderPOJO.getEntity(mallOrder);
		//商品信息
		Map<String , Object> queryGoodsMap = new HashMap<>();
		queryGoodsMap.put("schoolId", mallOrder.getSchoolId());
		queryGoodsMap.put("id", mallOrder.getCommodityId());
		Map<String, Object> goodsMap = mallGoodsInfoService.queryMap(queryGoodsMap);
		if(null != goodsMap){
			en.setProductId((Long) goodsMap.get("productId"));//商品对应的产品线
			en.setDateValidity(DateUtils.orderDateValidity((Long) goodsMap.get("dayValidity")));//订单有效期
		}
		mallOrderDao.save(en);
		/**
		 *检测是否要生成学员档案基础信息
		 */
		recordInfoService.CheckRecordInfoByOrder(en);
		//发送到Udesk消息队列
		try {
			if(null != en.getClassId() && en.getClassId() > 0){
				sendUdeskMq(en.getOrderId() , en.getClassId());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			//生产新订单,根据商品是否为录播课,推送到题库
			messageVideoCourseService.pushToMessageQueueClass(null, null, en.getOrderNo(),null);
		}catch(Exception e){
			logger.error("MessageVideoCourseService.pushToMessageQueueClass video course to tiku fail,orderNo:{},error message:{}",en.getOrderNo(),e.toString());
		}
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveGiveOrder(OrderPOJO mallOrder){
		
		mallOrder.setDr(0);
		mallOrder.setCreationTime(new Date());
		mallOrder.setModifiedTime(mallOrder.getCreationTime());
//		mallOrder.setTotalMoney(mallOrder.geto);
		MallOrderEntity en = OrderPOJO.getEntity(mallOrder);
		//商品信息
		Map<String , Object> queryGoodsMap = new HashMap<>();
		queryGoodsMap.put("schoolId", mallOrder.getSchoolId());
		queryGoodsMap.put("id", mallOrder.getCommodityId());
		Map<String, Object> goodsMap = mallGoodsInfoService.queryMap(queryGoodsMap);
		if(null != goodsMap){
			en.setProductId((Long) goodsMap.get("productId"));//商品对应的产品线
			en.setDateValidity(DateUtils.orderDateValidity((Long) goodsMap.get("dayValidity")));//订单有效期
		}
		mallOrderDao.save(en);
		
		try{
			//生产新订单,根据商品是否为录播课,推送到题库
			messageVideoCourseService.pushToMessageQueueClass(null, null, en.getOrderNo(),null);
		}catch(Exception e){
			logger.error("MessageVideoCourseService.pushToMessageQueueClass video course to tiku fail,orderNo:{},error message:{}",en.getOrderNo(),e.toString());
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(OrderPOJO mallOrder){
        Gson gson = new Gson();
		mallOrder.setModifiedTime(new Date());
		MallOrderEntity en = OrderPOJO.getEntity(mallOrder);
		Map map = new HashMap();
		map.put("list",mallOrder.getOrderList());
		map.put("modifiedTime",new Date());
		map.put("modifier",mallOrder.getUserId());
		map.put("classId",mallOrder.getClassId());
        try{
		//转班前先记录好旧的班级信息,方便推送给题库
            for (Long orderId : mallOrder.getOrderList()) {
                Map<String, Object> mapResult = this.queryKJClasstoTK(orderId);
                if (mapResult != null){
                    UserOldclassLogEntity entity = new UserOldclassLogEntity();
                    entity.setOrderNo((String) mapResult.get("orderNo"));
                    entity.setGoodId((Long) mapResult.get("goodId"));
                    entity.setClassId((Long) mapResult.get("classId"));
                    entity.setClassName((String) mapResult.get("className"));
                    entity.setClasstypeId((Long) mapResult.get("classTypeId"));
                    entity.setClasstypeName((String) mapResult.get("classTypeName"));
                    entity.setClassTeacherId((Long) mapResult.get("classTeacherId"));
                    entity.setClassTeacherNickname((String) mapResult.get("classTeacerNickName"));
                    entity.setClassTeacherMobile((String) mapResult.get("classTeacherAccount"));
                    entity.setUserId((Long) mapResult.get("userId"));
                    entity.setNickName((String) mapResult.get("nickName"));
                    entity.setMobile((String) mapResult.get("mobile"));
                    entity.setOnlyOne(0);
                    entity.setOpen(0);
                    entity.setIsNewClass(0);
                   /* poolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {*/
                            userOldclassLogService.save(entity);
                        }
                  /*  });
                }*/
            }
		mallOrderDao.updateBatchClass(map);
		
		//发送Udesk消息队列
		try {
			sendUdeskMq(mallOrder.getOrderList() , mallOrder.getClassId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		CourseUserplanEntity courseUserplan = new CourseUserplanEntity();
		courseUserplan.setClassId(en.getClassId());

		//修改每个订单的学员规划
        for (Long orderId : mallOrder.getOrderList()) {
            courseUserplan.setOrderId(orderId);
            //商品信息
            Map<String , Object> queryGoodsMap = new HashMap<>();
            queryGoodsMap.put("schoolId", mallOrder.getSchoolId());
            queryGoodsMap.put("id", mallOrder.getCommodityId());
            Map<String, Object> goodsMap = mallGoodsInfoService.queryMap(queryGoodsMap);
            if(null != goodsMap){
                en.setProductId((Long) goodsMap.get("productId"));//商品对应的产品线
//			en.setDateValidity(DateUtils.orderDateValidity((Long) goodsMap.get("dayValidity")));//订单有效期
            }
            //修改订单指定班级的同时也修改学员规划的班级,根据订单id修改学员规划班级
            courseUserplanService.updateByOrderId(courseUserplan);
                //发送消息给题库
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("orderId", orderId);
                Map<String, Object> mapResult = this.queryKJClasstoTK(orderId);
                if (mapResult != null){
                        List<String> codeList = courseUserplanService.queryCodeListByCommodityId(mapResult.get("goodId"));
                        mapResult.put("courseTkCode", tkCourseCode(codeList));
                        mapResult.put("open", 1);
                        mapResult.put("isNewClass",1);
                        mapDateFormatter(mapResult, "ts");
                        String json = gson.toJson(mapResult).toString();
                        logger.info("MessageVideoClassServiceImpl pushToMessageQueueClass json:{}", json);
                        ClassToTkLogEntity entity = new ClassToTkLogEntity();
                        entity.setCreatetime(new Date());
                        entity.setPushJson(json);
                        entity.setUserId((Long) mapResult.get("userId"));
                        entity.setGoodId((Long) mapResult.get("goodId"));
                        entity.setUserMobile((String) mapResult.get("mobile"));
                        entity.setRemark("转班后:MallOrderServiceImpl");
                        /*poolExecutor.execute(new Runnable() {
                            @Override
                            public void run() {*/
                                classToTkLogService.save(entity);
                         /*   }
                        });*/
                        amqpTemplate.convertAndSend(KJ_CLASS_MESSAGE, json);
                    }
                }
        }catch(Exception e){
            logger.error("messageKJClassService.pushToMessageQueueClass open permission to tiku fail,orderNo:{},error message:{}",en.getOrderNo(),e.toString());
        }
    }
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateValidate(OrderPOJO mallOrder) {
		mallOrder.setModifiedTime(new Date());
		MallOrderEntity en = OrderPOJO.getEntity(mallOrder);
		mallOrderDao.update(en);
		long userId=mallOrder.getUserId();
		MallOrderEntity logOrder=new MallOrderEntity();
		logOrder.setUserId(userId);
		logOrder.setOrderId(en.getOrderId());
		logOrder.setDateValidity(en.getDateValidity());
		logOrder.setOldDateValidity(en.getOldDateValidity());
		mallOrderDao.saveLogOrder(logOrder);
	}
	@Override
	public void update(MallOrderEntity mallOrderEntity) {
		mallOrderDao.update(mallOrderEntity);
	}
	@Override
	public void delete(Long orderId){
		mallOrderDao.delete(orderId);
		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Long[] orderIds){
		try {
			//删除Udesk学员账号
			this.deleteUdeskCustomer(orderIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//增加删除订单时 删除协议记录
			try {
		 
				contractRecordService.ContractRecordDeleteBatch(orderIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO
//		int userplanNum = this.courseUserplanService.queryUserplanByOrderId(orderIds[0]);
//		if(userplanNum > 0){
//			this.mallOrderDao.updateDrByOrderIds(orderIds);
//		}else{
//			mallOrderDao.deleteBatch(orderIds);
//		}
		mallOrderDao.deleteBatch(orderIds);
		for(int i=0;i<orderIds.length;i++){
			//发送消息到题库关闭权限
            this.sendMsgToTK(orderIds[i],0);
            //设置学员规划,学员规划详情,学习计划,学习计划详情dr=1;
            CourseUserplanEntity courseUserplanEntity = courseUserplanService.queryUserplanObjectByOrderId(orderIds[i]);
            if (courseUserplanEntity != null) {
                courseUserplanService.updateChangeByOrderNo(orderIds[i]);
                courseUserplanClassService.updateChangeByOrderNo(courseUserplanEntity.getUserPlanId());
                //s设置学习计划详情dr = 1
                List<Long> courseUserplanClassIdList = courseUserplanClassService.queryCourseUserplanClass(courseUserplanEntity.getUserPlanId());
                if (courseUserplanClassIdList.size() > 0 ){
                    for (Long courseUserplanClassId : courseUserplanClassIdList) {
                        courseUserplanClassDetailService.updateChangeByUserplanClassId(courseUserplanClassId);
                    }
                }
            }
        }
			/*int userplanNum = this.courseUserplanService.queryUserplanByOrderId(orderIds[i]);
			if(userplanNum > 0){
				this.courseUserplanService.deleteByOrderId(orderIds[i]);
			}
		}*/
		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] orderIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", orderIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallOrderDao.updateBatch(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] orderIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", orderIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallOrderDao.updateBatch(map);
	}

	@Override
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
	public List<Map<String , Object>> queryListGrid(Map<String , Object> map) {
		return this.mallOrderDao.queryListGrid(map);
	}

	@Override
	public int queryListGridTotal(Map<String, Object> map) {
		return this.mallOrderDao.queryListGridTotal(map);
	}

	@Override
	public List<Map<String, Object>> queryOrderForChangeUserplan(Map<String, Object> map) {
		MallOrderEntity order = this.queryObject(map);
		if(null != order){
			map.put("userId", order.getUserId());
			return this.mallOrderDao.queryOrderForChangeUserplan(map);
		}
		return null;
	}

	@Override
	public int queryOrderForChangeUserplanTotal(Map<String, Object> map) {
		return  this.mallOrderDao.queryOrderForChangeUserplanTotal(map);
	}
	
	/**
	 * 查询可生成学员规划的订单详情
	 */
	public MallOrderEntity queryOrderForUserplan(Map<String , Object> map){return this.mallOrderDao.queryOrderForUserplan(map);}

	@Override
	public int checkClassType(long id) {
	
		return this.mallOrderDao.checkClassType(id);
	}

	@Override
	public int checkProfession(long id) {
		return this.mallOrderDao.checkProfession(id);
	}

	@Override
	public int queryOrderExist(String nc_id , String nc_code , String order_no) {
		Integer num = 0;
		if(StringUtils.isNotBlank(nc_id)){//校验ncid
			num = this.mallOrderDao.queryOrderExistByNcId(nc_id);
		}
		if(num == 0 && StringUtils.isNotBlank(nc_code)){//校验nc_code
			num = this.mallOrderDao.queryOrderExistByNcCode(nc_code);
		}
		if(num == 0 && StringUtils.isNotBlank(order_no)){//校验order_no
			num = this.mallOrderDao.queryOrderExistByOrderNo(order_no);
		}
		return num;
	}
	@Transactional(readOnly = false)
	@Override
	public void updateDr(OrderPOJO mallOrder) {
		//商品信息
		Map<String , Object> queryGoodsMap = new HashMap<>();
		queryGoodsMap.put("schoolId", mallOrder.getSchoolId());
		queryGoodsMap.put("id", mallOrder.getCommodityId());
		Map<String, Object> goodsMap = mallGoodsInfoService.queryMap(queryGoodsMap);
		if(null != goodsMap){
			mallOrder.setProductId((Long) goodsMap.get("productId"));//商品对应的产品线
		}
		this.mallOrderDao.updateDr(mallOrder);
	}


	@Override
	public long getUserplanDetailId(Map<String, Object> map) {
		return this.mallOrderDao.getUserplanDetailId(map);
	}

	@Override
	public int countUserplanDetailId(Map<String, Object> map) {
		return this.mallOrderDao.countUserplanDetailId(map);
	}

	@Override
	public int queryUserOneOrder(String userId) {
		return this.mallOrderDao.queryUserOneOrder(userId);
	}

	@Override
	public String queryOrderId(String userId) {
		return this.mallOrderDao.queryOrderId(userId);
	}

	@Override
	public List<OrderPOJO> queryPOJO(Map<String, Object> map) {
		return this.mallOrderDao.queryPOJO(map);
	}

	@Override
	public int countUsersMobile(Map<String, Object> map) {
		return this.mallOrderDao.countUsersMobile(map);
	}

	@Override
	public void saveOrder(MallOrderEntity mallOrderEntity) {
		this.mallOrderDao.saveOrder(mallOrderEntity);
		try{
			//生产新订单,根据商品是否为录播课,推送到题库
			messageVideoCourseService.pushToMessageQueueClass(null, null, mallOrderEntity.getOrderNo(),null);
		}catch(Exception e){
			logger.error("MessageVideoCourseService.pushToMessageQueueClass video course to tiku fail,orderNo:{},error message:{}",mallOrderEntity.getOrderNo(),e.toString());
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateChange(Map<String, Object> map) {
		Long orderId = (Long) map.get("orderId");
		Long commodityId = (Long) map.get("commodityId");
		String commodityName = (String) map.get("commodityName");
		/*
		Long areaId = (Long) map.get("areaId");
		Long classTypeId = (Long) map.get("classTypeId");
		Long levelId = (Long) map.get("levelId");
		*/
		Long professionId = (Long) map.get("professionId");
		Long modifier = (Long) map.get("modifier");
		
		MallOrderEntity mallOrderEntity = this.mallOrderDao.queryObject(map);
		mallOrderEntity.setCommodityId(commodityId);
		mallOrderEntity.setCommodityName(commodityName);
		mallOrderEntity.setProfessionId(professionId);
		mallOrderEntity.setModifier(modifier);
		mallOrderEntity.setModifiedTime(new Date());
		this.mallOrderDao.update(mallOrderEntity);
		
		//发送到Udesk消息队列
		try {
			if(null != mallOrderEntity.getClassId() && mallOrderEntity.getClassId() > 0){
				
				sendUdeskMq(mallOrderEntity.getOrderId() , mallOrderEntity.getClassId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int userplanNum = this.courseUserplanService.queryUserplanByOrderId(orderId);
		if(userplanNum > 0){
			CourseUserplanEntity courseUserplanEntity = this.courseUserplanService.queryUserplanObjectByOrderId(orderId);
			courseUserplanEntity.setOrderId(orderId);
			courseUserplanEntity.setCommodityId(commodityId);
			courseUserplanEntity.setProfessionId(professionId);
			courseUserplanEntity.setModifyPerson(modifier);
			courseUserplanEntity.setModifiedTime(new Date());
			
			this.courseUserplanService.update(courseUserplanEntity);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateChangeArea(Map<String, Object> map) {
		Long orderId = (Long) map.get("orderId");
		Long areaId = (Long) map.get("areaId");
		Long modifier = (Long) map.get("modifier");
		
		MallOrderEntity mallOrderEntity = this.mallOrderDao.queryObject(map);
		mallOrderEntity.setAreaId(areaId);
		mallOrderEntity.setModifier(modifier);
		mallOrderEntity.setModifiedTime(new Date());
		this.mallOrderDao.update(mallOrderEntity);
		//发送到Udesk消息队列
		try {
			if(null != mallOrderEntity.getClassId() && mallOrderEntity.getClassId() > 0){
				
				sendUdeskMq(mallOrderEntity.getOrderId() , mallOrderEntity.getClassId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int userplanNum = this.courseUserplanService.queryUserplanByOrderId(orderId);
		if(userplanNum > 0){
			CourseUserplanEntity courseUserplanEntity = this.courseUserplanService.queryUserplanObjectByOrderId(orderId);
			courseUserplanEntity.setAreaId(areaId);
			courseUserplanEntity.setModifyPerson(modifier);
			courseUserplanEntity.setModifiedTime(new Date());
			this.courseUserplanService.update(courseUserplanEntity);
			
			this.courseUserplanDetailService.updateAreaId(courseUserplanEntity.getUserPlanId(), areaId);
			
		}
	}

    @Override
    public List<MallOrderEntity> queryObjectByNC(String userPhone, String ncId) {
        return mallOrderDao.queryObjectByNC(userPhone,ncId);
    }

    @Override
    public Map<String, Object> queryKJClasstoTK(Long orderId) {
        return mallOrderDao.queryKJClasstoTK(orderId);
    }

    @Override
    public void updateChangeByOrderNo(Long orderId) {
        mallOrderDao.updateChangeByOrderNo(orderId);
       
    }

    @Override
    public void closeOrderByNC(List<MallOrderEntity> mallOrderList) {
        try {
            if (mallOrderList != null && mallOrderList.size() > 0) {
                for (MallOrderEntity mallOrderEntity : mallOrderList) {
                	//发送消息给题库
	                Long orderId = mallOrderEntity.getOrderId();
	                logger.error("closeOrder ... orderId={}",orderId);
	                this.sendMsgToTK(orderId,0);
	                //删除在线协议内容
	                contractRecordService.ContractRecordDeleteByOrderId(orderId);
	              //设置学员规划,学员规划详情,学习计划,学习计划详情dr=1;
                    CourseUserplanEntity courseUserplanEntity = courseUserplanService.queryUserplanObjectByOrderId(orderId);
                 
                    if (courseUserplanEntity != null) {
                        courseUserplanService.updateChangeByOrderNo(orderId);
                        courseUserplanClassService.updateChangeByOrderNo(courseUserplanEntity.getUserPlanId());
                    }
	                //如果来源为0的 说明是学员生成的订单 这种订单不会删除但会清掉NCID NCCODE
	                if (mallOrderEntity.getSourceType()!=null&&mallOrderEntity.getSourceType()==0) {
	                	mallOrderDao.setNcId(orderId, null);
	                	 
	                	  logger.error("关闭订单出错,来源为0的不能被退订单.MallOrderServiceImpl closeOrderByNC not allowed! mallorderEntity:{}{ ",mallOrderEntity  );
	                	  continue;
	                }
	                //如果来源不是0的 则会删除订单
	                this.updateChangeByOrderNo(orderId);
                 }
            }
        }catch (Exception e){
            logger.error("关闭订单出错MallOrderServiceImpl closeOrderByNC cause by{}: "+e);
        }
    }

    @Override
    public int queryOrderExistByTkCode(Long userId, Long commodityId) {
        return mallOrderDao.queryOrderExistByTkCode(userId,commodityId);
    }

    public void sendMsgToTK(Long orderId,int openNum) {
        Gson gson = new Gson();
        Map<String, Object> mapResult = this.queryKJClasstoTK(orderId);
        Set<String> tkCodeSet = new HashSet<>();
        if (mapResult != null) {
            Long goodId = (Long) mapResult.get("goodId");
            Long userId = (Long) mapResult.get("userId");
            //题库课程列表
            List<String> codeList = courseUserplanService.queryCodeListByCommodityId(goodId);
            //1则全部开通权限
            if (openNum == 1){
                mapResult.put("courseTkCode", codeList);
                mapResult.put("open", 1);
                mapResult.put("isNewClass",1);
            }else {
                //0,查询该订单下的题库课程有没有关联同一学员的其他订单,如果有关联,则不要关闭该题库课程
                if (codeList != null && codeList.size() > 0) {
                    for (String tkCode : codeList) {
                        List<Long> commodityIdList = courseUserplanService.queryComnodityListByTkCode(tkCode);
                        if (commodityIdList != null && commodityIdList.size() == 1) {
                            tkCodeSet.add(tkCode);
                        }
                        if (commodityIdList != null && commodityIdList.size() > 1) {
                            for (Long commodityId : commodityIdList) {
                                if (goodId.compareTo(commodityId) != 0) {
                                    int num = this.queryOrderExistByTkCode(userId, commodityId);
                                    if (num == 0) {
                                        tkCodeSet.add(tkCode);
                                    }
                                }
                            }
                        }
                    }
                }
                //修复bug："courseTkCode":["kckm001","kckm002"],	修复为"courseTkCode":"kckm001,kckm002",
                String tkCodeStr = StringUtils.join(tkCodeSet.toArray(), ",");
                mapResult.put("courseTkCode", tkCodeStr);
                mapResult.put("open", 0);
                mapResult.put("isNewClass",1);
            }
            mapDateFormatter(mapResult, "ts");
            String json = gson.toJson(mapResult).toString();
            logger.error("MallOrderServiceImpl closeOrderByNC sendMsgToTK json:{}", json);
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("orderId", orderId);
            MallOrderEntity order = this.queryObject(paramMap);
            if(order!=null && order.getProductId()!=null && order.getProductId() == 1){
            	amqpTemplate.convertAndSend(ZK_CLASS_MESSAGE, json);
            }else{
            	amqpTemplate.convertAndSend(KJ_CLASS_MESSAGE, json);            	
            }
        }
    }

    @Override
    public List<MallOrderEntity> isExistByUserId(String ncId, String userPhone, String commodityNCPK) {
        return mallOrderDao.isExistByUserId(ncId,userPhone,commodityNCPK);
    }
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public int updateIsTeacher(Map<String, Object> map) {
		return mallOrderDao.updateIsTeacher(map);
	}

	@Override
	public List<MallOrderEntity> queryListByCommodityId(Long commodityId,Long classId) {
		List<MallOrderEntity> list = this.mallOrderDao.queryListByCommodityId(commodityId,classId);
		return list;
	}
	
	@Override
	public List<MallOrderEntity> queryListByCommodityIdAndUserId(Long commodityId, Long userId) {
		List<MallOrderEntity> list = this.mallOrderDao.queryListByCommodityIdAndUserId(commodityId,userId);
		return list;
	}
    /**
     * 日期格式化
     * @param map
     * @param dateKey
     */
    private static void mapDateFormatter(Map<String , Object> map , String dateKey){
        if(null != map && StringUtils.isNotBlank(dateKey)){
            Object object = map.get(dateKey);
            if(null != object){
                try {
                    Date date = (Date) object;
                    String format = DateUtils.format(date, DateUtils.DATE_TIME_PATTERN);
                    map.put(dateKey, format);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static String tkCourseCode(List<String> codeList){
        String result = null;
        if(null != codeList && !codeList.isEmpty()){
            StringBuffer sbf = new StringBuffer();
            for (String string : codeList) {
                sbf.append(string + ",");
            }
            result = sbf.substring(0, sbf.length() - 1);
        }
        return result;
    }
    
    //指定班级后发送Udesk消息队列
    private void sendUdeskMq(List<Long> orderIdlist,Long classId){
    	Gson gson = new Gson();
    	Map<String,Object> mapUdesk = new HashMap<>();
    	Map<String,Object> map = sysUserService.queryObjectByClassId(classId);
    	Integer ownerId = null;
    	String tags = null;
    	String className = null;
    	for (Long orderId : orderIdlist) {
    		OrderPOJO orderPOJO = new OrderPOJO();
    		orderPOJO = this.mallOrderDao.queryPOJOByOrderId(orderId);
        	if(null != map){
        		if(null != map.get("ownerId")){
        			ownerId = (int) map.get("ownerId");
        		}
        		if(null != map.get("nickName")){
        			tags = (String) map.get("nickName");
        		}
        		if(null != map.get("className")){
        			className = (String) map.get("className");
        		}
        	}
    		String phone = orderPOJO.getMobile().trim()+"-"+orderPOJO.getOrderId();
    		mapUdesk.put("nick_Name", orderPOJO.getNickName());
        	mapUdesk.put("phone", phone);
        	mapUdesk.put("tags", tags);
        	mapUdesk.put("ownerId", ownerId);
        	mapUdesk.put("payTime", DateUtils.format(orderPOJO.getPayTime(), DateUtils.DATE_PATTERN));
        	mapUdesk.put("province", orderPOJO.getAreaName());
        	mapUdesk.put("level", orderPOJO.getLevelName());
        	mapUdesk.put("className", className);
        	mapUdesk.put("schoolName", orderPOJO.getDeptName());
        	mapUdesk.put("profession", orderPOJO.getProfessionName());
        	mapUdesk.put("commodity", orderPOJO.getCommodityName());
        	
        	String json = gson.toJson(mapUdesk).toString();
        	try {
        		amqpTemplate.convertAndSend(SYNC_UDESK_MESSAGE, json);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
		}
    }
    //手动新增订单或对订单修改后后发送Udesk消息队列
    private void sendUdeskMq(Long orderId,Long classId){
    	Gson gson = new Gson();
    	Map<String,Object> mapUdesk = new HashMap<>();
    	Map<String,Object> map = sysUserService.queryObjectByClassId(classId);
    	Integer ownerId = null;
    	String tags = null;
    	String className = null;
    	OrderPOJO orderPOJO = new OrderPOJO();
		orderPOJO = this.mallOrderDao.queryPOJOByOrderId(orderId);
		if(null != map){
			if(null != map.get("ownerId")){
				ownerId = (int) map.get("ownerId");
			}
			if(null != map.get("nickName")){
				tags = (String) map.get("nickName");
			}
			if(null != map.get("className")){
				className = (String) map.get("className");
			}
		}
		String phone = orderPOJO.getMobile().trim()+"-"+orderPOJO.getOrderId();
		mapUdesk.put("nick_Name", orderPOJO.getNickName());
    	mapUdesk.put("phone", phone);
    	mapUdesk.put("tags", tags);
    	mapUdesk.put("ownerId", ownerId);
    	mapUdesk.put("payTime", DateUtils.format(orderPOJO.getPayTime(), DateUtils.DATE_PATTERN));
    	mapUdesk.put("province", orderPOJO.getAreaName());
    	mapUdesk.put("level", orderPOJO.getLevelName());
    	mapUdesk.put("className", className);
    	mapUdesk.put("schoolName", orderPOJO.getDeptName());
    	mapUdesk.put("profession", orderPOJO.getProfessionName());
    	mapUdesk.put("commodity", orderPOJO.getCommodityName());
    	
    	String json = gson.toJson(mapUdesk).toString();
    	try {
    		amqpTemplate.convertAndSend(SYNC_UDESK_MESSAGE, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public int queryTotalCustomers(Integer startOrderId,Integer endOrderId,List<String> teacherMobileList,List<String> orderNoList) {
		return this.mallOrderDao.queryTotalCustomers(startOrderId,endOrderId,teacherMobileList,orderNoList);
	}

	@Override
	public List<SyncCustomerPOJO> queryMapList(int offset,Integer startOrderId,Integer endOrderId,List<String> teacherMobileList,List<String> orderNoList) {
		return this.mallOrderDao.queryMapList(offset,startOrderId,endOrderId,teacherMobileList,orderNoList);
	}

	@Override
	public String queryPhoneByOrderId(Long orderId) {
		return this.mallOrderDao.queryPhoneByOrderId(orderId);
	}

    @Override
    public MallOrderEntity queryObjectByNcIdAndCommodityId(String ncId, String ncCommodityId) {
        return mallOrderDao.queryObjectByNcIdAndCommodityId(ncId,ncCommodityId);
    }

    @Override
    public MallOrderEntity queryObjectByMobileAndComodityId(String mobile, String ncCommodityId) {
        return mallOrderDao.queryObjectByMobileAndComodityId(mobile,ncCommodityId);
    }

    private void deleteUdeskCustomer(Long[] orderIds){
		for (Long orderId : orderIds) {
			//发送Udesk删除客户
			String userPhone = this.queryPhoneByOrderId(orderId);
    		String cellphone = userPhone + "-" +orderId.toString();
    		SendUdeskUtil.deleteCustomer(cellphone);
		}
	}

	@Override
	public void setNcId(Long orderId, String ncId) {
		if (orderId!=null&&orderId.intValue()>0) {
			mallOrderDao.setNcId(orderId, ncId);
		}
		
	}
}
