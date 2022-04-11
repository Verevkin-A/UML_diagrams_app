package userInterface;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import static java.lang.System.out;

public class SetWindow {
    Controller controller;
    AnchorPane root;

    public SetWindow() {
        this.root = new AnchorPane();
        this.controller = Controller.setController(this.root);
    }

    public AnchorPane createWindow() {

        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(this.controller.menuItemLoad, this.controller.menuItemSave);
        Menu menuAbout = new Menu("About");
        menuAbout.getItems().addAll(this.controller.menuItemHelp, this.controller.menuItemCredits);
        // configure menu title bar
        MenuBar menuBar = new MenuBar(menuFile, menuAbout);
        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        Controller.getController().setGridPane(gridPane);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        AnchorPane.setTopAnchor(scrollPane, 70.0);
        AnchorPane.setLeftAnchor(scrollPane, 950.0);
        AnchorPane.setRightAnchor(scrollPane, 20.0);
        AnchorPane.setBottomAnchor(scrollPane, 15.0);

        AnchorPane.setTopAnchor(this.controller.buttonCreateClass, 40.0);
        AnchorPane.setLeftAnchor(this.controller.buttonCreateClass, 950.0);

        this.root.getChildren().addAll(menuBar, scrollPane, this.controller.buttonCreateClass);

        return this.root;
    }
}
