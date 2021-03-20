package org.medianik.findway.gameobject;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import org.medianik.findway.annotaion.Event;
import org.medianik.findway.annotaion.EventPriority;

import java.util.List;

import static org.medianik.findway.util.Constants.*;

public class Cell extends GameObject implements MouseHandler{

    private Rectangle rectangle;
    private boolean isBig;

    public Cell(int x, int y, List<Node> nodes){
        super(x, y, nodes);
    }

    @Override
    protected void display(List<Node> nodes){
        rectangle = new Rectangle(CELL_SIZE, CELL_SIZE);
        rectangle.setTranslateX(getX());
        rectangle.setTranslateY(getY());
        nodes.add(rectangle);
        isBig = false;
    }

    @Override
    public int getX(){
        return super.getX()*(CELL_SIZE + GRID_OFFSET);
    }

    @Override
    public int getY(){
        return super.getY()*(CELL_SIZE + GRID_OFFSET);
    }

    @Override
    @Event.Priority(EventPriority.POST_PROCESSING)
    public void handleMouse(MouseEvent event, int tick){
        if(isBig){
            isBig = false;
            rectangle.setWidth(CELL_SIZE);
            rectangle.setHeight(CELL_SIZE);
        }
        if(rectangle.isHover() && !isBig){
            isBig = true;
            rectangle.setWidth(CELL_BIG_SIZE);
            rectangle.setHeight(CELL_BIG_SIZE);
        }

    }

    @Override
    public String toString(){
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
