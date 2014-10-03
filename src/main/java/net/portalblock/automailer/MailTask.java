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
