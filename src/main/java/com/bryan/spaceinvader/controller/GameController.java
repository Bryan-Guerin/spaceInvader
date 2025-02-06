package com.bryan.spaceinvader.controller;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.game.Game;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController extends BasicController implements Initializable {

    private static final Settings settings = Settings.getInstance();

    @FXML
    public StackPane root;
    public BorderPane gamePane;
    public Canvas canvas;
    public VBox progressPane;
    public Label scoreLabel;
    public ProgressBar progressBar;
    public Label currentLevelLabel;
    public Label rightLifeLabel;
    public ProgressBar healthBar;
    public VBox healthPane;
    public VBox shopMenu;
    public HBox shopLine1;
    public HBox shopLine2;
    public VBox pauseMenu;

    private Game game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.setHeight(1080);
        stage.setWidth(1920);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(null);
        game = new Game(canvas);

        progressBar.progressProperty().bind(game.getProgress().progressProperty());
        scoreLabel.textProperty().bind(game.getProgress().scoreProperty());
        currentLevelLabel.textProperty().bind(game.getProgress().currentLevelProperty());
        rightLifeLabel.textProperty().bind(game.getProgress().livesProperty());

        pauseMenu.setVisible(false);
        shopMenu.setVisible(false);

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                long deltaTime = now - lastTime;
                lastTime = now;

                if (deltaTime < settings.getPeriod()) {
                    try {
                        Thread.sleep((long) (settings.getPeriod() - deltaTime));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                // code à exécuter à chaque itération de la boucle
                game.play();
            }
        };

        Platform.runLater(() -> {
            stage.getScene().setOnKeyPressed(game::keyPressed);
            stage.getScene().setOnKeyReleased(game::keyReleased);
            timer.start();
        });
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        game.save();
        this.changeCurrentScene(stage, "menu-view.fxml");
    }

    public void onNextLevelButtonClick(ActionEvent actionEvent) {
        game.startNextLevel();
    }

    public void onPauseButtonClick(ActionEvent actionEvent) {

    }

    public void onSettingsButtonClick(ActionEvent actionEvent) {

    }

    public void onQuitButtonClick(ActionEvent actionEvent) {

    }
}
