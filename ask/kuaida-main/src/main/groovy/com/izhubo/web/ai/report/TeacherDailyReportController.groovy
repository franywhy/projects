package com.izhubo.web.ai.report;


import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.BreakIterator;
import java.text.SimpleDateFormat

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.SessionFactory
import org.json.JSONObject
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.alipay.api.DefaultAlipayClient;
import com.izhubo.model.AccScoreGainType
import com.izhubo.model.CheckStatus
import com.izhubo.model.Code
import com.izhubo.model.DR
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.BaseController
import com.izhubo.web.JavaController;
import com.izhubo.web.api.Web
import com.izhubo.web.mq.MessageProductor
import com.izhubo.web.school.SchoolController
import com.izhubo.web.score.ScoreBase
import com.izhubo.web.vo.UserWalletDetailListVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
import com.izhubo.rest.anno.Rest

import static com.izhubo.rest.common.doc.MongoKey.$set
/**
 * 钱包api
 * @ClassName: UserController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 赵琨
 * @date 2016年4月12日 上午9:47:16
 *
 */
@Rest
@RequestMapping("/aireport")
class TeacherDailyReportController extends BaseController {

	public DBCollection topics() {
		return mainMongo.getCollection("topics");
	}
	public DBCollection users() {
		return mainMongo.getCollection("users");
	}
	public DBCollection ai_teacher_daily_report() {
		return mainMongo.getCollection("ai_teacher_daily_report");
	}



	@Resource
	private SchoolController schoolController;

	@Resource
	private SessionFactory sessionFactory;
	
	

	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	private static SimpleDateFormat yyyyMMdd000 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

