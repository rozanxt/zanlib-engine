package zan.lib.sfx;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_INITIAL;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_STOPPED;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetSourcef;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSource3f;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceRewind;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;

import org.joml.Vector3f;

public class SoundSource {

	private final int index;

	private float offset;

	public SoundSource(SoundData sound) {
		index = alGenSources();
		offset = 0.0f;
		alSourcei(index, AL_BUFFER, sound.getIndex());
	}

	public void delete() {
		alDeleteSources(index);
	}

	public void play() {
		alSourcePlay(index);
	}

	public void pause() {
		offset = alGetSourcef(index, AL_SEC_OFFSET);
		alSourceStop(index);
	}

	public void resume() {
		alSourcef(index, AL_SEC_OFFSET, offset);
		alSourcePlay(index);
	}

	public void stop() {
		offset = 0.0f;
		alSourceStop(index);
	}

	public void rewind() {
		alSourceRewind(index);
	}

	public void setSource(int property, boolean value) {
		alSourcei(index, property, value ? AL_TRUE : AL_FALSE);
	}

	public void setSource(int property, int value) {
		alSourcei(index, property, value);
	}

	public void setSource(int property, float value) {
		alSourcef(index, property, value);
	}

	public void setSource(int property, Vector3f value) {
		alSource3f(index, property, value.x, value.y, value.z);
	}

	public boolean isInitial() {
		return alGetSourcei(index, AL_SOURCE_STATE) == AL_INITIAL;
	}

	public boolean isPlaying() {
		return alGetSourcei(index, AL_SOURCE_STATE) == AL_PLAYING;
	}

	public boolean isPaused() {
		return offset > 0.0f && isStopped();
	}

	public boolean isStopped() {
		return alGetSourcei(index, AL_SOURCE_STATE) == AL_STOPPED;
	}

	public int getIndex() {
		return index;
	}

}
