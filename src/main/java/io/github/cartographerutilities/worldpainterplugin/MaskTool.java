package io.github.cartographerutilities.worldpainterplugin;

import org.pepsoft.worldpainter.Dimension;
import org.pepsoft.worldpainter.MapDragControl;
import org.pepsoft.worldpainter.RadiusControl;
import org.pepsoft.worldpainter.WorldPainter;

import javax.swing.*;

import org.pepsoft.worldpainter.operations.RadiusOperation;
import org.pepsoft.worldpainter.operations.TerrainShapingOptions;
import org.pepsoft.worldpainter.operations.TerrainShapingOptionsPanel;

/**
 * @author Evan Funk
 */

public class MaskTool extends RadiusOperation {
    public MaskTool(WorldPainter view, RadiusControl radiusControl, MapDragControl mapDragControl){
        super("Mask", "Mask terrain to 256 or 0 based on sea level", view, radiusControl, mapDragControl, 100, "operation.mask", "maskicon");

    }

    @Override
    public JPanel getOptionsPanel(){
        return optionsPanel;
    }

    /*
    * The point of this tool is tool is to either set the terrain height to 255 or 0
    * for quick painting of land shape for the auto height tool
    * */
    @Override
    protected void tick(int centerX, int centerY, boolean inverse, boolean first, float dynamicLevel){
        int radius = getEffectiveRadius(), diameter = radius * 2;

        Dimension dimension = getDimension();
        dimension.setEventsInhibited(true);

        try{
            for (int x = 0; x < diameter; x++){
                for (int y = 0; y < diameter; y++){
                    if (dynamicLevel * getStrength(centerX, centerY, centerX + x - radius, centerY + y - radius) > 0) {
                        if (inverse) {
                            dimension.setHeightAt(centerX - radius + x, centerY - radius + y, 0);
                        }
                        else{
                            dimension.setHeightAt(centerX - radius + x, centerY - radius + y, 255);
                        }
                    }
                }
            }
        }finally{
            dimension.setEventsInhibited(false);
        }
    }

    private final TerrainShapingOptions<MaskTool> options = new TerrainShapingOptions<>();
    private final TerrainShapingOptionsPanel optionsPanel = new TerrainShapingOptionsPanel(options);
}
