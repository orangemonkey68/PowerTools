package me.orangemonkey68.powertools.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin{
    @Shadow @Nullable public abstract NbtCompound getNbt();

    @Shadow public abstract NbtCompound getOrCreateNbt();

    @Inject(at = @At(value = "HEAD"), method = "hasGlint", cancellable = true)
    void injectGlintCheck(CallbackInfoReturnable<Boolean> cir){
        if(this.getOrCreateNbt().getBoolean("glintOverride")){ //if it should override, return true
            cir.setReturnValue(true);
            cir.cancel();
        }

        //else do normal check
    }
}
