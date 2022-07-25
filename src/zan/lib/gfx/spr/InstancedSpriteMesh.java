package zan.lib.gfx.spr;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import zan.lib.gfx.msh.InstancedMesh;
import zan.lib.gfx.spr.InstancedSpriteMesh.Instance;

public class InstancedSpriteMesh extends InstancedMesh<Instance> {

	public static class Instance {

		private static final int SLOTS = 4;
		private static final int BYTES = SLOTS * Float.BYTES;

		private final float x;
		private final float y;

		private final int frame;

		public Instance(float x, float y, int frame) {
			this.x = x;
			this.y = y;
			this.frame = frame;
		}

	}

	private final SpriteMesh mesh;

	public InstancedSpriteMesh(SpriteMesh mesh) {
		super(mesh);

		this.mesh = mesh;

		mesh.bind();

		glBindBuffer(GL_ARRAY_BUFFER, ibo);

		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);

		glVertexAttribPointer(2, 2, GL_FLOAT, false, Instance.BYTES, 0 * Float.BYTES);
		glVertexAttribPointer(3, 2, GL_FLOAT, false, Instance.BYTES, 2 * Float.BYTES);

		glVertexAttribDivisor(2, 1);
		glVertexAttribDivisor(3, 1);

		mesh.unbind();
	}

	@Override
	public void process() {
		instanceCount = instances.size();

		FloatBuffer instanceData = MemoryUtil.memAllocFloat(instanceCount * Instance.SLOTS);
		for (Instance instance : instances) {
			instanceData.put(instance.x);
			instanceData.put(instance.y);

			int i = instance.frame % mesh.getColumns();
			int j = instance.frame / mesh.getColumns();

			instanceData.put((float) i / (float) mesh.getColumns());
			instanceData.put((float) j / (float) mesh.getRows());
		}
		glBindBuffer(GL_ARRAY_BUFFER, ibo);
		glBufferData(GL_ARRAY_BUFFER, instanceData.flip(), GL_STREAM_DRAW);
		MemoryUtil.memFree(instanceData);

		instances.clear();
	}

}
