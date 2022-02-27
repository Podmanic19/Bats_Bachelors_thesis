package model.agents;

import controller.SpeedDistribution;
import model.main.Main;
import model.map.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static model.main.Main.agentparams;
import static model.map.LineSegment.doIntersect;
import static java.lang.Math.sqrt;

public class BatAgent implements Serializable {
    private final int id;
    private Home home;
    private Coordinate position;
    private Vector direction; // vector movement
    private double speed; // speed - between 2 and 11.5
    private final double sightDist; // how far the agent can see
    private final double fov; // field of view
    private final double workRate; // how much work the agent performs per unit of time
    private final double interestBound; // may not be needed
    private State state; // the state the agent is in
    private double totalWork = 0;
    private final SpeedDistribution speedType;

    public BatAgent(int id, Coordinate position) {
        this.id = id;
        this.home = null;
        this.position = position;
        this.speedType = agentparams.SPEED_TYPE;
        generateRandSpeed();
        generateRandDir();
        this.sightDist = agentparams.SIGHT;
        this.fov = agentparams.FOV;
        this.workRate = agentparams.WORK_RATE;
        this.interestBound = agentparams.INTEREST_BOUNDARY;
        this.state = State.searching;
    }

    private void generateRandSpeed() {
        switch(speedType) {
            case GAUSSIAN:
                double rand_speed = (ThreadLocalRandom.current().nextGaussian() + 3.7);
                rand_speed = Math.max(rand_speed, agentparams.SPEED_MIN);
                rand_speed = Math.min(rand_speed, agentparams.SPEED_MAX);
                this.speed = rand_speed;
                break;
            case UNIFORM:
                this.speed = (ThreadLocalRandom.current().nextDouble(agentparams.SPEED_MIN, agentparams.SPEED_MAX));
                break;
            case STABLE:
                this.speed = agentparams.SPEED_MAX;
        }
    }

