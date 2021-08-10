package me.orangemonkey68.powertools.event;

import java.util.ArrayList;
import java.util.List;

public class PowerToolsScheduler {
    private static final PowerToolsScheduler INSTANCE = new PowerToolsScheduler();
    private PowerToolsScheduler(){}

    private final List<PowerToolsTask> tasks = new ArrayList<>();
    private final List<PowerToolsTask> tasksToRemove = new ArrayList<>();
    private boolean isDirty = false;

    public static PowerToolsScheduler getInstance(){
        return INSTANCE;
    }

    public void scheduleTask(PowerToolsTask task){
        tasks.add(task);
    }

    public void scheduleTask(int ticksToWait, Runnable runnable){
        scheduleTask(new PowerToolsTask(ticksToWait, runnable));
    }

    public void runNextTick(Runnable runnable){
        scheduleTask(0, runnable);
    }

    public void tick() {
        for (PowerToolsTask task : tasks) {
            if(task.getTicksToWait() == 0){
                task.execute();
                queRemoval(task);
            } else {
                task.setTicksToWait(task.getTicksToWait() - 1);
            }
        }
    }

    private void queRemoval(PowerToolsTask task) {
        this.isDirty = true; //mark dirty
        tasksToRemove.add(task);
    }

    public void cleanTasks() {
        tasksToRemove.forEach(tasks::remove);
    }

    public boolean isDirty() {
        return isDirty;
    }

}
