package org.medianik.findway;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.medianik.findway.util.Constants;

import java.io.InputStream;

import static org.medianik.findway.util.Constants.GLOBAL_OFFSET;

/**
 * JavaFX App
 */
public class App extends Application{

    public static final Logger logger = Logger.getLogger(App.class);
    private static App instance;

    private final Pane pane;
    private final Scene scene;
    private final Manager manager;

    public App(){
        App.logger.log(Level.INFO, "Creating new instance of " + App.class.getName());
        pane = new StackPane();
        scene = new Scene(pane, calculateWidth(), calculateHeight());
        instance = this;
        manager = new Manager(this);
        App.logger.log(Level.INFO, "Completed creating new instance of " + App.class.getName());
    }

    private double calculateWidth(){
        var bounds = Screen.getPrimary().getBounds();
        return bounds.getWidth() - 10*GLOBAL_OFFSET;
    }

    private double calculateHeight(){
        var bounds = Screen.getPrimary().getBounds();
        return bounds.getHeight() - 10*GLOBAL_OFFSET;
    }

    @Override
    public void start(Stage stage){
        App.logger.log(Level.INFO, "Starting an application.");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setTitle(Constants.APP_NAME);

        setupIcon(stage);

        manager.init();
        App.logger.log(Level.INFO, "Application has started.");
    }

    private void setupIcon(Stage stage){
        InputStream inputIcon = getClass().getResourceAsStream("/icon.png");
        Image icon = new Image(inputIcon);
        stage.getIcons().add(icon);
        App.logger.log(Level.INFO, "Icon has set up.");
    }

    public <T extends Event> void addEventHandler(EventType<T> event, EventHandler<? super T> eventHandler){
        scene.addEventHandler(event, eventHandler);
    }

    public <T extends Event> void removeEventHandler(EventType<T> event, EventHandler<? super T> eventHandler){
        scene.removeEventHandler(event, eventHandler);
    }

    public ObservableList<Node> getNodes(){
        return pane.getChildren();
    }

    public double width(){
        return pane.getScene().getWidth();
    }

    public double height(){
        return pane.getScene().getHeight();
    }

    public Manager getManager(){
        return manager;
    }

    @Override
    public String toString(){
        return "App{" +
                "pane=" + pane +
                ", scene=" + scene +
                ", manager=" + manager +
                '}';
    }

    public static App getInstance(){
        return instance;
    }

    public static void main(String... args){
        if(args.length != 0)
            logger.setLevel(Level.toLevel(args[0], Level.DEBUG));
        else
            logger.setLevel(Level.DEBUG);
        launch(App.class);
    }
}