package model.map;

import java.io.Serializable;

public class Vector implements Serializable {
    private double x;
    private double y;

    public Vector(Coordinate A, Coordinate B) {
        this.x = B.getX() - A.getX();
        this.y = B.getY() - A.getY();
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector A) {
        this.x = A.getX();
        this.y = A.getY();
    }

    public void add(Vector A) {
        this.x += A.getX();
        this.y += A.getY();
    }

    public void multiply(double multiplier) {
        this.x *= multiplier;
        this.y *= multiplier;
    }

    public void subtract(Vector A) {
        this.add(A.reverse());
    }

    public double absValue() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double scalarProduct(Vector B) {
        return this.x * B.getX() + y * B.getY();
    }

    public double angleBetween(Vector B) { // returns the degrees between this vector and vector B
        return Math.toDegrees(Math.acos(scalarProduct(B) / (this.absValue() * B.absValue())));
    }

    public double signedAngleBetween(Vector B) { // returns the degrees between this vector and vector B
        double angle = -Math.toDegrees(Math.acos(scalarProduct(B) / (this.absValue() * B.absValue())));
        if (Double.compare(this.x * B.getY() - this.y * B.getX(), 0) < 0)
            angle = -angle;
        return angle;
    }

    public void rotate(double angle) { // rotates the vector counterclockwise by angle in radians
        double rx = (this.x * Math.cos(angle)) - (this.y * Math.sin(angle));
        double ry = (this.x * Math.sin(angle)) + (this.y * Math.cos(angle));
        this.x = rx;
        this.y = ry;
    }

    public Vector reverse() {
        return new Vector(-this.x, -this.y);
    }

    public Vector reverseX() {
        return new Vector(-this.x, this.y);
    }

    public Vector reverseY() {
        return new Vector(this.x, -this.y);
    }

    public Vector getNormal() {
        return new Vector(-this.y, this.x);
    }

    public Vector copy() {
        return new Vector(this);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean goesUp() {
        return this.y > 0;
    }

    public boolean goesRight() {
        return this.x > 0;
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

    @Override
    public String toString() {
        return ("(" + String.format("%.2f", x) + ", " + String.format("%.2f", y) + ")");
    }

}
