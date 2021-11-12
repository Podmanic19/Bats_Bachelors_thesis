package Environment;

import java.io.Serializable;

public class Vector implements Serializable {
    private double x;
    private double y;

    public Vector(Coordinate A, Coordinate B){
        this.x = B.getX() - A.getX();
        this.y = B.getY() - A.getY();
    }

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector(Vector A){
        this.x = A.getX();
        this.y = A.getY();
    }

    public double absValue(){
        return  Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    public double scalarProduct(Vector B){
        return this.x * B.getX() + y * B.getY();
    }

    public double angleBetween(Vector B){               // returns the degrees between this vector and vector B
        return scalarProduct(B)/(this.absValue() * B.absValue());
    }

    public Vector reverse_vec(){
        return new Vector(- this.x, - this.y);
    }
    public Vector reverse_x(){
        return new Vector(- this.x, this.y);
    }
    public Vector reverse_y(){
        return new Vector(this.x, - this.y);
    }
    public Vector copy(){
        return new Vector(this);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(Coordinate A, Coordinate B) {

        this.x = B.getX() - A.getX();

    }

    public void setY(Coordinate A, Coordinate B) {

        this.y = B.getY() - A.getY();

    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
