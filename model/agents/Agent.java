package model.agents;
import model.main.Main;
import model.map.*;
import model.map.mapshell.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import static model.main.Main.agentparams;

public abstract class Agent implements Serializable {
    public static int ID = 1;
    protected int id;
    protected Map myMap;
    protected Home home;
    protected Coordinate position;
    protected Vector direction; // vector movement
    protected double speed; // speed - between 2 and 11.5
    protected double sightDist; // how far the agent can see
    protected double hearingDist; // how far the agent can see
    protected double fov; // field of view
    protected double workRate; // how much work the agent performs per unit of time
    protected double interestBound; // may not be needed
    protected State state; // the state the agent is in
    protected double totalWork = 0;
    protected int[] timeSpentInState;

    public void act() {
        switch (this.state) {
            case searching:
                this.timeSpentInState[0]++;
                search(); // Flying around randomly, looking for a home to clean
                break;
            case traveling:
                this.timeSpentInState[1]++;
                travel(); // Traveling towards a home
                break;
            case working:
                this.timeSpentInState[2]++;
                work(); // Working at a home
                break;
        }
    }

    protected void travel() {
        if (home == null) {
            state = State.searching;
            timeSpentInState[1]--;
            act();
        } else if (Double.compare(position.distanceTo(home.getCoords()), speed) <= 0) {
            position = new Coordinate(home.getCoords());
            state = State.working;
            home.addAgent(this);
        } else {
            checkForHomes();
            move();
            generateRandSpeed();
        }
    }

    protected void search() {}

    protected void move() {}

    protected void work() {
        speed = 0;
        CallType call = Double.compare((home.getPollution()), interestBound) >= 0 ? CallType.ATTRACTING : CallType.NONE;
        home.setCall(call);
        if (home == null || !home.decreasePollution(this)) {
            this.home = null;
            state = State.searching;
            timeSpentInState[2]--;
            act();
            return;
        }
        totalWork += workRate;
    }

    public Agent(int id, Coordinate position) {
        this.id = id;
        this.home = null;
        this.position = position;
        this.timeSpentInState = new int[3];
        this.sightDist = agentparams.SIGHT;
        this.fov = agentparams.FOV;
        this.workRate = agentparams.WORK_RATE;
        this.interestBound = agentparams.INTEREST_BOUNDARY;
        this.state = State.searching;
        this.hearingDist = agentparams.HEARING_DISTANCE;
        generateRandSpeed();
        generateRandDir();
    }

    public Agent(Agent a) {
        this.id = a.id;
        this.home = null;
        this.position = a.position;
        this.timeSpentInState = a.timeSpentInState;
        this.speed = a.speed;
        this.direction = a.direction;
        this.sightDist = a.sightDist;
        this.fov = a.fov;
        this.workRate = a.workRate;
        this.interestBound = a.interestBound;
        this.state = State.searching;
        this.hearingDist = a.hearingDist;
        generateRandSpeed();
        generateRandDir();
    }

    protected void generateRandSpeed() {
        double rand_speed = (ThreadLocalRandom.current().nextGaussian() + 3.7);
        rand_speed = Math.max(rand_speed, agentparams.SPEED_MIN);
        rand_speed = Math.min(rand_speed, agentparams.SPEED_MAX);
        this.speed = rand_speed;
    }

    protected void generateRandDir() {
        double x = Main.mapparams.POINT_MIN + (Main.mapparams.POINT_MAX - Main.mapparams.POINT_MIN) /
                (double) (10000 * ThreadLocalRandom.current().nextInt(10000) + 1);
        double y = Main.mapparams.POINT_MIN + (Main.mapparams.POINT_MAX - Main.mapparams.POINT_MIN) /
                (double) (10000 * ThreadLocalRandom.current().nextInt(10000) + 1);

        direction = new Vector(x - position.getX(), y - position.getY());

    }

    protected synchronized void checkForHomes() {

        if(agentparams.DECISIVE && home != null) return;

        ArrayList<Home> attractingHomes = new ArrayList<>();
        ArrayList<Home> notAttractingHomes = new ArrayList<>();

        for (Home home : myMap.getHomes()) {
            if (home.getPollution() <= 0)
                continue;
            double distance = this.position.distanceTo(home.getCoords());
            Vector agentToHome = new Vector(this.position, home.getCoords());
            double angle = direction.angleBetween(agentToHome);

            switch (home.getCall()) {
                case REPULSING:
                    break;
                case ATTRACTING: // if agents are attracting others to home
                    if (isInAttractionDistance(distance, home) || isVisible(home.getCoords(), angle)) {
                        attractingHomes.add(home);
                    }
                    break;
                case NONE:
                    if (isVisible(home.getCoords(), angle)) { // and he can see the home
                        notAttractingHomes.add(home);
                    }
                    break;
            }
        }
        chooseHome(attractingHomes, notAttractingHomes);
    }

    protected boolean isInAttractionDistance(double distance, Home home) {
        if (Double.compare(distance, hearingDist) > 0)
            return false;
        for (LineSegment wall :myMap.getWalls()) {
            if (wall.doIntersect(new LineSegment(position, home.getCoords()))) {
                return false;
            }
        }
        return true;
    }

    protected boolean isVisible(Coordinate c, double angle) {
        if (Double.compare(position.distanceTo(c), sightDist) > 0 || Double.compare(angle, fov / 2) > 0)
            return false;
        for (LineSegment wall : myMap.getWalls()) {
            if (wall.doIntersect(new LineSegment(position, c))) {
                return false;
            }
        }
        return true;
    }

    protected void chooseHome(ArrayList<Home> aHomes, ArrayList<Home> nAHomes) {
        Home home;

        if (!aHomes.isEmpty()) {
            int index = ThreadLocalRandom.current().nextInt(aHomes.size());
            home = aHomes.get(index);
        } else if (!nAHomes.isEmpty()) {
            int index = ThreadLocalRandom.current().nextInt(nAHomes.size());
            home = nAHomes.get(index);
        } else {
            return; // no home found
        }

        this.direction.setX(position, home.getCoords());
        this.direction.setY(position, home.getCoords()); // if a home is found fly towards it
        this.home = home; // set it as the home im traveling to
        state = State.traveling;
    }

    public int getId() {
        return id;
    }

    public Home getHome() {
        return home;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Vector getDirection() {
        return direction;
    }

    public double getWorkRate() {
        return workRate;
    }

    public State getState() {
        return state;
    }

    public double getTotalWork() {
        return totalWork;
    }

    public void giveMap(Map myMap) {
        this.myMap = myMap;
    }
}
