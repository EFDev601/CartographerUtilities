package io.github.cartographerutilities.worldpainterplugin;

import org.pepsoft.worldpainter.*;
import org.pepsoft.worldpainter.Dimension;
import org.pepsoft.worldpainter.brushes.Brush;
import org.pepsoft.worldpainter.operations.*;

import javax.swing.*;
import java.awt.*;

/*Adds the ability to adjust the water depth or land height
*without changing shapes already generated
* */
public class ScaleTool extends RadiusOperation {
    public ScaleTool(WorldPainter view, RadiusControl radiusControl, MapDragControl mapDragControl){
        super("Scale Tool", "Scale the ocean depth and land height", view, radiusControl, mapDragControl, 100, "operation.scale", "scaleicon");
    }

    @Override
    public JPanel getOptionsPanel(){
        return optionsPanel;
    }

    @Override
    protected void tick(int centerX, int centerY, boolean inverse, boolean first, float dynamicLevel){
        Dimension dimension = getDimension();
        Brush brush = getBrush();

        int radius = brush.getRadius();
        float waterScale = (float)options.getWaterScale();
        float landScale = (float)options.getLandScale();

        dimension.setEventsInhibited(true);

        try {
            for (int x = -radius; x < radius; x++) {
                for (int y = -radius; y < radius; y++) {
                    float height = dimension.getHeightAt(centerX + x, centerY + y);
                    float strength = dynamicLevel * brush.getStrength(x, y);
                    if (height < 62) {
                        dimension.setHeightAt(centerX + x, centerY + y, Mathf.Lerp(dimension.getHeightAt(centerX + x, centerY + y), Math.max((height - 62) * waterScale + 62, 0), strength));
                    } else {
                        dimension.setHeightAt(centerX + x, centerY + y, Mathf.Lerp(dimension.getHeightAt(centerX + x, centerY + y), Math.min((height - 62) * landScale + 62, 255), strength));
                    }
                }
            }
        }
        finally {
            dimension.setEventsInhibited(false);
        }
    }

    private final ScalingOptions<ScaleTool> options = new ScalingOptions<>();
    private final ScalingOptionsPanel optionsPanel = new ScalingOptionsPanel(options);

    private static class ScalingOptions<O extends Operation> implements OperationOptions<O> {
        public double getWaterScale(){
            return waterScale;
        }
        public void setWaterScale(double v){
            waterScale = v;
        }
        public double getLandScale(){
            return landScale;
        }
        public void setLandScale(double v){ landScale = v; }

        private double landScale = 1;
        private double waterScale = 1;
    }

    private static class ScalingOptionsPanel extends JPanel{
        public ScalingOptionsPanel(ScalingOptions<?> op){
            initComponents();
            setOptions(op);
        }

        public ScalingOptions<?> getOptions(){
            return options;
        }

        public void setOptions(ScalingOptions<?> newOp){
            this.options = newOp;
            tbWater.setValue(options.getWaterScale());
            tbLand.setValue(options.getLandScale());
        }

        private void initComponents(){
            setLayout(new GridLayout(8,1));
            tbWater.addActionListener(e -> {
                tbWater.setValue(tbWater.getValue());
                options.setWaterScale((double)tbWater.getValue());
                firePropertyChange("options", null, options);
            });
            tbLand.addActionListener(e -> {
                tbLand.setValue(tbLand.getValue());
                options.setLandScale((double)tbLand.getValue());
                firePropertyChange("options", null, options);
            });
            Label text = new Label("Water Scale: ");
            add(text);
            add(tbWater);
            text = new Label("Land Scale: ");
            add(text);
            add(tbLand);
        }

        private final JFormattedTextField tbWater = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbLand = new JFormattedTextField(new Double(1.111));
        private ScalingOptions<?> options;
    }
}