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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rs.ac.bg.etf.cryptography.controllers.Simulator;
import rs.ac.bg.etf.cryptography.math.ModuloMatrix;
import rs.ac.bg.etf.cryptography.simulators.HillCipher;
import rs.ac.bg.etf.cryptography.utils.UI;

public class CryptoanalysisPageOne extends Page {

    private BorderPane layout;

    private List<TextField> key;

    @Override
    public Scene getScene() {
        layout = new BorderPane();
        layout.setTop(createTopLayout());
        layout.setCenter(createCentralLayout());

        ScrollPane x = new ScrollPane(layout);
        return new Scene(x, 830, 700);
    }

    private VBox createTopLayout() {
        VBox layout = new VBox();
        layout.getChildren().add(createMenuBar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 10, 0, 10));
        grid.setHgap(30);
        grid.setVgap(10);
        grid.setPrefSize(830, 120);
        grid.add(UI.generateLetterMappingTable(), 0, 0);
        layout.getChildren().add(grid);

        HBox pane = new HBox(new Label("PLAINTEXT  "), new Label("  ->  "),
                UI.createRectangle("Hill Cipher Machine", 150, 60), new Label("  ->  "), new Label("  CIPHERTEXT"));
        pane.setAlignment(Pos.CENTER);
        layout.getChildren().add(pane);

        pane = new HBox(new Label(
                "The idea of chosen plaintext attack is to carefully choose plaintext input for encryption so that the output\nor ciphertext becomes the values of secret key."));
        pane.setPadding(new Insets(10, 10, 0, 10));
        layout.getChildren().add(pane);
        return layout;
    }

    private VBox createCentralLayout() {
        VBox grid = new VBox();
        grid.setPadding(new Insets(10, 10, 0, 10));
        grid.getChildren().addAll(createKeySizePane(), createKeyLayout());

        return grid;
    }

    private HBox createKeySizePane() {
        HBox pane = new HBox();
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.getChildren().add(new Label("Key size:"));
        ComboBox<Integer> keySizeInput = new ComboBox<>();
        keySizeInput.getItems().addAll(IntStream.range(2, 5).mapToObj(i -> i).collect(Collectors.toList()));
        keySizeInput.setMaxWidth(70);
        keySizeInput.getSelectionModel().select(Simulator.getKeySize());
        keySizeInput.setOnAction(e -> keySizeChanged(keySizeInput.getSelectionModel().getSelectedItem()));
        pane.getChildren().add(keySizeInput);

        return pane;
    }

    private GridPane createKeyLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(30);
        grid.setVgap(10);

        int row = 0;
        int col = 0;

        grid.add(new Label("Key"), col, row++);

        key = new ArrayList<>();

        for (int i = 0; i < Simulator.getKeySize(); i++) {
            for (int j = 0; j < Simulator.getKeySize(); j++) {
                TextField keyCell = new TextField();
                keyCell.setMaxWidth(40);
                key.add(keyCell);
                keyCell.textProperty().addListener((observable, oldValue, newValue) -> EncryptionPageOne
                        .limitKeyInput(keyCell, oldValue, newValue));
                grid.add(keyCell, col + j, row);
            }
            row++;
        }

        Button attack = new Button("Simulate attack");
        attack.setOnAction(e -> simulateAttack());
        grid.add(attack, Simulator.getKeySize(), 1, 1, Simulator.getKeySize());

        return grid;
    }

    private void simulateAttack() {
        if (!UI.isKeyFilled(key)) {
            new Alert(AlertType.WARNING, "Please fill all the key matrix fields.", ButtonType.OK).showAndWait();
            return;
        }

        try {
            ModuloMatrix matrix = new ModuloMatrix(UI.getKeyMatrix(Simulator.getKeySize(), key));
            ModuloMatrix.inverse(matrix);
            Simulator.setKey(matrix.getMatrix());
        } catch (ArithmeticException e) {
            new Alert(AlertType.WARNING, "Key matrix is not invertible.", ButtonType.OK).showAndWait();
            return;
        }
        UI.switchScene(HillCipher.window, new CryptoanalysisPageTwo().getScene());
    }

    private void keySizeChanged(int newKeySize) {
        Simulator.setKeySize(newKeySize);
        layout.setCenter(createCentralLayout());
    }
}
