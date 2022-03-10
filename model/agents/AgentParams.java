package model.agents;

import javafx.beans.property.SimpleBooleanProperty;
import model.serialization.Save;

public class AgentParams extends Save {
    public String NAME = "Default";
    public double LEFT = 0.8;
    public double RIGHT = 0.8;
    public double FORWARD = 4;
    public double BACK = 1;
    public int SIGHT = 100;
    public double FOV = 60;
    public double WORK_RATE = 0.5;
    public int INTEREST_BOUNDARY = 40;
    public double SPEED_MIN = 2.5;
    public double SPEED_MAX = 11.5;
    public boolean AVOID_OTHERS = false;
    public boolean REPULSIVE_CALL = false;
    public boolean DECISIVE = false;
    public SpeedDistribution SPEED_TYPE = SpeedDistribution.GAUSSIAN;

    private boolean selected = false;

    public SimpleBooleanProperty getSelected(){
        return new SimpleBooleanProperty(false);
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public String getNAME() {
        return NAME;
    }

    public double getLEFT() {
        return LEFT;
    }

    public double getRIGHT() {
        return RIGHT;
    }

    public double getFORWARD() {
        return FORWARD;
    }

    public double getBACK() {
        return BACK;
    }

    public int getSIGHT() {
        return SIGHT;
    }

    public double getFOV() {
        return FOV;
    }

    public double getWORK_RATE() {
        return WORK_RATE;
    }

    public int getINTEREST_BOUNDARY() {
        return INTEREST_BOUNDARY;
    }

    public double getSPEED_MIN() {
        return SPEED_MIN;
    }

    public double getSPEED_MAX() {
        return SPEED_MAX;
    }

    public boolean isAVOID_OTHERS() {
        return AVOID_OTHERS;
    }

    public boolean isREPULSIVE_CALL() {
        return REPULSIVE_CALL;
    }

    public boolean isDECISIVE() {
        return DECISIVE;
    }

    public SpeedDistribution getSPEED_TYPE() {
        return SPEED_TYPE;
    }
}
