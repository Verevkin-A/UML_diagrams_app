package userInterface;

import javafx.scene.Node;

public class Draggable {
    private double axisX;
    private double axisY;

    public void makeDraggable(Node node) {
        node.setOnMousePressed(mouseEvent -> {
            axisX = mouseEvent.getSceneX() - node.getTranslateX();
            axisY = mouseEvent.getSceneY() - node.getTranslateY();
        });
        node.setOnMouseDragged(mouseEvent -> {
            node.setTranslateX(mouseEvent.getSceneX() - axisX);
            node.setTranslateY(mouseEvent.getSceneY() - axisY);
        });
    }
}
