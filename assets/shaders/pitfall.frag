#define HIGHP

uniform sampler2D u_texture;
uniform sampler2D u_mask;
uniform vec2 u_maskSize;

uniform vec2 u_scl;

uniform float u_spacing;

uniform float u_camScl;

uniform vec2 u_clip;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

const int dstPrecision = 4;
const float dstSpacing = 8.0;

float interp(float a) {
    return 1 - pow(1 - a, u_spacing);
}

float dst(vec2 start, vec2 dir, float step, vec2 res, float maxLen) {
    start = start * res;
    float len = 0;
    float curStep = step;
    int curPrecision = 0;
    bool reverse = false;
    for(float i = 0; i < maxLen * 2; i++) {
        len += curStep;
        start += dir * curStep;
        vec4 col = texture2D(u_mask, start / res);
        if (
            (col.w < 0.9 && !reverse) ||
            (col.w > 0.9 && reverse)
        ) {
            reverse = !reverse;
            curStep *= -0.5;
            curPrecision++;
            if (curPrecision > dstPrecision) break;
            continue;
        }
        if (len > maxLen) break;
    }

    return len;
}

vec2 dstToZ(vec2 dst, vec2 pos, vec2 center) {
    vec2 diff = (pos - center) * u_resolution;
    vec2 diffTotal = diff + dst;

    vec2 p = diff/diffTotal;

    return vec2(1.0) - p;
}

// Original by Xelo
void main() {
    vec2 uv = v_texCoords;
    vec2 ir = vec2(1.0)/u_resolution;
    vec2 ar = u_maskSize / u_resolution;

    vec2 pos = (uv + ((u_campos - u_resolution / 2.0) * ir)) / ar;
    vec2 center = (vec2(0.5) + ((u_campos - u_resolution / 2.0) * ir)) / ar;

    vec4 col = texture2D(u_mask, pos);
    if (col.a < 1) discard;

    vec2 dir = normalize((uv - vec2(0.5)));
//    dir /= max(abs(dir.x), abs(dir.y));
    dir /= (ar * ar);
    dir *= u_camScl;

    vec2 dirH = normalize(vec2(dir.x, 0.0)) / (ar * ar) * u_camScl;
    vec2 dirV = normalize(vec2(0.0, dir.y)) / (ar * ar) * u_camScl;

//    float dst = dst(pos, dir, dstSpacing, u_resolution, 100);
    float dstH = dst(pos, dirH, dstSpacing, u_resolution, 100);
    float dstV = dst(pos, dirV, dstSpacing, u_resolution, 100);

    vec2 z = dstToZ((normalize(dirH) + normalize(dirV)) * vec2(dstH, dstV) * u_scl, pos, center);

    gl_FragColor = vec4(z.x, z.y, 0.0, 1.0);

    if (z.x > u_clip.x && z.x < u_clip.y) gl_FragColor = vec4(1.0) - gl_FragColor;
}
