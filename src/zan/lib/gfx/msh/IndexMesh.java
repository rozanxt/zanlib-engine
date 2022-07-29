package zan.lib.gfx.msh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public abstract class IndexMesh extends ArrayMesh {

	protected final int ebo;

	protected int indexCount;

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
		glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
	}

	public int getIndexCount() {
		return indexCount;
	}

}
