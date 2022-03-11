package controller.test;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.agents.AgentParams;
import model.gui.ChangeScene;
import model.gui.Popup;
import model.testing.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static model.main.Main.primaryStage;


public class ChooseMapsController implements ChangeScene, Popup, Initializable {

    @FXML TextField numMapsTf;
    @FXML TextField numItersTf;
    @FXML CheckBox  generateRandCb;

    private Test test;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        test = (Test) primaryStage.getUserData();
    }

    public void btnShowOnAction() {

    }

    public void btnMapSettingsOnAction() {

    }

    public void generateRandomOnAction() {
        numMapsTf.setDisable(!generateRandCb.isSelected());
    }

    public void btnNextOnAction() {

        try {
            sendTest();
            sceneChanger("testrunning");
        } catch (IOException e) {
            popup("Unable to load file 'view/testrunning.fxml");
            e.printStackTrace();
        }

    }

    public void btnPrevOnAction() {
        try {
            sceneChanger("choosetestparams");
        } catch (IOException e) {
            popup("Unable to load file 'view/choosetestparams.fxml");
            e.printStackTrace();
        }
    }

    private boolean checkTest() {

        boolean check = true;


        if(Objects.equals(numMapsTf.getText(), "0")) {
            check = false;
            popup("Invalid number of maps.");
        }

        ArrayList<Map> testedMaps = new ArrayList<AgentParams>();

        for(AgentParams a : mapsTable.getItems()) {
            check = false;
            if(a.SELECTED) testedAgents.add(a);
        }

        if(testedAgents.isEmpty()) {
            check = false;
            popup("No selected agents to test");
        }

        return check;

    }

    private void sendTest() {

        primaryStage.setUserData(test);

    }

}
