uniform float u_time;
uniform vec2 u_resolution;

varying vec2 v_texCoords;

void main() {
  vec2 fragCoord = v_texCoords * u_resolution;

  vec2 baseRes = vec2(1920.0, 1080.0);
  vec3 col = vec3(
    110.0 / 255.0,
    112.0 / 255.0,
    128.0 / 255.0
  );

  if (mod(fragCoord.x + fragCoord.y + u_time * 1920.0 / 10.0, 1920.0 / 10.0) < 1920.0 / 20.0) col = vec3(
    86.0 / 255.0,
    86.0 / 255.0,
    102.0 / 255.0
  );

  gl_FragColor = vec4(col, 1.0);
}