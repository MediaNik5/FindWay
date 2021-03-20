package org.medianik.findway;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.medianik.findway.annotaion.Event;
import org.medianik.findway.annotaion.EventPriority;
import org.medianik.findway.gameobject.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.medianik.findway.util.Constants.MILLIS_BETWEEN_TICKS;
import static org.medianik.findway.util.Constants.NUMBER_OF_CELLS;
import static org.medianik.findway.util.Util.invokeIfNeeded;

public class Manager{

    private final Set<GameObject> gameObjects;
    private final Set<MouseEvent> mouseEvents;
    private final Set<KeyEvent> keyEvents;
    private final App app;
    private int tick;

    public Manager(App app){
        gameObjects = ConcurrentHashMap.newKeySet(NUMBER_OF_CELLS*NUMBER_OF_CELLS);
        mouseEvents = ConcurrentHashMap.newKeySet();
        keyEvents = ConcurrentHashMap.newKeySet();
        this.app = app;
    }

    public void init(){
        initGrid();
        app.addEventHandler(MouseEvent.ANY, this::handleEvent);
        app.addEventHandler(KeyEvent.ANY, this::handleEvent);

        tick = 0;

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(MILLIS_BETWEEN_TICKS), e -> execute()));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
    }

    private void initGrid(){
        for(int x = -NUMBER_OF_CELLS/2; x <= NUMBER_OF_CELLS/2; x++)
            for(int y = -NUMBER_OF_CELLS/2; y <= NUMBER_OF_CELLS/2; y++)
                gameObjects.add(new Cell(x, y, app.getNodes()));
    }

    private void handleEvent(javafx.event.Event event){
        if(event instanceof MouseEvent)
            this.mouseEvents.add((MouseEvent) event);
        if(event instanceof KeyEvent)
            this.keyEvents.add((KeyEvent) event);
    }

    public void execute(){
        App.logger.log(Level.TRACE, "Starting tick " + ++tick);

        for(var priority : EventPriority.values()){
            try {
                process(gameObjects, priority);
            }catch(ReflectiveOperationException e){
                assert false : "This should not happen.";
            }
        }

        mouseEvents.clear();
        keyEvents.clear();
        App.logger.log(Level.TRACE, "Ending tick " + tick);
    }

    private void process(Set<GameObject> gos, EventPriority priority) throws ReflectiveOperationException{
        App.logger.log(Level.TRACE, "Running " + priority);
        for(var go : gos){
            if(!mouseEvents.isEmpty() && go instanceof MouseHandler)
                handleMouse(priority, (MouseHandler) go);
            if(!keyEvents.isEmpty() && go instanceof KeyHandler)
                handleKey(priority, (KeyHandler) go);
            if(go instanceof TickHandler)
                handleTick(priority, (TickHandler) go);
            if(go instanceof CustomEventHandler)
                handleCustomEvents(priority, (CustomEventHandler) go);
        }
    }

    private void handleMouse(EventPriority priority, @NotNull MouseHandler handler) throws ReflectiveOperationException{
        var method = handler.getClass().getMethod("handleMouse", MouseEvent.class, int.class);
        for(var e : new HashSet<>(mouseEvents))
            invokeIfNeeded(priority, handler, method, e, tick);
    }

    private void handleKey(EventPriority priority, @NotNull KeyHandler handler) throws ReflectiveOperationException{
        Method checker = handler.getClass().getMethod("isKeyToHandle", KeyCode.class);
        for(var event : new HashSet<>(keyEvents)){
            if((boolean) checker.invoke(handler, event.getCode())){
                var method = handler.getClass().getMethod("handleKey", KeyEvent.class, int.class);
                invokeIfNeeded(priority, handler, method, event, tick);
            }
        }
    }

    private void handleTick(EventPriority priority, @NotNull TickHandler handler) throws ReflectiveOperationException{
        var method = handler.getClass().getMethod("tick", int.class);
        invokeIfNeeded(priority, handler, method, tick);
    }

    private void handleCustomEvents(EventPriority priority, CustomEventHandler handler) throws InvocationTargetException, IllegalAccessException{
        var methods = Arrays.stream(handler.getClass().getDeclaredMethods()).filter((e) -> e.isAnnotationPresent(Event.class)).toArray(Method[]::new);
        for(var method : methods)
            invokeIfNeeded(priority, handler, method, tick);
    }
}
