package org.medianik.findway.gameobject;

import javafx.scene.Node;

import java.util.List;

public abstract class GameObject{

    protected final int x;
    protected final int y;
    protected final int startingTick;
    protected boolean destroyReady;
    private boolean alive;

    protected GameObject(int x, int y, int startingTick){
        this.x = x;
        this.y = y;
        this.startingTick = startingTick;
        alive = true;
        destroyReady = false;
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

    public void destroy(int tick, List<Node> nodes){
        if(!isAlive())
            throw new IllegalStateException("State of GameObject is dead, why call destroy?");
        alive = false;
    }

    public boolean isAlive(){
        return alive;
    }
}
