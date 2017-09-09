package zan.engine.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class TextureData {

	protected final int id;
	protected final int width;
	protected final int height;

	public TextureData(ByteBuffer data, int width, int height) {
		id = glGenTextures();
		this.width = width;
		this.height = height;
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void delete() {
		glDeleteTextures(id);
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static TextureData loadFromFile(String path) {
		TextureData texture = null;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			ByteBuffer data = stbi_load(path, w, h, c, 4);
			texture = new TextureData(data, w.get(), h.get());
			stbi_image_free(data);
		}
		return texture;
	}

}
