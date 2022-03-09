package controller.test;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.agents.BatAgent;

import java.util.Date;

public class ChooseAgentsAndEnvController {

    @FXML private TextField spawnTimeTf;
    @FXML private TextField homeGrowthTf;
    @FXML private TableView<BatAgent> agentsTable;
    @FXML private TableColumn<BatAgent, String> nameCol;
    @FXML private TableColumn<BatAgent, Boolean> repulseCol;
    @FXML private TableColumn<BatAgent, Integer> forwardCol;
    @FXML private TableColumn<BatAgent, Integer> backCol;
    @FXML private TableColumn<BatAgent, Integer> sightCol;
    @FXML private TableColumn<BatAgent, Double> fovCol;
    @FXML private TableColumn<BatAgent, Double> workCol;
    @FXML private TableColumn<BatAgent, Integer> interestCol;
    @FXML private TableColumn<BatAgent, Boolean> avoidCol;
    @FXML private TableColumn<BatAgent, Boolean> decisiveCol;
    @FXML private TableColumn<BatAgent, Date> speedCol;
    @FXML private TableColumn<BatAgent, CheckBox> chosenCol;

    public void btnAddOnAction() {

    }

    public void btnPrevOnAction() {

    }

    public void btnNextOnAction() {

    }

}
