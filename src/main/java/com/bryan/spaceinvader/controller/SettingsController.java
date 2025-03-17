package com.bryan.spaceinvader.controller;

import com.bryan.spaceinvader.model.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SettingsController extends BasicController implements Initializable {

    private final static Logger logger = LogManager.getLogger(SettingsController.class);

    @FXML
    public Slider volumeSlider;
    @FXML
    public ComboBox<Settings.Difficulty> difficultyComboBox;
    @FXML
    public ComboBox<String> resolutionComboBox;
    @FXML
    public CheckBox fullscreenCheckBox;
    @FXML
    private Label moveLeftLabel;
    @FXML
    private Label moveRightLabel;
    @FXML
    private Label shootLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initFields();

        difficultyComboBox.getItems().setAll(Settings.Difficulty.values());
        resolutionComboBox.getItems().setAll(Arrays.stream(Settings.Resolution.values()).map(Settings.Resolution::getValue).toList());

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> settings.setVolume(newValue.doubleValue()));

        logger.info("Settings controller initialized");
    }

    private void initFields() {
        moveLeftLabel.setText(settings.getKeyBinding(Settings.GameAction.MOVE_LEFT).getKeyCode().toString());
        moveRightLabel.setText(settings.getKeyBinding(Settings.GameAction.MOVE_RIGHT).getKeyCode().toString());
        shootLabel.setText(settings.getKeyBinding(Settings.GameAction.SHOOT).getKeyCode().toString());

        volumeSlider.setValue(settings.getVolume());
        difficultyComboBox.setValue(settings.getDifficulty());
        resolutionComboBox.setValue(settings.getResolution().getValue());
//        TODO 8 : add fullscreen option
//        fullscreenCheckBox.setSelected(settings.isFullScreen());
    }

    private void listenForKey(Settings.GameAction action, Label label) {
        label.setText("Press a key...");
        label.getScene().setOnKeyPressed(event -> {
            KeyCode newKey = event.getCode();
            if (settings.isKeyAlreadyBound(action, newKey)) {
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

    @FXML
    public void onChangeResolution() {
        this.settings.setResolution(Settings.Resolution.fromValue(resolutionComboBox.getValue()));
    }

    @FXML
    public void onChangeFullscreen() {
        this.settings.setFullscreen(fullscreenCheckBox.isSelected());
    }
}
