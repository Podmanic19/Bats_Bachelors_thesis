package Classes;

import Environment.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import static Classes.State.*;
import static Environment.LineSegment.doIntersect;
import static Main.Main.*;
import static java.lang.Math.sqrt;

public class Agent implements Serializable {
    private final int id;
    private Home home;
    private Coordinate position;
    private Vector direction;               // vector movement
    private double speed;                   // speed - between 2 and 11.5
    private final double sightDist;          // how far the agent can see
    private final double fov;               // field of view
    private final double workRate;          // how much work the agent performs per unit of time
    private final double interestBound;     // may not be needed
    private State state;                    // the state the agent is in

    public Agent(int id, Coordinate position) {
        this.id = id;
        this.home = null;
        this.position = position;
        generateRandSpeed();
        generateRandDir();
        this.sightDist =  agentparams.SIGHT;
        this.fov = agentparams.FOV;
        this.workRate =  agentparams.WORK_RATE;
        this.interestBound =  agentparams.INTEREST_BOUNDARY;
        this.state = searching;
    }

    private void generateRandSpeed(){
        double rand_speed = (new Random().nextGaussian() + 3.7);
        rand_speed = Math.max(rand_speed,agentparams.SPEED_MIN);
        rand_speed = Math.min(rand_speed,agentparams.SPEED_MAX);
        this.speed = rand_speed;
    }

    private void generateDir() {

        if(home != null){
            direction.setX(position, home.getCoords());
            direction.setY(position, home.getCoords());
            return;
        }

        double front = agentparams.FORWARD / (double) (10000 * envparams.GENERATOR.nextInt(10000) + 1);
        double back  = agentparams.BACK / (double) (10000 * envparams.GENERATOR.nextInt(10000) + 1);
        double left  = agentparams.LEFT / (double) (10000 * envparams.GENERATOR.nextInt(10000) + 1);
        double right = agentparams.RIGHT / (double) (10000 * envparams.GENERATOR.nextInt(10000) + 1);

        Vector n = (front - back) >= 0 ? this.direction.copy() : this.direction.reverse();
        Vector s = (left - right) >= 0 ? this.direction.reverseX() : this.direction.reverseY();

        double frontal_dir = sqrt(Math.pow(front - back, 2) / (Math.pow(n.getX(), 2) + Math.pow(n.getY(), 2)));
        double side_dir = sqrt(Math.pow(left - right, 2) / (Math.pow(s.getX(), 2) + Math.pow(s.getY(), 2)));

        direction.setX(frontal_dir*n.getX() + side_dir*s.getX());
        direction.setY(frontal_dir*n.getY() + side_dir*s.getY());
    }

    private void generateRandDir(){
        double x = envparams.POINT_MIN + (
                envparams.POINT_MAX - envparams.POINT_MIN)/
                (double) (10000 * envparams.GENERATOR.nextInt(10000) + 1);
        double y = envparams.POINT_MIN + (
                envparams.POINT_MAX - envparams.POINT_MIN)/
                (double) (10000 * envparams.GENERATOR.nextInt(10000) + 1);

        if(direction == null) {
            direction = new Vector(x - position.getX(), y - position.getY());
        }
        else{
            direction.setX(x - position.getX());
            direction.setY(y - position.getY());
        }

    }

    public void act() {
        switch (this.state) {
            case searching:
                search();                   // Flying around randomly, looking for a home to clean
                break;
            case traveling:
                travel();                   // Traveling towards a home
                break;
            case working:
                work();                     // Working at a home
                break;
        }
    }

