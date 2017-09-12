#version 330

in vec3 modelViewPosition;
in vec3 modelViewNormal;
in vec2 fragmentTexCoord;

out vec4 result;

uniform vec4 uniformColor;
uniform bool enableTexture;
uniform sampler2D textureUnit;

void main() {
	vec3 lightPosition = vec3(0.0, 0.0, 0.0);
	vec3 lightDifference = lightPosition - modelViewPosition;
	vec3 lightVector = normalize(lightDifference);
	float lightDistance = length(lightDifference);
	float diffuse = max(dot(modelViewNormal, lightVector), 0.0);
	diffuse = diffuse * (1.0 / (1.0 + (0.05 * lightDistance * lightDistance)));
	diffuse = diffuse + 0.3;
	vec4 color = uniformColor;
	if (enableTexture) color = color * texture2D(textureUnit, fragmentTexCoord);
	result = vec4(diffuse * color.xyz, color.w);
}
