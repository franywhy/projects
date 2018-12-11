package com.school.accountant.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {

	private MimeMessage mimeMsg; // MIME邮件对象
	private Session session; // 邮件会话对象
	private Properties props; // 系统属性
	private static boolean needAuth = true; // SMTP是否需要认证
	// smtp认证用户名和密码
	private String username;
	private String password;
	private MimeMultipart mp;
	
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	/**
	 * Constructor
	 * 
	 * @param smtp
	 *            邮件发送服务器
	 */
	public MailUtil(String smtp, Integer port) {
		setSmtpHost(smtp);
		setSmtpPort(port);
		createMimeMessage();
	}

	/**
	 * 设置邮件发送服务器
	 * 
	 * @param hostName
	 *            String
	 */
	public void setSmtpHost(String hostName) {
		logger.info("设置系统属性：mail.smtp.host = " + hostName);
		if (props == null)
			props = System.getProperties(); // 获得系统属性对象
		props.put("mail.smtp.host", hostName); // 设置SMTP主机
	}

	public void setSmtpPort(Integer port) {
		logger.info("设置系统属性：mail.smtp.port = " + port);
		if (port != null && port > 0) {
			if (props == null)
				props = System.getProperties(); // 获得系统属性对象
			props.put("mail.smtp.port", port); // 设置SMTP主机port
		}
	}

	/**
	 * 创建MIME邮件对象
	 * 
	 * @return
	 */
	public boolean createMimeMessage() {
		try {
			logger.info("准备获取邮件会话对象！");
			session = Session.getDefaultInstance(props, null); // 获得邮件会话对象
		} catch (Exception e) {
			logger.error("获取邮件会话对象时发生错误！" + e);
			return false;
		}
		logger.info("准备创建MIME邮件对象！");
		try {
			mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
			mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			logger.error("创建MIME邮件对象失败！" + e);
			return false;
		}
	}

	/**
	 * 设置SMTP是否需要验证
	 * 
	 * @param need
	 */
	public void setNeedAuth(boolean need) {
		logger.info("设置smtp身份认证：mail.smtp.auth = " + need);
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	/**
	 * 设置用户名和密码
	 * 
	 * @param name
	 * @param pass
	 */
	public void setNameAndPassword(String name, String pass) {
		username = name;
		password = pass;
	}

	/**
	 * 设置邮件主题
	 * 
	 * @param mailSubject
	 * @return
	 */
	public boolean setSubject(String mailSubject) {
		logger.info("设置邮件主题！");
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			logger.error("设置邮件主题发生错误！");
			return false;
		}
	}

	/**
	 * 设置邮件正文
	 * 
	 * @param mailBody
	 *            String
	 */
	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + mailBody, "text/html;charset=GBK");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			logger.error("设置邮件正文时发生错误！" + e);
			return false;
		}
	}

	/**
	 * 添加附件
	 * 
	 * @param filename
	 *            String
	 */
	public boolean addFileAffix(String filename) {
		logger.info("增加邮件附件：" + filename);
		if (filename != null && !"".equals(filename.trim())) {
			try {
				BodyPart bp = new MimeBodyPart();
				FileDataSource fileds = new FileDataSource(filename);
				bp.setDataHandler(new DataHandler(fileds));
				bp.setFileName(MimeUtility.encodeWord(fileds.getName()));
				mp.addBodyPart(bp);
				return true;
			} catch (Exception e) {
				logger.error("增加邮件附件：" + filename + "发生错误！" + e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置发信人邮件地址
	 * 
	 * @param from
	 *            String
	 */
	public boolean setFrom(String from) {
		logger.info("设置发信人邮件地址！" + from);
		try {
			mimeMsg.setFrom(new InternetAddress(from)); // 设置发信人
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 设置收信人邮件地址
	 * 
	 * @param to
	 *            String
	 */
	public boolean setTo(List<String> tos) {
		if (tos == null)
			return false;
		try {
			for (String to : tos) {
				mimeMsg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			}
			return true;
		} catch (Exception e) {
			logger.info("设置收信人邮件地址出错！");
			return false;
		}
	}

	/**
	 * 设置抄送人邮件地址
	 * 
	 * @param copyto
	 *            String
	 */
	public boolean setCopyTo(List<String> copytos) {
		if (copytos == null)
			return true;
		try {
			for (String copyto : copytos) {
				mimeMsg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto));
			}
			return true;
		} catch (Exception e) {
			logger.info("设置抄送人邮件地址出错！");
			return false;
		}
	}

	/**
	 * 执行发送邮件
	 */
	public boolean sendOut() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			logger.info("正在发送邮件....");
			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username, password);
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
			Address[] ccAddress = mimeMsg.getRecipients(Message.RecipientType.CC);
			if (ccAddress != null && ccAddress.length > 0) {
				transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
			}
			logger.info("发送邮件成功！");
			transport.close();
			return true;
		} catch (Exception e) {
			logger.error("邮件发送失败！" + e);
			return false;
		}
	}

	/**
	 * 发送邮件（不带附件，不带抄送）
	 * 
	 * @param smtp
	 * @param port
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean send(String smtp, Integer port, String from, List<String> to, String subject, String content,
			String username, String password) {

		MailUtil theMail = new MailUtil(smtp, port);
		theMail.setNeedAuth(needAuth); // 需要验证
		if (!theMail.setSubject(subject))
			return false;
		if (!theMail.setBody(content))
			return false;
		if (!theMail.setTo(to))
			return false;
		if (!theMail.setFrom(from))
			return false;
		theMail.setNameAndPassword(username, password);
		if (!theMail.sendOut()) {
			return false;
		}
		return true;
	}

	/**
	 * 发送邮件（不带附件，带抄送）
	 * 
	 * @param smtp
	 * @param port
	 * @param from
	 * @param to
	 * @param copyto
	 * @param subject
	 * @param content
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean sendAndCc(String smtp, Integer port, String from, List<String> to, List<String> copyto,
			String subject, String content, String username, String password) {

		MailUtil theMail = new MailUtil(smtp, port);
		theMail.setNeedAuth(needAuth); // 需要验证
		if (!theMail.setSubject(subject))
			return false;
		if (!theMail.setBody(content))
			return false;
		if (!theMail.setTo(to))
			return false;
		if (!theMail.setCopyTo(copyto))
			return false;
		if (!theMail.setFrom(from))
			return false;
		theMail.setNameAndPassword(username, password);
		if (!theMail.sendOut()) {
			return false;
		}
		return true;
	}

	/**
	 * 发送（带附件）
	 * 
	 * @param smtp
	 * @param port
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @param username
	 * @param password
	 * @param filename
	 * @return
	 */
	public static boolean send(String smtp, Integer port, String from, List<String> to, String subject, String content,
			String username, String password, String filename) {
		MailUtil theMail = new MailUtil(smtp, port);
		theMail.setNeedAuth(needAuth); // 需要验证
		if (!theMail.setSubject(subject))
			return false;
		if (!theMail.setBody(content))
			return false;
		if (!theMail.addFileAffix(filename))
			return false;
		if (!theMail.setTo(to))
			return false;
		if (!theMail.setFrom(from))
			return false;
		theMail.setNameAndPassword(username, password);
		if (!theMail.sendOut()) {
			return false;
		}
		return true;
	}

	/**
	 * 邮件发送（带附件和抄送）
	 * 
	 * @param smtp
	 *            邮件服务器地址。例如smtp.qq.com
	 * @param port
	 *            邮件服务器端口。例如smtp.qq.com对应的端口是25
	 * @param from
	 *            发件人邮箱
	 * @param to
	 *            收件人邮箱
	 * @param copyto
	 *            抄送人邮箱（没有则设置为null）
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件正文
	 * @param username
	 *            发件人名称
	 * @param password
	 *            发件邮箱密码
	 * @param filename
	 *            附件（例如：D:\test\test.jpg，没有则设置为null）
	 * @return
	 */
	public static boolean sendAndCc(String smtp, Integer port, String from, List<String> to, List<String> copyto,
			String subject, String content, String username, String password, String filename) {

		MailUtil theMail = new MailUtil(smtp, port);
		theMail.setNeedAuth(needAuth); // 需要验证
		if (!theMail.setSubject(subject))
			return false;
		if (!theMail.setBody(content))
			return false;
		if (!theMail.addFileAffix(filename))
			return false;
		if (!theMail.setTo(to))
			return false;
		if (!theMail.setCopyTo(copyto))
			return false;
		if (!theMail.setFrom(from))
			return false;
		theMail.setNameAndPassword(username, password);
		if (!theMail.sendOut()) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("2308040571@qq.com");
		MailUtil.send("smtp.qq.com", 25, "510476447@qq.com", list, "test", "test", "510476447", "haifei1025");
	}
}
