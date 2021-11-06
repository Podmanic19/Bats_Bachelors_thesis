package Environment;

import Classes.Agent;
import Classes.AgentParams;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static Classes.State.*;

public class EnvironmentMap {
    private EnvironmentParameters envparams;
    private AgentParams agentparams;
    private Random generator;
    private ArrayList<Agent> agents = new ArrayList<>();
    private ArrayList<Vector> walls = new ArrayList<>();
    private ArrayList<Home> homes = new ArrayList<>();
    private static EnvironmentMap instance = null;

    private EnvironmentMap(int seed)
    {
        envparams = new EnvironmentParameters();
        generator = new Random(seed);
        placeHomes();
        placeAgents();
    }

    public static EnvironmentMap getInstance()
    {
        if (instance == null)
            instance = new EnvironmentMap(0);

        return instance;
    }

    public static EnvironmentMap getInstance(int seed)
    {
        if (instance == null)
            instance = new EnvironmentMap(5);

        return instance;
    }

    private void placeHomes(){

        ArrayList<Coordinate> flatMap = flatten();
        HashSet<Integer> possibleHomes = fillPossibleHomes(flatMap);
        int homeID = 1;
        Collections.shuffle(flatMap);

        for(int i = 0; i < flatMap.size(); i++){
            if(homes.size() == envparams.NUMBER_HOME) break;
            if(possibleHomes.isEmpty()) break;

            if(!possibleHomes.contains(i)) continue;

            createHome(flatMap.get(i), homeID++);
            possibleHomes.remove(i);

            for(int j = 0; j < flatMap.size(); j++){
                boolean remove = false;
                if(!possibleHomes.contains(j)) continue;
                for(Home home: homes){
                    if(home.getCoords().cheapDistanceTo(flatMap.get(j)) < (int) Math.pow(envparams.MIN_DISTANCE, 2)) {
                        remove = true;
                        break;
                    }
                }
                if(remove) possibleHomes.remove(j);
            }
        }
    }

    private HashSet<Integer> fillPossibleHomes(ArrayList<Coordinate> flatMap){

        HashSet<Integer> possibleHomes = new HashSet<>();

        for(int i = 0; i < flatMap.size(); i++){
            //if(flatMap.get(i).isWall()) continue;
            possibleHomes.add(i);
        }

        return possibleHomes;

    }

    private void createHome(Coordinate coord, int id){

        int workNeeded = ThreadLocalRandom.current().nextInt(envparams.MIN_WORK, envparams.MAX_WORK + 1);

        homes.add(new Home(id, workNeeded, coord));

    }

    private void generateAgents(){
        agents = new ArrayList<>();

        for(int i = 0; i < envparams.AGENT_NUM; i++){
            agents.add(new Agent(i + 1, generateRandPos()));
        }
    }

    private boolean generateWall(int x, int y){
        if(x == 0 || x == envparams.POINT_MAX) return true;
        if(y == 0 || y == envparams.POINT_MAX) return true;
        return false;
    }

    private void placeAgents(){
        generateAgents();
    }

    private Coordinate generateRandPos(){

        int x = ThreadLocalRandom.current().nextInt(envparams.POINT_MIN, envparams.POINT_MAX + 1);
        int y = ThreadLocalRandom.current().nextInt(envparams.POINT_MIN, envparams.POINT_MAX + 1);

        return new Coordinate(
                ThreadLocalRandom.current().nextInt(envparams.POINT_MIN, envparams.POINT_MAX + 1),
                ThreadLocalRandom.current().nextInt(envparams.POINT_MIN, envparams.POINT_MAX + 1)
        );

    }

    private ArrayList<Coordinate> flatten(){

        ArrayList<Coordinate> flatMap = new ArrayList<>();
        for(int i = envparams.POINT_MIN; i < envparams.POINT_MAX; i++){
            for(int j = 0; j < envparams.POINT_MAX; j++){
                flatMap.add(new Coordinate(i,j));
            }
        }
        return flatMap;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public ArrayList<Vector> getWalls() {
        return walls;
    }

    public ArrayList<Home> getHomes() {
        return homes;
    }

    public int traveling(){
        int i = 0;
        for(Agent a : agents){
            if (a.getState() == traveling){
                i++;
            }
        }
        return i;
    }

    public int working(){
        int i = 0;
        for(Agent a : agents){
            if (a.getState() == working){
                i++;
            }
        }
        return i;
    }

    public int searching(){
        int i = 0;
        for(Agent a : agents){
            if (a.getState() == searching){
                i++;
            }
        }
        return i;
    }

    public EnvironmentParameters getEnvparams() {
        return envparams;
    }

    public AgentParams getAgentparams() {
        return agentparams;
    }
}