	private static String formateyyyyMMddhhmmss(){
		return yyyyMMdd.format(new Date());
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String formate(){
		return sdf.format(new Date());
	}
	private Date Dateformate(){

		Date date = sdf.parse(formate());
		return   date ;
	}

	/**
	 * 当天的开始时间
	 * @return
	 */
	public static long startOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime();
	}
	/**
	 * 当天的结束时间
	 * @return
	 */
	public static long endOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date date=calendar.getTime();
		return date.getTime();
	}
	/**
	 * 昨天的开始时间
	 * @return
	 */
	public static long startOfyesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime();
	}
	
	
	public static long startOflastmonths() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, -30);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime();
	}
	/**
	 * 昨天的结束时间
	 * @return
	 */
	public static long endOfyesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.add(Calendar.DATE, -1);
		Date date=calendar.getTime();
		return date.getTime();
	}
	
	
	public static long startOfDayLong(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DATE, day);
		Date date=calendar.getTime();
		return date.getTime();
	}



	public Map getResultOKDefine(Object data , Integer all_page , Long count , Integer page ,Integer size,int checked_cout, Long all_check_count ){
		Map map = new HashMap();
		map.put("code", Code.OK);
		map.put("msg", Code.OK_S);
		map.put("data", data);
		map.put("all_page", all_page);
		map.put("count", count);
		map.put("page", page);
		map.put("size", size);
		map.put("checked_cout", checked_cout);
		map.put("all_check_count", all_check_count);
		return map;
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	public void caTeacherDaily(int teacher_id,Date time)
	{
//		//以老师为分组，统计1天内的数据，有时候要统计一个月内
//
//1、同个学员提问，同个老师多次解答1天内最多的次数。
//2、同个学员提问，同个老师多次解答一个月内，重复最多次数
//3、出现回答问题很简单的题目的次数（题目次数在10次以内的数量）。
//4、平均每小时抢答问题的次数
//5、老师的抢答数和平均值比较，抢答数突然增加（增加的百分比）
//7、多个学员提问，同个老师抢答，而这些学员又刚好是同个校区（或者通过城市）一星期内
//8、12点到7点之间抢答的问题总数量（一天内）
//10、之前被标记有作弊行为的老师
//11、之前被标记有作弊行为的学生的问题
//12 提问和抢答在1秒内的问题（当场提问，当场抢答）
		
		
//修改意见：增加一个小时分组，回答单个学员最多的
//typeA和TypeB增加最多的id
		
		def startOfToday = time.time;
		def startOfyesterday = time.plus(-1).time;
		def startOflastmonths = time.plus(-30).time;
		def startOflastweeks =  time.plus(-7).time;
		 SimpleDateFormat time_00format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		 SimpleDateFormat time_07format = new SimpleDateFormat("yyyy-MM-dd 07:00:00");
		def starOfToday00 = time_00format.format(time);
		def starOfToday07 = time_07format.format(time);
		
		//1、同个学员提问，同个老师多次解答1天内最多的次数。
		def TypeA = 0;
		def TypeA_Id = 0;
			def iterA = topics().aggregate(
				$$('$match', [timestamp:[$gt:startOfyesterday,$lt:startOfToday],"teach_id":teacher_id]),
				$$('$group', [_id: [author_id: '$author_id'],count:[$sum: 1]]),
				new BasicDBObject('$sort', ["_id.count":-1])
				).results().iterator();
			while(iterA.hasNext()){
				def finaObj = iterA.next();
				TypeA  = finaObj.get("count");
				TypeA_Id = finaObj.get("_id").get("author_id");
				break;
			}
			
		
	    //2、同个学员提问，同个老师多次解答一个月内，重复最多次数
		def TypeB = 0;
		def TypeB_Id = 0;
		def iterB = topics().aggregate(
			$$('$match', [timestamp:[$gt:startOflastmonths,$lt:startOfToday],"teach_id":teacher_id]),
			$$('$group', [_id: [author_id: '$author_id'],count:[$sum: 1]]),
			new BasicDBObject('$sort', ["_id.count":-1])
			).results().iterator();
		
		while(iterB.hasNext()){
			def finaObj = iterB.next();
			TypeB  = finaObj.get("count");
			TypeB_Id =finaObj.get("_id").get("author_id");
			break;
		}
		//3、出现回答问题很简单的题目的次数（题目次数在10次以内的数量）。
		def TypeC = 0;
		TypeC = topics().count($$(["reply_count":[$lt:10],"teach_id":teacher_id,timestamp:[$gt:startOfyesterday,$lt:startOfToday]]));
		
		//4、平均每小时抢答问题的次数中最多的一次
		def TypeD = 0;
		def iterD = topics().aggregate(
			$$('$match', [timestamp:[$gt:startOfyesterday,$lt:startOfToday],"teach_id":teacher_id]),
			$$('$group', [_id: [$hour: '$timestamp_date'],count:[$sum: 1]]),
			new BasicDBObject('$sort', ["_id.count":-1])
			).results().iterator();
		
		while(iterD.hasNext()){
			def finaObj = iterD.next();
			TypeD  = finaObj.get("count");
			break;
		}
		
		//5、老师的抢答数和平均值比较，抢答数突然增加（增加的百分比）
		float TypeE = 0;
		def allcount = 0;
		def count_months = topics().count($$(timestamp:[$gt:startOflastmonths,$lt:startOfToday],"teach_id":teacher_id));
		def avger_months = count_months/30;		
		def today_count = topics().count($$(timestamp:[$gt:startOfyesterday,$lt:startOfToday],"teach_id":teacher_id));
		if(today_count>avger_months)
		{
			TypeE =( (today_count - avger_months)/avger_months)*100;
		}
		else
		{
			TypeE  = 0;
		}
		
		//7、多个学员提问，同个老师抢答，而这些学员又刚好是同个校区（或者通过城市）一天内
		def TypeF = 0;
		def inlist = topics().distinct("author_id",$$(timestamp:[$gt:startOfyesterday,$lt:startOfToday],"teach_id":teacher_id));
		def teachercity =  users().findOne($$("_id",teacher_id)).get("city");
		TypeF =  users().count($$(_id:[$in:inlist],"city":teachercity));
		def userlist = users().find($$(_id:[$in:inlist],"city":teachercity)).toArray();
		
		//8、12点到7点之间抢答的问题总数量（一天内）
		def TypeG = 0;
		def nightcount = 0;
		nightcount = topics().count($$(timestamp:[$gt:starOfToday00,$lt:starOfToday07],"teach_id":teacher_id));
		TypeG = nightcount;
		
		
		def TypeH = 0;
		
		def TypeI = 0;
		
		
		def result = $$("_id":sdf.format(time)+teacher_id);
		    result.append("date", time);
		    result.append("teacher_id", teacher_id);
		    result.append("typeA", TypeA);
			result.append("TypeA_Id", TypeA_Id);

			
			result.append("typeB", TypeB);
			result.append("TypeB_Id", TypeB_Id);
			result.append("typeC", TypeC);
			result.append("typeD", TypeD);
			result.append("typeE", TypeE);
			result.append("typeF", TypeF);
			result.append("typeF_userlist", userlist);
			
			result.append("typeG", TypeG);
			result.append("typeH", TypeH);
			result.append("typeI", TypeI);
		
		
		
		ai_teacher_daily_report().save(result);
	
	
	}
	
	@ResponseBody
	@RequestMapping(value = "getresult", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	def getresult(HttpServletRequest request){
	
		Date now1 =  yyyyMMdd000.parse("2016-03-28 00:00:00");
		caTeacherDaily(10360256,now1);
		
		
		Date now2 =  yyyyMMdd000.parse("2016-03-26 00:00:00");
		caTeacherDaily(10283401,now2);
		
		
		Date now3 =  yyyyMMdd000.parse("2016-03-28 00:00:00");
		caTeacherDaily(10169596,now3);
		
		
		Date now4 =  yyyyMMdd000.parse("2016-04-23 00:00:00");
		caTeacherDaily(10201938,now4);
		
		def iterA = users().find($$("priv",2)).iterator();
		int i=0;
		while(iterA.hasNext()){
			def finaObj = iterA.next();
			Integer _id  = Integer.parseInt(finaObj.get("_id").toString()) ;
			i++;
			Date now =  yyyyMMdd000.parse("2016-09-01 00:00:00");
			
			caTeacherDaily(_id,now);
		  
			 
			
		
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "getresult_v2", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	def getresult_v2(HttpServletRequest request){
	
		String star_date = request["date"].toString();
		
		def iterA = users().find($$("priv",2)).iterator();
		int i=0;
		while(iterA.hasNext()){
			def finaObj = iterA.next();
			Integer _id  = Integer.parseInt(finaObj.get("_id").toString()) ;
			i++;
			Date now =  yyyyMMdd000.parse(star_date+" 00:00:00");	
			caTeacherDaily(_id,now);		
			println i;

		}
	}
	
	@ResponseBody
	@RequestMapping(value = "getresult_v3", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	def getresult_v3(HttpServletRequest request){
	
		String month = request["month"].toString();
		String year = request["year"].toString();
		Calendar calendar = Calendar.getInstance();
		int year_int = Integer.parseInt(year);
		int month_int = Integer.parseInt(month) - 1;
		calendar.set(year_int, month_int, 1);
		int maxDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Object> datas = new ArrayList<Object>();
		for (int j=1; j<=maxDay; j++) {
		calendar.set(year_int, month_int, j);
		String star_date = sdf.format(calendar.getTime());
		
		def iterA = users().find($$("priv",2)).iterator();
		int i=0;
		while(iterA.hasNext()){
			def finaObj = iterA.next();
			Integer _id  = Integer.parseInt(finaObj.get("_id").toString()) ;
			i++;
			Date now =  yyyyMMdd000.parse(star_date+" 00:00:00");
			caTeacherDaily(_id,now);
			println i;

		}
		}
		
		
		
		
		
	}
	
	@ResponseBody
	@RequestMapping(value = "format_date", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	def format_date(HttpServletRequest request){
	

		
		def iterA = topics().find().iterator();
		int i=0;
		while(iterA.hasNext()){
			def finaObj = iterA.next();
			String _id  = finaObj.get("_id");
			i++;
			Date create_date =  new Date(Long.parseLong(finaObj.get("timestamp").toString()));
		
			BasicDBObject up = new BasicDBObject("timestamp_date", create_date);
			
			topics().update(new BasicDBObject("_id", _id), new BasicDBObject($set, up));
			
		
			
			println i;

		}
	}
	

	
	
	
	





	

	


	

	


	public static void main(String[] args) throws Exception {

		
		System.out.println(formateyyyyMMddhhmmss());
	}



}
