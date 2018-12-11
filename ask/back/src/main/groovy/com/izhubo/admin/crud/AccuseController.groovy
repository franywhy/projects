package com.izhubo.admin.crud

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController
import com.izhubo.model.AccuseStatus
import com.izhubo.model.MsgType
import com.izhubo.model.OpType

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.groovy.CrudClosures.*
import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$

/**
 * date: 14-5-16 下午5:20
 */
//@Rest
@RestWithSession
class AccuseController extends BaseController{

    @Resource
    MessageController messageController

    @Delegate Crud crud = new Crud(adminMongo.getCollection('accuse'),false,
            [_id:Str,status:Int],
            new Crud.QueryCondition(){
                public DBObject query(HttpServletRequest req) {
                    if(req['status'])
                        return new BasicDBObject('status',req['status'])
                }
                public DBObject sortby(HttpServletRequest req) {ID_DESC}
            }
    )

    private static final String[] TYPES = ["违禁品、传销","色情低俗","政治违规","外站拉人"]

    public Map edit(HttpServletRequest req) {
        Long _id =  req['_id'] as Long
        Integer status =  req['status'] as Integer
        def accuse = adminMongo.getCollection('accuse').findAndModify($$(_id:_id, status: AccuseStatus.未处理.ordinal()),
                $$($set,$$('status':status)));
        if(accuse != null){
            Integer uid =  accuse.get('uid') as Integer
            Integer roomId =  accuse.get('roomId') as Integer
            Integer type =  accuse.get('type') as Integer
            def nick_name = users().findOne(roomId,$$(nick_name:1))?.get('nick_name')
            Integer coin = 0;
            String msg = "尊敬的用户,你于(${new Date().format("yyyy-MM-dd HH:mm:ss")})在(${nick_name})的直播间举报了(${TYPES[type]}),"
            //奖励用户200星币
            if(status == AccuseStatus.通过.ordinal()){
                coin = 200
                msg += "经核实举报信息真实有效且为首位举报的用户，获得200星币的奖励，请再接再励。"
            }
            //返还用户星币
            else if(status == AccuseStatus.重复.ordinal()){
                coin = 100
                msg += "经核实验证举报信息真实有效但非首位举报的用户，退还100星币举报费用，请再接再厉。"
            }else if(status == AccuseStatus.未通过.ordinal()){
                msg += "经核实举报信息无法真实验证，请参考《爱主播美女视频秀场举报系统规则》进行违规行为的举报。"
            }
            def curr = System.currentTimeMillis()
            if(users().update($$(_id:uid), $$($inc:$$('finance.coin_count':coin))).getN() == 1){
                logMongo.getCollection("accuse_logs").insert($$(_id: uid + "_" + curr,
                        aid:_id,
                        uid:uid,
                        type:'award',
                        coin:coin,
                        timestamp:curr
                ))

                Crud.opLog(OpType.accuse_audit,[uid:uid,status:status,coin:coin])
                //发送消息
                //messageController.sendSingleMsg(uid, '举报结果反馈', msg, MsgType.系统消息);
                return [ code : 1]
            }
        }

        return [ code : 0]


    }
}
