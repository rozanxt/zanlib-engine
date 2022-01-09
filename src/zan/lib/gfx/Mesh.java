package zan.lib.gfx;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

	private static final int BYTES_PER_INT = 4;
	private static final int BYTES_PER_FLOAT = 4;

	private final int vao;
	private final int[] vbo;

	private int numElements;

	public Mesh(int numVBO, int numElements) {
		vao = GL30.glGenVertexArrays();
		vbo = new int[numVBO];
		for (int i = 0; i < vbo.length; i++) {
			vbo[i] = GL15.glGenBuffers();
		}
		this.numElements = numElements;
	}

	public void delete() {
		for (int i = 0; i < vbo.length; i++) {
			GL15.glDeleteBuffers(vbo[i]);
		}
		GL30.glDeleteVertexArrays(vao);
	}

	public void bind() {
		GL30.glBindVertexArray(vao);
	}

	public void unbind() {
		GL30.glBindVertexArray(0);
	}

	public void draw(int mode, int count, int offset) {
		GL15.glDrawElements(mode, count, GL11.GL_UNSIGNED_INT, offset * BYTES_PER_INT);
	}

	public void draw() {
		draw(GL11.GL_TRIANGLES, numElements, 0);
	}

	public void setVertexData(int index, float[] data) {
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo[index]);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		MemoryUtil.memFree(buffer);
	}

	public void setIndexData(int index, int[] data) {
		IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo[index]);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		MemoryUtil.memFree(buffer);
	}

	public void setVertexAttrib(int index, int size, int stride, int offset) {
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, stride * BYTES_PER_FLOAT, offset * BYTES_PER_FLOAT);
		GL20.glEnableVertexAttribArray(index);
	}

	public int getVAO() {
		return vao;
	}

	public int getVBO(int index) {
		return vbo[index];
	}

	public void setNumElements(int num) {
		numElements = num;
	}

	public int getNumElements() {
		return numElements;
	}

}
