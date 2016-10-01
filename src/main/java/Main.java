import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;
import java.io.*;

public class Main extends Application {

    public static String username;
    public static String pass;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Проверка существует ли конфиг:
        File config = new File("./config.ini");
        if(!config.exists()) {
            //Если не существует, то записать
            BufferedWriter bw = null;

            try {
                String[] credentials = Windows.auth();
                String configWrite = "name=" + credentials[0] + "\npass=" + credentials[1];
                File newConfig = new File("./config.ini");
                bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(newConfig), "UTF8"));
                bw.write(configWrite);
                username = credentials[0];
                pass = credentials[1];

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

                String[] trimText = new String[2];
                int i = 0;
                //Достаём логин и пароль из конфига
                while ((line = br.readLine()) != null) {
                    trimText[i] = line.split("=")[1];
                    ++i;
                }

                username = trimText[0];
                pass = trimText[1];

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
        Parent root = FXMLLoader.load(getClass().getResource("/layout.fxml")); // was java.sample.fxml
        primaryStage.setTitle("HTML Editor");
        primaryStage.setScene(new Scene(root, 615, 375));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
