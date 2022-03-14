package controller.test;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import model.agents.AgentParams;
import model.gui.ChangeScene;
import model.gui.NewWindowScene;
import model.gui.Popup;
import model.serialization.AgentsManager;
import model.testing.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static model.main.Main.primaryStage;

public class ChooseAgentsAndEnvController implements Initializable, NewWindowScene, ChangeScene, Popup {

    @FXML private TextField spawnTimeTf;
    @FXML private TextField homeGrowthTf;
    @FXML private CheckBox homeSpawningCb;
    @FXML private CheckBox homeGrowthCb;
    @FXML private TableView<AgentParams> agentsTable;
    @FXML private TableColumn<AgentParams, String> nameCol;
    @FXML private TableColumn<AgentParams, Double> forwardCol;
    @FXML private TableColumn<AgentParams, Double> backCol;
    @FXML private TableColumn<AgentParams, Integer> sightCol;
    @FXML private TableColumn<AgentParams, Double> fovCol;
    @FXML private TableColumn<AgentParams, Double> workCol;
    @FXML private TableColumn<AgentParams, Integer> interestCol;
    @FXML private TableColumn<AgentParams, Boolean> avoidCol;
    @FXML private TableColumn<AgentParams, Boolean> decisiveCol;
    @FXML private TableColumn<AgentParams, Boolean> repulseCol;
    @FXML private TableColumn<AgentParams, Boolean> chosenCol;

    private Test test;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableView();
        fillTableView();
        spawnTimeTf.setAlignment(Pos.CENTER);
        homeGrowthTf.setAlignment(Pos.CENTER);
        test = new Test();

    }

    public void btnAddOnAction() {

        createScene("agentsettings");
        agentsTable.getItems().clear();
        fillTableView();

    }

    public void homeGrowthOnAction() {
        homeGrowthTf.setDisable(!homeGrowthCb.isSelected());
    }

    public void homeSpawningOnAction() {
        spawnTimeTf.setDisable(!homeSpawningCb.isSelected());
    }

    public void btnPrevOnAction() {

        try {
            sceneChanger("mainscene");
        } catch (IOException e) {
            popup("Unable to load file 'view/choosetestparams.fxml");
            e.printStackTrace();
        }

    }

    public void btnNextOnAction() {

        try {
            if(!checkTest())
                return;
            sendTest();
            sceneChanger("choosemaps");
        } catch (IOException e) {
            popup("Couldn't load file 'view/choosemaps.fxml'");
            e.printStackTrace();
        }

    }

    private void setTableView() {
        agentsTable.getItems().clear();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        repulseCol.setCellValueFactory(new PropertyValueFactory<>("REPULSIVE_CALL"));
        forwardCol.setCellValueFactory(new PropertyValueFactory<>("FORWARD"));
        backCol.setCellValueFactory(new PropertyValueFactory<>("BACK"));
        sightCol.setCellValueFactory(new PropertyValueFactory<>("SIGHT"));
        fovCol.setCellValueFactory(new PropertyValueFactory<>("FOV"));
        workCol.setCellValueFactory(new PropertyValueFactory<>("WORK_RATE"));
        interestCol.setCellValueFactory(new PropertyValueFactory<>("INTEREST_BOUNDARY"));
        avoidCol.setCellValueFactory(new PropertyValueFactory<>("AVOID_OTHERS"));
        decisiveCol.setCellValueFactory(new PropertyValueFactory<>("DECISIVE"));
        chosenCol.setCellFactory(column -> new CheckBoxTableCell<>());
        chosenCol.setCellValueFactory(cellData -> {
            AgentParams cellValue = cellData.getValue();
            BooleanProperty property = cellValue.getSELECTED();

            property.addListener((observable, oldValue, newValue) ->
                    cellValue.setSELECTED(newValue)
            );

            return property;
        });
    }

    private void fillTableView() {

        try {
            ObservableList<AgentParams> observableAgents = FXCollections.observableArrayList();
            observableAgents.addAll(new AgentsManager().getAgents());
            agentsTable.setItems(observableAgents);
        } catch (IOException e) {
            popup("Couldn't load any agents from agents directory.");
            e.printStackTrace();
        }

    }

    private boolean checkTest() {

        boolean check = true;

        if(!homeGrowthTf.isDisable() && Objects.equals(homeGrowthTf.getText(), "0")) {
            check = false;
            popup("Invalid home growth rate.");
        }

        if(!spawnTimeTf.isDisable() && Objects.equals(spawnTimeTf.getText(), "0")) {
            check = false;
            popup("Invalid spawn time rate.");
        }

        ArrayList<AgentParams> testedAgents = new ArrayList<AgentParams>();

        for(AgentParams a : agentsTable.getItems()) {
            if(a.SELECTED) testedAgents.add(a);
        }

        if(testedAgents.isEmpty()) {
            check = false;
            popup("No selected agents to test.");
        }

        test.setAgentparams(testedAgents);
        return check;

    }

    private void sendTest() {

        primaryStage.setUserData(test);

    }


}
