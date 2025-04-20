#define HIGHP

uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform sampler2D u_noise;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;


uniform float samplelen;
uniform float epsilonp1;

uniform vec4 u_toplayer;
uniform float tvariants;
uniform vec4 u_bottomlayer;
uniform float bvariants;
uniform vec4 u_truss;

float tileDst(vec2 coord, vec2 dir, float step, float maxLength) {
    float len = 0;

    vec2 tile = vec2(coord);

    for (float i = 0.0; i < maxLength * 2.0 + 2.0; i++){
        len += step;
        tile += dir * step;
        if (
            texture2D(u_texture, tile).a < 0.9 ||
            len > maxLength
        ) {
            break;
        }
    }

    return len;
}

void main() {
    vec2 uvCoords = v_texCoords;

    vec2 coords = vec2(
        uvCoords.x * u_resolution.x,
        uvCoords.y * u_resolution.y
    );
    vec2 inverseResolution = vec2(
        1.0 / u_resolution.x,
        1.0 / u_resolution.y
    );

    vec2 tile = mod(coords, 8.0) / vec2(8.0);

    vec2 dir = (uvCoords - vec2(0.5)) * vec2(1.0, u_resolution.y/u_resolution.x);
    float length = length(dir);

    vec3 col = vec3(normalize(dir), 0.0);
    col += vec3(0.0, 0.0, tileDst(coords * inverseResolution, dir, 0.1, 1));
    col *= 0.5;
    col += texture2D(u_texture, uvCoords).rgb * 0.5;

    gl_FragColor = vec4(col, 1.0);

}
