#version 330 core

in vec2 fragmentTexCoord;

out vec4 outputColor;

uniform sampler2D textureUnit;

void main() {
	outputColor = texture2D(textureUnit, fragmentTexCoord);
}
