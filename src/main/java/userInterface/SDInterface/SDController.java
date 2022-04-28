package userInterface.SDInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class SDController {

    private GridPane gpActivations, gpMessages, gpObjects;

    @FXML
    private AnchorPane root;

    @FXML
    private ScrollPane spActivations, spMessages, spObjects;

    /**
     * Sequence diagram window initialization
     */
    @FXML
    public void initialize() {
        // initialize grid panes on pane
        gpActivations = new GridPane();
        gpInit(spActivations, gpActivations);
        gpMessages = new GridPane();
        gpInit(spMessages, gpMessages);
        gpObjects = new GridPane();
        gpInit(spObjects, gpObjects);
    }

    private void gpInit(ScrollPane scrollPane, GridPane gridPane) {
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        scrollPane.setContent(gridPane);
    }

    @FXML
    void addActivation(ActionEvent event) {

    }

    @FXML
    void addMessage(ActionEvent event) {

    }

    @FXML
    void addObject(ActionEvent event) {

    }

    @FXML
    void saveAction(ActionEvent event) {

    }

    @FXML
    void undoAction(ActionEvent event) {

    }

}

