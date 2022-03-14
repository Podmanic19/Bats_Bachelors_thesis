package controller.settings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.main.Main;
import model.testing.EnvironmentParameters;
import model.map.MapParameters;

import java.net.URL;
import java.util.ResourceBundle;

public class MapSettingsController implements Initializable {

    @FXML TextField nameTf;
    @FXML TextField maxCoordTf;
    @FXML TextField minCoordTf;
    @FXML TextField minDistanceTf;
    @FXML TextField minWorkTf;
    @FXML TextField maxWorkTf;
    @FXML TextField numHomesTf;
    @FXML TextField attDistanceTf;
    @FXML TextField numWallsTf;
    @FXML TextField wallLengthMinTf;
    @FXML TextField wallLengthMaxTf;
    @FXML TextField homeSpawnTimeTf;
    @FXML TextField homeGrowthSizeTf;

    MapParameters mapparams;
    EnvironmentParameters envparams;


    public void btnSaveOnAction(){

    }

    public void btnSetOnAction(){

        setParams();
        Main.mapparams = this.mapparams;
        Main.envparams = this.envparams;

    }

    private void setParams(){
        this.mapparams = new MapParameters();
        this.envparams = new EnvironmentParameters();
        this.mapparams.name = nameTf.getText();
        this.mapparams.POINT_MAX = Integer.parseInt(maxCoordTf.getText());
        this.mapparams.POINT_MIN = Integer.parseInt(minCoordTf.getText());
        this.mapparams.MIN_DISTANCE = Integer.parseInt(minDistanceTf.getText());
        this.mapparams.MIN_WORK = Integer.parseInt(minWorkTf.getText());
        this.mapparams.MAX_WORK = Integer.parseInt(maxWorkTf.getText());
        this.mapparams.NUMBER_HOME = Integer.parseInt(numHomesTf.getText());
        this.mapparams.ATTRACTION_DISTANCE = Integer.parseInt(attDistanceTf.getText());
        this.mapparams.WALLS_NUM = Integer.parseInt(numWallsTf.getText());
        this.mapparams.WALL_LENGTH_MIN = Integer.parseInt(wallLengthMinTf.getText());
        this.mapparams.WALL_LENGTH_MAX= Integer.parseInt(wallLengthMaxTf.getText());
        this.envparams.DYNAMIC_HOME_SPAWN_TIME= Integer.parseInt(homeSpawnTimeTf.getText());
        this.envparams.DYNAMIC_HOME_GROWTH_SIZE = Integer.parseInt(homeGrowthSizeTf.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

         nameTf.setText(Main.mapparams.name);
         maxCoordTf.setText(String.valueOf(Main.mapparams.POINT_MAX));
         minCoordTf.setText(String.valueOf(Main.mapparams.POINT_MIN));
         minDistanceTf.setText(String.valueOf(Main.mapparams.MIN_DISTANCE));
         minWorkTf.setText(String.valueOf(Main.mapparams.MIN_WORK));
         maxWorkTf.setText(String.valueOf(Main.mapparams.MAX_WORK));
         numHomesTf.setText(String.valueOf(Main.mapparams.NUMBER_HOME));
         attDistanceTf.setText(String.valueOf(Main.mapparams.ATTRACTION_DISTANCE));
         numWallsTf.setText(String.valueOf(Main.mapparams.WALLS_NUM));
         wallLengthMinTf.setText(String.valueOf(Main.mapparams.WALL_LENGTH_MIN));
         wallLengthMaxTf.setText(String.valueOf(Main.mapparams.WALL_LENGTH_MAX));
         homeSpawnTimeTf.setText(String.valueOf(Main.envparams.DYNAMIC_HOME_SPAWN_TIME));
         homeGrowthSizeTf.setText(String.valueOf(Main.envparams.DYNAMIC_HOME_GROWTH_SIZE));

    }
}
