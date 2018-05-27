package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EncryptionPageTwo extends SceneCreator {

    private Integer keySize;
    private String plaintext;
    private String fillCharacter;

    private List<TextField> key;

    public EncryptionPageTwo(Stage window, String plaintext, Integer keySize, String fillCharacter,
            List<TextField> key) {
        super(window);
        this.plaintext = plaintext;
        this.keySize = keySize;
        this.fillCharacter = fillCharacter;
        this.key = key;
    }

    @Override
    public Scene getScene() {
        BorderPane layout = new BorderPane();
        layout.setTop(createTopLayout());

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
        grid.setPrefSize(830, 590);

        grid.add(EncryptionPageOne.generateLetterMappingTable(), 0, 0, 1, 2);

        layout.getChildren().add(grid);

        return layout;

    }

}
