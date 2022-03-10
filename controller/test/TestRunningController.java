package controller.test;

import model.gui.ChangeScene;
import model.gui.Popup;

import java.io.IOException;

public class TestRunningController implements ChangeScene, Popup {

    public void btnPrevOnAction() {
        try {
            sceneChanger("choosemaps");
        } catch (IOException e) {
            popup("Unable to load file 'view/choosemaps.fxml");
            e.printStackTrace();
        }
    }

}
