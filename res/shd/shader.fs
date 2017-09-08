#version 330

in vec2 fragmentCoord;

out vec4 result;

uniform sampler2D textureUnit;

void main() {
	result = texture2D(textureUnit, fragmentCoord);
}
