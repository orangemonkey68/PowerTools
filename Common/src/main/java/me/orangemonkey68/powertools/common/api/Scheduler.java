package me.orangemonkey68.powertools.common.api;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private Scheduler() {}

    private static final Scheduler INSTANCE = new Scheduler();
    public static Scheduler getInstance() {
        return INSTANCE;
    }

    private final List<Task> tasks = new ArrayList<>();
    private final List<Task> tasksToRemove = new ArrayList<>();

    /**
     * A simple tick loop that starts by clearing any dead tasks, then loops over living tasks, and if they
     * have any ticks left, decrements them. If their ticks are at 0, the task is run then queued for deletion, which
     * will happen at the start of the next iteration.
     */
    public void tick(){
        clearDeadTasks();

        tasks.forEach(task -> {
            if(task.ticks <= 0){
                task.runnable.run();
                queueDeadTask(task);
            } else {
                task.ticks += -1;
            }
        });
    }

    public void scheduleTask(Task task){
        tasks.add(task);
    }

    public void scheduleTask(Runnable runnable, int ticks){
        scheduleTask(new Task(runnable, ticks));
    }

    public void scheduleNextTick(Runnable runnable){
        scheduleTask(runnable, 0);
    }

    private void queueDeadTask(Task task){
        tasksToRemove.add(task);
    }

    private void clearDeadTasks() {
        tasksToRemove.forEach(tasksToRemove::remove);
        tasksToRemove.clear();
    }



    public static class Task {
        public Task(Runnable runnable, int ticks){
            this.runnable = runnable;
            this.ticks = ticks;
        }

        protected final Runnable runnable;
        protected int ticks;
    }
}
