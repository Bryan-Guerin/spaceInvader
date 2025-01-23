package com.bryan.spaceinvader.controller;

import com.bryan.spaceinvader.model.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

@NoArgsConstructor
public class SettingsController extends BasicController implements Initializable {

//    TODO ajouter initialize pour remplir les champs avec les settings

    private final static Logger logger = LogManager.getLogger(SettingsController.class);

    @FXML
    public Button settingsShootButton;
    @FXML
    public Button settingsMoveLeftButton;
    @FXML
    public Button settingsMoveRightButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        settings =Settings.loadSettings();
        settings.keyShootProperty().bindBidirectional(settingsShootButton.textProperty());
        settings.keyMoveLeftProperty().bindBidirectional(settingsMoveLeftButton.textProperty());
        settings.keyMoveRightProperty().bindBidirectional(settingsMoveRightButton.textProperty());
        logger.info("Settings controller initialized");
    }

    public void onSettingsShootButtonClick(ActionEvent actionEvent) {
        logger.debug("Settings shoot button clicked");
    }

    public void onSettingsMoveLeftButtonClick(ActionEvent actionEvent) {
        logger.debug("Settings move left button clicked");
    }

    public void onSettingsMoveRightButtonClick(ActionEvent actionEvent) {
        logger.debug("Settings move right button clicked");

    }

    public void onSettingsReturnButtonClick(ActionEvent actionEvent) {
        settings.save();
        BasicController.settings = settings;
        this.changeCurrentScene(stage, "menu-view.fxml");
    }
}
