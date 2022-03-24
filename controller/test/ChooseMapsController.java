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
import model.gui.Popup;
import model.main.Main;
import model.map.Map;
import model.testing.Test;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import static model.main.Main.*;


public class ChooseMapsController implements ChangeScene, Popup, Initializable, PlaceHomes, PlaceAgents, PlaceWalls {

    @FXML Pane previewPane;
    @FXML TableView<Map> mapsTable;
    @FXML TableColumn<Map, String> nameCol;
    @FXML TableColumn<Map, Boolean> chosenCol;
    @FXML TextField numMapsTf;
    @FXML TextField numItersTf;
    @FXML TextField numAgentsTf;
    @FXML TextField numRandomMapsTf;
    @FXML TextField numHomesTf;
    @FXML Label generatingMapsLbl;
    @FXML Button generateBtn;
    @FXML Button saveBtn;
    @FXML Button mapSettingsBtn;
    @FXML Button nextBtn;
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
            Scene scene = new Scene(pane, 1000, 1000);
            stage.setMaxHeight(1029);
            stage.setMaxWidth(1007);
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
                    Map m = new Map();
                    m.setName("Map_" + (mapsTable.getItems().size()));
                    m.fillWithHomes(Integer.parseInt(numHomesTf.getText()));
                    m.fillWithBats(Integer.parseInt(numAgentsTf.getText()));
                    Platform.runLater(() -> {
                        mapsTable.getItems().add(m);
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

    public void btnMapSettingsOnAction() {

    }

    public void btnLoadOnAction() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose maps from subdirectory");
        File selectedDirectory = chooser.showDialog(new Stage());
        File[] directoryListing = selectedDirectory.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(child.getName().contains(".emap")) {
                    Map m = Map.load(child);
                    if(m == null){
                        popup("Unable to load map from file " + child.getName());
                        return;
                    }
                    m.fillWithHomes(Integer.parseInt(numHomesTf.getText()));
                    m.fillWithBats(Integer.parseInt(numAgentsTf.getText()));
                    mapsTable.getItems().add(m);
                }
            }
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

        for(Map m : mapsTable.getItems()){
            try {
                File f = new File("maps\\" + timeStamp);
                if(f.mkdirs() || f.exists()) {
                    m.save(timeStamp);
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
            if (Objects.equals(numMapsTf.getText(), "0")) {
                check = false;
                popup("Invalid number of maps.");
            }
            else {
                test.setNumMaps(Integer.parseInt(numMapsTf.getText()));
            }

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

            if (Integer.parseInt(numHomesTf.getText()) <= 0) {
                check = false;
                popup("Please set the number of iterations > 0");
            }
            else {
                test.setNumHomes(Integer.parseInt(numHomesTf.getText()));
            }
        }
        catch(NumberFormatException e){
            popup("Please fill all fields");
            e.printStackTrace();
        }

        ArrayList<Map> testedMaps = new ArrayList<>();

        for(Map m : mapsTable.getItems()) {
            if(m.isChosen()) testedMaps.add(m);
        }

        test.setMaps(testedMaps);


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
                changeNumMaps(newValue ? 1 : -1);
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

    private void changeNumMaps(int i) {
        int current = Integer.parseInt(numMapsTf.getText());
        numMapsTf.setText(String.valueOf(current + i));
    }

    private void sendTest() {

        primaryStage.setUserData(test);

    }

}
