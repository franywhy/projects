package com.izhubo.web.zone

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.izhubo.rest.AppProperties
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.IMessageCode
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.web.Crud
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.PhotoStatusType
import com.izhubo.web.BaseController
import com.izhubo.web.UpaiController
import com.izhubo.web.api.Web

import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.util.HtmlUtils

import groovy.transform.TypeChecked;
import groovy.transform.TypeCheckingMode;

import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$

/**
 * date: 13-7-29 下午5:57
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class PhotoController extends BaseController{


    public DBCollection union_photos() {
        return adminMongo.getCollection("union_photos");
    }

    static final Long SIX_HUNDRED_SECONDS = 600L

    static final String notify_url = (AppProperties.get('api.domain') + 'upai/notify').replace("/","\\/")

    static final Pattern URL_PATT = Pattern.compile("/\\d+/\\d{4}/\\w{32}.[a-z]{3,4}")
	
	@TypeChecked(TypeCheckingMode.SKIP)
    def token(){
        if(Web.isStar()){
            def uid = Web.getCurrentUserId();
            def json = "{\"bucket\":\"showphoto\"," +
                    "\"expiration\":${System.unixTime() + SIX_HUNDRED_SECONDS}," +
                    "\"save-key\":\"/${uid}/{mon}{day}/{filemd5}{.suffix}\"," +
                    "\"allow-file-type\":\"jpg,jpeg,gif,png\"," +
                    "\"image-width-range\":\"120,2048\","+
                    "\"image-height-range\":\"120,8192\","+
                    "\"content-length-range\":\"0,3145728\"," + // 0 ~ 3MB
//                    "\"x-gmkerl-type\":\"\","+
                    "\"return-url\":\"\"," +
                    "\"notify-url\":\"${notify_url}\"}"
            def policy = Base64.encodeBase64String(json.asBytes())
            return [code:1,data:[
                    action:'http://v0.api.upyun.com/showphoto/',policy:policy,
                    signature:MsgDigestUtil.MD5.digest2HEX("${policy}&${UpaiController.HTTP_FORM_KEY}")
            ]]
        }else{
            Web.notAllowed()
        }
    }

    /**
     * 用户举报
     * @return
     */
	@TypeChecked(TypeCheckingMode.SKIP)
    def accuse_token(){
        def uid = Web.getCurrentUserId();
        def json = "{\"bucket\":\"showaccuse\"," +
                "\"expiration\":${System.unixTime() + SIX_HUNDRED_SECONDS}," +
                "\"save-key\":\"/${uid}/{mon}{day}/{filemd5}{.suffix}\"," +
                "\"allow-file-type\":\"jpg,jpeg,gif,png\"," +
                "\"image-width-range\":\"120,2048\","+
                "\"image-height-range\":\"120,8192\","+
                "\"content-length-range\":\"0,3145728\"," + // 0 ~ 3MB
//                    "\"x-gmkerl-type\":\"\","+
                "\"return-url\":\"\"," +
                "\"notify-url\":\"${notify_url}\"}"
        println "accuse_token json: "+json
        def policy = Base64.encodeBase64String(json.asBytes())
        return [code:1,data:[
                action:'http://v0.api.upyun.com/showaccuse/',policy:policy,
                signature:MsgDigestUtil.MD5.digest2HEX("${policy}&${UpaiController.ACCUSE_HTTP_FORM_KEY}")
        ]]
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def add(HttpServletRequest req){
        if(Web.isStar()){
            def path = req['path']
            def type = Web.firstNumber(req)
            def title = req['title'].clean()
            if(URL_PATT.matcher(path.clean()).matches()
                    && path.startsWith("/"+Web.currentUserId()+"/")
                    && title.length() > 1
                    && title.length() < 100
                    && type >= 0
                    && type <= 9
            ){
                Integer starId = Web.getCurrentUserId()
                if(photos().save(new BasicDBObject(
                        _id:path,
                        user_id:starId,
                        timestamp:System.currentTimeMillis(),
                        type:type,
                        title: HtmlUtils.htmlEscape(title)
                )).getN() == 1){
                    UpaiController.send_audit(path)
                     mainRedis.delete(KeyUtils.ROOM.photoCount(starId))
                }
                return IMessageCode.OK
            }
        }
        Web.notAllowed()
    }


    def del(HttpServletRequest req){
        if(Web.isStar()){
            def path = req['path']
            def starId = Web.getCurrentUserId()
            def obj = photos().findAndRemove($$(_id,path).append("user_id",starId))
            if(obj != null){
                obj.put("del_time",System.currentTimeMillis())
                obj.put("s",UpaiController.USER_DELETE)
                mainMongo.getCollection("ban_photos").save(obj)
                mainRedis.delete(KeyUtils.ROOM.photoCount(starId))
            }
            return [code:  (null == obj  ? 0 : 1)]
        }
        Web.notAllowed()
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def set_title(HttpServletRequest req){
        if(Web.isStar()){
            def path = req['path']
            def title = req['title'].clean()
            if(URL_PATT.matcher(path.clean()).matches() && title.length() > 1 && title.length() < 100){
                return [code:photos().update($$(_id,path).append("user_id",Web.getCurrentUserId())
                        ,$$($set,$$('title',HtmlUtils.htmlEscape(title)))).getN()]
            }
        }
        Web.notAllowed()
    }


	@TypeChecked(TypeCheckingMode.SKIP)
    def praise(HttpServletRequest req){
        def path = req['path']
        if(URL_PATT.matcher(path.clean()).matches()){
            def photos = photos()
            def user_id =  Web.getCurrentUserId()
            def time = System.currentTimeMillis()
            def praise_id = user_id +"_" + path
            def setOnInsert = new HashMap()
            setOnInsert.put("user_id",user_id)
            setOnInsert.put(timestamp,time);
            def photoId = $$(_id,path)
            if(photos.count(photoId) == 1 &&
               logMongo.getCollection("praise").findAndModify($$(_id,praise_id),null,
                   null,false,$$($setOnInsert,setOnInsert),true,true).get(timestamp).equals(time)){
                return [code:photos.update(photoId,$$($inc,$$("praise",1))).getN()]
            }
        }
        [code:0]
    }

    @Value("#{application['pic.domain']}")
    String pic_domain = "http://img.show.izhubo.com/"
    /**
     * 主播上传合作推广照片
     * @param req
     * @return
     */
	@TypeChecked(TypeCheckingMode.SKIP)
    def add_union(HttpServletRequest req){
        if(Web.isStar()){
            def path = req['path']
            def pic_url = req['pic_url']
            if(StringUtils.isEmpty(pic_url) || StringUtils.isEmpty(path)){
                return IMessageCode.CODE0
            }
            if(union_photos().count($$('user_id', Web.getCurrentUserId())) <= 5){
                if(union_photos().save(new BasicDBObject(
                        _id:path,
                        user_id:Web.getCurrentUserId() as Integer,
                        pic_url:pic_url,
                        timestamp:System.currentTimeMillis(),
                        status: PhotoStatusType.未处理.ordinal()
                )).getN() > 0)
                return IMessageCode.OK

            }
       }
        Web.notAllowed()
    }

    /**
     * 主播合作推广照片列表
     * @param req
     * @return
     */
    def list_union(HttpServletRequest req){
       if(Web.isStar()){
            def photos = null
//            def user = users().findOne($$(_id , Web.currentUserId() as Integer), $$('union_pic.stars',1)).toMap();
//            if(user != null){
//                photos =  (user.get("union_pic") as Map )?.get("stars")
//               println p
//            }

            return Crud.list(req, union_photos(),$$("user_id" , Web.currentUserId() as Integer), ALL_FIELD, SJ_DESC)
            //return [code:  1, data : photos]
       }
        Web.notAllowed()
    }

    //图片处理
    File pic_folder

    @Value("#{application['pic.folder']}")
    void setPicFolder(String folder){
        pic_folder = new File(folder)
        pic_folder.mkdirs()
        println "初始化图片上传目录 : ${folder}"
    }
    /**
     * 主播删除合作推广照片
     * @param req
     * @return
     */
    def del_union(HttpServletRequest req){
       if(Web.isStar()){
           def path = req['path'] as String
//            users().update($$([_id : Web.currentUserId() as Integer]),
//                    $$($pull, $$('union_pic.stars', $$(["path": path,"status": 0])))
//                ,false,false,writeConcern)
           def obj = union_photos().findAndRemove($$(_id,path).append("user_id",Web.getCurrentUserId()))
           if(obj != null){
//               obj.put("del_time",System.currentTimeMillis())
//               obj.put("s",0)
//               mainMongo.getCollection("ban_photos").save(obj)
               File file = new File(pic_folder,path)
               file.delete();
           }
           return [code:  1]
       }
       Web.notAllowed()
    }
}
