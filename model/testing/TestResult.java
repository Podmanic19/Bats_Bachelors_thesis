package model.testing;

import model.map.mapshell.MapShell;

import java.io.*;
import java.util.ArrayList;

public class TestResult implements Aggregable, Serializable {

    ArrayList<AgentResult> agentResults = new ArrayList<>();

    public void update(AgentResult a) {

        agentResults.add(a);

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
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
            return null;
        }

        return tr;

    }

    @Override
    public double getAverageWorkDone() {
        return 0;
    }

    @Override
    public double getMaxWorkDone() {
        return 0;
    }

    @Override
    public double getMinimumWorkDone() {
        return 0;
    }

    @Override
    public int getTakenTime() {
        return 0;
    }

    public ArrayList<AgentResult> getAgentResults() {
        return agentResults;
    }
}
