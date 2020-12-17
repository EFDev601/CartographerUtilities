package io.github.cartographerutilities.worldpainterplugin;

import com.kenperlin.*;
import org.pepsoft.util.PerlinNoise;
import org.pepsoft.worldpainter.*;
import org.pepsoft.worldpainter.Dimension;
import org.pepsoft.worldpainter.operations.MouseOrTabletOperation;
import org.pepsoft.worldpainter.brushes.Brush;
import org.pepsoft.worldpainter.operations.Operation;
import org.pepsoft.worldpainter.operations.OperationOptions;
import org.pepsoft.worldpainter.operations.RadiusOperation;

import javax.swing.*;
import java.awt.*;

public class PerlinNoiseTool extends RadiusOperation {
    public PerlinNoiseTool (WorldPainter view, RadiusControl radiusControl, MapDragControl mapDragControl){
        super("Perlin Noise Tool", "Detail Terrain", view, radiusControl, mapDragControl, 100, "operation.perlinnoise", "perlinnoiseicon");
    }

    @Override
    public JPanel getOptionsPanel(){
        return optionsPanel;
    }

    //Using the built in perlin noise class, multiply the height at a point's magnitude from 62
    //by the perlin result.
    @Override
    protected void tick(int centerX, int centerY, boolean inverse, boolean first, float dynamicLevel) {
        int radius = getEffectiveRadius();

        Brush brush = getBrush();
        Dimension dimension = getDimension();

        int octaves = options.GetOctaves();
        double offsetX = options.GetOffsetX();
        double offsetY = options.GetOffsetY();
        double scaleX = options.GetScaleX();
        double scaleY = options.GetScaleY();
        double power = options.GetPower();
        double frequency = options.GetFrequency() / 100;
        double amplitude = options.GetAmplitude();
        double lacunarity = options.GetLacunarity();
        double gain = options.GetGain();

        dimension.setEventsInhibited(true);

        try {
            for (int i = -radius; i < radius; i++) {
                for (int j = -radius; j < radius; j++) {
                    float strength = brush.getStrength(i, j) * dynamicLevel;
                    float currHeight = dimension.getHeightAt(i + centerX, j + centerY);
                    float h = currHeight;
                    double perlinResult = 0;
                    double tempFreq = frequency, tempAmp = amplitude;
                    //Add perlin result for each octave
                    for (int o = 0; o < octaves; o++) {
                        perlinResult += noise.noise(((double) (i + centerX) * scaleX + offsetX) * tempFreq, ((double) (j + centerY) * scaleY + offsetY) * tempFreq, 0f) * tempAmp;

                        tempFreq *= lacunarity;
                        tempAmp *= gain;
                    }

                    perlinResult = Math.pow(perlinResult, power);

                    if (h < 62) {
                        h -= 62;
                        h *= perlinResult / 2 + 0.5;
                        //Interpolate the between the current height and new height based on the strength of the brush
                        dimension.setHeightAt(i + centerX, j + centerY, Mathf.Lerp(currHeight, Math.min(Math.max(0, h + 62), 61), strength));
                    } else {
                        h -= 62;
                        h *= perlinResult / 2 + 0.5;
                        //Interpolate the between the current height and new height based on the strength of the brush
                        dimension.setHeightAt(i + centerX, j + centerY, Mathf.Lerp(currHeight, Math.max(Math.min(h + 62, 255), 62), strength));
                    }
                }
            }
        }
        finally {
            dimension.setEventsInhibited(false);
        }
    }
    //randomize the seed
    private final ImprovedNoise noise = new ImprovedNoise((long)(Math.random() * Long.MAX_VALUE));
    private final NoiseOptions<ScaleTool> options = new NoiseOptions<>();
    private final NoiseOptionsPanel optionsPanel = new NoiseOptionsPanel(options);

    private static class NoiseOptions<O extends Operation> implements OperationOptions<O> {
        private int octaves = 3;
        public int GetOctaves() { return octaves; }
        public void SetOctaves(int v) { octaves = v; }
        private double offsetX = 0;
        public double GetOffsetX() { return offsetX; }
        public void SetOffsetX (double v) { offsetX = v; }
        private double offsetY = 0;
        public double GetOffsetY() { return offsetY; }
        public void SetOffsetY (double v) { offsetY = v; }
        private double scaleX = 1;
        public double GetScaleX() { return scaleX; }
        public void SetScaleX (double v) { scaleX = v; }
        private double scaleY = 1;
        public double GetScaleY() { return scaleY; }
        public void SetScaleY (double v) { scaleY = v; }
        private double power = 1;
        public double GetPower() { return power; }
        public void SetPower (double v) { power = v; }
        private double frequency = 1;
        public double GetFrequency() { return frequency; }
        public void SetFrequency (double v) { frequency = v; }
        private double amplitude = 0.5;
        public double GetAmplitude() { return amplitude; }
        public void SetAmplitude (double v) { amplitude = v; }
        private double lacunarity = 0.75;
        public double GetLacunarity() { return lacunarity; }
        public void SetLacunarity (double v) { lacunarity = v; }
        private double gain = 0.75;
        public double GetGain() { return gain; }
        public void SetGain (double v) { gain = v; }
    }

