#version 330 core

layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec2 vertexTexCoord;
layout(location = 2) in vec3 vertexNormal;

out vec3 fragmentPosition;
out vec3 fragmentNormal;
out vec2 fragmentTexCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	fragmentPosition = vec3(modelViewMatrix * vec4(vertexPosition, 1.0));
	fragmentNormal = vec3(modelViewMatrix * vec4(vertexNormal, 0.0));
	fragmentTexCoord = vertexTexCoord;
	gl_Position = projectionMatrix * vec4(fragmentPosition, 1.0);
}
