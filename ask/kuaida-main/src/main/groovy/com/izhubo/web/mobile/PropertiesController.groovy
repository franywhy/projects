package com.izhubo.web.mobile

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.doc.IMessageCode
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.web.Crud
import com.izhubo.common.doc.Param
import com.izhubo.common.util.KeyUtils
import com.izhubo.web.BaseController

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.izhubo.web.api.Web.roomId

/**
 * date: 13-2-28 下午4:04
 */
@Rest
class PropertiesController extends BaseController {

    Logger logger = LoggerFactory.getLogger(PropertiesController.class)

    def list(){
        String props_key = KeyUtils.all_props()
        String json =  mainRedis.opsForValue().get(props_key)
        if(StringUtils.isBlank(json))
        {
            def lst =adminMongo.getCollection("properties").find(null,$$(timestamp:0)).toArray()
            def result = [code: 1, data:lst]
            json = JSONUtil.beanToJson(result)
            mainRedis.opsForValue().set(props_key,json)
            return  result
        }
        return  JSONUtil.jsonToMap(json)
    }

}
