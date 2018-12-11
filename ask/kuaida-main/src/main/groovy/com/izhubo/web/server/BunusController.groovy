package com.izhubo.web.server

import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.SimpleDateFormat
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.mongodb.DBCollection
import com.mysqldb.dao.BunusDao
/**
* 奖品
* @ClassName: BunusController 
* @Description: 奖品
* @author shihongjie
* @date 2015年7月10日 下午4:13:57 
*
*/
@RestWithSession
class BunusController extends BaseController {
	
	
	private BunusDao bunusDao = new BunusDao();
	
	def bunuLottery(){
		
	}
	
	
	def DBCollection bunus(){
		return mainMongo.getCollection("bunus");
	}
	
	private Lock lock = new ReentrantLock();
	
	def getBunus(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 100);
		List list = new ArrayList();
		for(int i = 0 ; i < size ; i ++){
			def lot = lotterys();
			if(lot){
				Map map = new HashMap();
				map["level"] = lot["level"];
				map["weight"] = lot["weight"];
				map["i"] = i;
				list.add(map);
			}
		}
		return getResultOKS(list);
	}
	
	//考虑递归
	def lotterys(){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//查询奖品池
		//TODO 时间戳/外键
		String year_month = getDate();
		def bunusList = bunus().find($$("userid" : null , "year_month" : year_month) , $$("level" : 1 , "unique_mark" : 1, "weight" : 1 , "_id" : 1)).limit(1000)?.toArray();
		if(bunusList){
			int weight_total = 0;
			bunusList.each {def dbo ->
				weight_total += dbo["weight"] as Integer;
			}
			Random rand = new Random();
			int randInt = rand.nextInt(9)+1;
//			int randInt = rand.nextInt(99)+1;
			
			int randomNumber = (int) ((randInt%10) * weight_total);//搜索
//			int randomNumber = (int) (Math.random() * weight_total);//搜索
			
			int priority = 0;
			
//			bunusList.each {def dbo ->
			for(int i = 0 ; i < bunusList.size() ; i ++){
				def dbo = bunusList.get(i);
				priority += dbo["weight"] as Integer;
				if (priority >= randomNumber) {
//					Lock
					
					lock.lock();
					try {
						def bunu = bunus().findOne($$("_id" : dbo["_id"] , userid : null));
						if(bunu){
							bunus().update($$("_id" : dbo["_id"] ) , $$($set : $$("userid" : user_id)));
							dbo["userid"] = user_id;
							return dbo;
						}else{
							lottery();
						}
					} finally{
						//显示释放锁
						lock.unlock();
					}
					
				}
			}
		}
		return null;
	}
	
	
	def getBunu(){
		return getResultOKS(lottery());
	}
	
	//TODO 根据问题的(抢答时间/结束节时间)当做时间戳
	//考虑递归
	def lottery(){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//查询奖品池
		//TODO 时间戳/外键
		String year_month = getDate();
		def bunusList = bunus().find($$("userid" : null , "year_month" : year_month)).limit(1000)?.toArray();
		if(bunusList){
			int weight_total = 0;
			bunusList.each {def dbo ->
				weight_total += dbo["weight"] as Integer;
			}
			
			int randomNumber = (int) (Math.random() * weight_total);//搜索
			
			int priority = 0;
			
//			bunusList.each {def dbo ->
			for(int i = 0 ; i < bunusList.size() ; i ++){
				def dbo = bunusList.get(i);
				priority += dbo["weight"] as Integer;
				if (priority >= randomNumber) {
//					Lock
					
					lock.lock();
					try {
						def bunu = bunus().findOne($$("_id" : dbo["_id"] , userid : null));
						if(bunu){
							bunus().update($$("_id" : dbo["_id"] ) , $$($set : $$("userid" : user_id)));
							dbo["userid"] = user_id;
							return dbo;
						}else{
							lottery();
						}
					} finally{
						//显示释放锁
						lock.unlock();
					}
					
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取日期字符串。
	 *
	 * <pre>
	 *  日期字符串格式： yyyyMMdd
	 *  其中：
	 *      yyyy   表示4位年。
	 *      MM     表示2位月。
	 * </pre>
	 *
	 * @return String "yyyyMMdd"格式的日期字符串。
	 */
	public static String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		return formatter.format(new Date());
	}
	
	
	
//	// 抽奖
//	
//		public synchronized Gift getGift() {
//	//    	double d = Math.random();
//			
//			int randomNumber = (int) (Math.random() * total());//搜索
//	
//			int priority = 0;
//	
//			for (Gift g : gifts) {
//	
//				priority += g.getType().getPriority();
//	
//				if (priority >= randomNumber) {
//	
//					
//					// 从奖品库移出奖品
//					
//	//            	1.获取奖品
//	//            	2.更新该奖品的状态
//					gifts.remove(g);
//	
//					return g;
//	
//				}
//	
//			}
//	
//			// 抽奖次数多于奖品时，没有奖品
//	
//			return null;
//	
//		}
	
}
