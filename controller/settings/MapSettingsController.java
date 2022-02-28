package controller.settings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.map.MapParameters;

import java.net.URL;
import java.util.ResourceBundle;

import static model.main.Main.envparams;

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
    @FXML CheckBox dynamicHomesCb;

    MapParameters params;


    public void btnSaveOnAction(){

    }

    public void btnSetOnAction(){

        setParams();
        envparams = params;

    }

    private void setParams(){
        params = new MapParameters();
        params.name = nameTf.getText();
        params.POINT_MAX = Integer.parseInt(maxCoordTf.getText());
        params.POINT_MIN = Integer.parseInt(minCoordTf.getText());
        params.MIN_DISTANCE = Integer.parseInt(minDistanceTf.getText());
        params.MIN_WORK = Integer.parseInt(minWorkTf.getText());
        params.MAX_WORK = Integer.parseInt(maxWorkTf.getText());
        params.NUMBER_HOME = Integer.parseInt(numHomesTf.getText());
        params.ATTRACTION_DISTANCE = Integer.parseInt(attDistanceTf.getText());
        params.WALLS_NUM = Integer.parseInt(numWallsTf.getText());
        params.WALL_LENGTH_MIN = Integer.parseInt(wallLengthMinTf.getText());
        params.WALL_LENGTH_MAX= Integer.parseInt(wallLengthMaxTf.getText());
        params.DYNAMIC_HOME_SPAWN_TIME= Integer.parseInt(homeSpawnTimeTf.getText());
        params.DYNAMIC_HOME_GROWTH_SIZE = Integer.parseInt(homeGrowthSizeTf.getText());
        params.DYNAMIC_HOME_CREATION = dynamicHomesCb.isSelected();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

         nameTf.setText(envparams.name);
         maxCoordTf.setText(String.valueOf(envparams.POINT_MAX));
         minCoordTf.setText(String.valueOf(envparams.POINT_MIN));
         minDistanceTf.setText(String.valueOf(envparams.MIN_DISTANCE));
         minWorkTf.setText(String.valueOf(envparams.MIN_WORK));
         maxWorkTf.setText(String.valueOf(envparams.MAX_WORK));
         numHomesTf.setText(String.valueOf(envparams.NUMBER_HOME));
         attDistanceTf.setText(String.valueOf(envparams.ATTRACTION_DISTANCE));
         numWallsTf.setText(String.valueOf(envparams.WALLS_NUM));
         wallLengthMinTf.setText(String.valueOf(envparams.WALL_LENGTH_MIN));
         wallLengthMaxTf.setText(String.valueOf(envparams.WALL_LENGTH_MAX));
         homeSpawnTimeTf.setText(String.valueOf(envparams.DYNAMIC_HOME_SPAWN_TIME));
         homeGrowthSizeTf.setText(String.valueOf(envparams.DYNAMIC_HOME_GROWTH_SIZE));
         dynamicHomesCb.setSelected(envparams.DYNAMIC_HOME_CREATION);

    }
}
