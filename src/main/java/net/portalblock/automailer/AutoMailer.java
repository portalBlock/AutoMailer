package net.portalblock.automailer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

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
        for(Email email : config){
            for(String to : email.getTo()){
                mailer.mail(email, to);
            }
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