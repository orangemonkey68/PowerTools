package me.orangemonkey68.powertools.common.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.orangemonkey68.powertools.common.api.LogManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ConfigManager {
    private final Gson gson;
    private final LogManager logger;

    private Path configPath;
    private ConfigData cachedConfigData;
    private final ConfigData defaultConfigData;

    public ConfigManager(LogManager logger){
        this.logger = logger;

        this.defaultConfigData = new ConfigData(this);
        defaultConfigData.setBannedCommands(List.of("ban, kick, stop"));
        defaultConfigData.setDangerousPlaceConfirmationTime(30L);
        defaultConfigData.setCheckedItems(List.of("minecraft:boat", "minecraft:minecart", "minecraft:tnt_minecart", "minecraft:chest_minecart", "minecraft:hopper_minecart"));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    public void writeConfig(ConfigData data) {
        String json = gson.toJson(data);
        try {
            FileWriter writer = new FileWriter(configPath.toFile());
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            logger.warnLocalized("error.powertools.unableToSaveConfig");
            logger.getLogger().error(e.getStackTrace());
        }
    }

    public ConfigData getConfig() {
        if (cachedConfigData != null) {
            return cachedConfigData;
        } else {
            ConfigData data;
            try {
                data = gson.fromJson(new FileReader(configPath.toFile()), ConfigData.class);
            } catch (FileNotFoundException e) {
                logger.errorLocalized("error.powertools.configNotFound");
                logger.getLogger().error(e.getStackTrace());
                data = defaultConfigData;
            }
            this.cachedConfigData = data;
            return data;
        }
    }
}
