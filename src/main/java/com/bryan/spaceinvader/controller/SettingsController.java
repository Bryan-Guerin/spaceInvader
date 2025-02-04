package com.bryan.spaceinvader.controller;

import com.bryan.spaceinvader.model.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends BasicController implements Initializable {

    private final static Logger logger = LogManager.getLogger(SettingsController.class);

    public Settings settings = Settings.getInstance();

    @FXML
    public Slider volumeSlider;
    @FXML
    public Slider frequencySlider;
    @FXML
    public ComboBox<Settings.Difficulty> difficultyComboBox;
    @FXML
    private Label moveLeftLabel;
    @FXML
    private Label moveRightLabel;
    @FXML
    private Label shootLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateLabels();

        difficultyComboBox.getItems().setAll(Settings.Difficulty.values());

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            settings.setVolume(newValue.doubleValue());
        });

        frequencySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            settings.setFrequency(newValue.doubleValue());
        });

        logger.info("Settings controller initialized");
    }

    private void updateLabels() {
        moveLeftLabel.setText(settings.getKeyBinding(Settings.GameAction.MOVE_LEFT).getKeyCode().toString());
        moveRightLabel.setText(settings.getKeyBinding(Settings.GameAction.MOVE_RIGHT).getKeyCode().toString());
        shootLabel.setText(settings.getKeyBinding(Settings.GameAction.SHOOT).getKeyCode().toString());

        volumeSlider.setValue(settings.getVolume());
        frequencySlider.setValue(settings.getFrequency());
        difficultyComboBox.setValue(settings.getDifficulty());
    }

    private void listenForKey(Settings.GameAction action, Label label) {
        label.setText("Press a key...");
        label.getScene().setOnKeyPressed(event -> {
            KeyCode newKey = event.getCode();
            if (settings.isKeyAlreadyBound(newKey)) {
                label.setText("Key is already used!");
            } else {
                settings.setKeyBinding(action, newKey);
                label.setText(newKey.toString());
            }
            label.getScene().setOnKeyPressed(null); // Désactive l'écoute après l'input
        });
    }

    public void onSettingsReturnButtonClick(ActionEvent actionEvent) {
        settings.save();
        this.changeCurrentScene(stage, "menu-view.fxml");
    }

    @FXML
    private void onChangeMoveLeft() {
        listenForKey(Settings.GameAction.MOVE_LEFT, moveLeftLabel);
    }

    @FXML
    private void onChangeMoveRight() {
        listenForKey(Settings.GameAction.MOVE_RIGHT, moveRightLabel);
    }

    @FXML
    private void onChangeShoot() {
        listenForKey(Settings.GameAction.SHOOT, shootLabel);
    }

    @FXML
    public void onChangeDifficulty() {
        this.settings.setDifficulty(Settings.Difficulty.valueOf(difficultyComboBox.getValue().toString().toUpperCase()));
    }
}
