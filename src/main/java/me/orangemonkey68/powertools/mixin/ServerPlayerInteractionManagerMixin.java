package me.orangemonkey68.powertools.mixin;

import me.orangemonkey68.powertools.PowerTools;
import me.orangemonkey68.powertools.event.ItemUseEvent;
import me.orangemonkey68.powertools.nbt.PowerToolData;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;"), method = "interactItem", cancellable = true)
    void injectCallback(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        ItemUseEvent.EVENT.invoker().interact(player, stack, hand);
    }

    @Inject(at = @At(value = "HEAD"), method = "interactBlock", cancellable = true)
    void injectBlockPlaceCheck(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir){
        if(!player.isCreative() && !player.isSpectator()){ //if is survival/adventure
            //if it's a block or a dangerous item that may be consumed
            if (stack.getItem() instanceof BlockItem || PowerTools.getConfig().checkedItems.contains(Registry.ITEM.getId(stack.getItem()).toString())){
                NbtCompound tag = stack.getOrCreateNbt();
                if(tag.contains("powerTools")){ // is a powertool
                    PowerToolData data = PowerToolData.fromTag(tag.getCompound("powerTool"));
                    long cooldownTicks = PowerTools.getConfig().dangerousPlaceCooldownTicks;
                    float ticksSinceLastAttempt = (System.currentTimeMillis() - data.getLastDangerousAttempt())/50f;
                    PowerTools.LOGGER.info("Ticks since last dangerous use {}", ticksSinceLastAttempt);
                    PowerTools.LOGGER.info("Cooldown Ticks: {}", cooldownTicks);
                    PowerTools.LOGGER.info(ticksSinceLastAttempt <= cooldownTicks);

                    if(data.getLastDangerousAttempt() == 0){
                        data.setLastDangerousAttempt(System.currentTimeMillis());
                    }

                    if(ticksSinceLastAttempt >= cooldownTicks){
                        player.sendMessage(new TranslatableText("text.powertools.might-lost-item", cooldownTicks / 20f).formatted(Formatting.RED, Formatting.BOLD), true);
                        data.setLastDangerousAttempt(System.currentTimeMillis());

                        //reset tag data
                        tag.put("powerTools", data.toTag());
                        stack.setNbt(tag);
                        player.setStackInHand(hand, stack);

                        //cancel action
                        cir.setReturnValue(ActionResult.SUCCESS);
                        scheduleInventoryUpdate(player);
                        cir.cancel();
                    } else {
                        data.setLastDangerousAttempt(System.currentTimeMillis());
                    }
                }
            }
        }
    }

    private void scheduleInventoryUpdate(ServerPlayerEntity player){
        MinecraftServer server = player.getServer();
        if(server != null) {
            server.send(new ServerTask(1, () -> {
                server.getPlayerManager().sendPlayerStatus(player);
            }));
        }
    }
}
