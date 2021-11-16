package Environment;

public class LineSegment {
    private Coordinate A;
    private Coordinate B;

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

    static int orientation(Coordinate p, Coordinate q, Coordinate r)
    {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (Double.compare(val, 0) == 0) return 0; // collinear

        return Double.compare(val, 0) > 0 ? 1 : 2; // clock or counterclock wise
    }

    /***
     * @param first first line segment
     * @param second second line segment
     * @return whether they intersect
     */


    public static boolean doIntersect(LineSegment first, LineSegment second) {
        int o1 = orientation(first.getA(), first.getB(), second.getA());
        int o2 = orientation(first.getA(), first.getB(), second.getB());
        int o3 = orientation(second.getA(), second.getB(), first.getA());
        int o4 = orientation(second.getA(), second.getB(), first.getB());

        // General case
        if (o1 != o2 && o3 != o4) return true;

        // Special Cases
        if (o1 == 0 && onSegment(first.getA(), first.getB(), second.getA())) return true;
        if (o2 == 0 && onSegment(first.getA(), second.getB(), second.getA())) return true;
        if (o3 == 0 && onSegment(first.getB(), first.getA(), second.getB())) return true;
        if (o4 == 0 && onSegment(first.getB(), second.getA(), second.getB())) return true;

        return false;
    }

    static Coordinate intersectPoint(LineSegment first, LineSegment second){

        double a1 = first.getB().getY() - first.getA().getY();
        double b1 = first.getA().getX() - first.getB().getX();
        double c1 = a1 * (first.getA().getX()) + b1 * (first.getA().getY());

        double a2 = second.getB().getY() - second.getA().getY();
        double b2 = second.getA().getX() - second.getB().getX();
        double c2 = a2*(second.getA().getX()) + b2*(second.getA().getY());

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0)                               // line segments belong to the same line
        {
            return first.getA().distanceTo(second.getA()) < first.getA().distanceTo(second.getB()) ?
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
        return new Vector(A,B);
    }

    public Coordinate getA() {
        return A;
    }

    public Coordinate getB() {
        return B;
    }
}
