package zan.engine.gfx;

import java.util.ArrayList;
import java.util.List;

import zan.engine.util.TypeConverter;

public class TextItem {

	private static class TextBuild {
		public final float[] pos;
		public final float[] tex;
		public final int[] ind;

		public TextBuild(String text, TextFont font, int width) {
			List<Float> pos = new ArrayList<>();
			List<Float> tex = new ArrayList<>();
			List<Integer> ind = new ArrayList<>();
			char[] chars = text.toCharArray();

			float offsetX = 0.0f;
			int line = 0;
			int fontWidth = font.getTexture().getWidth();
			int fontHeight = font.getTexture().getHeight();
			for (int i = 0; i < chars.length; i++) {
				TextFont.CharInfo data = font.getCharInfo(chars[i]);

				pos.add(offsetX);
				pos.add((float) -line * fontHeight);
				tex.add((float) data.x / (float) fontWidth);
				tex.add(1.0f);

				pos.add(offsetX + data.w);
				pos.add((float) -line * fontHeight);
				tex.add((float) (data.x + data.w) / (float) fontWidth);
				tex.add(1.0f);

				pos.add(offsetX + data.w);
				pos.add((float) (1 - line) * fontHeight);
				tex.add((float) (data.x + data.w) / (float) fontWidth);
				tex.add(0.0f);

				pos.add(offsetX);
				pos.add((float) (1 - line) * fontHeight);
				tex.add((float) data.x / (float) fontWidth);
				tex.add(0.0f);

				ind.add(4 * i);
				ind.add(4 * i + 1);
				ind.add(4 * i + 2);
				ind.add(4 * i + 3);
				ind.add(4 * i);
				ind.add(4 * i + 2);

				offsetX += data.w;

				if (width > 0) {
					if (chars[i] == '\n' || (chars[i] == ' ' && offsetX > width)) {
						offsetX = 0.0f;
						line++;
					}
				}
			}

			this.pos = TypeConverter.FloatListToArray(pos);
			this.tex = TypeConverter.FloatListToArray(tex);
			this.ind = TypeConverter.IntegerListToArray(ind);
		}
	}

	private String text;
	private TextFont font;

	private int width;

	private final Mesh2D mesh;

	public TextItem(String text, TextFont font, int width) {
		this.text = text;
		this.font = font;
		this.width = width;
		TextBuild build = new TextBuild(text, font, width);
		mesh = new Mesh2D(build.pos, build.tex, build.ind);
	}

	public TextItem(String text, TextFont font) {
		this(text, font, 0);
	}

	public TextItem(TextFont font, int width) {
		this("", font, width);
	}

	public TextItem(TextFont font) {
		this("", font);
	}

	public void delete() {
		mesh.delete();
	}

	public void update() {
		TextBuild build = new TextBuild(text, font, width);
		mesh.bind();
		mesh.setVertexData(Mesh2D.POS, build.pos);
		mesh.setVertexData(Mesh2D.TEX, build.tex);
		mesh.setIndexData(Mesh2D.IND, build.ind);
		mesh.setNumElements(build.ind.length);
		mesh.unbind();
	}

	public void render() {
		font.getTexture().bind();
		mesh.bind();
		mesh.draw();
		mesh.unbind();
		font.getTexture().bind();
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setFont(TextFont font) {
		this.font = font;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
