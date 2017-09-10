package zan.engine.gfx.text;

import zan.engine.gfx.mesh.Mesh3D;
import zan.engine.gfx.texture.FontTexture;

public class Text3D extends TextItem {

	public Text3D(String text, FontTexture font, int width) {
		super(text, font, width);
		TextBuild build = new TextBuild(text, font, width);
		mesh = new Mesh3D(build.pos, build.tex, new float[] { 0 }, build.ind);
	}

	public Text3D(String text, FontTexture font) {
		this(text, font, 0);
	}

	public Text3D(FontTexture font) {
		this("", font);
	}

	@Override
	public void update() {
		TextBuild build = new TextBuild(text, font, width);
		mesh.bind();
		mesh.setVertexData(Mesh3D.POS, build.pos);
		mesh.setVertexData(Mesh3D.TEX, build.tex);
		mesh.setIndexData(Mesh3D.IND, build.ind);
		mesh.setNumElements(build.ind.length);
		mesh.unbind();
	}

}
