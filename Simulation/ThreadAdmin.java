package Simulation;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ThreadAdmin {
    public static Semaphore semaphore = new Semaphore(1,true);
    public static boolean visualisatorWaiting = false;
}
