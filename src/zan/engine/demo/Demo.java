package zan.engine.demo;

import java.awt.Font;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import zan.engine.core.Engine;
import zan.engine.core.Input;
import zan.engine.core.Module;
import zan.engine.core.Window;
import zan.engine.gfx.Mesh;
import zan.engine.gfx.OBJLoader;
import zan.engine.gfx.Shader;
import zan.engine.gfx.SpriteItem;
import zan.engine.gfx.TextFont;
import zan.engine.gfx.TextItem;
import zan.engine.gfx.Texture;
import zan.engine.sfx.SoundData;
import zan.engine.sfx.SoundSource;
import zan.engine.sfx.SoundSystem;

public class Demo implements Module {

	private Engine engine;

	private Shader shader;
	private Shader noshader;

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

	private TextFont font;
	private TextItem text;

	private SoundData sound;
	private SoundSource source;

	public Demo(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void init() {
		shader = Shader.loadFromFile("res/shd/shader.vs", "res/shd/shader.fs");
		shader.addUniform("projectionMatrix");
		shader.addUniform("modelViewMatrix");
		shader.addUniform("uniformColor");
		shader.addUniform("enableTexture");
		shader.addUniform("textureUnit");

		noshader = Shader.loadDefault();
		noshader.addUniform("projectionMatrix");
		noshader.addUniform("modelViewMatrix");
		noshader.addUniform("uniformColor");
		noshader.addUniform("enableTexture");
		noshader.addUniform("textureUnit");

		texture = Texture.loadFromFile("res/img/grassblock.png");
		cube = OBJLoader.loadFromFile("res/obj/block.obj");
		cubeShader = shader;
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

		font = new TextFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		text = new TextItem("ZanEngine", font, 550.0f);

		SoundSystem.init();
		sound = SoundData.loadFromFile("res/snd/humoresky.ogg");
		source = new SoundSource(sound);
	}

	@Override
	public void exit() {
		shader.delete();
		noshader.delete();
		texture.delete();
		cube.delete();
		sheet.delete();
		sprite.delete();
		font.delete();
		text.delete();
		sound.delete();
		source.delete();
		SoundSystem.exit();
	}

	@Override
	public void update() {
		Input input = engine.getInput();

		if (input.isKeyReleased(GLFW.GLFW_KEY_ESCAPE)) {
			engine.getWindow().close();
		} else if (input.isKeyReleased(GLFW.GLFW_KEY_F2)) {
			if (cubeShader == shader) {
				cubeShader = noshader;
			} else {
				cubeShader = shader;
			}
		} else if (input.isKeyReleased(GLFW.GLFW_KEY_F3)) {
			cubeEnableTexture = !cubeEnableTexture;
			if (cubeEnableTexture) {
				cubeUniformColor.set(1.0f, 1.0f, 1.0f, 1.0f);
			} else {
				cubeUniformColor.set(1.0f, 0.5f, 0.0f, 1.0f);
			}
		} else if (input.isKeyReleased(GLFW.GLFW_KEY_F4)) {
			if (source.isPlaying()) {
				source.pause();
			} else {
				source.play();
			}
		} else if (input.isKeyReleased(GLFW.GLFW_KEY_F5)) {
			source.stop();
		} else if (input.isKeyReleased(GLFW.GLFW_KEY_F11)) {
			engine.getWindow().setFullScreen(!engine.getWindow().isFullScreen());
		}

		if (input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
			spriteX -= 3.1f;
			spriteCounter += 1.0f;
			spriteFlip = true;
		} else if (input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
			spriteX += 3.1f;
			spriteCounter += 1.0f;
			spriteFlip = false;
		}
		if (spriteCounter > 2.0f) {
			spriteCounter = 0.0f;
			spriteFrame++;
			if (spriteFrame >= sprite.getCols()) {
				spriteFrame = 0;
			}
		}

		if (input.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE) || input.isKeyRepeated(GLFW.GLFW_KEY_BACKSPACE)) {
			text.setText(text.getText().isEmpty() ? text.getText() : text.getText().substring(0, text.getText().length() - 1));
			text.update();
		} else if (input.isKeyPressed(GLFW.GLFW_KEY_ENTER) || input.isKeyRepeated(GLFW.GLFW_KEY_ENTER)) {
			text.setText(text.getText() + "\n");
			text.update();
		} else if (input.isKeyPressed(GLFW.GLFW_KEY_TAB) || input.isKeyRepeated(GLFW.GLFW_KEY_TAB)) {
			text.setText(text.getText() + "    ");
			text.update();
		}
		if (input.hasCharEvent()) {
			while (input.hasCharEvent()) {
				char ch = input.getCharEvent();
				text.setText(text.getText() + ch);
				text.update();
			}
		}

		if (input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
			cubeRotationX += 0.5f * input.getMouseDeltaY();
			cubeRotationY += 0.5f * input.getMouseDeltaX();
		}
		cubeTranslationZ += 0.5f * input.getMouseScrollY();
	}

	@Override
	public void render() {
		float width = engine.getWindow().getWidth();
		float height = engine.getWindow().getHeight();

		GL11.glClearColor(0.0f, 0.1f, 0.1f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glViewport(0, 0, (int) width, (int) height);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);

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

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		noshader.bind();
		noshader.setUniform("projectionMatrix", new Matrix4f()
			.setOrtho(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, -1.0f, 1.0f));
		if (spriteFlip) {
			noshader.setUniform("modelViewMatrix", new Matrix4f()
				.translate(spriteX, -height / 2.0f, 0.0f)
				.translate(sprite.getWidth() / 2.0f, 0.0f, 0.0f)
				.reflect(1.0f, 0.0f, 0.0f, 0.0f)
				.translate(-sprite.getWidth() / 2.0f, 0.0f, 0.0f));
		} else {
			noshader.setUniform("modelViewMatrix", new Matrix4f()
				.translate(spriteX, -height / 2.0f, 0.0f));
		}
		noshader.setUniform("uniformColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		noshader.setUniform("enableTexture", true);
		noshader.setUniform("textureUnit", 0);
		sprite.render(spriteFrame);
		noshader.unbind();

		noshader.bind();
		noshader.setUniform("projectionMatrix", new Matrix4f()
			.setOrtho(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f, -1.0f, 1.0f));
		noshader.setUniform("modelViewMatrix", new Matrix4f()
			.translate((int) -(text.getWidth() / 2.0f), (int) (height / 2.0f - 30.0f), 0.0f));
		noshader.setUniform("uniformColor", new Vector4f(0.0f, 0.5f, 0.8f, 1.0f));
		noshader.setUniform("enableTexture", true);
		noshader.setUniform("textureUnit", 0);
		text.render();
		noshader.unbind();

		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void main(String[] args) {
		Engine engine = new Engine(60, 50);
		Window.Attributes attr = new Window.Attributes(640, 480);
		attr.title = "ZanEngine Demo";
		attr.icon = "res/ico/icon.png";
		Window window = new Window(attr);
		Input input = new Input(window);
		Module module = new Demo(engine);
		engine.setWindow(window);
		engine.setInput(input);
		engine.setModule(module);
		engine.start();
	}

}
