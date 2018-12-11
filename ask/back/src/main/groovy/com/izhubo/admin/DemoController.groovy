package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.Map;

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

/**
 * 测试例子
 * @author shihongjie
 * 2016-01-21
 *
 */
@RestWithSession
class DemoController extends BaseController {
	
	private static final Map<String, String> TEAMS = ['1':'巴西','2':'喀麦隆','3':'墨西哥','4':'克罗地亚',
		'5':'西班牙','6':'智利','7':'澳大利亚','8':'荷兰',
		'9':'哥伦比亚','10':'科特迪瓦','11':'日本','12':'希腊',
		'13':'乌拉圭','14':'英格兰','15':'哥斯达黎加','16':'意大利',
		'17':'瑞士','18':'厄瓜多尔','19':'洪都拉斯','20':'法国',
		'21':'阿根廷','22':'尼日利亚','23':'伊朗','24':'波黑',
		'25':'德国','26':'加纳','27':'美国','28':'葡萄牙',
		'29':'比利时','30':'阿尔及利亚','31':'韩国','32':'俄罗斯']
	

	DBCollection table(){
		return mainMongo.getCollection('hq_demo');
	}
	
	def teams(HttpServletRequest req){
		[code:1, data:TEAMS]
	}
	def duiwu(HttpServletRequest req){
		[code:1, data:TEAMS]
	}

	
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC);
	}

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def demo = $$("_id" : UUID.randomUUID().toString());
		demo.put("user_id", user.get("_id") as Integer);
		demo.put("content", req["content"]);
		demo.put("timestamp", System.currentTimeMillis());
		table().save(demo);
		Crud.opLog("demo",[save:demo["_id"]]);
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		String id = req[_id];
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"content" : req["content"]
					)
				));
			
			Crud.opLog("demo",[edit:id]);
		return OK();
	}

	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("demo",[del:id]);
		return OK();
	}
}
