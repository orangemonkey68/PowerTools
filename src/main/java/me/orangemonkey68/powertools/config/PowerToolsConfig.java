package me.orangemonkey68.powertools.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = "powertools")
public class PowerToolsConfig implements ConfigData {
    public List<String> bannedCommands = List.of("stop", "ban", "kick");
    public List<String> checkedItems = List.of("minecraft:boat", "minecraft:minecart");
    public long dangerousPlaceCooldownTicks = 30;
}
