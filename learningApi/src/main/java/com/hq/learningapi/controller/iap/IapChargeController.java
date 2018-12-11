package com.hq.learningapi.controller.iap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.entity.AppBannerEntity;
import com.hq.learningapi.pojo.BalancePOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.BalanceService;
import com.hq.learningapi.service.LoggerService;
import com.hq.learningapi.util.IosVerifyUtil;
import com.hq.learningapi.util.SSOTokenUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: learningapi
 * @description: IOS 内购
 * @author: Irving Wei
 * @create: 2018-09-10 17:02
 **/

@Controller
@RequestMapping("/buy")
public class IapChargeController extends AbstractRestController {
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private LoggerService loggerService;

    @Value("${local-info.itunes.apple.config-param}")
    private int CONFIG_PARAM;

    @ApiOperation(value = "ios内购-充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "receipt", value = "需要客户端传过来的参数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "transactionID", value = "交易单号,需要客户端传过来的参数", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<WrappedResponse<List<AppBannerEntity>>> getBanner(HttpServletRequest request) {
        String token = ServletRequestUtils.getStringParameter(request, "token", "");
        String transactionID = ServletRequestUtils.getStringParameter(request, "transactionID", "");
        String receipt = ServletRequestUtils.getStringParameter(request, "receipt", "");

        System.out.println("客户端传过来的值1：" + transactionID + "客户端传过来的值2：" + receipt);

        System.out.println("验证时间1：" + new Date());
        String verifyResult = IosVerifyUtil.buyAppVerify(receipt, CONFIG_PARAM);            //type:0  沙盒测试   type:1.线上测试    发送平台验证
        System.out.println("验证时间2：" + new Date());
        if (verifyResult == null) {                                            // 苹果服务器没有返回验证结果
            System.out.println("无订单信息!");
            loggerService.info(1, "苹果服务器没有返回验证结果", new UserInfoPOJO(), new Date(), transactionID);
            return this.fail("苹果服务器没有返回验证结果");
        } else {                                                                // 苹果验证有返回结果
            System.out.println("线上，苹果平台返回JSON:" + verifyResult);
            JSONObject job = JSONObject.parseObject(verifyResult);
            String states = job.getString("status");
            // 状态值
            int status = Integer.parseInt(states);

            if ("21007".equals(states)) {                                            //是沙盒环境，应沙盒测试，否则执行下面
                verifyResult = IosVerifyUtil.buyAppVerify(receipt, 0);            //2.再沙盒测试  发送平台验证
                System.out.println("沙盒环境，苹果平台返回JSON:" + verifyResult);
                job = JSONObject.parseObject(verifyResult);
                states = job.getString("status");
                String responseTip = "沙盒环境，苹果平台返回JSON:" + verifyResult;
                return this.fail(responseTip,TransactionStatus.INTERNAL_SERVER_ERROR);
            }

            System.out.println("苹果平台返回值：job" + job);

            // 获取用户信息
            UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);

            if (!"0".equals(states)) {
                // 记录日志，并返回失败
                loggerService.info(status, "验证失败，状态码: "+states, userInfo, new Date(), transactionID);
                return this.fail("验证失败，状态码: "+states);
            } else {// 前端所提供的收据是有效的    验证成功
                String r_receipt = job.getString("receipt");
                // 遍历订单号验证订单号是否正确。
                //  因为充值被设置为非消耗品。一次充值永久有效，即充值记录单号一直保持着
                boolean transactionIdFlag = false;
                String product_id = "";
                JSONObject in_app_jsonObject = JSONObject.parseObject(r_receipt);
                JSONArray jsonArray = in_app_jsonObject.getJSONArray("in_app");
                for(int i = 0;i < jsonArray.size();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getString("transaction_id").equals(transactionID)){
                        transactionIdFlag = true;
                        product_id += jsonObject.getString("product_id");
                        break;
                    }
                }

                if (!transactionIdFlag) { // 如果单号不一致
                    loggerService.info(status, "单号不一致", userInfo, new Date(), transactionID);
                    return this.fail("单号不一致");
                }
                try {
                    // 增加(充值)用户余额
                    String[] moneys = product_id.split("\\.");
                    double money = Double.parseDouble(moneys[moneys.length - 1]);
                    //如果单号一致  则保存到数据库
                    // 获取用户恒企币余额
                    BalancePOJO balance = balanceService.queryBalance(userInfo.getUserId());
                    if (null == balance) { // 若查询为空
                        // 新建记录
                        BalancePOJO balancePOJO = new BalancePOJO();
                        balancePOJO.setHqg(money);
                        balancePOJO.setMobile(userInfo.getMobileNo());
                        balancePOJO.setUserId(userInfo.getUserId());
                        balancePOJO.setUsername(userInfo.getNickName());
                        balanceService.insertBalance(balancePOJO);
                        //loggerService.info(status, "用户余额查询为空", userInfo, new Date(), transactionID);
                        double hqg =  balancePOJO.getHqg();
                        loggerService.info(status, "充值成功,用户充值数额：" + money + "；当前余额：" + hqg, userInfo, new Date(), transactionID);

                        HashMap<String,String> result = new HashMap<String,String>();
                        result.put("hqg",hqg+"");

                        return this.success(result,TransactionStatus.OK);
                    }else{
                        double hqg = balance.getHqg();
                        // 新增充值记录
                        hqg += money;
                        balance.setHqg(hqg);
                        balanceService.updateHqg(balance);
                        loggerService.info(status, "充值成功,用户充值数额：" + money + "；当前余额：" + hqg, userInfo, new Date(), transactionID);
                        HashMap<String,String> result = new HashMap<String,String>();
                        result.put("hqg",hqg+"");

                        return this.success(result,TransactionStatus.OK);
                    }
                } catch (Exception e) {
                    // 事务回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    // 日志记录
                    loggerService.info(status, "事务回滚", userInfo, new Date(), transactionID);
                    // 返回错误
                    return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }
}
