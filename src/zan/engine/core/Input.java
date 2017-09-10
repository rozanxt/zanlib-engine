package zan.engine.core;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

public class Input {

	private static class KeyEvent {
		public int mods = 0;
		public boolean pressed = false;
		public boolean released = false;
		public boolean repeated = false;

		public void clear() {
			mods = 0;
			pressed = false;
			released = false;
			repeated = false;
		}
	}

	private static class MouseEvent {
		public int mods = 0;
		public boolean pressed = false;
		public boolean released = false;

		public void clear() {
			mods = 0;
			pressed = false;
			released = false;
		}
	}

	private final Window window;

	private final KeyEvent[] keyEvents;
	private final MouseEvent[] mouseEvents;
	private final List<Character> charEvents;

	private float mouseX;
	private float mouseY;
	private float mouseDeltaX;
	private float mouseDeltaY;
	private float mouseScrollX;
	private float mouseScrollY;
	private boolean mouseEntered;
	private boolean mouseLeft;

	public Input(Window window) {
		this.window = window;

		keyEvents = new KeyEvent[GLFW_KEY_LAST];
		for (int i = 0; i < GLFW_KEY_LAST; i++) {
			keyEvents[i] = new KeyEvent();
		}
		mouseEvents = new MouseEvent[GLFW_MOUSE_BUTTON_LAST];
		for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
			mouseEvents[i] = new MouseEvent();
		}
		charEvents = new ArrayList<Character>();

		mouseX = 0.0f;
		mouseY = 0.0f;
		mouseDeltaX = 0.0f;
		mouseDeltaY = 0.0f;
		mouseScrollX = 0.0f;
		mouseScrollY = 0.0f;
		mouseEntered = false;
		mouseLeft = false;
	}

	public void clear() {
		for (int i = 0; i < GLFW_KEY_LAST; i++) {
			keyEvents[i].clear();
		}
		for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
			mouseEvents[i].clear();
		}
		charEvents.clear();

		mouseDeltaX = 0.0f;
		mouseDeltaY = 0.0f;
		mouseScrollX = 0.0f;
		mouseScrollY = 0.0f;
		mouseEntered = false;
		mouseLeft = false;
	}

	public void init() {
		glfwSetKeyCallback(window.getHandle(), (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_UNKNOWN) return;
			KeyEvent keyEvent = keyEvents[key];
			keyEvent.mods = mods;
			switch (action) {
			case GLFW_PRESS:
				keyEvent.pressed = true;
				break;
			case GLFW_RELEASE:
				keyEvent.released = true;
				break;
			case GLFW_REPEAT:
				keyEvent.repeated = true;
				break;
			}
		});
		glfwSetMouseButtonCallback(window.getHandle(), (window, button, action, mods) -> {
			if (button == GLFW_KEY_UNKNOWN) return;
			MouseEvent mouseEvent = mouseEvents[button];
			mouseEvent.mods = mods;
			switch (action) {
			case GLFW_PRESS:
				mouseEvent.pressed = true;
				break;
			case GLFW_RELEASE:
				mouseEvent.released = true;
				break;
			}
		});
		glfwSetCharCallback(window.getHandle(), (window, ch) -> {
			charEvents.add((char) ch);
		});
		glfwSetCursorPosCallback(window.getHandle(), (window, x, y) -> {
			mouseDeltaX = (float) x - mouseX;
			mouseDeltaY = (float) y - mouseY;
			mouseX = (float) x;
			mouseY = (float) y;
		});
		glfwSetScrollCallback(window.getHandle(), (window, x, y) -> {
			mouseScrollX = (float) x;
			mouseScrollY = (float) y;
		});
		glfwSetCursorEnterCallback(window.getHandle(), (window, entered) -> {
			if (entered) {
				mouseEntered = true;
			} else {
				mouseLeft = true;
			}
		});
	}

	public void exit() {

	}

	public void setMouseMode(int mode) {
		glfwSetInputMode(window.getHandle(), GLFW_CURSOR, mode);
	}

	public float getMouseX() {
		return mouseX;
	}

	public float getMouseY() {
		return mouseY;
	}

	public float getMouseDeltaX() {
		return mouseDeltaX;
	}

	public float getMouseDeltaY() {
		return mouseDeltaY;
	}

	public float getMouseScrollX() {
		return mouseScrollX;
	}

	public float getMouseScrollY() {
		return mouseScrollY;
	}

	public boolean isKeyMods(int key, int mods) {
		return (keyEvents[key].mods == mods);
	}

	public boolean isKeyPressed(int key) {
		return keyEvents[key].pressed;
	}

	public boolean isKeyReleased(int key) {
		return keyEvents[key].released;
	}

	public boolean isKeyRepeated(int key) {
		return keyEvents[key].repeated;
	}

	public boolean isKeyDown(int key) {
		return (glfwGetKey(window.getHandle(), key) == GLFW_PRESS);
	}

	public boolean isMouseMods(int button, int mods) {
		return (mouseEvents[button].mods == mods);
	}

	public boolean isMousePressed(int button) {
		return mouseEvents[button].pressed;
	}

	public boolean isMouseReleased(int button) {
		return mouseEvents[button].released;
	}

	public boolean isMouseDown(int button) {
		return (glfwGetMouseButton(window.getHandle(), button) == GLFW_PRESS);
	}

	public boolean hasMouseEntered() {
		return mouseEntered;
	}

	public boolean hasMouseLeft() {
		return mouseLeft;
	}

}
