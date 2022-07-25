package zan.lib.gfx.cam;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import zan.lib.app.Window;
import zan.lib.utl.Utility;

public class WorldCamera implements Camera {

	public static class State {

		public Vector3f eye = new Vector3f(0.0f, 0.0f, 0.0f);
		public Vector3f dir = new Vector3f(0.0f, 1.0f, 0.0f);
		public Vector3f up = new Vector3f(0.0f, 0.0f, 1.0f);

		public void set(State state) {
			eye.set(state.eye);
			dir.set(state.dir);
			up.set(state.up);
		}

		public static State interp(State prev, State next, float theta) {
			State state = new State();
			state.eye.set(Utility.lerp(prev.eye, next.eye, theta));
			state.dir.set(Utility.slerp(prev.dir, next.dir, theta));
			state.up.set(Utility.slerp(prev.up, next.up, theta));
			return state;
		}

	}

	private Window window;

	private int width;
	private int height;

	private float fovy = 60.0f;
	private float near = 0.01f;
	private float far = 1000.0f;

	private State prev = new State();
	private State next = new State();

	private Matrix4f proj = new Matrix4f();
	private Matrix4f view = new Matrix4f();

	public WorldCamera(Window window) {
		this.window = window;
	}

	@Override
	public void update(float delta) {
		prev.set(next);
	}

	@Override
	public void capture(float theta) {
		if (width != window.getWidth() || height != window.getHeight()) {
			width = window.getWidth();
			height = window.getHeight();
			proj.setPerspective((float) Math.toRadians(fovy), (float) width / (float) height, near, far);
		}

		State state = State.interp(prev, next, theta);
		view.setLookAlong(state.dir, state.up).translate(new Vector3f(state.eye).negate());
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
