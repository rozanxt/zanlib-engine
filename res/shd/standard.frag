#version 330 core

in vec3 fragmentPosition;
in vec3 fragmentNormal;
in vec2 fragmentTexCoord;

out vec4 outputColor;

uniform vec4 uniformColor;
uniform bool enableTexture;
uniform sampler2D textureUnit;

void main() {
	vec3 lightPosition = vec3(0.0, 0.0, 0.0);
	vec3 lightVector = lightPosition - fragmentPosition;
	vec3 lightDirection = normalize(lightVector);
	float lightDistance = length(lightVector);
	
	vec3 lightColor = vec3(1.0, 1.0, 1.0);
	float attenuation = 0.05;
	float ambientLight = 0.3;
	float diffuseLight = max(dot(fragmentNormal, lightDirection), 0.0) / (1.0 + (attenuation * lightDistance * lightDistance));
	
	vec4 color = uniformColor;
	if (enableTexture) color *= texture2D(textureUnit, fragmentTexCoord);
	outputColor = vec4((ambientLight + diffuseLight * lightColor) * color.xyz, color.w);
}
