package com.bryan.spaceinvader;

import com.bryan.spaceinvader.controller.BasicController;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class InitApplication extends Application {

    private static final Logger logger = LogManager.getLogger(InitApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(ResourceManager.loadResource("menu-view.fxml", FXMLLoader.class, ResourceType.FXML).load(), 320, 240);
        stage.setTitle("Space Invaders");
        stage.setScene(scene);
        stage.show();
        BasicController.setStage(stage);
        Platform.setImplicitExit(false);
        logger.info("Application started");
    }

    public static void main(String[] args) {
        launch();
    }
}