package Environment;

import java.io.Serializable;

import static Environment.LineSegment.doIntersect;
import static Environment.LineSegment.intersectPoint;
import static Main.Main.envMap;
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
    public WallCollision checkWalls(Coordinate b){

        LineSegment AtoB = new LineSegment(this, b);            // A in line segment being current position
        LineSegment closestWall = null;
        Coordinate intersect = null;
        double distanceToWall = -1;

        for(LineSegment w: envMap.getWalls()){
            if(doIntersect(w,AtoB)){
                intersect = intersectPoint(AtoB,w);
                double thisDistance = this.distanceTo(intersect);
                if(distanceToWall == -1 || Double.compare(thisDistance, distanceToWall) > 0){
                    distanceToWall = thisDistance;
                    closestWall = w;
                }
            }
        }

        return new WallCollision(closestWall, intersect);
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
