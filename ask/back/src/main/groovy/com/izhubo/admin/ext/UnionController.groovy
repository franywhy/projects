package com.izhubo.admin.ext
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.anno.Rest
import com.izhubo.admin.BaseController
import com.izhubo.admin.QdController
import com.izhubo.admin.Web

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
/**
 * date: 13-4-22 10:40
 * @author: wubinjie@ak.cc
 */
@Rest
class UnionController extends BaseController{

    @Resource
    QdController qdController

    DBCollection table(){adminMongo.getCollection('stat_channels')}

    def login(HttpServletRequest request){
        String input = request[auth_code]
        if (codeVerifError(request,input)){
            return [code: 30419,msg:'验证码错误']
        }

        String name = request["name"]
        String password = MsgDigestUtil.SHA.digest2HEX(request["password"].toString())
        def user = adminMongo.getCollection("channel_users").findOne(
                new BasicDBObject(_id:name,password:password),new BasicDBObject('password',0))
        if (null == user){
            return [code: 0,msg:'密码错误']
        }

        if ( ! user['qd']){
            return [code: 0,msg:'权限不足']
        }

        def qd = adminMongo.getCollection("channels").findOne(user['qd'],new BasicDBObject('comment',0))
        if( ! qd){
            return [code: 0,msg:'权限不足']
        }
        user.put("qdinfo",qd)
        request.getSession().setAttribute("union_user",user)
        return [code: 1,data: user]
    }

    def modif_pwd(HttpServletRequest req){
        Map user = req.getSession().getAttribute("union_user") as Map
        if (null == user){
            return [code: 0]
        }
        String pwd = MsgDigestUtil.SHA.digest2HEX(req['password'].toString())
        adminMongo.getCollection('channel_users')
                .update(new BasicDBObject(_id,user.get(_id)),new BasicDBObject('$set',[password:pwd]))
        [code: 1]
    }

    def show(HttpServletRequest req){
        def user = req.getSession().getAttribute("union_user")
        [code: user?1:0,data:user]
    }


    private doInQd(HttpServletRequest req ,Closure closure){
        def user = req.getSession().getAttribute('union_user')
        if(null == user){
            return [code:0]
        }
        closure.call(user['qd'])
    }


    def reg_pay_list(HttpServletRequest req){
        doInQd(req){String qid->
            def query  = Web.fillTimeBetween(req).and('qd').is(qid).get()
            qdController.reg_pay_list_service(query,req)
        }
    }



    def reg_list(HttpServletRequest req){
        doInQd(req){String qid->
            qdController.reg_list_service(qid,req)
        }
    }


    def pay_list(HttpServletRequest req){
        doInQd(req){String qid->
            qdController.pay_list_service(qid,req)
        }
    }


    def pay_rate(HttpServletRequest req){
        doInQd(req){String qid->

            qdController.pay_rate_service(Web.fillTimeBetween(req).and('qd').is(qid).get(),new BasicDBObject('timestamp',-1),req)
        }
    }

    def login_rate(HttpServletRequest req){
        doInQd(req){String qid->
            qdController.login_rate_service(Web.fillTimeBetween(req).and('qd').is(qid).get(),new BasicDBObject('timestamp',-1),req)
        }
    }

    def login_total(HttpServletRequest req){
        doInQd(req){String qid->
            qdController.login_total_service(qid,req)
        }
    }

}
