package model.agents;

import model.map.*;


import static java.lang.Math.sqrt;

public class ExplorerAgent extends Agent {

    private Vector horizontalDirection;    // direction in which agent will move on wall collision
    private Vector verticalDirection;

    public ExplorerAgent(int id, Coordinate position) {
        super(id, position);
    }

    public ExplorerAgent(Agent a) {
        super(a);
    }

    public void setHorizontalDirection(Vector direction) {
        this.horizontalDirection = direction;
    }

    public void setVerticalDirection(Vector direction) {
        this.verticalDirection = direction;
    }

    @Override
    protected void search() {
        checkForHomes(); // look for nearby homes
        move(); // move to new coordinates
        generateRandSpeed(); // get new speed
    }

    @Override
    protected void travel() {
        if (home == null) {
            state = State.searching;
            timeSpentInState[1]--;
            this.direction = verticalDirection;
            act();
        } else if (Double.compare(position.distanceTo(home.getCoords()), speed) <= 0) {
            position = new Coordinate(home.getCoords());
            state = State.working;
            home.addAgent(this);
        } else {
            checkForHomes();
            move();
            generateRandSpeed();
        }
    }
    @Override
    protected void work() {
        speed = 0;
        CallType call = Double.compare((home.getPollution()), interestBound) >= 0 ? CallType.ATTRACTING : CallType.NONE;
        home.setCall(call);
        if (home == null || !home.decreasePollution(this)) {
            this.home = null;
            state = State.searching;
            timeSpentInState[2]--;
            this.direction = verticalDirection;
            act();
            return;
        }
        totalWork += workRate;


    }

    @Override
    protected void move() {

        while (speed > 0) {

            double c = sqrt(Math.pow(this.speed, 2) /
                    (Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2)));

            Coordinate new_pos = new Coordinate(
                    position.getX() + (c * (direction.getX())),
                    position.getY() + (c * (direction.getY()))
            );

            WallCollision collision = position.checkWalls(new_pos, myMap.getWalls());

            if (collision.getWall() == null) { // if there are no walls between this position and the new one
                position = new_pos;
                this.speed = 0;
            }
            else {
                this.direction = this.direction.reverse();
                c = sqrt(1/(Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2)));
                new_pos = new Coordinate(
                        position.getX() + (c * (direction.getX())),
                        position.getY() + (c * (direction.getY()))
                );
                position = new_pos;
                this.speed -= 1;
                c = sqrt(1/(Math.pow(horizontalDirection.getX(), 2) + Math.pow(horizontalDirection.getY(), 2)));
                new_pos = positionInDirection(horizontalDirection, c);
                collision = position.checkWalls(new_pos, myMap.getWalls());
                if(collision.getWall() == null){
                    this.position = new_pos;
                    this.speed -= 1;
                }
                else {
                    for(int i = 0; i < 4; i++) {
                        if(collision.getWall() == myMap.getWalls().get(i)) {
                            this.horizontalDirection = horizontalDirection.reverse();
                        }
                    }
                    double num = 1;
                    while(collision.getWall() != null) {
                        c = sqrt(1/(Math.pow(direction.getX(), 2) + Math.pow(direction.getY(), 2)));
                        new_pos = new Coordinate(
                                position.getX() + (c * (direction.getX())),
                                position.getY() + (c * (direction.getY()))
                        );
                        position = new_pos;
                        c = sqrt(num/(Math.pow(horizontalDirection.getX(), 2) + Math.pow(horizontalDirection.getY(), 2)));
                        new_pos = new Coordinate(
                                position.getX() + (c * (horizontalDirection.getX())),
                                position.getY() + (c * (horizontalDirection.getY()))
                        );
                        collision = position.checkWalls(new_pos, myMap.getWalls());
                        num /= 2;
                        this.speed -= new_pos.distanceTo(position);
                    }
                    this.position = new_pos;

                }
            }
        }
    }

    private Coordinate positionInDirection(Vector direction, double multiplier) {
        return new Coordinate(
                position.getX() + (multiplier * (direction.getX())),
                position.getY() + (multiplier * (direction.getY()))
        );
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

}
