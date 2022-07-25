package zan.lib.app;

public interface Scene {

	void init();

	void exit();

	void update(float delta);

	void render(float theta);

}
