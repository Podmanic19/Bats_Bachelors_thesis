package model.map;

import javafx.beans.property.SimpleBooleanProperty;
import model.agents.State;
import model.agents.BatAgent;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static model.main.Main.mapparams;

public class Map implements Serializable{
    private String name;
    private final ArrayList<LineSegment> walls = new ArrayList<>();     //initial walls existing on the map
    private Coordinate[] initialAgentPositions;                         //positions of future agent placements
    private Coordinate[] initialHomePositions;                          //positions of future walls placements

    private ArrayList<BatAgent> agents = new ArrayList<>();          //agents existing on the map
    private ArrayList<Home> homes = new ArrayList<>();               //homes existing on the map


    private boolean chosen;

    public Map() {
        initialAgentPositions = new Coordinate[100];
        chosen = false;
        generateWalls();
        generateHomes();
        generateAgents();
    }

    public Map(Map m) {
        this.agents = new ArrayList<>();
        for(BatAgent a : m.getAgents()){
            agents.add(new BatAgent(a));
        }
        this.homes = new ArrayList<>();
        for(Home h : m.getHomes()){
            homes.add(new Home(h));
        }
        this.walls.addAll(m.getWalls());
    }

    public void fillWithElements(int numAgents, int numHomes) {

        for(int i = 0; i < numHomes; i++) {
            createHome(initialHomePositions[i], Home.ID++, 0);
        }

        for(int i = 0; i < numAgents; i++) {
            agents.add(new BatAgent(BatAgent.ID++, initialAgentPositions[i]));
        }

    }

    private void generateHomes() {

        initialHomePositions = new Coordinate[mapparams.NUMBER_HOME];
        ArrayList<Coordinate> flatMap = flatten();
        Collections.shuffle(flatMap);
        HashSet<Integer> possibleHomes = fillPossibleHomes(flatMap);

        int addedCoords = 0;
        for (int i = 0; i < flatMap.size(); i++) {
            if (addedCoords == mapparams.NUMBER_HOME)
                break;
            if (possibleHomes.isEmpty())
                break;
            if (!possibleHomes.contains(i))
                continue;

            initialHomePositions[addedCoords++] = flatMap.get(i);
            possibleHomes.remove(i);

            for (int j = 0; j < flatMap.size(); j++) {
                boolean remove = false;
                if (!possibleHomes.contains(j))
                    continue;
                for (int k = 0; k < addedCoords; k++) {
                    Coordinate c = initialHomePositions[k];
                    if (c.cheapDistanceTo(flatMap.get(j)) < (int) Math.pow(mapparams.MIN_DISTANCE, 2)) {
                        remove = true;
                        break;
                    }
                }
                if (remove)
                    possibleHomes.remove(j);
            }
        }
    }

