package rs.ac.bg.etf.cryptography.ui;

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
import rs.ac.bg.etf.cryptography.controllers.Simulator;
import rs.ac.bg.etf.cryptography.controllers.Simulator.SimMode;
import rs.ac.bg.etf.cryptography.simulators.HillCipher;
import rs.ac.bg.etf.cryptography.utils.UI;

public class MainPage extends Page {

    private VBox createFooter() {
        VBox layout = new VBox();
        layout.setPadding(new Insets(0, 10, 30, 10));
        layout.setAlignment(Pos.CENTER);

        Button startSimulation = new Button("Start Simulation");
        startSimulation.setOnAction(e -> startSimulation(Simulator.getMode()));
        layout.getChildren().add(startSimulation);

        return layout;
    }

    private VBox createCentralLayout() {
        VBox layout = new VBox();
        layout.setPadding(new Insets(30, 0, 30, 80));
        layout.setSpacing(20);

        Label heading = new Label(HillCipher.APP_TITLE);
        heading.setFont(new Font(20));
        VBox.setMargin(heading, new Insets(0, 0, 30, -30));

        layout.getChildren().add(heading);

        ToggleGroup simulatorMode = new ToggleGroup();

        for (SimMode mode : SimMode.values()) {
            RadioButton option = new RadioButton(Simulator.modeLabels.get(mode));
            option.setToggleGroup(simulatorMode);
            option.setOnAction(e -> Simulator.setMode(mode));
            if (mode.equals(SimMode.ENCRYPTION_SIM)) {
                simulatorMode.selectToggle(option);
            }
            layout.getChildren().add(option);
        }

        return layout;
    }

    @Override
    public Scene getScene() {
        BorderPane layout = new BorderPane();
        layout.setTop(createMenuBar());
        layout.setCenter(createCentralLayout());
        layout.setBottom(createFooter());

        return new Scene(layout, 300, 400);
    }

    private void startSimulation(SimMode simulatorMode) {
        switch (simulatorMode) {
        case ENCRYPTION_SIM:
            UI.switchScene(HillCipher.window, new EncryptionPageOne().getScene());
            break;
        case DECRYPTION_SIM:
            UI.switchScene(HillCipher.window, new DecryptionPageOne().getScene());
            break;
        case TEST:
            if (Simulator.loadTestFile()) {
                if (!Simulator.getPlaintext().isEmpty()) {
                    UI.switchScene(HillCipher.window, new EncryptionPageOne().getScene());
                } else {
                    UI.switchScene(HillCipher.window, new DecryptionPageOne().getScene());
                }
            }
            break;
        case CPA_ATTACK:
            UI.switchScene(HillCipher.window, new CryptoanalysisPageOne().getScene());
            break;
        case KPA_ATTACK:
            UI.switchScene(HillCipher.window, new KPAPageOne().getScene());
            break;
        }
    }

}
