package com.bryan.spaceinvader.controller;

import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BasicController {

    private static final Logger logger = LogManager.getLogger(BasicController.class);

    public static Stage stage;
    public Scene scene;

    protected void changeCurrentScene(Stage stage, String sceneName, boolean switchFullScreen) {
        this.scene = new Scene(ResourceManager.loadResource(sceneName, Parent.class, ResourceType.FXML), 1920, 1080);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setFullScreen(switchFullScreen);
        scene.getStylesheets().add(ResourceManager.computeFullPath("style.css", ResourceType.CSS).toExternalForm());
        scene.getStylesheets().add(ResourceManager.computeFullPath(getCSSName(sceneName), ResourceType.CSS).toExternalForm());

        if (logger.isDebugEnabled())
            logger.debug("Change scene to {} in stage {}", sceneName, stage.getTitle());
    }

    protected void changeCurrentScene(Stage stage, String sceneName) {
        changeCurrentScene(stage, sceneName, false);
    }

    public static void setStage(Stage stage) {
        BasicController.stage = stage;
    }

    private String getCSSName(String sceneName) {
        return sceneName.substring(0, sceneName.length() - 10) + ".css";
    }
}
