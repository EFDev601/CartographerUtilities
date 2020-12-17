package io.github.cartographerutilities.worldpainterplugin;

import org.pepsoft.worldpainter.Dimension;
import org.pepsoft.worldpainter.WorldPainter;
import org.pepsoft.worldpainter.operations.MouseOrTabletOperation;
import org.pepsoft.worldpainter.Constants;

import java.util.ArrayList;

public class RecalculateBorders extends MouseOrTabletOperation {
    public RecalculateBorders(WorldPainter view){
        super("Recalculate Borders", "Recalculate Edge Borders", view, "operation.recalculateBorders", "recalculatebordersicon");
    }

    /*Check if tile is beside water and is land
    * or if beside land and is water.
    * if so, add it to the public arrays.
    * */
    @Override
    protected void tick(int centerX, int centerY, boolean inverse, boolean first, float dynamicLevel) {
        Dimension dimension = getDimension();
        dimension.setEventsInhibited(true);

        ArrayList<Vector2> landBorder = new ArrayList<>();
        ArrayList<Vector2> seaBorder = new ArrayList<>();

        try {
            for (int x = dimension.getLowestX() * Constants.TILE_SIZE; x < (dimension.getHighestX() + 1) * Constants.TILE_SIZE; x++) {
                for (int y = dimension.getLowestY() * Constants.TILE_SIZE; y < (dimension.getHighestY() + 1) * Constants.TILE_SIZE; y++) {
                    float tempHeight = dimension.getHeightAt(x, y);
                    //Check surrounding points
                    if (tempHeight < 62 && (dimension.getHeightAt(x + 1, y) >= 62 ||
                            dimension.getHeightAt(x - 1, y) >= 62 ||
                            dimension.getHeightAt(x, y + 1) >= 62 ||
                            dimension.getHeightAt(x, y - 1) >= 62)) {
                        seaBorder.add(new Vector2(x, y));
                    } else if (tempHeight >= 62 && (dimension.getHeightAt(x + 1, y) < 62 ||
                            dimension.getHeightAt(x - 1, y) < 62 ||
                            dimension.getHeightAt(x, y + 1) < 62 ||
                            dimension.getHeightAt(x, y - 1) < 62)) {
                        landBorder.add(new Vector2(x, y));
                    }
                }
            }

            ProceduralUtilities.seaBorders = seaBorder.toArray(ProceduralUtilities.seaBorders);
            ProceduralUtilities.landBorders = landBorder.toArray(ProceduralUtilities.landBorders);
        }
        finally {
            dimension.setEventsInhibited(false);
        }
    }
}