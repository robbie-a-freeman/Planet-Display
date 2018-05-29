import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class HelloWorld {

    // The window handle
    private long window;
    // test mesh
    private Mesh testMesh;
    // basic test shader
    private Shader shader;
    // key press test
    private GLFWKeyCallback keyCallback;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // gives GLFW 3 for the major version of our context
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2); // gives GLFW 2 for the minor version of our context
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // UNKNOWN
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE); // UNKNOWN

        // Create the window
        window = glfwCreateWindow(1200, 1200, "Hello, World! Welcome to the World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically



        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        // Has to be here because otherwise the mesh can't utilize OpenGL
        // methods.
        GL.createCapabilities();

        shader = new Shader();
        shader.create("basic");

        testMesh = new Mesh(shader);
        testMesh.setPositionX(0.0f);
        testMesh.setPositionY(0.0f);
        testMesh.setPositionZ(1.0f);
        testMesh.setRotationX(0.0f);
        testMesh.setRotationY(0.0f);
        testMesh.setRotationZ(0.0f);
        testMesh.setScaleX(1.0f);
        testMesh.setScaleY(1.0f);
        testMesh.setScaleZ(1.0f);
        float goldenRatio = 0.8090170f;
        float zBooster = 0.0f;
        testMesh.create(new float[] {

                -1 * goldenRatio / 2.0f, 0, 1 / 2.0f + zBooster,
                goldenRatio / 2.0f, 0, 1 / 2.0f + zBooster,
                -1 * goldenRatio / 2.0f, 0, -1 / 2.0f + zBooster,
                goldenRatio / 2.0f, 0, -1 / 2.0f + zBooster,
                0, 1 / 2.0f, goldenRatio / 2.0f + zBooster,
                0, 1 / 2.0f, -1 * goldenRatio / 2.0f + zBooster,
                0, -1 / 2.0f, goldenRatio / 2.0f + zBooster,
                0, -1 / 2.0f, -1 * goldenRatio / 2.0f + zBooster,
                1 / 2.0f, goldenRatio / 2.0f, zBooster,
                -1 / 2.0f, goldenRatio / 2.0f, zBooster,
                1 / 2.0f, -1 * goldenRatio / 2.0f, zBooster,
                -1 / 2.0f, -1 * goldenRatio / 2.0f, zBooster

                /* swapping golden ratio with 1, could be useful
                -1 / 2.0f, 0, goldenRatio / 2.0f,
                1 / 2.0f, 0, goldenRatio / 2.0f,
                -1 / 2.0f, 0, -1 * goldenRatio / 2.0f,
                1 / 2.0f, 0, -1 * goldenRatio / 2.0f,
                0, goldenRatio / 2.0f, 1 / 2.0f,
                0, goldenRatio / 2.0f, -1 / 2.0f,
                0, -1 * goldenRatio / 2.0f, 1 / 2.0f,
                0, -1 * goldenRatio / 2.0f, -1 / 2.0f,
                goldenRatio / 2.0f, 1 / 2.0f, 0,
                -1 * goldenRatio / 2.0f, 1 / 2.0f, 0,
                goldenRatio / 2.0f, -1 / 2.0f, 0,
                -1 * goldenRatio / 2.0f, -1 / 2.0f, 0
                */
        }, new int[] {
                0,4,1,
                0,9,4,
                9,5,4,
                4,5,8,
                4,8,1,
                8,10,1,
                8,3,10,
                5,3,8,
                5,2,3,
                2,7,3,
                7,10,3,
                7,6,10,
                7,11,6,
                11,0,6,
                0,1,6,
                6,1,10,
                9,0,11,
                9,11,2,
                9,2,5,
                7,2,11
        });
    }

    private void loop() {

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // draw mesh, bind shader
            shader.useShader();

            glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());

            if (KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
                testMesh.addRotationY(-0.02f);
            }
            if (KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
                testMesh.addRotationY(0.02f);
            }
            if (KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
                testMesh.addRotationX(0.02f);
            }
            if (KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
                testMesh.addRotationX(-0.02f);
            }

            testMesh.draw();

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            glfwSwapBuffers(window); // swap the color buffers

        }
        testMesh.destroy();
        shader.destroy();
    }

    public static void main(String[] args) {
        new HelloWorld().run();
    }

}