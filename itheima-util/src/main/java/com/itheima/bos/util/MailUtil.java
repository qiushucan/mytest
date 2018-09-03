package com.itheima.bos.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtil {

	public static void sendMail(String recMail,String title,String content) {

		try {
			// 1.连接邮箱发送服务器 主流邮箱服务
			/**
			 * QQ： smtp.qq.com 新浪： smtp.sina.com 163： smtp.163.com 126: smtp.126.com
			 */
			/**
			 * 参数一：服务器参数 参数二: 账户和密码
			 */
			Properties props = new Properties();
			// 服务端地址
			props.setProperty("mail.smtp.host", "smtp.sina.com");
			// 设置验证登录
			props.setProperty("mail.smtp.auth", "true");

			Session session = Session.getInstance(props, new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("ericxu_12345@sina.com", "eric12345");
				}

			});

			// 开启debug，可以看到发送的过程
			session.setDebug(true);

			// 2.创建一封邮件
			MimeMessage mail = new MimeMessage(session);
			// 2.1 设置发件人（必须和登录账户一致的）
			mail.setFrom(new InternetAddress("ericxu_12345@sina.com"));
			// 2.2 收件人
			/**
			 * TO: 收件人 CC：抄送人 BCC：密送人
			 */
			mail.setRecipient(RecipientType.TO, new InternetAddress(recMail));
			// 2.3 标题
			mail.setSubject(title);
			// 2.4 正文
			mail.setContent(content, "text/html;charset=utf-8");

			// 3.发送邮件
			Transport.send(mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
