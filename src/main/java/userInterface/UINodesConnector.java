package userInterface;

import javafx.scene.control.TitledPane;

/**
 * Connection between all single nodes objects on the pane
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-12
 */
public class UINodesConnector {
    private Double axisX1, axisY1, axisX2, axisY2;
    private TitledPane from, to;

    public UINodesConnector (Double axisX1, Double axisY1, Double axisX2, Double axisY2, TitledPane from, TitledPane to) {
        this.axisX1 = axisX1;
        this.axisY1 = axisY1;
        this.axisX2 = axisX2;
        this.axisY2 = axisY2;
        this.from = from;
        this.to = to;
    }

    public Double getAxisX1() {
        return axisX1;
    }

    public Double getAxisY1() {
        return axisY1;
    }

    public Double getAxisX2() {
        return axisX2;
    }

    public Double getAxisY2() {
        return axisY2;
    }

    public TitledPane getFrom() {
        return from;
    }

    public TitledPane getTo() {
        return to;
    }
}
