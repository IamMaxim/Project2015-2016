package ru.iammaxim;

import org.lwjgl.glfw.GLFWVidMode;

import java.util.Set;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;

/**
 * Created by Maxim on 12.12.2015.
 */
public class DrawUtils {
    public static int width, height;
    public static float scale_factor = 1;
    public static GLFWVidMode vidmode;

    public static int[] textures;
    public static int[] texture_units;

    public static coord2D getNormalizedCoord(coord2D coord) {
        return new coord2D(coord.x/width*scale_factor, coord.y/height*scale_factor);
    }

    public static void setupScreen() {
        if (Settings.R_FULLSCREEN) {
            width = vidmode.width();
            height = vidmode.height();
        } else {
            width = Settings.R_DEF_WND_WIDTH;
            height = Settings.R_DEF_WND_HEIGHT;
        }
    }

    public static long getFullscreenMode() {
        if (Settings.R_FULLSCREEN)
            return glfwGetPrimaryMonitor();
        else return 0;
    }
}
