package model.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.main.Main;

import java.io.IOException;

public interface NewWindowScene {

    default void createScene(String view){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/" + view + ".fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
           e.printStackTrace();
        }
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }
}