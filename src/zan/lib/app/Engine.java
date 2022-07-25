package zan.lib.app;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import org.lwjgl.glfw.GLFWErrorCallback;

public class Engine implements Runnable {

	private static final long NANOS_PER_SECOND = 1_000_000_000L;

	private Window window;
	private Input input;
	private Scene scene;

	private int targetFPS;
	private int targetUPS;

	private int currentFPS;
	private int currentUPS;

	public Engine(int targetFPS, int targetUPS) {
		this.targetFPS = targetFPS;
		this.targetUPS = targetUPS;
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		init();
		loop();
		exit();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW!");
		}

		window.init();
		input.init();
		scene.init();
	}

	private void loop() {
		long prevTime = System.nanoTime();
		long nextTime = prevTime;
		long updateTime = 0L;

		long counter = 0L;
		int countFPS = 0;
		int countUPS = 0;

		while (!window.shouldClose()) {
			long currentTime = System.nanoTime();
			long elapsedTime = currentTime - prevTime;

			long deltaFPS = NANOS_PER_SECOND / targetFPS;
			long deltaUPS = NANOS_PER_SECOND / targetUPS;

			prevTime = currentTime;
			nextTime += deltaFPS;
			updateTime += elapsedTime;

			while (updateTime >= deltaUPS) {
				scene.update(1.0f / targetUPS);
				input.clear();
				updateTime -= deltaUPS;
				countUPS++;
			}

			scene.render((float) updateTime / (float) deltaUPS);
			window.refresh();
			countFPS++;

			counter += elapsedTime;
			if (counter >= NANOS_PER_SECOND) {
				currentFPS = countFPS;
				currentUPS = countUPS;
				countFPS = 0;
				countUPS = 0;
				counter -= NANOS_PER_SECOND;
			}

			if (!window.isVSync()) {
				while (System.nanoTime() < nextTime) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {

					}
				}
			}
		}
	}

	private void exit() {
		scene.exit();
		input.exit();
		window.exit();

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public Window getWindow() {
		return window;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public Input getInput() {
		return input;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	public void setTargetFPS(int targetFPS) {
		this.targetFPS = targetFPS;
	}

	public int getTargetFPS() {
		return targetFPS;
	}

	public void setTargetUPS(int targetUPS) {
		this.targetUPS = targetUPS;
	}

	public int getTargetUPS() {
		return targetUPS;
	}

	public int getCurrentFPS() {
		return currentFPS;
	}

	public int getCurrentUPS() {
		return currentUPS;
	}

}
