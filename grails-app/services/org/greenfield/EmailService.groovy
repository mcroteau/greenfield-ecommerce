package org.greenfield
 
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;

class EmailService {
	
	static scope = "singleton"
	def grailsApplication
	def applicationService


	def send(toAddress, fromAddress, subject, emailBody){
	
		final String username = applicationService.getMailUsername();
		final String password = applicationService.getMailPassword()
 
		Properties props = new Properties();
		def auth        = applicationService.getMailAuth()
		def starttls    = applicationService.getMailStartTlsEnabled()
		def host        = applicationService.getMailHost()
		def port        = applicationService.getMailPort()
		
		
		props.put("mail.smtp.auth",            auth);
		props.put("mail.smtp.starttls.enable", starttls);
		props.put("mail.smtp.host",            host);
		props.put("mail.smtp.port",            port);
 
 	   
		println "to : ${toAddress} -> from : ${fromAddress} -> auth : ${auth} -> starttls : ${starttls} -> host : ${host} -> port : ${port}"
		
		Session session = Session.getInstance(props,
		  	new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
		 	});
			
		//session.setDebug(true);
			
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
			message.setSubject(subject);
			message.setText(emailBody, "utf-8", "html");
        	
			Transport.send(message);
        	
        	
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}	
	
	}
}
