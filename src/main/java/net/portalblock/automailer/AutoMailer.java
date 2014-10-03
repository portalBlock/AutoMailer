/*
 * Copyright (c) 2014 portalBlock. This work is provided AS-IS without any warranty.
 * You must provide a link back to the original project and clearly point out any changes made to this project.
 * This license must be included in all project files.
 * Any changes merged with this project are property of the copyright holder but may include the author's name.
 */

package net.portalblock.automailer;

import it.sauronsoftware.cron4j.CronParser;
import it.sauronsoftware.cron4j.Scheduler;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by portalBlock on 10/2/2014.
 */
public class AutoMailer {
    private static Properties props = new Properties();
    private static String username, password, host;
    private static int port;
    private static Session session;
    public static void main(String[] args) throws Exception{
        JSONConfig config = new JSONConfig();
        config.load();
        username = config.getUsername();
        password = config.getPassword();
        host = config.getHost();
        port = config.getPort();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtps.ssl.checkserveridentity", "false");
        props.put("mail.smtps.ssl.trust", "*");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        AutoMailer mailer = new AutoMailer();
        Scheduler s = new Scheduler();
        for(Email email : config){
            s.schedule(email.getCron(), new MailTask(mailer, email));
        }
        s.start();
        while (true){
            Thread.sleep(TimeUnit.DAYS.toMillis(1));
        }
    }

    public void mail(Email email, String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(email.getFromAdr(), email.getFromNme()));
        msg.setRecipients(Message.RecipientType.TO, to);

        msg.setSubject(email.getSubject());
        msg.setSentDate(new Date());
        msg.setText(email.getBody());

        Transport trnsport;
        trnsport = session.getTransport("smtps");
        trnsport.connect(host, port, username, password);
        msg.saveChanges();
        trnsport.sendMessage(msg, msg.getAllRecipients());
        trnsport.close();
    }
}