package org.medianik.findway;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.log4j.Level;
import org.junit.jupiter.api.Test;
import org.medianik.findway.annotaion.Event;
import org.medianik.findway.annotaion.EventPriority;
import org.medianik.findway.gameobject.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


import static org.medianik.findway.util.Util.invokeIfNeeded;

public class EventTest{

    static {
        App.main();
    }
    @Test
    public void testEventPriority() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        App.log.log(Level.INFO, "Starting test testEventPriority.");
        var gos = new GameObject[]{new Cell(0, 0, new Pane().getChildren())};

        for(var priority : EventPriority.values())
            process(gos, priority);
        App.log.log(Level.INFO, "Ending test testEventPriority.");
    }

    private void process(GameObject[] gos, EventPriority priority) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        App.log.log(Level.INFO, "Running " + priority);
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

    @Test
    public void testCustomEvents() throws InvocationTargetException, IllegalAccessException{
        EventObject eventHandler = new EventObject(0, 0, new Pane().getChildren());
        for(var m : Arrays.stream(eventHandler.getClass().getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Event.class)).toArray(Method[]::new)){
            m.invoke(eventHandler, 0);
        }
    }
}
