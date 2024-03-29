package zan.lib.tst.scn;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;

import zan.lib.app.Application;
import zan.lib.app.Engine;
import zan.lib.app.Input;
import zan.lib.app.Scene;
import zan.lib.app.Window;

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
	public void update(float delta) {
		Window window = engine.getWindow();
		Input input = engine.getInput();

		if (input.isKeyReleased(GLFW_KEY_F11)) {
			window.setFullScreen(!window.isFullScreen());
		} else if (input.isKeyReleased(GLFW_KEY_F12)) {
			window.close();
		}
	}

	@Override
	public void render(float theta) {

	}

	public static void main(String[] args) {
		Application application = new Application();
		application.init();

		Engine engine = new Engine(60, 20);
		Window.Attributes attrib = new Window.Attributes(640, 480);
		attrib.title = "Barebone";
		Window window = new Window(attrib);
		Input input = new Input(window);
		Scene scene = new Barebone(engine);
		engine.setWindow(window);
		engine.setInput(input);
		engine.setScene(scene);
		engine.run();

		application.exit();
	}

}
