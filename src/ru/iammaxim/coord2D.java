package ru.iammaxim;

public class coord2D {
    public float x, y;

    public coord2D() {
        x = 0;
        y = 0;
    }

    public coord2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public coord2D(coord2D coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    public coord2D(float x, float y, boolean isNegative) {
        if (isNegative) {
            this.x = -x;
            this.y = -y;
        } else {
            this.x = x;
            this.y = y;
        }
    }

    public coord2D(coord2D coord, boolean isNegative) {
        if (isNegative) {
            this.x = -coord.x;
            this.y = -coord.y;
        } else {
            this.x = coord.x;
            this.y = coord.y;
        }
    }

    public coord2D(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public coord2D plus(coord2D coord) {
        this.x += coord.x;
        this.y += coord.y;
        return new coord2D(this.x, this.y);
    }

    public coord2D minus(coord2D coord) {
        this.x -= coord.x;
        this.y -= coord.y;
        return new coord2D(this.x, this.y);
    }

    public coord2D get() {
        return this;
    }
}
