#version 330

in vec2 fragmentCoord;

out vec4 result;

uniform bool useTexture;
uniform sampler2D textureUnit;
uniform vec4 tintColor;

void main() {
	vec4 color = tintColor;
	if (useTexture) color = color * texture2D(textureUnit, fragmentCoord); 
	result = color;
}
