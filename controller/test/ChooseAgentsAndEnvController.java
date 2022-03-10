package controller.test;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import model.agents.AgentParams;
import model.agents.SpeedDistribution;
import model.gui.ChangeScene;
import model.gui.Popup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static model.main.Main.agentparams;

public class ChooseAgentsAndEnvController implements Initializable, ChangeScene, Popup {

    @FXML private TextField spawnTimeTf;
    @FXML private TextField homeGrowthTf;
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
    @FXML private TableColumn<AgentParams, SpeedDistribution> speedCol;

    ArrayList<AgentParams> agentTypes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableView();
        agentsTable.getItems().add(agentparams);

    }

    public void btnAddOnAction() {



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
        decisiveCol.setCellValueFactory(new PropertyValueFactory<>("REPULSIVE_CALL"));
        chosenCol.setCellFactory(column -> new CheckBoxTableCell<>());
        chosenCol.setCellValueFactory(cellData -> {
            AgentParams cellValue = cellData.getValue();
            BooleanProperty property = cellValue.getSelected();

            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> cellValue.setSelected(newValue));

            return property;
        });

    }

}
