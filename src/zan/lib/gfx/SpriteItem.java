package zan.lib.gfx;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.ArrayList;
import java.util.List;

import zan.lib.utl.Utility;

public class SpriteItem {

	private Texture sheet;

	private int cols;
	private int rows;

	private float width;
	private float height;

	private Mesh2D mesh;

	public SpriteItem(Texture sheet, int cols, int rows) {
		this.sheet = sheet;
		this.cols = cols;
		this.rows = rows;

		List<Float> positions = new ArrayList<>();
		List<Float> texcoords = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();

		float sheetWidth = sheet.getWidth();
		float sheetHeight = sheet.getHeight();

		width = sheetWidth / cols;
		height = sheetHeight / rows;

		float widthRatio = width / sheetWidth;
		float heightRatio = height / sheetHeight;

		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				positions.add(0.0f);
				positions.add(0.0f);
				texcoords.add(i * widthRatio);
				texcoords.add((j + 1) * heightRatio);

				positions.add(width);
				positions.add(0.0f);
				texcoords.add((i + 1) * widthRatio);
				texcoords.add((j + 1) * heightRatio);

				positions.add(width);
				positions.add(height);
				texcoords.add((i + 1) * widthRatio);
				texcoords.add(j * heightRatio);

				positions.add(0.0f);
				positions.add(height);
				texcoords.add(i * widthRatio);
				texcoords.add(j * heightRatio);

				indices.add(4 * cols * j + 4 * i + 0);
				indices.add(4 * cols * j + 4 * i + 1);
				indices.add(4 * cols * j + 4 * i + 2);
				indices.add(4 * cols * j + 4 * i + 3);
				indices.add(4 * cols * j + 4 * i + 0);
				indices.add(4 * cols * j + 4 * i + 2);
			}
		}

		mesh = new Mesh2D(Utility.FloatListToArray(positions), Utility.FloatListToArray(texcoords), Utility.IntegerListToArray(indices));
	}

	public SpriteItem(Texture sheet) {
		this(sheet, 1, 1);
	}

	public void delete() {
		mesh.delete();
	}

	public void render(int frame) {
		sheet.bind();
		mesh.bind();
		mesh.draw(GL_TRIANGLES, 6, 6 * frame);
		mesh.unbind();
		sheet.unbind();
	}

	public void render() {
		render(0);
	}

	public int getColumns() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

}
