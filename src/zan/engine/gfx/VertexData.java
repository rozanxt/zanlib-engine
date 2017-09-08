package zan.engine.gfx;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

public abstract class VertexData {

	protected final int vao;
	protected final int[] vbo;

	protected final int vertexCount;

	public VertexData(int vertexCount, int vboCount) {
		vao = glGenVertexArrays();
		vbo = new int[vboCount];
		for (int i = 0; i < vbo.length; i++) {
			vbo[i] = glGenBuffers();
		}
		this.vertexCount = vertexCount;
	}

	public void delete() {
		for (int i = 0; i < vbo.length; i++) {
			glDeleteBuffers(vbo[i]);
		}
		glDeleteVertexArrays(vao);
	}

	public int getVAO() {
		return vao;
	}

	public int getVBO(int index) {
		return vbo[index];
	}

	public int getVertexCount() {
		return vertexCount;
	}

	protected void sendBufferData(int index, int[] data, int target) {
		IntBuffer buffer = null;
		try {
			buffer = MemoryUtil.memAllocInt(data.length);
			buffer.put(data).flip();
			glBindBuffer(target, vbo[index]);
			glBufferData(target, buffer, GL_STATIC_DRAW);
		} finally {
			if (buffer != null) {
				MemoryUtil.memFree(buffer);
			}
		}
	}

	protected void sendBufferData(int index, float[] data, int target) {
		FloatBuffer buffer = null;
		try {
			buffer = MemoryUtil.memAllocFloat(data.length);
			buffer.put(data).flip();
			glBindBuffer(target, vbo[index]);
			glBufferData(target, buffer, GL_STATIC_DRAW);
		} finally {
			if (buffer != null) {
				MemoryUtil.memFree(buffer);
			}
		}
	}

}
