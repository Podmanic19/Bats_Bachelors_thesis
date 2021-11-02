package Main;

import Classes.Agent;
import Environment.Environment;
import Environment.Point;

import java.io.Serializable;
import java.util.ArrayList;

public class Simulation implements Serializable {
    private Point[][] map;
    private ArrayList<SimulationStep> steps;
    private static Simulation instance = null;

    private Simulation()
    {
        this.steps = new ArrayList<SimulationStep>();
    }

    public static Simulation getInstance()
    {
        if (instance == null)
            instance = new Simulation();

        return instance;
    }

    public void addStep(SimulationStep step){
        steps.add(step);
    }

    public ArrayList<SimulationStep> getSteps() {
        return steps;
    }
}
