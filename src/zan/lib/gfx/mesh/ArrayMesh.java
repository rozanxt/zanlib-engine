package zan.lib.gfx.mesh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

public abstract class ArrayMesh implements Mesh {

	protected final int vao;
	protected final int vbo;

	protected int vertexCount;

	public ArrayMesh() {
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
	}

	@Override
	public void delete() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
	}

	@Override
	public void bind() {
		glBindVertexArray(vao);
	}

	@Override
	public void unbind() {
		glBindVertexArray(0);
	}

	@Override
	public void draw() {
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
	}

	protected void draw(int instanceCount) {
		glDrawArraysInstanced(GL_TRIANGLES, 0, vertexCount, instanceCount);
	}

}
