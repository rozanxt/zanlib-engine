package zan.engine.gfx.text;

import java.util.ArrayList;
import java.util.List;

import zan.engine.gfx.texture.FontTexture;
import zan.engine.util.TypeConverter;

public class TextBuild {

	public final float[] pos;
	public final float[] tex;
	public final int[] ind;

	public TextBuild(String text, FontTexture font, int width) {
		List<Float> pos = new ArrayList<>();
		List<Float> tex = new ArrayList<>();
		List<Integer> ind = new ArrayList<>();
		char[] chars = text.toCharArray();

		float offsetX = 0.0f;
		int line = 0;
		for (int i = 0; i < chars.length; i++) {
			FontTexture.CharInfo data = font.getCharInfo(chars[i]);

			pos.add(offsetX);
			pos.add((float) -line * font.getHeight());
			pos.add(0.0f);
			tex.add((float) data.x / (float) font.getWidth());
			tex.add(1.0f);

			pos.add(offsetX + data.w);
			pos.add((float) -line * font.getHeight());
			pos.add(0.0f);
			tex.add((float) (data.x + data.w) / (float) font.getWidth());
			tex.add(1.0f);

			pos.add(offsetX + data.w);
			pos.add((float) (1 - line) * font.getHeight());
			pos.add(0.0f);
			tex.add((float) (data.x + data.w) / (float) font.getWidth());
			tex.add(0.0f);

			pos.add(offsetX);
			pos.add((float) (1 - line) * font.getHeight());
			pos.add(0.0f);
			tex.add((float) data.x / (float) font.getWidth());
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
		this.ind = ind.stream().mapToInt(i -> i).toArray();
	}

}
