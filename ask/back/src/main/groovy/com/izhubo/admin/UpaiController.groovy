package com.izhubo.admin

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.mongodb.BasicDBObject
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.MsgDigestUtil

/**
 * @author: wubinjie@ak.cc
 * Date: 13-12-27 上午9:46
 */
@Rest
class UpaiController extends BaseController{

    static final String HTTP_FORM_KEY = "758HbeMpbaTwXymQW4QO1fnKSgI="

    static final String UPAI_NOTIFY_COLLECTION = "upai_notify_log"

    static final  Logger logger = LoggerFactory.getLogger(UpaiController.class)

    static final String DOMAIN = "http://showclock.b0.upaiyun.com";

	@TypeChecked(TypeCheckingMode.SKIP)
    def notify(HttpServletRequest req){
//        def code = req['code'] // 200
        def message = req['message']
        def path = req['url']
        def time = req['time']
        def url = DOMAIN + path
        println "map:${req.getParameterMap()}"
        def sign = MsgDigestUtil.MD5.digest2HEX("200&${message}&${path}&${time}&${HTTP_FORM_KEY}")
        if(sign.equals(req['sign'])){
            def notifys = adminMongo.getCollection(UPAI_NOTIFY_COLLECTION)
            def obj = new BasicDBObject(_id,path)
            if(notifys.count(obj) == 0){
                obj.put(timestamp,Long.valueOf(time)*1000)
                obj.put("url",url)
//                obj.put("image_width",Integer.valueOf(req.getParameter("image-width")))
//                obj.put("image_height",Integer.valueOf(req.getParameter("image-height")))
//                obj.put("image_type",req.getParameter("image-type"))
//                obj.put("image_frames",req.getParameter("image-frames"))
//                logger.debug("save obj: {}",obj)
                def count = notifys.save(obj).getN()
                return [code:1]
            }
        }
        [code:0]
    }
}