package zan.engine.test;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import zan.engine.core.Engine;
import zan.engine.core.Module;
import zan.engine.gfx.FontData;
import zan.engine.gfx.ShaderProgram;
import zan.engine.gfx.TextObject;
import zan.engine.gfx.TextureData;
import zan.engine.gfx.TextureObject;
import zan.engine.gfx.VertexData;
import zan.engine.gfx.VertexObject;
import zan.engine.util.OBJLoader;

public class TestModule implements Module {

	private Engine engine;

	private ShaderProgram shader3d;
	private ShaderProgram shader2d;

	private VertexData mesh;

	private TextureData texture;

	private FontData[] font;

	private VertexObject cube;
	private VertexObject text;

	private float rotationX = 30.0f;
	private float rotationY = 60.0f;

	public TestModule(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void init() {
		String wallOfText = "Randomness is the lack of pattern or predictability in events. [1] "
			+ "A random sequence of events, symbols or steps has no order and does not "
			+ "follow an intelligible pattern or combination. Individual random events "
			+ "are by definition unpredictable, but in many cases the frequency of "
			+ "different outcomes over a large number of events (or trials) is predictable. "
			+ "For example, when throwing two dice, the outcome of any particular roll is "
			+ "unpredictable, but a sum of 7 will occur twice as often as 4. In this view, "
			+ "randomness is a measure of uncertainty of an outcome, rather than haphazardness, "
			+ "and applies to concepts of chance, probability, and information entropy. "
			+ "The fields of mathematics, probability, and statistics use formal definitions "
			+ "of randomness. In statistics, a random variable is an assignment of a numerical "
			+ "value to each possible outcome of an event space. This association facilitates "
			+ "the identification and the calculation of probabilities of the events. "
			+ " Random variables can appear in random sequences. A random process is a sequence "
			+ "of random variables whose outcomes do not follow a deterministic pattern, but "
			+ "follow an evolution described by probability distributions. \n";

		glfwSetKeyCallback(engine.getWindow().getHandle(), (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				engine.getWindow().close();
			}
			if (key == GLFW_KEY_F11 && action == GLFW_RELEASE) {
				engine.getWindow().setFullScreen(!engine.getWindow().isFullScreen());
			}
			if (key == GLFW_KEY_0 && action == GLFW_RELEASE) {
				text.delete();
				text = TextObject.create(wallOfText, font[0], 550);
			}
			if (key == GLFW_KEY_1 && action == GLFW_RELEASE) {
				text.delete();
				text = TextObject.create(wallOfText, font[1], 550);
			}
			if (key == GLFW_KEY_2 && action == GLFW_RELEASE) {
				text.delete();
				text = TextObject.create(wallOfText, font[2], 550);
			}
			if (key == GLFW_KEY_3 && action == GLFW_RELEASE) {
				text.delete();
				text = TextObject.create(wallOfText, font[3], 550);
			}
		});

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		shader3d = ShaderProgram.loadFromFile("res/shd/shader3d.vs", "res/shd/shader3d.fs");
		shader3d.addUniform("projectionMatrix");
		shader3d.addUniform("modelViewMatrix");
		shader3d.addUniform("textureUnit");
		shader3d.addUniform("tintColor");

		shader2d = ShaderProgram.loadFromFile("res/shd/shader2d.vs", "res/shd/shader2d.fs");
		shader2d.addUniform("projectionMatrix");
		shader2d.addUniform("modelViewMatrix");
		shader2d.addUniform("textureUnit");
		shader3d.addUniform("tintColor");

		mesh = OBJLoader.loadFromFile("res/obj/block.obj");
		texture = TextureData.loadFromFile("res/img/grassblock.png");
		cube = new TextureObject(mesh, texture);

		font = new FontData[4];
		font[0] = FontData.create(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		font[1] = FontData.create(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		font[2] = FontData.create(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
		font[3] = FontData.create(new Font(Font.SANS_SERIF, Font.BOLD + Font.ITALIC, 20));
		text = TextObject.create(wallOfText, font[0], 550);
	}

	@Override
	public void exit() {
		shader3d.delete();
		shader2d.delete();
		mesh.delete();
		texture.delete();
		for (int i = 0; i < font.length; i++) {
			font[i].delete();
		}
		cube.delete();
		text.delete();
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
		int width = engine.getWindow().getWidth();
		int height = engine.getWindow().getHeight();

		glClearColor(0.0f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, width, height);

		shader3d.bind();
		shader3d.setUniform("projectionMatrix", new Matrix4f().perspective((float) Math.toRadians(60.0f), (float) width / height, 0.01f, 1000.0f));
		shader3d.setUniform("modelViewMatrix", new Matrix4f()
			.translate(0.0f, 0.0f, -5.0f)
			.rotate((float) Math.toRadians(rotationX), 1.0f, 0.0f, 0.0f)
			.rotate((float) Math.toRadians(rotationY), 0.0f, 1.0f, 0.0f)
			.scale(1.0f));
		shader3d.setUniform("textureUnit", 0);
		shader3d.setUniform("tintColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		cube.render();
		shader3d.unbind();

		shader2d.bind();
		shader2d.setUniform("projectionMatrix", new Matrix4f().ortho2D(-width / 2, width / 2, -height / 2, height / 2));
		shader2d.setUniform("modelViewMatrix", new Matrix4f().translate(-310.0f, 210.0f, 0.0f));
		shader3d.setUniform("tintColor", new Vector4f(1.0f, 1.0f, 1.0f, 0.1f));
		shader2d.setUniform("textureUnit", 0);
		text.render();
		shader2d.unbind();
	}

}
