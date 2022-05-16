package model.map;

import java.io.Serializable;

/**
 * Functions taken from https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
 **/

public class LineSegment implements Serializable {
    private final Coordinate A;
    private final Coordinate B;

    public LineSegment(Coordinate A, Coordinate B){
        this.A = A;
        this.B = B;
    }

    static boolean onSegment(Coordinate p, Coordinate q, Coordinate r)
    {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
    }

    /***
     * @param p point 1
     * @param q point 2
     * @param r point 3
     * @return clockwise or counterclockwise orientation
     */
    private int orientation(Coordinate p, Coordinate q, Coordinate r)
    {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (Double.compare(val, 0) == 0) return 0; // collinear

        return Double.compare(val, 0) > 0 ? 1 : 2; // clock or counterclock wise
    }

    public boolean liesOnLine(Coordinate c){
        return Double.compare(c.distanceTo(A) + c.distanceTo(B), this.length()) == 0;
    }

    public boolean doIntersect(LineSegment second) {
        int o1 = orientation(A, B, second.getA());
        int o2 = orientation(A, B, second.getB());
        int o3 = orientation(second.getA(), second.getB(), A);
        int o4 = orientation(second.getA(), second.getB(), B);

        // General case
        if (o1 != o2 && o3 != o4) return true;

        // Special Cases
        if (o1 == 0 && onSegment(A, B, second.getA())) return true;
        if (o2 == 0 && onSegment(A, second.getB(), second.getA())) return true;
        if (o3 == 0 && onSegment(B, A, second.getB())) return true;
        if (o4 == 0 && onSegment(B, second.getA(), second.getB())) return true;

        return false;
    }

    public Coordinate intersectPoint(LineSegment second){

        double a1 = B.getY() - A.getY();
        double b1 = A.getX() - B.getX();
        double c1 = a1 * (A.getX()) + b1 * (A.getY());

        double a2 = second.getB().getY() - second.getA().getY();
        double b2 = second.getA().getX() - second.getB().getX();
        double c2 = a2*(second.getA().getX()) + b2*(second.getA().getY());

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0)                               // line segments belong to the same line
        {
            return A.distanceTo(second.getA()) < A.distanceTo(second.getB()) ?
                    second.getA() : second.getB();          // return the line segment starting point closer to agent
        }
        else
        {
            double x = (b2 * c1 - b1 * c2)/determinant;
            double y = (a1 * c2 - a2 * c1)/determinant;
            return new Coordinate(x, y);
        }

    }

    public double length(){
        return  this.asVector().absValue();
    }

    public Vector asVector(){
        return new Vector(A, B);
    }

    public Coordinate getA() {
        return A;
    }

    public Coordinate getB() {
        return B;
    }
}
