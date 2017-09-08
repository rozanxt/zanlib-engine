package zan.engine.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

	protected final int program;

	public ShaderProgram(String vertexSource, String fragmentSource) {
		program = glCreateProgram();
		createProgram(vertexSource, fragmentSource);
	}

	public void bind() {
		glUseProgram(program);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void delete() {
		glDeleteProgram(program);
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

}
