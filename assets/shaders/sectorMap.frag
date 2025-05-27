#define HIGHP

#define LENGTH 40

uniform sampler2D u_texture;

uniform float u_opacity;
uniform vec2 u_resolution;
uniform vec2 u_size;
uniform vec2 u_position;

uniform int u_points_length;
uniform float u_points[LENGTH];

varying vec2 v_texCoords;

float interp(float p) {
    return p;
}

void main() {
    vec2 uv = vec2(v_texCoords.x, 1 - v_texCoords.y);
    vec2 scale = u_size/u_resolution;

    float overY = u_size.y - u_resolution.y;

    vec2 coords = vec2(0, overY);
    coords += u_position * vec2(-1, 1);
    coords /= u_resolution;
    coords += uv - vec2(0.5, -0.5);
    coords /= scale;

    vec4 col = texture2D(u_texture, coords);

    if (clamp(coords, vec2(0), vec2(1)) != coords) col = vec4(0);
    float p = 0;
    for (int i = 0; i < u_points_length; i+= 4) {
        vec2 point = vec2(u_points[i], u_resolution.y - u_points[i + 1] + overY);
        vec2 pointSize = vec2(u_points[i + 2], u_points[i + 2]);
        point += u_position * vec2(-1, 1);
        point -= u_resolution * vec2(0.5, -0.5);
        point /= scale;

        vec2 len = (point - coords * u_resolution) * scale / (pointSize / 2);

        if (1 - length(len) > 0) p += 1 - length(len);
    }

    col = vec4(p, p, p, 1);

    gl_FragColor = vec4(col.rgb, u_opacity * col.a);
}