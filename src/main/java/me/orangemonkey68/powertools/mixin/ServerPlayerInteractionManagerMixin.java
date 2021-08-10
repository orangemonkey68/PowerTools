package me.orangemonkey68.powertools.mixin;

import me.orangemonkey68.powertools.PowerTools;
import me.orangemonkey68.powertools.event.ItemUseEvent;
import me.orangemonkey68.powertools.event.PowerToolsScheduler;
import me.orangemonkey68.powertools.nbt.PowerToolData;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;"), method = "interactItem", cancellable = true)
    void injectCallback(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        ItemUseEvent.EVENT.invoker().interact(player, stack, hand);
    }

    @Inject(at = @At(value = "HEAD"), method = "interactBlock", cancellable = true)
    void injectBlockPlaceCheck(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir){
        if(!player.isCreative() && !player.isSpectator()){ //if is survival/adventure
            //if it's a block or a dangerous item that may be consumed
            if (stack.getItem() instanceof BlockItem || PowerTools.getConfig().checkedItems.contains(Registry.ITEM.getId(stack.getItem()).toString())){
                NbtCompound itemTag = stack.getOrCreateNbt();
                if(itemTag.contains("powerTools")) { //it's a powertool!
                    NbtCompound powerToolsTag = itemTag.getCompound("powerTools");
                    PowerToolData data = PowerToolData.fromTag(powerToolsTag);

                    boolean canBeUsed = data.canBeUsedDangerously();
                    int cooldownTicks = PowerTools.getConfig().dangerousPlaceCooldownTicks;

                    if (!canBeUsed) { //The item is in default state
                        player.sendMessage(new TranslatableText("text.powertools.might-lost-item", cooldownTicks / 20f).formatted(Formatting.BOLD, Formatting.RED), true);
                        data.setCanBeUsedDangerously(true); //set this to true, schedule callback to change it back to false in N ticks


                        //update data of current stack
                        itemTag.put("powerTools", data.toTag());
                        stack.setNbt(itemTag);
                        player.setStackInHand(hand, stack);

                        MinecraftServer server = player.getServer();
                        if(server != null) {
                            PowerToolsScheduler.getInstance().scheduleTask(PowerTools.getConfig().dangerousPlaceCooldownTicks, () -> {
                                PowerTools.LOGGER.info(stack.toString());
                                NbtCompound futureNbt = stack.getOrCreateNbt();
                                PowerToolData futureData = PowerToolData.fromTag(futureNbt.getCompound("powerTools"));
                                futureData.setCanBeUsedDangerously(false); //change this to false
                                futureNbt.put("powerTools", futureData.toTag()); //update NBT with new PowerTools tag
                                stack.setNbt(futureNbt);
                            });
                        }

                        scheduleInventoryUpdate(player);

                        //return SUCCESS to stop processing. FAIL might work as well, but could have more side effects
                        //this could cause a BlockItem with durability to lose durability despite not being used,
                        //however the game forbids this combo AFAIK. If you're reading this and have a solution, open a PR
                        cir.setReturnValue(ActionResult.SUCCESS);
                        cir.cancel();
                    } //else: do nothing, this should run the normal right-click behavior.
                }
            }
        }
    }

    private void scheduleInventoryUpdate(ServerPlayerEntity player){
        PowerToolsScheduler.getInstance().runNextTick(() -> {
            MinecraftServer server = player.getServer();
            if (server != null) {
                server.getPlayerManager().sendPlayerStatus(player);
            }
        });
    }
}
