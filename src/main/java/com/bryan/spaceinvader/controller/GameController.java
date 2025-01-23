package com.bryan.spaceinvader.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.ResourceBundle;

@NoArgsConstructor
public class GameController extends BasicController implements Initializable {

    @FXML
    public StackPane root;
    public BorderPane gamePane;
    public VBox scorePane;
    public Canvas canvas;
    public VBox progressPane;
    public Label scoreLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.widthProperty().addListener((observable, oldValue, newValue) -> refreshUI());
        stage.heightProperty().addListener((observable, oldValue, newValue) -> refreshUI());
    }

    private void refreshUI() {
        // Forcer un recalcul visuel
        root.requestLayout();
        scorePane.requestLayout();
        progressPane.requestLayout();
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //ReDraw the canvas
    }
}
