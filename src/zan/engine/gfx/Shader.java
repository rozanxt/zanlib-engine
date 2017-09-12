package zan.engine.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import zan.engine.util.TextResource;

public class Shader {

	private final Map<String, Integer> uniforms;

	private final int program;

	public Shader(String vertexSource, String fragmentSource) {
		uniforms = new HashMap<>();
		program = glCreateProgram();
		createProgram(vertexSource, fragmentSource);
	}

	public void delete() {
		glDeleteProgram(program);
	}

	public void bind() {
		glUseProgram(program);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void addUniform(String uniform) {
		int location = glGetUniformLocation(program, uniform);
		if (location < 0) {
			System.err.println("Unable to find uniform '" + uniform + "'!");
		}
		uniforms.put(uniform, location);
	}

	public int getUniform(String uniform) {
		return uniforms.get(uniform);
	}

	public void setUniform(String uniform, boolean value) {
		glUniform1i(getUniform(uniform), value ? GL_TRUE : GL_FALSE);
	}

	public void setUniform(String uniform, int value) {
		glUniform1i(getUniform(uniform), value);
	}

	public void setUniform(String uniform, float value) {
		glUniform1f(getUniform(uniform), value);
	}

	public void setUniform(String uniform, Vector2f value) {
		glUniform2f(getUniform(uniform), value.x, value.y);
	}

	public void setUniform(String uniform, Vector3f value) {
		glUniform3f(getUniform(uniform), value.x, value.y, value.z);
	}

	public void setUniform(String uniform, Vector4f value) {
		glUniform4f(getUniform(uniform), value.x, value.y, value.z, value.w);
	}

	public void setUniform(String uniform, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(16);
			value.get(buffer);
			glUniformMatrix4fv(getUniform(uniform), false, buffer);
		}
	}

	private void createProgram(String vertexSource, String fragmentSource) {
		int vertexShader = createShader(vertexSource, GL_VERTEX_SHADER);
		int fragmentShader = createShader(fragmentSource, GL_FRAGMENT_SHADER);
		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Unable to link shader program " + program + ":\n" + glGetProgramInfoLog(program));
		}
		deleteShader(vertexShader);
		deleteShader(fragmentShader);
	}

	private int createShader(String source, int type) {
		int shader = glCreateShader(type);
		glShaderSource(shader, source);
		glCompileShader(shader);
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Unable to compile shader " + shader + " of type " + type + ":\n" + glGetShaderInfoLog(shader));
		}
		glAttachShader(program, shader);
		return shader;
	}

	private void deleteShader(int shader) {
		glDetachShader(program, shader);
		glDeleteShader(shader);
	}

	public static Shader loadFromFile(String vertexPath, String fragmentPath) {
		return new Shader(TextResource.loadFromFileAsString(vertexPath), TextResource.loadFromFileAsString(fragmentPath));
	}

	public static Shader loadDefault() {
		String vertexSource = "#version 330\n" +
			"\n" +
			"layout (location = 0) in vec3 vertexPosition;\n" +
			"layout (location = 1) in vec2 vertexTexCoord;\n" +
			"\n" +
			"out vec2 fragmentTexCoord;\n" +
			"\n" +
			"uniform mat4 projectionMatrix;\n" +
			"uniform mat4 modelViewMatrix;\n" +
			"\n" +
			"void main() {\n" +
			"	gl_Position = projectionMatrix * modelViewMatrix * vec4(vertexPosition, 1.0);\n" +
			"	fragmentTexCoord = vertexTexCoord;\n" +
			"}\n";
		String fragmentSource = "#version 330\n" +
			"\n" +
			"in vec2 fragmentTexCoord;\n" +
			"\n" +
			"out vec4 result;\n" +
			"\n" +
			"uniform vec4 uniformColor;\n" +
			"uniform bool enableTexture;\n" +
			"uniform sampler2D textureUnit;\n" +
			"\n" +
			"void main() {\n" +
			"	vec4 color = uniformColor;\n" +
			"	if (enableTexture) color = color * texture2D(textureUnit, fragmentTexCoord); \n" +
			"	result = color;\n" +
			"}\n";
		return new Shader(vertexSource, fragmentSource);
	}

}
