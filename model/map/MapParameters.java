package model.map;

import java.io.Serializable;

public class MapParameters implements Serializable {
    public String name = "DEFAULT";
    public int AGENT_NUM = 20;
    public int POINT_MAX = 1000;
    public int POINT_MIN = 0;
    public int MIN_DISTANCE = 50;
    public int MIN_WORK = 10;
    public int MAX_WORK = 100;
    public int NUMBER_HOME = 100;
    public int WALLS_NUM = 0;
    public int WALL_LENGTH_MIN = 50;
    public int WALL_LENGTH_MAX = 150;
}