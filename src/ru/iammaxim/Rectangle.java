package ru.iammaxim;

/**
 * Created by Maxim on 12.12.2015.
 */
public class Rectangle {
    public coord2D c,lt,rt,lb,rb;
    public Rectangle() {
        c = new coord2D();
        lt = new coord2D();
        rt = new coord2D();
        lb = new coord2D();
        rb = new coord2D();
    }

    public Rectangle(coord2D c, coord2D lt, coord2D rt, coord2D lb, coord2D rb) {
        this.c = c;
        this.lt = lt;
        this.rt = rt;
        this.lb = lb;
        this.rb = rb;
    }
}
