package ru.iammaxim;

/**
 * Created by Maxim on 13.12.2015.
 */
public class Intersect {
    public float x, y;
    public Side side;
    public float mirrorRotation, mirror_x, mirror_y;
    public static final float INTERSECT_DEF_X = 100000;
    public static final float INTERSECT_DEF_Y = 100000;

    public enum Side {
        top,
        bottom,
        left,
        right
    }

    public Intersect() {
        this(INTERSECT_DEF_X, INTERSECT_DEF_Y, Side.bottom, 0, 0, 0);
    }

    public Intersect(coord2D coord, Side side) {
        this(coord, side, 0, 0, 0);
    }

    public Intersect(coord2D coord, Side side, float mirrorRotation, float mirror_x, float mirror_y) {
        this(coord.x, coord.y, side, mirrorRotation, mirror_x, mirror_y);
    }

    public Intersect(float x, float y, Side side, float mirrorRotation, float mirror_x, float mirror_y) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.mirrorRotation = mirrorRotation;
        this.mirror_x = mirror_x;
        this.mirror_y = mirror_y;
    }
}
