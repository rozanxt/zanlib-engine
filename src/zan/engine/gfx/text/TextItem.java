package zan.engine.gfx.text;

import zan.engine.gfx.mesh.Mesh;
import zan.engine.gfx.texture.FontTexture;

public abstract class TextItem {

	protected String text;
	protected FontTexture font;

	protected int width;

	protected Mesh mesh;

	public TextItem(String text, FontTexture font, int width) {
		this.text = text;
		this.font = font;
		this.width = width;
	}

	public void delete() {
		mesh.delete();
	}

	public abstract void update();

	public void render() {
		font.bind();
		mesh.bind();
		mesh.draw();
		mesh.unbind();
		font.unbind();
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setFont(FontTexture font) {
		this.font = font;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
