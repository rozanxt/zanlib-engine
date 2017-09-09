package zan.engine.gfx;

public class TextureObject extends VertexObject {

	protected final TextureData texture;

	public TextureObject(VertexData data, TextureData texture) {
		super(data);
		this.texture = texture;
	}

	@Override
	public void render() {
		texture.bind();
		super.render();
		texture.unbind();
	}

}
