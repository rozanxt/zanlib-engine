package zan.engine.test;

import zan.engine.core.Engine;
import zan.engine.core.Window;

public class TestMain {

	public static void main(String[] args) {
		Window.Attributes attr = new Window.Attributes();
		attr.title = "Test";
		attr.icon = "res/ico/icon.png";
		Engine engine = new Engine();
		engine.setWindow(new Window(attr));
		engine.setModule(new TestModule(engine));
		engine.start();
	}

}
