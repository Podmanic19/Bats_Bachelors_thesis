package controller.test;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import model.testing.AgentResult;
import model.testing.MapResult;
import model.testing.Statistic;
import model.testing.TestResult;

public class ShowTestResultController {

    private TestResult result;

    @FXML private Pane mainPane;

    @FXML private TitledPane testResultPane;
    @FXML private TitledPane agentResultPane;
    @FXML private TitledPane mapResultPane;
    @FXML private TitledPane mapIterationResultPane;

    private AgentResult chosenAgent;
    private MapResult chosenMapResult;
    private Statistic chosenIteration;

    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);

    public void kkt() {
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");
    }




}
