package model.testing;

import controller.settings.MapSettingsController;
import model.agents.AgentParams;
import model.map.mapshell.MapShell;

import java.io.*;
import java.util.ArrayList;

public class TestResult implements Serializable {

    private String name;
    private int numAgents;
    private boolean singleStart;
    private EnvironmentParameters envParams;
    private ArrayList<MapShell> maps;
    private ArrayList<AgentParams> agentTypes;
    private ArrayList<Statistic> observations= new ArrayList<>();

    public TestResult(String name, int numAgents, ArrayList<MapShell> maps, ArrayList<AgentParams> ap, EnvironmentParameters ep) {
        this.name = name;
        this.maps = maps;
        this.numAgents = numAgents;
        this.agentTypes = ap;
        this.singleStart = ep.SINGLE_POINT_STARTING_LOCATION;
        this.envParams = ep;
    }

    public void update(Statistic s) {

        observations.add(s);

    }

    public void save(File f) throws IOException {

        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        fos.close();

    }

    public static TestResult load(File f) throws IOException {


        TestResult tr;

        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tr = (TestResult) ois.readObject();
            tr.setName(f.getName().replace(".emap", ""));
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
            return null;
        }

        return tr;

    }

    public boolean toCSV(File f) {
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        BufferedWriter bufferedWriter = new BufferedWriter(myWriter);

        StringBuilder contents = new StringBuilder(
                "Agent name," +
                "Map name," +
                "Hearing distance," +
                "Number of agents," +
                "Time taken," +
                "Time searching," +
                "Time travelling," +
                "Time working,"
        );

        int maxTakenTime = getLongestTimeTaken();

        for(int i = 0; i < numAgents; i++) {
            contents.append("Agent ").append(i).append(" work,");
        }

        for(int i = 0; i < maxTakenTime; i++) {
            contents.append(i).append(",");
        }

        contents.deleteCharAt(contents.length()-1);
        contents.append("\n");
        try {
            bufferedWriter.write(new String(contents));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        for(Statistic s : observations) {
            try {
                bufferedWriter.write(s.exportData(maxTakenTime));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private int getLongestTimeTaken(){
        int max = 0;
        for (Statistic s : observations) {
            if(s.getTakenTime() > max) {
                max = s.getTakenTime();
            }
        }
        return max;
    }

    public EnvironmentParameters getEnvParams() {
        return envParams;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getNumAgents() {
        return numAgents;
    }

    public boolean isSingleStart() {
        return singleStart;
    }

    public ArrayList<AgentParams> getAgentTypes() {
        return agentTypes;
    }

    public ArrayList<MapShell> getMaps() {
        return maps;
    }

    public ArrayList<Statistic> getObservations() {
        return observations;
    }
}
