package zan.lib.gfx.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class Texture {

	public static class Attributes {

		public boolean mipmap = false;

		public int min_filter = GL_LINEAR;
		public int mag_filter = GL_LINEAR;

		public int wrap_s = GL_REPEAT;
		public int wrap_t = GL_REPEAT;

	}

	private final int index;

	private final int width;
	private final int height;

	public Texture(ByteBuffer data, int width, int height, Attributes attrib) {
		this.width = width;
		this.height = height;
		index = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, index);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		if (attrib.mipmap) glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, attrib.min_filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, attrib.mag_filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, attrib.wrap_s);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, attrib.wrap_t);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public Texture(ByteBuffer data, int width, int height) {
		this(data, width, height, new Attributes());
	}

	public static Texture loadFromFile(String path, Attributes attrib) {
		Texture texture = null;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			ByteBuffer data = stbi_load(path, w, h, c, 4);
			texture = new Texture(data, w.get(0), h.get(0), attrib);
			stbi_image_free(data);
		}
		return texture;
	}

	public static Texture loadFromFile(String path) {
		return loadFromFile(path, new Attributes());
	}

	public void delete() {
		glDeleteTextures(index);
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, index);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getIndex() {
		return index;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
