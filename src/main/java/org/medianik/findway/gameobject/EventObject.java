package org.medianik.findway.gameobject;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.log4j.Level;
import org.medianik.findway.App;
import org.medianik.findway.annotaion.Event;
import org.medianik.findway.annotaion.EventPriority;

import java.util.List;

public class EventObject extends GameObject implements CustomEventHandler, KeyHandler{
    public EventObject(int x, int y, List<Node> nodes){
        super(x, y, nodes);
    }

    @Override
    public boolean isKeyToHandle(KeyCode key){
        return false;
    }

    @Override
    public void handleKey(KeyEvent key, int tick){

    }

    @Event
    @Event.Priority(EventPriority.PRE_PROCESSING)
    public void event1(int tick){
        App.log.log(Level.DEBUG, "Invoked");
    }

    @Override
    protected void display(List<Node> nodes){

    }
}
