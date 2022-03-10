package model.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.main.Main;

import java.io.IOException;

public interface ChangeScene {

    default void sceneChanger(String view) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/" + view + ".fxml"));
        Scene scene = new Scene(loader.load());
        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();
    }

}
