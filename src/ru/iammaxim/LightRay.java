package ru.iammaxim;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static ru.iammaxim.DrawUtils.getNormalizedCoord;
import static ru.iammaxim.Physics.mirrors;
import static ru.iammaxim.Physics.ray;
import static ru.iammaxim.Physics.segmentIntersect;

/**
 * Created by Maxim on 12.12.2015.
 */
public class LightRay {
    public float x, y, rotation, length, width;
    public coord2D ct,cb,lt,rt,lb,rb,tmp1,tmp2;
    public Color3 color;
    public static int MAX_RAYS_COUNT = 50;
    public static int CURRENT_RAYS_COUNT = 0;
    public float intensity = 1;

    public LightRay() {
        this(0, 0, 0 , 1);
    }

    public LightRay(float x, float y, float rotation, float intensity) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.intensity = intensity;
        length = 10000;
        width = 4;
        color = new Color3(1 * intensity, 1 * intensity, 1 * intensity);
    }

    public void updateCoords() {
        updateCoords(length);
    }

    public void updateCoords(float length) {
        cb = new coord2D(x, y);
        tmp1 = new coord2D(Math.cos(rotation) * width / 2,-Math.sin(rotation) * width / 2);
        tmp2 = new coord2D(Math.sin(rotation) * length, Math.cos(rotation) * length);
        rb = new coord2D(cb).plus(tmp1);
        lb = new coord2D(cb).minus(tmp1);
        ct = new coord2D(cb).plus(tmp2);
        lt = new coord2D(ct).minus(tmp1);
        rt = new coord2D(ct).plus(tmp1);
    }

    public void setEndPoint(Intersect intersect) {
        updateCoords((float)Math.sqrt((x - intersect.x)*(x - intersect.x) + (y - intersect.y)*(y - intersect.y)));
    }

    public void calculateCollision(LightRay ray) {
        boolean rayEnded = false;
        List<Intersect> intersects = new ArrayList<>();
        coord2D tmp1;
        Intersect minIntersect = new Intersect();
        updateCoords();

        for (Mirror mirror : mirrors) {
            if ((tmp1 = segmentIntersect(cb, ct, mirror.lt, mirror.rt)) != null) intersects.add(new Intersect(tmp1, Intersect.Side.top, mirror.rotation, mirror.x, mirror.y));
            if ((tmp1 = segmentIntersect(cb, ct, mirror.lb, mirror.rb)) != null) intersects.add(new Intersect(tmp1, Intersect.Side.bottom, mirror.rotation, mirror.x, mirror.y));
            if ((tmp1 = segmentIntersect(cb, ct, mirror.lt, mirror.lb)) != null) intersects.add(new Intersect(tmp1, Intersect.Side.left, mirror.rotation, mirror.x, mirror.y));
            if ((tmp1 = segmentIntersect(cb, ct, mirror.rt, mirror.rb)) != null) intersects.add(new Intersect(tmp1, Intersect.Side.right, mirror.rotation, mirror.x, mirror.y));
        }

        for (Intersect intersect : intersects) {
            double tmp = Math.sqrt((intersect.x - x) * (intersect.x - x) + (intersect.y - y) * (intersect.y - y));
            if ((tmp < (Math.sqrt((minIntersect.x - x) * (minIntersect.x - x) + (minIntersect.y - y) * (minIntersect.y - y)))) && tmp > 1)
                minIntersect = intersect;
        }
        if (minIntersect.x != Intersect.INTERSECT_DEF_X) {
            setEndPoint(minIntersect);
            if (minIntersect.side == Intersect.Side.top) {
                if (CURRENT_RAYS_COUNT <= MAX_RAYS_COUNT) {
                    CURRENT_RAYS_COUNT++;
                    float _rotation = (2 * minIntersect.mirrorRotation - ray.rotation - (float)Math.PI + 2*(float)Math.PI) % (2*(float)Math.PI);
                    //if (UIframework.isOutAllowed)
                        //System.out.println("Intersect: "+minIntersect.x + " " + minIntersect.y + " " + _rotation / (float)Math.PI * 180);
                    LightRay newRay = new LightRay(minIntersect.x, minIntersect.y, _rotation, intensity - 0.02f);
                    newRay.calculateCollision(newRay);
                }
            }
        }
        Draw();
        //System.out.println("Ray: "+x+", "+y+", "+rotation / (float)Math.PI * 180);
    }

    public void Draw() {
        glColor3f(color.r, color.g, color.b);
        glBegin(GL_POLYGON);
        glVertex2f(getNormalizedCoord(lt).x, getNormalizedCoord(lt).y);
        glVertex2f(getNormalizedCoord(rt).x, getNormalizedCoord(rt).y);
        glVertex2f(getNormalizedCoord(rb).x, getNormalizedCoord(rb).y);
        glVertex2f(getNormalizedCoord(lb).x, getNormalizedCoord(lb).y);
        glEnd();
    }
}
