package me.orangemonkey68.powertools.common.api;

import org.apache.logging.log4j.Logger;

@SuppressWarnings("ClassCanBeRecord")
public class LogManager {
    public LogManager(Logger logger, LocalizationManager localization){
        this.logger = logger;
        this.localization = localization;
    }

    private final Logger logger;
    private final LocalizationManager localization;

    public Logger getLogger() {
        return logger;
    }

    public void infoRaw(String message) {
        logger.info(message);
    }

    public void infoRaw(String message, String... args){
        logger.info(message, (Object) args);
    }

    public void debugRaw(String message) {
        logger.debug(message);
    }

    public void debugRaw(String message, String... args){
        logger.debug(message, (Object) args);
    }

    public void infoLocalized(String key){
        logger.info(localization.getLocalizedString(key));
    }

    public void infoLocalized(String key, String... args){
        logger.info(localization.getLocalizedString(key, args));
    }

    public void warnLocalized(String key){
        logger.warn(localization.getLocalizedString(key));
    }

    public void warnLocalized(String key, String... args){
        logger.warn(localization.getLocalizedString(key, args));
    }

    public void errorLocalized(String key){
        logger.error(localization.getLocalizedString(key));
    }

    public void errorLocalized(String key, String... args){
        logger.error(localization.getLocalizedString(key, args));
    }

}
