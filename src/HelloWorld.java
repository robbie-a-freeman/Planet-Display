import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.HashMap;
import java.util.LinkedList;

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
    // vertices generated, with each coordinate grouped in a Vector3f
    private HashMap<Vector3f, Integer> vertexCoords;
    // Indices generated, with each face taking up 3 values with 3 consecutive keys
    private LinkedList<Integer> indices;

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
        testMesh.setPositionZ(0.0f);
        testMesh.setRotationX(0.0f);
        testMesh.setRotationY(0.0f);
        testMesh.setRotationZ(0.0f);
        testMesh.setScaleX(1.0f);
        testMesh.setScaleY(1.0f);
        testMesh.setScaleZ(1.0f);
        float goldenRatio = 0.8090170f;
        float zBooster = 0.0f;

        // TODO put setup and organization of vertex/index generation here
        vertexCoords = new HashMap<Vector3f, Integer>();
        indices = new LinkedList<>();

        Vector3f[] initialVertices = new Vector3f[]{
                new Vector3f(-1 * goldenRatio / 2.0f, 0, 1 / 2.0f + zBooster),
                new Vector3f(goldenRatio / 2.0f, 0, 1 / 2.0f + zBooster),
                new Vector3f(-1 * goldenRatio / 2.0f, 0, -1 / 2.0f + zBooster),
                new Vector3f(goldenRatio / 2.0f, 0, -1 / 2.0f + zBooster),
                new Vector3f(0, 1 / 2.0f, goldenRatio / 2.0f + zBooster),
                new Vector3f(0, 1 / 2.0f, -1 * goldenRatio / 2.0f + zBooster),
                new Vector3f(0, -1 / 2.0f, goldenRatio / 2.0f + zBooster),
                new Vector3f(0, -1 / 2.0f, -1 * goldenRatio / 2.0f + zBooster),
                new Vector3f(1 / 2.0f, goldenRatio / 2.0f, zBooster),
                new Vector3f(-1 / 2.0f, goldenRatio / 2.0f, zBooster),
                new Vector3f(1 / 2.0f, -1 * goldenRatio / 2.0f, zBooster),
                new Vector3f(-1 / 2.0f, -1 * goldenRatio / 2.0f, zBooster)
        };

        int[] initialIndices = new int[] {
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
        };

        for (int i = 0; i < initialIndices.length; i += 3) {
            generateVertices(
                    1,
                    initialVertices[initialIndices[i]],
                    initialVertices[initialIndices[i + 1]],
                    initialVertices[initialIndices[i + 2]]
            );
        }

        Object[] vertices = vertexCoords.keySet().toArray();
        float[] finalVertices = new float[vertices.length * 3];
        int i = 0;
        for (Object v: vertices) {
            finalVertices[i] = ((Vector3f) v).x;
            finalVertices[i + 1] = ((Vector3f) v).y;
            finalVertices[i + 2] = ((Vector3f) v).z;
            System.out.println("Coordinate " + i + ": " + finalVertices[i] + ", " + finalVertices[i + 1] + ", " + finalVertices[i + 2]);
            i += 3;
        }

        int[] finalIndices = new int[indices.size()];
        i = 0;
        while (!indices.isEmpty()) {
            finalIndices[i] = indices.pop();
            System.out.println(finalIndices[i]);
            i++;
        }

        testMesh.create(finalVertices, finalIndices);
    }

    private void generateVertices(int n, Vector3f v1, Vector3f v2, Vector3f v3) {
        if (n <= 1) { // base case; end recursive chain, add 3 indices to queue of indices

            System.out.println(vertexCoords.size());

            if (!vertexCoords.containsKey(v1)) {
                vertexCoords.put(v1, vertexCoords.size());
            }
            if (!vertexCoords.containsKey(v2)) {
                vertexCoords.put(v2, vertexCoords.size());
            }
            if (!vertexCoords.containsKey(v3)) {
                vertexCoords.put(v3, vertexCoords.size());
            }

            indices.add(vertexCoords.get(v1));
            indices.add(vertexCoords.get(v2));
            indices.add(vertexCoords.get(v3));

            return;
        }

        Vector3f v4, v5, v6; // new vertices to generate 4 subtriangles per face

        v4 = new Vector3f((v1.x + v2.x) / 2.0f, (v1.y + v2.y) / 2.0f, (v1.z + v2.z) / 2.0f);
        if (!vertexCoords.containsKey(v4)) {
            vertexCoords.put(v4, vertexCoords.size());
        }

        v5 = new Vector3f((v3.x + v2.x) / 2.0f, (v3.y + v2.y) / 2.0f, (v3.z + v2.z) / 2.0f);
        if (!vertexCoords.containsKey(v5)) {
            vertexCoords.put(v5, vertexCoords.size());
        }

        v6 = new Vector3f((v1.x + v3.x) / 2.0f, (v1.y + v3.y) / 2.0f, (v1.z + v3.z) / 2.0f);
        if (!vertexCoords.containsKey(v6)) {
            vertexCoords.put(v6, vertexCoords.size());
        }

        generateVertices(--n, v4, v5, v6);
        generateVertices(--n, v2, v5, v4);
        generateVertices(--n, v5, v3, v6);
        generateVertices(--n, v4, v6, v1);
    }

    private void loop() {

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

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

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            testMesh.draw();


            glfwSwapBuffers(window); // swap the color buffers

        }
        testMesh.destroy();
        shader.destroy();
    }

    public static void main(String[] args) {
        new HelloWorld().run();
    }

}