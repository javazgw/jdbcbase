/**
 *  *2010-4-23 下午04:43:25  add 
 */
package com.base.myproject.server.mail;

import java.security.Security;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Administrator
 *2010-4-23
 * javazgw@gmail.com
 */
public class SendMail {

	String filename = "";

	// 用于保存发送附件的文件名的集合
	Vector file = new Vector();
	//private static final String SMTP_HOST_NAME = "smtp.163.com";
	
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	/**
	 * 空参数的构造函数，不管需不需要，这是quertz的需要
	 */
	public SendMail() {
		System.out.println("SendMail init");
	}
	public static void main(String[] argv) throws MessagingException {
		
		String emailMsgTxt = "<a>Test Message Contents</a><hr>";
		 String emailSubjectTxt = "A test from gmail";
		 String emailFromAddress = "httechsoft@163.com";
		 String[] sendTo = { "javazgw@gmail.com" };
		 String pass = "ht219223499";
		 String SMTP_PORT = "25";
		 String SMTP_HOST_NAME = "smtp.163.com";
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
 
		new SendMail().sendSSLMessage(sendTo, emailSubjectTxt, emailMsgTxt,SMTP_HOST_NAME,SMTP_PORT,
				emailFromAddress,pass);
		System.out.println("Sucessfully Sent mail to All Users");
	}

	public void sendSSLMessage(String recipients[], String subject,
			String message,String smtphostname,String port,final String from,final String pass) throws MessagingException {
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", smtphostname);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "false");
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.socketFactory.port", port);
		//props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		//props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								from, pass);
					}
				});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		// msg.setContent(message, "text/plain");

		// --
		Multipart mp = new MimeMultipart();
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setContent(message, "text/html;charset=gb2312");
		mp.addBodyPart(mbp);
//		file.add("D:/zgw/1.xls");
//		file.add("D:/幸运的开始.pps");
		if (!file.isEmpty()) {// 有附件
			Enumeration efile = file.elements();
			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			while (efile.hasMoreElements()) {
				mbp = new MimeBodyPart();
				filename = efile.nextElement().toString(); // 选择出每一个附件名
				FileDataSource fds = new FileDataSource(filename); // 得到数据源
				mbp.setDataHandler(new DataHandler(fds)); // 得到附件本身并至入BodyPart
				//mbp.setFileName(fds.getName()); // 得到文件名同样至入BodyPart
				//中文
				mbp.setFileName("=?GBK?B?"+enc.encode(fds.getName().getBytes())+"?=");
				
				mp.addBodyPart(mbp);
			} 
			file.removeAllElements();
		}
		msg.setContent(mp);
		// --

		Transport.send(msg);
	}
}

