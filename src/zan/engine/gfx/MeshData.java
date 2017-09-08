package zan.engine.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class MeshData extends VertexData {

	public MeshData(float[] positions, float[] texcoords, float[] normals, int[] indices) {
		super(indices.length, 4);
		glBindVertexArray(vao);
		sendBufferData(0, positions, GL_ARRAY_BUFFER);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		sendBufferData(1, texcoords, GL_ARRAY_BUFFER);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);
		sendBufferData(2, normals, GL_ARRAY_BUFFER);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(2);
		sendBufferData(3, indices, GL_ELEMENT_ARRAY_BUFFER);
		glBindVertexArray(0);
	}

}
