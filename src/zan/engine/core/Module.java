package zan.engine.core;

public interface Module {

	void init();

	void exit();

	void input();

	void update();

	void render();

}
