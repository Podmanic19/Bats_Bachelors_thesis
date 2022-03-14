package controller.test;

import controller.PlaceAgents;
import controller.PlaceHomes;
import controller.PlaceWalls;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.gui.ChangeScene;
import model.gui.Popup;
import model.main.Main;
import model.map.Map;
import model.testing.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static model.main.Main.loadedMap;
import static model.main.Main.primaryStage;


public class ChooseMapsController implements ChangeScene, Popup, Initializable, PlaceHomes, PlaceAgents, PlaceWalls {

    @FXML Pane previewPane;
    @FXML TableView<Map> mapsTable;
    @FXML TableColumn<Map, String> nameCol;
    @FXML TableColumn<Map, Boolean> chosenCol;
    @FXML TextField numMapsTf;
    @FXML TextField numItersTf;
    @FXML TextField numAgentsTf;
    @FXML TextField numHomesTf;
    @FXML Button generateBtn;
    @FXML Button saveBtn;
    @FXML CheckBox  generateRandCb;

    private Test test;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        test = (Test) primaryStage.getUserData();
        numMapsTf.setAlignment(Pos.CENTER);
        setTableView();
    }

    public void btnShowOnAction() {

        if (mapsTable.getSelectionModel().getSelectedItem() != null) {
            Main.loadedMap = mapsTable.getSelectionModel().getSelectedItem();
            Pane pane = new Pane();
            Stage stage = new Stage();
            stage.setWidth(1000);
            stage.setHeight(1000);
            Scene scene = new Scene(pane, 1000, 1000);
            placeElements(pane);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(loadedMap.getName());
            stage.getIcons().add(new Image("/Image/images.jfif"));
            stage.show();
        }
        else {
            popup("No selected map.");
        }

    }

    public void btnGenerateOnAction() {

    }

    public void btnMapSettingsOnAction() {

    }

    public void btnLoadOnAction() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose maps from subdirectory");
        File selectedDirectory = chooser.showDialog(new Stage());
        File[] directoryListing = selectedDirectory.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(child.getName().contains(".emap")) mapsTable.getItems().add(Map.load(child));
            }
        }
        mapsTable.refresh();
    }

    public void generateRandomOnAction() {
        numMapsTf.setDisable(!generateRandCb.isSelected());
        generateBtn.setDisable(!generateRandCb.isSelected());
        saveBtn.setDisable(!generateRandCb.isSelected());
    }

    public void btnSaveOnAction() {

    }

    public void btnNextOnAction() {

        try {
            sendTest();
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

    private boolean checkTest() {

        boolean check = true;


        if(Objects.equals(numMapsTf.getText(), "0")) {
            check = false;
            popup("Invalid number of maps.");
        }

        ArrayList<Map> testedMaps = new ArrayList<>();

        for(Map m : mapsTable.getItems()) {
            check = false;
            if(m.isChosen()) testedMaps.add(m);
        }

        if(testedMaps.isEmpty()) {
            check = false;
            popup("No selected agents to test");
        }

        return check;

    }

    private void setTableView() {

        mapsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Main.loadedMap = mapsTable.getSelectionModel().getSelectedItem();
                showMap();
            }
        });


        mapsTable.getItems().clear();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        chosenCol.setCellFactory(column -> new CheckBoxTableCell<>());
        chosenCol.setCellValueFactory(cellData -> {
            Map cellValue = cellData.getValue();
            BooleanProperty property = cellValue.getSELECTED();

            property.addListener(((observable, oldValue, newValue) ->
            {
                cellValue.setSELECTED(newValue);
                int current = Integer.parseInt(numMapsTf.getText());
                if(newValue) {
                    numMapsTf.setText(String.valueOf(++current));
                }
                else{
                    numMapsTf.setText(String.valueOf(--current));
                }
            }));
            property.addListener((observable -> {}));

            return property;
        });
    }

    private void showMap() {
        previewPane.getChildren().clear();
        placeElements(previewPane);
    }

    private void placeElements(Pane pane) {
        placeHomes(pane);
        placeWalls(pane);
        placeAgents(pane);
    }

    private void sendTest() {

        primaryStage.setUserData(test);

    }

}
