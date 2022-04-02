package userInterface;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;

public class SetWindow {
    Draggable draggable = new Draggable();

    public AnchorPane createWindow() {
        AnchorPane root = new AnchorPane();

        TitledPane titledPane1 = new TitledPane();
        TitledPane titledPane2 = new TitledPane();

        GridPane gridPane1 = createGridPane();
        titledPane1.setContent(gridPane1);

        GridPane gridPane2 = createGridPane();
        titledPane2.setContent(gridPane2);

        titledPane1.setText("Address");
        titledPane1.setExpanded(true);
        titledPane1.setCollapsible(false);

        titledPane2.setText("Address2");
        titledPane2.setExpanded(true);
        titledPane2.setCollapsible(false);

        AnchorPane.setTopAnchor(titledPane1, 10.0);
        AnchorPane.setLeftAnchor(titledPane1, 10.0);

        AnchorPane.setTopAnchor(titledPane2, 10.0);
        AnchorPane.setLeftAnchor(titledPane2, 310.0);
        // bound line
        Line line = new Line(10, 10, 310, 10);
        // add all elements into canvas
        root.getChildren().addAll(titledPane1, titledPane2, line);
        // enable drag on all elements
        root.getChildren().forEach(this.draggable::makeDraggable);
        return root;
    }

    public GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.add(new Label("Street name"), 0, 1);
        gridPane.add(new TextField(), 1, 1);
        gridPane.add(new Label("House number"), 0, 0);
        gridPane.add(new TextField(), 1, 0);
        gridPane.add(new Label("City"), 0, 2);
        gridPane.add(new TextField(), 1, 2);
        gridPane.add(new Label("Province"), 0, 3);
        gridPane.add(new TextField(), 1, 3);
        gridPane.add(new Label("Postal"), 0, 4);
        gridPane.add(new TextField(), 1, 4);

        return gridPane;
    }
}
