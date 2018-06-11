package rs.ac.bg.etf.cryptography.ui;

import java.util.Arrays;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import rs.ac.bg.etf.cryptography.controllers.Simulator;
import rs.ac.bg.etf.cryptography.math.ModuloMatrix;
import rs.ac.bg.etf.cryptography.utils.Common;
import rs.ac.bg.etf.cryptography.utils.UI;

public class CryptoanalysisPageTwo extends Page {

    private BorderPane layout;

    private HBox bottom;

    private boolean showDetails = false;

    @Override
    public Scene getScene() {
        layout = new BorderPane();
        layout.setTop(createTopLayout());
        layout.setCenter(createCentralLayout());
        bottom = createBottomLayout();
        layout.setBottom(bottom);

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
        StackPane rectangle = UI.createRectangle("Hill Cipher Machine", 150, 60);
        HBox pane = new HBox(new Label("PLAINTEXT  "), new Label("  ->  "), rectangle, new Label("  ->  "),
                new Label("  CIPHERTEXT"));
        pane.setAlignment(Pos.CENTER);
        layout.getChildren().add(pane);

        pane = new HBox(
                new Label("The idea of chosen plaintext attack is to carefully choose plaintext input for encryption "
                        + "so that the output\nor ciphertext becomes the row values of secret key."));
        pane.setPadding(new Insets(10, 10, 0, 10));
        layout.getChildren().add(pane);
        return layout;
    }

    private VBox createCentralLayout() {
        VBox grid = new VBox();
        grid.setPadding(new Insets(10, 10, 0, 10));
        grid.setSpacing(10);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < Simulator.getKeySize(); i++) {
            if (showDetails) {
                grid.getChildren().add(createDetailsRow(i));
            } else {
                grid.getChildren().add(createRow(i));
            }
        }

        return grid;
    }

    private HBox createBottomLayout() {
        HBox pane = new HBox();
        pane.setPadding(new Insets(20, 10, 0, 10));
        pane.setAlignment(Pos.CENTER);
        Button showDetails = new Button("Show Details");
        showDetails.setOnAction(e -> showDetails());
        pane.getChildren().add(showDetails);

        return pane;
    }

    private void showDetails() {
        showDetails = true;
        layout.getChildren().remove(bottom);
        layout.setCenter(createCentralLayout());
    }

    private HBox createDetailsRow(int n) {
        String plaintext = createPlaintext(n);
        String ciphertext = Simulator.encrypt(plaintext, Simulator.getKey());

        HBox pane = new HBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(10);

        GridPane plaintextPane = UI.getPaneFromNumbersWithTooltips(Common.mapLetters(plaintext), 1,
                Simulator.getKeySize());
        GridPane keyPane = UI.getPaneFromMatrix(new ModuloMatrix(Simulator.getKey()));
        GridPane ciphertextPane = UI.getPaneFromNumbersWithTooltips(Common.mapLetters(ciphertext), 1,
                Simulator.getKeySize());

        pane.getChildren().addAll(plaintextPane, new Label("X"), keyPane, new Label("="), ciphertextPane);
        return pane;
    }

    private HBox createRow(int n) {
        String plaintext = createPlaintext(n);
        String ciphertext = Simulator.encrypt(plaintext, Simulator.getKey());

        HBox pane = new HBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(10);

        TextField plainTxt = new TextField(plaintext);
        plainTxt.setEditable(false);
        StackPane rectangle = UI.createRectangle("Secret Key", 150, 20);
        TextField ciphTxt = new TextField(ciphertext);
        ciphTxt.setEditable(false);

        pane.getChildren().addAll(plainTxt, new Label("X"), rectangle, new Label("="), ciphTxt);
        return pane;
    }

    private String createPlaintext(int n) {
        char[] plaintext = new char[Simulator.getKeySize()];
        Arrays.fill(plaintext, 'A');
        plaintext[n] = 'B';

        return new String(plaintext);
    }
}
