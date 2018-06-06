package rs.ac.bg.etf.cryptography.simulators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Jama.Matrix;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import rs.ac.bg.etf.cryptography.ui.Page;

public class EncryptionPageTwo extends Page {

    private Integer keySize;
    private String plaintext;
    private String fillCharacter;
    private String filledPlaintext;
    private String ciphertext = "";
    private Map<Integer, List<Integer>> plaintextMatrixElements = new HashMap<>();
    private Map<Integer, Matrix> plaintextMatrices = new HashMap<>();
    private Map<Integer, Matrix> ciphertextMatrices = new HashMap<>();
    private List<TextField> key;

    public EncryptionPageTwo(String plaintext, Integer keySize, String fillCharacter, List<TextField> key) {
        this.plaintext = plaintext;
        this.keySize = keySize;
        this.fillCharacter = fillCharacter;
        this.key = key;
        fillPlaintext();
    }

    @Override
    public Scene getScene() {
        BorderPane layout = new BorderPane();
        layout.setTop(createTopLayout());
        layout.setCenter(createCentralLayout());
        layout.setBottom(createBottomLayout());

        ScrollPane x = new ScrollPane(layout);
        x.setHbarPolicy(ScrollBarPolicy.NEVER);
        x.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        return new Scene(x, 840, 620);
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

        grid.add(new Label("Plaintext:"), 0, 2, 4, 1);
        TextField plaintextInput = new TextField();
        plaintextInput.setMinWidth(350);
        plaintextInput.setText(plaintext);
        plaintextInput.setEditable(false);
        grid.add(plaintextInput, 0, 3, 4, 1);

        grid.add(new Label("Key size:"), 4, 2, 1, 1);
        ComboBox<Integer> keySizeInput = new ComboBox<>();
        keySizeInput.getItems().add(keySize);
        keySizeInput.setMaxWidth(70);
        keySizeInput.getSelectionModel().select(0);
        grid.add(keySizeInput, 4, 3, 1, 1);

        grid.add(new Label("Fill character:"), 5, 2, 1, 1);
        ComboBox<String> fillCharacterPicker = new ComboBox<>();
        grid.add(fillCharacterPicker, 5, 3, 1, 1);

        fillCharacterPicker.getItems().add(fillCharacter);
        fillCharacterPicker.getSelectionModel().select(fillCharacter);
        layout.getChildren().add(grid);

        return layout;

    }

    private GridPane createCentralLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < filledPlaintext.length() / keySize; i++) {
            plaintextMatrixElements.put(i, new ArrayList<>());
        }

        for (int i = 0; i < filledPlaintext.length(); i++) {
            plaintextMatrixElements.get(i / keySize).add(filledPlaintext.charAt(i) - 'A');
        }

        for (int i = 0; i < filledPlaintext.length() / keySize; i++) {
            Matrix m = new Matrix(1, keySize);
            List<Integer> elements = plaintextMatrixElements.get(i);
            for (int j = 0; j < keySize; j++) {
                m.set(0, j, elements.get(j));
            }
            plaintextMatrices.put(i, m);
        }

        int row = 0;
        int col = 0;

        for (int i = 0; i < filledPlaintext.length() / keySize; i++) {
            List<Integer> matrix = plaintextMatrixElements.get(i);
            for (int j = 0; j < matrix.size(); j++) {
                TextField tf = new TextField("" + matrix.get(j));
                tf.setEditable(false);
                tf.setMaxWidth(35);
                tf.setMaxHeight(30);
                tf.setTooltip(new Tooltip("" + (char) (matrix.get(j) + 'A')));
                grid.add(tf, col + j, row, 1, matrix.size());
            }
            grid.add(new Label("X"), col + matrix.size() + 1, row, 1, matrix.size());
            row += 2 + matrix.size();
        }

        row = 0;
        col += keySize + 3;

        for (int i = 0; i < filledPlaintext.length() / keySize; i++) {
            for (int j = 0; j < keySize; j++) {
                for (int k = 0; k < keySize; k++) {
                    TextField tf = new TextField(key.get(j * keySize + k).getText());
                    tf.setEditable(false);
                    tf.setMaxWidth(35);
                    tf.setMaxHeight(30);
                    grid.add(tf, col + k, row);
                }
                row++;
            }
            grid.add(new Label("="), col + keySize + 1, row - keySize, 1, keySize);
            row += 2;
        }

        Matrix keyMatrix = EncryptionPageOne.getKeyMatrix(keySize, key);

        for (int i = 0; i < filledPlaintext.length() / keySize; i++) {
            Matrix m = plaintextMatrices.get(i).times(keyMatrix);
            ciphertextMatrices.put(i, m);
        }

        row = 0;
        col += keySize + 3;

        for (int i = 0; i < filledPlaintext.length() / keySize; i++) {
            Matrix matrix = ciphertextMatrices.get(i);
            for (int j = 0; j < keySize; j++) {
                TextField tf = new TextField("" + (int) matrix.get(0, j) % 26);
                tf.setEditable(false);
                tf.setMaxWidth(35);
                tf.setMaxHeight(30);
                String letter = "" + (char) (matrix.get(0, j) % 26 + 'A');
                ciphertext += letter;
                tf.setTooltip(new Tooltip(letter));
                grid.add(tf, col + j, row, 1, keySize);
            }
            grid.add(new Label("(mod 26)"), col + keySize + 1, row, 1, keySize);
            row += 2 + keySize;
        }

        return grid;
    }

    private GridPane createBottomLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Ciphertext:"), 0, 0, 4, 1);
        TextField ciphertext = new TextField();
        ciphertext.setEditable(false);
        ciphertext.setText(this.ciphertext);
        grid.add(ciphertext, 0, 1, 4, 1);

        return grid;
    }

    private void fillPlaintext() {
        filledPlaintext = plaintext;
        int fillNeeded = plaintext.length() % keySize == 0 ? 0 : keySize - plaintext.length() % keySize;
        while (fillNeeded-- != 0) {
            filledPlaintext += fillCharacter;
        }
    }

}
