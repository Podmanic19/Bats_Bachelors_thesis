package controller.test;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.gui.ChangeScene;
import model.gui.Popup;
import model.map.mapshell.Map;
import model.map.mapshell.MapShell;
import model.testing.Test;
import model.testing.TestResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.main.Main.mapparams;
import static model.main.Main.primaryStage;

public class TestRunningController implements Initializable, ChangeScene, Popup {

    @FXML Button btnShow;
    @FXML Label agentLbl;
    @FXML Label mapLbl;
    @FXML Label iterLbl;
    @FXML ProgressBar totalProgressPb;
    @FXML ProgressBar agentProgressPb;
    @FXML ProgressBar mapProgressPb;

    Test test;
    TestResult testResult;

    public void btnPrevOnAction() {
        try {
            sceneChanger("choosemaps");
        } catch (IOException e) {
            popup("Unable to load file 'view/choosemaps.fxml");
            e.printStackTrace();
        }
    }

    public void btnShowOnAction() {


        try {
            FileChooser fch = new FileChooser();
            FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Test Results", "*.test");
            fch.getExtensionFilters().add(fileExtensions);
            File file = fch.showSaveDialog(new Stage());
            if(file == null) {
                popup("No valid file selected");
                return;
            }
            sendResult();
            testResult.save(file);
            popup("File saved successfully");
        } catch (IOException e) {
            popup("Ran into error while saving file");
            e.printStackTrace();
        }

        try {
            sceneChanger("showtestresult");
        } catch (IOException e) {
            popup("Couldn't load file view/showtestresult.fxml");
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        test = (Test) primaryStage.getUserData();

        new Thread(() -> {
            test.run(this);
        }).start();
    }

    private void sendResult() {

        primaryStage.setUserData(testResult);

    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public Label getAgentLbl() {
        return agentLbl;
    }

    public Label getMapLbl() {
        return mapLbl;
    }

    public Label getIterLbl() {
        return iterLbl;
    }

    public ProgressBar getTotalProgressPb() {
        return totalProgressPb;
    }

    public ProgressBar getAgentProgressPb() {
        return agentProgressPb;
    }

    public ProgressBar getMapProgressPb() {
        return mapProgressPb;
    }

    public Test getTest() {
        return test;
    }

    public void setShow(boolean show){
        btnShow.setDisable(!show);
    }
}
