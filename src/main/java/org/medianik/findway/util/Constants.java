package org.medianik.findway.util;

import org.medianik.findway.App;

public class Constants{

    public static final int TICKS_PER_SECOND = 60;
    public static final double MILLIS_BETWEEN_TICKS = 1000./TICKS_PER_SECOND;


    public static final int GLOBAL_OFFSET = 10;
    public static final int GRID_SIZE =
            (int) Math.min(App.getInstance().width() - 30*GLOBAL_OFFSET,
                    App.getInstance().height() - 30*GLOBAL_OFFSET);
    public static final int NUMBER_OF_CELLS = 10;
    public static final int GRID_OFFSET = 10;
    public static final int CELL_SIZE = GRID_SIZE/NUMBER_OF_CELLS - GRID_OFFSET;
    public static final int CELL_BIG_SIZE = GRID_SIZE/NUMBER_OF_CELLS - GRID_OFFSET/3;
    public static final String APP_NAME = "Find way";
}
