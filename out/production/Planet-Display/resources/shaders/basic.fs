#version 330

out vec4 fragColor;

void main() {
    float distSq;
    distSq = (0.0f - gl_FragCoord.x) * (0.0f - gl_FragCoord.x) + (0.0f - gl_FragCoord.y) * (0.0f - gl_FragCoord.y)
             + (1.0f - gl_FragCoord.z) * (1.0f - gl_FragCoord.z);
    fragColor = vec4(1.0f - distSq, 1.5f - gl_FragCoord.z, gl_FragCoord.z - 0.5f, 1.0f);
}

