package model.testing;

import java.io.Serializable;

public class EnvironmentParameters implements Serializable {
    public int DYNAMIC_HOME_SPAWN_TIME = 0;
    public double DYNAMIC_HOME_GROWTH_SIZE = 0;

    public EnvironmentParameters() {
    }

    public EnvironmentParameters(int DYNAMIC_HOME_SPAWN_TIME, double DYNAMIC_HOME_GROWTH_SIZE) {
        this.DYNAMIC_HOME_SPAWN_TIME = DYNAMIC_HOME_SPAWN_TIME;
        this.DYNAMIC_HOME_GROWTH_SIZE = DYNAMIC_HOME_GROWTH_SIZE;
    }
}
