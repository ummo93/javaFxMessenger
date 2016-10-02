/**
 * Created by shiyu on 29.09.2016.
 */
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Sender {

    private String username;
    private String password;
    private String smtpHost;
    private String smtpPort;
    private Properties props;

    public Sender(String username, String password, String smtpHost, String smtpPort) {
        this.username = username;
        this.password = password;
        this.smtpHost = smtpHost; //For example smtp.gmail.com
        this.smtpPort = smtpPort; //For example 465

        props = new Properties();
        props.put("mail.smtp.host", this.smtpHost);
        props.put("mail.smtp.socketFactory.port", this.smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", this.smtpPort);
    }

    public boolean send(String subject, String text, String fromEmail, String toEmail){
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(subject);
            //текст
            message.setText(text);

            message.setHeader("Content-type", "text/html");

            //отправляем сообщение
            Transport.send(message);

            //Освобождаем память
            message = null;
            session = null;
            this.username = null;
            this.password = null;
            this.smtpHost = null;
            this.smtpPort = null;
            this.props = null;

        } catch (MessagingException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

}
