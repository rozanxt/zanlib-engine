package zan.lib.gfx.camera;

import org.joml.Matrix4fc;

public interface Camera {

	void update(float theta);

	void capture(float theta);

	Matrix4fc getProjectionMatrix();

	Matrix4fc getViewMatrix();

}
