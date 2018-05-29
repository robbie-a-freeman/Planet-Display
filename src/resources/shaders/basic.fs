#version 330

out vec4 fragColor;

void main() {
    fragColor = vec4(0.0f, 1.5f - gl_FragCoord.z, gl_FragCoord.z - 0.5f, 1.0f);
}

