package Environment;

import Classes.Agent;
import Classes.State;
import com.sun.javafx.scene.traversal.ContainerTabOrder;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static Classes.State.*;
import static Environment.EnvironmentConstants.*;

public class Environment {
    private Point[][] map;
    private ArrayList<Agent> agents = new ArrayList<>();
    private ArrayList<Point> walls = new ArrayList<>();
    private ArrayList<Point> homes = new ArrayList<>();
    private static Environment instance = null;

    private Environment()
    {
        generateMap();
        placeHomes();
        placeAgents();
    }

    public static Environment getInstance()
    {
        if (instance == null)
            instance = new Environment();

        return instance;
    }

    private void generateMap() {
        map = new Point[POINT_MAX][POINT_MAX];
        for (int i = POINT_MIN; i < POINT_MAX; i++) {
            for (int j = POINT_MIN; j < POINT_MAX; j++) {
                map[i][j] = new Point(new Coordinate(i, j), generateWall(i, j));
            }
        }
    }

    private void placeHomes(){

        ArrayList<Point> flatMap = flatten();
        HashSet<Integer> possibleHomes = fillPossibleHomes(flatMap);
        int homeID = 1;
        Collections.shuffle(flatMap);

        for(int i = 0; i < flatMap.size(); i++){
            if(homes.size() == NUMBER_HOME) break;
            if(possibleHomes.isEmpty()) break;

            if(!possibleHomes.contains(i)) continue;

            createHome(flatMap.get(i), homeID++);
            possibleHomes.remove(i);

            for(int j = 0; j < flatMap.size(); j++){
                boolean remove = false;
                if(!possibleHomes.contains(j)) continue;
                for(Point home: homes){
                    if(home.getCoords().cheapDistanceTo(flatMap.get(j).getCoords()) < (int) Math.pow(MIN_DISTANCE, 2)) {
                        remove = true;
                        break;
                    }
                }
                if(remove) possibleHomes.remove(j);
            }
        }
    }

    private HashSet<Integer> fillPossibleHomes(ArrayList<Point> flatMap){

        HashSet<Integer> possibleHomes = new HashSet<>();

        for(int i = 0; i < flatMap.size(); i++){
            if(flatMap.get(i).isWall()) continue;
            possibleHomes.add(i);
        }

        return possibleHomes;

    }

    private void createHome(Point p, int id){

        int workNeeded = ThreadLocalRandom.current().nextInt(MIN_WORK, MAX_WORK + 1);

        p.setHome(new Home(id, workNeeded));
        homes.add(p);

    }

    private void generateAgents(){
        agents = new ArrayList<>();

        for(int i = 0; i < AGENT_NUM; i++){
            agents.add(new Agent(i + 1, generateRandPos()));
        }
    }

    private boolean generateWall(int x, int y){
        if(x == 0 || x == POINT_MAX) return true;
        if(y == 0 || y == POINT_MAX) return true;
        return false;
    }

    private void placeAgents(){
        generateAgents();
    }

    private Coordinate generateRandPos(){

        int x = ThreadLocalRandom.current().nextInt(POINT_MIN, POINT_MAX + 1);
        int y = ThreadLocalRandom.current().nextInt(POINT_MIN, POINT_MAX + 1);

        while(getPoint(x,y).isWall()){
            x = ThreadLocalRandom.current().nextInt(POINT_MIN, POINT_MAX + 1);
            y = ThreadLocalRandom.current().nextInt(POINT_MIN, POINT_MAX + 1);
        }

        return new Coordinate(
                ThreadLocalRandom.current().nextInt(POINT_MIN, POINT_MAX + 1),
                ThreadLocalRandom.current().nextInt(POINT_MIN, POINT_MAX + 1)
        );

    }

    private ArrayList<Point> flatten(){

        Point[] flatMap = new Point[(int) Math.pow((POINT_MAX-POINT_MIN),2)];
        for(int i = POINT_MIN; i < POINT_MAX; i++){
            if (POINT_MAX - POINT_MIN >= 0)
                System.arraycopy(
                        map[i],
                        POINT_MIN,
                        flatMap,
                        i * POINT_MAX + POINT_MIN,
                        POINT_MAX - POINT_MIN
                );
        }
        return new ArrayList<>(Arrays.asList(flatMap));
    }

    public Point[][] getMap() {
        return map;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public Point getPoint(int x, int y){
        return this.map[x][y];
    }

    public ArrayList<Point> getWalls() {
        return walls;
    }

    public ArrayList<Point> getHomes() {
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
}
