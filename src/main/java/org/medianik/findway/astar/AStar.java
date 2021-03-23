package org.medianik.findway.astar;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.apache.log4j.Level;
import org.medianik.findway.App;
import org.medianik.findway.gameobject.Cell;
import org.medianik.findway.util.ExplicitPriorityQueue;

import java.util.HashMap;

import static org.medianik.findway.util.Constants.DEFAULT_PROCESS_DELAY;

public class AStar{

    private final Cell startCell;
    private final Cell goalCell;

    private final Grid grid;

    private final ExplicitPriorityQueue<Cell> frontier;
    private final HashMap<Cell, Cell> cameFrom;
    private final HashMap<Cell, Integer> costSoFar;

    public AStar(Grid grid){
        this.grid = grid;
        startCell = Cell.getStartCell();
        goalCell = Cell.getEndCell();

        frontier = new ExplicitPriorityQueue<>();
        cameFrom = new HashMap<>();
        costSoFar = new HashMap<>();

        frontier.add(0, 0, startCell);
        cameFrom.put(startCell, null);
        costSoFar.put(startCell, 0);
    }

    public void run(int delay){
        if(delay <= 0){
            App.logger.log(Level.DEBUG, "Starting the astar algorithm.");
            while(wayNotFound()){
                nextStep(grid);
                logStatus();
            }
            markPath();

            App.logger.log(Level.DEBUG, "Finishing the astar algorithm.");
        }else{
            Timeline tl = new Timeline();
            tl.getKeyFrames().add(new KeyFrame(Duration.millis(DEFAULT_PROCESS_DELAY), e -> {
                if(wayNotFound()){
                    nextStep(grid);
                    logStatus();
                }else{
                    markPath();

                    App.logger.log(Level.DEBUG, "Exiting from astar timeline.");
                    tl.stop();
                }
            }));

            tl.setCycleCount(Animation.INDEFINITE);
            tl.play();
            App.logger.log(Level.DEBUG, "Starting astar timeline.");
        }
    }

    private boolean wayNotFound(){
        return !frontier.isEmpty();
    }

    private void nextStep(Grid grid){
        Cell current = frontier.poll();
        assert current != null;

        for(var next : grid.getNeighbours(current)){
            var newCost = cost(current, next);
            if(!costSoFar.containsKey(next) || newCost <= costSoFar.get(next)){
                next.highlight();

                costSoFar.put(next, newCost);
                newCost += heuristic(next, goalCell);
                next.setCost(newCost);
                cameFrom.put(next, current);

                if(next == goalCell){
                    frontier.clear();
                    return;
                }

                frontier.replaceValue(newCost, heuristic(next, goalCell), next);
            }
        }
    }

    private void logStatus(){
        App.logger.log(Level.TRACE, "State of frontier: " + frontier);
        App.logger.log(Level.TRACE, "State of cameFrom: " + cameFrom);
        App.logger.log(Level.TRACE, "State of costSoFa: " + costSoFar);
    }

    private void markPath(){
        var current = goalCell;

        while(current != startCell){
            current.markAsPath();
            current = cameFrom.get(current);
        }

        grid.markColoredAsEmpty();
    }

    private int cost(Cell current, Cell next){
        return costSoFar.get(current) + grid.getCost(current, next);
    }

    public static int heuristic(Cell a, Cell b){
        return Math.abs(a.getGridX() - b.getGridX()) + Math.abs(a.getGridY() - b.getGridY());
    }
}
