package zan.engine.sfx;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

import org.joml.Vector3f;

public class SoundSource {

	private final int id;

	private float offset;

	public SoundSource(SoundData sound) {
		id = alGenSources();
		offset = 0.0f;
		alSourcei(id, AL_BUFFER, sound.getID());
	}

	public void delete() {
		alDeleteSources(id);
	}

	public void play() {
		alSourcePlay(id);
	}

	public void pause() {
		offset = alGetSourcef(id, AL_SEC_OFFSET);
		alSourceStop(id);
	}

	public void resume() {
		alSourcef(id, AL_SEC_OFFSET, offset);
		alSourcePlay(id);
	}

	public void stop() {
		offset = 0.0f;
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

	public boolean isInitial() {
		return alGetSourcei(id, AL_SOURCE_STATE) == AL_INITIAL;
	}

	public boolean isPlaying() {
		return alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING;
	}

	public boolean isPaused() {
		return ((offset > 0.0f) && isStopped());
	}

	public boolean isStopped() {
		return alGetSourcei(id, AL_SOURCE_STATE) == AL_STOPPED;
	}

	public int getID() {
		return id;
	}

}
