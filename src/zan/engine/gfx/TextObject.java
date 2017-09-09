package zan.engine.gfx;

import java.util.ArrayList;
import java.util.List;

import zan.engine.util.ConversionUtil;

public class TextObject extends TextureObject {

	public TextObject(VertexData data, TextureData texture) {
		super(data, texture);
	}

	@Override
	public void delete() {
		data.delete();
	}

	public static TextObject create(String text, FontData font) {
		return create(text, font, 0);
	}

	public static TextObject create(String text, FontData font, int width) {
		List<Float> positions = new ArrayList<>();
		List<Float> texcoords = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		char[] characters = text.toCharArray();

		float offsetX = 0.0f;
		int line = 0;
		for (int i = 0; i < characters.length; i++) {
			FontData.CharData data = font.getCharData(characters[i]);

			positions.add(offsetX);
			positions.add((float) -line * font.getHeight());
			positions.add(0.0f);
			texcoords.add((float) data.x / (float) font.getWidth());
			texcoords.add(1.0f);

			positions.add(offsetX + data.w);
			positions.add((float) -line * font.getHeight());
			positions.add(0.0f);
			texcoords.add((float) (data.x + data.w) / (float) font.getWidth());
			texcoords.add(1.0f);

			positions.add(offsetX + data.w);
			positions.add((float) (1 - line) * font.getHeight());
			positions.add(0.0f);
			texcoords.add((float) (data.x + data.w) / (float) font.getWidth());
			texcoords.add(0.0f);

			positions.add(offsetX);
			positions.add((float) (1 - line) * font.getHeight());
			positions.add(0.0f);
			texcoords.add((float) data.x / (float) font.getWidth());
			texcoords.add(0.0f);

			indices.add(4 * i);
			indices.add(4 * i + 1);
			indices.add(4 * i + 2);
			indices.add(4 * i + 3);
			indices.add(4 * i);
			indices.add(4 * i + 2);

			offsetX += data.w;

			if (width > 0) {
				if (characters[i] == '\n' || (characters[i] == ' ' && offsetX > width)) {
					offsetX = 0.0f;
					line++;
				}
			}
		}

		return new TextObject(new MeshData(ConversionUtil.FloatListToArray(positions), ConversionUtil.FloatListToArray(texcoords), new float[0], indices.stream().mapToInt(ind -> ind).toArray()), font);
	}

}
