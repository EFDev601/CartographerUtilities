package io.github.cartographerutilities.worldpainterplugin;

import java.lang.*;

public final class Mathf {
    public static float Lerp(float a, float b, float t){
        t = Clamp01(t);
        return LerpUnclamped(a, b, t);
    }

    public static float InvLerp(float a, float b, float v){
        return Clamp01(InvLerpUnclamped(a, b, v));
    }

    public static float LerpUnclamped(float a, float b, float t){
        return a * (1f - t) + (b * t);
    }

    public static float InvLerpUnclamped(float a, float b, float v){
        return (v - a) / (b - a);
    }

    public static float Clamp(float min, float max, float v){
        if (v >= max)
            return max;
        else if (v <= min)
            return min;
        else
            return v;
    }

    public static float Clamp01(float v){
        return Clamp(0, 1, v);
    }

    public static float Map(float min, float max, float mid, float v){
        v = Clamp(min, max, v);
        if (v < mid)
            return min;
        else
            return max;
    }

    public static float Mod(float v, float p){
        return (float)Math.pow(v, p);
    }

    public static float ModDouble(float v, float p){
        float pre = Math.abs(v * 2 - 1);
        return (float)Math.pow(pre, p) * Math.signum(p) / 2 + 0.5f;
    }

    public static float Sig(float x, float tx, float v, float s){
        double e1 = 1d / (1d + Math.exp(-s*(x+0.5-tx)));
        double e2 = 1d / (1d + Math.exp(-s*(x-0.5-tx)));
        return (float)(e1 - e2) * v;
    }
}