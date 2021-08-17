package me.orangemonkey68.powertools.common.api.events;

import me.orangemonkey68.powertools.common.api.ActionResult;
import me.orangemonkey68.powertools.common.api.PowerToolData;
import me.orangemonkey68.powertools.common.api.wrapper.APIPlayer;

import java.util.ArrayList;
import java.util.List;

//TODO: Maybe generalize the Event class to repeat less code? Look at Fabric event system
public class PlayerUsePowerToolEvent {
    private final List<Listener> listeners = new ArrayList<>();

    public void invoke(boolean isBlock, String id, APIPlayer player, PowerToolData data){
        for (Listener listener : listeners) {
            ActionResult result = listener.execute(isBlock, id, player, data);

            if(result != ActionResult.PASS){
                break;
            }
        }
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    @FunctionalInterface
    interface Listener {
        ActionResult execute(boolean isBlock, String id, APIPlayer player, PowerToolData data);
    }
}
