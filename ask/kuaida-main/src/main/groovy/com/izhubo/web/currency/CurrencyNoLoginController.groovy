package com.izhubo.web.currency

import javax.annotation.Resource;
import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.SimpleDateFormat
import java.util.List;

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import com.izhubo.model.CurrencyGainType
import com.izhubo.model.CurrencySyncState
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.persistent.KGS
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.vo.DailyRecommandVO
import com.izhubo.webservice.MemberaccountModel
import com.izhubo.webservice.SynchroMemberSoapProxy
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.wordnik.swagger.annotations.ApiOperation


/**
 * 每日推送相关内容
 * @date 2016年3月9日 下午5:42:49
 * @param @param request
 */
@Rest
@RequestMapping("/currencynologin")
class CurrencyNoLoginController extends BaseController {



	@Resource
	KGS currencyflowKGS;

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



	def TryToSync(HttpServletRequest request){

		SynchroMemberSoapProxy ss = new SynchroMemberSoapProxy();
		List<DBObject> failList = currency_log().find(new BasicDBObject("is_success",CurrencySyncState.尚未成功.ordinal())).toArray();
		for (int i = 0; i < failList.size(); i++) {
			BasicDBObject dbObject =   (BasicDBObject) failList.get(i);
			BasicDBObject result = new BasicDBObject();
			BasicDBObject up = new BasicDBObject();
			try {

				Integer user_id = dbObject.getInt("currency_user_id");
				String currency_money = dbObject.getString("currency_money");
				Integer gaintype = dbObject.getInt("currency_type");
				String gaintypeName = "";
				String md5result = "kuiada"+getUserPhoneByUserId(user_id)+currency_money+dbObject.get("currency_flow_id").toString()+AESKEY;
				md5result = MsgDigestUtil.MD5.digest2HEX(md5result).toLowerCase();



				MemberaccountModel menb = ss.updateUserCash("kuaida", getUserPhoneByUserId(user_id), currency_money,  gaintype, gaintypeName, dbObject.get("currency_flow_id").toString(), md5result);
				if(menb !=null) {
					up = new BasicDBObject('$set':
					new BasicDBObject("is_success" : CurrencySyncState.成功.ordinal()).append("last_sync_time", new Date())
					)
				}
				else {
					up = new BasicDBObject('$set':
					new BasicDBObject("is_success" : CurrencySyncState.尚未成功.ordinal()).append("last_sync_time", new Date())
					)
				}
			} catch (Exception e) {
				up = new BasicDBObject('$set':
				new BasicDBObject("is_success" : CurrencySyncState.尚未成功.ordinal()).append("last_sync_time", new Date())
				)
				e.printStackTrace()
			}

			currency_log().update(
					new BasicDBObject("currency_flow_id":dbObject.get("currency_flow_id")),up
					);

			//同步成功之后，写入数据库
		}
	}
	//扣除虚拟货币,成功true 失败false
	def deductCurrency(Integer user_id,double money)
	{
		SynchroMemberSoapProxy ss = new SynchroMemberSoapProxy();
		BasicDBObject result = new BasicDBObject();
		result.put("currency_flow_id", currencyflowKGS.nextId());
		result.put("currency_money", money);
		result.put("currency_time", System.currentTimeMillis());
		result.put("currency_type", CurrencyGainType.扣减.ordinal());
		result.put("currency_user_id", user_id);
		result.put("currency_user_name",getUserPhoneByUserId(user_id));
		result.put("is_success", CurrencySyncState.成功.ordinal());

		String md5result = "kuiada"+getUserPhoneByUserId(user_id)+money.toString()+result.get("currency_flow_id").toString()+AESKEY;
		md5result = MsgDigestUtil.MD5.digest2HEX(md5result).toLowerCase();
		try {

			MemberaccountModel menb = ss.updateUserCash("kuaida", getUserPhoneByUserId(user_id), money.toString(),  CurrencyGainType.扣减.ordinal(), CurrencyGainType.扣减.name(), result.get("currency_flow_id").toString(), md5result);
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
