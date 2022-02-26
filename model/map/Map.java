package model.map;

import model.main.Main;
import model.agents.State;
import model.agents.BatAgent;
import model.serialization.Save;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static model.main.Main.envparams;
import static model.map.LineSegment.doIntersect;
import static model.map.LineSegment.liesOnLine;

public class Map extends Save {
    private ArrayList<BatAgent> agents = new ArrayList<>();
    private ArrayList<LineSegment> walls = new ArrayList<>();
    private ArrayList<Home> homes = new ArrayList<>();

    public Map() {
        generateWalls();
        generateHomes();
        generateAgents();
    }

    private void generateHomes() {
        ArrayList<Coordinate> flatMap = flatten();
        Collections.shuffle(flatMap);
        HashSet<Integer> possibleHomes = fillPossibleHomes(flatMap);
        int homeID = 1;

        for (int i = 0; i < flatMap.size(); i++) {
            if (homes.size() == envparams.NUMBER_HOME)
                break;
            if (possibleHomes.isEmpty())
                break;
            if (!possibleHomes.contains(i))
                continue;

            createHome(flatMap.get(i), homeID++);
            possibleHomes.remove(i);

            for (int j = 0; j < flatMap.size(); j++) {
                boolean remove = false;
                if (!possibleHomes.contains(j))
                    continue;
                for (Home home : homes) {
                    if (home.getCoords().cheapDistanceTo(flatMap.get(j)) < (int) Math.pow(envparams.MIN_DISTANCE, 2)) {
                        remove = true;
                        break;
                    }
                }
                if (remove)
                    possibleHomes.remove(j);
            }
        }
    }

    private HashSet<Integer> fillPossibleHomes(ArrayList<Coordinate> flatMap) {

        HashSet<Integer> possibleHomes = new HashSet<>();

        for (int i = 0; i < flatMap.size(); i++) {
            possibleHomes.add(i);
        }

        for (int i = 0; i < flatMap.size(); i++) {
            for (LineSegment w : walls) {
                if (liesOnLine(flatMap.get(i), w)) {
                    possibleHomes.remove(i);
                }
            }
        }

        for (Integer i : possibleHomes) {
            if (Double.compare(flatMap.get(i).getX(), 0) == 0) {
                System.out.println(flatMap.get(i));
            }
        }
        return possibleHomes;

    }

    private void createHome(Coordinate coord, int id) {

        int workNeeded = ThreadLocalRandom.current().nextInt(envparams.POINT_MIN,envparams.POINT_MAX + 1);
        homes.add(new Home(id, workNeeded, coord));

        System.out.println(coord.getX() + " " + coord.getY());

    }

    private void generateAgents() {
        agents = new ArrayList<>();

        for (int i = 0; i < envparams.AGENT_NUM; i++) {
            agents.add(new BatAgent(i + 1, generateRandPos()));
        }
    }

    private void generateWalls() {
        walls.add(new LineSegment(new Coordinate(envparams.POINT_MIN, envparams.POINT_MIN),
                new Coordinate(envparams.POINT_MIN, envparams.POINT_MAX)));

        walls.add(new LineSegment(new Coordinate(envparams.POINT_MAX, envparams.POINT_MIN),
                new Coordinate(envparams.POINT_MAX, envparams.POINT_MAX)));

        walls.add(new LineSegment(new Coordinate(envparams.POINT_MIN, envparams.POINT_MIN),
                new Coordinate(envparams.POINT_MAX, envparams.POINT_MIN)));

        walls.add(new LineSegment(new Coordinate(envparams.POINT_MIN, envparams.POINT_MAX),
                new Coordinate(envparams.POINT_MAX, envparams.POINT_MAX)));

        ArrayList<Coordinate> flatMap = flatten();

        for (int i = 0; i < envparams.WALLS_NUM; i++) {
            boolean didIntersect = false;
            int index = ThreadLocalRandom.current().nextInt(flatMap.size());
            Coordinate first = flatMap.get(index);

            double x1 = first.getX();
            double y1 = first.getY();

            int wallLen = ThreadLocalRandom.current().nextInt(envparams.WALL_LENGTH_MAX + 1 - envparams.WALL_LENGTH_MIN)
                    + envparams.WALL_LENGTH_MIN;
            double angle = ThreadLocalRandom.current().nextDouble() * Math.PI * 2;
            double x2 = x1 + Math.cos(angle) * wallLen;
            double y2 = y1 + Math.sin(angle) * wallLen;

            x2 = x2 > envparams.POINT_MIN ? x2 : envparams.POINT_MIN;
            y2 = y2 > envparams.POINT_MIN ? y2 : envparams.POINT_MIN;

            x2 = x2 < envparams.POINT_MAX ? x2 : envparams.POINT_MAX;
            y2 = y2 < envparams.POINT_MAX ? y2 : envparams.POINT_MAX;

            Coordinate second = new Coordinate(x2, y2);
            LineSegment wall = new LineSegment(first, second);

            for (LineSegment w : walls) {
                if (doIntersect(wall, w)) {
                    didIntersect = true;
                    i--;
                    break;
                }
            }

            if (!didIntersect)
                walls.add(wall);
        }

    }

    private Coordinate generateRandPos() {
        boolean generate = true;
        Coordinate c = new Coordinate(
                ThreadLocalRandom.current().nextInt(envparams.POINT_MIN + 1, envparams.POINT_MAX),
                ThreadLocalRandom.current().nextInt(envparams.POINT_MIN + 1, envparams.POINT_MAX));

        while (generate) {
            generate = false;
            for (LineSegment w : walls) {
                if (liesOnLine(c, w)) {
                    generate = true;
                    break;
                }
            }
            c = new Coordinate(
                    ThreadLocalRandom.current().nextInt(envparams.POINT_MIN + 1, envparams.POINT_MAX),
                    ThreadLocalRandom.current().nextInt(envparams.POINT_MIN + 1, envparams.POINT_MAX));
        }

        return c;

    }

    private ArrayList<Coordinate> flatten() {

        ArrayList<Coordinate> flatMap = new ArrayList<>();
        for (int i = envparams.POINT_MIN; i < envparams.POINT_MAX; i++) {
            for (int j = 0; j < envparams.POINT_MAX; j++) {
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

    public int traveling() {
        int i = 0;
        for (BatAgent a : agents) {
            if (a.getState() == State.traveling) {
                i++;
            }
        }
        return i;
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
