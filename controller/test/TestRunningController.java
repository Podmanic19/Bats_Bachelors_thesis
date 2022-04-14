package controller.test;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.agents.AgentParams;
import model.agents.BatAgent;
import model.gui.ChangeScene;
import model.gui.Popup;
import model.main.Main;
import model.map.Home;
import model.map.mapshell.Map;
import model.map.mapshell.MapShell;
import model.testing.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import static model.main.Main.*;

public class TestRunningController implements Initializable, ChangeScene, Popup {

    @FXML Button btnShow;
    @FXML Label agentLbl;
    @FXML Label mapLbl;
    @FXML Label iterLbl;
    @FXML ProgressBar totalProgressPb;
    @FXML ProgressBar agentProgressPb;
    @FXML ProgressBar mapProgressPb;

    TestParams tp;
    Test test;
    TestResult testResult;

    public void btnPrevOnAction() {
        try {
            sceneChanger("choosemaps");
            test.cancel();
        } catch (IOException e) {
            popup("Unable to load file 'view/choosemaps.fxml");
            e.printStackTrace();
        }
    }

    public void btnShowOnAction() {

        FileChooser fch = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Test Results", "*.test");
        fch.getExtensionFilters().add(fileExtensions);
        File file = fch.showSaveDialog(new Stage());
        if (file == null) {
            popup("No valid file selected");
            return;
        }
        sendResult();
        new Thread(() -> {
            try {
                testResult.save(file);
            } catch (IOException e) {
                popup("Ran into error while saving file");
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                popup("File saved successfully");
            });
        }).start();


        try {
            sceneChanger("showtestresult");
        } catch (IOException e) {
            popup("Couldn't load file view/showtestresult.fxml");
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tp = (TestParams) primaryStage.getUserData();
        test = new Test(tp);
        Thread t = new Thread(test);
        t.setDaemon(true);
        t.start();

    }

