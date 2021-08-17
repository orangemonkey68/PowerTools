package me.orangemonkey68.powertools.common.api;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private ResourceBundle resources;

    public LocalizationManager(ResourceBundle resources) {
        this.resources = resources;
    }

    public void setResourceBundle(ResourceBundle resources) {
        this.resources = resources;
    }

    public ResourceBundle getResourceBundle(ResourceBundle resources){
        return resources;
    }

    public String getLocalizedString(String key) {
        return resources.getString(key);
    }

    public String getLocalizedString(String key, String... arguments) {
        String localizedString = resources.getString(key);

        for (String argument : arguments) {
            if(localizedString.contains("%s")) {
                localizedString = localizedString.replaceFirst("%s", argument);
            } else {
                break; //If it falls to the else block, there are no more instances of "%s" to replace and should exit.
            }
        }

        return localizedString;
    }

    public Locale getLocale () {
        return resources.getLocale();
    }
}
