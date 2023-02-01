package com.filavents.books_highlights.utils;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Notification {

  private Notification() {

  }

  private static final Logger logger = LoggerFactory.getLogger(Notification.class);

  public static void main(String[] args) {

  }

  /**
   * Send email using Mailgun
   *
   * @param subject Email subject
   * @param messageBody Email message body
   * @param toEmail Email to send to
   */
  public static void sendEmail(String subject, String messageBody, String toEmail) {
    final String username = System.getenv("SMTP_USER");
    final String password = System.getenv("SMTP_PASSWORD");

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.mailgun.org");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("noreply@filavents.com"));
      message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(toEmail));
      message.setSubject(subject);
      message.setText(messageBody);

      Transport.send(message);

      logger.info("Email sent to: " + toEmail);
    } catch (MessagingException e) {
      logger.error("Error sending email to: " + toEmail + " " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