    private void generateDir() {

        if (home != null) {
            direction.setX(position, home.getCoords());
            direction.setY(position, home.getCoords());
            return;
        }

        if (agentparams.AVOID_OTHERS && this.state == State.searching) {
            ArrayList<Vector> vectorsToOthers = new ArrayList<>();

            for (BatAgent a : Main.envMap.getAgents()) {
                if (a == this)
                    continue;
                if (a.getState() == State.working)
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
        dirJa();
        // dirZelenka();
    }

    private void dirJa() {

        int degrees = ThreadLocalRandom.current().nextInt((90 - (-90)) + 1) + (-90);
        double forward = agentparams.FORWARD * ThreadLocalRandom.current().nextInt(100);
        double back = agentparams.BACK * ThreadLocalRandom.current().nextInt(100);
        this.direction.rotate(Math.toRadians(degrees));
        if (Double.compare(back, forward) < 0) {
            this.direction.reverse();
        }

    }

    private void dirZelenka() {
        double front = agentparams.FORWARD / (double) (10000 * ThreadLocalRandom.current().nextInt(10000) + 1);
        double back = agentparams.BACK / (double) (10000 * ThreadLocalRandom.current().nextInt(10000) + 1);
        double left = agentparams.LEFT / (double) (10000 * ThreadLocalRandom.current().nextInt(10000) + 1);
        double right = agentparams.RIGHT / (double) (10000 * ThreadLocalRandom.current().nextInt(10000) + 1);

        Vector n = (front - back) >= 0 ? this.direction.copy() : this.direction.reverse();
        Vector s = (left - right) >= 0 ? this.direction.reverseX() : this.direction.reverseY();

        double frontal_dir = sqrt(Math.pow(front - back, 2) / (Math.pow(n.getX(), 2) + Math.pow(n.getY(), 2)));
        double side_dir = sqrt(Math.pow(left - right, 2) / (Math.pow(s.getX(), 2) + Math.pow(s.getY(), 2)));

        direction.setX(frontal_dir * n.getX() + side_dir * s.getX());
        direction.setY(frontal_dir * n.getY() + side_dir * s.getY());
    }

    private void generateRandDir() {
        double x = Main.envparams.POINT_MIN + (Main.envparams.POINT_MAX - Main.envparams.POINT_MIN) /
                (double) (10000 * ThreadLocalRandom.current().nextInt(10000) + 1);
        double y = Main.envparams.POINT_MIN + (Main.envparams.POINT_MAX - Main.envparams.POINT_MIN) /
                (double) (10000 * ThreadLocalRandom.current().nextInt(10000) + 1);

        direction = new Vector(x - position.getX(), y - position.getY());

    }

    public void act() {
        switch (this.state) {
            case searching:
                search(); // Flying around randomly, looking for a home to clean
                break;
            case traveling:
                travel(); // Traveling towards a home
                break;
            case working:
                work(); // Working at a home
                break;
        }
    }

    private void move() { // method that changes the position of the agent
        while (speed > 0) {
            double c = sqrt(Math.pow(this.speed, 2) /
                    (Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2)));

            Coordinate new_pos = new Coordinate(
                    position.getX() + (c * (direction.getX())),
                    position.getY() + (c * (direction.getY())));

            WallCollision collision = position.checkWalls(new_pos);

            if (collision.getWall() == null) { // if there are no walls between this position and the new one
                position = new_pos;
                this.speed = 0;
            } else {
                collideWithWall(collision);
                this.speed -= position.distanceTo(collision.getCollisionPoint());
                if (speed == 0)
                    speed = 0.5 + (1 - 0.5) * ThreadLocalRandom.current().nextDouble();
            }
        }
    }

    private void collideWithWall(WallCollision wall) {
        Vector newDirection = wall.getWall().asVector().getNormal();
        double projectionCoef = 2 * (direction.scalarProduct(newDirection)) / Math.pow((newDirection.absValue()), 2);

        if (projectionCoef == 0) {
            direction.reverse();
        } else {
            newDirection.multiply(projectionCoef);
            direction.subtract(newDirection);
        }
    }

    private void search() {
        checkForHomes(); // look for nearby homes
        move(); // move to new coordinates
        generateDir(); // get new direction
        generateRandSpeed(); // get new speed
    }

    private synchronized void checkForHomes() {
        ArrayList<Home> attractingHomes = new ArrayList<>();
        ArrayList<Home> notAttractingHomes = new ArrayList<>();

        for (Home home : Main.envMap.getHomes()) {
            if (home.getPollution() <= 0)
                continue;
            double distance = this.position.distanceTo(home.getCoords());
            Vector agentToHome = new Vector(this.position, home.getCoords());
            double angle = direction.angleBetween(agentToHome);

            if (home.isAttracting()) { // if agents are attracting others to home
                if (isInAttractionDistance(distance, home) || isVisible(home.getCoords(), angle)) {
                    attractingHomes.add(home);
                }
            } else {
                if (isVisible(home.getCoords(), angle)) { // and he can see the home
                    notAttractingHomes.add(home);
                }
            }
        }
        chooseHome(attractingHomes, notAttractingHomes);
    }

    private boolean isInAttractionDistance(double distance, Home home) {
        if (Double.compare(distance, home.getAttraction_distance()) > 0)
            return false;
        for (LineSegment wall : Main.envMap.getWalls()) {
            if (doIntersect(new LineSegment(position, home.getCoords()), wall)) {
                return false;
            }
        }
        return true;
    }

    private boolean isVisible(Coordinate c, double angle) {
        if (Double.compare(position.distanceTo(c), sightDist) > 0 || Double.compare(angle, fov / 2) > 0)
            return false;
        for (LineSegment wall : Main.envMap.getWalls()) {
            if (doIntersect(new LineSegment(position, c), wall)) {
                return false;
            }
        }
        return true;
    }

    private void chooseHome(ArrayList<Home> aHomes, ArrayList<Home> nAHomes) {
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

    private void travel() {
        if (home == null) {
            state = State.searching;
            act();
        } else if (Double.compare(position.distanceTo(home.getCoords()), speed) <= 0) {
            position = new Coordinate(home.getCoords());
            state = State.working;
            home.addAgent(this);
        } else {
            checkForHomes();
            move();
            // generateDir();
            generateRandSpeed();
        }
    }

    private void work() {
        speed = 0;
        if (home == null) {
            state = State.searching;
            act();
            return;
        }
        home.setAttracting(Double.compare((home.getPollution()), interestBound) >= 0);
        boolean worked = home.decreasePollution(this);
        if (!worked) {
            this.home = null;
            state = State.searching;
            act();
        }
        totalWork += workRate;
    }

    public double getWorkRate() {
        return workRate;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public State getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public Vector getDirection() {
        return direction;
    }
}
