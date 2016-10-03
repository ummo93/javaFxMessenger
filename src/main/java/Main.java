import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;
import java.io.*;

public class Main extends Application {
//TODO https://ru.wikipedia.org/wiki/JavaMail
    public static String username;
    public static String pass;
    public static String smtpHost;
    public static String smtpPort;


    public static void main(String[] args) {
        launch(args);
    }

    public static void startApp(Stage stage) throws Exception {
        //Проверка существует ли конфиг:
        File config = new File("./config.ini");
        if (!config.exists()) {
            //Если не существует, то записать
            BufferedWriter bw = null;

            try {
                String[] credentials = Windows.auth();
                String configWrite = "name=" + credentials[0] + "\npass=" + credentials[1] + "\nsmtpHost=undefined"
                        + "\nsmtpPort=undefined";
                File newConfig = new File("./config.ini");
                bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(newConfig), "UTF8"));
                bw.write(configWrite);
                username = credentials[0];
                pass = credentials[1];
                smtpHost = "smtp."+credentials[0].split("@")[1];
                smtpPort = "465";

            } catch (IOException e) {

                e.printStackTrace();
            } finally {

                try {
                    bw.close();
                } catch (Exception e) {
                }
            }

        } else {
            //Если существует, то прочитать
            BufferedReader br = null;
            String line;
            try {
                br = new BufferedReader(new InputStreamReader
                        (new FileInputStream("./config.ini"), "UTF-8"));

                String[] trimText = new String[4];
                int i = 0;
                //Достаём логин и пароль из конфига
                while ((line = br.readLine()) != null) {
                    trimText[i] = line.split("=")[1];
                    ++i;
                }

                username = trimText[0];
                pass = trimText[1];
                smtpHost = trimText[2];
                smtpPort = trimText[3];

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        //Инициализация компонентов оконного приложения
        Parent root = FXMLLoader.load(Main.class.getResource("/layout.fxml")); // was java.sample.fxml

        stage.setTitle("HTML Editor");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startApp(primaryStage);
    }

    public static void wipeConfig() {
        File config = new File("./config.ini");
        config.delete();
    }

    public static void updateConfig() {

        BufferedWriter bw = null;

        try {
            String configWrite = "name=" + username + "\npass=" + pass + "\nsmtpHost=" + smtpHost
                    + "\nsmtpPort=" + smtpPort;
            File newConfig = new File("./config.ini");
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(newConfig), "UTF8"));
            bw.write(configWrite);

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
