package Classes;

import Environment.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static Classes.State.*;
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

        Vector n = (front - back) >= 0 ? this.direction.copy() : this.direction.reverse_vec();
        Vector s = (left - right) >= 0 ? this.direction.reverse_x() : this.direction.reverse_y();

        double frontal_dir = sqrt(Math.pow(front - back, 2) / (Math.pow(n.getX(), 2) + Math.pow(n.getY(), 2)));
        double side_dir = sqrt(Math.pow(left - right, 2) / (Math.pow(s.getX(), 2) + Math.pow(s.getY(), 2)));

        direction.setX(frontal_dir*n.getX() + side_dir*s.getX());
        direction.setY(frontal_dir*n.getY() + side_dir*s.getY());
    }

    private void generateRandDir(){
        double x = envparams.POINT_MIN + (
                envparams.POINT_MAX - envparams.POINT_MIN)/
                (double) (10000 * envparams.GENERATOR.nextInt(10000) + 1);
        double y =envparams.POINT_MIN + (
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

    private Coordinate placeBack(Coordinate new_pos){
        System.out.println("PLACING AGENT" + this.id + "BACK INTO ENVIRONMENT");
        Coordinate P;
        Coordinate Q;
        if(new_pos.getX() <= envparams.POINT_MIN){
            P = new Coordinate(envparams.POINT_MIN,  envparams.POINT_MIN);
            Q = new Coordinate(envparams.POINT_MIN, envparams.POINT_MAX);
        }
        else if(new_pos.getX() >= envparams.POINT_MAX){
            P = new Coordinate(envparams.POINT_MAX, envparams.POINT_MIN);
            Q = new Coordinate(envparams.POINT_MAX,envparams.POINT_MAX);
        }
        else if(new_pos.getY() <= envparams.POINT_MIN){
            P = new Coordinate(envparams.POINT_MIN, envparams.POINT_MIN);
            Q = new Coordinate(envparams.POINT_MAX, envparams.POINT_MIN);
        }
        else if(new_pos.getY() >= envparams.POINT_MIN){
            P = new Coordinate(envparams.POINT_MIN,envparams.POINT_MAX);
            Q = new Coordinate(envparams.POINT_MAX,envparams.POINT_MAX);
        }
        else{
            System.out.println("Fatal errror - couldnt place agent back into environment.");
            System.exit(1);
            return null;
        }

        Vector m = new Vector(position, P);
        Vector n = new Vector(P, Q);

        double a = direction.getX() * m.getY() - direction.getY() * m.getX();
        double b = direction.getY() * n.getX() - direction.getX() * n.getY();
        double param = a/b;

        return new Coordinate((P.getX() + n.getX()*param), (P.getY() + n.getY()*param));
    }

    private void move(){            // method that changes the position of the agent
        double c =  sqrt(Math.pow(this.speed,2)/
                (Math.pow(direction.getX(),2) + Math.pow(direction.getY(),2)));

        Coordinate new_pos = new Coordinate(
                position.getX()+(c*(direction.getX())),
                position.getY()+(c*(direction.getY()))
        );

        if (this.position.is_traversable(new_pos)){
            position = new_pos;
        }
        else{                                   //TODO add clause for walls
            position = placeBack(new_pos);
        }
    }


    private void search(){
        checkForHomes();                        // look for nearby homes
        move();                                 // move to new coordinates
        generateRandDir();                      // get new direction
        generateRandSpeed();                    // get new speed
    }

    private void checkForHomes(){
        ArrayList<Home> attractingHomes = new ArrayList<>();
        ArrayList<Home> notAttractingHomes = new ArrayList<>();

        for(Home home: envMap.getHomes()){
            double distance = this.position.distanceTo(home.getCoords());
            Vector agentToHome = new Vector(this.position, home.getCoords());
            double angle = direction.angleBetween(agentToHome);

            if(home.isAttracting()){                     // if agents are attracting others to home
                if(isInAttractionDistance(distance,home) && isVisible(distance, angle)){
                        attractingHomes.add(home);
                }
            }
            else{
                if(isVisible(distance, angle)){                   //and he can see the home
                    notAttractingHomes.add(home);
                }
            }
        }

        chooseHome(attractingHomes, notAttractingHomes);

    }

    private boolean isInAttractionDistance(double distance, Home home){
        return (distance < home.getAttraction_distance());
    }

    private boolean isVisible(double distance, double angle){
        //TODO add clause for walls in fov blocking vision of home
        return (distance < sightDist && angle < fov/2);
    }

    private void chooseHome(ArrayList<Home> aHomes, ArrayList<Home> nAHomes){
        Home home;

        if(!aHomes.isEmpty()){
            int index = envparams.GENERATOR.nextInt(aHomes.size());
            home = aHomes.get(index);
        }
        else if (!nAHomes.isEmpty()){
            int index = envparams.GENERATOR.nextInt(aHomes.size());
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

    public void setPosition(Coordinate position) {
        this.position = position;
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
}