    private void move(){                                 // method that changes the position of the agent
        while(speed > 0) {
            double c = sqrt(Math.pow(this.speed, 2) /
                    (Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2)));

            Coordinate new_pos = new Coordinate(
                    position.getX() + (c * (direction.getX())),
                    position.getY() + (c * (direction.getY()))
            );

            WallCollision collision = position.checkWalls(new_pos);

            if (collision.getWall() == null) {          // if there are no walls between this position and the new one
                position = new_pos;
                this.speed = 0;
            } else {
                collideWithWall(collision);
                this.speed -= position.distanceTo(collision.getCollisionPoint());
                if (speed < 0) {
                    System.out.println("si kkt dusan");
                }
                if(speed == 0) speed = 0.5 + (1 - 0.5) * envparams.GENERATOR.nextDouble();
            }
        }
    }

    private void collideWithWall(WallCollision wall){
        Vector normal = wall.getWall().asVector().getNormal();
        double projectionCoef = (direction.scalarProduct(normal))/Math.pow((normal.absValue()), 2);

        if(projectionCoef == 0){
            direction.reverse();
        }
        else {
            Vector projected = new Vector(normal.getX() * projectionCoef, normal.getY() * projectionCoef);
            setDirection(new Vector(direction.getX() - 2 * projected.getX(),
                    direction.getY() - 2 * projected.getY()));
        }
    }

    private void search(){
        checkForHomes();                       // look for nearby homes
        move();                                // move to new coordinates
        generateDir();                         // get new direction
        generateRandSpeed();                   // get new speed
    }

    private void checkForHomes(){
        ArrayList<Home> attractingHomes = new ArrayList<>();
        ArrayList<Home> notAttractingHomes = new ArrayList<>();

        for(Home home: envMap.getHomes()){
            double distance = this.position.distanceTo(home.getCoords());
            Vector agentToHome = new Vector(this.position, home.getCoords());
            double angle = direction.angleBetween(agentToHome);

            if(home.isAttracting()){                     // if agents are attracting others to home
                if(isInAttractionDistance(distance,home) && isVisible(home, angle)){
                    attractingHomes.add(home);
                }
            }
            else{
                if(isVisible(home, angle)){                   //and he can see the home
                    notAttractingHomes.add(home);
                }
            }
        }
        chooseHome(attractingHomes, notAttractingHomes);
    }

    private boolean isInAttractionDistance(double distance, Home home){
        if(distance > home.getAttraction_distance()) return false;
        for(LineSegment wall :envMap.getWalls()){
            if(doIntersect(new LineSegment(position, home.getCoords()), wall)){
                return false;
            }
        }
        return true;
    }

    private boolean isVisible(Home home, double angle){
        if(position.distanceTo(home.getCoords()) > sightDist || angle > fov/2) return false;
        for(LineSegment wall :envMap.getWalls()){
            if(doIntersect(new LineSegment(position, home.getCoords()), wall)){
                return false;
            }
        }
        return true;
    }

    private void chooseHome(ArrayList<Home> aHomes, ArrayList<Home> nAHomes){
        Home home;

        if(!aHomes.isEmpty()){
            int index = envparams.GENERATOR.nextInt(aHomes.size());
            home = aHomes.get(index);
        }
        else if (!nAHomes.isEmpty()){
            int index = envparams.GENERATOR.nextInt(nAHomes.size());
            home = nAHomes.get(index);
        }
        else{
            return;                             // no home found
        }

        this.direction.setX(position, home.getCoords());
        this.direction.setY(position, home.getCoords());    // if a home is found fly towards it
        this.home = home;                                   // set it as the home im traveling to
        state = traveling;
    }

    private void travel(){
        if(home == null){
            state = searching;
            act();
        }
        else if(position.distanceTo(home.getCoords()) <= speed){
          position = new Coordinate(home.getCoords());
          state = working;
          home.addAgent(this);
        }
        else{
            checkForHomes();
            move();
            generateDir();
            generateRandSpeed();
        }
    }

    private void work(){
        speed = 0;
        if(home == null){
            state = searching;
            act();
            return;
        }
        home.setAttracting((home.getPollution()) >= interestBound);
        boolean worked = home.decreasePollution(this);
        if(!worked){
            this.home = null;
            state = searching;
            act();
        }
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
}
