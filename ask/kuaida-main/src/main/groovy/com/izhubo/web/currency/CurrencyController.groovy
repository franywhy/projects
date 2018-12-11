package com.izhubo.web.currency

import javax.annotation.Resource;
import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$






import java.text.SimpleDateFormat
import java.util.TreeMap.PrivateEntryIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod





import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode







import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody








import com.izhubo.model.CurrencyGainType
import com.izhubo.model.CurrencySyncState
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.rest.persistent.KGS
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.vo.DailyRecommandVO
import com.izhubo.webservice.MemberaccountModel
import com.izhubo.webservice.SynchroMemberSoapProxy
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.wordnik.swagger.annotations.ApiOperation





import org.springframework.beans.factory.annotation.Value

import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
/**
 * 每日推送相关内容
 * @date 2016年3月9日 下午5:42:49
 * @param @param request
 */
@RestWithSession
@RequestMapping("/currency")
class CurrencyController extends BaseController {


	private Lock lock = new ReentrantLock();
	@Resource
	KGS currencyflowKGS;

	@Value("#{application['currencysync.domain']}")
	private String kjcity_currency_syncdomain ="http://my.kjcity.com/";

	private static final String AESKEY = "%^\$AF>.12*******";

	/** 虚拟货币操作日志 */
	public DBCollection currency_log() {
		return mainMongo.getCollection("currency_log");
	}

	/** 虚拟货币失败操作日志 */
	public DBCollection currency_fail_log() {
		return mainMongo.getCollection("currency_fail_log");
	}

	/** 虚拟货币类型 */
	public DBCollection currency_type() {
		return mainMongo.getCollection("currency_type");
	}

	@RequestMapping(value = "getCurrencyBalance/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取虚拟货币余额", httpMethod = "GET",  notes = "获取虚拟货币余额", response = DailyRecommandVO.class)
	def getCurrencyBalance(HttpServletRequest request){
		Integer user_id = Web.getCurrentUserId();
		String username =getUserPhoneByUserId();
		String  md5result = MsgDigestUtil.MD5.digest2HEX(username+AESKEY).toLowerCase();
		BasicDBObject jo = new BasicDBObject();
		jo.put("balance", 0);
		jo.put("allrecharge", 0);
		jo.put("allcost", 0);
		try {
			String reqResult = HttpClientUtil4_3.get(kjcity_currency_syncdomain+"/api/accountHandler.ashx?username="+username+"&key="+md5result,null);
			JSONObject jresult = new JSONObject(reqResult);
			if(jresult.getInt("code").equals(0)) {
				Double balance =  java.lang.Double.valueOf(jresult.getJSONObject("datas").get("CASH").toString()) ;
				Double allrecharge =java.lang.Double.valueOf(jresult.getJSONObject("datas").get("ALLCASH").toString()) ;
				Double allcost = allrecharge - balance;
				jo.put("balance", balance);
				jo.put("allrecharge", allrecharge);
				jo.put("allcost", allcost);
			}
		} catch (Exception e) {


			println e.toString();
		}

		getResultOK(jo);
	}

