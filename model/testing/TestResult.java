package model.testing;

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

    public boolean toCSV(File f) {
        FileWriter myWriter = null;
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
                "Number of agents," +
                "Time taken," +
                "Time searching," +
                "Time travelling," +
                "Time working," +
                "Hearing distance,"
        );

        Statistic example = agentResults.get(0).getMapResults().get(0).getIterations().get(0);
        int numberOfAgents = example.getWorkDone().length;
        int maxTakenTime = getLongestTimeTaken();

        for(int i = 0; i < numberOfAgents; i++) {
            contents.append("Agent ").append(i).append(" work,");
        }

        for(int i = 0; i < maxTakenTime; i++) {
            contents.append("Second ").append(i).append(",");
        }

        contents.deleteCharAt(contents.length()-1);
        contents.append("\n");
        try {
            bufferedWriter.write(new String(contents));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        for(AgentResult ar : agentResults) {
            String agentName = ar.getAgentType().getNAME();
            String hearingDistance = String.valueOf(ar.getAgentType().getHEARING_DISTANCE());
            for(MapResult mr : ar.getMapResults()) {
                String mapName = mr.getMapName();
                for(Statistic s : mr.getIterations()) {
                    try {
                        bufferedWriter.write(s.exportData(agentName, mapName, hearingDistance, maxTakenTime));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int getLongestTimeTaken(){
        int max = 0;
        for(AgentResult ar : agentResults) {
            for (MapResult mr : ar.getMapResults()) {
                for (Statistic s : mr.getIterations()) {
                    if(s.getTakenTime() > max) {
                        max = s.getTakenTime();
                    }
                }
            }
        }
        return max;
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
