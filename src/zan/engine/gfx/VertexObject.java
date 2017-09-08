package zan.engine.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class VertexObject implements RenderObject {

	protected final VertexData data;

	protected final int mode;
	protected final int count;
	protected final int offset;

	public VertexObject(VertexData vertexData, int mode, int count, int offset) {
		this.data = vertexData;
		this.mode = mode;
		this.count = count;
		this.offset = offset;
	}

	public void render() {
		glBindVertexArray(data.getVAO());
		glDrawElements(mode, count, GL_UNSIGNED_INT, offset);
		glBindVertexArray(0);
	}

}
