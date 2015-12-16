package ru.iammaxim;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import java.util.ArrayList;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static ru.iammaxim.Physics.mirrors;
import static ru.iammaxim.Physics.ray;

public class Main {
    private Long window_handle;

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;
    private GLFWMouseButtonCallback mouseCallback;
    private GLFWCursorPosCallback posCallback;

    private double mousePosX, mousePosY;
    private boolean isMouseButtonPressed = false;




    public static void main(String[] args) {
        System.out.println("Initializing...");
        new Main().run();
    }

    private void run() {
        try {
            init();
            loop();
            glfwDestroyWindow(window_handle);
            keyCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if ( glfwInit() != GLFW_TRUE ) throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        DrawUtils.vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        DrawUtils.setupScreen();
        window_handle = glfwCreateWindow(DrawUtils.width, DrawUtils.height, "Project", DrawUtils.getFullscreenMode(), NULL);
        if ( window_handle == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetKeyCallback(window_handle, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GLFW_TRUE);
                if (action == 0 || action == 2)
                parseKeyboardInput(key);
            }
        });
        glfwMakeContextCurrent(window_handle);
        glfwSwapInterval(1);
        glfwShowWindow(window_handle);
        glfwSetMouseButtonCallback(window_handle, mouseCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == 1) isMouseButtonPressed = true; else isMouseButtonPressed = false;
                if (button == 0 && action == 1) {
                    parseMouseInput(mousePosX, mousePosY);
                }
            }
        });
        glfwSetCursorPosCallback(window_handle, posCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mousePosX = UIframework.getNormalizedMousePosX(xpos);
                mousePosY = UIframework.getNormalizedMousePosY(ypos);
                moveMirror();
            }
        });

        mirrors = new ArrayList<>();
        Mirror.add(mirrors);
        ray = new LightRay(0, 0, 60 * (float)Math.PI / 180, 1);
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        for (Mirror mirror : mirrors) mirror.updateCoords();
        Draw();
        while (glfwWindowShouldClose(window_handle) == GLFW_FALSE) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            LightRay.CURRENT_RAYS_COUNT = 0;
            for (Mirror mirror : mirrors) mirror.updateCoords();
            ray.calculateCollision(ray);
            Draw();
            glfwSwapBuffers(window_handle);
            glfwPollEvents();
        }
    }

    private void Draw() {
        for (Mirror mirror: mirrors) {
            if (mirror.isEnabled) {
                mirror.Draw();
            }
        }
        glFlush();
    }

    private void parseMouseInput(double x, double y) {
        for (Mirror mirror: mirrors) {
            mirror.setSelected(false);
            if (UIframework.checkMirror(mirror, x, y)) mirror.setSelected(true);
        }
    }

    private void parseKeyboardInput(int key) {
        switch (key) {
            case 45:
                for (Mirror mirror : mirrors) {
                    if (mirror.isSelected) mirror.rotate(-5);
                }
                break;
            case 61:
                for (Mirror mirror : mirrors) {
                    if (mirror.isSelected) mirror.rotate(5);
                }
                break;
            case 91:
                ray.rotation += -5 * (float)Math.PI / 180;
                break;
            case 93:
                ray.rotation += 5 * (float)Math.PI / 180;
                break;
            case 49:
                mirrors.add(new Mirror((float)mousePosX, (float)mousePosY));
                break;
            case 32:
                UIframework.isOutAllowed = !UIframework.isOutAllowed;
            default:
                break;
        }
    }

    private void moveMirror() {
        if (isMouseButtonPressed) {
            for (Mirror mirror : mirrors) {
                if (mirror.isSelected) {
                    mirror.x = (float)mousePosX;
                    mirror.y = (float)mousePosY;
                    if (mirror.x < -DrawUtils.width + mirror.width/2) mirror.x = -DrawUtils.width + mirror.width/2;
                    if (mirror.x > DrawUtils.width - mirror.width/2) mirror.x = DrawUtils.width - mirror.width/2;
                    if (mirror.y < -DrawUtils.height + mirror.thickness/2) mirror.y = -DrawUtils.height + mirror.thickness/2;
                    if (mirror.y > DrawUtils.height - mirror.thickness/2) mirror.y = DrawUtils.height - mirror.thickness/2;
                    mirror.updateCoords();
                }
            }
        }
    }

    private void exitApp() {
        glfwSetWindowShouldClose(window_handle, GLFW_TRUE);
    }
}
