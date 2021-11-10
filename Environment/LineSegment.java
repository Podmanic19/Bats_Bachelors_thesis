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

        if (val == 0) return 0; // collinear

        return (val > 0)? 1 : 2; // clock or counterclock wise
    }

    /***
     * @param first first line segment
     * @param second second line segment
     * @return whether they intersect
     */
    static boolean doIntersect(LineSegment first, LineSegment second)
    {
        int o1 = orientation(first.getA(), second.getB(), first.getB());
        int o2 = orientation(first.getA(), second.getA(), second.getB());
        int o3 = orientation(first.getB(), second.getB(), first.getA());
        int o4 = orientation(first.getB(), second.getB(), second.getA());

        // General case
        if (o1 != o2 && o3 != o4) return true;

        // Special Cases
        if (o1 == 0 && onSegment(first.getA(), first.getB(), second.getA())) return true;
        if (o2 == 0 && onSegment(first.getA(), second.getB(), second.getA())) return true;
        if (o3 == 0 && onSegment(first.getB(), first.getA(), second.getB())) return true;
        if (o4 == 0 && onSegment(first.getB(), second.getA(), second.getB())) return true;

        return false;
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
