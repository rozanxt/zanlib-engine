package zan.lib.gfx.txt;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.MemoryUtil;

import zan.lib.gfx.msh.IndexMesh;
import zan.lib.gfx.tex.Texture;

public class TextMesh extends IndexMesh {

	public static class Vertex {

		private static final int SLOTS = 4;
		private static final int BYTES = SLOTS * Float.BYTES;

		private final float x;
		private final float y;
		private final float s;
		private final float t;

		public Vertex(float x, float y, float s, float t) {
			this.x = x;
			this.y = y;
			this.s = s;
			this.t = t;
		}

	}

	private String text;

	private TextFont font;

	private float maxWidth;

	private float width;
	private float height;

	private float offset;

	private int line;

	public TextMesh(TextFont font) {
		this.font = font;
	}

	public void setFont(TextFont font) {
		this.font = font;
	}

	public void setMaxWidth(float maxWidth) {
		this.maxWidth = maxWidth;
	}

	public void build(String text) {
		this.text = text;

		Texture texture = font.getTexture();
		float fontWidth = texture.getWidth();
		float fontHeight = texture.getHeight();
		char[] chars = text.toCharArray();

		List<Vertex> vertices = new ArrayList<>();
		List<Integer> elements = new ArrayList<>();

		line = 0;
		offset = 0.0f;
		width = offset;
		height = fontHeight;

		for (int i = 0; i < chars.length; i++) {
			TextFont.CharInfo data = font.getCharInfo(chars[i]);

			vertices.add(new TextMesh.Vertex(offset, - line * fontHeight, data.x / fontWidth, 1.0f));
			vertices.add(new TextMesh.Vertex(offset + data.w, - line * fontHeight, (data.x + data.w) / fontWidth, 1.0f));
			vertices.add(new TextMesh.Vertex(offset + data.w, (1 - line) * fontHeight, (data.x + data.w) / fontWidth, 0.0f));
			vertices.add(new TextMesh.Vertex(offset, (1 - line) * fontHeight, data.x / fontWidth, 0.0f));

			elements.add(4 * i);
			elements.add(4 * i + 1);
			elements.add(4 * i + 2);
			elements.add(4 * i + 3);
			elements.add(4 * i);
			elements.add(4 * i + 2);

			offset += data.w;

			if (offset > width) {
				width = offset;
			}
			if (chars[i] == '\n' || (maxWidth > 0 && offset > maxWidth && chars[i] == ' ')) {
				offset = 0.0f;
				line++;
			}
		}

		buffer(vertices, elements);
	}

	private void buffer(List<Vertex> vertices, List<Integer> indices) {
		vertexCount = vertices.size();
		indexCount = indices.size();

		glBindVertexArray(vao);

		FloatBuffer vertexData = MemoryUtil.memAllocFloat(vertexCount * Vertex.SLOTS);
		for (Vertex vertex : vertices) {
			vertexData.put(vertex.x);
			vertexData.put(vertex.y);
			vertexData.put(vertex.s);
			vertexData.put(vertex.t);
		}
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexData.flip(), GL_STREAM_DRAW);
		MemoryUtil.memFree(vertexData);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(0, 2, GL_FLOAT, false, Vertex.BYTES, 0 * Float.BYTES);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.BYTES, 2 * Float.BYTES);

		IntBuffer indexData = MemoryUtil.memAllocInt(indexCount);
		for (Integer index : indices) {
			indexData.put(index);
		}
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData.flip(), GL_STREAM_DRAW);
		MemoryUtil.memFree(indexData);

		glBindVertexArray(0);
	}

	@Override
	public void bind() {
		super.bind();
		font.getTexture().bind();
	}

	@Override
	public void unbind() {
		font.getTexture().unbind();
		super.unbind();
	}

	public String getText() {
		return text;
	}

	public TextFont getFont() {
		return font;
	}

	public float getMaxWidth() {
		return maxWidth;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getOffset() {
		return offset;
	}

	public int getLineCount() {
		return line + 1;
	}

}
