package rs.ac.bg.etf.cryptography.ui;

import java.math.BigInteger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rs.ac.bg.etf.cryptography.controllers.Simulator;
import rs.ac.bg.etf.cryptography.math.ModuloMatrix;
import rs.ac.bg.etf.cryptography.utils.UI;

public class InverseKeyDetails extends Page {

    private Stage window;

    public InverseKeyDetails(Stage window) {
        this.window = window;
    }

    @Override
    public Scene getScene() {
        BorderPane layout = new BorderPane();
        layout.setCenter(createLayout());

        ScrollPane x = new ScrollPane(layout);
        x.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        x.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        return new Scene(x, 540, 550);
    }

    private GridPane createLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        int row = 0;
        grid.add(new Label("Inverse key can be calculated based on the key using the following formulae:"), 0, row++);
        grid.add(new Label("inv(K) = 1 / det(K) * adj(K), det(K) \u2260 0"), 0, row++);
        int det = (int) Simulator.getKey().det();
        TextField detText = new TextField("" + det);
        detText.setMaxWidth(40);
        detText.setEditable(false);
        HBox detLabel = new HBox(new Label("First step is to calculate det(K) = "), detText);
        detLabel.setAlignment(Pos.CENTER_LEFT);
        grid.add(detLabel, 0, row++);
        grid.add(new Label("In modulo arithmetic:"), 0, row++);
        grid.add(new Label("1 / det(K) = x (mod 26)"), 0, row++);
        grid.add(new Label("<=> x * det(K) = 1 (mod 26)"), 0, row++);
        grid.add(new Label("<=> (x * det(K) - 1) mod 26 = 0"), 0, row++);
        grid.add(new Label("<=> (x * det(K) - 1) mod 26 = 0".replace("det(K)", det < 0 ? "(" + det + ")" : "" + det)),
                0, row++);
        TextField xText = new TextField("" + new BigInteger("" + det).modInverse(new BigInteger("26")));
        xText.setMaxWidth(40);
        xText.setEditable(false);
        HBox x = new HBox(new Label("Using the extended GCD algrithm calculate x = "), xText);
        x.setAlignment(Pos.CENTER_LEFT);
        grid.add(x, 0, row++);

        grid.add(new Label("Second step is to calculate adjugate key matrix:"), 0, row++);

        HBox adjugate = new HBox(new Label("adj(K) (mod 26) = "),
                UI.getPaneFromMatrix(ModuloMatrix.adjugateNoModulo(new ModuloMatrix(Simulator.getKey()))),
                new Label("(mod 26) = "),
                UI.getPaneFromMatrix(ModuloMatrix.adjugate(new ModuloMatrix(Simulator.getKey()))));
        adjugate.setAlignment(Pos.CENTER_LEFT);
        grid.add(adjugate, 0, row++);

        grid.add(new Label("Final step is to calculate the inverse key: "), 0, row++);

        xText = new TextField("" + new BigInteger("" + det).modInverse(new BigInteger("26")));
        xText.setMaxWidth(40);
        xText.setEditable(false);

        HBox invKey = new HBox(new Label("inv(K) = x * adj(K) (mod 26) = "), xText, new Label(" * "),
                UI.getPaneFromMatrix(ModuloMatrix.adjugate(new ModuloMatrix(Simulator.getKey()))),
                new Label(" (mod 26) = "));
        invKey.setAlignment(Pos.CENTER_LEFT);
        grid.add(invKey, 0, row++);

        invKey = new HBox(new Label("inv(K) = "),
                UI.getPaneFromMatrix(ModuloMatrix.inverseNoModulo(new ModuloMatrix(Simulator.getKey()))),
                new Label(" (mod 26) = "),
                UI.getPaneFromMatrix(ModuloMatrix.inverse(new ModuloMatrix(Simulator.getKey()))));
        invKey.setAlignment(Pos.CENTER_LEFT);
        grid.add(invKey, 0, row++);

        Button close = new Button("Close");
        close.setOnAction(e -> window.close());
        HBox buttonPanel = new HBox(close);
        buttonPanel.setAlignment(Pos.CENTER);
        grid.add(buttonPanel, 0, row++);

        return grid;
    }

}
