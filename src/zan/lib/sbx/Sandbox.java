package zan.lib.sbx;

import static java.awt.Font.PLAIN;
import static java.awt.Font.SANS_SERIF;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Font;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import zan.lib.app.Engine;
import zan.lib.app.Input;
import zan.lib.app.Scene;
import zan.lib.app.Window;
import zan.lib.gfx.Mesh;
import zan.lib.gfx.OBJLoader;
import zan.lib.gfx.Shader;
import zan.lib.gfx.SpriteItem;
import zan.lib.gfx.TextFont;
import zan.lib.gfx.TextItem;
import zan.lib.gfx.Texture;
import zan.lib.sfx.SoundData;
import zan.lib.sfx.SoundSource;
import zan.lib.sfx.SoundSystem;

public class Sandbox implements Scene {

	private Engine engine;

	private Shader defShader;
	private Shader stdShader;

	private Texture texture;
	private Mesh cube;
	private Shader cubeShader;
	private Vector4f cubeUniformColor;
	private boolean cubeEnableTexture;
	private float cubeRotationX;
	private float cubeRotationY;
	private float cubeTranslationZ;

	private Texture sheet;
	private SpriteItem sprite;
	private float spriteX;
	private float spriteCounter;
	private int spriteFrame;
	private boolean spriteFlip;

	private String content;

	private TextFont font;
	private TextItem text;
	private TextItem help;
	private boolean showHelp;

	private SoundData sound;
	private SoundSource source;

	public Sandbox(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void init() {
		defShader = Shader.loadFromFile("res/shd/defshader.vs", "res/shd/defshader.fs");
		stdShader = Shader.loadFromFile("res/shd/stdshader.vs", "res/shd/stdshader.fs");

		texture = Texture.loadFromFile("res/img/grassblock.png");
		cube = OBJLoader.loadFromFile("res/obj/cube.obj");
		cubeShader = stdShader;
		cubeUniformColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		cubeEnableTexture = true;
		cubeRotationX = 0.0f;
		cubeRotationY = 0.0f;
		cubeTranslationZ = -5.0f;

		sheet = Texture.loadFromFile("res/img/animation.png");
		sprite = new SpriteItem(sheet, 12, 1);
		spriteX = -65.0f;
		spriteFrame = 4;
		spriteFlip = false;

		content = "ZanLibEngine";
		font = new TextFont(new Font(SANS_SERIF, PLAIN, 20));
		text = new TextItem(content, font);
		help = new TextItem("F1: Help", font);
		showHelp = false;

		SoundSystem.init();
		sound = SoundData.loadFromFile("res/snd/humoresky.ogg");
		source = new SoundSource(sound);
	}

	@Override
	public void exit() {
		defShader.delete();
		stdShader.delete();
		texture.delete();
		cube.delete();
		sheet.delete();
		sprite.delete();
		font.delete();
		text.delete();
		help.delete();
		sound.delete();
		source.delete();
		SoundSystem.exit();
	}

	@Override
	public void update(float theta) {
		Input input = engine.getInput();
		Window window = engine.getWindow();

		if (input.isKeyReleased(GLFW_KEY_ESCAPE)) {
			window.close();
		} else if (input.isKeyReleased(GLFW_KEY_F1)) {
			if (showHelp) {
				help.setText("F1: Help");
			} else {
				help.setText("F1: Help\n" +
					"F2: Toggle shading\n" +
					"F3: Toggle texture\n" +
					"F4: Toggle music\n" +
					"F5: Rewind music\n" +
					"F11: Toggle fullscreen\n" +
					"Left mouse button drag: Rotate cube\n" +
					"Mouse scroll: Zoom in / out\n" +
					"Left / right arrow key: Move sprite\n");
			}
			showHelp = !showHelp;
			help.update();
		} else if (input.isKeyReleased(GLFW_KEY_F2)) {
			if (cubeShader == stdShader) {
				cubeShader = defShader;
			} else {
				cubeShader = stdShader;
			}
		} else if (input.isKeyReleased(GLFW_KEY_F3)) {
			cubeEnableTexture = !cubeEnableTexture;
			if (cubeEnableTexture) {
				cubeUniformColor.set(1.0f, 1.0f, 1.0f, 1.0f);
			} else {
				cubeUniformColor.set(1.0f, 0.5f, 0.0f, 1.0f);
			}
		} else if (input.isKeyReleased(GLFW_KEY_F4)) {
			if (source.isPlaying()) {
				source.pause();
			} else if (source.isPaused()) {
				source.resume();
			} else {
				source.play();
			}
		} else if (input.isKeyReleased(GLFW_KEY_F5)) {
			source.stop();
		} else if (input.isKeyReleased(GLFW_KEY_F11)) {
			window.setFullScreen(!window.isFullScreen());
		}

		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			spriteX -= 3.1f;
			spriteCounter += 1.0f;
			spriteFlip = true;
		} else if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			spriteX += 3.1f;
			spriteCounter += 1.0f;
			spriteFlip = false;
		}
		if (spriteCounter > 2.0f) {
			spriteCounter = 0.0f;
			spriteFrame++;
			if (spriteFrame >= sprite.getColumns()) {
				spriteFrame = 0;
			}
		}

