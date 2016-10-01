import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Created by shiyu on 29.09.2016.
 */
public class Windows {

    public static void alert(String title, String headerText, String text, Window window){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(window);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(text);

        alert.showAndWait();
    }
    public static void success(String title, String headerText, String text, Window window){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(window);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(text);

        alert.showAndWait();
    }
    public static void warning(String title, String headerText, String text, Window window){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(window);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static String[] auth(){

        String[] credent = new String[2];

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Enter your email and password");
        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        grid.add(new Label("Email:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);


        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);

        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {

            credent[0] = usernamePassword.getKey();
            credent[1] = usernamePassword.getValue();

        });

        //Защита от бездны
        if((credent[0] == null) || (credent[1] == null)) {
            credent[0] = "undefinded";
            credent[1] = "undefinded";
            return credent;

        }else{

            return credent;
        }

    }
}
