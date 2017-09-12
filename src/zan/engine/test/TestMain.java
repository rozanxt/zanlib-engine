package zan.engine.test;

import zan.engine.core.Engine;
import zan.engine.core.Input;
import zan.engine.core.Module;
import zan.engine.core.Window;

public class TestMain {

	public static void main(String[] args) {
		Engine engine = new Engine(60, 50);
		Window.Attributes attr = new Window.Attributes(640, 480);
		attr.title = "Test";
		attr.icon = "res/ico/icon.png";
		Window window = new Window(attr);
		Input input = new Input(window);
		Module module = new TestModule(engine);
		engine.setWindow(window);
		engine.setInput(input);
		engine.setModule(module);
		engine.start();
	}

}
