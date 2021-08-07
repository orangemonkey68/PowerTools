package me.orangemonkey68.powertools.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

public interface ItemUseEvent {
    Event<ItemUseEvent> EVENT = EventFactory.createArrayBacked(ItemUseEvent.class,
            (listeners) -> (player, stack, hand) -> {
                for (ItemUseEvent listener : listeners){
                    ActionResult result = listener.interact(player, stack, hand);

                    if(!result.equals(ActionResult.PASS)){
                        return result;
                    }
                }

            return ActionResult.PASS;
    });

    ActionResult interact(ServerPlayerEntity player, ItemStack stack, Hand hand);
}
