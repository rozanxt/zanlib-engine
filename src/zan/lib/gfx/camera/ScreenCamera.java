package zan.lib.gfx.camera;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import zan.lib.app.Window;

public class ScreenCamera implements Camera {

	private Window window;

	private float width;
	private float height;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;

	public ScreenCamera(Window window) {
		this.window = window;
		projectionMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
	}

	@Override
	public void update(float theta) {

	}

	@Override
	public void capture(float theta) {
		if (width != window.getWidth() || height != window.getHeight()) {
			width = window.getWidth();
			height = window.getHeight();
			projectionMatrix.setOrtho2D(0, width, 0, height);
		}
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
