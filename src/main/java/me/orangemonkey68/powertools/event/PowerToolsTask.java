package me.orangemonkey68.powertools.event;

public class PowerToolsTask {
    private int ticksToWait;
    private final Runnable runnable;

    public PowerToolsTask(int ticksToWait, Runnable runnable){
        this.ticksToWait = ticksToWait;
        this.runnable = runnable;
    }

    public int getTicksToWait(){
        return ticksToWait;
    }

    public void setTicksToWait(int ticks){
        this.ticksToWait = ticks;
    }

    public void execute(){
        runnable.run();
    }
}
