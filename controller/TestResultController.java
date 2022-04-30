package controller;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import model.agents.AgentParams;
import model.gui.Popup;
import model.serialization.AgentsManager;
import model.testing.TestResult;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.main.Main.primaryStage;

public class TestResultController implements Initializable, Popup {

    @FXML private TextField dynamicHomeGrowthTf;
    @FXML private TextField dynamicHomeSpawnTf;
    @FXML private TextField numberOfAgentsTf;
    @FXML private CheckBox singleStartCb;
    @FXML private Label testNameLbl;
    @FXML private TableView<AgentParams> agentsTable;
    @FXML private TableColumn<AgentParams, String> nameCol;
    @FXML private TableColumn<AgentParams, Double> forwardCol;
    @FXML private TableColumn<AgentParams, Double> backCol;
    @FXML private TableColumn<AgentParams, Integer> sightCol;
    @FXML private TableColumn<AgentParams, Double> fovCol;
    @FXML private TableColumn<AgentParams, Integer> hearingCol;
    @FXML private TableColumn<AgentParams, Boolean> avoidCol;
    @FXML private TableColumn<AgentParams, Boolean> decisiveCol;
    @FXML private TableColumn<AgentParams, Boolean> repulseCol;

    private TestResult testResult;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        testResult = (TestResult) primaryStage.getUserData();
        dynamicHomeGrowthTf.setText(String.valueOf(testResult.getEnvParams().DYNAMIC_HOME_GROWTH_SIZE));
        dynamicHomeSpawnTf.setText(String.valueOf(testResult.getEnvParams().DYNAMIC_HOME_SPAWN_TIME));
        singleStartCb.setSelected(testResult.getEnvParams().SINGLE_POINT_STARTING_LOCATION);
        numberOfAgentsTf.setText(String.valueOf(testResult.getNumAgents()));
        testNameLbl.setText(testResult.getName().replace(".test", ""));
        testNameLbl.setAlignment(Pos.CENTER);
        setTableView();
        fillTableView();

    }

    private void setTableView() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        repulseCol.setCellValueFactory(new PropertyValueFactory<>("REPULSIVE_CALL"));
        forwardCol.setCellValueFactory(new PropertyValueFactory<>("FORWARD"));
        backCol.setCellValueFactory(new PropertyValueFactory<>("BACK"));
        sightCol.setCellValueFactory(new PropertyValueFactory<>("SIGHT"));
        fovCol.setCellValueFactory(new PropertyValueFactory<>("FOV"));
        hearingCol.setCellValueFactory(new PropertyValueFactory<>("HEARING_DISTANCE"));
        avoidCol.setCellValueFactory(new PropertyValueFactory<>("AVOID_OTHERS"));
        decisiveCol.setCellValueFactory(new PropertyValueFactory<>("DECISIVE"));
    }

    private void fillTableView() {

        ObservableList<AgentParams> observableAgents = FXCollections.observableArrayList();
        observableAgents.addAll(testResult.getAgentTypes());
        agentsTable.setItems(observableAgents);
        agentsTable.getSortOrder().add(nameCol);
        agentsTable.sort();


    }

}
