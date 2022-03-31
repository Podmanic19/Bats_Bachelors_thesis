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
import model.gui.ChangeScene;
import model.gui.Popup;
import model.testing.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static model.main.Main.primaryStage;

public class ShowTestResultController implements Initializable, Popup, ChangeScene {

    private TestResult result;

    @FXML private ScrollPane mainPane;

    @FXML private ComboBox<AgentResult> agentTypeCb;
    @FXML private ComboBox<MapResult> mapNameCb;
    @FXML private ComboBox<Statistic> iterationCb;

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

    public void btnMainMenuOnAction() {
        try {
            sceneChanger("startingscene");
        } catch (IOException e) {
            popup("Couldn't load file view/startingscene.fxml");
            e.printStackTrace();
        }
    }

    /***
     * Creates a barchart of the total time spent on each map comparison across agent types
     */
    public void btnTotalTimeComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Total time spent on maps","Total time spent on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getTotalTimeSpent()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }
    /***
     * Creates a barchart of the minimum time spent on each map comparison across agent types
     */
    public void btnMinimumTimeComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Minimum time spent on maps","Minimum time spent on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getMinimumTimeSpent()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }
    /***
     * Creates a barchart of the maximum time spent on each map comparison across agent types
     */
    public void btnMaximumTimeComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Maximum time spent on maps","Maximum time spent on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getMaximumTimeSpent()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }

    /***
     * Creates a barchart of the average time spent on each map comparison across agent types
     */
    public void btnAverageTimeComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Average time spent on maps","Average time spent on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getAverageTimeSpent()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }

    /***
     * Creates a barchart of the median time spent on each map comparison across agent types
     */
    public void btnMedianTimeComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Median time spent on maps","Median time spent on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getMedianTimeSpent()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }

    /***
     * Creates a barchart of the total work done on each map comparison across agent types
     */
    public void btnTotalWorkComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Total work done on maps","Total work done on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getTotalWorkDone()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }
    /***
     * Creates a barchart of the minimum work done on each map comparison across agent types
     */
    public void btnMinimumWorkComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Minimum work done on maps","Minimum work done on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getMinimumWorkDone()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }
    /***
     * Creates a barchart of the maximum total work done on each map comparison across agent types
     */
    public void btnMaximumWorkComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Maximum work done on maps","Maximum work done on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getMaxWorkDone()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }
    /***
     * Creates a barchart of the average work done on each map comparison across agent types
     */
    public void btnAverageWorkComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Average work done on maps","Average done on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getAverageWorkDone()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }
    /***
     * Creates a barchart of the median work done on each map comparison across agent types
     */
    public void btnMedianWorkComparison() {
        BarChart<String,Number> bc =
                createBarChart("Agent", "Median work done on maps","Median work done on each map by given agent comparison");

        int size = result.getAgentResults().get(0).getMapResults().size();
        XYChart.Series<String,Number>[] mapSeries = new XYChart.Series[size];
        for(int i = 0; i < size; i++) {
            mapSeries[i] = new XYChart.Series<>();
        }

        for(AgentResult a : result.getAgentResults()) {
            int i = 0;
            for(MapResult m : a.getMapResults()) {
                mapSeries[i++].getData().add(new XYChart.Data<>(a.getAgentType(), m.getMedianWorkDone()));
            }
        }

        bc.getData().addAll(mapSeries);

        mainPane.setContent(bc);
    }

    /***
     * Creates a barchart of the maximum time spent on each map by given agent type
     */
    public void btnTotalTimeSpent() {

        BarChart<String,Number> bc =
                createBarChart("Map", "Total time spent","Total time spent on each map by given agent");

        ArrayList<Integer> xAxis = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            xAxis.add(map.getTotalTimeSpent());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), xAxis);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of the maximum time spent on each map by given agent type
     */
    public void btnMaximumTimeSpent() {

        BarChart<String,Number> bc =
                createBarChart("Map", "Maximum time spent","Maximum time spent on each map by given agent");

        ArrayList<Integer> xAxis = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            xAxis.add(map.getMaximumTimeSpent());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), xAxis);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of the minimum time spent on each map by given agent type
     */
    public void btnMinimumTimeSpent() {

        BarChart<String,Number> bc =
                createBarChart("Map", "Minimum time spent","Minimum time spent on each map by given agent");

        ArrayList<Integer> xAxis = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            xAxis.add(map.getMinimumTimeSpent());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), xAxis);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of the average time spent on each map by given agent type
     */
    public void btnAverageTimeSpent() {

        BarChart<String,Number> bc =
                createBarChart("Map", "Median time spent","Average time spent on each map by given agent");

        ArrayList<Double> xAxis = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            xAxis.add(map.getAverageTimeSpent());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), xAxis);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of the median time spent on each map by given agent type
     */
    public void btnMedianTimeSpent() {

        BarChart<String,Number> bc =
                createBarChart("Map", "Median time spent","Median of time spent on each map by given agent");

        ArrayList<Double> xAxis = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            xAxis.add(map.getMedianTimeSpent());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), xAxis);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of the total work done on each map by given agent type
     */
    public void btnTotalWorkOnMap() {

        BarChart<String,Number> bc =
                createBarChart("Map", "Total work done","Average work done by agents on maps");

        ArrayList<Double> xAxis = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            xAxis.add(map.getTotalWorkDone());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), xAxis);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of the averages of work done on each map by given agent type
     */
    public void btnAverageWorkDoneOnMap() {

        BarChart<String,Number> bc =
                createBarChart("Map", "Average work done","Average work done by agents on maps");

        ArrayList<Double> yAxis = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            yAxis.add(map.getAverageWorkDone());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), yAxis);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of medians of work done on each map by given agent type
     */
    public void btnMedianWorkDoneOnMap() {
        BarChart<String,Number> bc =
                createBarChart("Map", "Median work done","Median work done by agents on maps");

        ArrayList<Double> medianWork = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            medianWork.add(map.getMedianWorkDone());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), medianWork);
        bc.getData().addAll(series);

        mainPane.setContent(bc);
    }

    /***
     * Creates a barchart of medians of work done on each map by given agent type
     */
    public void btnMaximumWorkDoneOnMap() {
        BarChart<String,Number> bc =
                createBarChart("Map", "Maximum work done","Maximum work done by agents on maps");

        ArrayList<Double> medianWork = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            medianWork.add(map.getMaxWorkDone());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), medianWork);
        bc.getData().addAll(series);

        mainPane.setContent(bc);
    }

    public void btnMinimumWorkDoneOnMap() {
        BarChart<String,Number> bc =
                createBarChart("Map", "Minimum work done","Minimum work done by agents on maps");

        ArrayList<Double> medianWork = new ArrayList<>();

        for(int i = 0; i < chosenAgent.getMapResults().size(); i++) {
            MapResult map = chosenAgent.getMapResults().get(i);
            medianWork.add(map.getMinimumWorkDone());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), medianWork);
        bc.getData().addAll(series);

        mainPane.setContent(bc);
    }

    /***
     * Creates a barchart of total work done on a map over each iteration
     */
    public void btnTotalWorkDone() {

        BarChart<String,Number> bc =
                createBarChart("Iteration", "Total work done","Total work done by agents in this iteration");

        ArrayList<Double> totalWork = new ArrayList<>();

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            totalWork.add(iteration.getTotalWorkDone());
        }

        XYChart.Series series = createSeries(createListOfMapNames(), totalWork);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of maximal work done by agents over each iteration on given map
     */
    public void btnMaxWork() {

        BarChart<String,Number> bc =
                createBarChart("Iteration", "Maximum work done","Maximum work done by agents in this iteration");

        ArrayList<Double> maxWork = new ArrayList<>();

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            maxWork.add(iteration.getMaxWorkDone());
        }

        XYChart.Series series = createSeries(createListOfNums(0, chosenMapResult.getIterations().size()), maxWork);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }
    /***
     * Creates a barchart of minimal work done by agents over each iteration on given map
     */
    public void btnMinWork() {

        BarChart<String,Number> bc =
                createBarChart("Iteration", "Minimum work done","Minimum work done by agents in this iteration");

        ArrayList<Double> minWork = new ArrayList<>();

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            minWork.add(iteration.getMinimumWorkDone());
        }

        XYChart.Series series = createSeries(createListOfNums(0, chosenMapResult.getIterations().size()), minWork);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of average work done by agents over each iteration on given map
     */
    public void btnAverageWork() {

        BarChart<String,Number> bc =
                createBarChart("Iteration", "Average work done", "Average work done by agents in this iteration");

        ArrayList<Double> averageWorkDone = new ArrayList<>();

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            averageWorkDone.add(iteration.getAverageWorkDone());
        }

        XYChart.Series series = createSeries(createListOfNums(0,chosenMapResult.getIterations().size()), averageWorkDone);

        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    /***
     * Creates a barchart of medians of work done by agents over each iteration over given map
     */
    public void btnMedWork() {
        BarChart<String,Number> bc =
                createBarChart("Iteration", "Median work done", "Median of work done by agents in this iteration");

        ArrayList<Double> medWorkDone = new ArrayList<>();
        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            medWorkDone.add(iteration.getMedianWorkDone());
        }

        XYChart.Series series = createSeries(createListOfNums(0, chosenMapResult.getIterations().size()), medWorkDone);

        bc.getData().addAll(series);

        mainPane.setContent(bc);
    }


    /***
     * shows the average lifetime of a home in each iteration over given map
     */
    public void btnAverageLifetime() {
        BarChart<String,Number> bc = createBarChart("Iteration", "Time spent in seconds", "Time spent cleaning the map in each iteration of test");

        ArrayList<Double> lifetimes = new ArrayList<>();

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            lifetimes.add(iteration.getAverageLifeTime());
        }

        XYChart.Series series = createSeries(createListOfNums(0, chosenMapResult.getIterations().size()), lifetimes);
        bc.getData().addAll(series);

        mainPane.setContent(bc);
    }

    /***
     * Creates a barchart of times taken to clean up a map by each iteration over given map
     */
    public void btnTimePerIteration() {

        BarChart<String,Number> bc =
                createBarChart("Iteration", "Time spent in seconds", "Time spent cleaning the map in each iteration of test");

        ArrayList<Integer> iterationTimes = new ArrayList<>();

        for(int i = 0; i < chosenMapResult.getIterations().size(); i++) {
            Statistic iteration = chosenMapResult.getIterations().get(i);
            iterationTimes.add(iteration.getTakenTime());
        }

        XYChart.Series series = createSeries(createListOfNums(0, chosenMapResult.getIterations().size()), iterationTimes);
        bc.getData().addAll(series);

        mainPane.setContent(bc);

    }

    public void btnShowsStatesOnAction() {

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Working", chosenIteration.getTimeWorking()),
                        new PieChart.Data("Searching", chosenIteration.getTimeSearching()),
                        new PieChart.Data("Travelling", chosenIteration.getTimeTravelling()));
        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Time spent by agents in given states");

        chart.setPrefHeight(800);
        chart.setPrefWidth(800);

        mainPane.setContent(chart);
    }

    /***
     * Craetes a linechart of pollution progression in given iteration over map
     */
    public void btnShowPollutionOnAction() {

        LineChart<Number, Number> lChart =
                createLineChart("Seconds", "Total pollution", "Pollution progression");
        XYChart.Series series = createSeries(createListOfNums(0, chosenIteration.getTakenTime()), chosenIteration.getTotalPollution());
        lChart.getData().add(series);
        mainPane.setContent(lChart);
    }

    private BarChart<String,Number> createBarChart(String xLabel, String yLabel, String title) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);

        bc.setTitle(title);

        bc.setPrefHeight(800);
        bc.setPrefWidth(800);


        return bc;
    }

    private LineChart<Number, Number> createLineChart(String xLabel, String yLabel, String title) {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle(title);

        lineChart.setPrefHeight(800);
        lineChart.setPrefWidth(800);


        return lineChart;

    }

    private XYChart.Series createSeries(List xAxis, List yAxis) {
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i < xAxis.size(); i++){
            series.getData().add(new XYChart.Data(xAxis.get(i),yAxis.get(i)));
        }

        return series;
    }

    private ArrayList<String> createListOfMapNames() {
        ArrayList<String> mapNames = new ArrayList<>();
        for(MapResult m : chosenAgent.getMapResults()) {
            mapNames.add(m.getMapName());
        }
        return mapNames;
    }

    private ArrayList<String> createListOAgentNames(ArrayList<AgentResult> results) {
        ArrayList<String> agentNames = new ArrayList<>();
        for(AgentResult a : results) {
            agentNames.add(a.getAgentType());
        }
        return agentNames;
    }

    private ArrayList<Integer> createListOfNums(int start, int end) {
        ArrayList<Integer> listOfNums = new ArrayList<>();
        for(int i = start; i < end; i++) {
            listOfNums.add(i);
        }
        return listOfNums;
    }

    public void agentTypeOnAction() {

        chosenAgent = agentTypeCb.getValue();

        if (chosenAgent == null) {
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
            iterationCb.setValue(null);
            iterationCb.setDisable(true);
            return;
        }

        initIterationsComboBox();

    }

    public void iterNumOnAction() {

        chosenIteration = iterationCb.getValue();

        if(chosenIteration == null) {
            return;
        }

        int iterationNumber = iterationCb.getItems().indexOf(chosenIteration);
        chosenIteration = chosenMapResult.getIterations().get(iterationNumber);

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
