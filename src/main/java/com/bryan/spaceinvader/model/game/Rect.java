package com.bryan.spaceinvader.model.game;

import com.bryan.spaceinvader.model.invader.AbsInvader;
import javafx.scene.canvas.Canvas;

// Const of the rect around invaders and define different properties like gap, spacing and size
// No idea how to name it better
public class Rect {

    public double leftGap;
    public double topGap;
    public double xGap;
    public double yGap;

    public Rect(Canvas canvas, GameConfig gameConfig) {
        xGap = 2 * AbsInvader.SIZE;
        yGap = 1.5 * AbsInvader.SIZE;
        leftGap = ((int) ((canvas.getWidth() - gameConfig.getNumberOfColumns() * xGap + AbsInvader.SIZE) / 2));
        topGap = canvas.getHeight() * 0.05;
    }

}
