package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import model.agents.AgentParams;

import java.awt.*;

public class AgentSettingsController {

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
    @FXML ComboBox<String> speedDistCB;
    @FXML CheckBox avoidOthersCb;
    @FXML CheckBox repulseCallCb;
    @FXML Button btnSave;
    AgentParams params;


    public void btnSaveOnAction(){
        params = new AgentParams();
        params.name = nameTf.getText();
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

}
