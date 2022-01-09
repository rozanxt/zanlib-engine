package zan.lib.gfx;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture {

	public static class Attributes {
		public int min_filter = GL11.GL_LINEAR;
		public int mag_filter = GL11.GL_LINEAR;
		public int wrap_s = GL11.GL_REPEAT;
		public int wrap_t = GL11.GL_REPEAT;
		public boolean mipmap = false;
	}

	private final int id;
	private final int width;
	private final int height;

	public Texture(ByteBuffer data, int width, int height, Attributes attr) {
		id = GL11.glGenTextures();
		this.width = width;
		this.height = height;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		if (attr.mipmap) GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, attr.min_filter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, attr.mag_filter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, attr.wrap_s);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, attr.wrap_t);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public Texture(ByteBuffer data, int width, int height) {
		this(data, width, height, new Attributes());
	}

	public static Texture loadFromFile(String path) {
		Texture texture = null;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			ByteBuffer data = STBImage.stbi_load(path, w, h, c, 4);
			texture = new Texture(data, w.get(0), h.get(0));
			STBImage.stbi_image_free(data);
		}
		return texture;
	}

	public void delete() {
		GL11.glDeleteTextures(id);
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
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

}
