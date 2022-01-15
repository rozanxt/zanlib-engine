package zan.lib.sfx;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

public class SoundData {

	private final int index;

	private final int format;
	private final int sample;

	public SoundData(ShortBuffer data, int format, int sample) {
		index = alGenBuffers();
		this.format = format;
		this.sample = sample;
		alBufferData(index, format, data, sample);
	}

	public static SoundData loadFromFile(String path) {
		SoundData sound = null;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer c = stack.mallocInt(1);
			IntBuffer s = stack.mallocInt(1);
			ShortBuffer data = stb_vorbis_decode_filename(path, c, s);
			int channels = c.get();
			int sampling = s.get();
			int format = -1;
			if (channels == 1) {
				format = AL_FORMAT_MONO16;
			} else if (channels == 2) {
				format = AL_FORMAT_STEREO16;
			}
			sound = new SoundData(data, format, sampling);
			LibCStdlib.free(data);
		}
		return sound;
	}

	public void delete() {
		alDeleteBuffers(index);
	}

	public int getIndex() {
		return index;
	}

	public int getFormat() {
		return format;
	}

	public int getSampleRate() {
		return sample;
	}

}
