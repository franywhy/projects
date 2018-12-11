package com.izhubo.admin

import com.mongodb.DBCollection
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.admin.crud.MessageController
import com.izhubo.model.MsgType

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.doc.MongoKey.SJ_DESC
import static com.izhubo.rest.common.util.WebUtils.$$

/**
 * @author: wubinjie@ak.cc
 * Date: 13-11-19 下午2:08
 */
@RestWithSession
public class TopicController extends BaseController{

    @Resource
    MessageController messageController

    DBCollection topics(){topicMongo.getCollection("topics")}

    DBCollection comments(){topicMongo.getCollection("comments")}
    def list(HttpServletRequest req){
        Crud.list(req, topics(), null, ALL_FIELD, SJ_DESC)
    }

    def del(HttpServletRequest req){
        def id = req[_id];
        def topic = topics().findOne($$(_id, id))

        topics().remove($$(_id, id))
        //删除话题下所有评论
        comments().remove($$("tid",id))
        //通知用户
        //messageController.sendSingleMsg(topic.get("uid") as Integer, '话题关闭', '你所发表的话题 '+topic.get('title')+' 因为包含违规内容而被关闭。如有任何疑问欢迎联系客服QQ', MsgType.系统消息);
        [code:1]
    }
}
