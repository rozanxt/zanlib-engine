package zan.lib.demo;

import org.lwjgl.glfw.GLFW;

import zan.lib.core.Engine;
import zan.lib.core.Input;
import zan.lib.core.Scene;
import zan.lib.core.Window;

public class Barebone implements Scene {

	private Engine engine;

	public Barebone(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void init() {

	}

	@Override
	public void exit() {

	}

	@Override
	public void update() {
		Window window = engine.getWindow();
		Input input = engine.getInput();
		if (input.isKeyReleased(GLFW.GLFW_KEY_ESCAPE)) {
			window.close();
		} else if (input.isKeyReleased(GLFW.GLFW_KEY_F11)) {
			window.setFullScreen(!window.isFullScreen());
		}
	}

	@Override
	public void render() {

	}

	public static void main(String[] args) {
		Engine engine = new Engine(60, 50);
		Window.Attributes attr = new Window.Attributes(640, 480);
		attr.title = "Barebone";
		Window window = new Window(attr);
		Input input = new Input(window);
		Scene scene = new Barebone(engine);
		engine.setWindow(window);
		engine.setInput(input);
		engine.setScene(scene);
		engine.start();
	}

}
