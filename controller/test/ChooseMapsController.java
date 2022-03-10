package controller.test;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.gui.ChangeScene;
import model.gui.Popup;

import java.io.IOException;


public class ChooseMapsController implements ChangeScene, Popup {

    @FXML TextField numMapsTf;
    @FXML TextField numItersTf;
    @FXML CheckBox  generateRandCb;

    public void btnShowOnAction() {

    }

    public void btnMapSettingsOnAction() {

    }

    public void btnNextOnAction() {

        try {
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

}
