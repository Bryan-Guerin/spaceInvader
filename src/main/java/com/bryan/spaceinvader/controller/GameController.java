package com.bryan.spaceinvader.controller;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.game.Game;
import com.bryan.spaceinvader.model.shop.ShopItem;
import com.bryan.spaceinvader.model.shop.ShopItemNode;
import com.bryan.spaceinvader.model.shop.ShopItemType;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    public Label currentLevelLabel;
    public Label leftLifeLabel;
    public VBox healthPane;
    public VBox shopMenu;
    public HBox shopLine1;
    public HBox shopLine2;
    public VBox pauseMenu;
    public VBox scoreProgressBar;
    public VBox scoreBar;
    public VBox healthProgressBar;
    public VBox healthBar;
    public Label shopErrorLabel;

    private Game game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.setHeight(1080);
        stage.setWidth(1920);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(null);
        game = new Game(canvas, scoreBar, healthBar, shopMenu, pauseMenu);

        initBinds();
        initShop();

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

    private void initShop() {
        for (ShopItemType itemType : ShopItemType.values()) {
            ShopItemNode shopItemNode = new ShopItemNode(new ShopItem(itemType));
            shopItemNode.setOnMouseClicked(this::onShopButtonClick);
            if (itemType.ordinal() < ShopItemType.values().length / 2) {
                shopLine1.getChildren().add(shopItemNode);
            } else {
                shopLine2.getChildren().add(shopItemNode);
            }
        }
    }

    public void onShopButtonClick(MouseEvent event) {
        ShopItemNode itemNode = (ShopItemNode) event.getSource();

        if (itemNode.getShopItem().isLocked()) {
            shopErrorLabel.setVisible(true);
//            shopErrorLabel.setText("Vous devez atteindre le niveau " + itemNode.getShopItem().getType().getUnlockLevel() + " !"); // TODO évoluer la limite de niveau ?
            shopErrorLabel.setText("Niveau maximum atteint !");
        } else {
            if (game.getProgress().getScore() < itemNode.getShopItem().getCost()) {
                shopErrorLabel.setVisible(true);
                shopErrorLabel.setText("Vous n'avez pas assez de points !"); // TODO message custom ?
            } else {
                shopErrorLabel.setVisible(false);
                game.getProgress().spendScore(itemNode.getShopItem().getCost());
                game.getPlayer().handleUpgrade(itemNode.handlePurchase());
                shopMenu.requestLayout();
            }
        }
        event.consume();
    }

    private void initBinds() {

        double scoreProgressBarHeight = 800;
        double healthProgressBarHeight = 800;

        scoreBar.setMaxHeight(scoreProgressBarHeight);
        scoreBar.setPrefHeight(0);
        scoreBar.setPrefWidth(scoreProgressBar.getWidth());
        healthBar.setMaxHeight(healthProgressBarHeight);
        healthBar.setPrefHeight(healthProgressBarHeight);
        healthBar.setPrefWidth(healthProgressBar.getWidth());

        scoreLabel.textProperty().bind(game.getProgress().scoreProperty());
        currentLevelLabel.textProperty().bind(game.getProgress().currentLevelProperty());
        leftLifeLabel.textProperty().bind(game.getProgress().livesProperty());
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        game.save();
//        this.changeCurrentScene(stage, "menu-view.fxml");
    }

    public void onNextLevelButtonClick(ActionEvent actionEvent) {
        shopMenu.setVisible(false);
        game.startNextLevel();
    }

    public void onPauseButtonClick(ActionEvent actionEvent) {
        game.handlePauseResume();
    }

    public void onSettingsButtonClick(ActionEvent actionEvent) {

    }

    public void onQuitButtonClick(ActionEvent actionEvent) {

    }
}
