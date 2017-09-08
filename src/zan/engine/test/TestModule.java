package zan.engine.test;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import zan.engine.core.Engine;
import zan.engine.core.Module;
import zan.engine.gfx.MeshData;
import zan.engine.gfx.RenderObject;
import zan.engine.gfx.ShaderProgram;
import zan.engine.gfx.VertexData;
import zan.engine.gfx.VertexObject;
import zan.engine.util.ResourceUtil;

public class TestModule implements Module {

	private Engine engine;

	private ShaderProgram shader;

	private VertexData mesh;

	private RenderObject object;

	public TestModule(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void init() {
		glfwSetKeyCallback(engine.getWindow().getHandle(), (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				engine.getWindow().close();
			}
			if (key == GLFW_KEY_F11 && action == GLFW_RELEASE) {
				engine.getWindow().setFullScreen(!engine.getWindow().isFullScreen());
			}
		});

		shader = new ShaderProgram(ResourceUtil.getTextResourceAsString("res/shd/shader.vs"), ResourceUtil.getTextResourceAsString("res/shd/shader.fs"));

		float[] positions = new float[] {
			-0.5f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f,
			0.5f, 0.5f, 0.0f,
			-0.5f, 0.5f, 0.0f,
		};
		float[] texcoords = new float[0];
		float[] normals = new float[0];
		int[] indices = { 0, 1, 3, 2, 3, 1 };
		mesh = new MeshData(positions, texcoords, normals, indices);
		object = new VertexObject(mesh, GL_TRIANGLES, mesh.getVertexCount(), 0);
	}

	@Override
	public void exit() {
		mesh.delete();
		shader.delete();
	}

	@Override
	public void input() {

	}

	@Override
	public void update() {

	}

	@Override
	public void render() {
		glClearColor(1.0f, 0.5f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, engine.getWindow().getWidth(), engine.getWindow().getHeight());

		shader.bind();
		object.render();
		shader.unbind();
	}

}
