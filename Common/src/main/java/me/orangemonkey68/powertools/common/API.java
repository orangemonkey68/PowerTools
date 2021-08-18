package me.orangemonkey68.powertools.common;

import me.orangemonkey68.powertools.common.api.LocalizationManager;
import me.orangemonkey68.powertools.common.api.LogManager;
import me.orangemonkey68.powertools.common.api.Scheduler;

public interface API {
    LogManager getLogManager();

    LocalizationManager getLocalizationManager();

    Scheduler getScheduler();


}
