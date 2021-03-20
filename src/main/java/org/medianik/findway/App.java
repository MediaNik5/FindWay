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

    static{
        logger.setLevel(Level.DEBUG);
    }

    private final Pane pane;
    private final Scene scene;
    private final Manager manager;

    public App(){
        pane = new StackPane();
        scene = new Scene(pane, calculateWidth(), calculateHeight());
        instance = this;
        manager = new Manager(this);
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
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setTitle(Constants.APP_NAME);

        setupIcon(stage);

        manager.init();
    }

    private void setupIcon(Stage stage){
        InputStream inputIcon = getClass().getResourceAsStream("/icon.png");
        Image icon = new Image(inputIcon);
        stage.getIcons().add(icon);
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

    public static App getInstance(){
        return instance;
    }

    public static void main(String... args){
        launch(App.class);
    }

}