    public void addHome(int spawn_time){

        Coordinate c = new Coordinate(
                ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN, mapparams.POINT_MAX + 1),
                ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN, mapparams.POINT_MAX + 1)
        );

        while(liesOnWall(c) || alreadyHome(c)){
                c.setX(ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN, mapparams.POINT_MAX + 1));
                c.setY(ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN, mapparams.POINT_MAX + 1));
        }

        createHome(c,Home.ID, spawn_time);

    }

    private boolean alreadyHome(Coordinate c){
        for(Coordinate other: initialHomePositions){
            if(c.getX() == other.getX() && c.getY() == other.getY()) return true;
        }
        return false;
    }

    private boolean liesOnWall(Coordinate c){
        for(LineSegment w: walls){
            if(w.liesOnLine(c)) return true;
        }
        return false;
    }

    private HashSet<Integer> fillPossibleHomes(ArrayList<Coordinate> flatMap) {

        HashSet<Integer> possibleHomes = new HashSet<>();

        for (int i = 0; i < flatMap.size(); i++) {
            possibleHomes.add(i);
        }

        for (int i = 0; i < flatMap.size(); i++) {
            if(liesOnWall(flatMap.get(i))) possibleHomes.remove(i);
        }

        for (Integer i : possibleHomes) {
            if (Double.compare(flatMap.get(i).getX(), 0) == 0) {
                System.out.println(flatMap.get(i));
            }
        }
        return possibleHomes;

    }

    private void createHome(Coordinate coord, int id, int spawn_time) {

        int workNeeded = ThreadLocalRandom.current().nextInt(mapparams.MIN_WORK, mapparams.MAX_WORK + 1);
        homes.add(new Home(id, workNeeded, spawn_time, coord));

    }

    private void generateAgents() {
        initialAgentPositions = new Coordinate[mapparams.AGENT_NUM];
        for(int  i = 0 ; i < mapparams.AGENT_NUM; i++){
            initialAgentPositions[i] = generateRandAgentPos();
        }
    }

    private void generateWalls() {
        walls.add(new LineSegment(new Coordinate(mapparams.POINT_MIN, mapparams.POINT_MIN),
                new Coordinate(mapparams.POINT_MIN, mapparams.POINT_MAX)));

        walls.add(new LineSegment(new Coordinate(mapparams.POINT_MAX, mapparams.POINT_MIN),
                new Coordinate(mapparams.POINT_MAX, mapparams.POINT_MAX)));

        walls.add(new LineSegment(new Coordinate(mapparams.POINT_MIN, mapparams.POINT_MIN),
                new Coordinate(mapparams.POINT_MAX, mapparams.POINT_MIN)));

        walls.add(new LineSegment(new Coordinate(mapparams.POINT_MIN, mapparams.POINT_MAX),
                new Coordinate(mapparams.POINT_MAX, mapparams.POINT_MAX)));

        ArrayList<Coordinate> flatMap = flatten();

        for (int i = 0; i < mapparams.WALLS_NUM; i++) {
            boolean didIntersect = false;
            int index = ThreadLocalRandom.current().nextInt(flatMap.size());
            Coordinate first = flatMap.get(index);

            double x1 = first.getX();
            double y1 = first.getY();

            int wallLen = ThreadLocalRandom.current().nextInt(mapparams.WALL_LENGTH_MAX + 1 - mapparams.WALL_LENGTH_MIN)
                    + mapparams.WALL_LENGTH_MIN;
            double angle = ThreadLocalRandom.current().nextDouble() * Math.PI * 2;
            double x2 = x1 + Math.cos(angle) * wallLen;
            double y2 = y1 + Math.sin(angle) * wallLen;

            x2 = x2 > mapparams.POINT_MIN ? x2 : mapparams.POINT_MIN;
            y2 = y2 > mapparams.POINT_MIN ? y2 : mapparams.POINT_MIN;

            x2 = x2 < mapparams.POINT_MAX ? x2 : mapparams.POINT_MAX;
            y2 = y2 < mapparams.POINT_MAX ? y2 : mapparams.POINT_MAX;

            Coordinate second = new Coordinate(x2, y2);
            LineSegment wall = new LineSegment(first, second);

            for (LineSegment w : walls) {
                if (w.doIntersect(wall)) {
                    didIntersect = true;
                    i--;
                    break;
                }
            }

            if (!didIntersect)
                walls.add(wall);
        }

    }

    private Coordinate generateRandAgentPos() {
        boolean generate = true;
        Coordinate c = new Coordinate(
                ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN + 1, mapparams.POINT_MAX),
                ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN + 1, mapparams.POINT_MAX));

        while (generate) {
            generate = false;
            for (LineSegment w : walls) {
                if (w.liesOnLine(c)) {
                    generate = true;
                    break;
                }
            }
            c = new Coordinate(
                    ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN + 1, mapparams.POINT_MAX),
                    ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN + 1, mapparams.POINT_MAX));
        }

        return c;
    }

    private ArrayList<Coordinate> flatten() {

        ArrayList<Coordinate> flatMap = new ArrayList<>();
        for (int i = mapparams.POINT_MIN; i < mapparams.POINT_MAX; i++) {
            for (int j = 0; j < mapparams.POINT_MAX; j++) {
                Coordinate c = new Coordinate(i, j);
                flatMap.add(c);
            }
        }
        return flatMap;
    }

    public ArrayList<BatAgent> getAgents() {
        return agents;
    }

    public ArrayList<LineSegment> getWalls() {
        return walls;
    }

    public ArrayList<Home> getHomes() {
        return homes;
    }

    public boolean isChosen() {
        return chosen;
    }

    public int traveling() {
        int i = 0;
        for (BatAgent a : agents) {
            if (a.getState() == State.traveling) {
                i++;
            }
        }
        return i;
    }

    public void save(File f) {
        try {
            FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            agents.clear();
            homes.clear();
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void save(String directory) throws IOException {

        File f = new File("maps\\" + directory);
        if(f.mkdirs()){
            try {
                FileOutputStream fos = new FileOutputStream("maps\\" + directory + "\\" + this.getName() + ".emap");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this);
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            throw new IOException();
        }
    }


    public static Map load(File inFile) {

        Map m;

        try {
            FileInputStream fis = new FileInputStream(inFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            m = (Map) ois.readObject();
            m.setName(inFile.getName().replace(".emap", ""));
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
            return null;
        }

        return m;

    }

    public void setName(String name){
        this.name = name;
    }

    public SimpleBooleanProperty getSELECTED(){
        return new SimpleBooleanProperty(this.chosen);
    }

    public void setSELECTED(boolean selected){
        this.chosen = selected;
    }

    public String getName() {
        return name;
    }

    public int working() {
        int i = 0;
        for (BatAgent a : agents) {
            if (a.getState() == State.working) {
                i++;
            }
        }
        return i;
    }

    public int searching() {
        int i = 0;
        for (BatAgent a : agents) {
            if (a.getState() == State.searching) {
                i++;
            }
        }
        return i;
    }
}
