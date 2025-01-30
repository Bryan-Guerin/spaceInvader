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

    protected void changeCurrentScene(Stage stage, String sceneName, boolean switchFullScreen) {
        Scene scene = new Scene(ResourceManager.loadResource(sceneName, Parent.class, ResourceType.FXML));
        stage.setScene(scene);
        stage.setFullScreen(switchFullScreen);

        if (logger.isDebugEnabled())
            logger.debug("Change scene to {} in stage {}", sceneName, stage.getTitle());
    }

    protected void changeCurrentScene(Stage stage, String sceneName) {
        changeCurrentScene(stage, sceneName, false);
    }

    public static void setStage(Stage stage) {
        BasicController.stage = stage;
    }
}
