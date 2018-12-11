package com.tenpay.mobile;

import com.tenpay.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * wap手机支付初始化请求类
 * ============================================================================
 * api说明：
 * init(),初始化函数，默认给一些参数赋值，如cmdno,date等。
 * getGateURL()/setGateURL(),获取/设置入口地址,不包含参数值
 * getKey()/setKey(),获取/设置密钥
 * getParameter()/setParameter(),获取/设置参数值
 * getAllParameters(),获取所有参数
 * getRequestURL(),获取带参数的请求URL
 * doSend(),重定向到财付通支付
 * getDebugInfo(),获取debug信息
 *
 * ============================================================================
 *
 */

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-29
 * Time: 下午5:15
 * To change this template use File | Settings | File Templates.
 */
public class MobilePayInitRequestHandler extends RequestHandler
{       public MobilePayInitRequestHandler(HttpServletRequest request,
                                           HttpServletResponse response) {
    super(request, response);

    this.setGateUrl("https://cl.tenpay.com/cgi-bin/wappayv2.0/wappay_init.cgi");

}

    public void init() {

        this.setParameter("ver", "2.0");

        //字符编码  1 UTF-8, 2 GB2312,
        this.setParameter("charset", "1");

        //请求来源，填211
        this.setParameter("sale_plat", "211");

        //银行类型:财付通支付填0
        this.setParameter("bank_type", "0");

        //签名
        this.setParameter("sign", "");

    }

    protected void createSign() {
        super.createSign();
        this.setParameter("sign", getParameter("sign").toUpperCase());
    }


}
