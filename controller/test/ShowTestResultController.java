package controller.test;

import controller.PlaceAgents;
import controller.PlaceHomes;
import controller.PlaceWalls;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.gui.ChangeScene;
import model.gui.LoadToPane;
import model.gui.Popup;
import model.map.mapshell.Map;
import model.map.mapshell.MapShell;
import model.testing.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import static model.main.Main.*;

public class ShowTestResultController implements Initializable, Popup, ChangeScene, PlaceWalls, PlaceAgents, PlaceHomes, LoadToPane {

    private TestResult result;

    @FXML private Pane mainPane;
    @FXML private ComboBox<MapShell> mapCb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        result = (TestResult) primaryStage.getUserData();
        try {
            load(mainPane, "testresult");
        } catch (IOException e) {
            popup("Coulnd't load file 'view/testresult.fxml'");
            e.printStackTrace();
        }
        initMapComboBox();
    }

    public void saveOnAction() {
        Node n = mainPane;
        FileChooser fch = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Graph png", "*.png");
        fch.getExtensionFilters().add(fileExtensions);
        File file = fch.showSaveDialog(new Stage());
        if(file == null) {
            popup("No valid file selected");
            return;
        }
        saveAsPNG(n, file);
    }


    private void saveAsPNG(Node n, File file) {
        WritableImage image = n.snapshot(new SnapshotParameters(), null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            popup("File saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void csvOnAction() {
        FileChooser fch = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Test Results", "*.csv");
        fch.getExtensionFilters().add(fileExtensions);
        File file = fch.showSaveDialog(new Stage());
        if(file == null) {
            Platform.runLater(() -> popup("No valid file selected"));
            return;
        }

        AtomicBoolean success = new AtomicBoolean(false);
        new Thread(() -> {
            success.set(result.toCSV(file));
            Platform.runLater(() -> {
                popup("File saved successfully");
            });
        }).start();
    }

    public void btnMainMenuOnAction() {
        try {
            sceneChanger("startingscene");
        } catch (IOException e) {
            popup("Couldn't load file view/startingscene.fxml");
            e.printStackTrace();
        }
    }

    public void btnShowTestOnAction() {
        mainPane.getChildren().clear();
        try {
            load(mainPane, "testresult");
        } catch (IOException e) {
            popup("Couldn't load file view/testresult.fxml");
            e.printStackTrace();
        }

    }

    public void btnShowMapOnAction() {
        mainPane.getChildren().clear();
        MapShell shell = mapCb.getValue();
        Map m = new Map(shell, result.getAgentTypes().get(0), result.getNumAgents(), result.isSingleStart());
        placeHomes(m.getHomes(), mainPane);
        placeAgents(m.getAgents(), mainPane);
        placeWalls(m.getWalls(), mainPane);
    }

    private void initMapComboBox() {

        ObservableList<MapShell> agentTypes = FXCollections.observableArrayList(result.getMaps());

        mapCb.getItems().setAll(agentTypes);

    }

}
