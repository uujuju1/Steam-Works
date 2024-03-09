#define HIGHP

#define SCALE 500.0

uniform sampler2D u_noise;

uniform vec3 u_baseColor;

uniform vec2 u_resolution;
uniform vec2 u_campos;
uniform float u_time;

varying vec2 v_texCoords;

void main() {
    vec2 c = v_texCoords.xy;
    vec2 coords = (c * u_resolution) + u_campos;

    float atime = u_time/4000.0;

    float noise = (texture2D(u_noise, (coords) / SCALE + vec2(atime) * vec2(-0.9, 0.8)).x + texture2D(u_noise, (coords) / SCALE + vec2(atime * 1.1) * vec2(0.8, -1.0)).x) / 2.0;
//    float noise = texture2D(u_noise, coords).x;
    vec4 color = vec4(u_baseColor, 1.0);

    color *= round(noise * 4.0)/4.0;

    gl_FragColor = color;
}