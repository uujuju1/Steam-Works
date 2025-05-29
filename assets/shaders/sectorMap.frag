#define HIGHP

#define LENGTH 40

//#define TIMESCL 1.0/600.0;

uniform sampler2D u_texture;
//uniform sampler2D u_noise;

uniform float u_opacity;
uniform vec2 u_resolution;
uniform vec2 u_size;
uniform vec2 u_position;

uniform int u_points_length;
uniform float u_points[LENGTH];

uniform float u_time;

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

//    vec4 noise = vec4(0);

//    float OCTAVES = 4;
//
//    for(float i = 1; i < OCTAVES + 1; i++) {
//        vec2 dir = vec2(cos(1.6 * i * 10), sin(1.6 * i * 10)) * u_time * TIMESCL;
//        noise += texture2D(u_noise, coords * i + dir) / i;
//    }
//    noise /= 2;

    float p = 10000;
    for (int i = 0; i < u_points_length; i+= 4) {
        vec2 point = vec2(u_points[i], u_resolution.y - u_points[i + 1] + overY);
        vec2 pointSize = vec2(u_points[i + 2], u_points[i + 2]);
        point += u_position * vec2(-1, 1);
        point -= u_resolution * vec2(0.5, -0.5);
        point /= scale;

        vec2 len = (point - coords * u_resolution) * scale / (pointSize / 2);

        p = min(length(len) / 1.414, p);
    }

    if (p > 1) col *= vec4(0.5, 0.5, 0.5, 1);
    if (p > 1.25) col *= vec4(0, 0, 0, 1);

    gl_FragColor = vec4(col.rgb, u_opacity * col.a);
}