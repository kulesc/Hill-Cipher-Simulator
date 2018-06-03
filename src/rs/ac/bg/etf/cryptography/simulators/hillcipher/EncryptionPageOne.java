package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Jama.Matrix;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EncryptionPageOne extends SceneCreator {

    private ComboBox<Integer> keySizeInput;

    private BorderPane layout;

    private TextField plaintextInput;

    private List<TextField> key;

    private Scene scene;

    private ComboBox<String> fillCharacterPicker;

    public EncryptionPageOne(Stage window) {
        super(window);
    }

    public String getFillCharacter() {
        return fillCharacterPicker.getSelectionModel().getSelectedItem();
    }

    public Integer getKeySize() {
        return keySizeInput.getSelectionModel().getSelectedItem();
    }

    public List<TextField> getKey() {
        return key;
    }

    public String getPlaintext() {
        return plaintextInput.getText();
    }

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

        grid.add(generateLetterMappingTable(), 0, 0, 6, 2);

        grid.add(new Label("Plaintext:"), 0, 2, 4, 1);
        plaintextInput = new TextField();
        plaintextInput.setMinWidth(350);
        grid.add(plaintextInput, 0, 3, 4, 1);
        plaintextInput.setPromptText("Enter the message to be encrypted...");
        plaintextInput.textProperty().addListener(
                (observable, oldValue, newValue) -> limitPlainTextInput(plaintextInput, oldValue, newValue));

        grid.add(new Label("Key size:"), 4, 2, 1, 1);
        keySizeInput = new ComboBox<>();
        keySizeInput.getItems().addAll(IntStream.range(2, 10).mapToObj(i -> i).collect(Collectors.toList()));
        keySizeInput.setMaxWidth(70);
        keySizeInput.getSelectionModel().select(0);
        keySizeInput.setOnAction(e -> keySizeChanged());
        grid.add(keySizeInput, 4, 3, 1, 1);

        grid.add(new Label("Fill character:"), 5, 2, 1, 1);
        fillCharacterPicker = new ComboBox<>();
        grid.add(fillCharacterPicker, 5, 3, 1, 1);

        fillCharacterPicker.getItems().addAll(IntStream.rangeClosed('A', 'Z')
                .mapToObj(character -> "" + (char) character).collect(Collectors.toList()));
        fillCharacterPicker.getSelectionModel().select("X");

        layout.getChildren().add(grid);

        return layout;

    }

    private GridPane createCentralLayout() throws NumberFormatException {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(30);
        grid.setVgap(10);

        int keySize = keySizeInput.getSelectionModel().getSelectedItem();
        grid.add(new Label("Key"), 0, 0);

        key = new ArrayList<>();
        
        for (int i = 0; i < keySize; i++) {
            for (int j = 0; j < keySize; j++) {
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

    public static TableView<LetterMapping> generateLetterMappingTable() {
        TableView<LetterMapping> table = new TableView<>();

        table.getColumns().addAll(IntStream.rangeClosed('A', 'Z').mapToObj(character -> {
            String columnName = "" + (char) character;
            TableColumn<LetterMapping, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(new PropertyValueFactory<LetterMapping, String>(columnName.toLowerCase()));
            return column;
        }).collect(Collectors.toList()));

        table.getItems().add(new LetterMapping());
        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(30));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private void keySizeChanged() {
        layout.setCenter(createCentralLayout());
        scene.setRoot(layout);
    }

    private static void limitKeyInput(TextField field, String oldValue, String newValue) {
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

    private static void limitPlainTextInput(TextField field, String oldValue, String newValue) {
        if (!newValue.isEmpty()) {
            if (newValue.chars().allMatch(Character::isLetter)) {
                field.setText(newValue.toUpperCase());
            } else {
                field.clear();
                field.setText(oldValue);
            }
        }
    }

    private boolean isKeyFilled() {
        for (TextField x : key) {
            if (x.getText().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static Matrix getKeyMatrix(int size, List<TextField> key) {
        Matrix keyMatrix = new Matrix(size, size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                keyMatrix.set(i, j, Integer.parseInt(key.get(i * size + j).getText()));
            }
        }

        return keyMatrix;
    }

    private void goToSecondPage() {

        if (plaintextInput.getText().isEmpty()) {
            new Alert(AlertType.WARNING, "Plaintext field must not be empty.", ButtonType.OK).showAndWait();
            return;
        }

        if (!isKeyFilled()) {
            new Alert(AlertType.WARNING, "Key matrix must not be empty.", ButtonType.OK).showAndWait();
            return;
        }

        Matrix m = getKeyMatrix(keySizeInput.getSelectionModel().getSelectedItem(), key);
        if (!m.lu().isNonsingular()) {
            new Alert(AlertType.WARNING, "Key matrix is not ivertible.", ButtonType.OK).showAndWait();
            return;
        }
        
        Main.switchScene(new EncryptionPageTwo(Main.window, getPlaintext(), getKeySize(), getFillCharacter(), getKey())
                .getScene());
    }

}
