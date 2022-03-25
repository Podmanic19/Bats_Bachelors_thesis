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

    public void btnTimeSpentOnMap() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Total time spent on map");
        xAxis.setLabel("Map");
        yAxis.setLabel("Time spent");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Total time spent cleaning given map by agent");

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), map.getTotalTimeSpent()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(800);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnMaximumTimeSpent() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Maximum time spent on map");
        xAxis.setLabel("Map");
        yAxis.setLabel("Time spent");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Maximum time spent cleaning given map by agent");

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), map.getMaximumTimeSpent()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(800);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);

    }

    public void btnMinimumTimeSpent() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Minimum time spent on map");
        xAxis.setLabel("Map");
        yAxis.setLabel("Time spent");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Minimum time spent cleaning given map by agent");

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), map.getMinimumTimeSpent()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(800);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);

    }

    public void btnAverageTimeSpent() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Average time spent on map");
        xAxis.setLabel("Map");
        yAxis.setLabel("Average time spent");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Average time spent cleaning given map by agent");

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), map.getAverageTimeSpent()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(800);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);

    }

    public void btnMedianTimeSpent() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Median time spent on map");
        xAxis.setLabel("Map");
        yAxis.setLabel("Median of time spent");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Median of time spent cleaning given map by agent");

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), map.getMedianTimeSpent()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(800);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);

    }

    public void btnTotalWorkOnMap() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Total work done on map");
        xAxis.setLabel("Map");
        yAxis.setLabel("Work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Total work done by agents cleaning this map");

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), map.getTotalWorkDone()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(800);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnAverageWorkDoneOnMap() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Average work done on map");
        xAxis.setLabel("Map");
        yAxis.setLabel("Average work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Total work done by agents cleaning this map");

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), map.getAverageWorkDone()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(800);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);

    }
    public void btnMedianWorkDoneOnMap() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Average work done on map");
        xAxis.setLabel("Map");
        yAxis.setLabel("Average work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Total work done by agents cleaning this map");

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), map.getMedianWorkDone()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(800);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }


    public void btnTotalWorkDone() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Maximum work done by an agent in this iteration");
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Maximum work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Maximum work done by agent in given iteration");

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), iteration.getTotalWorkDone()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnMaxWork() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Maximum work done by an agent in this iteration");
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Maximum work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Maximum work done by agent in given iteration");

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), iteration.getMaxWorkDone()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnMinWork() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Minimum work done by agents in this iteration");
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Minimum work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Minimum work done by agent in given iteration");

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), iteration.getMinimumWorkDone()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnAverageWork() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Average work done by agents in this iteration");
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Average work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("The average of work done by an agent in given iteration");

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), iteration.getAverageWorkDone()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnMedWork() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Median of work done by agents in this iteration");
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Median work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Median of wrok done by agents in given iteration");

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), iteration.getMedianWorkDone()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }


    public void btnAverageLifetime() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Time spent cleaning the map in each iteration of test");
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Time spent in seconds");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Time spent");

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), iteration.getAverageLifeTime()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);
    }

    public void btnTimePerIteration() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Time spent cleaning the map in each iteration of test");
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Time spent in seconds");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Time spent");

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), iteration.getTakenTime()));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);

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
        for(int i = 0; i < chosenIteration.getTakenTime(); i++){
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

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Work done by agents");
        xAxis.setLabel("Agents");
        yAxis.setLabel("Work done");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Work done");

        for(int i = 0; i < chosenIteration.getWorkDone().length; i++) {
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), chosenIteration.getWorkDone()[i]));
        }

        bc.getData().addAll(series1);
        bc.setPrefWidth(3000);
        bc.setPrefHeight(800);


        mainPane.setContent(bc);

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
