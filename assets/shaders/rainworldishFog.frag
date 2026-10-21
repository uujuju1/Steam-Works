uniform sampler2D u_texture;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;
uniform float u_scale;

uniform float u_intensity;

varying vec2 v_texCoords;

const vec4 tint = vec4(0.75, 1.0, 0.75, 0.5);

const float bands = 20;

void main() {
  vec2 uv = (v_texCoords * u_resolution + u_campos) / u_resolution / u_scale * vec2(1.0, 2.0) * 1.0;

  float time = u_time / 5.0;

  float color1 = texture2D(u_texture, uv + vec2(time * -2.0, time / 5.0) / u_resolution / u_scale).a;
  float color2 = texture2D(u_texture, uv + vec2(time * 1.5, time * -0.5) / u_resolution / u_scale).a;

  float alpha = (color1 + color2) / 2.0;
	gl_FragColor = vec4(tint.xyz, tint.a * round(alpha * alpha * bands) / bands) * u_intensity;
}
