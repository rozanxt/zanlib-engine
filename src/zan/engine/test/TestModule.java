package zan.engine.test;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;

import zan.engine.core.Engine;
import zan.engine.core.Module;
import zan.engine.gfx.MeshData;
import zan.engine.gfx.RenderObject;
import zan.engine.gfx.ShaderProgram;
import zan.engine.gfx.Texture;
import zan.engine.gfx.TextureObject;
import zan.engine.gfx.VertexData;

public class TestModule implements Module {

	private Engine engine;

	private ShaderProgram shader;

	private VertexData mesh;

	private Texture texture;

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

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		shader = ShaderProgram.loadShader("res/shd/shader.vs", "res/shd/shader.fs");
		shader.addUniform("projectionMatrix");
		shader.addUniform("modelViewMatrix");
		shader.addUniform("textureUnit");

		float[] positions = new float[] {
			-0.5f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f,
			0.5f, 0.5f, 0.0f,
			-0.5f, 0.5f, 0.0f,
		};
		float[] texcoords = new float[] {
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f,
		};
		float[] normals = new float[0];
		int[] indices = { 0, 1, 3, 2, 3, 1 };
		mesh = new MeshData(positions, texcoords, normals, indices);
		texture = Texture.loadTexture("res/ico/icon.png");
		object = new TextureObject(mesh, texture);
	}

	@Override
	public void exit() {
		texture.delete();
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
		shader.setUniform("projectionMatrix", new Matrix4f());
		shader.setUniform("modelViewMatrix", new Matrix4f());
		shader.setUniform("textureUnit", 0);
		object.render();
		shader.unbind();
	}

}
