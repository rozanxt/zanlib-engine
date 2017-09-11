package zan.engine.gfx.texture;

import static org.lwjgl.opengl.GL11.*;

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

import zan.engine.util.TypeConverter;

public class FontTexture extends Texture {

	public static class CharInfo {
		public final int x;
		public final int w;

		public CharInfo(int x, int w) {
			this.x = x;
			this.w = w;
		}
	}

	private final Map<Character, CharInfo> fontInfo;

	public FontTexture(ByteBuffer data, int width, int height, int minfilter, int magfilter, Map<Character, CharInfo> fontInfo) {
		super(data, width, height, minfilter, magfilter);
		this.fontInfo = fontInfo;
	}

	public FontTexture(ByteBuffer data, int width, int height, Map<Character, CharInfo> fontInfo) {
		this(data, width, height, GL_NEAREST, GL_NEAREST, fontInfo);
	}

	public CharInfo getCharInfo(char ch) {
		return fontInfo.get(ch);
	}

	public static FontTexture load(Font font) {
		return load(font, "ISO-8859-1");
	}

	public static FontTexture load(Font font, String charset) {
		CharsetEncoder ce = Charset.forName(charset).newEncoder();
		StringBuilder sa = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		for (char ch = 0; ch < Character.MAX_VALUE; ch++) {
			if (ce.canEncode(ch)) {
				sa.append(ch);
				sb.append(ch).append(' ');
			}
		}
		String metricsChars = sa.toString();
		String textureChars = sb.toString();

		Map<Character, CharInfo> fontInfo = new HashMap<>();
		int width = 0;
		int height = 0;
		int padding = 0;
		if (font.isItalic()) padding = 1;

		BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		for (char ch : metricsChars.toCharArray()) {
			CharInfo ci = new CharInfo(width, fm.charWidth(ch) + padding);
			fontInfo.put(ch, ci);
			width += fm.charWidth(ch) + fm.charWidth(' ');
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

		ByteBuffer buffer = TypeConverter.BufferedImageToByteBuffer(bi);
		FontTexture result = new FontTexture(buffer, bi.getWidth(), bi.getHeight(), fontInfo);
		MemoryUtil.memFree(buffer);
		return result;
	}

}
