package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EncryptionPageTwo extends SceneCreator {

    private Integer keySize;
    private String plaintext;
    private String fillCharacter;
    private String filledPlaintext;
    private Map<Integer, List<Integer>> plaintextMatrices = new HashMap<>();
    private List<TextField> key;

    public EncryptionPageTwo(Stage window, String plaintext, Integer keySize, String fillCharacter,
            List<TextField> key) {
        super(window);
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

        for(int i = 0; i < filledPlaintext.length() / keySize; i++) {
            plaintextMatrices.put(i, new ArrayList<>());
        }
        
        for(int i = 0; i < filledPlaintext.length(); i++) {
            plaintextMatrices.get(i / keySize).add(filledPlaintext.charAt(i) - 'A');
        }
        
        grid.add(new Label("Ciphertext:"), 0, 0, 4, 1);
        TextField ciphertext = new TextField();
        ciphertext.setEditable(false);
        grid.add(ciphertext, 0, 1, 4, 1);

        return grid;
    }
    
    private void fillPlaintext() {
        filledPlaintext = plaintext;
        int fillNeeded = plaintext.length() % keySize == 0 ? 0 : keySize - plaintext.length() % keySize;
        while(fillNeeded-- != 0) {
            filledPlaintext += fillCharacter;
        }
    }

}
