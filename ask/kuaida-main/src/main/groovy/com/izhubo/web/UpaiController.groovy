package com.izhubo.web

import static com.izhubo.rest.common.doc.MongoKey._id
import static com.izhubo.rest.common.doc.MongoKey.timestamp
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.mongodb.BasicDBObject
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.common.util.http.HttpClientUtil
import com.izhubo.common.util.StaticNewSpring

/**
 * upai notify
 * date: 13-7-29 下午4:03
 * @author: wubinjie@ak.cc
 */
@Rest
class UpaiController extends BaseController{

    static final String HTTP_FORM_KEY = "bl37fSAQyZ0ZMcF/cZMGjwWNuQU="
    static final String UPAI_NOTIFY_COLLECTION = "upai_notify"

    static final String ACCUSE_HTTP_FORM_KEY = "aBwvEke/borpWEFTUUHtO11FKTQ="

    static final Logger logger = LoggerFactory.getLogger(UpaiController.class)


    static final String photo_domain = "http://showphoto.b0.upaiyun.com";

    // use xylog
    //db.createCollection("upai_notify", {capped:true, size:1073741824})

//    static {
//
//        (DB) StaticSpring.get("logMongo");
//        if( ! adminDb.collectionExists(LOG_COLL_NAME) ){
//            // 1 GB
//            adminDb.createCollection(LOG_COLL_NAME,new BasicDBObject("capped",true).append("size",1<<30));
//        }
//    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def notify(HttpServletRequest req){
//        def code = req['code'] // 200
        def message = req['message']
        def url = req['url']
        def time = req['time']
//        def image_width = req['image-width']
//        def image_height = req['image­height']
//        def image_type = req['image­type']
//        def image_frames = req['image-frames']
//        logger.debug("map:${req.getParameterMap()}" )
        if(MsgDigestUtil.MD5.digest2HEX("200&${message}&${url}&${time}&${HTTP_FORM_KEY}").equals(req['sign'])){
            logger.debug("sign PASS")
            def notifys = logMongo.getCollection(UPAI_NOTIFY_COLLECTION)
            def obj = new BasicDBObject(_id,url)
            if(notifys.count(obj) == 0){
                obj.put(timestamp,Long.valueOf(time)*1000)
                obj.put("image_width",Integer.valueOf(req.getParameter("image-width")))
                obj.put("image_height",Integer.valueOf(req.getParameter("image-height")))
                obj.put("image_type",req.getParameter("image-type"))
                obj.put("image_frames",req.getParameter("image-frames"))
//                logger.debug("save obj: {}",obj)
                notifys.save(obj)
                return [code:1]
            }
        }
        [code:0]
    }



    static void send_audit(String path){
        def url = photo_domain + path
        def sign = MsgDigestUtil.MD5.digest2HEX(url+audit_key)
        final String shenHeURL = "http://review.izhubo.com/webmsg/q/10900/s?sign=${sign}&url=${url}"
        StaticNewSpring.execute(new Runnable() {
            void run() {
                HttpClientUtil.get(shenHeURL,null,HttpClientUtil.UTF8)
            }
        })
    }

    private static final String audit_key = "0ZMcF/cZM&*^tTp"
    static final Integer USER_DELETE = Integer.valueOf(0)
    private static final Integer NOT_ALLOWD = Integer.valueOf(1)
	
	@TypeChecked(TypeCheckingMode.SKIP)
    def audit(HttpServletRequest req){
        def json = req['json']
        def i = 0
        if(MsgDigestUtil.MD5.digest2HEX("${json}${audit_key}").equals(req['sign'])){
            def list = JSONUtil.jsonToBean(json,ArrayList.class)
            def photos = photos()
            def ban_photos = mainMongo.getCollection("ban_photos")
            for(Object obj : list){
                def map = (Map)obj
                if(NOT_ALLOWD.equals( ((Number)map.get("code")).intValue() )){
                    String url = (String)map.get("id")
                    String path = url.replace(photo_domain,"")
                    def photo =  photos.findAndRemove(new BasicDBObject(_id,path))
                    if( photo != null ){
                        i++
                        photo.put("s",NOT_ALLOWD)
                        photo.put("ban_time",System.currentTimeMillis())
                        photo.put("audit_message",map.get("message"))
                        ban_photos.save(photo)
                    }
                }
            }
        }

        [code:i]
    }

    /**
     * import com.izhubo.rest.common.util.JSONUtil
     import com.izhubo.rest.common.util.MsgDigestUtil
     import com.izhubo.web.UpaiController
     import org.apache.commons.codec.binary.Base64

     def sds="eyJidWNrZXQiOiJzaG93cGhvdG8iLCJleHBpcmF0aW9uIjoxMzc1MjM4NjEyLCJzYXZlLWtleSI6Ii8xMDI0MDM5L3ttb259e2RheX0ve2ZpbGVtZDV9ey5zdWZmaXh9IiwiaW1hZ2Utd2lkdGgtcmFuZ2UiOiIwLDQwOTYiLCJpbWFnZS1oZWlnaHQtcmFuZ2UiOiIwLDQwOTYiLCJhbGxvdy1maWxlLXR5cGUiOiJqcGcsanBlZyxnaWYscG5nIiwiY29udGVudC1sZW5ndGgtcmFuZ2UiOiIwLDQwOTYwMDAiLCJyZXR1cm4tdXJsIjoiIiwibm90aWZ5LXVybCI6Imh0dHA6XC9cL3Rlc3QuYXBpLmRvbmd0aW5nLmNvbVwvdXBhaVwvbm90aWZ5In0"

     def de = new String(Base64.decodeBase64(sds))
     println de
     println JSONUtil.jsonToMap(de)
     println MsgDigestUtil.MD5.digest2HEX("200&ok&/1024039/0731/3d0df14286bd382e6e5e39c4f40016be.png&1375238286&${UpaiController.HTTP_FORM_KEY}")
     */
}
