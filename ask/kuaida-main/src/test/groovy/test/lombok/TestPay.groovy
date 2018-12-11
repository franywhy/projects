package test.lombok

import com.yeepay.nonbankcard.NonBankcardService
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest
import test.BaseTest

/**
 * date: 13-5-7 下午2:08
 * @author: wubinjie@ak.cc
 */
class TestPay extends BaseTest{



    @Test
    void divideByZero() {
        shouldFail(ArithmeticException) {
            println 1/0
        }
    }

    @Test
    void yb_wap(){
        println(1)


        def req = new MockHttpServletRequest()
        req.p4_verifyAmt=true
        req.pa9_cardPwd=1
        req.pa_MP=1269861
        req.pr_NeedResponse=1
        req.pd_FrpId='CMBCHINA-NET-B2C'
        req.p2_Order='1269861_100_CMBCHINA-NET-B2C_1369826416428'
        req.pa8_cardNo=1
//        req.pz_userId=1269861
        req.p3_Amt=100
        req.p8_Url="show.izhubo.com"
        req.p5_Pid='show.izhubo.com'
        req.p0_Cmd='ChargeCardDirect'
        req.p1_MerId=10011898701
        req.pa7_cardAmt=50
        shouldFail(RuntimeException) {
            NonBankcardService.reqPay(req,"100", req.p2_Order,"1269861","JUNNET")
        }

    }

}