    private static class NoiseOptionsPanel extends JPanel {
        public NoiseOptionsPanel(NoiseOptions<?> op){
            initComponents();
            setOptions(op);
        }

        public NoiseOptions<?> getOptions(){
            return options;
        }

        public void setOptions(NoiseOptions<?> newOp){
            this.options = newOp;

            tbOctaves.setValue(options.GetOctaves());
            tbScaleX.setValue(options.GetScaleX());
            tbScaleY.setValue(options.GetScaleY());
            tbOffsetX.setValue(options.GetOffsetX());
            tbOffsetY.setValue(options.GetOffsetY());
            tbPower.setValue(options.GetPower());
            tbFrequency.setValue(options.GetFrequency());
            tbAmplitude.setValue(options.GetAmplitude());
            tbLacunarity.setValue(options.GetLacunarity());
            tbGain.setValue(options.GetGain());
        }

        private void initComponents(){
            setLayout(new GridLayout(9,3));
            tbOctaves.addActionListener(e -> {
                tbOctaves.setValue(tbOctaves.getValue());
                options.SetOctaves((int)tbOctaves.getValue());
                firePropertyChange("options", null, options);
            });
            tbScaleX.addActionListener(e -> {
                tbScaleX.setValue(tbScaleX.getValue());
                options.SetScaleX((double)tbScaleX.getValue());
                firePropertyChange("options", null, options);
            });
            tbScaleY.addActionListener(e -> {
                tbScaleY.setValue(tbScaleY.getValue());
                options.SetScaleY((double)tbScaleY.getValue());
                firePropertyChange("options", null, options);
            });
            tbOffsetX.addActionListener(e -> {
                tbOffsetX.setValue(tbOffsetX.getValue());
                options.SetOffsetX((double)tbOffsetX.getValue());
                firePropertyChange("options", null, options);
            });
            tbOffsetY.addActionListener(e -> {
                tbOffsetY.setValue(tbOffsetY.getValue());
                options.SetOffsetY((double)tbOffsetY.getValue());
                firePropertyChange("options", null, options);
            });
            tbPower.addActionListener(e -> {
                tbPower.setValue(tbPower.getValue());
                options.SetPower((double)tbPower.getValue());
                firePropertyChange("options", null, options);
            });
            tbFrequency.addActionListener(e -> {
                tbFrequency.setValue(tbFrequency.getValue());
                options.SetFrequency((double)tbFrequency.getValue());
                firePropertyChange("options", null, options);
            });
            tbAmplitude.addActionListener(e -> {
                tbAmplitude.setValue(tbAmplitude.getValue());
                options.SetAmplitude((double)tbAmplitude.getValue());
                firePropertyChange("options", null, options);
            });
            tbLacunarity.addActionListener(e -> {
                tbLacunarity.setValue(tbLacunarity.getValue());
                options.SetLacunarity((double)tbLacunarity.getValue());
                firePropertyChange("options", null, options);
            });
            tbGain.addActionListener(e -> {
                tbGain.setValue(tbGain.getValue());
                options.SetGain((double)tbGain.getValue());
                firePropertyChange("options", null, options);
            });

            add(new Label("Octaves: "));
            add(tbOctaves);
            add(new Label(" "));
            add(new Label("Scale: "));
            add(tbScaleX); add(tbScaleY);
            add(new Label("Offset: ")); add(tbOffsetX);
            add(tbOffsetY);
            add(new Label("Power: "));
            add(tbPower);
            add(new Label(" "));
            add(new Label("Frequency: "));
            add(tbFrequency);
            add(new Label(" "));
            add(new Label("Amplitude: "));
            add(tbAmplitude);
            add(new Label(" "));
            add(new Label("Lacunarity: "));
            add(tbLacunarity);
            add(new Label(" "));
            add(new Label("Gain: "));
            add(tbGain);
            add(new Label(" "));
        }

        private final JFormattedTextField tbOctaves = new JFormattedTextField(new Integer(1));
        private final JFormattedTextField tbScaleX = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbScaleY = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbOffsetX = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbOffsetY = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbPower = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbFrequency = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbAmplitude = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbLacunarity = new JFormattedTextField(new Double(1.111));
        private final JFormattedTextField tbGain = new JFormattedTextField(new Double(1.111));
        private NoiseOptions<?> options;
    }
}
