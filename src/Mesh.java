import org.lwjgl.BufferUtils;
import org.joml.*;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int vertexArrayObject;
    private int vertexBufferObject;
    private int vertexCount;

    private int indexBufferObject;
    private int indexCount;

    private Shader shader;

    private Vector3f position, angle, scale;

    public Mesh (Shader shader) {
        this.shader = shader;
        position = new Vector3f();
        angle = new Vector3f();
        scale = new Vector3f();
    }

    // returns a boolean for a reason related to shaders
    public boolean create(float vertices[], int indices[]) {
        indexCount = indices.length;
        vertexArrayObject = glGenVertexArrays();
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices);
        indexBuffer.flip();

        glBindVertexArray(vertexArrayObject);

        // initialize and bind buffer
        vertexBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);

        // send data, saves data statically because it's not being edited. Also can choose other ways of saving for
        // being able to edit: DYNAMIC or STREAM
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        indexBufferObject = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);


        // telling openGL what data we are giving it and how to read it. Very important
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindVertexArray(0);

        // triangular
        vertexCount = indices.length;

        return true;
    }

    public void destroy() {
        glDeleteVertexArrays(vertexArrayObject);
        glDeleteBuffers(vertexBufferObject);
        glDeleteBuffers(indexBufferObject);
    }

    public void draw() {
        // bind and unbind vertex array
        glBindVertexArray(vertexArrayObject);

        // allows us to use a specific index to draw
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        shader.loadTransformationMatrix(getTransformationMatrix());

        // only draws 20 indices exactly
        
        glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);

        glBindVertexArray(0);
    }

    public Matrix4f getTransformationMatrix() {
        return MatrixMath.createTransformationMatrix(position, angle, scale);
    }

    public void addPositionX(float value) {
        position.x += value;
    }

    public void addPositionY(float value) {
        position.y += value;
    }

    public void addPositionZ(float value) {
        position.z += value;
    }

    public void addRotationX(float value) {
        angle.x += value;
    }

    public void addRotationY(float value) {
        angle.y += value;
    }

    public void addRotationZ(float value) {
        angle.z += value;
    }

    public void addScaleX(float value) {
        scale.x += value;
    }

    public void addScaleY(float value) {
        scale.y += value;
    }

    public void addScaleZ(float value) {
        scale.z += value;
    }
    /*
    public Mesh getMesh() {
        return model;
    }

    public void setModel(Mesh mesh) {
        this.model = model;
    }
    */
    public float getPositionX() {
        return position.x;
    }

    public float getPositionY() {
        return position.y;
    }

    public float getPositionZ() {
        return position.z;
    }

    public void setPositionX(float value) {
        this.position.x = value;
    }

    public void setPositionY(float value) {
        this.position.y = value;
    }

    public void setPositionZ(float value) {
        this.position.z = value;
    }

    public float getRotationX() {
        return position.x;
    }

    public float getRotationY() {
        return position.y;
    }

    public float getRotationZ() {
        return position.z;
    }

    public void setRotationX(float value) {
        this.angle.x = value;
    }

    public void setRotationY(float value) {
        this.angle.y = value;
    }

    public void setRotationZ(float value) {
        this.angle.z = value;
    }

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
    }

    public float getScaleZ() {
        return scale.z;
    }

    public void setScaleX(float value) {
        this.scale.x = value;
    }

    public void setScaleY(float value) {
        this.scale.y = value;
    }

    public void setScaleZ(float value) {
        this.scale.z = value;
    }

}
