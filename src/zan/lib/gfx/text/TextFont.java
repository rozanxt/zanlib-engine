package zan.lib.gfx.text;

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

import zan.lib.gfx.texture.Texture;
import zan.lib.utl.Utility;

public class TextFont {

	public static class CharInfo {

		public final int x;
		public final int w;

		public CharInfo(int x, int w) {
			this.x = x;
			this.w = w;
		}

	}

	private final Map<Character, CharInfo> info;

	private final Texture texture;

	public TextFont(Font font, String charset) {
		CharsetEncoder ce = Charset.forName(charset).newEncoder();
		StringBuilder mc = new StringBuilder();
		StringBuilder tc = new StringBuilder();
		for (char ch = 0; ch < Character.MAX_VALUE; ch++) {
			if (ce.canEncode(ch)) {
				mc.append(ch);
				tc.append(ch).append(' ');
			}
		}
		String metricsChars = mc.toString();
		String textureChars = tc.toString();

		info = new HashMap<>();
		int width = 0;
		int height = 0;
		int padding = 0;
		if (font.isItalic()) padding = 1;

		BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gf = bi.createGraphics();
		gf.setFont(font);
		FontMetrics fm = gf.getFontMetrics();
		for (char ch : metricsChars.toCharArray()) {
			CharInfo ci = new CharInfo(width, fm.charWidth(ch) + padding);
			info.put(ch, ci);
			width += fm.charWidth(ch) + fm.charWidth(' ');
			height = Math.max(height, fm.getHeight());
		}
		gf.dispose();

		bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		gf = bi.createGraphics();
		gf.setFont(font);
		fm = gf.getFontMetrics();
		gf.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gf.setColor(Color.WHITE);
		gf.drawString(textureChars, 0, fm.getAscent());
		gf.dispose();

		ByteBuffer data = Utility.BufferedImageToByteBuffer(bi);
		texture = new Texture(data, bi.getWidth(), bi.getHeight());
		MemoryUtil.memFree(data);
	}

	public TextFont(Font font) {
		this(font, "ISO-8859-1");
	}

	public void delete() {
		texture.delete();
	}

	public CharInfo getCharInfo(char ch) {
		return info.get(ch);
	}

	public Texture getTexture() {
		return texture;
	}

}
