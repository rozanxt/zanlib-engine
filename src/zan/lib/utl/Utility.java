package zan.lib.utl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.MemoryUtil;

public final class Utility {

	private Utility() {

	}

	public static float lerp(float a, float b, float theta) {
		return a + theta * (b - a);
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
