package me.orangemonkey68.powertools.common.api;

import java.util.List;
import java.util.UUID;

public class PowerToolData {
    public PowerToolData(UUID owner, List<UUID> allowedPlayers, List<String> commandList, long remainingUses, long cooldownTicks, boolean canUse, boolean canBePlaced) {
        this.owner = owner;
        this.allowedPlayers = allowedPlayers;
        this.commandList = commandList;
        this.remainingUses = remainingUses;
        this.cooldownTicks = cooldownTicks;
        this.canUse = canUse;
        this.canBePlaced = canBePlaced;
    }

    UUID owner;
    List<UUID> allowedPlayers;
    List<String> commandList;
    long remainingUses;
    long cooldownTicks;
    boolean canUse;
    boolean canBePlaced;
    boolean shouldConsume;

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getAllowedPlayers() {
        return allowedPlayers;
    }

    public void setAllowedPlayers(List<UUID> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }

    public List<String> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }

    public long getRemainingUses() {
        return remainingUses;
    }

    public void setRemainingUses(long remainingUses) {
        this.remainingUses = remainingUses;
    }

    public long getCooldownTicks() {
        return cooldownTicks;
    }

    public void setCooldownTicks(long cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public boolean isCanBePlaced() {
        return canBePlaced;
    }

    public void setCanBePlaced(boolean canBePlaced) {
        this.canBePlaced = canBePlaced;
    }

    public boolean shouldConsume() {
        return shouldConsume;
    }

    public void setShouldConsume(boolean consume) {
        this.shouldConsume = consume;
    }

}
