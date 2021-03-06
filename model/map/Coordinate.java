package model.map;

import java.io.Serializable;
import java.util.ArrayList;

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
     * Returns wall and collision point between given two points if there is a wall on line between them
     */
    public WallCollision checkWalls(Coordinate b, ArrayList<LineSegment> walls){

        LineSegment AtoB = new LineSegment(this, b);            // A in line segment being current position
        LineSegment closestWall = null;
        Coordinate intersect = null;
        double distanceToWall = -1;

        for(LineSegment w: walls){
            if(w.doIntersect(AtoB)){
                intersect = w.intersectPoint(AtoB);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate)) {
            return false;
        }
        Coordinate other = (Coordinate) o;
        if(this == other) return true;

        return this.x == other.x && this.y == other.y;
    }
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
