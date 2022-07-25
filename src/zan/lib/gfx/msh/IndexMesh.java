package zan.lib.gfx.msh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

public abstract class IndexMesh extends ArrayMesh {

	protected final int ebo;

	protected int elementCount;

	public IndexMesh() {
		ebo = glGenBuffers();
	}

	@Override
	public void delete() {
		super.delete();
		glDeleteBuffers(ebo);
	}

	@Override
	public void draw() {
		glDrawElements(GL_TRIANGLES, elementCount, GL_UNSIGNED_INT, 0);
	}

	@Override
	protected void draw(int instanceCount) {
		glDrawElementsInstanced(GL_TRIANGLES, elementCount, GL_UNSIGNED_INT, 0, instanceCount);
	}

}
