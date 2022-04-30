package model.agents;
import model.main.Main;
import model.map.*;
import model.map.mapshell.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.sqrt;
import static model.main.Main.agentparams;

public class BatAgent implements Serializable {
    private Map myMap;
    private Home home;
    private Coordinate position;
    private Vector direction; // vector movement
    private double speed; // speed - between 2 and 11.5
    private double sightDist; // how far the agent can see
    private double hearingDist; // how far the agent can see
    private double fov; // field of view
    private double workRate; // how much work the agent performs per unit of time
    private double interestBound; // may not be needed
    private State state; // the state the agent is in
    private double totalWork = 0;
    private int[] timeSpentInState;
    private boolean decisive;
    private boolean avoiding;

    public BatAgent(Coordinate position, AgentParams agentParams) {
        this.home = null;
        this.position = position;
        this.timeSpentInState = new int[3];
        this.sightDist = agentParams.SIGHT;
        this.fov = agentParams.FOV;
        this.workRate = agentParams.WORK_RATE;
        this.interestBound = agentParams.INTEREST_BOUNDARY;
        this.state = State.searching;
        this.hearingDist = agentParams.HEARING_DISTANCE;
        this.decisive = agentParams.DECISIVE;
        this.avoiding = agentParams.AVOID_OTHERS;
        generateRandSpeed();
        generateRandDir();
    }

    private void generateDir() {

        if (home != null) {
            direction.setX(position, home.getCoords());
            direction.setY(position, home.getCoords());
            return;
        }

        if (avoiding && this.state == State.searching) {
            ArrayList<Vector> vectorsToOthers = new ArrayList<>();

            for (BatAgent a : myMap.getAgents()) {
                if (a == this)
                    continue;
                if (a.getState() == State.working)                                      //working agents are not visible
                    continue;
                Vector thisToAgent = new Vector(this.position, a.getPosition());
                double angle = direction.angleBetween(thisToAgent);

                if (isVisible(a.getPosition(), angle)) {
                    vectorsToOthers.add(new Vector(this.position, a.getPosition()));
                }

            }
            if (!vectorsToOthers.isEmpty()) {
                Vector direction = new Vector(0, 0);
                for (Vector v : vectorsToOthers) {
                    direction.subtract(v);
                }
                this.direction = direction;
                return;
            }

        }
        changeDir();
    }

    private void changeDir() {

        int degrees = 0;
        if(agentparams.LEFT > 0 && agentparams.RIGHT > 0) {
            degrees = ThreadLocalRandom.current().nextInt((90 - (-90)) + 1) + (-90);
        }
        double forward = agentparams.FORWARD * ThreadLocalRandom.current().nextInt(100);
        double back = agentparams.BACK * ThreadLocalRandom.current().nextInt(100);
        this.direction.rotate(Math.toRadians(degrees));
        if (Double.compare(back, forward) < 0) {
            this.direction.reverse();
        }

    }

    protected void move() { // method that changes the position of the agent
        while (speed > 0) {
            double c = sqrt(Math.pow(this.speed, 2) /
                    (Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2)));

            Coordinate new_pos = new Coordinate(
                    position.getX() + (c * (direction.getX())),
                    position.getY() + (c * (direction.getY())));

            WallCollision collision = position.checkWalls(new_pos, myMap.getWalls());

            if (collision.getWall() == null) { // if there are no walls between this position and the new one
                position = new_pos;
                this.speed = 0;
            }
            else {
                collideWithWall(collision);
                this.speed -= position.distanceTo(collision.getCollisionPoint());
                if (speed == 0) {                                                  //if agent were to get stuck in wall
                    speed = 0.1 + (1 - 0.5) * ThreadLocalRandom.current().nextDouble();
                }
            }
        }
    }

    private void collideWithWall(WallCollision wall) {
        Vector newDirection = wall.getWall().asVector().getNormal();
        double projectionCoef = 2 * (direction.scalarProduct(newDirection)) / Math.pow((newDirection.absValue()), 2);

        if (projectionCoef == 0) {                          // if colliding vertically
            direction.reverse();
        } else {
            newDirection.multiply(projectionCoef);         // if colliding under an angle reflect in same angle
            direction.subtract(newDirection);
        }
    }

    protected void search() {
        checkForHomes(); // look for nearby homes
        move(); // move to new coordinates
        generateDir(); // get new direction
        generateRandSpeed(); // get new speed
    }


    public void act() {
        switch (this.state) {
            case searching:
                this.timeSpentInState[0]++;
                search(); // Flying around randomly, looking for a home to clean
                break;
            case travelling:
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
        state = State.travelling;
    }

    protected synchronized void checkForHomes() {

        if(home != null && home.getCall() == CallType.ATTRACTING){  // if already flying to a home that is attracting me
            if(decisive && home.getPollution() > 0) return;
        }

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
                    if(isInAttractionDistance(distance, home)) {
                        attractingHomes.add(home);
                    }
                    else if(isVisible(home.getCoords(), angle)) {
                        notAttractingHomes.add(home);
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
