import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.mail.*;
import java.util.Properties;

public class Incoming {

    //TODO Java exception in parsing from multipart to String. Fix up! Line 48

    private String user;
    private String pass;
    private String host;

    public Incoming(String user, String pass, String host){

        this.user = user;
        this.pass = pass;
        this.host = host;

    }

    public ObservableList getMessage() throws Exception {

        ObservableList<MailSubject> latestSubject = FXCollections.observableArrayList();
        // Создание свойств
        Properties props = new Properties();

        //включение debug-режима
        props.put("mail.debug", "false");

        //Указываем протокол - IMAP с SSL
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props);
        Store store = session.getStore();

        //подключаемся к почтовому серверу
        store.connect(this.host, this.user, this.pass);

        //получаем папку с входящими сообщениями
        Folder inbox = store.getFolder("INBOX");

        //открываем её только для чтения
        inbox.open(Folder.READ_ONLY);

        int messageCount = inbox.getMessageCount();

        for(int i = messageCount; i > messageCount - 10; i--){

            latestSubject.add(new MailSubject(inbox.getMessage(i).getSubject(), (((Multipart) inbox.getMessage(i).getContent()).getBodyPart(0).getContent().toString())));
        }
        return latestSubject;
    }
}
