package org.medianik.findway.astar;

import org.medianik.findway.App;
import org.medianik.findway.gameobject.Cell;
import org.medianik.findway.gameobject.CellType;

import java.util.*;

import static org.medianik.findway.util.Constants.NUMBER_OF_CELLS;
import static org.medianik.findway.util.Util.inRange;

public class Grid{

    private final Cell[][] grid = new Cell[NUMBER_OF_CELLS + 1][NUMBER_OF_CELLS + 1];
    private final App app;

    public Grid(App app){
        this.app = app;
    }

    public void init(Set<? super Cell> gameObjects){
        for(int x = -NUMBER_OF_CELLS/2; x <= NUMBER_OF_CELLS/2; x++)
            for(int y = -NUMBER_OF_CELLS/2; y <= NUMBER_OF_CELLS/2; y++)
                gameObjects.add(grid[toInner(x)][toInner(y)] = new Cell(x, y, app.getManager().getTick(), app.getNodes()));
    }

    private int toInner(int v){
        return v + NUMBER_OF_CELLS/2;
    }

    public Set<Cell> getNeighbours(Cell cell){
        return getNeighbours(cell.getGridX(), cell.getGridY());
    }

    public Set<Cell> getNeighbours(int x, int y){
        x = toInner(x);
        y = toInner(y);

        Set<Cell> set = new HashSet<>(4);
        for(int addX = -1; addX <= 1; addX += 2){
            if(!inRange(x + addX, 0, grid.length - 1))
                continue;

            Cell cell = grid[x + addX][y];
            if(isServiceCell(cell)) continue;

            set.add(cell);
        }

        for(int addY = -1; addY <= 1; addY += 2){
            if(!inRange(y + addY, 0, grid.length - 1))
                continue;

            Cell cell = grid[x][y + addY];
            if(isServiceCell(cell)) continue;

            set.add(cell);
        }
        return set;
    }

    private boolean isServiceCell(Cell cell){
        return cell.getType() == CellType.WALL || cell.getType() == CellType.START;
    }

    private int toOuter(int v){
        return v - NUMBER_OF_CELLS/2;
    }

    public void markColoredAsEmpty(){
        for(Cell[] cells : grid)
            for(Cell cell : cells)
                if(cell.getType() == CellType.COLORED)
                    cell.resetType();
    }

    public int getCost(Cell current, Cell next){
        return AStar.heuristic(current, next);
    }

    public void clear(){
        for(int x = 0; x < grid.length; x++)
            for(int y = 0; y < grid.length; y++)
                grid[x][y] = null;
    }
}
