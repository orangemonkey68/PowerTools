package me.orangemonkey68.powertools.common;

import me.orangemonkey68.powertools.common.api.LocalizationManager;
import me.orangemonkey68.powertools.common.api.LogManager;

public interface API {
    LogManager getLogManager();

    LocalizationManager getLocalizationManager();

}
