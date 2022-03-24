package controller.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.WritableImage;
import model.gui.Popup;
import model.testing.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.main.Main.primaryStage;

public class ShowTestResultController implements Initializable, Popup {

    private TestResult result;

    @FXML private ScrollPane mainPane;

    @FXML private ComboBox<AgentResult> agentTypeCb;
    @FXML private ComboBox<MapResult> mapNameCb;
    @FXML private ComboBox<Statistic> iterationCb;

    @FXML private Label agentTypeLbl;
    @FXML private Label mapNameLbl;
    @FXML private Label iterationLbl;

    @FXML private TitledPane testResultPane;
    @FXML private TitledPane agentResultPane;
    @FXML private TitledPane mapResultPane;
    @FXML private TitledPane mapIterationResultPane;

    private AgentResult chosenAgent;
    private MapResult chosenMapResult;
    private Statistic chosenIteration;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        result = (TestResult) primaryStage.getUserData();
        initAgentTypeCombobox();
    }

    public void btnShowsStatesOnAction() {

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Working", chosenIteration.getTimeWorking()),
                        new PieChart.Data("Searching", chosenIteration.getTimeSearching()),
                        new PieChart.Data("Travelling", chosenIteration.getTimeTravelling()));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Time spent by agents in given states");

        chart.setPrefHeight(800);
        chart.setPrefWidth(800);

        mainPane.setContent(chart);
    }

    public void btnSearchingOnAction() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Searching");

        int numIters = chosenMapResult.getIterations().size();

        for(int i = 0; i < numIters; i++) {
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), chosenIteration.getTimeSearching()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnTravellingOnAction() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Searching");

        int numIters = result.getAgentResults().get(0).getMapResults().get(0).getIterations().size();
        for(int i = 0; i < numIters; i++) {
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), chosenIteration.getTimeTravelling()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnWorkingOnAction() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Searching");

        int numIters = result.getAgentResults().get(0).getMapResults().get(0).getIterations().size();
        for(int i = 0; i < numIters; i++) {
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), chosenIteration.getTimeWorking()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnShowPollutionOnAction() {

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Seconds");
        yAxis.setLabel("Total Pollution");
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setPrefHeight(800);
        lineChart.setPrefWidth(800);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data
        for(int i = 0; i < chosenIteration.getNumSeconds(); i++){
            series.getData().add(new XYChart.Data(i, chosenIteration.getTotalPollution().get(i)));
        }

        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);
        lineChart.autosize();
        lineChart.applyCss();
        lineChart.setTitle("Pollution progression");
        lineChart.setStyle("-fx-stroke-width: 1px;");
        lineChart.getData().add(series);
        mainPane.setContent(lineChart);
    }

    public void agentTypeOnAction() {

        chosenAgent = agentTypeCb.getValue();

        if (chosenAgent == null) {
            agentTypeLbl.setText("UNSELECTED");
            mapNameCb.setValue(null);
            iterationCb.setValue(null);
            mapNameCb.setDisable(true);
            iterationCb.setDisable(true);
            return;
        }

        initMapNamesComboBox();

    }

    public void mapNameOnAction() {

        chosenMapResult = mapNameCb.getValue();

        if (chosenMapResult == null) {
            mapNameLbl.setText("UNSELECTED");
            iterationCb.setValue(null);
            iterationCb.setDisable(true);
            return;
        }

        initIterationsComboBox();

    }

    public void iterNumOnAction() {

        chosenIteration = iterationCb.getValue();

        if(chosenIteration == null) {
            iterationLbl.setText("UNSELECTED");
            return;
        }

        int iterationNumber = iterationCb.getItems().indexOf(chosenIteration);
        chosenIteration = chosenMapResult.getIterations().get(iterationNumber);
        iterationLbl.setText(chosenIteration.getIterationNumber());

    }

    public void btnWorkDoneOnAction() {

    }

    public void saveOnAction() {
        Node n = mainPane.getContent();
        saveAsPNG(n, "charts\\chart1.png");
    }


    private void saveAsPNG(Node n, String path) {
        WritableImage image = n.snapshot(new SnapshotParameters(), null);
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            popup("File saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initAgentTypeCombobox() {

        ObservableList<AgentResult> agentTypes = FXCollections.observableArrayList(result.getAgentResults());

        agentTypeCb.getItems().setAll(agentTypes);
        mapNameCb.setDisable(true);
        iterationCb.setDisable(true);

    }


    private void initMapNamesComboBox() {

        mapNameCb.setDisable(false);

        ObservableList<MapResult> mapResults = FXCollections.observableArrayList(chosenAgent.getMapResults());

        mapNameCb.getItems().setAll(mapResults);

        iterationCb.setValue(null);
        iterationCb.setDisable(true);

    }


    private void initIterationsComboBox() {

        iterationCb.setDisable(false);

        ObservableList<Statistic> iterations = FXCollections.observableArrayList(chosenMapResult.getIterations());

        iterationCb.getItems().setAll(iterations);

    }

}
