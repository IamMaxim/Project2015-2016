package ru.iammaxim;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static ru.iammaxim.DrawUtils.getNormalizedCoord;
import static ru.iammaxim.DrawUtils.textures;

/**
 * Created by Maxim on 12.12.2015.
 */
public class Mirror {
    public float x, y, rotation, width, thickness;
    public boolean isEnabled = true, isSelected = false;
    public coord2D lc, rc, ct, cb, lt, rt, lb, rb , tmp1, tmp2;
    public Color3 color = new Color3();

    public Mirror() {
        this(0, 0, 0, 100, 50);
    }

    public Mirror(float x, float y) {
        this(x, y, 0, 100, 50);
    }

    public Mirror(float x, float y, float rotation, float width, float thickness) {
        this.x = x;
        this.y = y;
        this.rotation = rotation * (float)Math.PI / 180;
        this.width = width;
        this.thickness = thickness;
        color = new Color3(0, 1, 1);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation * (float)Math.PI / 180;
    }

    public void rotate(float rotation) {
        this.rotation += rotation * (float)Math.PI / 180;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected)
            setColor(new Color3(1, 1, 0));
        else setColor(new Color3(0, 1, 1));
    }

    public void updateCoords() {
        /*
        lc = new coord2D((float) (x - Math.cos(rotation) * width / 2), (float) (y - Math.sin(rotation) * width / 2));
        rc = new coord2D().minus(lc);
        ct = new coord2D((float) (x - Math.sin(rotation) * thickness / 2), (float) (y + Math.cos(rotation) * thickness / 2));
        cb = new coord2D().minus(ct);
        lt = new coord2D(lc).plus(ct);
        rt = new coord2D(rc).plus(ct);
        rb = new coord2D(rc).minus(ct);
        lb = new coord2D(lc).minus(ct);
        */

        tmp1 = new coord2D(Math.cos(rotation) * width / 2,Math.sin(rotation) * width / 2);
        tmp2 = new coord2D(Math.sin(rotation) * thickness / 2, Math.cos(rotation) * thickness / 2);
        lc = new coord2D(x - tmp1.x, - y + tmp1.y);
        rc = new coord2D(x + tmp1.x, - y - tmp1.y);
        ct = new coord2D(x - tmp2.x, y + tmp2.y);
        cb = new coord2D(x + tmp2.x, y - tmp2.y);
        lt = new coord2D(lc).plus(tmp2);
        rt = new coord2D(rc.x, rc.y).plus(tmp2);
        rb = new coord2D(rc).minus(tmp2);
        lb = new coord2D(lc.x, lc.y).minus(tmp2);
    }

    public static void add(List<Mirror> mirrors) {
        mirrors.add(new Mirror(200, 0));
    }

    public void Draw() {
        /*
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[0]);
        */
        glColor3f(color.r, color.g, color.b);
        //glBindTexture(GL11.GL_TEXTURE_2D, textures[0]);
        glBegin(GL_POLYGON);
        glVertex2f(getNormalizedCoord(lt).x, getNormalizedCoord(lt).y);
        glVertex2f(getNormalizedCoord(rt).x, getNormalizedCoord(rt).y);
        glVertex2f(getNormalizedCoord(rb).x, getNormalizedCoord(rb).y);
        glVertex2f(getNormalizedCoord(lb).x, getNormalizedCoord(lb).y);
        glEnd();

        glColor3f(0.5f, 0.5f, 0.5f);
        glLineWidth(5);
        glBegin(GL_LINES);
        glVertex2f(getNormalizedCoord(lt).x, getNormalizedCoord(lt).y);
        glVertex2f(getNormalizedCoord(rt).x, getNormalizedCoord(rt).y);
        glEnd();
    }

    public void setColor(Color3 color) {
        this.color = color;
    }
}
