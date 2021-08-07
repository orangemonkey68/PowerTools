package me.orangemonkey68.powertools.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

public class NBTHelper {
    public NBTHelper(){

    }

    //returns itemstack with the Powertool tag
    public ItemStack getStackWithTag(ItemStack stack, PowerToolData data) {
        NbtCompound rootTag = stack.getOrCreateNbt();
        if(rootTag.contains("powerTool")){
            return stack;
        } else {
            rootTag.put("powerTools", data.toTag());
            stack.setNbt(rootTag);
            return stack;
        }
    }

    @Nullable
    public PowerToolData getData(ItemStack stack){
        NbtCompound tag = stack.getNbt();

        if(tag == null)
            return null;

        return PowerToolData.fromTag(stack.getNbt().getCompound("powerTools"));
    }

    public ItemStack setGlintOverride(ItemStack stack, boolean shouldGlint) {
        NbtCompound tag = stack.getOrCreateNbt();
        tag.putBoolean("glintOverride", shouldGlint);
        stack.setNbt(tag);
        return stack;
    }
}
