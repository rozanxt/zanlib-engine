package zan.lib.gfx.cam;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import zan.lib.app.Window;

public class ScreenCamera implements Camera {

	private Window window;

	private int width;
	private int height;

	private Matrix4f proj;
	private Matrix4f view;

	public ScreenCamera(Window window) {
		this.window = window;
		proj = new Matrix4f();
		view = new Matrix4f();
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void capture(float theta) {
		if (width != window.getWidth() || height != window.getHeight()) {
			width = window.getWidth();
			height = window.getHeight();
			proj.setOrtho2D(0, width, 0, height);
		}
	}

	@Override
	public Matrix4fc getProjectionMatrix() {
		return proj;
	}

	@Override
	public Matrix4fc getViewMatrix() {
		return view;
	}

}
