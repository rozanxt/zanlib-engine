package zan.lib.gfx.msh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

public abstract class InstancedMesh implements Mesh {

	protected final ArrayMesh mesh;

	protected final int ibo;

	protected int instanceCount;

	public InstancedMesh(ArrayMesh mesh) {
		this.mesh = mesh;
		ibo = glGenBuffers();
	}

	@Override
	public void delete() {
		glDeleteBuffers(ibo);
	}

	@Override
	public void bind() {
		mesh.bind();
	}

	@Override
	public void unbind() {
		mesh.unbind();
	}

	@Override
	public void draw() {
		glDrawArraysInstanced(GL_TRIANGLES, 0, mesh.getVertexCount(), instanceCount);
	}

	public int getInstanceCount() {
		return instanceCount;
	}

}
