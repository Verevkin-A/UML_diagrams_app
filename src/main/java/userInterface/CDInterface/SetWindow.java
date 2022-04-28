package userInterface.CDInterface;

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
    CDController CDController;
    AnchorPane root;

    /**
     * setWindow constructor
     */
    public SetWindow() {
        this.root = new AnchorPane();
        this.CDController = CDController.setController(this.root);
    }

    /**
     * Main window initialization
     *
     * @return root pane object
     */
    public AnchorPane createWindow() {
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(this.CDController.menuItemLoad, this.CDController.menuItemSave, this.CDController.menuItemUndo);
        Menu menuAbout = new Menu("About");
        menuAbout.getItems().addAll(this.CDController.menuItemHelp, this.CDController.menuItemCredits);
        // configure menu title bar
        MenuBar menuBar = new MenuBar(menuFile, menuAbout);
        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        // set up classes and nodes grid panes
        GridPane gridPaneClasses = new GridPane();
        gpInit(gridPaneClasses);
        GridPane gridPaneNodes = new GridPane();
        gpInit(gridPaneNodes);

        userInterface.CDInterface.CDController.getController().setGridPanes(gridPaneClasses, gridPaneNodes);
        // set up classes and nodes scroll panes
        ScrollPane scrollPaneClasses = new ScrollPane(gridPaneClasses);
        spInit(scrollPaneClasses, 70.0, 950.0, 20.0, 400.0);
        ScrollPane scrollPaneNodes = new ScrollPane(gridPaneNodes);
        spInit(scrollPaneNodes, 440.0, 950.0, 20.0, 15.0);
        // set up classes and nodes labels
        Label labelClasses = new Label("Classes:");
        AnchorPane.setTopAnchor(labelClasses, 45.0);
        AnchorPane.setLeftAnchor(labelClasses, 950.0);
        Label labelNodes = new Label("Nodes:");
        AnchorPane.setTopAnchor(labelNodes, 415.0);
        AnchorPane.setLeftAnchor(labelNodes, 950.0);
        // position add class and node buttons
        AnchorPane.setTopAnchor(this.CDController.buttonCreateClass, 37.0);
        AnchorPane.setLeftAnchor(this.CDController.buttonCreateClass, 1095.0);

        AnchorPane.setTopAnchor(this.CDController.buttonCreateNode, 407.0);
        AnchorPane.setLeftAnchor(this.CDController.buttonCreateNode, 1095.0);
        // add all objects on the main pane
        this.root.getChildren().addAll(menuBar, scrollPaneClasses, scrollPaneNodes, labelClasses, labelNodes,
                this.CDController.buttonCreateClass, this.CDController.buttonCreateNode);

        return this.root;
    }

    /**
     * Grid Pane initialization
     *
     * @param gridPane Grid Pane to initialize
     */
    private void gpInit(GridPane gridPane) {
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
    }

    /**
     * Setting up position of Scroll Pane
     *
     * @param scrollPane Scroll Pane to initialize
     * @param spTop length from the top of Anchor Pane
     * @param spLeft length from the left of Anchor Pane
     * @param spRight length from the right of Anchor Pane
     * @param spBottom length from the bottom of Anchor Pane
     */
    private void spInit(ScrollPane scrollPane, Double spTop, Double spLeft, Double spRight, Double spBottom) {
        AnchorPane.setTopAnchor(scrollPane, spTop);
        AnchorPane.setLeftAnchor(scrollPane, spLeft);
        AnchorPane.setRightAnchor(scrollPane, spRight);
        AnchorPane.setBottomAnchor(scrollPane, spBottom);
    }
}
