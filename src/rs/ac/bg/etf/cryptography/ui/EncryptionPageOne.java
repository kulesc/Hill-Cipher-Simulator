package rs.ac.bg.etf.cryptography.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Jama.Matrix;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import rs.ac.bg.etf.cryptography.controllers.Simulator;
import rs.ac.bg.etf.cryptography.math.ModuloMatrix;
import rs.ac.bg.etf.cryptography.simulators.HillCipher;
import rs.ac.bg.etf.cryptography.utils.UI;

public class EncryptionPageOne extends Page {

    private Scene scene;

    private BorderPane layout;

    private List<TextField> key;

    @Override
    public Scene getScene() {
        layout = new BorderPane();
        layout.setTop(createTopLayout());
        layout.setCenter(createCentralLayout());
        layout.setBottom(createBottomLayout());
        scene = new Scene(layout, 840, 600);

        return scene;
    }

    private VBox createTopLayout() {

        VBox layout = new VBox();
        layout.getChildren().add(createMenuBar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 10, 10, 10));
        grid.setHgap(30);
        grid.setVgap(10);

        grid.add(UI.generateLetterMappingTable(), 0, 0, 6, 2);

        grid.add(new Label("Plaintext:"), 0, 2, 4, 1);
        TextField plaintextInput = new TextField();
        plaintextInput.setMinWidth(350);
        grid.add(plaintextInput, 0, 3, 4, 1);
        plaintextInput.setPromptText("Enter the message to be encrypted...");
        plaintextInput.textProperty().addListener((observable, oldValue, newValue) -> {
            UI.limitPlainTextInput(plaintextInput, oldValue, newValue);
            Simulator.setPlaintext(plaintextInput.getText());
        });

        grid.add(new Label("Key size:"), 4, 2, 1, 1);
        ComboBox<Integer> keySizeInput = new ComboBox<>();
        keySizeInput.getItems().addAll(IntStream.range(2, 10).mapToObj(i -> i).collect(Collectors.toList()));
        keySizeInput.setMaxWidth(70);
        keySizeInput.getSelectionModel().select(0);
        Simulator.setKeySize(2);
        keySizeInput.setOnAction(e -> keySizeChanged(keySizeInput.getSelectionModel().getSelectedItem()));
        grid.add(keySizeInput, 4, 3, 1, 1);

        grid.add(new Label("Fill character:"), 5, 2, 1, 1);
        ComboBox<String> fillCharacterPicker = new ComboBox<>();
        grid.add(fillCharacterPicker, 5, 3, 1, 1);

        fillCharacterPicker.getItems().addAll(IntStream.rangeClosed('A', 'Z')
                .mapToObj(character -> "" + (char) character).collect(Collectors.toList()));
        fillCharacterPicker.getSelectionModel().select("X");
        Simulator.setFillCharacter("X");

        layout.getChildren().add(grid);

        return layout;

    }

    private GridPane createCentralLayout() throws NumberFormatException {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(30);
        grid.setVgap(10);

        grid.add(new Label("Key"), 0, 0);

        key = new ArrayList<>();

        for (int i = 0; i < Simulator.getKeySize(); i++) {
            for (int j = 0; j < Simulator.getKeySize(); j++) {
                TextField keyCell = new TextField();
                keyCell.setMaxWidth(40);
                keyCell.textProperty()
                        .addListener((observable, oldValue, newValue) -> limitKeyInput(keyCell, oldValue, newValue));
                key.add(keyCell);
                grid.add(keyCell, j, i + 1);
            }
        }

        return grid;
    }

    private GridPane createBottomLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 40, 20, 10));
        grid.setHgap(30);
        grid.setVgap(10);
        grid.setAlignment(Pos.BOTTOM_RIGHT);

        Button encipher = new Button("Encipher");
        encipher.setOnAction(e -> goToSecondPage());
        grid.add(encipher, 5, 0, 1, 1);

        return grid;
    }

    public static void limitKeyInput(TextField field, String oldValue, String newValue) {
        if (!newValue.isEmpty()) {
            try {
                long value = Integer.parseInt(newValue);
                if (value < 0 || value > 25 || (value == 0 && newValue.length() > 1))
                    throw new Exception();
                field.setText(newValue);
            } catch (Exception e) {
                field.clear();
                field.setText(String.valueOf(oldValue));
            }
        }
    }

    private void keySizeChanged(int newKeySize) {
        Simulator.setKeySize(newKeySize);
        layout.setCenter(createCentralLayout());
    }

    private void goToSecondPage() {
        if (Simulator.getPlaintext().equals("")) {
            new Alert(AlertType.WARNING, "Plaintext field must not be empty.", ButtonType.OK).showAndWait();
            return;
        }

        if (!UI.isKeyFilled(key)) {
            new Alert(AlertType.WARNING, "Key matrix must not be empty.", ButtonType.OK).showAndWait();
            return;
        }

        try {
            Matrix key = UI.getKeyMatrix(Simulator.getKeySize(), this.key);
            ModuloMatrix.inverse(new ModuloMatrix(key));
            Simulator.setKey(key);
        } catch (ArithmeticException e) {
            new Alert(AlertType.WARNING, "Key matrix is not invertible.", ButtonType.OK).showAndWait();
            return;
        }

        UI.switchScene(HillCipher.window, new EncryptionPageTwo().getScene());
    }

}
