package org.medianik.findway.gameobject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.List;

import static org.medianik.findway.util.Constants.BUTTON_MIN_HEIGHT;
import static org.medianik.findway.util.Constants.BUTTON_MIN_WIDTH;

public class ButtonWrapper extends GameObject{

    private final String text;
    private final EventHandler<ActionEvent> action;
    private Button button;

    public ButtonWrapper(int x, int y, int startingTick, String text, EventHandler<ActionEvent> action, List<Node> nodes){
        super(x, y, startingTick);
        this.text = text;
        this.action = action;
        display(nodes);
    }

    @Override
    protected void display(List<Node> nodes){
        button = new Button(text);
        button.setTranslateX(getX());
        button.setTranslateY(getY());
        button.setMinSize(BUTTON_MIN_WIDTH, BUTTON_MIN_HEIGHT);
        button.setOnAction(action);
        nodes.add(button);
    }

    @Override
    public int hashCode(){
        return 0;
    }

    @Override
    public void destroy(int tick, List<Node> nodes){
        super.destroy(tick, nodes);
        button.setOnAction(null);
        nodes.remove(button);
    }

    public String getText(){
        return text;
    }
}
