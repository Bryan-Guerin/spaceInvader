package com.bryan.spaceinvader.controller;

import javafx.fxml.FXML;

public class MenuController extends BasicController {

    @FXML
    public void onPlayButtonClick() {
        this.changeCurrentScene(stage, "game-view.fxml", false);
    }

    @FXML
    public void onSettingsButtonClick() {
        this.changeCurrentScene(stage, "settings-view.fxml");
    }

    @FXML
    public void onQuitButtonClick() {
        System.exit(0);
    }

}