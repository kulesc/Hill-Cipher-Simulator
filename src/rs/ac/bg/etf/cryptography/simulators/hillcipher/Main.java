package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage window;

    @Override
    public void start(final Stage window) {
        try {
            Main.window = window;
            window.setTitle("Hill Cipher Simulator");

            window.setScene(new MainPage(window).getScene());
            window.setResizable(false);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchScene(Scene scene) {
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
