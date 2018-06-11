package rs.ac.bg.etf.cryptography.simulators;

import javafx.application.Application;
import javafx.stage.Stage;
import rs.ac.bg.etf.cryptography.ui.MainPage;

public class HillCipher extends Application {

    public static final String APP_TITLE = "Hill Cipher Simulator";

    public static Stage window;

    @Override
    public void start(final Stage window) {
        try {
            HillCipher.window = window;
            window.setTitle(HillCipher.APP_TITLE);
            window.setScene(new MainPage().getScene());
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
