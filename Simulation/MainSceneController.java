package Simulation;

import Main.LoadToPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainSceneController implements LoadToPane {
    @FXML Button btnCreate;
    @FXML Button btnEnvSettings;
    @FXML Button btnAgentSettings;
    @FXML Button btnLoadEnv;
    @FXML Pane paneMain;

    public void btnCreateEnv(){

    }

    public void btnEnvSettings() {
        try {
            load(paneMain, "envsettings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnAgentSettings(){
        try {
            load(paneMain, "agentsettings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLoad(){

    }

}
