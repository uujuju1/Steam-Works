#define HIGHP

uniform sampler2D u_texture;
uniform sampler2D u_mask;

uniform sampler2D u_wall;
uniform vec4 u_walluv;

uniform vec2 u_maskprojectionuv;
uniform vec2 u_maskprojectionuv2;
uniform vec2 u_masksize;

uniform vec2 u_clip;
uniform float u_camscale;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

const int marchPrecision = 6;

float interpCircle(float a) {
    return sin(a * 3.14/2.0);
}

float map(float value, float froma, float toa, float fromb, float tob) {
    return fromb + (value - froma) * (tob - fromb) / (toa - froma);
}
vec2 mapVec2(vec2 value, vec2 froma, vec2 toa, vec2 fromb, vec2 tob) {
    return fromb + (value - froma) * (tob - fromb) / (toa - froma);
}

vec2 projectMask(vec2 coords) {
    return mapVec2(
        coords,
        u_maskprojectionuv,
        u_maskprojectionuv2,
        vec2(0.0),
        vec2(1.0)
    );
}
vec2 unProjectMask(vec2 coords) {
    return mapVec2(
        coords,
        vec2(0.0),
        vec2(1.0),
        u_maskprojectionuv,
        u_maskprojectionuv2
    );
}

vec2 raycast(vec2 center, vec2 pos, float step, float maxLength) {
    vec2 worldCoords = pos * u_masksize;
    vec2 worldCenter = center * u_masksize;

    vec2 dir = normalize(worldCoords - worldCenter);

    bool reverse = false;
    int level = 0;
    for(float i = 0; i < maxLength * 2; i++) {
        if (length(worldCoords - pos * u_masksize) > maxLength || level > marchPrecision) {
            break;
        }
        worldCoords += dir * step;

        vec4 col = texture2D(u_mask, worldCoords / u_masksize);

        if (
            (col.a < 0.9 && !reverse) ||
            (col.a > 0.9 && reverse)
        ) {
            reverse = !reverse;
            level++;
            step *= -0.5;
        }
    }

    return worldCoords;
}

void main() {
    vec2 uv = v_texCoords;
    vec2 projectedUV = projectMask(uv);

    vec4 baseColor = texture2D(u_texture, uv);
    vec4 maskColor = texture2D(u_mask, projectedUV);

    if (baseColor.a < 0.9 || maskColor.a < 0.9) discard;

    vec2 worldCoords = projectedUV * u_masksize;
    vec2 worldCenter = projectMask(vec2(0.5)) * u_masksize;

    vec2 hitPos = raycast(projectMask(vec2(0.5)), projectedUV, 16, 200.0);
    float dstPos = length(hitPos - worldCoords);
    float dstCenter = length(hitPos - worldCenter);

    float mapped = map(dstPos, 0.0, dstCenter, 0.0, 1.0);

    float z = (1/(1 - mapped) - 1) * u_clip.y / log(2, u_camscale * 2);

    gl_FragColor = vec4(vec3(0.0), 1.0);

    if (z <= 16) {
        vec2 plateUV = vec2(mod(hitPos.x + hitPos.y, 8.0)/8.0, mod(z, 1));
        vec4 color = texture2D(u_wall, mapVec2(plateUV, vec2(0.0), vec2(1.0), u_walluv.xy, u_walluv.zw));
        vec3 fade = vec3(16.0 - z)/ 16.0;
        gl_FragColor = color * vec4(fade, 1.0);
    }
}
