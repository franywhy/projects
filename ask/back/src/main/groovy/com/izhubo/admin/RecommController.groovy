package com.izhubo.admin
import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.rest.groovy.CrudClosures.*
import static org.apache.commons.io.output.NullOutputStream.NULL_OUTPUT_STREAM
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud

/**
 * 主播推荐
 * date: 14-528 下午2:31
 */
//@Rest
@RestWithSession
class RecommController extends BaseController{

    DBCollection table(){adminMongo.getCollection('config')}

    static final String index_recommend_list = 'index_star_recommend_list'
    static final Map index_recommend_list_fields=[stars:{String str->str.split(',').collect {Integer.valueOf(it.toString())}  }]
	
	@TypeChecked(TypeCheckingMode.SKIP)
    def save_index_star_recommend(HttpServletRequest req){
        def star = req['stars']
        def info  = new BasicDBObject(_id,index_recommend_list)
        if(StringUtils.isEmpty(star)){
            table().remove(info)
            Crud.opLog("sys_index_star_recommend_list",info)
            return OK();
        }

        index_recommend_list_fields.each{String field,Closure v->
            info.put(field,v.call(req.getParameter(field)))
        }
        if(table().save(info).getN() == 1){
            Crud.opLog("sys_index_star_recommend_list",info)
        }
        OK()
    }

    def add_index_star_recommend(HttpServletRequest req){
        Integer starId = req['starId'] as Integer
        def week = req['dayOfweek']
        def info  = new BasicDBObject(_id,index_recommend_list)
        if(table().update($$(_id,index_recommend_list),$$($addToSet , $$("week_"+week, starId)), true, false).getN() == 1){
            Crud.opLog("sys_index_star_recommend_list",info)
        }
        OK()
    }

    def del_index_star_recommend(HttpServletRequest req){
        Integer starId = req['starId'] as Integer
        def week = req['dayOfweek']
        def info  = new BasicDBObject(_id,index_recommend_list)
        if(table().update($$(_id,index_recommend_list),$$($pull , $$("week_"+week, starId))).getN() == 1){
            Crud.opLog("sys_index_star_recommend_list",info)
        }
        OK()
    }
    def show_index_star_recommend(HttpServletRequest req){
        def config = table().findOne(index_recommend_list)
        [code:1,data:config]
    }
}
