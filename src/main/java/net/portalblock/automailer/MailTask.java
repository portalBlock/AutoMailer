/*
 * Copyright (c) 2014 portalBlock. This work is provided AS-IS without any warranty.
 * You must provide a link back to the original project and clearly point out any changes made to this project.
 * This license must be included in all project files.
 * Any changes merged with this project are property of the copyright holder but may include the author's name.
 */

package net.portalblock.automailer;

/**
 * Created by portalBlock on 10/3/2014.
 */
public class MailTask implements Runnable {

    private AutoMailer mailer;
    private Email email;

    public MailTask(AutoMailer mailer, Email email) {
        this.mailer = mailer;
        this.email = email;
    }

    @Override
    public void run() {
        for(String to : email.getTo()){
            try{
                mailer.mail(email, to);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
