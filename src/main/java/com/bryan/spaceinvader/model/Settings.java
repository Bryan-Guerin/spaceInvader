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
    private static final Path filePath = Paths.get("spaceinader/cfg/settings.json").toAbsolutePath();

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
    private double frequency; // TODO fixer à 60 et retirer le custom
    private Difficulty difficulty;

    @JsonIgnore
    private transient double period;

    private Settings() {
    }

    // Chargement des réglages depuis un fichier JSON
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

    // Méthode pour sauvegarder les réglages dans un fichier JSON
    public void save() {
        if (logger.isDebugEnabled())
            logger.debug("Saving settings");

        File file = filePath.toFile();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            file.getParentFile().mkdirs(); // Crée les dossiers si nécessaire
            mapper.writeValue(file, this);
        } catch (Exception e) {
            logger.error("Error while saving settings: {}", filePath, e);
        }

        if (logger.isInfoEnabled())
            logger.info("Settings saved successfully to file: {}", filePath);
    }

    // Gestion des KeyBindings
    public void setKeyBinding(GameAction action, KeyCode keyCode) {
        keyBindings.put(action, new KeyBind(keyCode));
        save(); // Sauvegarde automatique après modification
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

    public Map<GameAction, KeyBind> getAllKeyBindings() {
        return new HashMap<>(keyBindings);
    }

    public boolean isKeyAlreadyBound(KeyCode keyCode) {
        return keyBindings.values().stream()
                .anyMatch(keyBind -> keyBind.getKeyCode().equals(keyCode));
    }

    private void getDefault() {
        getDefaultKeyBinds();
        difficulty = Difficulty.NORMAL;
        volume = DEFAULT_VOLUME;
        frequency = DEFAULT_FREQUENCY;
    }

    private void getDefaultKeyBinds() {
        keyBindings.put(GameAction.MOVE_LEFT, new KeyBind(KeyCode.Q));
        keyBindings.put(GameAction.MOVE_RIGHT, new KeyBind(KeyCode.D));
        keyBindings.put(GameAction.SHOOT, new KeyBind(KeyCode.SPACE));
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

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
        this.period = 1000.0 / frequency;
    }

    public double getPeriod() {
        this.period = 1000.0 / frequency;
        return period;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

}
