package com.bryan.spaceinvader.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {

    private static final Logger logger = LogManager.getLogger(Settings.class);
    private static final Path filePath = Paths.get("cfg/settings.json").toAbsolutePath();

    private SimpleStringProperty keyMoveLeft;
    private SimpleStringProperty keyMoveRight;
    private SimpleStringProperty keyShoot;

    public Settings() {
        this.keyMoveLeft = new SimpleStringProperty();
        this.keyMoveRight = new SimpleStringProperty();
        this.keyShoot = new SimpleStringProperty();
    }

    public static Settings loadSettings() {
        File file = filePath.toFile();
        Settings settings = new Settings();

        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                settings = mapper.readValue(file, Settings.class);
            } catch (Exception e) {
                if (logger.isErrorEnabled())
                    logger.error("Error while loading settings", e);
            }
        } else {
            settings.setKeyMoveLeft("Q");
            settings.setKeyMoveRight("D");
            settings.setKeyShoot("SPACE");
            try {
                filePath.getParent().toFile().mkdirs();
                filePath.toFile().createNewFile();
            } catch (Exception e) {
                if (logger.isErrorEnabled())
                    logger.error("Error while creating settings file", e);
            }
            settings.save();
        }
        return settings;
    }


    public void save() {
        if (logger.isDebugEnabled())
            logger.debug("Saving settings");

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(filePath.toFile(), this);
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("Error while saving settings", e);
        }

        if (logger.isInfoEnabled())
            logger.info("Settings saved");
    }


    public String getKeyMoveLeft() {
        return keyMoveLeft.get();
    }

    public SimpleStringProperty keyMoveLeftProperty() {
        return keyMoveLeft;
    }

    public void setKeyMoveLeft(String keyMoveLeft) {
        this.keyMoveLeft.set(keyMoveLeft);
    }

    public String getKeyMoveRight() {
        return keyMoveRight.get();
    }

    public SimpleStringProperty keyMoveRightProperty() {
        return keyMoveRight;
    }

    public void setKeyMoveRight(String keyMoveRight) {
        this.keyMoveRight.set(keyMoveRight);
    }

    public String getKeyShoot() {
        return keyShoot.get();
    }

    public SimpleStringProperty keyShootProperty() {
        return keyShoot;
    }

    public void setKeyShoot(String keyShoot) {
        this.keyShoot.set(keyShoot);
    }
}
