#version 330 core

layout(location = 0) in vec2 vertexPosition;
layout(location = 1) in vec2 vertexTexCoord;

out vec2 fragmentTexCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	fragmentTexCoord = vertexTexCoord;
	gl_Position = projectionMatrix*modelViewMatrix*vec4(vertexPosition, 0.0, 1.0);
}
