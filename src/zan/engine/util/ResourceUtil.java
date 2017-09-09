package zan.engine.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ResourceUtil {

	private ResourceUtil() {

	}

	public static String getTextResourceAsString(String path) {
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

	public static List<String> getTextResourceAsStringList(String path) {
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
