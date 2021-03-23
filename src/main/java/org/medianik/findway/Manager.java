package org.medianik.findway;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.medianik.findway.annotaion.Event;
import org.medianik.findway.annotaion.EventPriority;
import org.medianik.findway.astar.AStar;
import org.medianik.findway.astar.Grid;
import org.medianik.findway.exception.EnvironmentHolder;
import org.medianik.findway.gameobject.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.medianik.findway.util.Constants.*;
import static org.medianik.findway.util.Util.invokeIfNeeded;

public class Manager{

    private final Grid grid;
    private final Set<GameObject> gameObjects;
    private final Set<MouseEvent> mouseEvents;
    private final Set<KeyEvent> keyEvents;
    private final App app;
    private Timeline astarAlgorithm = null;
    private Timeline main;
    private int tick;

    public Manager(App app){
        grid = new Grid(app);
        gameObjects = ConcurrentHashMap.newKeySet(NUMBER_OF_CELLS*NUMBER_OF_CELLS);
        mouseEvents = ConcurrentHashMap.newKeySet();
        keyEvents = ConcurrentHashMap.newKeySet();
        this.app = app;
    }

    public void init(){
        App.logger.log(Level.INFO, "Initializing " + Manager.class.getName());
        tick = 0;

        grid.init(gameObjects);
        initButtons();

        app.addEventHandler(MouseEvent.ANY, this::handleEvent);
        app.addEventHandler(KeyEvent.ANY, this::handleEvent);

        main = new Timeline(new KeyFrame(Duration.millis(MILLIS_BETWEEN_TICKS), this::execute));
        main.setCycleCount(Animation.INDEFINITE);
        main.play();

        App.logger.log(Level.INFO, "Completed initializing of " + Manager.class.getName());
    }

    private void initButtons(){
        var startButton = new ButtonWrapper(
                (int) (-app.width()/2 + GLOBAL_OFFSET*10),
                GLOBAL_OFFSET/2 + BUTTON_MIN_HEIGHT/2,
                0,
                START_BUTTON_TEXT,
                this::runAStar,
                app.getNodes()
        );
        var resetButton = new ButtonWrapper(
                (int) (-app.width()/2 + GLOBAL_OFFSET*10),
                -GLOBAL_OFFSET/2 - BUTTON_MIN_HEIGHT/2,
                0,
                RESET_BUTTON_TEXT,
                this::reset,
                app.getNodes()
        );
        gameObjects.add(startButton);
        gameObjects.add(resetButton);
    }

    private void runAStar(ActionEvent ignored){
        if(astarAlgorithm == null){
            astarAlgorithm = new Timeline(new KeyFrame(Duration.millis(DEFAULT_START_DELAY), e -> new AStar(grid).run(DEFAULT_PROCESS_DELAY)));
            astarAlgorithm.play();
        }
    }

    private void reset(ActionEvent event){
        App.logger.log(Level.INFO, "Starting resetting of " + Manager.class.getName());
        if(astarAlgorithm != null)
            astarAlgorithm.stop();
        astarAlgorithm = null;
        grid.clear();

        for(var go : gameObjects)
            go.destroy(tick, app.getNodes());
        gameObjects.clear();
        mouseEvents.clear();
        keyEvents.clear();

        app.removeEventHandler(MouseEvent.ANY, this::handleEvent);
        app.removeEventHandler(KeyEvent.ANY, this::handleEvent);

        main.stop();
        App.logger.log(Level.INFO, "Completed resetting of " + Manager.class.getName());

        init();
    }

    private void handleEvent(javafx.event.Event event){
        if(event instanceof MouseEvent)
            this.mouseEvents.add((MouseEvent) event);
        if(event instanceof KeyEvent)
            this.keyEvents.add((KeyEvent) event);
    }

    public void execute(ActionEvent ignored){
        App.logger.log(Level.TRACE, "Starting tick " + ++tick);

        for(var priority : EventPriority.values()){
            try {
                process(gameObjects, priority);
            }catch(ReflectiveOperationException e){
                App.logger.log(Level.FATAL, "Unexpected fatal ERROR happened. Environment holder is passed.");
                App.logger.log(Level.ERROR, new EnvironmentHolder(this));
                e.printStackTrace();
                System.exit(1);
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
            if(go instanceof CustomEventHandler)
                handleCustomEvents(priority, (CustomEventHandler) go);
            if(go instanceof TickHandler)
                handleTick(priority, (TickHandler) go);
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

    @Override
    public String toString(){
        return "Manager{" +
                "grid=" + grid +
                ", gameObjects=" + gameObjects +
                ", mouseEvents=" + mouseEvents +
                ", keyEvents=" + keyEvents +
                ", tick=" + tick +
                '}';
    }

    public int getTick(){
        return tick;
    }
}
