package rs.ac.bg.etf.cryptography.simulators.hillcipher;

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

	public static void display() {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("About");

		GridPane layout = new GridPane();
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setVgap(8);
		layout.setHgap(8);

		Label heading = new Label("Hill Cipher Simulator");
		heading.setFont(new Font(20));
		GridPane.setConstraints(heading, 0, 0, 2, 1);
		GridPane.setHalignment(heading, HPos.CENTER);

		Label descriptionLabel = new Label("Description:");
		GridPane.setConstraints(descriptionLabel, 0, 1);
		GridPane.setValignment(descriptionLabel, VPos.TOP);

		Label description = new Label("Hill Cipher Simulator is program designed\n"
				+ "to help students learn and understand how\n" + "Hill Cipher Works.");
		GridPane.setConstraints(description, 1, 1);

		Label createdbyLabel = new Label("Created by:");
		GridPane.setConstraints(createdbyLabel, 0, 2);

		Label createdby = new Label("Ivan Kulezic");
		GridPane.setConstraints(createdby, 1, 2);

		Label createdatLabel = new Label("Created at:");
		GridPane.setConstraints(createdatLabel, 0, 3);
		GridPane.setValignment(createdatLabel, VPos.TOP);

		Label createdat = new Label("University of Belgrade,\n" + "School of Electrical Engineering,\n"
				+ "Department of Computer Engineering\nand Information Theory");
		GridPane.setConstraints(createdat, 1, 3);

		Button ok = new Button("OK");
		GridPane.setConstraints(ok, 0, 4, 2, 1);
		GridPane.setHalignment(ok, HPos.CENTER);
		ok.setOnAction(e -> window.close());
		
		layout.getChildren().addAll(heading, descriptionLabel, description, createdbyLabel, createdby, createdatLabel,
				createdat, ok);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.sizeToScene();
		window.setResizable(false);
		window.showAndWait();
	}
}
