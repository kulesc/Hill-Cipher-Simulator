package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainPage extends SceneCreator {

    private RadioButton encryption, decryption;
    private ToggleGroup simulatorMode;

    public MainPage(Stage window) {
        super(window);
    }

    private VBox createFooter() {
        VBox layout = new VBox();
        layout.setPadding(new Insets(30, 10, 30, 10));
        Button startSimulation = new Button("Start Simulation");
        startSimulation.setOnAction(e -> startSimulation(simulatorMode));
        layout.getChildren().addAll(startSimulation);
        layout.setAlignment(Pos.CENTER);
        return layout;
    }

    private VBox createCentralLayout() {
        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 10, 30, 10));

        Label heading = new Label("Hill Cipher Simulator");
        heading.setFont(new Font(20));
        VBox.setMargin(heading, new Insets(0, 15, 50, 15));

        simulatorMode = new ToggleGroup();

        encryption = new RadioButton("Encryption");
        encryption.setToggleGroup(simulatorMode);
        encryption.setSelected(true);
        VBox.setMargin(encryption, new Insets(0, 0, 20, 0));

        decryption = new RadioButton("Decryption");
        decryption.setToggleGroup(simulatorMode);

        layout.getChildren().addAll(heading, encryption, decryption);
        layout.setAlignment(Pos.CENTER);
        return layout;
    }

    @Override
    public Scene getScene() {
        BorderPane layout = new BorderPane();
        layout.setTop(createMenuBar());
        layout.setCenter(createCentralLayout());
        layout.setBottom(createFooter());

        return new Scene(layout, 250, 300);
    }

    private void startSimulation(ToggleGroup simulatorMode) {
        RadioButton selectedMode = (RadioButton) simulatorMode.getSelectedToggle();
        if (selectedMode.equals(encryption)) {
            Main.switchScene(new EncryptionPageOne(window).getScene());
        } else if (selectedMode.equals(decryption)) {
            // TODO: Add switching to decryption scene
        }
    }

}
