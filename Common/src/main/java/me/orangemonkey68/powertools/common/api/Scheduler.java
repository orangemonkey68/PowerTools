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

    /**
     * Adds a task to the queue
     * @param task The task to be queued
     */
    public void scheduleTask(Task task){
        tasks.add(task);
    }

    /**
     * A convenience method that just calls {@link #scheduleTask(Task)} with a new Task object
     * @param runnable The runnable passed into the {@link Task}
     * @param ticks The number of ticks to wait
     */
    public void scheduleTask(Runnable runnable, int ticks){
        scheduleTask(new Task(runnable, ticks));
    }

    /**
     * A convenience method that calls {@link #scheduleTask(Runnable, int)} with a 0 tick wait.
     * @param runnable The runnable passed into the {@link Task}
     */
    public void scheduleNextTick(Runnable runnable){
        scheduleTask(runnable, 0);
    }

    //Queues task to be deleted
    private void queueDeadTask(Task task){
        tasksToRemove.add(task);
    }

    //Clears executed tasks from the task list
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
