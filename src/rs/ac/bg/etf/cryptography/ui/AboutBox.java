package rs.ac.bg.etf.cryptography.ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutBox {

    private static final String TITLE = "About";

    private String heading;

    public AboutBox(String heading) {
        this.heading = heading;
    }

    public void display() {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(AboutBox.TITLE);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(10);
        layout.setHgap(10);

        Label heading = new Label(this.heading);
        heading.setFont(new Font(20));
        GridPane.setHalignment(heading, HPos.CENTER);
        layout.add(heading, 0, 0, 2, 1);

        Label descriptionLabel = new Label("Description:");
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        layout.add(descriptionLabel, 0, 1);

        Label description = new Label("Hill Cipher Simulator is program designed\n"
                + "to help students learn and understand how\n" + "Hill Cipher works.");
        layout.add(description, 1, 1);

        Label createdbyLabel = new Label("Created by:");
        layout.add(createdbyLabel, 0, 2);

        Label createdby = new Label("Ivan Kulezic");
        layout.add(createdby, 1, 2);

        Label createdatLabel = new Label("Created at:");
        GridPane.setValignment(createdatLabel, VPos.TOP);
        layout.add(createdatLabel, 0, 3);

        Label createdat = new Label("University of Belgrade,\n" + "School of Electrical Engineering,\n"
                + "Department of Computer Engineering\nand Information Theory");
        layout.add(createdat, 1, 3);

        Button ok = new Button("OK");
        GridPane.setHalignment(ok, HPos.CENTER);
        ok.setOnAction(e -> window.close());
        layout.add(ok, 0, 4, 2, 1);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }
}
