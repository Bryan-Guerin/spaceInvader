package com.bryan.spaceinvader.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.ResourceBundle;

@NoArgsConstructor
public class MenuController extends BasicController {

    @FXML
    public void onPlayButtonClick() {
        this.changeCurrentScene(stage, "game-view.fxml");
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