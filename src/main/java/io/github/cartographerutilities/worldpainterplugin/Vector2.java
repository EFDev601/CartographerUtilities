package io.github.cartographerutilities.worldpainterplugin;

import java.lang.*;
import java.util.ArrayList;

public class Vector2{
    public float x;
    public float y;

    public Vector2(float X, float Y){
        x = X;
        y = Y;
    }
    public Vector2(){
        x = 0;
        y = 0;
    }

    public static Vector2 Lerp(Vector2 a, Vector2 b, Vector2 t){
        return new Vector2(Mathf.Lerp(a.x, b.x, t.x), Mathf.Lerp(a.y, b.y, t.y));
    }

    public static Vector2 LerpUnclamped(Vector2 a, Vector2 b, Vector2 t){
        return new Vector2(Mathf.LerpUnclamped(a.x, b.x, t.x), Mathf.LerpUnclamped(a.y, b.y, t.y));
    }

    public static Vector2 InvLerp(Vector2 a, Vector2 b, Vector2 v){
        return new Vector2(Mathf.InvLerp(a.x, b.x, v.x), Mathf.InvLerp(a.y, b.y, v.y));
    }

    public static Vector2 InvLerpUnclamped(Vector2 a, Vector2 b, Vector2 v){
        return new Vector2(Mathf.InvLerpUnclamped(a.x, b.x, v.x), Mathf.InvLerpUnclamped(a.y, b.y, v.y));
    }

    public static float Dist(Vector2 a, Vector2 b){
        return (float)Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static Vector2 Closest(Vector2 in, Vector2[] tests, InterpolationMode mode){
        if (tests.length > 0) {
            int close = 0;
            float dist;
            if (mode == InterpolationMode.BiCubic) dist = Dist(in, tests[0]);
            else dist = Math.abs(in.x - tests[0].x) + Math.abs(in.y - tests[0].y);

            if (tests.length > 1){
                for (int i = 1; i < tests.length; i++){
                    float temp;

                    if (mode == InterpolationMode.BiCubic) temp = Dist(in, tests[0]);
                    else temp = Math.abs(in.x - tests[0].x) + Math.abs(in.y - tests[0].y);

                    if (temp < dist){
                        close = i;
                        dist = temp;
                    }
                }
            }
            return tests[close];
        }
        return null;
    }

    public static Vector2 Furthest(Vector2 in, Vector2[] tests, InterpolationMode mode){
        if (tests.length > 0) {
            int close = 0;
            float dist;
            if (mode == InterpolationMode.BiCubic) dist = Dist(in, tests[0]);
            else dist = Math.abs(in.x - tests[0].x) + Math.abs(in.y - tests[0].y);

            if (tests.length > 1){
                for (int i = 1; i < tests.length; i++){
                    float temp;

                    if (mode == InterpolationMode.BiCubic) temp = Dist(in, tests[0]);
                    else temp = Math.abs(in.x - tests[0].x) + Math.abs(in.y - tests[0].y);

                    if (temp > dist){
                        close = i;
                        dist = temp;
                    }
                }
            }
            return tests[close];
        }
        return null;
    }

    public static Vector2 Closest(Vector2 in, ArrayList<Vector2> tests, InterpolationMode mode){
        int size = tests.size();
        if (size > 0) {
            int close = 0;
            float dist;
            if (mode == InterpolationMode.BiCubic) dist = Dist(in, tests.get(0));
            else dist = Math.abs(in.x - tests.get(0).x) + Math.abs(in.y - tests.get(0).y);

            if (size > 1){
                for (int i = 1; i < size; i++){
                    float temp;

                    if (mode == InterpolationMode.BiCubic) temp = Dist(in, tests.get(0));
                    else temp = Math.abs(in.x - tests.get(0).x) + Math.abs(in.y - tests.get(0).y);

                    if (temp < dist){
                        close = i;
                        dist = temp;
                    }
                }
            }
            return tests.get(close);
        }
        return null;
    }

    public static Vector2 Furthest(Vector2 in, ArrayList<Vector2> tests, InterpolationMode mode){
        int size = tests.size();
        if (size > 0) {
            int close = 0;
            float dist;
            if (mode == InterpolationMode.BiCubic) dist = Dist(in, tests.get(0));
            else dist = Math.abs(in.x - tests.get(0).x) + Math.abs(in.y - tests.get(0).y);

            if (size > 1){
                for (int i = 1; i < size; i++){
                    float temp;

                    if (mode == InterpolationMode.BiCubic) temp = Dist(in, tests.get(0));
                    else temp = Math.abs(in.x - tests.get(0).x) + Math.abs(in.y - tests.get(0).y);

                    if (temp > dist){
                        close = i;
                        dist = temp;
                    }
                }
            }
            return tests.get(close);
        }
        return null;
    }
}