package me.orangemonkey68.powertools.mixin;

import me.orangemonkey68.powertools.event.PowerToolsScheduler;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "tick", at = @At(value = "TAIL"))
    void injectTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
//        PowerToolsScheduler.getInstance().tick();
        PowerToolsScheduler scheduler = PowerToolsScheduler.getInstance();
        scheduler.tick();
        if(scheduler.isDirty()){
            scheduler.cleanTasks();
        }
    }
}
