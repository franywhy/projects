package test.mail

import org.junit.Test
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import test.BaseTest

import javax.annotation.Resource

/**
 * TODO Comment here.
 * date: 13-9-4 下午4:57
 * @author: wubinjie@ak.cc
 */
class TestMail extends BaseTest{


    @Resource
    MailSender mailSender



    @Test
    void mail(){
        def msg = new SimpleMailMessage()

        msg.setFrom("爱主播美女视频秀场<no-reply@ak.cc>")
        msg.setSubject("爱主播美女视频秀场(show.izhubo.com)注册邮箱确认")
        msg.setTo("wubinjie@ak.cc")
        msg.setText("""
您好！欢迎您加入爱主播美女视频秀场！

您注册的邮箱帐号为：420776449@qq.com，ID为1477865。

请点击下面的链接来确认您的邮箱帐号:

http://show.izhubo.com/users/confirmation?confirmation_token=dbTaZdzipQNaESntugYq

若以上链接无法点击，请将上面的地址复制到浏览器地址栏访问以进行确认。

该邮件由系统发送，请勿回复。

---------------------------------------

爱主播美女视频秀场 show.izhubo.com

${new Date().format("yyyy年MM月dd日 HH:mm")}

        """)

        mailSender.send(msg)
    }
}
