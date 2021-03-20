package org.medianik.findway.gameobject;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum CellType{
    START(Paint.valueOf("#3c3345")),
    END(Color.BLACK),
    WALL(Paint.valueOf("#454247")),
    EMPTY(Color.GRAY);

    private final Paint paint;

    CellType(Paint paint){
        this.paint = paint;
    }

    public Paint getPaint(){
        return paint;
    }
}
