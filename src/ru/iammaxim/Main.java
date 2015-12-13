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
        //LoadTextures();

        mirrors = new ArrayList<Mirror>();
        Mirror.add(mirrors);
        mirrors.get(0).setEnabled(true);

        ray = new LightRay();
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Draw();
        while (glfwWindowShouldClose(window_handle) == GLFW_FALSE) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            LightRay.CURRENT_RAYS_COUNT = 0;
            ray.calculateCollision();
            Draw();
            glfwSwapBuffers(window_handle);
            glfwPollEvents();
        }
    }

    private void Draw() {
        for (Mirror mirror: mirrors) {
            if (mirror.isEnabled) {
                mirror.updateCoords();
                mirror.Draw();
            }
        }
        //ray.updateCoords();
        //ray.Draw();

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

    /*
    private void LoadTextures() {
        int textures_count = 1;
        textures = new int[textures_count];
        texture_units = new int[textures_count];
        try {
            InputStream is = new FileInputStream(System.getProperty("user.dir") + "\\UIButton_base.png");
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer buf = null;
            int tWidth = decoder.getWidth();
            int tHeight = decoder.getHeight();
            buf = ByteBuffer.allocateDirect(4 * tWidth * tHeight);
            decoder.decode(buf, tWidth * 4, PNGDecoder.Format.RGBA);
            buf.flip();
            is.close();
            textures[0] = GL11.glGenTextures();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[0]);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            exitApp();
        } catch (IOException e) {
            e.printStackTrace();
            exitApp();
        }
    }
    */

    private void exitApp() {
        glfwSetWindowShouldClose(window_handle, GLFW_TRUE);
    }
}
