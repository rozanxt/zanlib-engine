package zan.lib.app;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import org.lwjgl.glfw.GLFWErrorCallback;

public class Application {

	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW!");
		}
	}

	public void exit() {
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

}
