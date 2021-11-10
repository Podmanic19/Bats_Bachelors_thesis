package Environment;

import java.util.Random;

public class EnvironmentParameters {
    public int AGENT_NUM = 60;
    public int POINT_MAX = 800;
    public int POINT_MIN = 0;
    public int MIN_DISTANCE = 50;
    public int MIN_WORK = 10;
    public int MAX_WORK = 100;
    public int NUMBER_HOME = 100;
    public int ATTRACTION_DISTANCE = 100;
    public Random GENERATOR = new Random(0);

    public void setAGENT_NUM(int AGENT_NUM) {
        this.AGENT_NUM = AGENT_NUM;
    }

    public void setPOINT_MAX(int POINT_MAX) {
        this.POINT_MAX = POINT_MAX;
    }

    public void setPOINT_MIN(int POINT_MIN) {
        this.POINT_MIN = POINT_MIN;
    }

    public void setMIN_DISTANCE(int MIN_DISTANCE) {
        this.MIN_DISTANCE = MIN_DISTANCE;
    }

    public void setMIN_WORK(int MIN_WORK) {
        this.MIN_WORK = MIN_WORK;
    }

    public void setMAX_WORK(int MAX_WORK) {
        this.MAX_WORK = MAX_WORK;
    }

    public void setNUMBER_HOME(int NUMBER_HOME) {
        this.NUMBER_HOME = NUMBER_HOME;
    }

    public void setATTRACTION_DISTANCE(int ATTRACTION_DISTANCE) {
        this.ATTRACTION_DISTANCE = ATTRACTION_DISTANCE;
    }

    public void setGENERATOR(int seed) {
        this.GENERATOR = new Random(seed);
    }
}