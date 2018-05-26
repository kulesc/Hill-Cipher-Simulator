package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class EncryptionPage extends SceneCreator {

    private TextField keySizeInput;

    private BorderPane layout;

    private List<TextField> key = new ArrayList<>();

    private Scene scene;

    public EncryptionPage(Stage window) {
        super(window);
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
        TextField plaintextInput = new TextField();
        plaintextInput.setMinWidth(350);
        grid.add(plaintextInput, 0, 3, 4, 1);
        plaintextInput.setPromptText("Enter the message to be encrypted...");

        grid.add(new Label("Key size:"), 4, 2, 1, 1);
        keySizeInput = new TextField();
        keySizeInput.setMaxWidth(70);
        keySizeInput.setText("2");
        keySizeInput.textProperty().addListener((observable, oldValue, newValue) -> keySizeChanged());
        grid.add(keySizeInput, 4, 3, 1, 1);

        grid.add(new Label("Fill character:"), 5, 2, 1, 1);
        ComboBox<String> fillCharacterPicker = new ComboBox<>();
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

        int keySize = Integer.parseInt(keySizeInput.getText());
        grid.add(new Label("Key"), 0, 0);

        for (int i = 0; i < keySize; i++) {
            for (int j = 0; j < keySize; j++) {
                TextField keyCell = new TextField();
                keyCell.setMaxWidth(40);
                key.add(keyCell);
                grid.add(keyCell, i, j + 1);
            }
        }

        return grid;
    }

    private GridPane createBottomLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(30);
        grid.setVgap(10);

        Button encipher = new Button("Encipher");
        grid.add(encipher, 5, 0, 1, 1);

        return grid;
    }

    private TableView<LetterMapping> generateLetterMappingTable() {
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

        return table;
    }

    private void keySizeChanged() {
        try {
            layout.setCenter(createCentralLayout());
            scene.setRoot(layout);
        } catch (NumberFormatException e) {
        }
    }
}
