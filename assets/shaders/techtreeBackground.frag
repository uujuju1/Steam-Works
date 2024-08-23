#define HIGHP

#define TIMESCL 2000.0

uniform float u_time;
uniform float u_opacity;
uniform vec2 u_resolution;

varying vec2 v_texCoords;

void main() {
    vec2 uv = v_texCoords.xy;

    float scalar = u_resolution.x/u_resolution.y;

    uv -= 0.5;
    uv.y /= scalar;

    float dst = abs(uv).x + abs(uv).y;

    vec3 col = vec3(0.1);

    float time = u_time/TIMESCL;

    for(int i = 0; i < 10; i++) {
        float within = mod(0.0 + time + float(i)/5.0, 2.0);
        if (dst < within && dst > within - 0.05) {
            float a = dot(normalize(uv), normalize(vec2(1, 1)));
            float b = dot(normalize(uv), normalize(vec2(1, -1)));
            col = vec3(0);
            if (abs(a) > 0.9) col = vec3(1.0, 0.82, 0.5) * floor(((abs(a) - 0.9) * 10.0) * 5.0)/5.0;
            if (abs(b) > 0.9) col = vec3(1.0, 0.82, 0.5) * floor(((abs(b) - 0.9) * 10.0) * 5.0)/5.0;
        }
    }
    if (dst < 0.1) col = vec3(1.0, 0.82, 0.5);

    gl_FragColor = vec4(col, u_opacity);
}