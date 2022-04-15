package controller.test;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import model.testing.EnvironmentParameters;
import model.testing.TestParams;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static model.main.Main.primaryStage;

public class ChooseAgentsAndEnvController implements Initializable, NewWindowScene, ChangeScene, Popup {

    @FXML private TextField spawnTimeTf;
    @FXML private TextField homeGrowthTf;
    @FXML private TextField simulationLengthTf;
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
    @FXML private TableColumn<AgentParams, Boolean> chosenCol;

    private TestParams test;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableView();
        fillTableView();
        spawnTimeTf.setAlignment(Pos.CENTER);
        homeGrowthTf.setAlignment(Pos.CENTER);
        test = new TestParams();

    }

    public void btnAddOnAction() {

        createScene("agentsettings");
        agentsTable.getItems().clear();
        fillTableView();

    }

    public void btnDeleteOnAction() {

        AgentParams ap = agentsTable.getSelectionModel().getSelectedItem();
        if(ap == null) {
            popup("No agent type selected");
            return;
        }

        agentsTable.getItems().remove(ap);
        try {
            new AgentsManager().delete(ap);
        } catch (IOException e) {
            popup("Couldn't delete given agent type.");
            e.printStackTrace();
        }


    }

    public void btnPrevOnAction() {

        try {
            sceneChanger("startingscene");
        } catch (IOException e) {
            popup("Unable to load file 'view/startingscene.fxml");
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
        hearingCol.setCellValueFactory(new PropertyValueFactory<>("HEARING_DISTANCE"));
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
            agentsTable.getSortOrder().add(nameCol);
            agentsTable.sort();
        } catch (IOException e) {
            popup("Couldn't load any agents from agents directory.");
            e.printStackTrace();
        }

    }

    private boolean checkTest() {

        boolean check = true;
        double growthSize = 0;
        int spawnTime = 0;
        int simLength = 0;

        growthSize = Double.parseDouble(homeGrowthTf.getText());

        try {
            growthSize = Double.parseDouble(homeGrowthTf.getText());
            if(Double.compare(growthSize, 0) < 0) throw new NumberFormatException();
        }
        catch(NumberFormatException e) {
            popup("Invalid growth rate.");
            e.printStackTrace();
        }

        try {
            spawnTime = Integer.parseInt(spawnTimeTf.getText());
            if(spawnTime < 0) throw new NumberFormatException();
        }
        catch(NumberFormatException e) {
            popup("Invalid spawn time rate.");
            e.printStackTrace();
        }

        if(!simulationLengthTf.isDisable()) {
            try {
                simLength = Integer.parseInt(simulationLengthTf.getText());
                if(simLength < 14000) throw new NumberFormatException();
            }
            catch (NumberFormatException e) {
                popup("Number of second for each iteration must be at least 14 000");
                check = false;
            }
        }

        ArrayList<AgentParams> testedAgents = new ArrayList<>();

        for(AgentParams a : agentsTable.getItems()) {
            if(a.SELECTED) testedAgents.add(a);
        }

        if(testedAgents.isEmpty()) {
            check = false;
            popup("No selected agents to test.");
        }

        if(!check) {
            return false;
        }

        test.setRunTime(simLength);
        test.setEnvparams(new EnvironmentParameters(spawnTime,growthSize));
        test.setAgentparams(testedAgents);
        return true;

    }

    private void sendTest() {

        primaryStage.setUserData(test);

    }


}
