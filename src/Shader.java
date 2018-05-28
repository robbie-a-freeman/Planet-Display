import org.lwjgl.BufferUtils;
import org.lwjgl.ovr.OVRVector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int vertexShader, fragmentShader, program;

    public Shader() {

    }

    public boolean create(String shader) {
        int success;

        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, readSource(shader + ".vs"));
        glCompileShader(vertexShader);

        success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(vertexShader));
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, readSource(shader + ".fs"));
        glCompileShader(fragmentShader);

        success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(fragmentShader));
            return false;
        }

        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glLinkProgram(program);
        success = glGetProgrami(program, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            System.err.println(glGetProgramInfoLog(program));
            return false;
        }
        glValidateProgram(program);
        success = glGetProgrami(program, GL_VALIDATE_STATUS);
        if (success == GL_FALSE) {
            System.err.println(glGetProgramInfoLog(program));
            return false;
        }
        return true;
    }

    public void destroy() {
        glDetachShader(program, vertexShader);
        glDetachShader(program, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(program);
    }

    protected void getAllUniforms() {

    }

    protected int getUniform(String name) {
        return glGetUniformLocation(program, name);
    }

    protected void loadFloatUniform(int location, float value) {
        glUniform1f(location, value);
    }

    protected void loadIntUniform(int location, int value) {
        glUniform1i(location, value);
    }

    protected void loadVectorUniform(int location, Vector3f value) {
        glUniform3f(location, value.x, value.y, value.z);
    }

    protected void loadMatrixUniform(int location, Matrix4f value) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

        value.toBuffer(buffer);
        buffer.flip();
        glUniformMatrix4fv(location, false, buffer);
    }

    public void useShader() {
        glUseProgram(program);
    }

    public  String readSource(String file) {
        BufferedReader reader = null;
        StringBuilder sourceBuilder = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/shaders/" + file)));
            String line;

            while ((line = reader.readLine()) != null) {
                sourceBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sourceBuilder.toString();
    }

}
