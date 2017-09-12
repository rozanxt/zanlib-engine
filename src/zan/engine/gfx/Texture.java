package zan.engine.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class Texture {

	public static class Attributes {
		public int min_filter = GL_LINEAR;
		public int mag_filter = GL_LINEAR;
		public int wrap_s = GL_REPEAT;
		public int wrap_t = GL_REPEAT;
		public boolean mipmap = false;
	}

	private final int id;
	private final int width;
	private final int height;

	public Texture(ByteBuffer data, int width, int height, Attributes attr) {
		id = glGenTextures();
		this.width = width;
		this.height = height;
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		if (attr.mipmap) glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, attr.min_filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, attr.mag_filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, attr.wrap_s);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, attr.wrap_t);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public Texture(ByteBuffer data, int width, int height) {
		this(data, width, height, new Attributes());
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

	public static Texture loadFromFile(String path) {
		Texture texture = null;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			ByteBuffer data = stbi_load(path, w, h, c, 4);
			texture = new Texture(data, w.get(0), h.get(0));
			stbi_image_free(data);
		}
		return texture;
	}

}
