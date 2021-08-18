package me.orangemonkey68.powertools.common.api.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigData {
    public ConfigData (ConfigManager manager) {
        this.manager = manager;
    }

    private final ConfigManager manager;

    private List<String> bannedCommands = new ArrayList<>();
    private List<String> checkedItems = new ArrayList<>();
    private long dangerousPlaceConfirmationTime;

    public List<String> getBannedCommands() {
        return bannedCommands;
    }

    public List<String> getCheckedItems() {
        return checkedItems;
    }

    public long getDangerousPlaceConfirmationTime() {
        return dangerousPlaceConfirmationTime;
    }

   public void setBannedCommands(List<String> bannedCommands) {
        this.bannedCommands = bannedCommands;
       manager.writeConfig(this);
   }

    public void setCheckedItems(List<String> checkedItems) {
        this.checkedItems = checkedItems;
        manager.writeConfig(this);
    }

    public void setDangerousPlaceConfirmationTime(long dangerousPlaceConfirmationTime) {
        this.dangerousPlaceConfirmationTime = dangerousPlaceConfirmationTime;
        manager.writeConfig(this);
    }

}
