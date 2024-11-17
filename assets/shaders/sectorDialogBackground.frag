#define HIGHP

#define TIMESCL 600.0

uniform float u_time;
uniform float u_opacity;
uniform vec2 u_position;
uniform vec2 u_resolution;

varying vec2 v_texCoords;

void main() {
    vec2 uv = vec2(
        mod(u_position.x + v_texCoords.x * u_resolution.x, 100.0),
        mod(u_position.y + v_texCoords.y * u_resolution.y, 100.0)
    ) / vec2(100);
    vec2 uvA = vec2(
        mod(u_position.x + v_texCoords.x * u_resolution.x, 500.0),
        mod(u_position.y + v_texCoords.y * u_resolution.y, 500.0)
    ) / vec2(500);

    vec3 col = vec3(0.0392);

    if (mod(uvA.x + uvA.y - u_time/TIMESCL, 0.5) < 0.25) col = vec3(1.0, 0.82, 0.5);

    if (
        uv.x < 0.15 || uv.x > 0.85 ||
        uv.y < 0.15 || uv.y > 0.85
    ) col = vec3(0.0392);

    if (
        uv.x < 0.05 || uv.x > 0.95 ||
        uv.y < 0.05 || uv.y > 0.95
    ) col = vec3(0.1215);

    gl_FragColor = vec4(col, u_opacity);
}