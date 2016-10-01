import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

public class Controller {

    private static Sender sender = new Sender(Main.username, Main.pass);

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
    public void initialize() {
        userDefine.setText(Main.username);
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

        File config = new File("./config.ini");

        BufferedWriter bw = null;

        try {
            String[] credentials = Windows.auth();
            String configWrite = "name=" + credentials[0] + "\npass=" + credentials[1];
            File newConfig = new File("./config.ini");
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(newConfig), "UTF8"));
            bw.write(configWrite);
            Main.username = credentials[0];
            Main.pass = credentials[1];
            Controller.sender = new Sender(Main.username, Main.pass);
            userDefine.setText(Main.username);

        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            try {
                bw.close();
            } catch (Exception e) {
            }
        }


    }
}
