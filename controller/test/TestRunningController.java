package controller.test;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import model.gui.ChangeScene;
import model.gui.Popup;
import model.map.Map;
import model.testing.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.main.Main.primaryStage;

public class TestRunningController implements Initializable, ChangeScene, Popup {

    @FXML Label agentLbl;
    @FXML Label mapLbl;
    @FXML Label iterLbl;
    @FXML ProgressBar totalProgressPb;
    @FXML ProgressBar agentProgressPb;
    @FXML ProgressBar mapProgressPb;

    Test test;

    public void btnPrevOnAction() {
        try {
            sceneChanger("choosemaps");
        } catch (IOException e) {
            popup("Unable to load file 'view/choosemaps.fxml");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        test = (Test) primaryStage.getUserData();
        new Thread(() -> {
            test.run(agentLbl, mapLbl, iterLbl, totalProgressPb, agentProgressPb, mapProgressPb);
        }).start();
    }
}
