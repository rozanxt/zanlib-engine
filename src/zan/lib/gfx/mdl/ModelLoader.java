package zan.lib.gfx.mdl;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import zan.lib.utl.Utility;

public final class ModelLoader {

	private static class Index {

		private static final int NO_VALUE = -1;

		private int pos = NO_VALUE;
		private int tex = NO_VALUE;
		private int nrm = NO_VALUE;

	}

	private static class Face {

		private Index[] indices = new Index[3];

		private Face(String t0, String t1, String t2) {
			indices[0] = parseToken(t0);
			indices[1] = parseToken(t1);
			indices[2] = parseToken(t2);
		}

		private Index parseToken(String token) {
			Index index = new Index();
			String[] items = token.split("/");
			int length = items.length;
			index.pos = Integer.parseInt(items[0]) - 1;
			if (length > 1) {
				index.tex = items[1].length() > 0 ? Integer.parseInt(items[1]) - 1 : Index.NO_VALUE;
				if (length > 2) {
					index.nrm = Integer.parseInt(items[2]) - 1;
				}
			}
			return index;
		}

	}

	private ModelLoader() {

	}

	public static ModelMesh loadFromFile(String path) {
		return parseOBJ(Utility.loadFromFileAsStringList(path));
	}

	public static ModelMesh parseOBJ(List<String> data) {
		List<Vector3f> positions = new ArrayList<>();
		List<Vector2f> texcoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();

		for (String line : data) {
			String[] tokens = line.split("\\s+");
			switch (tokens[0]) {
			case "v":
				positions.add(new Vector3f(
					Float.parseFloat(tokens[1]),
					Float.parseFloat(tokens[2]),
					Float.parseFloat(tokens[3])));
				break;
			case "vt":
				texcoords.add(new Vector2f(
					Float.parseFloat(tokens[1]),
					1.0f - Float.parseFloat(tokens[2])));
				break;
			case "vn":
				normals.add(new Vector3f(
					Float.parseFloat(tokens[1]),
					Float.parseFloat(tokens[2]),
					Float.parseFloat(tokens[3])));
				break;
			case "f":
				faces.add(new Face(tokens[1], tokens[2], tokens[3]));
				break;
			}
		}

		List<ModelMesh.Vertex> vertices = new ArrayList<>();

		for (Face face : faces) {
			for (Index index : face.indices) {
				Vector3f pos = positions.get(index.pos);
				Vector2f tex = index.tex != Index.NO_VALUE ? texcoords.get(index.tex) : new Vector2f();
				Vector3f nrm = index.nrm != Index.NO_VALUE ? normals.get(index.nrm) : new Vector3f();
				vertices.add(new ModelMesh.Vertex(pos, tex, nrm));
			}
		}

		return new ModelMesh(vertices);
	}

}
