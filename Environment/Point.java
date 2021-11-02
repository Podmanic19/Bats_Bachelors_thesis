package Environment;

import java.io.Serializable;

public class Point implements Serializable {
    private final Coordinate coords;
    private Home home;
    private boolean isWall;

    public Point(Coordinate c, boolean isWall) {
        this.coords = new Coordinate(c);
        this.isWall = isWall;
        this.home = null;
    }

    public void deleteHome(){
        this.home = null;
    }

    public boolean isHome(){
        return this.home != null;
    }

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }

        if (!(o instanceof Point)) {
            return false;
        }
        Point c = (Point) o;

        return (this.coords.getX() == c.coords.getX() && this.coords.getX() == c.coords.getY()) ;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Home getHome() {
        if(this.isHome()) return home;
        else return null;
    }

    public Coordinate getCoords() {
        return coords;
    }

    public boolean isWall() {
        return isWall;
    }
}
