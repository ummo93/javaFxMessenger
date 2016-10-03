import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

public class Controller {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MenuItem close;
    @FXML
    private MenuItem save;
    @FXML
    private HTMLEditor htmlarea;
    @FXML
    private TextArea helpview;
    @FXML
    private MenuItem helpReturn;
    @FXML
    private TextField emailInput;
    @FXML
    private TextField subjectInput;
    @FXML
    private Label userDefine;
    @FXML
    private TextField smtpHost;
    @FXML
    private TextField smtpPort;
    @FXML
    private SplitPane splitIncoming;
    @FXML
    private ListView incomingTree;
    @FXML
    private WebView mailView;

    private ObservableList<MailSubject> messageList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        userDefine.setText(Main.username);
        smtpHost.setText(Main.smtpHost);
        smtpPort.setText(Main.smtpPort);
    }

    @FXML
    public void onClickSave() {

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog((Stage) anchorPane.getScene().getWindow());

        if (file != null) {
            SaveFile(htmlarea.getHtmlText().replace("contenteditable=\"true\"", "contenteditable=\"false\""), file);
        }

    }

    @FXML
    public void onClickClose() {
        ((Stage) anchorPane.getScene().getWindow()).close();
    }

    @FXML
    public void onClickSend() {

        if ((subjectInput.getText().length() > 0) && (emailInput.getText().length() > 0)) {

            Main.smtpHost = smtpHost.getText();
            Main.smtpPort = smtpPort.getText();
            Main.wipeConfig();
            Main.updateConfig();

            Sender sender = new Sender(Main.username, Main.pass, smtpHost.getText(), smtpPort.getText());

            if (sender.send(subjectInput.getText(),
                    htmlarea.getHtmlText().replace("contenteditable=\"true\"", "contenteditable=\"false\""),
                    "me",
                    emailInput.getText())) {
                Windows.success("Success!", "All right!", "Mail has been send", anchorPane.getScene().getWindow());


            } else {
                Windows.alert(
                        "Error",
                        "Validation error",
                        "Check the fields in the form on the right panel. If all right, possible google need to check this captcha https://accounts.google.com/DisplayUnlockCaptcha.",
                        anchorPane.getScene().getWindow()
                );
            }
            sender = null;

        } else {

            Windows.alert(
                    "Error",
                    "Validation error",
                    "Check the fields in the form on the right panel. If all right, possible google need to check this captcha https://accounts.google.com/DisplayUnlockCaptcha.",
                    anchorPane.getScene().getWindow()
            );

        }

    }

    @FXML
    public void onHelpClick() {
        helpview.setVisible(true);
        helpReturn.setVisible(true);
        emailInput.setVisible(false);
    }

    @FXML
    public void onReturnClick() {
        helpview.setVisible(false);
        helpReturn.setVisible(false);
        emailInput.setVisible(true);
    }

    private void SaveFile(String content, File file) {
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    public void onChangeUser() {

        String[] credentials = Windows.auth();

        Main.wipeConfig();

        Main.username = credentials[0];
        Main.pass = credentials[1];
        Main.smtpHost = "smtp."+credentials[0].split("@")[1];
        Main.smtpPort = "465";

        Main.updateConfig();

        Windows.success("Success!", "Programm will be restart that changes has been saved!", "Programm will be restart", anchorPane.getScene().getWindow());

        //Program has been close
        ((Stage) anchorPane.getScene().getWindow()).close();

        //Garbage collector
        Runtime.getRuntime().gc();

        try {
            Main.startApp(((Stage) anchorPane.getScene().getWindow()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeIncomingScene(Event event) {


        WebEngine browser = mailView.getEngine();

        splitIncoming.setVisible(true);
        Incoming loadIncoming = new Incoming(Main.username, Main.pass, "imap."+Main.username.split("@")[1]);
        try {

            this.messageList = loadIncoming.getMessage();

            ObservableList<String> listOfSubjects = FXCollections.observableArrayList();

            for(int i = 0; i<messageList.size(); i++){
                listOfSubjects.add(messageList.get(i).getMessages()[0]);
            }
            incomingTree.setItems(listOfSubjects);

            //Выводим содержимое мыла в текстовую область
            incomingTree.setOnMouseClicked(e -> {

                int flag = getMatches(e.getTarget().toString().split("\"")[1], listOfSubjects);

                if(flag != -1){
                    browser.loadContent(this.messageList.get(getMatches(e.getTarget().toString().split("\"")[1], listOfSubjects)).getMessages()[1]);
                }
            });

        }catch(Exception e){
            Windows.alert(
                    "Program exception",
                    "Program exception",
                    e.toString(),
                    anchorPane.getScene().getWindow()
            );
        }
    }

    public void backToMainScene(Event event) {
        splitIncoming.setVisible(false);
    }


    public int getMatches(String string, ObservableList<String> array){
        int index = -1;
        for (int i = 0; i<array.size(); i++){
            if(string.equals(array.get(i))){
                index = i;
            }
        }
        return index;
    }
}
