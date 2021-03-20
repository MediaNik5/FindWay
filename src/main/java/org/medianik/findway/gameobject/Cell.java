package org.medianik.findway.gameobject;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import org.apache.log4j.Level;
import org.medianik.findway.App;
import org.medianik.findway.annotaion.Event;
import org.medianik.findway.annotaion.EventPriority;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.medianik.findway.util.Constants.*;
import static org.medianik.findway.util.Util.inEpsilonRange;
import static org.medianik.findway.util.Util.isPresent;

public class Cell extends GameObject implements MouseHandler{

    private static final AtomicReference<Cell> startCell = new AtomicReference<>(null);
    private static final AtomicReference<Cell> endCell = new AtomicReference<>(null);

    private static final AtomicReference<CellType> lastType = new AtomicReference<>(null);

    private final SimpleObjectProperty<CellType> type = new SimpleObjectProperty<>();
    private final ContextMenu contextMenu;
    private Rectangle rectangle;
    private boolean isBig;

    public Cell(int x, int y, List<Node> nodes){
        super(x, y, nodes);
        type.addListener(this::onTypeChange);
        type.set(CellType.EMPTY);

        contextMenu = new ContextMenu();
        initContextMenu();
        rectangle.setOnContextMenuRequested(e ->
                contextMenu.show(rectangle, e.getScreenX(), e.getScreenY()));
    }

    private void onTypeChange(Observable observable){
        rectangle.setFill(type.getValue().getPaint());

        if(this.equals(startCell.get()) && type.get() != CellType.START)
            startCell.set(null);
        else if(this.equals(endCell.get()) && type.get() != CellType.END)
            endCell.set(null);
    }

    private void initContextMenu(){
        MenuItem start = new MenuItem("Mark as start.");
        start.setOnAction(event -> replaceWithThis(CellType.START, startCell));

        MenuItem end = new MenuItem("Mark as end.");
        end.setOnAction(event -> replaceWithThis(CellType.END, endCell));

        MenuItem empty = new MenuItem("Mark as empty.");
        empty.setOnAction(event -> this.type.set(CellType.EMPTY));

        MenuItem wall = new MenuItem("Mark as wall.");
        wall.setOnAction(event -> this.type.set(CellType.WALL));

        contextMenu.getItems().addAll(start, end, empty, wall);
    }

    private void replaceWithThis(CellType type, AtomicReference<Cell> cell){
        this.type.set(type);
        Cell old = cell.getAndSet(this);
        if(isPresent(old) && !this.equals(old))
            old.type.set(CellType.EMPTY);
    }

    @Override
    protected void display(List<Node> nodes){
        rectangle = new Rectangle(CELL_SIZE, CELL_SIZE);
        rectangle.setTranslateX(getX());
        rectangle.setTranslateY(getY());
        nodes.add(rectangle);
        isBig = false;
    }

    @Override
    public int getX(){
        return super.getX()*(CELL_SIZE + GRID_OFFSET);
    }

    @Override
    public int getY(){
        return super.getY()*(CELL_SIZE + GRID_OFFSET);
    }

    @Override
    @Event.Priority(EventPriority.POST_PROCESSING)
    public void handleMouse(MouseEvent event, int tick){
        if(isBig){
            isBig = false;
            rectangle.setWidth(CELL_SIZE);
            rectangle.setHeight(CELL_SIZE);
            App.logger.log(Level.TRACE, this + " has been made normal.");
        }
        if(isMouseOver(event.getX(), event.getY()) && !isBig){
            isBig = true;
            rectangle.setWidth(CELL_BIG_SIZE);
            rectangle.setHeight(CELL_BIG_SIZE);
            App.logger.log(Level.TRACE, this + " has been made big.");
        }
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED && isMouseOver(event.getX(), event.getY())){
            lastType.set(this.type.get() == CellType.WALL ? CellType.EMPTY : CellType.WALL);
        }
        if(event.isPrimaryButtonDown() && isMouseOver(event.getX(), event.getY())){
            App.logger.log(Level.TRACE, this + " has been pressed.");
            switch(this.type.get()){
                case WALL, EMPTY -> type.set(lastType.get());
            }
        }
    }
    private boolean isMouseOver(double x, double y){
        x = x - App.getInstance().width()/2;
        y = y - App.getInstance().height()/2;
        return inEpsilonRange((int) x, getX(), CELL_SIZE/2) &&
                inEpsilonRange((int) y, getY(), CELL_SIZE/2);
    }

    @Override
    public String toString(){
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Cell)) return false;

        final Cell cell = (Cell) o;

        return getX() == cell.getX() &&
                getY() == cell.getY();
    }

    @Override
    public int hashCode(){
        return Objects.hash(getX(), getY());
    }

    public static Cell getStartCell(){
        if(startCell.get() == null)
            throw new NullPointerException("Start cell is not present.");
        return startCell.get();
    }

    public static Cell getEndCell(){
        if(endCell.get() == null)
            throw new NullPointerException("End cell is not present.");
        return endCell.get();
    }

}
