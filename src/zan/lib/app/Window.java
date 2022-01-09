package zan.lib.app;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Window {

	public static class Attributes {
		public String title = "";
		public String icon = "";

		public int x = GLFW.GLFW_DONT_CARE;
		public int y = GLFW.GLFW_DONT_CARE;

		public int width;
		public int height;

		public boolean fullscreen = false;
		public boolean vsync = true;
		public boolean resizable = true;
		public boolean decorated = true;
		public boolean focused = true;
		public boolean autoiconify = true;
		public boolean floating = false;
		public boolean maximized = false;
		public boolean minimized = false;
		public boolean visible = true;

		public int samples = 0;

		public Attributes(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	private final Attributes attr;

	private int width;
	private int height;

	private long handle;

	public Window(Attributes attr) {
		this.attr = attr;
	}

	public void init() {
		initHints();
		initWindow();
		initCallbacks();
		initContext();
		initFinish();
	}

	private void initHints() {
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, attr.resizable ? GL11.GL_TRUE : GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, attr.decorated ? GL11.GL_TRUE : GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, attr.focused ? GL11.GL_TRUE : GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_AUTO_ICONIFY, attr.autoiconify ? GL11.GL_TRUE : GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FLOATING, attr.floating ? GL11.GL_TRUE : GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, attr.maximized ? GL11.GL_TRUE : GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, attr.samples);
	}

	private void initWindow() {
		long monitor = GLFW.glfwGetPrimaryMonitor();
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(monitor);

		if (attr.fullscreen) {
			width = vidmode.width();
			height = vidmode.height();
			handle = GLFW.glfwCreateWindow(width, height, attr.title, monitor, MemoryUtil.NULL);
		} else {
			width = attr.width;
			height = attr.height;
			handle = GLFW.glfwCreateWindow(width, height, attr.title, MemoryUtil.NULL, MemoryUtil.NULL);
		}
		if (handle == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create the GLFW window!");
		}

		if (attr.x == GLFW.GLFW_DONT_CARE && attr.y == GLFW.GLFW_DONT_CARE) {
			attr.x = (vidmode.width() - attr.width) / 2;
			attr.y = (vidmode.height() - attr.height) / 2;
		}
		GLFW.glfwSetWindowPos(handle, attr.x, attr.y);

		setIcon(attr.icon);
	}

	private void initCallbacks() {
		GLFW.glfwSetWindowPosCallback(handle, (window, x, y) -> {
			if (!attr.fullscreen) {
				attr.x = x;
				attr.y = y;
			}
		});
		GLFW.glfwSetWindowSizeCallback(handle, (window, width, height) -> {
			if (!attr.fullscreen) {
				attr.width = width;
				attr.height = height;
			}
		});
		GLFW.glfwSetFramebufferSizeCallback(handle, (window, width, height) -> {
			this.width = width;
			this.height = height;
		});
	}

	private void initContext() {
		GLFW.glfwMakeContextCurrent(handle);
		GLFW.glfwSwapInterval(attr.vsync ? 1 : 0);
		GL.createCapabilities();
	}

	private void initFinish() {
		if (attr.minimized) GLFW.glfwIconifyWindow(handle);
		if (attr.visible) GLFW.glfwShowWindow(handle);
	}

	public void refresh() {
		GLFW.glfwSwapBuffers(handle);
		GLFW.glfwPollEvents();
	}

	public void exit() {
		Callbacks.glfwFreeCallbacks(handle);
		GLFW.glfwDestroyWindow(handle);
	}

	public void setTitle(String title) {
		attr.title = title;
		GLFW.glfwSetWindowTitle(handle, title);
	}

	public String getTitle() {
		return attr.title;
	}

	public void setIcon(String icon) {
		attr.icon = icon;
		if (!icon.isEmpty()) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				IntBuffer w = stack.mallocInt(1);
				IntBuffer h = stack.mallocInt(1);
				IntBuffer c = stack.mallocInt(1);
				ByteBuffer ico = STBImage.stbi_load(icon, w, h, c, 4);
				GLFW.glfwSetWindowIcon(handle, GLFWImage.malloc(1, stack).width(w.get(0)).height(h.get(0)).pixels(ico));
				STBImage.stbi_image_free(ico);
			}
		}
	}

	public String getIcon() {
		return attr.icon;
	}

	public void setPos(int x, int y) {
		GLFW.glfwSetWindowPos(handle, x, y);
	}

	public int getX() {
		return attr.x;
	}

	public int getY() {
		return attr.y;
	}

	public void setSize(int width, int height) {
		GLFW.glfwSetWindowSize(handle, width, height);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setFullScreen(boolean fullscreen) {
		attr.fullscreen = fullscreen;
		long monitor = GLFW.glfwGetPrimaryMonitor();
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(monitor);
		if (fullscreen) {
			GLFW.glfwSetWindowMonitor(handle, monitor, 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
		} else {
			GLFW.glfwSetWindowMonitor(handle, MemoryUtil.NULL, attr.x, attr.y, attr.width, attr.height, vidmode.refreshRate());
		}
		GLFW.glfwSwapInterval(attr.vsync ? 1 : 0);
		attr.fullscreen = (GLFW.glfwGetWindowMonitor(handle) == monitor);
	}

	public boolean isFullScreen() {
		return attr.fullscreen;
	}

	public void setVSync(boolean vsync) {
		attr.vsync = vsync;
		GLFW.glfwSwapInterval(vsync ? 1 : 0);
	}

	public boolean isVSync() {
		return attr.vsync;
	}

	public void close() {
		GLFW.glfwSetWindowShouldClose(handle, true);
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(handle);
	}

	public long getHandle() {
		return handle;
	}

}
