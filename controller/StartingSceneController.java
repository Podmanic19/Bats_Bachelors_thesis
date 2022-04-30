package controller;


import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.gui.ChangeScene;
import model.gui.Popup;
import model.testing.TestResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.main.Main.primaryStage;

public class StartingSceneController implements ChangeScene, Popup, Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        primaryStage.setUserData(null);
    }

    public void btnRunTestsOnAction()  {
        try {
            sceneChanger("choosetestparams");
        } catch (IOException e) {
            popup("Couldn't load file view/choosetestparams.fxml");
            e.printStackTrace();
        }
    }

    public void btnShowTestResultsOnAction() {

        FileChooser fch = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Test results", "*.test");
        fch.getExtensionFilters().add(fileExtensions);
        File file = fch.showOpenDialog(new Stage());
        TestResult r = null;
        try {
            r = TestResult.load(file);
        } catch (IOException e){
            popup("Couldn't load given test");
            e.printStackTrace();
        }
        if(file == null || r == null) {
            popup("No test selected");
            return;
        }
        try {
            primaryStage.setUserData(r);
            sceneChanger("showtestresult");
        } catch (IOException e) {
            popup("Couldn't load file view/showtestresult.fxml");
            e.printStackTrace();
        }
    }

    public void btnRunVisualisationOnAction() {
        try {
            sceneChanger("visualisation");
        } catch (IOException e) {
            popup("Couldn't load file view/visualisation.fxml");
            e.printStackTrace();
        }
    }

}
