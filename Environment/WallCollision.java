package Environment;

public class WallCollision {
    private LineSegment wall;
    private Coordinate collisionPoint;

    public WallCollision(LineSegment wall, Coordinate collisionPoint) {
        this.wall = wall;
        this.collisionPoint = collisionPoint;
    }

    public LineSegment getWall() {
        return wall;
    }

    public Coordinate getCollisionPoint() {
        return collisionPoint;
    }
}
