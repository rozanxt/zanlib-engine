package zan.lib.gfx.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;
import java.util.List;

import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.lwjgl.system.MemoryUtil;

import zan.lib.gfx.mesh.ArrayMesh;

public class ModelMesh extends ArrayMesh {

	public static class Vertex {

		private static final int SLOTS = 8;
		private static final int BYTES = SLOTS * Float.BYTES;

		private final Vector3fc pos;
		private final Vector2fc tex;
		private final Vector3fc nrm;

		public Vertex(Vector3fc pos, Vector2fc tex, Vector3fc nrm) {
			this.pos = pos;
			this.tex = tex;
			this.nrm = nrm;
		}

	}

	public ModelMesh(List<Vertex> vertices) {
		vertexCount = vertices.size();

		glBindVertexArray(vao);

		FloatBuffer vertexData = MemoryUtil.memAllocFloat(vertexCount * Vertex.SLOTS);
		for (Vertex vertex : vertices) {
			vertexData.put(vertex.pos.x());
			vertexData.put(vertex.pos.y());
			vertexData.put(vertex.pos.z());
			vertexData.put(vertex.tex.x());
			vertexData.put(vertex.tex.y());
			vertexData.put(vertex.nrm.x());
			vertexData.put(vertex.nrm.y());
			vertexData.put(vertex.nrm.z());
		}
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexData.flip(), GL_STATIC_DRAW);
		MemoryUtil.memFree(vertexData);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.BYTES, 0 * Float.BYTES);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.BYTES, 3 * Float.BYTES);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.BYTES, 5 * Float.BYTES);

		glBindVertexArray(0);
	}

}
