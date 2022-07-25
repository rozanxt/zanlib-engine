package zan.lib.gfx.msh;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.util.ArrayList;
import java.util.List;

public abstract class InstancedMesh<T> implements Mesh {

	protected final ArrayMesh mesh;

	protected final int ibo;

	protected final List<T> instances;

	protected int instanceCount;

	public InstancedMesh(ArrayMesh mesh) {
		this.mesh = mesh;
		ibo = glGenBuffers();
		instances = new ArrayList<>();
	}

	@Override
	public void delete() {
		mesh.delete();
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
		mesh.draw(instanceCount);
	}

	public void add(T instance) {
		instances.add(instance);
	}

	public abstract void process();

}
