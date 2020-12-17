package io.github.cartographerutilities.worldpainterplugin;

import static io.github.cartographerutilities.worldpainterplugin.Version.VERSION;

import java.util.*;

import org.pepsoft.worldpainter.App;
import org.pepsoft.worldpainter.MapDragControl;
import org.pepsoft.worldpainter.operations.Operation;
import org.pepsoft.worldpainter.plugins.AbstractPlugin;
import org.pepsoft.worldpainter.plugins.OperationProvider;

public class ProceduralUtilities extends AbstractPlugin implements OperationProvider{
    public ProceduralUtilities(){
        super("Cartographer Utilities", VERSION);
        init();
    }

    @Override
    public List<Operation> getOperations(){
        return operations;
    }

    private void init(){
        operations.add(new RecalculateBorders(null));//Done
        operations.add(new MaskTool(null, App.getInstanceIfExists(), mapctrl));//Done and brushed
        operations.add(new AutoHeightTool(null, App.getInstanceIfExists(), mapctrl));//Done
        operations.add(new PerlinNoiseTool(null, App.getInstanceIfExists(), mapctrl));
        operations.add(new ScaleTool(null, App.getInstanceIfExists(), mapctrl));//Done
    }

    private final List<Operation> operations = new ArrayList<>();
    private final MapDragControl mapctrl = new MapDragControl() {
        @Override
        public boolean isMapDraggingInhibited() {
            return false;
        }

        @Override
        public void setMapDraggingInhibited(boolean mapDraggingInhibited) {

        }
    };

    public static Vector2[] seaBorders = new Vector2[0];
    public static Vector2[] landBorders = new Vector2[0];
}