    private void sendResult() {

        primaryStage.setUserData(testResult);

    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public Label getAgentLbl() {
        return agentLbl;
    }

    public Label getMapLbl() {
        return mapLbl;
    }

    public Label getIterLbl() {
        return iterLbl;
    }

    public ProgressBar getTotalProgressPb() {
        return totalProgressPb;
    }

    public ProgressBar getAgentProgressPb() {
        return agentProgressPb;
    }

    public ProgressBar getMapProgressPb() {
        return mapProgressPb;
    }

    public Test getTest() {
        return test;
    }

    public void setShow(boolean show){
        btnShow.setDisable(!show);
    }

    class Test extends Task {

        private ArrayList<AgentParams> agentparams;
        private ArrayList<MapShell> uninitializedMaps;
        private EnvironmentParameters envparams;
        private int numAgents;
        private int runTime;
        private int itersPerMap;
        private boolean singleStart;

        @Override
        protected TestResult call() throws Exception {
            return test();
        }

        public Test(TestParams tp) {
            this.agentparams = tp.getAgentparams();
            this.uninitializedMaps = tp.getUninitializedMaps();
            this.envparams = tp.getEnvparams();
            this.numAgents = tp.getNumAgents();
            this.runTime = tp.getRunTime();
            this.itersPerMap = tp.getItersPerMap();
            this.singleStart = tp.isSingleStart();
        }

        public TestResult test(){

            Main.envparams = this.envparams;
            TestResult result = new TestResult();
            Instant start = Instant.now();
            int agentIter = -1, mapIter, currentIter;

            //ITERATE OVER ALL AGENT TYPES
            for(AgentParams currentAgentParams : agentparams) {
                ++agentIter;
                int finalAgentIter = agentIter;
//                Platform.runLater(() -> {
//                    agentLbl.setText(currentAgentParams.getNAME());
//                    totalProgressPb.setProgress((double) finalAgentIter /agentparams.size());
//                });
                mapIter = -1;
                AgentResult agentResult = new AgentResult(currentAgentParams);
                //ITERATE OVER MAPS
                for (MapShell shell : uninitializedMaps) {
                    ++mapIter;
                    int finalMapIter = mapIter;
//                    Platform.runLater(() -> {
//                        mapLbl.setText(shell.getName());
//                        agentProgressPb.setProgress((double) finalMapIter / uninitializedMaps.size());
//                    });
                    currentIter = -1;
                    MapResult mapResult = new MapResult(shell.getName());
                    //TEST MAP NUMBER OF ITERATIONS PER MAP TIMES
                    for (int i = 1; i <= itersPerMap; i++) {
                        ++currentIter;
                        int finalCurrentIter = currentIter;
//                        Platform.runLater(() -> {
//                            iterLbl.setText(String.valueOf(finalCurrentIter));
//                            mapProgressPb.setProgress((double) finalCurrentIter / itersPerMap);
//                        });
                        Map testedMap = new Map(shell, currentAgentParams, numAgents, singleStart);
                        int j = -1;
                        Statistic s = new Statistic(String.valueOf(i));
                        Instant iterStart = Instant.now();
                        HashSet<Home> allHomes = new HashSet<>();
                        for(Home h : testedMap.getHomes()) {
                            if(h.getPollution() >= 0) allHomes.add(h);
                        }
                        //SOLVE MAP
                        while (!terminate(++j, testedMap.getHomes())) {
                            if(isCancelled()) return null;
                            testedMap.getAgents().parallelStream().forEach(BatAgent::act);
                            testedMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
                            for (Home h : testedMap.getHomes()) {
                                h.incrementLifetime();
                                h.increasePollution(envparams.DYNAMIC_HOME_GROWTH_SIZE);
                            }
                            if (envparams.DYNAMIC_HOME_SPAWN_TIME > 0 && j % envparams.DYNAMIC_HOME_SPAWN_TIME == 0)
                                testedMap.addHome();
                            s.updatePollution(testedMap.getHomes());
                            s.updateTimeInState(testedMap.getAgentsInState());
                        }
                        allHomes.addAll(testedMap.getHomes());
                        Instant iterEnd = Instant.now();
                        System.out.println("Iteration " + i + " runtime: " + Duration.between(iterStart, iterEnd) +
                                " iterations: " + j);
                        s.aggregate(j, testedMap.getAgents(), allHomes);
                        mapResult.update(s);
                    }
                    mapResult.aggregate();
                    agentResult.update(mapResult);
                }
                result.update(agentResult);
            }

//            Platform.runLater(() -> {
//                totalProgressPb.setProgress(100);
//                agentProgressPb.setProgress(100);
//                mapProgressPb.setProgress(100);
//                iterLbl.setText("100");
//                mapLbl.setText(uninitializedMaps.get(uninitializedMaps.size()-1).getName());
//                agentLbl.setText(agentparams.get(agentparams.size()-1).getNAME());
//            });

            Instant end = Instant.now();
            System.out.println("Total runtime: " + Duration.between(start, end));

            btnShow.setDisable(false);

            return result;

        }

        public void setAgentparams(ArrayList<AgentParams> agentparams) {
            this.agentparams = agentparams;
        }

        public void setUninitializedMaps(ArrayList<MapShell> uninitializedMaps) {
            this.uninitializedMaps = uninitializedMaps;
        }

        public void setNumAgents(int numAgents) {
            this.numAgents = numAgents;
        }

        public void setItersPerMap(int itersPerMap) {
            this.itersPerMap = itersPerMap;
        }

        public void setEnvparams(EnvironmentParameters envparams) {
            this.envparams = envparams;
        }

        public void setRunTime(int runTime) {
            this.runTime = runTime;
        }

        public void setSingleStart(boolean singleStart) {
            this.singleStart = singleStart;
        }

        private boolean terminate(int currSecond, ArrayList<Home> homes) {
            if(envparams.DYNAMIC_HOME_SPAWN_TIME > 0) {
                return (currSecond == runTime);
            }
            return currSecond == runTime || homes.isEmpty();
        }

    }
}
