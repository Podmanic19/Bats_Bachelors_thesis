package controller.test;

import controller.PlaceAgents;
import controller.PlaceHomes;
import controller.PlaceWalls;
import javafx.application.Platform;
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
import model.gui.NewWindowScene;
import model.gui.Popup;
import model.map.mapshell.Map;
import model.map.mapshell.MapShell;
import model.testing.TestParams;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import static model.main.Main.*;


public class ChooseMapsController implements ChangeScene, Popup, Initializable, PlaceHomes, PlaceAgents, PlaceWalls, NewWindowScene {

    @FXML Pane previewPane;
    @FXML TableView<MapShell> mapsTable;
    @FXML TableColumn<MapShell, String> nameCol;
    @FXML TableColumn<MapShell, Boolean> chosenCol;
    @FXML TextField numMapsTf;
    @FXML TextField numItersTf;
    @FXML TextField numAgentsTf;
    @FXML TextField numRandomMapsTf;
    @FXML Label generatingMapsLbl;
    @FXML Button generateBtn;
    @FXML Button saveBtn;
    @FXML Button mapSettingsBtn;
    @FXML Button nextBtn;
    @FXML CheckBox generateRandCb;
    @FXML CheckBox singleStartingPosition;

    private TestParams test;
    private Map shownMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        test = (TestParams) primaryStage.getUserData();
        numMapsTf.setAlignment(Pos.CENTER);
        setTableView();
    }

    public void btnShowOnAction() {

        MapShell selected = mapsTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            shownMap = mapFromShell(selected);
            Pane pane = new Pane();
            Stage stage = new Stage();
            Scene scene = new Scene(pane, 1000, 1000);
            stage.setMaxHeight(1029);
            stage.setMaxWidth(1007);
            placeElements(shownMap, pane);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(selected.getName());
            stage.getIcons().add(new Image("/Image/SKYBAT.png"));
            stage.show();
        }
        else {
            popup("No selected map.");
        }

    }

    public void btnGenerateOnAction() {

        generatingMapsLbl.setVisible(true);
        saveBtn.setDisable(true);
        mapSettingsBtn.setDisable(true);
        nextBtn.setDisable(true);
        try{
            int num = Integer.parseInt(numRandomMapsTf.getText());
            if(num <= 0) {
                popup("Desired number of random maps needs to be > 0");
                return;
            }
            new Thread(() -> {
                for(int i = 1; i <= num; i++){
                    MapShell s = new MapShell();
                    s.setName("Map_" + (mapsTable.getItems().size() + 1));
                    Platform.runLater(() -> {
                        mapsTable.getItems().add(s);
                        changeNumMaps(1);
                    });
                }
                Platform.runLater(() -> {
                    generatingMapsLbl.setVisible(false);
                    nextBtn.setDisable(false);
                    saveBtn.setDisable(false);
                    mapSettingsBtn.setDisable(false);
                });
            }).start();

        }
        catch (NumberFormatException e){
            popup("Please set the number of maps as a positive integer");
        }


    }

    public void singleStartingPosition() {
        this.test.setSingleStart(singleStartingPosition.isSelected());;
    }

    public void btnMapSettingsOnAction() {
        createScene("mapsettings");
    }

    public void btnLoadOnAction() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose maps from subdirectory");
        File selectedDirectory = chooser.showDialog(new Stage());
        File[] directoryListing;
        try{
            directoryListing = selectedDirectory.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if(child.getName().contains(".emap")) {
                        MapShell s = MapShell.load(child);
                        if(s == null){
                            popup("Unable to load map from file " + child.getName());
                            return;
                        }
                        mapsTable.getItems().add(s);
                    }
                }
            }
        }
        catch(NullPointerException e) {
            popup("No selected files");
        }

        mapsTable.refresh();
    }

    public void generateRandomOnAction() {
        numRandomMapsTf.setDisable(!generateRandCb.isSelected());
        generateBtn.setDisable(!generateRandCb.isSelected());
        saveBtn.setDisable(!generateRandCb.isSelected());
        mapSettingsBtn.setDisable(!generateRandCb.isSelected());
    }

    public void btnSaveOnAction() {

        if(mapsTable.getItems().isEmpty()) {
            popup("No maps to save.");
            return;
        }

        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());

        for(MapShell s : mapsTable.getItems()){
            try {
                File f = new File("maps\\" + timeStamp);
                if(f.mkdirs() || f.exists()) {
                    s.save(timeStamp);
                }
            } catch (IOException e) {
                popup("Couldn't save maps");
                e.printStackTrace();
                return;
            }
        }
        popup("Maps successfully saved.");
    }

    public void btnNextOnAction() {

        try {
            if(checkTest()) {
                sendTest();
                sceneChanger("testrunning");
            }
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


        try {
            if (Integer.parseInt(numAgentsTf.getText()) <= 0 || Integer.parseInt(numAgentsTf.getText()) > 100) {
                check = false;
                popup("Please set the number of agents between 1 and 100");
            }
            else {
                test.setNumAgents(Integer.parseInt(numAgentsTf.getText()));
            }

            if (Integer.parseInt(numItersTf.getText()) <= 0) {
                check = false;
                popup("Please set the number of iterations > 0");
            }
            else {
                test.setItersPerMap(Integer.parseInt(numItersTf.getText()));
            }

        }
        catch(NumberFormatException e){
            popup("Please fill all fields");
            e.printStackTrace();
        }

        ArrayList<MapShell> testedMaps = new ArrayList<>();

        for(MapShell s : mapsTable.getItems()) {
            if(s.isChosen()) testedMaps.add(s);
        }

        if(testedMaps.isEmpty()) {
            popup("Please select at least one map");
            check = false;
        }
        test.setUninitializedMaps(testedMaps);


        return check;

    }

    private void setTableView() {

        mapsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                shownMap = mapFromShell(mapsTable.getSelectionModel().getSelectedItem());
                showMap();
            }
        });

        mapsTable.getItems().clear();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        chosenCol.setCellFactory(column -> new CheckBoxTableCell<>());
        chosenCol.setCellValueFactory(cellData -> {
            MapShell cellValue = cellData.getValue();
            BooleanProperty property = cellValue.getSELECTED();

            property.addListener(((observable, oldValue, newValue) ->
            {
                cellValue.setSELECTED(newValue);
                changeNumMaps(newValue ? 1 : -1);
            }));
            property.addListener((observable -> {}));

            return property;
        });
    }

    private void showMap() {
        previewPane.getChildren().clear();
        placeElements(shownMap, previewPane);
    }

    private void placeElements(Map map, Pane pane) {
        placeHomes(map.getHomes(), pane);
        placeWalls(map.getWalls(), pane);
        placeAgents(map.getAgents(), pane);
    }

    private void changeNumMaps(int i) {
        int current = Integer.parseInt(numMapsTf.getText());
        numMapsTf.setText(String.valueOf(current + i));
    }

    private void sendTest() {

        primaryStage.setUserData(test);

    }

    private Map mapFromShell(MapShell shell) {
        int numAgents = Integer.parseInt(numAgentsTf.getText());
        return new Map(shell, agentparams, numAgents, singleStartingPosition.isSelected());
    }

}
