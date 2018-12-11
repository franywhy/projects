package com.izhubo.web.income

import com.alibaba.fastjson.JSONObject
import com.hqonline.model.UserIncomType
import com.izhubo.model.ApplyState
import com.izhubo.model.Code
import com.izhubo.rest.AppProperties
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.persistent.KGS
import com.izhubo.utils.HttpClientUtil4_3
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.server.EarningsController
import com.izhubo.web.server.UserIncomController
import com.izhubo.web.server.UserInfoController
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mysqldb.dao.ApplyDao
import com.mysqldb.dao.BunusDao
import com.mysqldb.model.Apply
import com.mysqldb.model.UserFinance
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang.StringUtils
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.orm.hibernate4.HibernateTransactionManager
import org.springframework.web.bind.ServletRequestUtils

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

import static com.izhubo.rest.common.util.WebUtils.$$

@RestWithSession
class InComeController extends BaseController {

	private static Logger logger = LoggerFactory
	.getLogger(InComeController.class);

	@Resource
	protected StringRedisTemplate mainRedis;

	@Resource
	private SessionFactory sessionFactory;
	@Resource
	private UserIncomController userincomController;

	
	private DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus");
	}
	
	
	@Resource
	private  HibernateTransactionManager txManager;

	private ApplyDao applyDao = new ApplyDao();
	
	private BunusDao bunusDao = new BunusDao();
	
	@Resource
	private UserInfoController userInfoController;
	@Resource
	private EarningsController earningsController;

	@Resource
	KGS applyKGS;

	private static final String PRIVKEY = "nWLFRmgUd2CypottTm0d";

	public Object apply(HttpServletRequest req, HttpServletResponse res) {
		
		

		Map<String, Object> map = new HashMap<String, Object>();
		int user_id = Web.getCurrentUserId();
		String nickname = req.getParameter("nick_name");
		//支付宝正式姓名
		String real_name = req.getParameter("real_name");
		//支付宝账号
		String alipay_account = req.getParameter("alipay_account");
		
		//2017-03-25  支付宝参数校验
		if(StringUtils.isBlank(real_name) || StringUtils.isBlank(alipay_account)){
			return getResultParamsError();
		}
//		//2017-03-25 去重空格
//		real_name = real_name.trim();
//		alipay_account = alipay_account.trim();
		//2017-03-25 去除字符串中的空格、回车、换行符、制表符
		real_name = replaceBlank(real_name);
		alipay_account = replaceBlank(alipay_account);
		
//		String apply_money = req.getParameter("apply_money");
		//申请金额
		String apply_money = String.valueOf(earningsController.userFinanceMoneyByUserId(user_id));
		String psw = req.getParameter("psw");
		
		//判断用户金额是否大于100
		Double intMon = Double.valueOf(apply_money);
		//这里也要动态配置到数据库
		if(intMon < 100 ){
			return getResultParamsError();
		}
		float monFloat = Float.valueOf(apply_money)

		try {
			String tuid = users().findOne($$("_id":user_id),$$("tuid":1)).get("tuid")
			String mobile = qQUser().findOne($$("tuid":tuid),$$("username":1)).get("username")
			Map<String, String> param = new HashMap<>()
			param.put("mobileNo",mobile)
			param.put("passWord",Base64.encodeBase64String(psw.getBytes()))
			logger.info("SSO[/inner/checkPassWord]参数！param="+param.toString())
			String result = HttpClientUtil4_3.post(AppProperties.get("sso_checkPassWord_post"),param,null)
			logger.info("SSO[/inner/checkPassWord]接口返回结果！接口返回结果result="+result)
			if(StringUtils.isNotBlank(result)){
				JSONObject object = JSONObject.parseObject(result)
				if(200 == object.getIntValue("code") && "true".equals(object.getString("data"))) {

					UserFinance uf = (UserFinance) sessionFactory
							.getCurrentSession().createCriteria(UserFinance.class)
							.add(Restrictions.eq(UserFinance.PROP_USERID, user_id))
							.uniqueResult();

					userInfoController.updateUserInfo(real_name, alipay_account);

					if(uf == null)
					{
						return getResult(Code.申请金额超出总金额, Code.申请金额超出总金额S,
								Code.申请金额超出总金额_S);
					}
					if (uf.getUserMoney()  >= monFloat) {


						try {
							sessionFactory.getCurrentSession().beginTransaction();
							Apply app = new Apply();
							app.setUserId(user_id);
							app.setRealName(real_name);
							app.setAlipayAccount(alipay_account);

							def ahurl = mainMongo.getCollection("ah_url").findOne($$("_id" : 1025))
							if(ahurl) {
								app.setFeeRatio(ahurl.get("name") as String)
							}

							if(apply_money!="")
							{
								if(ahurl) {
									float money = monFloat
									float ratio = ahurl.get("url") as float
									apply_money = String.valueOf(money - money * ratio)
								}
								BigDecimal bd=new BigDecimal(apply_money);
								//设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
								bd=bd.setScale(2, BigDecimal.ROUND_HALF_UP);
								app.setApplyMoney(bd);
							}

							Long now = System.currentTimeMillis();

							app.setApplyState(ApplyState.已申请.ordinal());
							app.setNickName(nickname);
							app.setApplyFlowId(applyKGS.nextId().toString());
							app.setCreateTime(new java.sql.Timestamp(now));
							app.setApplyYearMonth(dateToString(new Date()));

							applyDao.init(sessionFactory);
							applyDao.saveEntity(app);
							uf.setUserMoney(uf.getUserMoney() - monFloat);
							sessionFactory.getCurrentSession().update(uf);
							sessionFactory.getCurrentSession().getTransaction().commit();
							sessionFactory.getCurrentSession().flush();

							//申请提现日志
							userincomController.save(user_id, 0-intMon, UserIncomType.申请提现.ordinal() ,  null , now , app.getId());
						} catch (Exception e) {

							sessionFactory.getCurrentSession().getTransaction().rollback();

							throw new RuntimeException(e);
						}


				

						return getResultOK("提现申请成功");
					} else {
						return getResult(Code.申请金额超出总金额, Code.申请金额超出总金额_S,
								Code.申请金额超出总金额_S);
					}
				} else {
					return getResult(Code.密码不正确, object.getString("data"), object.getString("data"))
				}
			} else {
				return getResult(Code.密码不正确, Code.密码不正确_S, Code.密码不正确_S)
			}
		} catch (Exception e) {
			logger.error(e.getMessage())
			e.printStackTrace()
			return getResult(Code.系统异常稍后再试, Code.系统异常稍后再试_S,
			Code.系统异常稍后再试_S)
		}

	}
	
	/**
	 * 去除字符串中的空格、回车、换行符、制表符
	 * @param str
	 * @return
	 */
	private static Pattern ALIPAY_PATTERN = Pattern.compile("\\s*|\t|\r|\n");
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Matcher m = ALIPAY_PATTERN.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	public static String dateToString(Date time){
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat ("yyyyMM");
		String ctime = formatter.format(time);

		return ctime;
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	public Object history_income(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> map = new HashMap<String, Object>();
		int user_id = Web.getCurrentUserId();
		Integer size = ServletRequestUtils.getIntParameter(req , "size" , 20);
		Integer page = ServletRequestUtils.getIntParameter(req , "page" , 1);
		
		
		BasicDBObject query = new BasicDBObject();
		query.append("user_id" , user_id);


		def sort = $$("create_time" : -1);
		

				def count = topic_bunus().count(query);
				int allpage = count / size + ((count% size) >0 ? 1 : 0);
				//查询结果
				def queryResult = null;
				if(count > 0){
					//需要查询的字段
		
					queryResult = topic_bunus().find(query).sort(sort).skip((page - 1) * size).limit(size).toArray();
				}
				
				
				if(queryResult){
					queryResult.each {def dbo->
						
						//这里判断是否预约,預約表還沒設計
						
					
							dbo["yearMon"] = dateToString(new Date(Long.valueOf(dbo["create_time"].toString())));
							dbo["mtimestamp"] = dbo["create_time"];
							dbo["openTime"] = dbo["create_time"];
							dbo["lotteryTime"] = dbo["create_time"];
				
		
						
					}
				}
				
				return getResultOK(queryResult, allpage, count , page , size);
			



	}
	
	


}


