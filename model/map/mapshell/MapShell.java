package model.map.mapshell;

import javafx.beans.property.SimpleBooleanProperty;
import model.map.Coordinate;
import model.map.LineSegment;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import static model.main.Main.mapparams;

public class MapShell implements Serializable {

    private String name;
    private final ArrayList<LineSegment> walls = new ArrayList<>();     //initial walls existing on the map
    private Coordinate[] initialAgentPositions;                         //positions of future agent placements
    private Coordinate[] initialHomePositions;                          //positions of future walls placements
    private Coordinate[] futureHomePositions;
    private double[] initialPollutions;

    private boolean chosen;

    public MapShell() {
        chosen = false;
        futureHomePositions = new Coordinate[200];
        generateWalls();
        generateHomes();
        generatePollutions();
        generateAgents();
    }

    private void generateFutureHomes(ArrayList<Coordinate> flatMap) {

        HashSet<Coordinate> alreadyHomes = new HashSet<>(Arrays.asList(initialHomePositions));

        int added = -1;
        for(int i = 0; i < flatMap.size(); i++) {
            if(added == 199) break;
            Coordinate c = flatMap.get(i);
            if(liesOnWall(c) || alreadyHomes.contains(c)) continue;
            futureHomePositions[++added] = c;
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

        generateFutureHomes(flatMap);

    }

    private void generatePollutions() {
        this.initialPollutions = new double[100];
        for (int i = 0; i < 100; i++) {
            initialPollutions[i] = ThreadLocalRandom.current().nextInt(mapparams.MIN_WORK, mapparams.MAX_WORK + 1);
        }
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

    private void generateAgents() {
        initialAgentPositions = new Coordinate[100];
        for(int  i = 0 ; i < 100; i++){
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

    public void save(File f) {
        try {
            FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void save(String directory) throws IOException {

        FileOutputStream fos = new FileOutputStream("maps\\" + directory + "\\" + this.getName() + ".emap");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        fos.close();

    }


    public static MapShell load(File inFile) {

        MapShell m;

        try {
            FileInputStream fis = new FileInputStream(inFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            m = (MapShell) ois.readObject();
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

    public String getName() {
        return name;
    }

    public ArrayList<LineSegment> getWalls() {
        return walls;
    }

    public Coordinate[] getInitialAgentPositions() {
        return initialAgentPositions;
    }

    public Coordinate[] getInitialHomePositions() {
        return initialHomePositions;
    }

    public double[] getInitialPollutions() {
        return initialPollutions;
    }

    public boolean isChosen() {
        return chosen;
    }

    public SimpleBooleanProperty getSELECTED(){
        return new SimpleBooleanProperty(this.chosen);
    }

    public void setSELECTED(boolean selected){
        this.chosen = selected;
    }

    public Coordinate[] getFutureHomePositions() {
        return futureHomePositions;
    }
}
