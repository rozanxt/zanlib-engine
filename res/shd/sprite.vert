#version 330 core

layout(location = 0) in vec2 vertexPosition;
layout(location = 1) in vec2 vertexTexCoord;
layout(location = 2) in vec2 instancePosition;
layout(location = 3) in vec2 instanceTexCoord;

out vec2 fragmentTexCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
	fragmentTexCoord = instanceTexCoord + vertexTexCoord;
	gl_Position = projectionMatrix * viewMatrix * vec4(instancePosition + vertexPosition, 0.0, 1.0);
}
