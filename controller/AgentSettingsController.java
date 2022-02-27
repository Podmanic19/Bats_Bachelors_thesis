package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.agents.AgentParams;
import model.gui.IAlert;

import java.net.URL;
import java.util.ResourceBundle;

import static model.main.Main.agentparams;


public class AgentSettingsController implements Initializable, IAlert {

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
        try{
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
            params.SPEED_TYPE = speedDistCB.getValue();
            params.AVOID_OTHERS = avoidOthersCb.isSelected();
            params.REPULSIVE_CALL = repulseCallCb.isSelected();
        }
        catch (Exception e){
            e.printStackTrace();
            popup("Please make sure all fields are filled in correctly.");
        }
    }

    public void btnSetOnAction(){
        if(nameTf.getText().isEmpty() || rightTf.getText().isEmpty() || leftTf.getText().isEmpty() ||
        backTf.getText().isEmpty() || forwardTf.getText().isEmpty() || sightTf.getText().isEmpty() ||
        fovTf.getText().isEmpty() || workRateTf.getText().isEmpty() || interestTf.getText().isEmpty() ||
        speedMinTf.getText().isEmpty() || speedMaxTf.getText().isEmpty() || speedDistCB.getValue() == null){
            popup("All fields must be filled in.");
        }
        agentparams = params;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTf.setText(agentparams.NAME);
        rightTf.setText(String.valueOf(agentparams.RIGHT));
        leftTf.setText(String.valueOf(agentparams.LEFT));
        backTf.setText(String.valueOf(agentparams.BACK));
        forwardTf.setText(String.valueOf(agentparams.FORWARD));
        sightTf.setText(String.valueOf(agentparams.SIGHT));
        fovTf.setText(String.valueOf(agentparams.FOV));
        workRateTf.setText(String.valueOf(agentparams.WORK_RATE));
        interestTf.setText(String.valueOf(agentparams.INTEREST_BOUNDARY));
        speedMinTf.setText(String.valueOf(agentparams.SPEED_MIN));
        speedMaxTf.setText(String.valueOf(agentparams.SPEED_MAX));
        avoidOthersCb.setSelected(agentparams.AVOID_OTHERS);
        repulseCallCb.setSelected(agentparams.REPULSIVE_CALL);
        ObservableList<SpeedDistribution> priorities = FXCollections.observableArrayList(SpeedDistribution.values());
        speedDistCB.getItems().setAll(priorities);
        speedDistCB.setValue(SpeedDistribution.GAUSSIAN);
    }
}
