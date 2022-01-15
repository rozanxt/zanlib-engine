package zan.lib.sbx.scene;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;
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

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import zan.lib.app.Engine;
import zan.lib.app.Input;
import zan.lib.app.Scene;
import zan.lib.app.Window;
import zan.lib.gfx.camera.Camera;
import zan.lib.gfx.camera.ScreenCamera;
import zan.lib.gfx.model.InstancedModelMesh;
import zan.lib.gfx.model.ModelLoader;
import zan.lib.gfx.shader.Shader;
import zan.lib.gfx.sprite.InstancedSpriteMesh;
import zan.lib.gfx.sprite.SpriteMesh;
import zan.lib.gfx.texture.Texture;
import zan.lib.sbx.camera.InteractiveCamera;

public class Sandbox implements Scene {

	private Engine engine;

	private Map<String, Shader> shaders;

	private Camera camera;
	private Camera screen;

	private Texture sheet;

	private InstancedModelMesh mesh;
	private InstancedSpriteMesh sprite;

	public Sandbox(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void init() {
		Window window = engine.getWindow();
		Input input = engine.getInput();

		shaders = new HashMap<>();
		shaders.put("plain", Shader.loadFromFile("res/shd/plain.vert", "res/shd/plain.frag"));
		shaders.put("model", Shader.loadFromFile("res/shd/model.vert", "res/shd/model.frag"));
		shaders.put("sprite", Shader.loadFromFile("res/shd/sprite.vert", "res/shd/sprite.frag"));
		shaders.put("text", Shader.loadFromFile("res/shd/text.vert", "res/shd/text.frag"));

		camera = new InteractiveCamera(window, input);
		screen = new ScreenCamera(window);

		sheet = Texture.loadFromFile("res/img/animation.png");

		mesh = new InstancedModelMesh(ModelLoader.loadFromFile("res/obj/toycar.obj"));
		sprite = new InstancedSpriteMesh(new SpriteMesh(sheet, 12, 1));
	}

	@Override
	public void exit() {
		for (Shader shader : shaders.values()) shader.delete();
		sheet.delete();
		mesh.delete();
		sprite.delete();
	}

	@Override
	public void update(float theta) {
		Window window = engine.getWindow();
		Input input = engine.getInput();

		if (input.isKeyReleased(GLFW_KEY_F11)) {
			window.setFullScreen(!window.isFullScreen());
		} else if (input.isKeyReleased(GLFW_KEY_F12)) {
			window.close();
		}

		camera.update(theta);
		screen.update(theta);
	}

	@Override
	public void render(float theta) {
		Shader shader;

		Window window = engine.getWindow();

		glViewport(0, 0, window.getWidth(), window.getHeight());

		camera.capture(theta);
		screen.capture(theta);

		mesh.add(new InstancedModelMesh.Instance(new Matrix4f(), new Vector4f(0.0f, 0.5f, 0.8f, 1.0f), 1));
		mesh.add(new InstancedModelMesh.Instance(new Matrix4f().translate(1.5f, 3.0f, -0.5f).rotateX(2.0f).rotateY(1.0f), new Vector4f(1.0f, 0.5f, 0.0f, 1.0f), 2));
		mesh.process();

		for (int i = 0; i < 6; i++) {
			sprite.add(new InstancedSpriteMesh.Instance(120.0f * i + 30.0f, 0.0f, 2 * i));
		}
		sprite.process();

		glClearColor(0.0f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		shader = shaders.get("model");
		shader.bind();
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		mesh.bind();
		mesh.draw();
		mesh.unbind();
		shader.unbind();

		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);

		shader = shaders.get("sprite");
		shader.bind();
		shader.setUniform("projectionMatrix", screen.getProjectionMatrix());
		shader.setUniform("viewMatrix", screen.getViewMatrix());
		sprite.bind();
		sprite.draw();
		sprite.unbind();
		shader.unbind();

		glDisable(GL_BLEND);
	}

	public static void main(String[] args) {
		Engine engine = new Engine(60, 20);
		Window.Attributes attrib = new Window.Attributes(800, 600);
		attrib.title = "Sandbox";
		Window window = new Window(attrib);
		Input input = new Input(window);
		Scene scene = new Sandbox(engine);
		engine.setWindow(window);
		engine.setInput(input);
		engine.setScene(scene);
		engine.start();
	}

}
