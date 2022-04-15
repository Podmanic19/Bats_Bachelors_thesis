package controller.test;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.agents.AgentParams;
import model.agents.BatAgent;
import model.gui.ChangeScene;
import model.gui.Popup;
import model.map.Home;
import model.map.mapshell.Map;
import model.map.mapshell.MapShell;
import model.testing.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import static model.main.Main.*;

public class TestRunningController implements Initializable, ChangeScene, Popup {

    @FXML Button btnShow;
    @FXML ProgressBar totalProgressPb;

    TestParams tp;
    Test test;
    TestResult testResult;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tp = (TestParams) primaryStage.getUserData();
        test = new Test(tp);
        test.setOnSucceeded(e -> {
            this.testResult = test.getValue();
            btnShow.setDisable(false);
        });
        Thread t = new Thread(test);
        t.setDaemon(true);
        t.start();
        totalProgressPb.progressProperty().bind(test.progressProperty());

    }

    public void btnMainMenuOnAction() {

        test.cancel();
        Platform.runLater(()-> {
            try {
                sceneChanger("startingscene");
            } catch (IOException e) {
                popup("Unable to load file 'view/startingscene.fxml");
                e.printStackTrace();
            }
        });

    }
    public void btnPrevOnAction() {

        test.cancel();
        Platform.runLater(() -> {
            try {
                sceneChanger("choosemaps");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
                Platform.runLater(() -> {
                    try {
                        popup("File saved successfully");
                        sceneChanger("showtestresult");
                    } catch (IOException e) {
                        popup("Couldn't load file view/showtestresult.fxml");
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                popup("Ran into error while saving file");
                e.printStackTrace();
            }
        }).start();

    }

    private void sendResult() {

        primaryStage.setUserData(testResult);

    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public Test getTest() {
        return test;
    }

    static class Test extends Task<TestResult> {

        private ArrayList<AgentParams> agentParams;
        private ArrayList<MapShell> uninitializedMaps;
        private final EnvironmentParameters envParams;
        private final int numAgents;
        private final int runTime;
        private final int itersPerMap;
        private final boolean singleStart;
        private final int total_work;

        @Override
        protected TestResult call() throws Exception {
            return runTests();
        }

        public Test(TestParams tp) {
            this.agentParams = tp.getAgentparams();
            this.uninitializedMaps = tp.getUninitializedMaps();
            this.envParams = tp.getEnvparams();
            this.numAgents = tp.getNumAgents();
            this.runTime = tp.getRunTime();
            this.itersPerMap = tp.getItersPerMap();
            this.singleStart = tp.isSingleStart();
            this.total_work = agentParams.size() * uninitializedMaps.size() * itersPerMap;
        }

        public TestResult runTests(){

            TestResult result = new TestResult("Test", numAgents, uninitializedMaps, agentParams, envParams);  // cereate test result
            int progress = 0;

            for(int agentIter = 0; agentIter < agentParams.size(); agentIter++) {   // test agent types
                AgentParams currentAgent = agentParams.get(agentIter);
                int hearingDist = currentAgent.HEARING_DISTANCE;
                for(int mapIter = 0; mapIter < uninitializedMaps.size(); mapIter++) {   // over each map
                    MapShell shell = uninitializedMaps.get(mapIter);
                    for (int currentIter = 0; currentIter < itersPerMap; currentIter++) {  // itersPerMap times
                        if(isCancelled()) return null;
                        Map map = new Map(shell, currentAgent, numAgents, singleStart);
                        Statistic s = test(map, currentAgent.NAME, shell.getName(), numAgents, hearingDist);
                        result.update(s);   // update the test result with statistic
                        updateProgress(progress++, total_work); // update progress bar
                    }
                }
            }

            updateProgress(total_work, total_work);

            return result;

        }

        private Statistic test(Map map, String agentName, String mapName, int numAgents, int hearingDist) {
            Statistic s = new Statistic(agentName, mapName, numAgents, hearingDist);
            int j = 0;
            while (notTerminated(j, map.getHomes())) {
                if(isCancelled()) return null;
                map.getAgents().parallelStream().forEach(BatAgent::act);
                map.getHomes().removeIf(h -> (h.getPollution() <= 0));
                for (Home h : map.getHomes()) {
                    h.incrementLifetime();
                    h.increasePollution(envParams.DYNAMIC_HOME_GROWTH_SIZE);
                }
                if (envParams.DYNAMIC_HOME_SPAWN_TIME > 0 && j % envParams.DYNAMIC_HOME_SPAWN_TIME == 0)
                    map.addHome();
                s.updatePollution(map.getHomes());
                s.updateTimeInState(map.getAgentsInState());
                j++;
            }
            System.out.println("Time taken: " + j);
            s.aggregate(j, map.getAgents());
            return s;
        }

        public void setAgentParams(ArrayList<AgentParams> agentParams) {
            this.agentParams = agentParams;
        }

        public void setUninitializedMaps(ArrayList<MapShell> uninitializedMaps) {
            this.uninitializedMaps = uninitializedMaps;
        }

        private boolean notTerminated(int currSecond, ArrayList<Home> homes) {
            if(envParams.DYNAMIC_HOME_SPAWN_TIME > 0) {
                return (currSecond != runTime);
            }
            return currSecond != runTime && !homes.isEmpty();
        }

    }
}
