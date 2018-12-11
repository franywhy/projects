package io.renren.controller;

import java.net.InetAddress;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.renren.entity.*;
import io.renren.service.*;
import io.renren.service.impl.MallAdjustAreaServiceImpl;
import io.renren.service.impl.MallAdjustProfessionServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.pojo.order.OrderPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 订单档案表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 14:30:47
 */
@Controller
@RequestMapping("/mall/order")
public class MallOrderController extends AbstractController {
    @Autowired
    private MallOrderService mallOrderService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private MallGoodsDetailsService mallGoodsDetailsService;
    @Autowired
    private MallGoodsInfoService mallGoodsInfoService;
    @Autowired
    private MallClassService mallClassService;
    @Autowired
    private MallAdjustAreaService mallAdjustAreaService;
    @Autowired
    private MallAdjustProfessionService mallAdjustProfessionService;
    @Resource
    KGS orderNoKGS;
    @Resource
    KGS userKGS;
    private static final String ORDERNO_HEAD = "min_";
    private static String PSW = "1c3f360330c442c3cc62d1608fe7a3a3";
    private static String GOODSID = "201707031424";

    @Value("#{application['goods.id']}")
    private void setGOODSID(String str) {
        GOODSID = str;
    }

    private static String CLASSESID = "201707031424";

    @Value("#{application['mallclass.id']}")
    private void setCLASSESID(String str) {
        CLASSESID = str;
    }

    @ResponseBody
    @RequestMapping("/listGrid")
    public R listGrid(
            String orderNo, String userMobile, Integer sourceType, Integer classId,
            Integer page, Integer limit,
            HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);

        map.put("orderNo", orderNo);
        map.put("userMobile", userMobile);
        map.put("sourceType", sourceType);
        map.put("classId", classId);
        map.put("schoolId", SchoolIdUtils.getSchoolId(request));
        List<Map<String, Object>> list = mallOrderService.queryListGrid(map);
        int total = mallOrderService.queryListGridTotal(map);
        PageUtils pageUtil = new PageUtils(list, total, limit, page);

