package rs.ac.bg.etf.cryptography.simulators.hillcipher;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DecryptionPageOne extends SceneCreator {

    private BorderPane layout;

    private Scene scene;

    private TextField ciphertextInput;

    private ComboBox<Integer> keySizeInput;

    private List<TextField> key;

    private List<TextField> inverseKey;

    private Matrix keyMatrix;

    private ModuloMatrix inverseKeyMatrix;

    public DecryptionPageOne(Stage window) {
        super(window);
    }

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

        grid.add(EncryptionPageOne.generateLetterMappingTable(), 0, 0, 6, 2);

        grid.add(new Label("Ciphertext:"), 0, 2, 4, 1);
        ciphertextInput = new TextField();
        ciphertextInput.setMinWidth(350);
        grid.add(ciphertextInput, 0, 3, 4, 1);
        ciphertextInput.setPromptText("Enter the message to be decrypted...");
        ciphertextInput.textProperty().addListener((observable, oldValue, newValue) -> EncryptionPageOne
                .limitPlainTextInput(ciphertextInput, oldValue, newValue));

        grid.add(new Label("Key size:"), 4, 2, 1, 1);
        keySizeInput = new ComboBox<>();
        keySizeInput.getItems().addAll(IntStream.range(2, 10).mapToObj(i -> i).collect(Collectors.toList()));
        keySizeInput.setMaxWidth(70);
        keySizeInput.getSelectionModel().select(0);
        keySizeInput.setOnAction(e -> keySizeChanged());
        grid.add(keySizeInput, 4, 3, 1, 1);

        layout.getChildren().add(grid);

        return layout;

    }

    private GridPane createCentralLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        int keySize = keySizeInput.getSelectionModel().getSelectedItem();
        grid.add(new Label("Key"), 0, 0);

        key = new ArrayList<>();

        for (int i = 0; i < keySize; i++) {
            for (int j = 0; j < keySize; j++) {
                TextField keyCell = new TextField();
                keyCell.setMaxWidth(40);
                keyCell.textProperty().addListener((observable, oldValue, newValue) -> EncryptionPageOne
                        .limitKeyInput(keyCell, oldValue, newValue));
                key.add(keyCell);
                grid.add(keyCell, j, i + 1);
            }
        }

        grid.add(new Label("Inverse key"), keySize + 2, 0, 2, 1);

        inverseKey = new ArrayList<>();

        for (int i = 0; i < keySize; i++) {
            for (int j = 0; j < keySize; j++) {
                TextField keyCell = new TextField();
                keyCell.setMaxWidth(40);
                keyCell.setEditable(false);
                inverseKey.add(keyCell);
                grid.add(keyCell, j + keySize + 2, i + 1);
            }
        }

        Button generateInverseKey = new Button("Generate Inverse Key");
        generateInverseKey.setOnAction(e -> generateInverseKey());
        grid.add(generateInverseKey, keySize, 1, 1, keySize);

        return grid;
    }

    private GridPane createBottomLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.BOTTOM_RIGHT);

        Button decipher = new Button("Decipher");
        decipher.setOnAction(e -> goToSecondPage());
        grid.add(decipher, 5, 0, 1, 1);

        return grid;
    }

    private void generateInverseKey() {

        int keySize = keySizeInput.getSelectionModel().getSelectedItem();

        try {
            keyMatrix = EncryptionPageOne.getKeyMatrix(keySize, key);
        } catch (NumberFormatException e) {
            new Alert(AlertType.WARNING, "The key matrix must not contain empty fields.", ButtonType.OK).showAndWait();
            return;
        }
        if (!keyMatrix.lu().isNonsingular()) {
            new Alert(AlertType.WARNING, "The key you entered is not invertible.", ButtonType.OK).showAndWait();
            return;
        }

        try {
            inverseKeyMatrix = ModuloMatrix.inverse(new ModuloMatrix(keyMatrix));
        } catch (ArithmeticException e) {
            new Alert(AlertType.WARNING, "The key you entered is not invertible.", ButtonType.OK).showAndWait();
            return;
        }

        for (int i = 0; i < keySize; i++) {
            for (int j = 0; j < keySize; j++) {
                inverseKey.get(i * keySize + j).setText("" + inverseKeyMatrix.get(i, j));
            }
        }
    }

    private void goToSecondPage() {
        if (ciphertextInput.getText().isEmpty()) {
            new Alert(AlertType.WARNING, "Ciphertext must not be empty.", ButtonType.OK).showAndWait();
            return;
        }
        if (inverseKeyMatrix == null) {
            new Alert(AlertType.WARNING, "Generate inverse key before proceeding.", ButtonType.OK).showAndWait();
            return;
        }
        Main.switchScene(
                new DecryptionPageTwo(window, ciphertextInput.getText(), inverseKeyMatrix.getMatrix()).getScene());
    }

    private void keySizeChanged() {
        layout.setCenter(createCentralLayout());
    }
}
