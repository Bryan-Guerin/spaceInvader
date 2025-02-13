package com.bryan.spaceinvader;

import com.bryan.spaceinvader.controller.BasicController;
import com.bryan.spaceinvader.model.game.Game;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class InitApplication extends Application {

    private static final Logger logger = LogManager.getLogger(InitApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(ResourceManager.loadResource("menu-view.fxml", FXMLLoader.class, ResourceType.FXML).load());
        stage.getIcons().add(ResourceManager.loadResource("logo.png", Image.class, ResourceType.IMAGE));
        scene.getStylesheets().add(ResourceManager.computeFullPath("style.css", ResourceType.CSS).toExternalForm());
        scene.getStylesheets().add(ResourceManager.computeFullPath("menu.css", ResourceType.CSS).toExternalForm());
        Font.loadFont(ResourceManager.computeFullPath("PressStart2P-Regular.ttf", ResourceType.FONT).toExternalForm(), 20);
        stage.setTitle("Space Invaders");
        stage.setMaximized(true);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Game.stopPlayerShootingThread();
                Platform.exit();
                System.exit(0);
            }
        });
        stage.setScene(scene);
        stage.show();
        BasicController.setStage(stage);
        logger.info("Application started");
    }

    public static void main(String[] args) {
        launch();
    }
}