	@RequestMapping(value = "getCurrencyLog/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取虚拟货币消费记录", httpMethod = "GET",  notes = "获取虚拟货币余额", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "size", value = "每页size条数据,默认20条", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "第page页,默认第1页", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "iscost", value = "0 消费 1充值", required = false, dataType = "int", paramType = "query"),
	])
	def getCurrencyLog(HttpServletRequest request){
		Integer user_id = Web.getCurrentUserId();
		String username =getUserPhoneByUserId();
		String  md5result = MsgDigestUtil.MD5.digest2HEX(username+AESKEY).toLowerCase();

		Integer iscost = ServletRequestUtils.getIntParameter(request, "iscost",0);
		Integer page  = ServletRequestUtils.getIntParameter(request, "page",1);
		Integer size  = ServletRequestUtils.getIntParameter(request, "size",20);


		//1增加 2扣减
		if(iscost == 0)
		{
			iscost = 2;
		}
		else if(iscost == 1)
		{
			iscost = 1;
		}

		getResultOK( getCurrencyLogFromKjcity(username,page,size,iscost));
	}
	@RequestMapping(value = "getCurrencyLog_v310/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取虚拟货币消费记录", httpMethod = "GET",  notes = "获取虚拟货币余额", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "size", value = "每页size条数据,默认20条", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "第page页,默认第1页", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "iscost", value = "0 消费 1充值", required = false, dataType = "int", paramType = "query"),
	])
	def getCurrencyLog_v310(HttpServletRequest request){
		Integer user_id = Web.getCurrentUserId();
		String username =getUserPhoneByUserId();
		String  md5result = MsgDigestUtil.MD5.digest2HEX(username+AESKEY).toLowerCase();

		Integer iscost = ServletRequestUtils.getIntParameter(request, "iscost",0);
		Integer page  = ServletRequestUtils.getIntParameter(request, "page",1);
		Integer size  = ServletRequestUtils.getIntParameter(request, "size",20);


		//1增加 2扣减
		if(iscost == 0)
		{
			iscost = 2;
		}
		else if(iscost == 1)
		{
			iscost = 1;
		}
		return getCurrencyLogFromKjcity(username,page,size,iscost)
	}

	def String process_create_time(String timestr)
	{

		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		long longstr = long.parseLong(timestr.substring(6,19));
		Date createdate = new Date(longstr);
		String resut = formatter.format(createdate);
		return resut;

	}


	def String moneyfomart(Double money)
	{
		if(money>=0)
		{
			return "+"+money.toString();
		}
		else
		{
			return money.toString();
		}

	}



	//从会计城那里同步消费记录json数组
	@TypeChecked(TypeCheckingMode.SKIP)
	def getCurrencyLogFromKjcity(String username,int pageindex,int pagesize,int iscost){
		String  md5result = MsgDigestUtil.MD5.digest2HEX(username+AESKEY).toLowerCase();

		List resultlist = new ArrayList();



		try {
			String reqResult = HttpClientUtil4_3.get(kjcity_currency_syncdomain+"/api/accountLogHandler.ashx?username="+username+"&key="+md5result+"&pageindex="+pageindex+"&pagesize="+pagesize+"&type="+iscost,null);
			JSONObject jresult = new JSONObject(reqResult);
			if(jresult)
			{
				JSONArray paylist =jresult.getJSONArray("datas");
				for(int i=0;i<paylist.length();i++)
				{
					JSONObject item = paylist.getJSONObject(i);
					BasicDBObject bc = new BasicDBObject();

					if(item.getInt("OPTYPE") == 1)
					{
						bc.put("money", moneyfomart(item.getDouble("INCOME")));
					}
					else if(item.getInt("OPTYPE") == 2)
					{
						bc.put("money", moneyfomart(item.getDouble("OUTLAY")));
					}
					bc.put("memo", item.getString("MEMO"));
					bc.put("create_time", process_create_time(item.get("CREATIONTIME").toString()));
					resultlist.add(bc);
				}




			}


		} catch (Exception e) {


			e.printStackTrace();
		}

		getResultOK(resultlist);
	}

	@RequestMapping(value = "getCurrencyType/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取虚拟货币充值类型", httpMethod = "GET",  notes = "获取虚拟货币充值类型", response = DailyRecommandVO.class)
	def getCurrencyType(HttpServletRequest request){




		getResultOK(currency_type().find($$("type":"default")).toArray());
	}


	@RequestMapping(value = "getIOSPAYCurrencyType/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取虚拟货币充值类型_iospay", httpMethod = "GET",  notes = "获取虚拟货币充值类型_iospay", response = DailyRecommandVO.class)
	def getIOSPAYCurrencyType(HttpServletRequest request){




		getResultOK(currency_type().find($$("type":"ios")).toArray());
	}

	//扣除虚拟货币,成功true 失败false
	def deductCurrency(Integer user_id,double money,String paytype)
	{



		//锁
		lock.lock();
		try {
			if(money>0)
			{
				money = money*-1;
			}
			SynchroMemberSoapProxy ss = new SynchroMemberSoapProxy();
			BasicDBObject result = new BasicDBObject();
			result.put("currency_flow_id", currencyflowKGS.nextId());
			result.put("currency_money", money);
			result.put("currency_time", System.currentTimeMillis());
			result.put("currency_type", CurrencyGainType.扣减.ordinal());
			result.put("currency_type_str", paytype);
			result.put("currency_user_id", user_id);
			result.put("currency_user_name",getUserPhoneByUserId(user_id));
			result.put("is_success", CurrencySyncState.成功.ordinal());


			//判断余额是否足够，目前采用http请求去获取，后期优化
			Double balance = 0;
			String username =getUserPhoneByUserId(user_id);
			String  banlancemd5result = MsgDigestUtil.MD5.digest2HEX(username+AESKEY).toLowerCase();
			try {
				String reqResult = HttpClientUtil4_3.get(kjcity_currency_syncdomain+"/api/accountHandler.ashx?username="+username+"&key="+banlancemd5result,null);
				JSONObject jresult = new JSONObject(reqResult);
				if(jresult.getInt("code").equals(0))
				{
					balance =  java.lang.Double.valueOf(jresult.getJSONObject("datas").get("CASH").toString()) ;
				}


			} catch (Exception e) {

				return false;
			}

			//这里扣减的数额是负数，因此用+号
			if((balance +money)<0 )
			{
				return false;
			}

			String md5result = "kuiada"+getUserPhoneByUserId(user_id)+money.toString()+result.get("currency_flow_id").toString()+AESKEY;
			md5result = MsgDigestUtil.MD5.digest2HEX(md5result).toLowerCase();

			//先判断虚拟货币的余额是否足够

			try {

				MemberaccountModel menb = ss.updateUserCash("kuiada", getUserPhoneByUserId(user_id), money.toString(),  CurrencyGainType.扣减.ordinal(), paytype, result.get("currency_flow_id").toString(), md5result);
				if(menb !=null)
				{
					currency_log().save(result);
					return true;
				}
				else
				{
					currency_fail_log().save(result);
					return false;
				}
			} catch (Exception e) {
				currency_fail_log().save(result);
				return false;

			}
		} catch (Exception e) {

			return false;
		}finally{
			//显示释放锁
			lock.unlock();
		}

	}

	public Object apply(HttpServletRequest req) {

		increaseCurrency(10017793,10.0,CurrencyGainType.充值);
	}

	public Object apply_d(HttpServletRequest req) {

		return	deductCurrency(10017793,-10.0,"打赏");
	}



	//新增虚拟货币，新增始终是异步操作，默认是成功
	public void increaseCurrencyByFlow(String folowid,Integer user_id,double money,CurrencyGainType gaintype)
	{
		//请求第三方接口，并记录日志，调用第三方接口
		SynchroMemberSoapProxy ss = new SynchroMemberSoapProxy();
		BasicDBObject result = new BasicDBObject();
		result.put("currency_flow_id", folowid);
		result.put("currency_money", money);
		result.put("currency_time", System.currentTimeMillis());
		result.put("currency_type", gaintype.ordinal());
		result.put("currency_user_id", user_id);
		result.put("currency_user_name",getUserPhoneByUserId(user_id));

		String md5result = "kuiada"+getUserPhoneByUserId(user_id)+money.toString()+ result.get("currency_flow_id").toString()+AESKEY;
		md5result = MsgDigestUtil.MD5.digest2HEX(md5result).toLowerCase();

		try {

			MemberaccountModel menb = ss.updateUserCash("kuiada", getUserPhoneByUserId(user_id), money.toString(),  gaintype.ordinal(), gaintype.name(), result.get("currency_flow_id").toString(), md5result);
			if(menb !=null)
			{
				result.put("is_success", CurrencySyncState.成功.ordinal());
			}
			else
			{
				result.put("is_success", CurrencySyncState.尚未成功.ordinal());
			}
		} catch (Exception e) {
			result.put("is_success", CurrencySyncState.尚未成功.ordinal());
			e.printStackTrace()
		}

		currency_log().save(result);

	}


	//新增虚拟货币，新增始终是异步操作，默认是成功
	public void increaseCurrency(Integer user_id,double money,CurrencyGainType gaintype)
	{

		//请求第三方接口，并记录日志，调用第三方接口
		increaseCurrencyByFlow(currencyflowKGS.nextId().toString(),user_id,money,gaintype);


	}

	//获取会豆余额
	double getCurrencyBalance(Integer user_id)
	{
		return 100;
	}

	//判断时间是否在当前范围
	//是否在直播中 0：尚未开始  1：已经开始 2：已经结束
	def checkTimeZone(Date start,Date end,Date now)
	{
		if(now<start)
		{
			return  0;
		}
		else if(now>end)
		{
			return  2;
		}
		else
		{
			return  1;
		}
	}

	public static String getTimeShort(Date time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String dateString = formatter.format(time);
		return dateString;
	}

	/**
	 * 获取现在时间
	 *
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}







}
