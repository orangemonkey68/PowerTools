package me.orangemonkey68.powertools.nbt;

import net.minecraft.nbt.*;

import java.util.List;
import java.util.UUID;

public class PowerToolData {
    public PowerToolData(List<String> commandList, long cooldownTicks, long maxUses, boolean shouldConsume, List<UUID> allowedPlayers, long lastUsedTime, boolean canBeUsedDangerously) {
        this.commandList = commandList;
        this.cooldownTicks = cooldownTicks;
        this.maxUses = maxUses;
        this.shouldConsume = shouldConsume;
        this.allowedPlayers = allowedPlayers;
        this.lastUsedTime = lastUsedTime;
        this.canBeUsedDangerously = canBeUsedDangerously;
    }

    private List<String> commandList;
    private long cooldownTicks;
    private Long maxUses;
    private boolean shouldConsume;
    private List<UUID> allowedPlayers;
    private long lastUsedTime;
    private boolean canBeUsedDangerously;


    public static PowerToolData fromTag(NbtCompound tag){
        return new PowerToolData(
                tag.getList("commandList", 8).stream().map(nbtElement -> ((NbtString)nbtElement).asString()).toList(),
                tag.getLong("cooldownTicks"),
                tag.getLong("maxUses"),
                tag.getBoolean("shouldConsume"),
                tag.getList("allowedPlayers", 11).stream().map(nbtElement -> NbtHelper.toUuid((NbtIntArray)nbtElement)).toList(),
                tag.getLong("lastUsedTime"),
                tag.getBoolean("canBeUsedDangerously"));
    }

    public NbtCompound toTag(){
        NbtCompound tag = new NbtCompound();
        NbtList commandListTag = new NbtList();
        NbtList allowedPlayersTag = new NbtList();

        commandList.forEach(command -> commandListTag.add(commandListTag.size(), NbtString.of(command)));
        allowedPlayers.forEach(uuid -> allowedPlayersTag.add(allowedPlayersTag.size(), NbtHelper.fromUuid(uuid)));

        tag.put("commandList", commandListTag);
        tag.putLong("cooldownTicks", cooldownTicks);
        tag.putLong("maxUses", maxUses);
        tag.putBoolean("shouldConsume", shouldConsume);
        tag.put("allowedPlayers", allowedPlayersTag);
        tag.putLong("lastUsedTime", lastUsedTime);
        tag.putBoolean("canBeUsedDangerously", canBeUsedDangerously);

        return tag;
    }

    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }

    public void setCooldownTicks(long cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    public void setAllowedPlayers(List<UUID> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }

    public List<String> getCommandList() {
        return commandList;
    }

    public long getCooldownTicks() {
        return cooldownTicks;
    }

    public Long getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Long maxUses) {
        this.maxUses = maxUses;
    }

    public boolean isShouldConsume() {
        return shouldConsume;
    }

    public void setShouldConsume(boolean shouldConsume) {
        this.shouldConsume = shouldConsume;
    }


    public List<UUID> getAllowedPlayers() {
        return allowedPlayers;
    }

    public boolean canBeUsedDangerously() {
        return canBeUsedDangerously;
    }

    public void setCanBeUsedDangerously(boolean canBeUsedDangerously) {
        this.canBeUsedDangerously = canBeUsedDangerously;
    }

    public long getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(long lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }





}
