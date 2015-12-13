package ru.iammaxim;

/**
 * Created by Maxim on 13.12.2015.
 */
public class Intersect {
    public float x, y;
    public Side side;
    public float mirrorRotation;
    public static final float INTERSECT_DEF_X = 100000;
    public static final float INTERSECT_DEF_Y = 100000;

    public enum Side {
        top,
        bottom,
        left,
        right
    }

    public Intersect() {
        this(INTERSECT_DEF_X, INTERSECT_DEF_Y, Side.bottom, 0);
    }

    public Intersect(coord2D coord, Side side) {
        this(coord, side, 0);
    }

    public Intersect(coord2D coord, Side side, float mirrorRotation) {
        this(coord.x, coord.y, side, mirrorRotation);
    }

    public Intersect(float x, float y, Side side, float mirrorRotation) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.mirrorRotation = mirrorRotation;
    }
}
