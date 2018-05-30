#version 330

in vec3 position;

uniform mat4 transformation;

void main(void) {
    gl_Position = transformation * vec4(position, 1.0f);
}

