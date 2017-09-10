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

	public int getUniformLocation(String uniform) {
		return uniforms.get(uniform);
	}

	public void setUniform(String uniform, int value) {
		glUniform1i(getUniformLocation(uniform), value);
	}

	public void setUniform(String uniform, float value) {
		glUniform1f(getUniformLocation(uniform), value);
	}

	public void setUniform(String uniform, Vector2f value) {
		glUniform2f(getUniformLocation(uniform), value.x, value.y);
	}

	public void setUniform(String uniform, Vector3f value) {
		glUniform3f(getUniformLocation(uniform), value.x, value.y, value.z);
	}

	public void setUniform(String uniform, Vector4f value) {
		glUniform4f(getUniformLocation(uniform), value.x, value.y, value.z, value.w);
	}

	public void setUniform(String uniform, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(16);
			value.get(buffer);
			glUniformMatrix4fv(getUniformLocation(uniform), false, buffer);
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

}
