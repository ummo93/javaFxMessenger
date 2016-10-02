public class MailSubject {
    private String subject;
    private String content;

    public MailSubject(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
    public String[] getMessages(){
        String[] message = new String[2];
        message[0] = this.subject;
        message[1] = this.content;
        return message;
    }
}
