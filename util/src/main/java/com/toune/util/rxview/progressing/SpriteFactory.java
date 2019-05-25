package com.toune.util.rxview.progressing;


import com.toune.util.rxview.progressing.sprite.Sprite;
import com.toune.util.rxview.progressing.style.ChasingDots;
import com.toune.util.rxview.progressing.style.Circle;
import com.toune.util.rxview.progressing.style.CubeGrid;
import com.toune.util.rxview.progressing.style.DoubleBounce;
import com.toune.util.rxview.progressing.style.FadingCircle;
import com.toune.util.rxview.progressing.style.FoldingCube;
import com.toune.util.rxview.progressing.style.MultiplePulse;
import com.toune.util.rxview.progressing.style.MultiplePulseRing;
import com.toune.util.rxview.progressing.style.Pulse;
import com.toune.util.rxview.progressing.style.PulseRing;
import com.toune.util.rxview.progressing.style.RotatingCircle;
import com.toune.util.rxview.progressing.style.RotatingPlane;
import com.toune.util.rxview.progressing.style.ThreeBounce;
import com.toune.util.rxview.progressing.style.WanderingCubes;
import com.toune.util.rxview.progressing.style.Wave;

/**
 * @author vondear
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
