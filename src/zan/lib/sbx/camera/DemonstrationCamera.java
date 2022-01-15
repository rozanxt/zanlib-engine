package zan.lib.sbx.camera;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import zan.lib.app.Input;
import zan.lib.app.Window;
import zan.lib.gfx.camera.Camera;
import zan.lib.utl.Utility;

public class DemonstrationCamera implements Camera {

	private static class State {

		private float rotationX = 0.0f;
		private float rotationY = 0.0f;
		private float translationZ = 0.0f;

		private void set(State state) {
			this.rotationX = state.rotationX;
			this.rotationY = state.rotationY;
			this.translationZ = state.translationZ;
		}

	}

	private Window window;
	private Input input;

	private float width;
	private float height;

	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();

	private State prevState = new State();
	private State nextState = new State();

	private float fovy = 60.0f;
	private float near = 0.01f;
	private float far = 1000.0f;

	public DemonstrationCamera(Window window, Input input) {
		this.window = window;
		this.input = input;
		prevState.translationZ = -5.0f;
		nextState.translationZ = -5.0f;
	}

	@Override
	public void update(float theta) {
		prevState.set(nextState);

		if (input.isMouseDown(GLFW_MOUSE_BUTTON_1)) {
			nextState.rotationX += 0.5f * input.getMouseDeltaY();
			nextState.rotationY += 0.5f * input.getMouseDeltaX();
		}
		nextState.translationZ += input.getMouseScrollY();
	}

	@Override
	public void capture(float theta) {
		if (width != window.getWidth() || height != window.getHeight()) {
			width = window.getWidth();
			height = window.getHeight();
			projectionMatrix.setPerspective((float) Math.toRadians(fovy), width / height, near, far);
		}

		float rotationX = Utility.lerp(prevState.rotationX, nextState.rotationX, theta);
		float rotationY = Utility.lerp(prevState.rotationY, nextState.rotationY, theta);
		float translationZ = Utility.lerp(prevState.translationZ, nextState.translationZ, theta);

		viewMatrix.identity()
			.translate(0.0f, 0.0f, translationZ)
			.rotate((float) Math.toRadians(rotationX), 1.0f, 0.0f, 0.0f)
			.rotate((float) Math.toRadians(rotationY), 0.0f, 1.0f, 0.0f);
	}

	@Override
	public Matrix4fc getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public Matrix4fc getViewMatrix() {
		return viewMatrix;
	}

}
