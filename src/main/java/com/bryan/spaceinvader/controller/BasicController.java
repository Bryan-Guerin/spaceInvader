package com.bryan.spaceinvader.controller;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BasicController {

    private static final Logger logger = LogManager.getLogger(BasicController.class);

    public static Stage stage;
    public static StackPane sceneContainer;
    public final Settings settings = Settings.getInstance();

    protected void changeCurrentScene(Stage stage, String sceneName, boolean switchFullScreen) {
        Node root = ResourceManager.loadResource(sceneName, Parent.class, ResourceType.FXML);
        sceneContainer.getChildren().clear();
        sceneContainer.getChildren().add(root);


        if (switchFullScreen) {
            if (!stage.isFullScreen())
                stage.setFullScreen(true);
        } else {
            stage.setFullScreen(false);
            stage.setWidth(settings.getResolution().getWidth());
            stage.setHeight(settings.getResolution().getHeight());
            stage.centerOnScreen();
        }

        stage.getScene().getStylesheets().add(ResourceManager.computeFullPath(getCSSName(sceneName), ResourceType.CSS).toExternalForm());

        if (logger.isDebugEnabled())
            logger.debug("Change scene to {} in stage {}", sceneName, stage.getTitle());
    }

    protected void changeCurrentScene(Stage stage, String sceneName) {
        changeCurrentScene(stage, sceneName, settings.isFullScreen());
    }

    public static void setStage(Stage stage) {
        BasicController.stage = stage;
    }

    public static void setSceneContainer(StackPane sceneContainer) {
        BasicController.sceneContainer = sceneContainer;
    }

    private String getCSSName(String sceneName) {
        return sceneName.substring(0, sceneName.length() - 10) + ".css";
    }
}
