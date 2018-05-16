import org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL15.*;
import org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.GL30.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {

    private int vertexArrayObject;
    private int vertexBufferObject;

    private int vertexCount;

    public Mesh () {

    }

    // returns a boolean for a reason related to shaders
    public boolean create(float vertices[]) {
        vertexArrayObject = glGenVertexArrays();
        glBindVertexArray(vertexArrayObject);

        // initialize and bind buffer
        vertexBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);

        // send data, saves data statically because it's not being edited. Also can choose other ways of saving for
        // being able to edit: DYNAMIC or STREAM
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // telling openGL what data we are giving it and how to read it. Very important
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindVertexArray(0);

        // triangular
        vertexCount = vertices.length / 3;
        return true;
    }

    public void destroy() {
        glDeleteBuffers(vertexBufferObject);
        glDeleteVertexArrays(vertexArrayObject);
    }

    public void draw() {
        // bind and unbind vertex array
        glBindVertexArray(vertexArrayObject);

        // allows us to use a specific index to draw
        glEnableVertexAttribArray(0);

        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

        glDisableVertexAttribArray(0);

        glBindVertexArray(0);
    }

}
