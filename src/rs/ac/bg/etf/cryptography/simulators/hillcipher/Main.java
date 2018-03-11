package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage window;

	@Override
	public void start(final Stage window) {
		try {
			Main.window = window;
			window.setTitle("Hill Cipher Simulator");

			BorderPane layout = new BorderPane();
			layout.setTop(createMenuBar());
			layout.setCenter(createCentralLayout());
			layout.setBottom(createFooter());

			Scene scene = new Scene(layout, 250, 300);
			window.setScene(scene);
			window.setResizable(false);
			window.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private VBox createFooter() {
		VBox layout = new VBox();
		layout.setPadding(new Insets(30, 10, 30, 10));
		Button startSimulation = new Button("Start Simulation");

		layout.getChildren().addAll(startSimulation);
		layout.setAlignment(Pos.CENTER);
		return layout;
	}

	private VBox createCentralLayout() {
		VBox layout = new VBox();
		layout.setPadding(new Insets(20, 10, 30, 10));

		Label heading = new Label("Hill Cipher Simulator");
		heading.setFont(new Font(20));
		VBox.setMargin(heading, new Insets(0, 15, 50, 15));

		final ToggleGroup simulatorMode = new ToggleGroup();

		RadioButton encryption = new RadioButton("Encryption");
		encryption.setToggleGroup(simulatorMode);
		encryption.setSelected(true);
		VBox.setMargin(encryption, new Insets(0, 0, 20, 0));

		RadioButton decryption = new RadioButton("Decryption");
		decryption.setToggleGroup(simulatorMode);

		layout.getChildren().addAll(heading, encryption, decryption);
		layout.setAlignment(Pos.CENTER);
		return layout;
	}

	private MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("File");
		MenuItem exit = new MenuItem("Exit");
		fileMenu.getItems().addAll(exit);
		exit.setOnAction(e -> window.close());

		Menu helpMenu = new Menu("Help");
		MenuItem about = new MenuItem("About");
		helpMenu.getItems().addAll(about);
		about.setOnAction(e -> AboutBox.display());

		menuBar.getMenus().addAll(fileMenu, helpMenu);
		return menuBar;
	}

}
