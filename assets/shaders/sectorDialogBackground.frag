#define HIGHP

#define TIMESCL 600.0
#define STROKE 0.015625
#define STROKEIN 0.046875
#define DIVISION 0.1

uniform float u_time;
uniform float u_opacity;
uniform vec2 u_position;
uniform vec2 u_resolution;

varying vec2 v_texCoords;

void main() {
    vec2 uv = (v_texCoords.xy + u_position/u_resolution) * vec2(u_resolution.x/u_resolution.y, 1);

    vec3 col = vec3(0.0392);

    if (mod(uv.x + uv.y - u_time/TIMESCL, 0.5) < 0.25) col = vec3(1.0, 0.82, 0.5);

    if (
        mod(uv.x + STROKEIN/2.0, DIVISION) < STROKEIN ||
        mod(uv.y + STROKEIN/2.0, DIVISION) < STROKEIN
    ) col = vec3(0.0392);

    if (
        mod(uv.x + STROKE/2.0, DIVISION) < STROKE ||
        mod(uv.y + STROKE/2.0, DIVISION) < STROKE
    ) col = vec3(0.1215);

    gl_FragColor = vec4(col, u_opacity);
}