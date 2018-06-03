package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public abstract class SceneCreator {

    protected Stage window;

    public SceneCreator(Stage window) {
        this.window = window;
    }

    public abstract Scene getScene();

    protected MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> window.close());
        MenuItem restart = new MenuItem("Restart");
        restart.setOnAction(e -> Main.switchScene(new MainPage(Main.window).getScene()));
        fileMenu.getItems().addAll(restart, exit);

        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");
        helpMenu.getItems().addAll(about);
        about.setOnAction(e -> AboutBox.display());

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }
}
