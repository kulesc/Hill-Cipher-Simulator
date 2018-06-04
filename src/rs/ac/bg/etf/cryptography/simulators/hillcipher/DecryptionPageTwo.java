package rs.ac.bg.etf.cryptography.simulators.hillcipher;

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
import javafx.stage.Stage;

public class DecryptionPageTwo extends SceneCreator {
    private String ciphertext;

    private String plaintext = "";

    private Matrix inverseKey;

    private Map<Integer, List<Integer>> ciphertextMatrixElements = new HashMap<>();
    private Map<Integer, Matrix> ciphertextMatrices = new HashMap<>();
    private Map<Integer, Matrix> plaintextMatrices = new HashMap<>();

    public DecryptionPageTwo(Stage window, String ciphertext, Matrix inverseKey) {
        super(window);
        this.ciphertext = ciphertext;
        this.inverseKey = inverseKey;
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

        grid.add(new Label("Ciphertext:"), 0, 2, 4, 1);
        TextField plaintextInput = new TextField();
        plaintextInput.setMinWidth(350);
        plaintextInput.setText(ciphertext);
        plaintextInput.setEditable(false);
        grid.add(plaintextInput, 0, 3, 4, 1);

        grid.add(new Label("Key size:"), 4, 2, 1, 1);
        ComboBox<Integer> keySizeInput = new ComboBox<>();
        keySizeInput.getItems().add(inverseKey.getColumnDimension());
        keySizeInput.setMaxWidth(70);
        keySizeInput.getSelectionModel().select(0);
        grid.add(keySizeInput, 4, 3, 1, 1);

        layout.getChildren().add(grid);

        return layout;

    }

    private GridPane createCentralLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < ciphertext.length() / inverseKey.getColumnDimension(); i++) {
            ciphertextMatrixElements.put(i, new ArrayList<>());
        }

        for (int i = 0; i < ciphertext.length(); i++) {
            ciphertextMatrixElements.get(i / inverseKey.getColumnDimension()).add(ciphertext.charAt(i) - 'A');
        }

        for (int i = 0; i < ciphertext.length() / inverseKey.getColumnDimension(); i++) {
            Matrix m = new Matrix(1, inverseKey.getColumnDimension());
            List<Integer> elements = ciphertextMatrixElements.get(i);
            for (int j = 0; j < inverseKey.getColumnDimension(); j++) {
                m.set(0, j, elements.get(j));
            }
            ciphertextMatrices.put(i, m);
        }

        int row = 0;
        int col = 0;

        for (int i = 0; i < ciphertext.length() / inverseKey.getColumnDimension(); i++) {
            List<Integer> matrix = ciphertextMatrixElements.get(i);
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
        col += inverseKey.getColumnDimension() + 3;

        for (int i = 0; i < ciphertext.length() / inverseKey.getColumnDimension(); i++) {
            for (int j = 0; j < inverseKey.getColumnDimension(); j++) {
                for (int k = 0; k < inverseKey.getColumnDimension(); k++) {
                    TextField tf = new TextField("" + (int) inverseKey.get(j, k));
                    tf.setEditable(false);
                    tf.setMaxWidth(35);
                    tf.setMaxHeight(30);
                    grid.add(tf, col + k, row);
                }
                row++;
            }
            grid.add(new Label("="), col + inverseKey.getColumnDimension() + 1, row - inverseKey.getColumnDimension(),
                    1, inverseKey.getColumnDimension());
            row += 2;
        }

        for (int i = 0; i < ciphertext.length() / inverseKey.getColumnDimension(); i++) {
            Matrix m = ciphertextMatrices.get(i).times(inverseKey);
            plaintextMatrices.put(i, m);
        }

        row = 0;
        col += inverseKey.getColumnDimension() + 3;

        for (int i = 0; i < ciphertext.length() / inverseKey.getColumnDimension(); i++) {
            Matrix matrix = plaintextMatrices.get(i);
            for (int j = 0; j < inverseKey.getColumnDimension(); j++) {
                TextField tf = new TextField("" + (int) matrix.get(0, j) % 26);
                tf.setEditable(false);
                tf.setMaxWidth(35);
                tf.setMaxHeight(30);
                String letter = "" + (char) (matrix.get(0, j) % 26 + 'A');
                plaintext += letter;
                tf.setTooltip(new Tooltip(letter));
                grid.add(tf, col + j, row, 1, inverseKey.getColumnDimension());
            }
            grid.add(new Label("(mod 26)"), col + inverseKey.getColumnDimension() + 1, row, 1,
                    inverseKey.getColumnDimension());
            row += 2 + inverseKey.getColumnDimension();
        }

        return grid;
    }

    private GridPane createBottomLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Plaintext:"), 0, 0, 4, 1);
        TextField plaintext = new TextField();
        plaintext.setEditable(false);
        plaintext.setText(this.plaintext);
        grid.add(plaintext, 0, 1, 4, 1);

        return grid;
    }

}
