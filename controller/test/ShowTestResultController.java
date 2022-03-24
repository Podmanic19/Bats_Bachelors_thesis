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
import java.util.ArrayList;
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

    }

    public void btnShowsStatesOnAction() {

        Statistic s = result.getAgentResults().get(0).getMapResults().get(0).getIterations().get(0);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Working", s.getTimeWorking()),
                        new PieChart.Data("Searching", s.getTimeSearching()),
                        new PieChart.Data("Travelling", s.getTimeTravelling()));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Time spent by agents in given states");

        chart.setPrefHeight(800);
        chart.setPrefWidth(800);


    }

    public void btnSearchingOnAction() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");

        ArrayList<XYChart.Series> mapIterations = new ArrayList<>();

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Searching");

        int numIters = result.getAgentResults().get(0).getMapResults().get(0).getIterations().size();
        for(int i = 0; i < numIters; i++) {
            Statistic s = result.getAgentResults().get(0).getMapResults().get(0).getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), s.getTimeSearching()));
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

        ArrayList<XYChart.Series> mapIterations = new ArrayList<>();

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Searching");

        int numIters = result.getAgentResults().get(0).getMapResults().get(0).getIterations().size();
        for(int i = 0; i < numIters; i++) {
            Statistic s = result.getAgentResults().get(0).getMapResults().get(0).getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), s.getTimeTravelling()));
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

        ArrayList<XYChart.Series> mapIterations = new ArrayList<>();

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Searching");

        int numIters = result.getAgentResults().get(0).getMapResults().get(0).getIterations().size();
        for(int i = 0; i < numIters; i++) {
            Statistic s = result.getAgentResults().get(0).getMapResults().get(0).getIterations().get(i);
            series1.getData().add(new XYChart.Data(String.valueOf(i+1), s.getTimeWorking()));
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
        Statistic s = result.getAgentResults().get(0).getMapResults().get(0).getIterations().get(0);
        for(int i = 0; i < s.getNumSeconds(); i++){
            series.getData().add(new XYChart.Data(i, s.getTotalPollution().get(i)));
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
    }

    public void agentTypeOnAction() {

        chosenAgent = agentTypeCb.getValue();

        if (chosenAgent == null) {
            agentTypeLbl.setText("UNSELECTED");
            mapNameCb.setValue(null);
            iterationCb.setValue(null);
            return;
        }

        chosenMapResult = chosenAgent.getMapResults().get(0);
        mapNameCb.setValue(chosenMapResult);

        chosenIteration = chosenMapResult.getIterations().get(0);
        iterationCb.setValue(chosenIteration);

    }

    public void mapNameOnAction() {

        chosenMapResult = mapNameCb.getValue();

        if (chosenMapResult == null) {
            mapNameLbl.setText("UNSELECTED");
            iterationCb.setValue(null);
            return;
        }

        chosenIteration = chosenMapResult.getIterations().get(0);
        iterationCb.setValue(chosenIteration);

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

}
