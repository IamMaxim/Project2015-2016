package ru.iammaxim;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxim on 13.12.2015.
 */
public class Physics {
    public static List<Mirror> mirrors;
    public static LightRay ray;

    /*
    public static void calculateCollision() {
        boolean rayEnded = false;
        List<Intersect> intersects = new ArrayList<>();
        coord2D tmp1;
        Intersect minIntersect = new Intersect(100000, 100000, Intersect.Side.bottom);
        ray.updateCoords();

        for (Mirror mirror : mirrors) {
            if ((tmp1 = segmentIntersect(ray.cb, ray.ct, mirror.lt, mirror.rt)) != null) intersects.add(new Intersect(tmp1, Intersect.Side.top));
            if ((tmp1 = segmentIntersect(ray.cb, ray.ct, mirror.lb, mirror.rb)) != null) intersects.add(new Intersect(tmp1, Intersect.Side.bottom));
            if ((tmp1 = segmentIntersect(ray.cb, ray.ct, mirror.lt, mirror.lb)) != null) intersects.add(new Intersect(tmp1, Intersect.Side.left));
            if ((tmp1 = segmentIntersect(ray.cb, ray.ct, mirror.rt, mirror.rb)) != null) intersects.add(new Intersect(tmp1, Intersect.Side.right));
        }

        for (Intersect intersect : intersects) {
            if (Math.sqrt(ray.x * ray.x + intersect.y * intersect.y) < (Math.sqrt(minIntersect.x * minIntersect.x + minIntersect.y * minIntersect.y)))
                minIntersect = intersect;
        }
        if (minIntersect.x != 100000) {
            System.out.println("Intersect: " + minIntersect.side);
            ray.setEndPoint(minIntersect);
            if (minIntersect.side != Intersect.Side.top) {
                rayEnded = true;
            } else {

            }
        }
        ray.Draw();
    }
    */

    public static coord2D segmentIntersect(coord2D p0, coord2D p1, coord2D p2, coord2D p3) {
        float   A1 = p1.y - p0.y,
                B1 = p0.x - p1.x,
                C1 = A1 * p0.x + B1 * p0.y,
                A2 = p3.y - p2.y,
                B2 = p2.x - p3.x,
                C2 = A2 * p2.x + B2 * p2.y,
                denominator = A1 * B2 - A2 * B1;

        if(denominator == 0) {
            return null;
        }

        float   intersectX = (B2 * C1 - B1 * C2) / denominator,
                intersectY = (A1 * C2 - A2 * C1) / denominator,
                rx0 = (intersectX - p0.x) / (p1.x - p0.x),
                ry0 = (intersectY - p0.y) / (p1.y - p0.y),
                rx1 = (intersectX - p2.x) / (p3.x - p2.x),
                ry1 = (intersectY - p2.y) / (p3.y - p2.y);

        if(((rx0 >= 0 && rx0 <= 1) || (ry0 >= 0 && ry0 <= 1)) &&
                ((rx1 >= 0 && rx1 <= 1) || (ry1 >= 0 && ry1 <= 1))) {
            return new coord2D(intersectX, intersectY);
        }
        else {
            return null;
        }
    }
}
