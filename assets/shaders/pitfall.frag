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

// make sure it's an odd number so that the hitPos ends up inside the mask
const int marchPrecision = 7;


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
    for(float i = 0.0; i < maxLength * 2.0; i++) {
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

    if (maskColor.a < 0.9) {
        gl_FragColor = baseColor;
        return;
    }

    vec2 worldCoords = projectedUV * u_masksize;
    vec2 worldCenter = projectMask(vec2(0.5)) * u_masksize;

    vec2 hitPos = raycast(worldCenter, worldCoords, normalize(worldCoords - worldCenter), 8.0, 200.0);
    float dstPos = length(hitPos - worldCoords);
    float dstCenter = length(hitPos - worldCenter);

    float mapped = map(dstPos, 0.0, dstCenter, 0.0, 1.0);

    // in tiles
    float z = (1.0/(1.0 - mapped) - 1.0) * u_scale;

    gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);

    // walls
    if (z <= 32.0) {
        vec2 tile = floor(hitPos / 8.0);

        float offset = rand(tile) * 16.0;

        if (z <= 32.0 - offset) {
            vec2 divisions = floor(u_wallsize / u_walltilesize);
            vec2 selection = floor(vec2(rand(tile + floor(z)), rand(tile + floor(z) + 1.0)) * divisions);

            vec2 plateUV = vec2(mod(hitPos.x + hitPos.y, 8.0)/8.0, mod(z, 1.0));
            vec4 color = texture2D(u_wall, mapVec2(plateUV + selection, vec2(0.0), vec2(4.0), u_walluv.xy, u_walluv.zw));
            vec3 fade = vec3(32.0 - z)/ 32.0;
            gl_FragColor = color * vec4(fade, 1.0);
        }
    }

    // TODO fix connecting with other pitfalls
    // chasm
    if (z >= 48 && texture2D(u_mask, hitPos / u_masksize).r < 0.9) {
        float unMapped = 1.0 - 1.0/(64.0/u_scale + 1.0);
        vec2 unProjected = (worldCenter * -unMapped + worldCoords) / (vec2(1.0 - unMapped));

        if (
            texture2D(u_mask, unProjected / u_masksize).r < 0.9
        ) {
            float startOffset = texture2D(u_noise, hitPos / 32.0 + vec2(u_time, -u_time * 2)/3600).a * 8.0;
            float endOffset = texture2D(u_noise, hitPos / 32.0 + vec2(-u_time * 3, u_time)/3600).a * 8.0;

            float fadeNoise = 0;
            for (int i = 0; i < 4; i++) {
                float randX = rand(vec2(i, 1.0)) * 2.0 - 1.0;
                float randY = rand(vec2(1.0, i)) * 2.0 - 1.0;
                fadeNoise += texture2D(u_noise, unProjected / 128.0 + vec2(u_time * randX, u_time * randY)/3600.0).a;
            }
            fadeNoise /= 2.0;
            fadeNoise *= fadeNoise;

            vec3 fade = vec3(min(1.0, map(z, 56.0 - startOffset, 64.0 - endOffset, 0.0, 1.0)));
            gl_FragColor = vec4(fade * fadeNoise, 1.0) * vec4(115.0, 16.0, 7.0, 255.0)/255.0;
        }
    }

    // waterfall
    if (z <= 32.0 && texture2D(u_mask, hitPos / u_masksize).g < 0.9) {
        vec4 color = vec4(110.0, 112.0, 155.0, 0)/255.0;
        for(float i = 0.0; i < 3.0; i++) {
            float offsetX = rand(vec2(i));
            float offsetY = rand(vec2(z));
            float scl = pow(2.0, i);
            vec2 noiseUV = vec2((hitPos.x + hitPos.y + offsetX)/32.0, z/64.0/scl - u_time/120.0);
            vec4 col = texture2D(u_noise, noiseUV);
            col = pow(col, vec4(2.0 - z/32.0));

            if (col.a > 0.25) {
                color = mix(color, col, vec4(col.a));
            }
        }
        vec3 fade = vec3(32.0 - z)/ 32.0;
        if (color.r > 0.0) gl_FragColor = vec4(color.rgb * fade, 1.0);
    }

    // grating
    if (z >= 4.0) {
        float unMapped = 1.0 - 1.0/(4.0/u_scale + 1.0);
        vec2 unProjected = (worldCenter * -unMapped + worldCoords) / (vec2(1.0 - unMapped));

        if (texture2D(u_mask, unProjected / u_masksize).b < 0.9) {
            vec2 gratingUV = mod(unProjected * 4.0 / u_gratingsize, vec2(1.0));
            vec4 color = texture2D(u_grating, mapVec2(gratingUV, vec2(0.0), vec2(1.0), u_gratinguv.xy, u_gratinguv.zw));
            float dstPos = length(raycast(worldCenter, unProjected, vec2(0.707), 8.0, 40.0) - unProjected);
            if (color.a > 0.0) {
                gl_FragColor = color;
                if (dstPos < 32.0) gl_FragColor = vec4(mix(color.rgb, vec3(0.0), vec3(0.3)), 1.0);
            }
        }
    }
}
