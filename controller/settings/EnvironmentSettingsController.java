package controller.settings;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.gui.Popup;
import model.main.Main;
import model.testing.EnvironmentParameters;

public class EnvironmentSettingsController implements Popup {

    @FXML TextField growthRateTf;
    @FXML TextField spawnRateTf;

    EnvironmentParameters envparams = new EnvironmentParameters();

    public void btnSetOnAction() {

        int spawnRate = 0;
        double growthRate = 0;

        try{
            growthRate = Double.parseDouble(growthRateTf.getText());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            popup("Invalid value in growth rate field, please enter a number bigger than 0.");
            return;
        }
        try{
            spawnRate = Integer.parseInt(spawnRateTf.getText());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            popup("Invalid value in growth rate field, please enter a number bigger than 0.");
            return;
        }
        this.envparams.DYNAMIC_HOME_SPAWN_TIME = spawnRate;
        this.envparams.DYNAMIC_HOME_GROWTH_SIZE = growthRate;
        Main.envparams = this.envparams;
    }

}
