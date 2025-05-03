#define HIGHP

uniform sampler2D u_texture;
uniform sampler2D u_noise;
uniform sampler2D u_mask;

uniform vec2 u_maskprojectionuv;
uniform vec2 u_maskprojectionuv2;
uniform vec2 u_masksize;

uniform float u_scale;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

const int marchPrecision = 6;


uniform sampler2D u_wall;
uniform vec4 u_walluv;
uniform vec2 u_wallsize;
uniform float u_walltilesize;


uniform sampler2D u_grating;
uniform vec4 u_gratinguv;
uniform vec2 u_gratingsize;

float rand(vec2 n) {
    return fract(sin(dot(n, vec2(12.9898, 4.1414))) * 43758.5453);
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

vec2 raycast(vec2 center, vec2 pos, vec2 dir, float step, float maxLength) {
    vec2 hitPos = pos;

    bool reverse = false;
    int level = 0;
    for(float i = 0; i < maxLength * 2; i++) {
        if (length(hitPos - pos) > maxLength || level > marchPrecision) {
            break;
        }
        hitPos += dir * step;

        vec4 col = texture2D(u_mask, hitPos / u_masksize);

        if (
            (col.a < 0.9 && !reverse) ||
            (col.a > 0.9 && reverse)
        ) {
            reverse = !reverse;
            level++;
            step *= -0.5;
        }
    }

    return hitPos;
}

void main() {
    vec2 uv = v_texCoords;
    vec2 projectedUV = projectMask(uv);

    vec4 baseColor = texture2D(u_texture, uv);
    vec4 maskColor = texture2D(u_mask, projectedUV);

    if (maskColor.a < 0.9) discard;

    vec2 worldCoords = projectedUV * u_masksize;
    vec2 worldCenter = projectMask(vec2(0.5)) * u_masksize;

    vec2 hitPos = raycast(worldCenter, worldCoords, normalize(worldCoords - worldCenter), 8, 200.0);
    float dstPos = length(hitPos - worldCoords);
    float dstCenter = length(hitPos - worldCenter);

    float mapped = map(dstPos, 0.0, dstCenter, 0.0, 1.0);

    float z = (1/(1 - mapped) - 1) * u_scale;

    gl_FragColor = vec4(vec3(0.0), 1.0);

    // walls
    if (z <= 32) {
        vec2 tile = floor(hitPos / 8.0);

        float offset = rand(tile) * 16;

        if (z <= 32 - offset) {
            vec2 divisions = floor(u_wallsize / u_walltilesize);
            vec2 selection = floor(vec2(rand(tile + floor(z)), rand(tile + floor(z) + 1)) * divisions);

            vec2 plateUV = vec2(mod(hitPos.x + hitPos.y, 8.0)/8.0, mod(z, 1));
            vec4 color = texture2D(u_wall, mapVec2(plateUV + selection, vec2(0.0), vec2(4.0), u_walluv.xy, u_walluv.zw));
            vec3 fade = vec3(32.0 - z)/ 32.0;
            gl_FragColor = color * vec4(fade, 1.0);
        }
    }

    // TODO proper grating positioning when alongside ungrated pitfalls
    // grating
    if (z >= 4 && maskColor.b < 0.9) {
        float unMapped = 1 - 1/(4/u_scale + 1);
        vec2 unProjected = (worldCenter * -unMapped + worldCoords) / (vec2(1 - unMapped));
        vec2 gratingUV = mod(unProjected * 4 / u_gratingsize, vec2(1));
        vec4 color = texture2D(u_grating, mapVec2(gratingUV, vec2(0.0), vec2(1.0), u_gratinguv.xy, u_gratinguv.zw));
        float dstPos = length(raycast(worldCenter, unProjected, vec2(0.707), 8, 40) - unProjected);
        if (color.a > 0) {
            gl_FragColor = color;
            if (dstPos < 32) gl_FragColor = vec4(mix(color.rgb, vec3(0), vec3(0.3)), 1.0);
        }
    }
}
