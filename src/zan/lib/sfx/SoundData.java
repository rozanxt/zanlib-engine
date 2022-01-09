package zan.lib.sfx;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

public class SoundData {

	private final int id;
	private final int format;
	private final int sampling;

	public SoundData(ShortBuffer data, int format, int sampling) {
		id = AL10.alGenBuffers();
		this.format = format;
		this.sampling = sampling;
		AL10.alBufferData(id, format, data, sampling);
	}

	public static SoundData loadFromFile(String path) {
		SoundData sound = null;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer c = stack.mallocInt(1);
			IntBuffer s = stack.mallocInt(1);
			ShortBuffer data = STBVorbis.stb_vorbis_decode_filename(path, c, s);
			int channels = c.get();
			int sampling = s.get();
			int format = -1;
			if (channels == 1) {
				format = AL10.AL_FORMAT_MONO16;
			} else if (channels == 2) {
				format = AL10.AL_FORMAT_STEREO16;
			}
			sound = new SoundData(data, format, sampling);
			LibCStdlib.free(data);
		}
		return sound;
	}

	public void delete() {
		AL10.alDeleteBuffers(id);
	}

	public int getID() {
		return id;
	}

	public int getFormat() {
		return format;
	}

	public int getSampling() {
		return sampling;
	}

}
