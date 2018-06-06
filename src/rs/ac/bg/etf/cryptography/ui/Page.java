package rs.ac.bg.etf.cryptography.ui;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import rs.ac.bg.etf.cryptography.controllers.Simulator;
import rs.ac.bg.etf.cryptography.simulators.HillCipher;

public abstract class Page {

    public abstract Scene getScene();

    protected MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> HillCipher.window.close());
        MenuItem restart = new MenuItem("Restart");
        restart.setOnAction(e -> Simulator.reset());
        fileMenu.getItems().addAll(restart, exit);

        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");
        helpMenu.getItems().addAll(about);
        about.setOnAction(e -> new AboutBox(HillCipher.APP_TITLE).display());

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }
}
