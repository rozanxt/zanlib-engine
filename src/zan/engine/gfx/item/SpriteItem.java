package zan.engine.gfx.item;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import zan.engine.gfx.mesh.Mesh2D;
import zan.engine.gfx.texture.SpriteTexture;
import zan.engine.util.TypeConverter;

public class SpriteItem {

	private SpriteTexture sheet;

	private Mesh2D mesh;

	private int frame;

	public SpriteItem(SpriteTexture sheet) {
		List<Float> positions = new ArrayList<>();
		List<Float> texcoords = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		float sheetWidth = (float) sheet.getWidth();
		float sheetHeight = (float) sheet.getHeight();
		float width = sheetWidth / (float) sheet.getCols();
		float height = sheetHeight / (float) sheet.getRows();
		int j = 0;

		// for (int j = 0; j < sheet.getRows(); j++) {
		for (int i = 0; i < sheet.getCols(); i++) {
			positions.add(0.0f);
			positions.add(0.0f);
			positions.add(width);
			positions.add(0.0f);
			positions.add(width);
			positions.add(height);
			positions.add(0.0f);
			positions.add(height);

			texcoords.add(i * width / sheetWidth);
			texcoords.add((j + 1) * height / sheetHeight);
			texcoords.add((i + 1) * width / sheetWidth);
			texcoords.add((j + 1) * height / sheetHeight);
			texcoords.add((i + 1) * width / sheetWidth);
			texcoords.add(j * height / sheetHeight);
			texcoords.add(i * width / sheetWidth);
			texcoords.add(j * height / sheetHeight);

			indices.add(4 * sheet.getCols() * j + 4 * i + 0);
			indices.add(4 * sheet.getCols() * j + 4 * i + 1);
			indices.add(4 * sheet.getCols() * j + 4 * i + 2);
			indices.add(4 * sheet.getCols() * j + 4 * i + 3);
			indices.add(4 * sheet.getCols() * j + 4 * i + 0);
			indices.add(4 * sheet.getCols() * j + 4 * i + 2);
		}
		// }
		this.sheet = sheet;
		mesh = new Mesh2D(TypeConverter.FloatListToArray(positions), TypeConverter.FloatListToArray(texcoords), TypeConverter.IntegerListToArray(indices));
		frame = 0;
	}

	public void delete() {
		mesh.delete();
	}

	public void render() {
		sheet.bind();
		mesh.bind();
		mesh.draw(GL_TRIANGLES, 6, 6 * frame);
		mesh.unbind();
		sheet.unbind();
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

}
