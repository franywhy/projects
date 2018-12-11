package com.izhubo.admin

import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.ServletRequestUtils

import javax.servlet.http.HttpServletRequest
import java.util.regex.Pattern

import static com.izhubo.rest.common.util.WebUtils.$$

@RestWithSession
class TeacherTopicDataController extends BaseController {
	
	
	public DBCollection users(){return mainMongo.getCollection("users");}
	public DBCollection topics(){return mainMongo.getCollection("topics");}
	public DBCollection qQUser(){return qquserMongo.getCollection("qQUser");}
	private DBCollection topic_bunus() {
		return mainMongo.getCollection("topic_bunus")
	}
	
	def list(HttpServletRequest request){
		
		Integer q_id = ServletRequestUtils.getIntParameter(request, "_id");
		String q_nick_name = ServletRequestUtils.getStringParameter(request, "nick_name");
		String username = ServletRequestUtils.getStringParameter(request, "username");
		
		Date stime = Web.getStime(request);
		Date etime = Web.getEtime(request);
		
		Long sl = 0l;
		Long el = 0l;
		if(stime){
			sl = stime.getTime();
		}
		if(stime){
			el = etime.getTime();
		}
		
		def query = QueryBuilder.start();
		query.and("priv1").is(1)
		query.and("priv").is(UserType.主播.ordinal());
		//手机号码
		if(StringUtils.isNotBlank(username) && q_id == null){
			String tuid = qQUser().findOne($$("username" : username) , $$("tuid" : 1))?.get("tuid");
			if(StringUtils.isNotBlank(tuid)){
				q_id = (Integer) users().findOne($$("tuid" : tuid ) , $$("_id" : 1))?.get("_id");
			}
		}
		if(q_id){
			query.and("_id").is(q_id);
		}
		
		if(StringUtils.isNotBlank(q_nick_name)){
			Pattern pattern = Pattern.compile("^.*" + q_nick_name + ".*\$", Pattern.CASE_INSENSITIVE);
			query.and("nick_name").regex(pattern);
		}
		
		def show = $$("_id" : 1 , "tuid" : 1 , "nick_name" : 1);
		def sort_sj = $$("topic_count" : -1);
		Crud.list(request,users(),query.get(),show,sort_sj){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				//用户id
				Integer user_id = obj["_id"] as Integer;
				
				String tuid = obj["tuid"];
				//用户联系方式
				obj["username"] = qQUser().findOne($$("tuid" : tuid) , $$("username" : 1))?.get("username");
				//删除tuid
				obj.remove("tuid");
				
				//抢答数量
				obj["topic_num"] = topics().count($$("teach_id" : user_id , "race_time" : $$( $gte : sl , $lte : el)));
				//好评数量
				Long good_num = topics().count($$("teach_id" : user_id , "race_time" : $$( $gte : sl , $lte : el) , "evaluation_type" : TopicEvaluationType.满意.ordinal() ));
				obj["good_num"] = good_num;
				Long ggood_num = topics().count($$("teach_id" : user_id , "race_time" : $$( $gte : sl , $lte : el) , "evaluation_type" : TopicEvaluationType.非常满意.ordinal()));
				obj["ggood_num"] = ggood_num;
				//不满意数量
				obj["bad_num"] = topics().count($$("teach_id" : user_id , "race_time" : $$( $gte : sl , $lte : el) , "evaluation_type" : TopicEvaluationType.不满意.ordinal()));
				//未评价数量
				obj["uneval_num"] = topics().count($$("teach_id" : user_id , "race_time" : $$( $gte : sl , $lte : el) , "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : TopicEvaluationType.未评价.ordinal()));
				//已结束问题数量
				Long topic_end_num = topics().count($$("teach_id" : user_id , "race_time" : $$( $gte : sl , $lte : el) , "type" : TopicsType.问题已结束.ordinal()));
				//聊天中的问题数量
				obj["topic_exchang_num"] = topics().count($$("teach_id" : user_id , "race_time" : $$( $gte : sl , $lte : el) , "type" : TopicsType.抢答成功.ordinal()));
				Double gp = 0d;
				if(topic_end_num > 0 ){
					gp = NumberUtil.formatDouble2((good_num + ggood_num) / topic_end_num);
				}
				//满意度
				obj["gp"] = gp;

				def query_bunus = $$("user_id" : user_id, open_time: $$($gte: sl, $lte: el))
				//红包数量
				obj["bunus_count"] = topic_bunus().count(query_bunus)
				//红包金额
				def bunus_money = topic_bunus().aggregate($$('$match', query_bunus), $$('$group', [_id : '$user_id', money:[$sum: '$mmoney']])).results().iterator()
				while (bunus_money.hasNext()){
					def money = bunus_money.next()
					if(money){
						obj["bunus_money"] = String.format("%.2f", money.get('money'))
					}
				}
			}
		}
	}
}
