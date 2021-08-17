package me.orangemonkey68.powertools.common.api;

import org.apache.logging.log4j.Logger;

import java.util.ResourceBundle;

public class LogManager {
    public LogManager(Logger logger, ResourceBundle resources){
        this.logger = logger;
        this.resources = resources;
    }

    private final Logger logger;
    private final ResourceBundle resources;

    public Logger getLogger() {
        return logger;
    }

    public void infoLocalized(String key){
        logger.info(resources.getString(key));
    }

    public void warnLocalized(String key){
        logger.warn(resources.getString(key));
    }

    public void errorLocalized(String key){
        logger.error(resources.getString(key));
    }

    public void debugLocalized(String key){
        logger.debug(resources.getString(key));
    }

    public String getLocalizedText(String key){
        return resources.getString(key);
    }

}
