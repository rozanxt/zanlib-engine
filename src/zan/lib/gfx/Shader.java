package zan.lib.gfx;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import zan.lib.utl.TextResource;

public class Shader {

	private final Map<String, Integer> uniforms;

	private final int program;

	public Shader(String vertexSource, String fragmentSource) {
		uniforms = new HashMap<>();
		program = GL20.glCreateProgram();
		createProgram(vertexSource, fragmentSource);
	}

	public static Shader loadFromFile(String vertexPath, String fragmentPath) {
		return new Shader(TextResource.loadFromFileAsString(vertexPath), TextResource.loadFromFileAsString(fragmentPath));
	}

	public void delete() {
		GL20.glDeleteProgram(program);
	}

	public void bind() {
		GL20.glUseProgram(program);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	public void addUniform(String uniform) {
		int location = GL20.glGetUniformLocation(program, uniform);
		if (location < 0) {
			System.err.println("Unable to find uniform '" + uniform + "'!");
		}
		uniforms.put(uniform, location);
	}

	public int getUniform(String uniform) {
		return uniforms.get(uniform);
	}

	public void setUniform(String uniform, boolean value) {
		GL20.glUniform1i(getUniform(uniform), value ? GL11.GL_TRUE : GL11.GL_FALSE);
	}

	public void setUniform(String uniform, int value) {
		GL20.glUniform1i(getUniform(uniform), value);
	}

	public void setUniform(String uniform, float value) {
		GL20.glUniform1f(getUniform(uniform), value);
	}

	public void setUniform(String uniform, Vector2f value) {
		GL20.glUniform2f(getUniform(uniform), value.x, value.y);
	}

	public void setUniform(String uniform, Vector3f value) {
		GL20.glUniform3f(getUniform(uniform), value.x, value.y, value.z);
	}

	public void setUniform(String uniform, Vector4f value) {
		GL20.glUniform4f(getUniform(uniform), value.x, value.y, value.z, value.w);
	}

	public void setUniform(String uniform, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(16);
			value.get(buffer);
			GL20.glUniformMatrix4fv(getUniform(uniform), false, buffer);
		}
	}

	private void createProgram(String vertexSource, String fragmentSource) {
		int vertexShader = createShader(vertexSource, GL20.GL_VERTEX_SHADER);
		int fragmentShader = createShader(fragmentSource, GL20.GL_FRAGMENT_SHADER);
		GL20.glLinkProgram(program);
		if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.err.println("Unable to link shader program " + program + ":\n" + GL20.glGetProgramInfoLog(program));
		}
		deleteShader(vertexShader);
		deleteShader(fragmentShader);
	}

	private int createShader(String source, int type) {
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, source);
		GL20.glCompileShader(shader);
		if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Unable to compile shader " + shader + " of type " + type + ":\n" + GL20.glGetShaderInfoLog(shader));
		}
		GL20.glAttachShader(program, shader);
		return shader;
	}

	private void deleteShader(int shader) {
		GL20.glDetachShader(program, shader);
		GL20.glDeleteShader(shader);
	}

}
