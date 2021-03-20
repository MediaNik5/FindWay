package org.medianik.findway.gameobject;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.List;

public abstract class GameObject{

    protected final int x;
    protected final int y;
    protected boolean destroyReady;
    private boolean alive;

    protected GameObject(int x, int y, List<Node> nodes){
        this.x = x;
        this.y = y;
        alive = true;
        destroyReady = false;
        display(nodes);
    }

    protected abstract void display(List<Node> nodes);

    @Override
    public abstract int hashCode();

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean destroyReady(){
        return destroyReady && alive;
    }

    public void destroy(int tick, Pane pane){
        if(!isAlive())
            throw new IllegalStateException("State of GameObject is dead, why call destroy?");
        alive = false;
    }

    public boolean isAlive(){
        return alive;
    }
}
