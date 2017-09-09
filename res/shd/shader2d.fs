#version 330

in vec3 modelViewPosition;
in vec3 modelViewNormal;
in vec2 fragmentCoord;

out vec4 result;

uniform sampler2D textureUnit;
uniform vec4 tintColor;

void main() {
	result = tintColor * texture2D(textureUnit, fragmentCoord);
}
