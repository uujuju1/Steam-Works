#define HIGHP

#define LENGTH 80

#define TIMESCL 1.0/1200.0

uniform sampler2D u_texture;
//uniform sampler2D u_noise;

uniform float u_opacity;
uniform vec2 u_resolution;
uniform vec2 u_size;
uniform vec2 u_position;

uniform int u_points_length;
uniform float u_points[LENGTH];

uniform int u_boxes_length;
uniform float u_boxes[LENGTH];

uniform float u_time;

varying vec2 v_texCoords;

float noise(vec2 pos) {
    float p = 0;
    for (float i = 1; i < 11; i++) {
        p += 0.05 + 0.05 * sin(50 * (pos.x + 10) / i + pos.y * i);
    }
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

    float p = 10000;
    for (int i = 0; i < u_points_length; i+= 4) {
        vec2 point = vec2(u_points[i], u_resolution.y - u_points[i + 1] + overY);
        vec2 pointSize = vec2(u_points[i + 2], u_points[i + 2]);
        point += u_position * vec2(-1, 1);
        point -= u_resolution * vec2(0.5, -0.5);
        point /= scale;

        vec2 len = (point - coords * u_resolution) * scale / (pointSize / 2);

        float val = 0;
        val += (noise(coords * 8 + vec2(u_time, -u_time) / TIMESCL) / 4);
        val += (noise(coords * vec2(4, -4) + vec2(u_time * 2, u_time) / TIMESCL) / 4);
        val /= 2;
        val += length(len) / 1.414;

        p = min(val, p);
    }

    if (p > 1) col *= vec4(0.5, 0.5, 0.5, 1);
    if (p > 1.25) col *= vec4(0, 0, 0, 1);

    for (int i = 0; i < u_boxes_length; i+= 4) {
        vec2 boxPos = vec2(u_boxes[i], u_resolution.y - u_boxes[i + 1] + overY);
        vec2 boxSize = vec2(u_boxes[i + 2], u_boxes[i + 3]);
        boxPos += u_position * vec2(-1, 1);
        boxPos -= u_resolution * vec2(0.5, -0.5);
        boxPos /= scale;

        vec2 box = (boxPos - coords * u_resolution) * scale;

        if (
            abs(box.x) < boxSize.x / 2 &&
            abs(box.y) < boxSize.y / 2
        ) {

            vec2 time = box + boxSize / 2 + u_time / 8;

            float bars = mod(time.x + time.y, 60);
            if (
                !(
                    abs(box.x) < boxSize.x / 2 - 5 &&
                    abs(box.y) < boxSize.y / 2 - 5
                ) && bars >= 30
            ) col = mix(col, vec4(1), vec4(0.3));

            if (
                (
                    abs(box.x) < boxSize.x / 2 - 5 &&
                    abs(box.y) < boxSize.y / 2 - 5
                )
            ) {
                if (bars < 30) {
                    col = mix(col, vec4(1), vec4(0.2));
                } else {
                    col = mix(col, vec4(1), vec4(0.1));
                }
            }
        };


    }

    gl_FragColor = vec4(col.rgb, u_opacity * col.a);
}