package model.agents;

import controller.SpeedDistribution;
import model.serialization.Save;

public class AgentParams extends Save {
    public String NAME = "Default";
    public double LEFT = 0.8;
    public double RIGHT = 0.8;
    public double FORWARD = 4;
    public double BACK = 1;
    public int SIGHT = 150;
    public double FOV = 60;
    public double WORK_RATE = 0.5;
    public int INTEREST_BOUNDARY = 40;
    public double SPEED_MIN = 2.5;
    public double SPEED_MAX = 11.5;
    public boolean AVOID_OTHERS = false;
    public boolean REPULSIVE_CALL = false;
    public SpeedDistribution SPEED_TYPE = SpeedDistribution.GAUSSIAN;


}
