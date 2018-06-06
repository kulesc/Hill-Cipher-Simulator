package rs.ac.bg.etf.cryptography.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI {

    public static void switchScene(Stage window, Scene scene) {
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }
}
