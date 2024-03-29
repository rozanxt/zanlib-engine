package zan.lib.gfx.mdl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glVertexAttribIPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.ByteBuffer;
import java.util.List;

import org.joml.Matrix4fc;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryUtil;

import zan.lib.gfx.msh.ArrayMesh;
import zan.lib.gfx.msh.InstancedMesh;

public class InstancedModelMesh extends InstancedMesh {

	public static class Instance {

		private static final int BYTES = 20 * Float.BYTES + 4 * Integer.BYTES;

		private final Matrix4fc tfm;
		private final Vector4fc clr;

		private final int sid;

		public Instance(Matrix4fc tfm, Vector4fc clr, int sid) {
			this.tfm = tfm;
			this.clr = clr;
			this.sid = sid;
		}

	}

	public InstancedModelMesh(ArrayMesh mesh) {
		super(mesh);

		mesh.bind();

		glBindBuffer(GL_ARRAY_BUFFER, ibo);

		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		glEnableVertexAttribArray(7);
		glEnableVertexAttribArray(8);
		glEnableVertexAttribArray(9);

		glVertexAttribPointer(4, 4, GL_FLOAT, false, Instance.BYTES, 0 * Float.BYTES);
		glVertexAttribPointer(5, 4, GL_FLOAT, false, Instance.BYTES, 4 * Float.BYTES);
		glVertexAttribPointer(6, 4, GL_FLOAT, false, Instance.BYTES, 8 * Float.BYTES);
		glVertexAttribPointer(7, 4, GL_FLOAT, false, Instance.BYTES, 12 * Float.BYTES);
		glVertexAttribPointer(8, 4, GL_FLOAT, false, Instance.BYTES, 16 * Float.BYTES);
		glVertexAttribIPointer(9, 4, GL_INT, Instance.BYTES, 20 * Float.BYTES);

		glVertexAttribDivisor(4, 1);
		glVertexAttribDivisor(5, 1);
		glVertexAttribDivisor(6, 1);
		glVertexAttribDivisor(7, 1);
		glVertexAttribDivisor(8, 1);
		glVertexAttribDivisor(9, 1);

		mesh.unbind();
	}

	public void build(List<Instance> instances) {
		instanceCount = instances.size();

		ByteBuffer instanceData = MemoryUtil.memAlloc(instanceCount * Instance.BYTES);
		for (Instance instance : instances) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					instanceData.putFloat(instance.tfm.get(i, j));
				}
			}
			for (int i = 0; i < 4; i++) {
				instanceData.putFloat(instance.clr.get(i));
			}
			int sid = instance.sid;
			for (int i = 0; i < 4; i++) {
				instanceData.putInt(sid % 255);
				sid /= 255;
			}
		}
		glBindBuffer(GL_ARRAY_BUFFER, ibo);
		glBufferData(GL_ARRAY_BUFFER, instanceData.flip(), GL_STREAM_DRAW);
		MemoryUtil.memFree(instanceData);
	}

}
