package model.agents;

import model.main.Main;
import model.map.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.sqrt;
import static model.main.Main.agentparams;

public class BatAgent extends Agent {

    public BatAgent(int id, Coordinate position) {
        super(id, position);
        generateRandSpeed();
        generateRandDir();
    }

    public BatAgent(Agent a) {
        super(a);
    }

    public void remake(AgentParams a) {
        this.timeSpentInState = new int[3];
        this.sightDist = a.SIGHT;
        this.fov = a.FOV;
        this.workRate = a.WORK_RATE;
        this.interestBound = a.INTEREST_BOUNDARY;
        this.state = State.searching;
    }


    private void generateDir() {

        if (home != null) {
            direction.setX(position, home.getCoords());
            direction.setY(position, home.getCoords());
            return;
        }

        if (agentparams.AVOID_OTHERS && this.state == State.searching) {
            ArrayList<Vector> vectorsToOthers = new ArrayList<>();

            for (Agent a : myMap.getAgents()) {
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
        //dirZelenka();
    }

    private void dirJa() {

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
                if (speed == 0) {
                    speed = 0.1 + (1 - 0.5) * ThreadLocalRandom.current().nextDouble();
                }
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

    @Override
    protected void search() {
        checkForHomes(); // look for nearby homes
        move(); // move to new coordinates
        generateDir(); // get new direction
        generateRandSpeed(); // get new speed
    }


}
