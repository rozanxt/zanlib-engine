package zan.lib.utl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryUtil;

public final class Utility {

	public static final float eps = 1e-6f;

	private Utility() {

	}

	public static float lerp(float prev, float next, float theta) {
		return prev + theta * (next - prev);
	}

	public static Vector2f lerp(Vector2fc prev, Vector2fc next, float theta) {
		return new Vector2f(prev).lerp(next, theta);
	}

	public static Vector3f lerp(Vector3fc prev, Vector3fc next, float theta) {
		return new Vector3f(prev).lerp(next, theta);
	}

	public static Vector4f lerp(Vector4fc prev, Vector4fc next, float theta) {
		return new Vector4f(prev).lerp(next, theta);
	}

	public static float slerp(float prev, float next, float period, float theta) {
		if (Math.abs(next - prev) > period / 2.0f) {
			if (next > prev) {
				prev += period;
			} else {
				next += period;
			}
		}
		return lerp(prev, next, theta) % period;
	}

	public static Vector2f slerp(Vector2fc prev, Vector2fc next, float theta) {
		Vector2fc a = new Vector2f(prev).normalize();
		Vector2fc b = new Vector2f(next).normalize();
		float dot = a.dot(b);
		float len = (float) (theta * Math.acos(dot));
		Vector2fc u;
		if (Math.abs(dot) > 1.0f - eps) {
			u = new Vector2f(-a.y(), a.x());
		} else {
			u = new Vector2f(b).sub(new Vector2f(a).mul(dot)).normalize();
		}
		return new Vector2f(a).mul((float) Math.cos(len)).add(new Vector2f(u).mul((float) Math.sin(len))).normalize();
	}

	public static Vector3f slerp(Vector3fc prev, Vector3fc next, float theta) {
		Vector3fc a = new Vector3f(prev).normalize();
		Vector3fc b = new Vector3f(next).normalize();
		float dot = a.dot(b);
		float len = (float) (theta * Math.acos(dot));
		Vector3fc u;
		if (Math.abs(dot) > 1.0f - eps) {
			u = new Vector3f(-a.y(), a.x(), a.z());
		} else {
			u = new Vector3f(b).sub(new Vector3f(a).mul(dot)).normalize();
		}
		return new Vector3f(a).mul((float) Math.cos(len)).add(new Vector3f(u).mul((float) Math.sin(len))).normalize();
	}

	public static Vector4f slerp(Vector4fc prev, Vector4fc next, float theta) {
		Vector4fc a = new Vector4f(prev).normalize();
		Vector4fc b = new Vector4f(next).normalize();
		float dot = a.dot(b);
		float len = (float) (theta * Math.acos(dot));
		Vector4fc u;
		if (Math.abs(dot) > 1.0f - eps) {
			u = new Vector4f(-a.y(), a.x(), a.z(), a.w());
		} else {
			u = new Vector4f(b).sub(new Vector4f(a).mul(dot)).normalize();
		}
		return new Vector4f(a).mul((float) Math.cos(len)).add(new Vector4f(u).mul((float) Math.sin(len))).normalize();
	}

	public static int[] IntegerListToArray(List<Integer> list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static float[] FloatListToArray(List<Float> list) {
		float[] array = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static ByteBuffer BufferedImageToByteBuffer(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		ByteBuffer data = MemoryUtil.memAlloc(width * height * Integer.BYTES);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = pixels[width * j + i];
				data.put((byte) ((pixel >> 16) & 0xFF));
				data.put((byte) ((pixel >> 8) & 0xFF));
				data.put((byte) (pixel & 0xFF));
				data.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		data.flip();
		return data;
	}

	public static String loadFromFileAsString(String path) {
		StringBuilder text = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = reader.readLine()) != null) {
				text.append(line).append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text.toString();
	}

	public static List<String> loadFromFileAsStringList(String path) {
		List<String> text = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			while ((line = reader.readLine()) != null) {
				text.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

}