        return R.ok().put("page", pageUtil);
    }

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    //@RequiresPermissions("mallorder:list")
    public R list(HttpServletRequest request) {
        Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "orderNo");
        stringQuery(map, request, "mobile");
        stringQuery(map, request, "nickName");
        stringQuery(map, request, "ncId");
        stringQuery(map, request, "ncCode");
        longQuery(map, request, "classId");
        longQuery(map, request, "classTypeId");
        longQuery(map, request, "levelId");
        longQuery(map, request, "areaId");
        longQuery(map, request, "professionId");
        longQuery(map, request, "userplanType");
        longQuery(map, request, "productId");
         intQuery(map, request, "signStatus");
        stringQuery(map, request, "deptIdList");
        stringQuery(map, request, "deptIdList");
        //判断是查询全部还是查询多部门
        String deptIdList = (String) map.get("deptIdList");
        if (deptIdList != null && !"".equals(deptIdList.trim())) {
            String[] split = deptIdList.split(",");
            map.put("deptIdList", Arrays.asList(split));
        } 
        //查询列表数据（实现检索）
        List<OrderPOJO> mallOrderList = mallOrderService.queryList(map);
        if (map.get("mobile") != null || map.get("nickName") != null) {
            //初始化
            map.put("useUsers", true);
        }
        int total = this.mallOrderService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(mallOrderList, total, request);
        return R.ok().put("page", pageUtil);
    }

    /**
     * 订单列表信息
     */
    @ResponseBody
    @RequestMapping("/info/{orderId}")
    //@RequiresPermissions("mallorder:info")
    public R info(@PathVariable("orderId") Long orderId, HttpServletRequest request) {
        String schoolId = SchoolIdUtils.getSchoolId(request);
        Map<String, Object> map = new HashMap<>();
        map.put("schoolId", schoolId);
        map.put("orderId", orderId);
        MallOrderEntity mallOrder = mallOrderService.queryObject(map);
        return R.ok().put("mallOrder", mallOrder);
    }

    /**
     * 新增订单
     */
    @SysLog("保存订单")
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions("mallorder:save")
    public R save(@RequestBody OrderPOJO mallOrder, HttpServletRequest request) {
        Long userId = mallOrder.getUserId();
        if (userId == null) {
            return R.error("请选择用户");
        }
        //支付状态 0.未支付 1.发起支付 ,2.支付成功
        mallOrder.setPayStatus(2);
        //訂單號
        mallOrder.setOrderNo(ORDERNO_HEAD + orderNoKGS.nextId());
        //商品名称
        String commodityName = mallOrder.getCommodityName();
        if (commodityName == null) {
            return R.error("商品名称不能为空");
        }

        if (null == mallOrder.getDeptId() || mallOrder.getDeptId() <= 0) {
            return R.error("请选择部门!");
        }
        //商品是否开通题库权限
        mallOrder.setOnlyOne(mallOrder.getOnlyOne());

        mallOrder.setCommodityName(commodityName);
        //商品ID
        mallOrder.setCommodityId(mallOrder.getCommodityId());

        //订单名称
        mallOrder.setOrderName(mallOrder.getCommodityName());
        //订单描述
        mallOrder.setOrderDescribe(mallOrder.getCommodityName());
        //订单总额
        mallOrder.setTotalMoney(mallOrder.getTotalMoney());
        //备注
        String remark = mallOrder.getRemark();
		/*if(remark==null){
			return R.error("备注不能為空");
		}*/
        mallOrder.setRemark(remark);
        //平台IP
        mallOrder.setSchoolId(SchoolIdUtils.getSchoolId(request));
        //支付金額
        mallOrder.setPayMoney(0.0);
        //支付時間
        mallOrder.setPayTime(new Date());
        //优惠金额
        mallOrder.setFavorableMoney(0.0);
        //优惠券ID
        mallOrder.setDiscountId(null);
        //第三方支付回调时间
        mallOrder.setPayCallblackTime(new Date());
        //第三方支付回调信息
        mallOrder.setPayCallblackMsg(null);
        //支付宝交易号
        mallOrder.setAlipayTradeNo(null);
        //支付IP
        mallOrder.setPayip(null);
        //生成支付地址
        mallOrder.setPayUrl(null);
        //商品图片
        String pic = mallOrder.getPic();
        if (pic == null) {
            return R.error("商品图片不能為空");
        }
        mallOrder.setPic(pic);
        //商品小图
        String spic = mallOrder.getSpic();
        if (spic == null) {
            return R.error("商品小图不能為空");
        }
        mallOrder.setSpic(spic);
        //dr 0.正常 1.删除
        mallOrder.setDr(0);
        //用户操作状态 0.正常 1.取消 2.删除
        mallOrder.setUstatus(0);
        //微信开放ID
        mallOrder.setWxOpenId(null);
        //0.未选择 1.支付宝 2.微信
        mallOrder.setPayType(0);
        //银行编码
        mallOrder.setBankCode(null);
        //银行名称
        mallOrder.setBankName(null);
        //創建用戶
        mallOrder.setCreator(getUserId());
        //创建时间
        mallOrder.setCreationTime(new Date());
        //最近修改用户
        mallOrder.setModifier(null);
        //最近修改时间
        mallOrder.setModifiedTime(new Date());
        ////来源 0.线上;1.NC;2測試
        mallOrder.setSourceType(2);
        //NCID
        mallOrder.setNcId(null);
        //省份ID
        Long area = mallOrder.getAreaId();
        if (area == null) {
            return R.error("请选择省份");
        }
        mallOrder.setAreaId(area);
        //层次ID
        mallOrder.setLevelId(mallOrder.getLevelId());
        //班级ID
        Long classId = mallOrder.getClassId();
        if (classId == null) {
            return R.error("请选择班级");
        }
        mallOrder.setClassId(classId);
        //班型ID
        mallOrder.setClassTypeId(mallOrder.getClassTypeId());
        //专业ID
        mallOrder.setProfessionId(mallOrder.getProfessionId());
        mallOrderService.save(mallOrder);
        return R.ok();
    }

    /**
     * 新增体验订单
     *
     * @throws Exception
     */
    @SysLog("新增体验订单")
    @ResponseBody
    @RequestMapping("/saveAttempt")
    @RequiresPermissions("mallorder:saveAttempt")
    public R saveAttemptOrder(@RequestBody OrderPOJO mallOrder, HttpServletRequest request) throws Exception {
        UsersEntity users = new UsersEntity();
        String ip = InetAddress.getLocalHost().toString();
        Map<String, Object> map = getMap(request);
        map.put("mobilePhoneNo", mallOrder.getMobile());
        map.put("mobile", mallOrder.getMobile());
        map.put("id", GOODSID);
        map.put("classId", CLASSESID);
        if (mallGoodsInfoService.checkExist(map) == false) {
            return R.error("体验商品不存在");
        } else if (mallClassService.checkExist(map) == false) {
            return R.error("体验班级不存在");
        } else {
            //省份ID
            Long areaId = null;
            try {
                areaId = mallGoodsDetailsService.getAreaIdByGoodsId(map);
                mallOrder.setAreaId(areaId);
            } catch (Exception ex) {
                return R.error("商品明细表中不存在该商品");
            }

            Long userId = null;
		/*try {
			userId=usersService.getUserIdByMobilePhoneNo(map);
		}
		catch (Exception ex) {
//			return R.error("该用户不存在");
			
		}*/
            if (this.usersService.mobileExist(map)) {
                if (mallOrderService.countUsersMobile(map) == 1) {
                    userId = usersService.getUserIdByMobilePhoneNo(map);
                    mallOrder.setUserId(userId);
                }
                return R.error("该手机号已经绑定了订单");
            } else {
                long userID = (long) userKGS.nextId();
                users.setUserId(userID);
                users.setPic("http://zikao.hqjy.com/Avatar.png");
                users.setPassword(PSW);
                users.setNickName(mallOrder.getMobile().substring(mallOrder.getMobile().length() - 4, mallOrder.getMobile().length()));
                users.setSchoolId(SchoolIdUtils.getSchoolId(request));
                users.setMobile(mallOrder.getMobile());
                usersService.save(users);
                mallOrder.setUserId(userID);
            }
            MallGoodsInfoEntity mallGoodsInfoEntity = mallGoodsInfoService.queryGoodsInfoId(map);
            MallClassEntity mallClassEntity = mallClassService.queryObject(map);
            //支付状态 0.未支付 1.发起支付 ,2.支付成功
            mallOrder.setPayStatus(2);
            //訂單號
            mallOrder.setOrderNo(ORDERNO_HEAD + orderNoKGS.nextId());
            //商品名称
            String commodityName = mallGoodsInfoEntity.getName();
            if (commodityName == null) {
                return R.error("商品名称不能為空");
            }
            mallOrder.setCommodityName(commodityName);
            //商品ID
            mallOrder.setCommodityId(Long.parseLong(GOODSID));

            //订单名称
            mallOrder.setOrderName(commodityName);
            //订单描述
            mallOrder.setOrderDescribe(commodityName);
            //订单总额
            mallOrder.setTotalMoney(0.0);
            mallOrder.setRemark("体验订单");
            //平台IP
            mallOrder.setSchoolId(SchoolIdUtils.getSchoolId(request));
            //支付金額
            mallOrder.setPayMoney(0.0);
            //支付時間
            mallOrder.setPayTime(new Date());
            //优惠金额
            mallOrder.setFavorableMoney(0.0);
            //优惠券ID
            mallOrder.setDiscountId(null);
            //第三方支付回调时间
            mallOrder.setPayCallblackTime(new Date());
            //第三方支付回调信息
            mallOrder.setPayCallblackMsg(null);
            //支付宝交易号
            mallOrder.setAlipayTradeNo(null);
            //支付IP

            mallOrder.setPayip(ip);


            //生成支付地址
            mallOrder.setPayUrl(null);
            //商品图片
            mallOrder.setPic(mallGoodsInfoEntity.getOriginPath());
            //商品小图
            mallOrder.setSpic(mallGoodsInfoEntity.getThumbPath());
            // 是否开通题库权限
            mallOrder.setOnlyOne(mallGoodsInfoEntity.getOnlyOne());
            //dr 0.正常 1.删除
            mallOrder.setDr(0);
            //用户操作状态 0.正常 1.取消 2.删除
            mallOrder.setUstatus(0);
            //微信开放ID
            mallOrder.setWxOpenId(null);
            //0.未选择 1.支付宝 2.微信
            mallOrder.setPayType(0);
            //银行编码
            mallOrder.setBankCode(null);
            //银行名称
            mallOrder.setBankName(null);
            //創建用戶
            mallOrder.setCreator(getUserId());
            //创建时间
            mallOrder.setCreationTime(new Date());
            //最近修改用户
            mallOrder.setModifier(null);
            //最近修改时间
            mallOrder.setModifiedTime(new Date());
            ////来源 0.线上;1.NC;2測試;3体验
            mallOrder.setSourceType(3);
            //NCID
            mallOrder.setNcId(null);

            //层次ID
            mallOrder.setLevelId(mallGoodsInfoEntity.getLevelId());
            //班级ID
            mallOrder.setClassId(Long.parseLong(CLASSESID));
            //班型ID
            mallOrder.setClassTypeId(mallGoodsInfoEntity.getClassTypeId());
            //专业ID
            mallOrder.setProfessionId(mallGoodsInfoEntity.getProfessionId());
            mallOrderService.save(mallOrder);
            return R.ok();
        }
    }

    /**
     * 学员列表批量指定班级/订单指定班级
     */
    @SysLog("批量指定班级")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("mall:order:update")
    public R update(@RequestBody OrderPOJO mallOrder, HttpServletRequest request) {
		/*if(StringUtils.isBlank(mallOrder.getOrderNo())){
			return R.error("[班级]不能为空");
		}*/
        if (mallOrder.getClassId() == null) {
            return R.error("[班级]不能为空");
        }
        //判断订单的产品线是否相同
        String schoolId = SchoolIdUtils.getSchoolId(request);
        for (Long orderId : mallOrder.getOrderList()) {
            Map<String, Object> map = new HashMap<>();
            map.put("schoolId", schoolId);
            map.put("orderId", orderId);
            MallOrderEntity mallOrderEntity = mallOrderService.queryObject(map);
            if (mallOrder.getProductId() != mallOrderEntity.getProductId()) {
                return R.error("操作失败!!<br/>" + "订单号为: " + mallOrderEntity.getOrderNo() + " 的学员,转移班级与原班级产品线不同!");
            }
        }
        mallOrder.setModifier(getUserId());
        mallOrderService.update(mallOrder);
        return R.ok();
    }

    /**
     * 修改订单有效期
     */
    @SysLog("修改订单有效期")
    @ResponseBody
    @RequestMapping("/updateDvalidate")
    public R updateValidate(@RequestBody OrderPOJO mallOrder) {
        mallOrder.setModifier(getUserId());
        Long orderId = mallOrder.getOrderId();
        mallOrderService.updateValidate(mallOrder);
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除订单")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("mallorder:delete")
    public R delete(@RequestBody Long[] orderIds) {
        mallOrderService.deleteBatch(orderIds);
        return R.ok();
    }

    /**
     * 禁用
     */
    @SysLog("禁用订单")
    @ResponseBody
    @RequestMapping("/pause")
    //@RequiresPermissions("mallorder:pause")
    public R pause(@RequestBody Long[] orderIds) {
        mallOrderService.pause(orderIds);
        return R.ok();
    }

    /**
     * 启用
     */
    @SysLog("启用订单")
    @ResponseBody
    @RequestMapping("/resume")
    //@RequiresPermissions("mallorder:resume")
    public R resume(@RequestBody Long[] orderIds) {
        mallOrderService.resume(orderIds);
        return R.ok();
    }

    /**
     * 转专业
     */
    @SysLog("转专业")
    @ResponseBody
    @RequestMapping("/updateChange")
    @RequiresPermissions("mall:order:updateChange")
    public R updateChange(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        longQuery(map, request, "orderId");//订单id
        MallOrderEntity mallOrder = mallOrderService.queryObject(map);
        longQuery(map, request, "commodityId");//商品id
        stringQuery(map, request, "commodityName");//商品名称
		/*
		longQuery(map, request, "areaId");//省份id
		longQuery(map, request, "classTypeId");//班型id
		longQuery(map, request, "levelId");//层次id
		*/
        longQuery(map, request, "professionId");//专业id
        map.put("modifier", getUserId());//修改人

        this.mallOrderService.updateChange(map);
        Long professionId = (Long) map.get("professionId");
        Long lastProfessionId = mallOrder.getProfessionId();
        if (!lastProfessionId.equals(professionId)) {
            //保存转省份记录
            MallAdjustProfessionEntity mallAdjustProfessionEntity = new MallAdjustProfessionEntity();
            mallAdjustProfessionEntity.setOrderId((Long) map.get("orderId"));
            mallAdjustProfessionEntity.setUserId(mallOrder.getUserId());
            mallAdjustProfessionEntity.setLastProfessionId(lastProfessionId);
            mallAdjustProfessionService.save(mallAdjustProfessionEntity);
        }
        return R.ok();
    }

    /**
     * 转省份
     */
    @SysLog("转省份")
    @ResponseBody
    @RequestMapping("/updateChangeArea")
    @RequiresPermissions("mall:order:updateChangeArea")
    public R updateChangeArea(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        longQuery(map, request, "orderId");//订单id
//		longQuery(map, request, "commodityId");//商品id
        MallOrderEntity mallOrder = mallOrderService.queryObject(map);
        longQuery(map, request, "areaId");//省份id
//		longQuery(map, request, "classTypeId");//班型id
//		longQuery(map, request, "levelId");//层次id
//		longQuery(map, request, "professionId");//专业id
        map.put("modifier", getUserId());//修改人
        this.mallOrderService.updateChangeArea(map);

        Long areaId = (Long) map.get("areaId");
        Long lastareaId = mallOrder.getAreaId();
        if (!lastareaId.equals(areaId)) {
            //保存转省份记录
            MallAdjustAreaEntity mallAdjustArea = new MallAdjustAreaEntity();
            mallAdjustArea.setOrderId((Long) map.get("orderId"));
            mallAdjustArea.setUserId(mallOrder.getUserId());
            mallAdjustArea.setLastAreaId(lastareaId);
            mallAdjustAreaService.save(mallAdjustArea);
        }
        return R.ok();
    }
	
	/*public String getIp() throws Exception{
		String ip=request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || ip == "null" ||  "unknown".equalsIgnoreCase(ip)){
		ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || ip == "null" || "unknown".equalsIgnoreCase(ip)){
		ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || ip == "null" || "unknown".equalsIgnoreCase(ip)){
		ip = request.getRemoteAddr();
		}
		return ip;
		
		Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements())
		{
		NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
		System.out.println(netInterface.getName());
		Enumeration addresses = netInterface.getInetAddresses();
		while (addresses.hasMoreElements())
		{
		ip = (InetAddress) addresses.nextElement();
		if (ip != null && ip instanceof Inet4Address)
		{
		System.out.println("本机的IP = " + ip.getHostAddress());
		} 
		}
		}
		return InetAddress.getLocalHost();
	}*/

    /**
     * 转角色
     */
    @SysLog("转角色")
    @ResponseBody
    @RequestMapping("/updateIsTeacher")
    @RequiresPermissions("mallorder:updateIsTeacher")
    public R updateIsTeacher(HttpServletRequest request) {
        Map<String, Object> map = getMap(request);
        longQuery(map, request, "orderId");//订单id
        longQuery(map, request, "isTeacher");//教师角色
        map.put("modifier", getUserId());//修改人
        map.put("modifiedTime", new Date());
        this.mallOrderService.updateIsTeacher(map);
        return R.ok();
    }
}
