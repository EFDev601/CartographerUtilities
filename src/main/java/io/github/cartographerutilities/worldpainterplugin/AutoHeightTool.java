package io.github.cartographerutilities.worldpainterplugin;

import org.pepsoft.worldpainter.Dimension;
import org.pepsoft.worldpainter.MapDragControl;
import org.pepsoft.worldpainter.RadiusControl;
import org.pepsoft.worldpainter.WorldPainter;
import org.pepsoft.worldpainter.brushes.Brush;
import org.pepsoft.worldpainter.operations.*;

import javax.swing.*;
import java.awt.*;


public class AutoHeightTool extends RadiusOperation {
    public AutoHeightTool(WorldPainter view, RadiusControl radiusControl, MapDragControl mapDragControl){
        super("Auto Height", "Automatically set the height of terrain", view, radiusControl, mapDragControl, 100, "operation.autoheight", "autoheighticon");
    }

    @Override
    public JPanel getOptionsPanel(){
        return optionsPanel;
    }

    /*
    * The basis of this function is to set the height of the terrain
    * to it's distance from the closest sea/land point.
    *
    * NOTE: recalculate borders must be called first, so to calculate all the points
    * bordering land and sea
    * */
    @Override
    protected void tick(int centerX, int centerY, boolean inverse, boolean first, float dynamicLevel){
        int radius = getEffectiveRadius(), diameter = radius * 2;

        Dimension dimension = getDimension();
        dimension.setEventsInhibited(true);

        Brush brush = getBrush();

        try{
            for (int x = -radius; x <= radius; x++){
                for (int y = -radius; y <= radius; y++){
                    float strength = dynamicLevel * brush.getStrength(x, y);
                    if(strength != 0){
                        float currHeight = dimension.getHeightAt(centerX + x, centerY + y);
                        Vector2 target = new Vector2(centerX + x, centerY + y);
                        boolean land = currHeight >= 62;

                        float dist, tempDist;

                        if (land){
                            if (ProceduralUtilities.seaBorders.length > 0) {
                                dist = Vector2.Dist(target, ProceduralUtilities.seaBorders[0]);
                                if (ProceduralUtilities.seaBorders.length > 1) {
                                    //Check for closest bordering point
                                    for (int i = 1; i < ProceduralUtilities.seaBorders.length; i++) {
                                        tempDist = Vector2.Dist(target, ProceduralUtilities.seaBorders[i]);
                                        if (tempDist < dist) {
                                            dist = tempDist;
                                        }
                                    }
                                }
                                dist *= options.slope;
                                //Interpolate between the current height and the automatic height based on brush strength
                                dimension.setHeightAt(centerX + x, centerY + y, Mathf.Lerp(currHeight, Math.min(dist + 62, 255), strength));
                            }
                            else{
                                dimension.setHeightAt(centerX + x, centerY + y, Mathf.Lerp(currHeight, 255, strength));
                            }
                        }
                        else {
                            if (ProceduralUtilities.landBorders.length > 0) {
                                dist = Vector2.Dist(target, ProceduralUtilities.landBorders[0]);
                                if (ProceduralUtilities.landBorders.length > 1) {
                                    //Check for closest bordering point
                                    for (int i = 1; i < ProceduralUtilities.landBorders.length; i++) {
                                        tempDist = Vector2.Dist(target, ProceduralUtilities.landBorders[i]);
                                        if (tempDist < dist) {
                                            dist = tempDist;
                                        }
                                    }
                                }
                                dist *= options.getSlope();
                                //Interpolate between the current height and the automatic height based on brush strength
                                dimension.setHeightAt(centerX + x, centerY + y, Mathf.Lerp(currHeight, Math.max(62 - dist, 0), strength));
                            } else {
                                dimension.setHeightAt(centerX + x, centerY + y, Mathf.Lerp(currHeight, 0, strength));
                            }
                        }
                    }
                }
            }
        }finally{
            dimension.setEventsInhibited(false);
        }
    }
    private final AutoHeightOptions<AutoHeightTool> options = new AutoHeightOptions<>();
    private final AutoHeightOptionsPanel optionsPanel = new AutoHeightOptionsPanel(options);

    private static class AutoHeightOptions<O extends Operation> implements OperationOptions<O> {
        public double getSlope(){
            return slope;
        }
        public void setSlope(double v){
            slope = v;
        }

        private double slope = 1;
    }
    private static class AutoHeightOptionsPanel extends JPanel{
        public AutoHeightOptionsPanel(AutoHeightOptions<?> op){
            initComponents();
            setOptions(op);
        }

        public AutoHeightOptions<?> getOptions(){
            return options;
        }

        public void setOptions(AutoHeightOptions<?> newOp){
            this.options = newOp;
            tbSlope.setValue(options.getSlope());
        }

        private void initComponents(){
            setLayout(new GridLayout(8,1));
            tbSlope.setMinimumSize(new java.awt.Dimension(80,30));
            tbSlope.setMaximumSize(new java.awt.Dimension(500,100));
            tbSlope.addActionListener(e -> {
                tbSlope.setValue(tbSlope.getValue());
                options.setSlope((double)tbSlope.getValue());
                firePropertyChange("options", null, options);
            });
            add(new Label("Slope: "));
            add(tbSlope);
        }

        private final JFormattedTextField tbSlope = new JFormattedTextField(new Double(1.111));
        private AutoHeightOptions<?> options;
    }
}