package Environment;

import java.io.Serializable;

import static Environment.EnvironmentParameters.POINT_MAX;
import static Environment.EnvironmentParameters.POINT_MIN;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Coordinate implements Serializable {
    private double x;
    private double y;

    public double distanceTo(Coordinate B){                             // distance from this point to point B
        return sqrt((pow((B.x - x),2) + pow((B.y - y),2)));
    }

    public double cheapDistanceTo(Coordinate B){
        return (pow((B.x - x),2) + pow((B.y - y),2));
    }

    public Coordinate(Coordinate c) {
        this.x = c.x;
        this.y = c.y;
    }

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public Coordinate reverse_vec(){
        return new Coordinate(- this.x, - this.y);
    }
    public Coordinate reverse_x(){
        return new Coordinate(- this.x, this.y);
    }
    public Coordinate reverse_y(){
        return new Coordinate(this.x, - this.y);
    }
    public Coordinate copy(){
        return new Coordinate(this);
    }

    /***
     * Returns true if it is possible to get from this coordinate to the coordinate provided - whether there is a wall
     * between these two coordinates or whether the provided coordinate is outside the environment boundaries
     *
     */
    public boolean is_traversable(Coordinate b){
        if(b.getX() < POINT_MIN || b.getY() < POINT_MIN){
            return false;
        }
        else if(b.getX() > POINT_MAX || b.getY() > POINT_MAX){
            return false;
        }
        else if(false){
            // TODO create condition for walls
        }

        return true;
    }

    @Override
    public String toString(){
        return ("(" + x + "," + y + ")");
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
