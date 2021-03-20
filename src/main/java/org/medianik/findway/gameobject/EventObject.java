package org.medianik.findway.gameobject;

import javafx.scene.Node;

import java.util.List;
import java.util.Objects;

public class EventObject extends GameObject implements CustomEventHandler{
    public EventObject(int x, int y, List<Node> nodes){
        super(x, y, nodes);
    }

    @Override
    protected void display(List<Node> nodes){

    }


    @Override
    public int hashCode(){
        return Objects.hash(getX(), getY());
    }
}
