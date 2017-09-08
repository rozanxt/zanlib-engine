package zan.engine.test;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;

import zan.engine.core.Engine;
import zan.engine.core.Module;
import zan.engine.gfx.RenderObject;
import zan.engine.gfx.ShaderProgram;
import zan.engine.gfx.Texture;
import zan.engine.gfx.TextureObject;
import zan.engine.gfx.VertexData;
import zan.engine.util.OBJLoader;

public class TestModule implements Module {

	private Engine engine;

	private ShaderProgram shader;

	private VertexData mesh;

	private Texture texture;

	private RenderObject object;

	private float rotationX = 0.0f;
	private float rotationY = 0.0f;

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

		glEnable(GL_DEPTH_TEST);

		shader = ShaderProgram.loadShader("res/shd/shader.vs", "res/shd/shader.fs");
		shader.addUniform("projectionMatrix");
		shader.addUniform("modelViewMatrix");
		shader.addUniform("textureUnit");

		mesh = OBJLoader.loadOBJ("res/obj/block.obj");
		texture = Texture.loadTexture("res/img/grassblock.png");
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
		if (glfwGetKey(engine.getWindow().getHandle(), GLFW_KEY_RIGHT) == GLFW_PRESS) {
			rotationY += 2.0f;
		}
		if (glfwGetKey(engine.getWindow().getHandle(), GLFW_KEY_LEFT) == GLFW_PRESS) {
			rotationY -= 2.0f;
		}
		if (glfwGetKey(engine.getWindow().getHandle(), GLFW_KEY_UP) == GLFW_PRESS) {
			rotationX -= 2.0f;
		}
		if (glfwGetKey(engine.getWindow().getHandle(), GLFW_KEY_DOWN) == GLFW_PRESS) {
			rotationX += 2.0f;
		}
	}

	@Override
	public void render() {
		glClearColor(0.0f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, engine.getWindow().getWidth(), engine.getWindow().getHeight());

		shader.bind();
		shader.setUniform("projectionMatrix", new Matrix4f().perspective(
			(float) Math.toRadians(60.0f),
			(float) engine.getWindow().getWidth() / engine.getWindow().getHeight(),
			0.01f,
			1000.0f));
		shader.setUniform("modelViewMatrix", new Matrix4f()
			.translate(0.0f, 0.0f, -5.0f)
			.rotate((float) Math.toRadians(rotationX), 1.0f, 0.0f, 0.0f)
			.rotate((float) Math.toRadians(rotationY), 0.0f, 1.0f, 0.0f)
			.scale(1.2f));
		shader.setUniform("textureUnit", 0);
		object.render();
		shader.unbind();
	}

}
