package com.bryan.spaceinvader;

import com.bryan.spaceinvader.controller.BasicController;
import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.game.Game;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.util.Objects.nonNull;

public class InitApplication extends Application {

    private static final Logger logger = LogManager.getLogger(InitApplication.class);

    private FileLock lock;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setOnCloseRequest(_ -> {
            Game.stopPlayerShootingThread();
            if (nonNull(lock)) {
                try {
                    lock.release();
                } catch (IOException ignored) {
                }
            }
            Platform.exit();
            System.exit(0);
        });

        if (!createFileLock()) {
            showDialogBox(stage);
            return;
        }

        initApplicationStage(stage);
    }

    private void initApplicationStage(Stage stage) throws IOException {
        Settings settings = Settings.getInstance();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Space Invaders");
        stage.getIcons().add(ResourceManager.loadResource("logo.png", Image.class, ResourceType.IMAGE));
        stage.setFullScreen(settings.isFullScreen());
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("");

        stage.setWidth(settings.getResolution().getWidth());
        stage.setHeight(settings.getResolution().getHeight());
        stage.centerOnScreen();

        StackPane sceneContainer = new StackPane();
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(sceneContainer);
        AnchorPane.setTopAnchor(sceneContainer, 0.0D);
        AnchorPane.setRightAnchor(sceneContainer, 0.0D);
        AnchorPane.setBottomAnchor(sceneContainer, 0.0D);
        AnchorPane.setLeftAnchor(sceneContainer, 0.0D);

        sceneContainer.getChildren().add(ResourceManager.loadResource("menu-view.fxml", FXMLLoader.class, ResourceType.FXML).load());
        sceneContainer.getStylesheets().add(ResourceManager.computeFullPath("style.css", ResourceType.CSS).toExternalForm());
        sceneContainer.getStylesheets().add(ResourceManager.computeFullPath("menu.css", ResourceType.CSS).toExternalForm());
        Font.loadFont(ResourceManager.computeFullPath("PressStart2P-Regular.ttf", ResourceType.FONT).toExternalForm(), 20);

        stage.setScene(new Scene(anchorPane));
        stage.show();
        BasicController.setStage(stage);
        BasicController.setSceneContainer(sceneContainer);
        logger.info("Application started");
    }

    private void showDialogBox(Stage stage) {
        stage.setTitle("Space Invaders - Error Message");
        Label label = new Label("Another instance of the application is running");
        label.setStyle("-fx-font-size: 14px; -fx-padding: 20 0;");
        Button btn = new Button();
        btn.setText("OK");
        btn.setOnAction(_ -> stage.close());

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(label, btn);
        stage.setScene(new Scene(root, 400, 200));
        stage.show();
    }

    // TODO Create file lock to ensure only one instance is running at a time
    //  Limitation, it depends on the execution path of the application (relative path of the lock).
    //  So, should I switch to absolute path or socket method (to wake up the running instance for example) ?
    private boolean createFileLock() {
        Path filePath = Paths.get("spaceinvader/app.lock").toAbsolutePath();
        try {
            File file = filePath.toFile();
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileChannel fc = FileChannel.open(file.toPath(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
            lock = fc.tryLock();
            if (lock == null) {
                logger.info("Another instance of the application is running");
                return false;
            }
            return true;
        } catch (IOException e) {
            logger.error("Error while creating file lock", e);
            return false;
        }
    }
}