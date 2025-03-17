package com.bryan.spaceinvader.controller;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.game.AbsInvader;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.max;

public class GameController extends BasicController implements Initializable {

    private final static Logger logger = LogManager.getLogger(GameController.class);
    private static final Settings settings = Settings.getInstance();

    @FXML
    public StackPane root;
    public BorderPane gameBorderPane;
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
    public VBox scoreBarContainer;
    public VBox scoreBar;
    public VBox healthBarContainer;
    public VBox healthBar;
    public Label shopInfoLabel;
    public Label currentHealthLabel;
    public StackPane gameStackPane;

    private Game game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                        logger.trace("Game loop interrupted");
                    }
                }

                // code à exécuter à chaque itération de la boucle
                game.play();
            }
        };

        Platform.runLater(() -> {
            healthPane.setPrefWidth(max(progressPane.getWidth(), healthPane.getMaxWidth())); // Only for symmetry purposes
            progressPane.setPrefWidth(max(healthPane.getWidth(), progressPane.getMaxWidth()));

            canvas.setWidth(stage.getWidth() - 2 * healthPane.getWidth());
            canvas.setHeight(stage.getHeight());



            AbsInvader.computeInvaderSize(stage.getWidth());
            game = new Game(canvas, scoreBar, healthBar, shopMenu, pauseMenu);

            initBinds();
            stage.getScene().setOnKeyPressed(game::keyPressed);
            stage.getScene().setOnKeyReleased(game::keyReleased);

            gameStackPane.layout();
            timer.start();
        });
    }

    private void initShop() {
        shopInfoLabel.setText("");
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

    // TODO Shop : Lorsque achat, le niveau commence à 0 (et pas 1) donc 9 / 10 est max.
    //  Vider le message d'erreur entre 2 ouvertures de shop
    //  Ajouter indication visuel lorsque level max atteint
    //  Ajouter animation lors de l'achat et un cd pour éviter le multi click (10 ms ?)
    public void onShopButtonClick(MouseEvent event) {
        ShopItemNode itemNode = (ShopItemNode) event.getSource();

        if (itemNode.getShopItem().isLocked()) {
//            shopErrorLabel.setText("Vous devez atteindre le niveau " + itemNode.getShopItem().getType().getUnlockLevel() + " !"); // TODO évoluer la limite de niveau ?
            shopInfoLabel.setText("Niveau maximum atteint !");
        } else {
            if (game.getProgress().getScore() < itemNode.getShopItem().getCost()) {
                shopInfoLabel.setText("Vous n'avez pas assez de points !");
            } else {
                shopInfoLabel.setText("Vous avez amélioré " + itemNode.getShopItem().getType() + " au niveau "
                        + itemNode.getShopItem().getCurrentLevel() + " / " + itemNode.getShopItem().getType().getMaxLevel() + " !");
                game.getProgress().spendScore(itemNode.getShopItem().getCost());
                game.getPlayer().handleUpgrade(itemNode.handlePurchase());
                shopMenu.requestLayout();
            }
        }
        event.consume();
    }

    private void initBinds() {
        double scoreBarHeight = scoreBarContainer.getHeight();
        double healthBarHeight = healthBarContainer.getHeight();

        scoreBar.setMaxHeight(scoreBarHeight);
        scoreBar.setPrefHeight(0);
        scoreBar.setPrefWidth(scoreBarContainer.getWidth());
        healthBar.setMaxHeight(healthBarHeight);
        healthBar.setPrefHeight(healthBarHeight);
        healthBar.setPrefWidth(healthBarContainer.getWidth());

        scoreLabel.textProperty().bind(game.getProgress().scoreProperty());
        currentLevelLabel.textProperty().bind(game.getProgress().currentLevelProperty());
        leftLifeLabel.textProperty().bind(game.getProgress().livesProperty());
        currentHealthLabel.textProperty().bind(game.getPlayer().healthProperty());
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
        System.exit(0);
    }
}
