package userInterface;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * Main window initialization
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-02
 */
public class SetWindow {
    Controller controller;
    AnchorPane root;

    /**
     * setWindow constructor
     */
    public SetWindow() {
        this.root = new AnchorPane();
        this.controller = Controller.setController(this.root);
    }

    /**
     * Main window initialization
     *
     * @return root pane object
     */
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
        // set up classes and nodes grid panes
        GridPane gridPaneClasses = new GridPane();
        gridPaneClasses.setPadding(new Insets(10));
        gridPaneClasses.setVgap(5);
        gridPaneClasses.setHgap(5);

        GridPane gridPaneNodes = new GridPane();
        gridPaneNodes.setPadding(new Insets(10));
        gridPaneNodes.setVgap(5);
        gridPaneNodes.setHgap(5);
        Controller.getController().setGridPanes(gridPaneClasses, gridPaneNodes);
        // set up classes and nodes scroll panes
        ScrollPane scrollPaneClasses = new ScrollPane(gridPaneClasses);
        AnchorPane.setTopAnchor(scrollPaneClasses, 70.0);
        AnchorPane.setLeftAnchor(scrollPaneClasses, 950.0);
        AnchorPane.setRightAnchor(scrollPaneClasses, 20.0);
        AnchorPane.setBottomAnchor(scrollPaneClasses, 400.0);

        ScrollPane scrollPaneNodes = new ScrollPane(gridPaneNodes);
        AnchorPane.setTopAnchor(scrollPaneNodes, 440.0);
        AnchorPane.setLeftAnchor(scrollPaneNodes, 950.0);
        AnchorPane.setRightAnchor(scrollPaneNodes, 20.0);
        AnchorPane.setBottomAnchor(scrollPaneNodes, 15.0);
        // set up classes and nodes labels
        Label labelClasses = new Label("Classes:");
        AnchorPane.setTopAnchor(labelClasses, 45.0);
        AnchorPane.setLeftAnchor(labelClasses, 950.0);
        Label labelNodes = new Label("Nodes:");
        AnchorPane.setTopAnchor(labelNodes, 415.0);
        AnchorPane.setLeftAnchor(labelNodes, 950.0);
        // position add class and node buttons
        AnchorPane.setTopAnchor(this.controller.buttonCreateClass, 37.0);
        AnchorPane.setLeftAnchor(this.controller.buttonCreateClass, 1095.0);

        AnchorPane.setTopAnchor(this.controller.buttonCreateNode, 407.0);
        AnchorPane.setLeftAnchor(this.controller.buttonCreateNode, 1095.0);
        // add all objects on the main pane
        this.root.getChildren().addAll(menuBar, scrollPaneClasses, scrollPaneNodes, labelClasses, labelNodes,
                this.controller.buttonCreateClass, this.controller.buttonCreateNode);

        return this.root;
    }
}
