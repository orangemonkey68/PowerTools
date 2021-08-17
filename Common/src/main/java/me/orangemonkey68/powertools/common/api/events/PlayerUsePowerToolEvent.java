package me.orangemonkey68.powertools.common.api.events;

import me.orangemonkey68.powertools.common.api.ActionResult;
import me.orangemonkey68.powertools.common.api.PowerToolData;
import me.orangemonkey68.powertools.common.api.wrapper.APIPlayer;

public interface PlayerUsePowerToolEvent {
    Event<PlayerUsePowerToolEvent> EVENT = EventFactory.createArrayBacked(
            PlayerUsePowerToolEvent.class,
            (listeners) -> (id, player, data, usedOnBlock) -> {
                    for(PlayerUsePowerToolEvent listener : listeners) {
                        ActionResult result = listener.interact(id, player, data, usedOnBlock);

                        if(result != ActionResult.PASS) {
                            return result;
                        }
                    }

                    return ActionResult.PASS;
            }
    );

    ActionResult interact(String id, APIPlayer player, PowerToolData data, boolean usedOnBlock);
}
