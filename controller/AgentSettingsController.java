package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.agents.AgentParams;
import model.main.Main;

import java.net.URL;
import java.util.ResourceBundle;


public class AgentSettingsController implements Initializable {

    @FXML TextField nameTf;
    @FXML TextField rightTf;
    @FXML TextField leftTf;
    @FXML TextField backTf;
    @FXML TextField forwardTf;
    @FXML TextField sightTf;
    @FXML TextField fovTf;
    @FXML TextField workRateTf;
    @FXML TextField interestTf;
    @FXML TextField speedMinTf;
    @FXML TextField speedMaxTf;
    @FXML ChoiceBox<SpeedDistribution> speedDistCB;
    @FXML CheckBox avoidOthersCb;
    @FXML CheckBox repulseCallCb;
    @FXML Button btnSave;
    AgentParams params;


    public void btnSaveOnAction(){
        params = new AgentParams();
        params.NAME = nameTf.getText();
        params.RIGHT = Double.parseDouble(rightTf.getText());
        params.LEFT = Double.parseDouble(leftTf.getText());
        params.BACK = Double.parseDouble(backTf.getText());
        params.FORWARD = Double.parseDouble(forwardTf.getText());
        params.SIGHT = Integer.parseInt(sightTf.getText());
        params.FOV = Double.parseDouble(fovTf.getText());
        params.WORK_RATE = Double.parseDouble(workRateTf.getText());
        params.INTEREST_BOUNDARY = Integer.parseInt(interestTf.getText());
        params.SPEED_MIN = Double.parseDouble(speedMinTf.getText());
        params.SPEED_MAX = Double.parseDouble(speedMaxTf.getText());
    }

    public void btnSetOnAction(){
        Main.agentparams = params;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTf.setText(Main.agentparams.NAME);
        rightTf.setText(String.valueOf(Main.agentparams.RIGHT));
        leftTf.setText(String.valueOf(Main.agentparams.LEFT));
        backTf.setText(String.valueOf(Main.agentparams.BACK));
        forwardTf.setText(String.valueOf(Main.agentparams.FORWARD));
        sightTf.setText(String.valueOf(Main.agentparams.SIGHT));
        fovTf.setText(String.valueOf(Main.agentparams.FOV));
        workRateTf.setText(String.valueOf(Main.agentparams.WORK_RATE));
        interestTf.setText(String.valueOf(Main.agentparams.INTEREST_BOUNDARY));
        speedMinTf.setText(String.valueOf(Main.agentparams.SPEED_MIN));
        speedMaxTf.setText(String.valueOf(Main.agentparams.SPEED_MAX));
    }
}
