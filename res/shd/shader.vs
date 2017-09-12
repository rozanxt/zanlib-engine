#version 330

layout (location = 0) in vec3 vertexPosition;
layout (location = 1) in vec2 vertexTexCoord;
layout (location = 2) in vec3 vertexNormal;

out vec3 modelViewPosition;
out vec3 modelViewNormal;
out vec2 fragmentTexCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
	vec4 mvPos = modelViewMatrix * vec4(vertexPosition, 1.0);
	gl_Position = projectionMatrix * mvPos;
	modelViewPosition = vec3(mvPos);
	modelViewNormal = vec3(modelViewMatrix * vec4(vertexNormal, 0.0));
	fragmentTexCoord = vertexTexCoord;
}
