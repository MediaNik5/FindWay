package org.medianik.findway;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.log4j.Level;
import org.junit.jupiter.api.Test;
import org.medianik.findway.annotaion.EventPriority;
import org.medianik.findway.gameobject.Cell;
import org.medianik.findway.gameobject.GameObject;
import org.medianik.findway.gameobject.MouseHandler;
import org.medianik.findway.gameobject.TickHandler;

import java.lang.reflect.InvocationTargetException;

import static org.medianik.findway.util.Util.invokeIfNeeded;

public class EventTest{

    static{
        App.main();
    }

    @Test
    public void testEventPriority() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        App.logger.log(Level.INFO, "Starting test testEventPriority.");
        var gos = new GameObject[]{new Cell(0, 0, 0, new Pane().getChildren())};

        for(var priority : EventPriority.values())
            process(gos, priority);
        App.logger.log(Level.INFO, "Ending test testEventPriority.");
    }

    private void process(GameObject[] gos, EventPriority priority) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        App.logger.log(Level.INFO, "Running " + priority);
        for(var go : gos){
            if(go instanceof TickHandler)
                handleTick(priority, (TickHandler) go);
            if(go instanceof MouseHandler)
                handleClick(priority, (MouseHandler) go);
        }
    }

    private void handleTick(EventPriority priority, TickHandler handler) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        var method = handler.getClass().getMethod("tick", int.class);
        invokeIfNeeded(priority, handler, method, 0);
    }

    private void handleClick(EventPriority priority, MouseHandler handler) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        var method = handler.getClass().getMethod("handleMouse", MouseEvent.class);
        invokeIfNeeded(priority, handler, method, (MouseEvent) null);
    }
}
