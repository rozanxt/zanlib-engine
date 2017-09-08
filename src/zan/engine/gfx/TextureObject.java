package zan.engine.gfx;

public class TextureObject extends VertexObject {

	protected Texture texture;

	public TextureObject(VertexData data, Texture texture) {
		super(data);
		this.texture = texture;
	}

	@Override
	public void render() {
		if (texture.isValid()) texture.bind();
		super.render();
		texture.unbind();
	}

}
