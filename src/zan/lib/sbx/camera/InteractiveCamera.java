package zan.lib.sbx.camera;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_3;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import zan.lib.app.Input;
import zan.lib.app.Window;
import zan.lib.gfx.camera.Camera;
import zan.lib.utl.Utility;

public class InteractiveCamera implements Camera {

	private static class State {

		private Vector3f target = new Vector3f();
		private float distance = 0.0f;
		private float altitude = 60.0f;
		private float azimuth = 0.0f;

		private void set(State state) {
			target.set(state.target);
			distance = state.distance;
			altitude = state.altitude;
			azimuth = state.azimuth;
		}

	}

	private Window window;
	private Input input;

	private float width;
	private float height;

	private State prevState = new State();
	private State nextState = new State();

	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();

	private Vector3f frontVector = new Vector3f();
	private Vector3f upVector = new Vector3f();
	private Vector3f sideVector = new Vector3f();

	private int zoomLevel = 2;
	private int zoomLevels = 10;
	private float zoomMinimum = 2.0f;
	private float zoomMaximum = 100.0f;
	private float[] zoomValues = new float[zoomLevels];

	private Vector3f scrollVelocity = new Vector3f(0.0f, 0.0f, 0.0f);
	private float scrollBaseMaxSpeed = 0.5f;
	private float scrollBaseAcceleration = 0.05f;
	private float scrollAttenuation = 0.5f;
	private float scrollAreaSize = 2.0f;

	private float fovy = 60.0f;
	private float near = 0.01f;
	private float far = 1000.0f;

	public InteractiveCamera(Window window, Input input) {
		this.window = window;
		this.input = input;
		for (int i = 0; i < zoomLevels; i++) {
			zoomValues[i] = (float) (zoomMinimum*Math.exp(Math.log(zoomMaximum/zoomMinimum)*i/(zoomLevels-1)));
		}
		prevState.distance = zoomValues[zoomLevel];
		nextState.distance = zoomValues[zoomLevel];
	}

	@Override
	public void update(float theta) {
		prevState.set(nextState);

		float prevAltitude = (float) Math.toRadians(prevState.altitude);
		float prevAzimuth = (float) Math.toRadians(prevState.azimuth);
		frontVector.set(Math.sin(prevAzimuth)*Math.cos(prevAltitude), -Math.sin(prevAltitude), -Math.cos(prevAzimuth)*Math.cos(prevAltitude)).negate();
		upVector.set(Math.sin(prevAzimuth)*Math.sin(prevAltitude), Math.cos(prevAltitude), -Math.cos(prevAzimuth)*Math.sin(prevAltitude));
		sideVector.set(Math.cos(prevAzimuth), 0.0f, Math.sin(prevAzimuth));

		float scrollMaxSpeed = scrollBaseMaxSpeed*zoomValues[zoomLevel]/zoomMinimum;
		float scrollAcceleration = scrollBaseAcceleration*zoomValues[zoomLevel]/zoomMinimum;

		if (Math.abs(input.getMouseX()) < scrollAreaSize || input.isKeyDown(GLFW_KEY_LEFT)) {
			scrollVelocity.fma(-scrollAcceleration, sideVector);
		}
		if (Math.abs(input.getMouseX()-window.getWidth()) < scrollAreaSize || input.isKeyDown(GLFW_KEY_RIGHT)) {
			scrollVelocity.fma(scrollAcceleration, sideVector);
		}
		if (Math.abs(input.getMouseY()) < scrollAreaSize || input.isKeyDown(GLFW_KEY_UP)) {
			scrollVelocity.fma(scrollAcceleration, upVector);
		}
		if (Math.abs(input.getMouseY()-window.getHeight()) < scrollAreaSize || input.isKeyDown(GLFW_KEY_DOWN)) {
			scrollVelocity.fma(-scrollAcceleration, upVector);
		}

		if (scrollVelocity.length() > scrollMaxSpeed) {
			scrollVelocity.normalize(scrollMaxSpeed);
		}
		nextState.target.add(scrollVelocity);
		scrollVelocity.mul(scrollAttenuation);
		if (scrollVelocity.length() < scrollAttenuation*scrollAcceleration) {
			scrollVelocity.zero();
		}

		if (input.isMouseDown(GLFW_MOUSE_BUTTON_3)) {
			nextState.azimuth += input.getMouseDeltaX();
			nextState.altitude += input.getMouseDeltaY();
		}

		zoomLevel -= (int) input.getMouseScrollY();
		zoomLevel = Math.min(Math.max(zoomLevel, 0), zoomLevels-1);

		if (Math.abs(zoomValues[zoomLevel]-nextState.distance) >= scrollAttenuation*scrollAcceleration) {
			nextState.distance += scrollAttenuation*(zoomValues[zoomLevel]-nextState.distance);
		} else {
			nextState.distance = zoomValues[zoomLevel];
		}

		nextState.altitude = Math.min(Math.max(nextState.altitude, -90.0f), 90.0f);
		if (nextState.azimuth < 0.0f) {
			prevState.azimuth += 360.0f;
			nextState.azimuth += 360.0f;
		}
		if (nextState.azimuth > 360.0f) {
			prevState.azimuth -= 360.0f;
			nextState.azimuth -= 360.0f;
		}
	}

	@Override
	public void capture(float theta) {
		if (width != window.getWidth() || height != window.getHeight()) {
			width = window.getWidth();
			height = window.getHeight();
			projectionMatrix.setPerspective((float) Math.toRadians(fovy), width / height, near, far);
		}

		Vector3f target = new Vector3f();
		prevState.target.lerp(nextState.target, theta, target);
		float distance = Utility.lerp(prevState.distance, nextState.distance, theta);
		float altitude = Utility.lerp(prevState.altitude, nextState.altitude, theta);
		float azimuth = Utility.lerp(prevState.azimuth, nextState.azimuth, theta);

		viewMatrix.identity()
			.translate(0.0f, 0.0f, -distance)
			.rotateX((float) Math.toRadians(altitude))
			.rotateY((float) Math.toRadians(azimuth))
			.translate(target.negate());
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
