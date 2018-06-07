package rs.ac.bg.etf.cryptography.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import rs.ac.bg.etf.cryptography.controllers.Simulator;
import rs.ac.bg.etf.cryptography.controllers.Simulator.SimMode;
import rs.ac.bg.etf.cryptography.math.ModuloMatrix;
import rs.ac.bg.etf.cryptography.simulators.HillCipher;
import rs.ac.bg.etf.cryptography.utils.UI;

public class DecryptionPageOne extends Page {

    private BorderPane layout;

    private Scene scene;

    private List<TextField> key;

    private List<TextField> inverseKey;

    @Override
    public Scene getScene() {
        layout = new BorderPane();
        layout.setTop(createTopLayout());
        layout.setCenter(createCentralLayout());
        layout.setBottom(createBottomLayout());

        ScrollPane x = new ScrollPane(layout);
        x.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        x.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scene = new Scene(x, 840, 620);

        return scene;
    }

    private VBox createTopLayout() {

        VBox layout = new VBox();
        layout.getChildren().add(createMenuBar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 10, 10, 10));
        grid.setHgap(30);
        grid.setVgap(10);
        grid.setPrefSize(830, 190);

        grid.add(UI.generateLetterMappingTable(), 0, 0, 6, 2);

        grid.add(new Label("Ciphertext:"), 0, 2, 4, 1);
        TextField ciphertextInput = new TextField();
        ciphertextInput.setMinWidth(350);
        grid.add(ciphertextInput, 0, 3, 4, 1);
        if (Simulator.getMode() == SimMode.TEST) {
            ciphertextInput.setEditable(false);
            ciphertextInput.setText(Simulator.getCiphertext());
        }
        ciphertextInput.setPromptText("Enter the message to be decrypted...");
        ciphertextInput.textProperty().addListener((observable, oldValue, newValue) -> {
            UI.limitPlainTextInput(ciphertextInput, oldValue, newValue);
            Simulator.setCiphertext(ciphertextInput.getText());
        });

        grid.add(new Label("Key size:"), 4, 2, 1, 1);
        ComboBox<Integer> keySizeInput = new ComboBox<>();
        if (Simulator.getMode() == SimMode.TEST) {
            keySizeInput.getItems().add(Simulator.getKeySize());
        } else {
            keySizeInput.getItems().addAll(IntStream.range(2, 10).mapToObj(i -> i).collect(Collectors.toList()));
        }
        keySizeInput.setMaxWidth(70);
        keySizeInput.getSelectionModel().select(Simulator.getKeySize());
        keySizeInput.setOnAction(e -> keySizeChanged(keySizeInput.getSelectionModel().getSelectedItem()));
        grid.add(keySizeInput, 4, 3, 1, 1);

        layout.getChildren().add(grid);

        return layout;

    }

    private GridPane createCentralLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        if (Simulator.getMode() != SimMode.TEST) {
            grid.add(new Label("Key"), 0, 0);

            key = new ArrayList<>();

            for (int i = 0; i < Simulator.getKeySize(); i++) {
                for (int j = 0; j < Simulator.getKeySize(); j++) {
                    TextField keyCell = new TextField();
                    keyCell.setMaxWidth(40);
                    keyCell.textProperty().addListener((observable, oldValue, newValue) -> EncryptionPageOne
                            .limitKeyInput(keyCell, oldValue, newValue));
                    key.add(keyCell);

                    grid.add(keyCell, j, i + 1);
                }
            }
        }
        grid.add(new Label("Inverse key"), Simulator.getKeySize() + 2, 0, 2, 1);

        inverseKey = new ArrayList<>();

        for (int i = 0; i < Simulator.getKeySize(); i++) {
            for (int j = 0; j < Simulator.getKeySize(); j++) {
                TextField keyCell = new TextField();
                keyCell.setMaxWidth(40);
                keyCell.setEditable(false);
                inverseKey.add(keyCell);
                if (Simulator.getMode() == SimMode.TEST) {
                    keyCell.setText("" + (int) Simulator.getInverseKey().get(i, j));
                }
                grid.add(keyCell, j + Simulator.getKeySize() + 2, i + 1);
            }
        }

        if (Simulator.getMode() != SimMode.TEST) {
            Button generateInverseKey = new Button("Generate Inverse Key");
            generateInverseKey.setOnAction(e -> generateInverseKey());
            grid.add(generateInverseKey, Simulator.getKeySize(), 1, 1, Simulator.getKeySize());
        }

        return grid;
    }

    private GridPane createBottomLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        Button button;
        if (Simulator.getMode() == SimMode.TEST) {
            grid.add(new Label("Plaintext:"), 0, 0, 4, 1);
            TextField plaintext = new TextField();
            grid.add(plaintext, 0, 1, 4, 1);
            plaintext.textProperty().addListener((observable, oldValue, newValue) -> {
                UI.limitPlainTextInput(plaintext, oldValue, newValue);
                Simulator.setPlaintextAnswer(plaintext.getText());
            });

            button = new Button("Check Solution");
            grid.add(button, 4, 1, 1, 1);
        } else {
            grid.setAlignment(Pos.BOTTOM_RIGHT);
            button = new Button("Decipher");
            grid.add(button, 5, 0, 1, 1);
        }
        button.setOnAction(e -> goToSecondPage());

        return grid;
    }

    private void generateInverseKey() {
        try {
            ModuloMatrix inverseKeyMatrix = ModuloMatrix
                    .inverse(new ModuloMatrix(UI.getKeyMatrix(Simulator.getKeySize(), key)));
            for (int i = 0; i < Simulator.getKeySize(); i++) {
                for (int j = 0; j < Simulator.getKeySize(); j++) {
                    inverseKey.get(i * Simulator.getKeySize() + j).setText("" + inverseKeyMatrix.get(i, j));
                }
            }
            Simulator.setInverseKey(inverseKeyMatrix.getMatrix());
        } catch (NumberFormatException e) {
            new Alert(AlertType.WARNING, "The key matrix must not contain empty fields.", ButtonType.OK).showAndWait();
        } catch (ArithmeticException e) {
            new Alert(AlertType.WARNING, "The key you entered is not invertible.", ButtonType.OK).showAndWait();
        }
    }

    private void goToSecondPage() {
        if (Simulator.getCiphertext().isEmpty()) {
            new Alert(AlertType.WARNING, "Ciphertext must not be empty.", ButtonType.OK).showAndWait();
            return;
        }
        if (!UI.isKeyFilled(inverseKey)) {
            new Alert(AlertType.WARNING, "Generate inverse key before proceeding.", ButtonType.OK).showAndWait();
            return;
        }
        if ((Simulator.getMode() == SimMode.TEST) && Simulator.getPlaintextAnswer().isEmpty()) {
            new Alert(AlertType.WARNING, "Please fill your answer.", ButtonType.OK).showAndWait();
            return;
        }
        UI.switchScene(HillCipher.window, new DecryptionPageTwo().getScene());

        if ((Simulator.getMode() == SimMode.TEST)) {
            if (Simulator.getPlaintextAnswer().equals(Simulator.getPlaintext())) {
                new Alert(AlertType.INFORMATION, "Congratulations, correct answer.", ButtonType.OK).showAndWait();
                return;
            } else {
                new Alert(AlertType.ERROR, "Unfortunately the answer is not correct.", ButtonType.OK).showAndWait();
                return;
            }
        }
    }

    private void keySizeChanged(Integer newKeySize) {
        Simulator.setKeySize(newKeySize);
        layout.setCenter(createCentralLayout());
    }
}
