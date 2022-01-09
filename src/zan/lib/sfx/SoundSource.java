package zan.lib.sfx;

import org.joml.Vector3f;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public class SoundSource {

	private final int id;

	private float offset;

	public SoundSource(SoundData sound) {
		id = AL10.alGenSources();
		offset = 0.0f;
		AL10.alSourcei(id, AL10.AL_BUFFER, sound.getID());
	}

	public void delete() {
		AL10.alDeleteSources(id);
	}

	public void play() {
		AL10.alSourcePlay(id);
	}

	public void pause() {
		offset = AL10.alGetSourcef(id, AL11.AL_SEC_OFFSET);
		AL10.alSourceStop(id);
	}

	public void resume() {
		AL10.alSourcef(id, AL11.AL_SEC_OFFSET, offset);
		AL10.alSourcePlay(id);
	}

	public void stop() {
		offset = 0.0f;
		AL10.alSourceStop(id);
	}

	public void rewind() {
		AL10.alSourceRewind(id);
	}

	public void setProperty(int property, boolean value) {
		AL10.alSourcei(id, property, value ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public void setProperty(int property, int value) {
		AL10.alSourcei(id, property, value);
	}

	public void setProperty(int property, float value) {
		AL10.alSourcef(id, property, value);
	}

	public void setProperty(int property, Vector3f value) {
		AL10.alSource3f(id, property, value.x, value.y, value.z);
	}

	public boolean isInitial() {
		return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE) == AL10.AL_INITIAL;
	}

	public boolean isPlaying() {
		return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public boolean isPaused() {
		return ((offset > 0.0f) && isStopped());
	}

	public boolean isStopped() {
		return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED;
	}

	public int getID() {
		return id;
	}

}