		if (input.isKeyPressed(GLFW_KEY_BACKSPACE) || input.isKeyRepeated(GLFW_KEY_BACKSPACE)) {
			content = content.isEmpty() ? content : content.substring(0, content.length() - 1);
		}
		if (input.isKeyPressed(GLFW_KEY_ENTER) || input.isKeyRepeated(GLFW_KEY_ENTER)) {
			content += "\n";
		}
		if (text.getOffset() < 450.0f) {
			if (input.isKeyPressed(GLFW_KEY_TAB) || input.isKeyRepeated(GLFW_KEY_TAB)) {
				content += "    ";
			}
			while (input.hasCharEvent()) {
				content += input.getCharEvent();
			}
		}
		if (text.getText() != content) {
			text.setText(content);
			text.update();
		}

		if (input.isMouseDown(GLFW_MOUSE_BUTTON_1)) {
			cubeRotationX += 0.5f * input.getMouseDeltaY();
			cubeRotationY += 0.5f * input.getMouseDeltaX();
		}
		cubeTranslationZ += 0.5f * input.getMouseScrollY();
	}

	@Override
	public void render(float theta) {
		Window window = engine.getWindow();
		float width = window.getWidth();
		float height = window.getHeight();

		glViewport(0, 0, (int) width, (int) height);

		glClearColor(0.0f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		cubeShader.bind();
		cubeShader.setUniform("projectionMatrix", new Matrix4f()
			.setPerspective((float) Math.toRadians(60.0f), width / height, 0.01f, 1000.0f));
		cubeShader.setUniform("modelViewMatrix", new Matrix4f()
			.translate(0.0f, 0.0f, cubeTranslationZ)
			.rotate((float) Math.toRadians(cubeRotationX), 1.0f, 0.0f, 0.0f)
			.rotate((float) Math.toRadians(cubeRotationY), 0.0f, 1.0f, 0.0f));
		cubeShader.setUniform("uniformColor", cubeUniformColor);
		cubeShader.setUniform("enableTexture", cubeEnableTexture);
		cubeShader.setUniform("textureUnit", 0);
		texture.bind();
		cube.bind();
		cube.draw();
		cube.unbind();
		texture.unbind();
		cubeShader.unbind();

		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		defShader.bind();
		defShader.setUniform("projectionMatrix", new Matrix4f()
			.setOrtho(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, -1.0f, 1.0f));
		if (spriteFlip) {
			defShader.setUniform("modelViewMatrix", new Matrix4f()
				.translate(spriteX, -height / 2.0f, 0.0f)
				.translate(sprite.getWidth() / 2.0f, 0.0f, 0.0f)
				.reflect(1.0f, 0.0f, 0.0f, 0.0f)
				.translate(-sprite.getWidth() / 2.0f, 0.0f, 0.0f));
		} else {
			defShader.setUniform("modelViewMatrix", new Matrix4f()
				.translate(spriteX, -height / 2.0f, 0.0f));
		}
		defShader.setUniform("uniformColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		defShader.setUniform("enableTexture", true);
		defShader.setUniform("textureUnit", 0);
		sprite.render(spriteFrame);
		defShader.unbind();

		defShader.bind();
		defShader.setUniform("projectionMatrix", new Matrix4f()
			.setOrtho(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, -1.0f, 1.0f));
		defShader.setUniform("modelViewMatrix", new Matrix4f()
			.translate((int) -(text.getWidth() / 2.0f), (int) (height / 2.0f - 30.0f), 0.0f));
		defShader.setUniform("uniformColor", new Vector4f(0.0f, 0.5f, 0.8f, 1.0f));
		defShader.setUniform("enableTexture", true);
		defShader.setUniform("textureUnit", 0);
		text.render();
		defShader.setUniform("modelViewMatrix", new Matrix4f()
			.translate(-width / 2.0f + 10.0f, (int) (height / 2.0f - 30.0f), 0.0f));
		if (showHelp) {
			defShader.setUniform("uniformColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		} else {
			defShader.setUniform("uniformColor", new Vector4f(1.0f, 1.0f, 1.0f, 0.5f));
		}
		help.render();
		defShader.unbind();

		glDisable(GL_BLEND);
	}

	public static void main(String[] args) {
		Engine engine = new Engine(60, 50);
		Window.Attributes attrib = new Window.Attributes(640, 480);
		attrib.title = "Sandbox";
		attrib.icon = "res/ico/icon.png";
		Window window = new Window(attrib);
		Input input = new Input(window);
		Scene scene = new Sandbox(engine);
		engine.setWindow(window);
		engine.setInput(input);
		engine.setScene(scene);
		engine.start();
	}

}
