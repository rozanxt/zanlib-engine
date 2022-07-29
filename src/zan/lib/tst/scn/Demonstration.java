package zan.lib.tst.scn;

import static java.awt.Font.PLAIN;
import static java.awt.Font.SANS_SERIF;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
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
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import zan.lib.app.Application;
import zan.lib.app.Engine;
import zan.lib.app.Input;
import zan.lib.app.Scene;
import zan.lib.app.Window;
import zan.lib.gfx.cam.Camera;
import zan.lib.gfx.cam.ScreenCamera;
import zan.lib.gfx.mdl.ModelLoader;
import zan.lib.gfx.msh.Mesh;
import zan.lib.gfx.shd.Shader;
import zan.lib.gfx.spr.SpriteMesh;
import zan.lib.gfx.tex.Texture;
import zan.lib.gfx.txt.TextFont;
import zan.lib.gfx.txt.TextMesh;
import zan.lib.sfx.SoundData;
import zan.lib.sfx.SoundSource;
import zan.lib.sfx.SoundSystem;
import zan.lib.tst.cam.DemonstrationCamera;

public class Demonstration implements Scene {

	private Engine engine;

	private Map<String, Shader> shaders;

	private Camera camera;
	private Camera screen;

	private Texture texture;
	private Mesh cube;
	private Shader cubeShader;
	private Vector4f cubeUniformColor;
	private boolean cubeEnableTexture;

	private Texture sheet;
	private SpriteMesh sprite;
	private float spriteX;
	private float spriteCounter;
	private int spriteFrame;
	private boolean spriteFlip;

	private String content;

	private TextFont font;
	private TextMesh text;
	private TextMesh help;
	private TextMesh ufps;

	private boolean showHelp;

	private SoundData sound;
	private SoundSource source;

