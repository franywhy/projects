package com.izhubo.web

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

import com.izhubo.rest.AppProperties
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.MsgDigestUtil

/**
 *
 * mail xiangguan.
 *
 *
 * date: 13-9-4 下午6:03
 * @author: wubinjie@ak.cc
 */
@Rest
class EmailController extends BaseController{
	@Resource
	MailSender mailSender

	@TypeChecked(TypeCheckingMode.SKIP)
	def verify(HttpServletRequest req,HttpServletResponse res){
		def id = req[_id]
		def time = req[timestamp]
		def code =req['code']
		if(MsgDigestUtil.MD5.digest2HEX(id +_KEY+ time).equals(code)){
			users().update($$(_id,new Integer(id)).append("mail_verfiy",null),
					$$($set,$$("mail_verfiy",Boolean.TRUE)),false,false,writeConcern)
			res.sendRedirect("http://${INDEX}app100699141.twsapp.com?verify=true")
		}else{
			res.setContentType("text/html; charset=UTF-8")
			def out = res.getWriter()
			out.println("验证无效")
			out.close()
		}
	}

	static final String API_DOMAIN =  AppProperties.get('api.domain')
	static final String INDEX  =  API_DOMAIN.contains("test")? "ttest." :"ttapi."
	static final String _KEY = "izhubo@#verify^&%jsk->YY"
	void send(String userMail,Object userId){
		def msg = new SimpleMailMessage()
		def time = System.currentTimeMillis().toString()
		msg.setFrom("爱主播美女视频秀场<no-reply@ak.cc>")
		msg.setSubject("爱主播美女视频秀场(www.izhubo.com)注册邮箱确认")
		msg.setTo(userMail)
		msg.setText("""
您好！欢迎您加入爱主播美女视频秀场！

您注册的邮箱帐号为：${userMail}，ID为${userId}。

请点击下面的链接来确认您的邮箱帐号:

${API_DOMAIN}email/verify?_id=${userId}&timestamp=${time}&code=${MsgDigestUtil.MD5.digest2HEX(userId.toString() +_KEY+ time)}

若以上链接无法点击，请将上面的地址复制到浏览器地址栏访问以进行确认。

该邮件由系统发送，请勿回复。

---------------------------------------

爱主播美女视频秀场 www.izhubo.com

${new Date().format("yyyy年MM月dd日 HH:mm")}

""")

		try{
			mailSender.send(msg)
		}catch (MailException e){
			e.printStackTrace()
		}
	}
}
