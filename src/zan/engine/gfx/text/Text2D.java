package zan.engine.gfx.text;

import zan.engine.gfx.mesh.Mesh2D;
import zan.engine.gfx.texture.FontTexture;

public class Text2D extends TextItem {

	public Text2D(String text, FontTexture font, int width) {
		super(text, font, width);
		TextBuild build = new TextBuild(text, font, width);
		mesh = new Mesh2D(build.pos, build.tex, build.ind);
	}

	public Text2D(String text, FontTexture font) {
		this(text, font, 0);
	}

	public Text2D(FontTexture font) {
		this("", font);
	}

	@Override
	public void update() {
		TextBuild build = new TextBuild(text, font, width);
		mesh.bind();
		mesh.setVertexData(Mesh2D.POS, build.pos);
		mesh.setVertexData(Mesh2D.TEX, build.tex);
		mesh.setIndexData(Mesh2D.IND, build.ind);
		mesh.setNumElements(build.ind.length);
		mesh.unbind();
	}

}
