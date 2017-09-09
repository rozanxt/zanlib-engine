package zan.engine.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.system.MemoryUtil;

import zan.engine.util.ConversionUtil;

public class FontData extends TextureData {

	protected static class CharData {
		public final int x;
		public final int w;

		public CharData(int x, int w) {
			this.x = x;
			this.w = w;
		}
	}

	protected final Map<Character, CharData> charData;

	public FontData(ByteBuffer data, int width, int height, Map<Character, CharData> charData) {
		super(data, width, height);
		this.charData = charData;
	}

	public CharData getCharData(char c) {
		return charData.get(c);
	}

	public static FontData create(Font font) {
		return create(font, "ISO-8859-1");
	}

	public static FontData create(Font font, String charset) {
		CharsetEncoder ce = Charset.forName(charset).newEncoder();
		StringBuilder sa = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		for (char c = 0; c < Character.MAX_VALUE; c++) {
			if (ce.canEncode(c)) {
				sa.append(c);
				sb.append(c).append(' ');
			}
		}
		String metricsChars = sa.toString();
		String textureChars = sb.toString();

		Map<Character, CharData> charData = new HashMap<>();
		int width = 0;
		int height = 0;

		BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int padding = 0;
		if (font.isItalic()) {
			padding = 1;
		}
		for (char c : metricsChars.toCharArray()) {
			CharData cd = new CharData(width, fm.charWidth(c) + padding);
			charData.put(c, cd);
			width += fm.charWidth(c) + fm.charWidth(' ');
			height = Math.max(height, fm.getHeight());
		}
		g2d.dispose();

		bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = bi.createGraphics();
		g2d.setFont(font);
		fm = g2d.getFontMetrics();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);
		g2d.drawString(textureChars, 0, fm.getAscent());
		g2d.dispose();

		ByteBuffer buffer = ConversionUtil.BufferedImageToByteBuffer(bi);
		FontData result = new FontData(buffer, bi.getWidth(), bi.getHeight(), charData);
		MemoryUtil.memFree(buffer);
		return result;
	}

}
