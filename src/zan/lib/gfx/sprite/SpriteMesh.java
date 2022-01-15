package zan.lib.gfx.sprite;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.MemoryUtil;

import zan.lib.gfx.mesh.IndexMesh;
import zan.lib.gfx.texture.Texture;

public class SpriteMesh extends IndexMesh {

	public static class Vertex {

		private static final int SLOTS = 4;
		private static final int BYTES = SLOTS*Float.BYTES;

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

	private final Texture sheet;

	private final int cols;
	private final int rows;

	private final float width;
	private final float height;

	public SpriteMesh(Texture sheet, int cols, int rows) {
		this.sheet = sheet;
		this.cols = cols;
		this.rows = rows;

		List<Vertex> vertices = new ArrayList<>();
		List<Integer> elements = new ArrayList<>();

		float sheetWidth = sheet.getWidth();
		float sheetHeight = sheet.getHeight();

		width = sheetWidth / cols;
		height = sheetHeight / rows;

		float widthRatio = width / sheetWidth;
		float heightRatio = height / sheetHeight;

		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				vertices.add(new Vertex(0.0f, 0.0f, i * widthRatio, (j + 1) * heightRatio));
				vertices.add(new Vertex(width, 0.0f, (i + 1) * widthRatio, (j + 1) * heightRatio));
				vertices.add(new Vertex(width, height, (i + 1) * widthRatio, j * heightRatio));
				vertices.add(new Vertex(0.0f, height, i * widthRatio, j * heightRatio));

				elements.add(4 * cols * j + 4 * i + 0);
				elements.add(4 * cols * j + 4 * i + 1);
				elements.add(4 * cols * j + 4 * i + 2);
				elements.add(4 * cols * j + 4 * i + 3);
				elements.add(4 * cols * j + 4 * i + 0);
				elements.add(4 * cols * j + 4 * i + 2);
			}
		}

		mesh(vertices, elements);
	}

	public SpriteMesh(Texture sheet) {
		this(sheet, 1, 1);
	}

	private void mesh(List<Vertex> vertices, List<Integer> elements) {
		vertexCount = vertices.size();
		elementCount = elements.size();

		glBindVertexArray(vao);

		FloatBuffer vertexData = MemoryUtil.memAllocFloat(vertexCount*Vertex.SLOTS);
		for (Vertex vertex : vertices) {
			vertexData.put(vertex.x);
			vertexData.put(vertex.y);
			vertexData.put(vertex.s);
			vertexData.put(vertex.t);
		}
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexData.flip(), GL_STATIC_DRAW);
		MemoryUtil.memFree(vertexData);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(0, 2, GL_FLOAT, false, Vertex.BYTES, 0*Float.BYTES);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.BYTES, 2*Float.BYTES);

		IntBuffer elementData = MemoryUtil.memAllocInt(elementCount);
		for (Integer element : elements) {
			elementData.put(element);
		}
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementData.flip(), GL_STATIC_DRAW);
		MemoryUtil.memFree(elementData);

		glBindVertexArray(0);
	}

	@Override
	public void bind() {
		super.bind();
		sheet.bind();
	}

	@Override
	public void unbind() {
		sheet.unbind();
		super.unbind();
	}

	@Override
	public void draw() {
		drawFrame(0);
	}

	@Override
	public void draw(int instanceCount) {
		glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, instanceCount);
	}

	public void drawFrame(int frame) {
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 6 * frame * Integer.BYTES);
	}

	public int getColumns() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

	public int getFrameCount() {
		return cols * rows;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

}
