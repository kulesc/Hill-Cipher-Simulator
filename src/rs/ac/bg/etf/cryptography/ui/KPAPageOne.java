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

public class KPAPageOne extends Page {

    private BorderPane layout;
    protected ComboBox<Integer> keySizeInput;
    private List<TextField> plaintextBlocks;
    private List<TextField> ciphertextBlocks;

    @Override
    public Scene getScene() {
        layout = new BorderPane();
        layout.setTop(createTopLayout());
        layout.setCenter(createCentralLayout());
        layout.setBottom(createBottomLayout());

        ScrollPane x = new ScrollPane(layout);
        return new Scene(x, 950, 700);
    }

    protected VBox createTopLayout() {
        VBox layout = new VBox();
        layout.getChildren().add(createMenuBar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 10, 0, 10));
        grid.setHgap(30);
        grid.setVgap(10);
        grid.setPrefSize(950, 120);
        grid.add(UI.generateLetterMappingTable(), 0, 0);
        layout.getChildren().add(grid);

        keySizeInput = new ComboBox<>();
        keySizeInput.getItems().addAll(IntStream.range(2, 5).mapToObj(i -> i).collect(Collectors.toList()));
        keySizeInput.setMaxWidth(70);
        keySizeInput.getSelectionModel().select(Simulator.getKeySize());
        keySizeInput.setOnAction(e -> keySizeChanged(keySizeInput.getSelectionModel().getSelectedItem()));

        HBox pane = new HBox(new Label("Key size: "), keySizeInput);
        pane.setAlignment(Pos.CENTER);
        layout.getChildren().add(pane);

        return layout;
    }

    protected GridPane createCentralLayout() {
        GridPane grid = new GridPane();
        grid.setHgap(150);
        grid.setVgap(50);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("PLAINTEXT BLOCKS"), 0, 0);
        grid.add(new Label("CIPHERTEXT BLOCKS"), 1, 0);

        plaintextBlocks = new ArrayList<>();
        ciphertextBlocks = new ArrayList<>();

        for (int i = 0; i < Simulator.getKeySize(); i++) {
            TextField ptb = new TextField();
            ptb.setPrefWidth(250);
            ptb.textProperty()
                    .addListener((observable, oldValue, newValue) -> UI.limitPlainTextInput(ptb, oldValue, newValue));
            grid.add(ptb, 0, i + 1);
            plaintextBlocks.add(ptb);

            TextField ctb = new TextField();
            ctb.setPrefWidth(250);
            ctb.textProperty()
                    .addListener((observable, oldValue, newValue) -> UI.limitPlainTextInput(ctb, oldValue, newValue));
            grid.add(ctb, 1, i + 1);
            ciphertextBlocks.add(ctb);
        }

        return grid;
    }

    protected VBox createBottomLayout() {
        VBox pane = new VBox();
        pane.setPadding(new Insets(50, 0, 0, 0));
        Button simulate = new Button("Simulate Attack");
        simulate.setOnAction(e -> startSimulation());
        pane.getChildren().add(simulate);
        pane.setAlignment(Pos.CENTER);

        return pane;
    }

    private void startSimulation() {
        List<TextField> textFields = new ArrayList<>();
        textFields.addAll(ciphertextBlocks);
        textFields.addAll(plaintextBlocks);

        for (TextField tf : textFields) {
            if (tf.getText().length() != Simulator.getKeySize()) {
                new Alert(AlertType.WARNING, "All plaintext and ciphertext blocks must be exactly "
                        + Simulator.getKeySize() + " characters long.", ButtonType.OK).showAndWait();
                return;
            }
        }

        Matrix u = UI.getMatrixFromTextFields(plaintextBlocks);

        try {
            ModuloMatrix.inverse(new ModuloMatrix(u));
        } catch (ArithmeticException e) {
            new Alert(AlertType.WARNING, "Blocks of plaintext must be linearly independent.", ButtonType.OK)
                    .showAndWait();
            return;
        }

        Simulator.setU(u);
        Simulator.setW(UI.getMatrixFromTextFields(ciphertextBlocks));

        UI.switchScene(HillCipher.window, new KPAPageTwo().getScene());
    }

    protected void keySizeChanged(int newKeySize) {
        Simulator.setKeySize(newKeySize);
        layout.setCenter(createCentralLayout());
    }
}
