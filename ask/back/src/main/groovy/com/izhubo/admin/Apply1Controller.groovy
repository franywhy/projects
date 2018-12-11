package com.izhubo.admin

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import com.izhubo.model.ApplyType
import com.izhubo.model.OpType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class Apply1Controller extends BaseController{

    DBCollection table(){adminMongo.getCollection('applys')}


    // 签约申请查询
    def list(HttpServletRequest req){
        def query = Web.fillTimeBetween(req)
        query = Web.fillTimeBetween(query, "lastmodif", req)
        ['status','broker','xy_user_id'].each {String field->
            intQuery(query,req,field)
        }
        stringQuery(query,req,'qq')

        Crud.list(req,table(),query.get(),new BasicDBObject('sfz_pic',0),MongoKey.SJ_DESC){List<BasicDBObject> data->
            def users = users()
            def rooms = rooms()
            for(BasicDBObject obj: data){ // 更新昵称 http://192.168.1.181/redmine/issues/3810
                obj.put("nick_name", users.findOne(obj['xy_user_id'],new BasicDBObject("nick_name",1))?.get("nick_name") )
                def myroom =  rooms.findOne(new BasicDBObject('xy_star_id',obj['xy_user_id'] as Integer))
                obj.put('test',myroom?.get("test"));
				//add by shihongjie 2014-12-12
				//大类
				def mainType = mainMongo.getCollection('mainType').findOne(new BasicDBObject("_id":obj["mainType_Id"]),new BasicDBObject("MainTypeName" :1,"mainTypeCode" :1));
                obj.put('mainTypeCode',mainType.get("mainTypeCode"));
                obj.put('MainTypeName',mainType.get("MainTypeName"));
				//行业
				def industry = mainMongo.getCollection('industry').findOne(new BasicDBObject("_id":obj["industry_Id"]),new BasicDBObject("industryName" :1,"industryCode" :1));
                obj.put('industryName' , industry.get("industryName"));
                obj.put('industryCode' , industry.get("industryCode"));
				//专业
				def professional = mainMongo.getCollection('professional').findOne(new BasicDBObject("_id":obj["professional_Id"]),new BasicDBObject("ProfessionalName" :1,"professionalCode" :1));
                obj.put('ProfessionalName',professional.get("ProfessionalName"));
                obj.put('professionalCode',professional.get("professionalCode"));
            }
        }
    }
	/**
	 * 审核 用户申请签约主播  通过/不通过
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
    def handle(HttpServletRequest req)
    {
        def status = req.getInt('status');
		//1.未通过  2.通过
        if (status == ApplyType.通过.ordinal() || status == ApplyType.未通过.ordinal()){

            Long time = System.currentTimeMillis()
			//修改申请记录状态
            def record =  table().findAndModify(new BasicDBObject(_id:req[_id],status:ApplyType.未处理.ordinal()),
                    new BasicDBObject('$set':[status:status,lastmodif:time]))
            if (record ){
				//用户ID
                def user_id = record.get('xy_user_id') as Integer
				//如果是通过操作
                if (status == ApplyType.通过.ordinal()){
					//代理人ID
                   def brokerId = record.get('broker') as Integer
                   def sex = record.get('sex') as Integer
				   //更新user表中的用户信息
				   //priv : 更新用户类型  用户类型:1运营人员,2主播,3普通用户,4客服人员,5经纪人
				   //star : 主播信息
				   //broker : 代理人ID
                   if (users().update(new BasicDBObject(_id,user_id),
                           new BasicDBObject($set:[priv:UserType.主播.ordinal(),star:
                                   [room_id:user_id,timestamp:time,broker:brokerId,sex:sex]
                           ])
                           ,false,false,writeConcern).getN() == 1)
                   {
					   //通过id获取用户昵称
                       String nick_name = users().findOne(new BasicDBObject(_id,user_id),new BasicDBObject(nick_name:1))?.get("nick_name")
					   //创建新的直播间
					   //live : 是否开播 
					   //visiter_count : 访问人数
					   //room_ids : 房间编号
					   //nick_name : 主播昵称
					   
            		   //mainTypeCode : 大类
            		   //industryCode : 行业
            		   //professionalCode : 专业
                       rooms().save(new BasicDBObject(
                               _id:user_id,xy_star_id:user_id,live:Boolean.FALSE, bean : 0, visiter_count:0,found_time:time,room_ids:user_id.toString(),nick_name: nick_name ,real_sex:0,
							   //add by shihongjie 2014-12-12
							   mainType_Id:record.get('mainType_Id'),//大类
							   industry_Id:record.get('industry_Id'),//行业
							   professional_Id:record.get('professional_Id')//专业编号
                       ),writeConcern)
					   //如果没有代理人
                       if (null != brokerId)
                       {
						   //将用户信息中代理人id设置为自己 代理人数量自增一
                           users().update(new BasicDBObject(_id,brokerId),
                                   new BasicDBObject($addToSet:['broker.stars':user_id],$inc:['broker.star_total':1]))
                       }
                       Web.api("java/flushuser?id1=${user_id}")
                   }
                }
				//日志记录
                Crud.opLog(OpType.apply_handle,[user_id:user_id,status:status])
            }
        }
        OK()
    }
	//add by shihongjie 2014-12-12
    def show(HttpServletRequest req){
        def obj = table().findOne(req[_id]);
		if(null != obj){
//			//大类
//			obj.put('MainTypeName',mainMongo.getCollection('mainType').findOne(new BasicDBObject("mainTypeCode":obj["mainTypeCode"]),new BasicDBObject("MainTypeName" :1))?.get("MainTypeName"));
//			//行业
//			obj.put('industryName',mainMongo.getCollection('industry').findOne(new BasicDBObject("industryCode":obj["industryCode"]),new BasicDBObject("industryName" :1))?.get("industryName"));
//			//专业
//			obj.put('ProfessionalName',mainMongo.getCollection('professional').findOne(new BasicDBObject("professionalCode":obj["professionalCode"]),new BasicDBObject("ProfessionalName" :1))?.get("ProfessionalName"));
			
			//大类
			def mainType = mainMongo.getCollection('mainType').findOne(new BasicDBObject("_id":obj["mainType_Id"]),new BasicDBObject("MainTypeName" :1,"MainTypeCode" :1));
			obj.put('mainTypeCode',mainType.get("mainTypeCode"));
			obj.put('MainTypeName',mainType.get("MainTypeName"));
			//行业
			def industry = mainMongo.getCollection('industry').findOne(new BasicDBObject("_id":obj["industry_Id"]),new BasicDBObject("industryName" :1,"industryCode" :1));
			obj.put('industryName' , industry.get("industryName"));
			obj.put('industryCode' , industry.get("industryCode"));
			//专业
			def professional = mainMongo.getCollection('professional').findOne(new BasicDBObject("_id":obj["professional_Id"]),new BasicDBObject("ProfessionalName" :1,"ProfessionalCode" :1));
			obj.put('ProfessionalName',professional.get("ProfessionalName"));
			obj.put('professionalCode',professional.get("professionalCode"));
		}
		return obj;
    }
//    def show(HttpServletRequest req){
//    	table().findOne(req[_id])
//    }
	
	

}