	public Demonstration(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void init() {
		Window window = engine.getWindow();
		Input input = engine.getInput();

		shaders = new HashMap<>();
		shaders.put("default", Shader.loadFromFile("res/shd/default.vert", "res/shd/default.frag"));
		shaders.put("standard", Shader.loadFromFile("res/shd/standard.vert", "res/shd/standard.frag"));
		shaders.put("text", Shader.loadFromFile("res/shd/text.vert", "res/shd/text.frag"));

		camera = new DemonstrationCamera(window, input);
		screen = new ScreenCamera(window);

		texture = Texture.loadFromFile("res/img/block.png");
		cube = ModelLoader.loadFromFile("res/obj/block.obj");
		cubeShader = shaders.get("standard");
		cubeUniformColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		cubeEnableTexture = true;

		sheet = Texture.loadFromFile("res/img/animation.png");
		sprite = new SpriteMesh(sheet, 12, 1);
		spriteX = 0.0f;
		spriteFrame = 4;
		spriteFlip = false;

		content = "ZanLibEngine";

		font = new TextFont(new Font(SANS_SERIF, PLAIN, 20));
		text = new TextMesh(font);
		text.setMaxWidth(450.0f);
		text.build(content);
		help = new TextMesh(font);
		help.build("F1: Help");
		ufps = new TextMesh(font);

		showHelp = false;

		SoundSystem.init();
		sound = SoundData.loadFromFile("res/snd/humoresky.ogg");
		source = new SoundSource(sound);
	}

	@Override
	public void exit() {
		for (Shader shader : shaders.values()) shader.delete();
		texture.delete();
		cube.delete();
		sheet.delete();
		sprite.delete();
		font.delete();
		text.delete();
		help.delete();
		ufps.delete();
		sound.delete();
		source.delete();
		SoundSystem.exit();
	}

	@Override
	public void update(float delta) {
		Window window = engine.getWindow();
		Input input = engine.getInput();

		if (input.isKeyReleased(GLFW_KEY_F1)) {
			if (showHelp) {
				help.build("F1: Help");
			} else {
				help.build("F1: Help\n" +
					"F2: Toggle shading\n" +
					"F3: Toggle texture\n" +
					"F4: Toggle music\n" +
					"F5: Rewind music\n" +
					"F11: Toggle fullscreen\n" +
					"F12: Exit application\n" +
					"Left mouse button drag: Rotate cube\n" +
					"Mouse scroll: Zoom in / out\n" +
					"Left / right arrow key: Move sprite\n");
			}
			showHelp = !showHelp;
		} else if (input.isKeyReleased(GLFW_KEY_F2)) {
			if (cubeShader == shaders.get("standard")) {
				cubeShader = shaders.get("default");
			} else {
				cubeShader = shaders.get("standard");
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
		} else if (input.isKeyReleased(GLFW_KEY_F12)) {
			window.close();
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
		while (input.hasCharEvent()) {
			char ch = input.getCharEvent();
			if (text.getOffset() + text.getFont().getCharInfo(ch).w < text.getMaxWidth()) {
				content += ch;
			}
		}
		if (text.getText() != content) {
			text.build(content);
		}

		ufps.build(String.format("FPS: %d\nUPS: %d", engine.getCurrentFPS(), engine.getCurrentUPS()));

		camera.update(delta);
		screen.update(delta);
	}

	@Override
	public void render(float theta) {
		Shader shader;

		Window window = engine.getWindow();

		int width = window.getWidth();
		int height = window.getHeight();

		glViewport(0, 0, width, height);

		camera.capture(theta);
		screen.capture(theta);

		glClearColor(0.0f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		shader = cubeShader;
		shader.bind();
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("modelViewMatrix", camera.getViewMatrix());
		shader.setUniform("uniformColor", cubeUniformColor);
		shader.setUniform("enableTexture", cubeEnableTexture);
		shader.setUniform("textureUnit", 0);
		texture.bind();
		cube.bind();
		cube.draw();
		cube.unbind();
		texture.unbind();
		shader.unbind();

		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		shader = shaders.get("default");
		shader.bind();
		shader.setUniform("projectionMatrix", screen.getProjectionMatrix());
		if (spriteFlip) {
			shader.setUniform("modelViewMatrix", new Matrix4f()
				.translate(spriteX, 0.0f, 0.0f)
				.translate(sprite.getWidth() / 2.0f, 0.0f, 0.0f)
				.reflect(1.0f, 0.0f, 0.0f, 0.0f)
				.translate(-sprite.getWidth() / 2.0f, 0.0f, 0.0f));
		} else {
			shader.setUniform("modelViewMatrix", new Matrix4f()
				.translate(spriteX, 0.0f, 0.0f));
		}
		shader.setUniform("uniformColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		shader.setUniform("enableTexture", true);
		shader.setUniform("textureUnit", 0);
		sprite.bind();
		sprite.draw(spriteFrame);
		sprite.unbind();
		shader.unbind();

		shader = shaders.get("text");
		shader.bind();

		shader.setUniform("projectionMatrix", screen.getProjectionMatrix());
		shader.setUniform("modelViewMatrix", new Matrix4f()
			.translate((int) (width / 2 - text.getWidth() / 2), height - 30, 0));
		shader.setUniform("uniformColor", new Vector4f(0.0f, 0.5f, 0.8f, 1.0f));
		shader.setUniform("textureUnit", 0);
		text.bind();
		text.draw();
		text.unbind();

		if (showHelp) {
			shader.setUniform("uniformColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		} else {
			shader.setUniform("uniformColor", new Vector4f(1.0f, 1.0f, 1.0f, 0.5f));
		}

		shader.setUniform("modelViewMatrix", new Matrix4f()
			.translate(10, height - 30, 0));
		help.bind();
		help.draw();
		help.unbind();

		shader.setUniform("modelViewMatrix", new Matrix4f()
			.translate((int) (width - ufps.getWidth() - 10), height - 30, 0));
		ufps.bind();
		ufps.draw();
		ufps.unbind();

		shader.unbind();

		glDisable(GL_BLEND);
	}

	public static void main(String[] args) {
		Application application = new Application();
		application.init();

		Engine engine = new Engine(60, 50);
		Window.Attributes attrib = new Window.Attributes(800, 600);
		attrib.title = "Demonstration";
		attrib.icon = "res/ico/icon.png";
		Window window = new Window(attrib);
		Input input = new Input(window);
		Scene scene = new Demonstration(engine);
		engine.setWindow(window);
		engine.setInput(input);
		engine.setScene(scene);
		engine.run();

		application.exit();
	}

}
