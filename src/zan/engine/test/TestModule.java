package zan.engine.test;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import zan.engine.core.Engine;
import zan.engine.core.Input;
import zan.engine.core.Module;
import zan.engine.gfx.Shader;
import zan.engine.gfx.item.SpriteItem;
import zan.engine.gfx.item.TextItem;
import zan.engine.gfx.mesh.Mesh;
import zan.engine.gfx.texture.FontTexture;
import zan.engine.gfx.texture.SpriteTexture;
import zan.engine.gfx.texture.Texture;
import zan.engine.sfx.SoundData;
import zan.engine.sfx.SoundSystem;
import zan.engine.sfx.SoundSource;
import zan.engine.util.OBJLoader;

public class TestModule implements Module {

	private Engine engine;

	private Shader shader;
	private Shader noshader;

	private Texture texture;
	private Mesh mesh;

	private FontTexture[] font;
	private TextItem text;

	private SpriteTexture sheet;
	private SpriteItem sprite;

	private SoundData music;
	private SoundSource source;

	private float rotationX = 30.0f;
	private float rotationY = 60.0f;
	private float translationZ = -5.0f;

	private int ticks = 0;
	private int frame = 0;

	public TestModule(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void init() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		shader = Shader.loadFromFile("res/shd/shader.vs", "res/shd/shader.fs");
		shader.addUniform("projectionMatrix");
		shader.addUniform("modelViewMatrix");
		shader.addUniform("useTexture");
		shader.addUniform("textureUnit");
		shader.addUniform("tintColor");

		noshader = Shader.loadFromFile("res/shd/noshader.vs", "res/shd/noshader.fs");
		noshader.addUniform("projectionMatrix");
		noshader.addUniform("modelViewMatrix");
		noshader.addUniform("useTexture");
		noshader.addUniform("textureUnit");
		noshader.addUniform("tintColor");

		texture = Texture.loadFromFile("res/img/grassblock.png");
		mesh = OBJLoader.loadFromFile("res/obj/block.obj");

		font = new FontTexture[4];
		font[0] = FontTexture.load(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		font[1] = FontTexture.load(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		font[2] = FontTexture.load(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
		font[3] = FontTexture.load(new Font(Font.SANS_SERIF, Font.BOLD + Font.ITALIC, 20));
		text = new TextItem("ZanEngine", font[0]);

		sheet = SpriteTexture.loadFromFile("res/img/animation.png", 1, 12);
		sprite = new SpriteItem(sheet);
		sprite.setFrame(frame);

		SoundSystem.init();
		music = SoundData.loadFromFile("res/snd/humoresky.ogg");
		source = new SoundSource(music);
	}

	@Override
	public void exit() {
		shader.delete();
		noshader.delete();
		texture.delete();
		mesh.delete();
		for (int i = 0; i < font.length; i++) {
			font[i].delete();
		}
		text.delete();
		sheet.delete();
		sprite.delete();
		source.delete();
		music.delete();
		SoundSystem.exit();
	}

	@Override
	public void update() {
		Input input = engine.getInput();

		if (input.isKeyReleased(GLFW_KEY_ESCAPE)) {
			engine.getWindow().close();
		}
		if (input.isKeyReleased(GLFW_KEY_F11)) {
			engine.getWindow().setFullScreen(!engine.getWindow().isFullScreen());
		}

		if (input.isKeyReleased(GLFW_KEY_ENTER)) {
			if (source.isInitial()) {
				source.play();
			} else if (source.isPlaying()) {
				source.pause();
			} else if (source.isPaused()) {
				source.resume();
			} else if (source.isStopped()) {
				source.rewind();
			}
		}
		if (input.isKeyReleased(GLFW_KEY_BACKSPACE)) {
			source.stop();
		}

		if (input.isKeyPressed(GLFW_KEY_0)) {
			text.setFont(font[0]);
			text.update();
		}
		if (input.isKeyPressed(GLFW_KEY_1)) {
			text.setFont(font[1]);
			text.update();
		}
		if (input.isKeyPressed(GLFW_KEY_2)) {
			text.setFont(font[2]);
			text.update();
		}
		if (input.isKeyPressed(GLFW_KEY_3)) {
			text.setFont(font[3]);
			text.update();
		}

		if (input.isMouseDown(GLFW_MOUSE_BUTTON_1)) {
			rotationX += 0.5f * input.getMouseDeltaY();
			rotationY += 0.5f * input.getMouseDeltaX();
		}

		translationZ += 0.5f * input.getMouseScrollY();

		ticks++;
		if (ticks >= 3) {
			frame++;
			ticks = 0;
		}
		if (frame >= 12) {
			frame = 0;
		}
		sprite.setFrame(frame);
	}

	@Override
	public void render() {
		int width = engine.getWindow().getWidth();
		int height = engine.getWindow().getHeight();

		glClearColor(0.0f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, width, height);

		shader.bind();
		shader.setUniform("projectionMatrix", new Matrix4f().perspective((float) Math.toRadians(60.0f), (float) width / height, 0.01f, 1000.0f));
		shader.setUniform("modelViewMatrix", new Matrix4f()
			.translate(0.0f, 0.0f, translationZ)
			.rotate((float) Math.toRadians(rotationX), 1.0f, 0.0f, 0.0f)
			.rotate((float) Math.toRadians(rotationY), 0.0f, 1.0f, 0.0f)
			.scale(1.0f));
		shader.setUniform("useTexture", 1);
		shader.setUniform("textureUnit", 0);
		shader.setUniform("tintColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		texture.bind();
		mesh.bind();
		mesh.draw();
		mesh.unbind();
		texture.unbind();
		shader.unbind();

		noshader.bind();
		noshader.setUniform("projectionMatrix", new Matrix4f().ortho2D(-width / 2.0f, width / 2.0f, 0.0f, height));
		noshader.setUniform("modelViewMatrix", new Matrix4f().translate(-50.0f, height - 30.0f, 0.0f));
		noshader.setUniform("useTexture", 1);
		noshader.setUniform("textureUnit", 0);
		noshader.setUniform("tintColor", new Vector4f(0.0f, 0.5f, 0.8f, 0.5f));
		text.render();
		noshader.setUniform("modelViewMatrix", new Matrix4f().translate(width * 1.0f / 4.0f, height / 2.0f - 64.0f, 0.0f));
		noshader.setUniform("tintColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		sprite.render();
		noshader.unbind();
	}

}
