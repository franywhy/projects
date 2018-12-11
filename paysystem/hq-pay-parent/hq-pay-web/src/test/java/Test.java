import static org.mockito.Matchers.matches;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hqjy.pay.utils.MD5Util;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.hqjy.pay.App;
import com.hqjy.pay.PayConfig;
import com.hqjy.pay.PayConfigService;
import com.hqjy.pay.controller.base.BaseController;
import com.hqjy.pay.weixin.RandomStringGenerator;

/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)*/
public class Test {
	public static void main(String[] args) {
		
	}

	/**
	 * 加密 encryption
	 * 解密 decrypt
	 */
	@org.junit.Test
	public void encrypt(){
		String orderNo = "456785423154575451";
		String orderTimestamp = "2018-06-22";
		String tradeMoney = "0.02";
		String s = MD5Util.string2MD5(orderNo + orderTimestamp + tradeMoney + "hengqijypay");
		//System.err.println(s);

		String ciphertext = MD5Util
				.string2MD5("1.0" + "pay_110191" + "zfb" + "hengqijypay");
		System.err.println(ciphertext);

		BigDecimal b
				 = new BigDecimal("0.1");
		double b1 = 1;
		System.err.println(b1);
	}
}
