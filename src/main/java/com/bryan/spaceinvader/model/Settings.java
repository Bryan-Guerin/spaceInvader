package com.bryan.spaceinvader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Settings implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final double DEFAULT_VOLUME = 1.0;
    private static final double DEFAULT_FREQUENCY = 60.0;

    private static final Logger logger = LogManager.getLogger(Settings.class);
    private static final Path filePath = Paths.get("spaceinvader/cfg/settings.json").toAbsolutePath();

    @JsonIgnore
    private static Settings instance;

    public static Settings getInstance() {
        if (instance == null)
            instance = loadSettings();
        return instance;
    }

    @JsonProperty(value = "allKeyBindings")
    private final HashMap<GameAction, KeyBind> keyBindings = new HashMap<>();

    private double volume;
    private Difficulty difficulty;
    @JsonProperty(value = "fullScreen")
    private Boolean fullscreen;
    private Resolution resolution;

    private Settings() {
    }

    private static Settings loadSettings() {
        File file = filePath.toFile();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Beau formatage du JSON
        Settings settings = new Settings();

        if (file.exists()) {
            try {
                settings = mapper.readValue(file, Settings.class);
                logger.info("Settings loaded successfully from file: {}", filePath);
            } catch (Exception e) {
                logger.error("Error loading settings from file: {}", filePath, e);
            }
        } else {
            settings.getDefault();
            settings.save();
        }
        return settings;
    }

    public void save() {
        if (logger.isDebugEnabled())
            logger.debug("Saving settings");

        File file = filePath.toFile();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            file.getParentFile().mkdirs();
            mapper.writeValue(file, this);
        } catch (Exception e) {
            logger.error("Error while saving settings: {}", filePath, e);
        }

        if (logger.isInfoEnabled())
            logger.info("Settings saved successfully to file: {}", filePath);
    }

    private void getDefault() {
        getDefaultKeyBinds();
        fullscreen = true;
        difficulty = Difficulty.NORMAL;
        volume = DEFAULT_VOLUME;
        resolution = Resolution.R1920x1080;
    }

    private void getDefaultKeyBinds() {
        keyBindings.put(GameAction.MOVE_LEFT, new KeyBind(KeyCode.Q));
        keyBindings.put(GameAction.MOVE_RIGHT, new KeyBind(KeyCode.D));
        keyBindings.put(GameAction.SHOOT, new KeyBind(KeyCode.SPACE));
    }

    public void setKeyBinding(GameAction action, KeyCode keyCode) {
        keyBindings.put(action, new KeyBind(keyCode));
        save();
    }

    public KeyBind getKeyBinding(GameAction action) {
        return keyBindings.get(action);
    }

    public GameAction getGameActionByKeyCode(KeyCode keyCode) {
        return keyBindings.entrySet().stream()
                .filter(entry -> entry.getValue().getKeyCode().equals(keyCode))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Check if a key is already bound
     * Exclude the given action
     * @return true if the key is already bound to another action
     */
    public boolean isKeyAlreadyBound(Settings.GameAction action, KeyCode keyCode) {
        return keyBindings.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(action))
                .anyMatch(entry -> entry.getValue().getKeyCode().equals(keyCode));
    }

    public enum GameAction {
        MOVE_LEFT,
        MOVE_RIGHT,
        SHOOT
    }

    public enum Difficulty {
        EASY,
        NORMAL,
        HARD
    }

    public enum Resolution {
        R2048x1080("2048x1080", 2048, 1080),
        R1920x1080("1920x1080", 1920, 1080),
        R1280x720("1280x720", 1280, 720),
        R800x600("800x600", 800, 600);

        private final String value;
        private final double width;
        private final double height;

        Resolution(String value, double width, double height) {
            this.value = value;
            this.width = width;
            this.height = height;
        }

        public String getValue() {
            return value;
        }

        public static Resolution fromValue(String value) {
            String str = 'R' + value;
            return valueOf(str);
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }

    // Permet d'indiquer au process de serialisation comment instancier la classe
    @Serial
    private Object readResolve() {
        return getInstance();
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @JsonIgnore
    public double getPeriod() {
        return 1000.0 / DEFAULT_FREQUENCY;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isFullScreen() {
        return fullscreen;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

}
