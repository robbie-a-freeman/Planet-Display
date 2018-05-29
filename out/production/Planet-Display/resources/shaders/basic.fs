#version 330

out vec4 fragColor;

void main() {
    fragColor = vec4(0, 1.0f - gl_FragCoord.z, gl_FragCoord.z, 1);

}

