package zan.engine.sfx;

import static org.lwjgl.openal.AL10.*;

import org.joml.Vector3f;

public class SoundSource {

	private final int id;

	public SoundSource(SoundData sound) {
		id = alGenSources();
		alSourcei(id, AL_BUFFER, sound.getID());
	}

	public void delete() {
		alDeleteSources(id);
	}

	public void play() {
		alSourcePlay(id);
	}

	public void pause() {
		alSourcePause(id);
	}

	public void stop() {
		alSourceStop(id);
	}

	public void rewind() {
		alSourceRewind(id);
	}

	public void setProperty(int property, boolean value) {
		alSourcei(id, property, value ? AL_TRUE : AL_FALSE);
	}

	public void setProperty(int property, float value) {
		alSourcef(id, property, value);
	}

	public void setProperty(int property, Vector3f value) {
		alSource3f(id, property, value.x, value.y, value.z);
	}

	public int getState() {
		return alGetSourcei(id, AL_SOURCE_STATE);
	}

	public int getID() {
		return id;
	}

}
