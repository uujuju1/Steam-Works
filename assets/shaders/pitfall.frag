#define HIGHP

uniform sampler2D u_texture;
uniform sampler2D u_mask;
uniform vec2 u_maskSize;

uniform vec2 u_scl;

uniform float u_spacing;

uniform vec2 u_clip;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

float dst(vec2 start, vec2 dir, float step, vec2 res, float maxLen) {
    vec2 pos = start * res;
    float len = 0;
    for(float i = 0; i < maxLen * 2; i++) {
        len += step;
        pos += dir * step;
        if (
            texture2D(u_mask, pos / res).w < 0.9 ||
            len > maxLen
        ) {
            break;
        }
    }

    return len;
}

// Original by Xelo
void main() {
    vec2 uv = v_texCoords;
    vec2 ir = vec2(1.0)/u_resolution;
    vec2 ar = u_maskSize / u_resolution;

    vec2 pos = (uv + ((u_campos - u_resolution / 2.0) * ir)) / ar;

    vec2 dir = normalize((uv - vec2(0.5)));
    dir /= max(abs(dir.x), abs(dir.y));
    dir *= u_scl;

    vec3 col = texture2D(u_mask, pos).rgb;

    float z = dst(pos, dir, u_spacing, u_resolution, 100);

    gl_FragColor = vec4(z / 100.0, 0.0, 0.0, 1.0);

    if (z > u_clip.x && z < u_clip.y) gl_FragColor.y = 1;
}
