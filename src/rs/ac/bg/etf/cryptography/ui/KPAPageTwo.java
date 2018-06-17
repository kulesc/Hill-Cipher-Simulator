package rs.ac.bg.etf.cryptography.ui;

import Jama.Matrix;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rs.ac.bg.etf.cryptography.controllers.Simulator;
import rs.ac.bg.etf.cryptography.math.ModuloMatrix;
import rs.ac.bg.etf.cryptography.utils.Common;
import rs.ac.bg.etf.cryptography.utils.UI;

public class KPAPageTwo extends KPAPageOne {

    @Override
    protected VBox createTopLayout() {
        VBox pane = super.createTopLayout();

        keySizeInput.setOnAction(null);
        keySizeInput.getItems().remove(0, 3);
        keySizeInput.getItems().add(Simulator.getKeySize());
        keySizeInput.getSelectionModel().select(0);
        return pane;
    }

    @Override
    protected GridPane createCentralLayout() {
        GridPane grid = new GridPane();
        grid.setHgap(150);
        grid.setVgap(50);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.CENTER);

        HBox pane = new HBox();
        pane.setSpacing(20);
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.getChildren().addAll(new Label("U"), new Label("="), UI.getPaneFromMatrixWithTooltips(Simulator.getU()));
        pane.getChildren().addAll(new Label("W"), new Label("="), UI.getPaneFromMatrixWithTooltips(Simulator.getW()));
        Matrix inverseU = ModuloMatrix.inverse(new ModuloMatrix(Simulator.getU())).getMatrix();
        pane.getChildren().addAll(new Label("inv(U)"), new Label("="), UI.getPaneFromMatrixWithTooltips(inverseU));
        grid.add(pane, 0, 0);

        pane = new HBox();
        pane.setSpacing(10);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(new Label("U"), new Label("x"), new Label("K"), new Label("="), new Label("W"));
        grid.add(pane, 0, 1);

        pane = new HBox();
        pane.setSpacing(10);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(new Label("K"), new Label("="), new Label("inv(U)"), new Label("x"), new Label("W"));
        grid.add(pane, 0, 2);

        pane = new HBox();
        pane.setSpacing(10);
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.getChildren().addAll(new Label("K"), new Label("="));
        Matrix key = inverseU.times(Simulator.getW());
        pane.getChildren().addAll(UI.getPaneFromMatrixWithTooltips(inverseU), new Label("X"),
                UI.getPaneFromMatrixWithTooltips(Simulator.getW()), new Label("="),
                UI.getPaneFromMatrixWithTooltips(key), new Label("="), UI.getPaneFromMatrixWithTooltips(
                        Common.moduloMatrix(key, key.getRowDimension(), key.getColumnDimension())));
        grid.add(pane, 0, 3);

        return grid;
    }

    @Override
    protected VBox createBottomLayout() {
        return new VBox();
    }

}
