package zan.engine.gfx.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class SpriteTexture extends Texture {

	private final int rows;
	private final int cols;

	public SpriteTexture(ByteBuffer data, int width, int height, int minfilter, int magfilter, int rows, int cols) {
		super(data, width, height, minfilter, magfilter);
		this.rows = rows;
		this.cols = cols;
	}

	public SpriteTexture(ByteBuffer data, int width, int height, int rows, int cols) {
		this(data, width, height, GL_NEAREST, GL_NEAREST, rows, cols);
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public static SpriteTexture loadFromFile(String path, int rows, int cols) {
		SpriteTexture texture = null;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			ByteBuffer data = stbi_load(path, w, h, c, 4);
			texture = new SpriteTexture(data, w.get(), h.get(), rows, cols);
			stbi_image_free(data);
		}
		return texture;
	}

}
