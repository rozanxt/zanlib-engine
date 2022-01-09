#version 330

in vec2 fragmentTexCoord;

out vec4 result;

uniform vec4 uniformColor;
uniform bool enableTexture;
uniform sampler2D textureUnit;

void main() {
	vec4 color = uniformColor;
	if (enableTexture) color = color * texture2D(textureUnit, fragmentTexCoord);
	result = color;
}
