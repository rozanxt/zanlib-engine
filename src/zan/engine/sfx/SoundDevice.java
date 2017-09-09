package zan.engine.sfx;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.ALC10.*;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

public class SoundDevice {

	private final long device;
	private final long context;

	public SoundDevice() {
		device = alcOpenDevice(alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER));
		context = alcCreateContext(device, new int[] { 0 });
		alcMakeContextCurrent(context);
		AL.createCapabilities(ALC.createCapabilities(device));
	}

	public void delete() {
		alcDestroyContext(context);
		alcCloseDevice(device);
	}

	public void setDistanceModel(int model) {
		alDistanceModel(model);
	}

	public void setDopplerFactor(float factor) {
		alDopplerFactor(factor);
	}

	public void setSoundSpeed(float speed) {
		alSpeedOfSound(speed);
	}

	public void setListenerPosition(Vector3f position) {
		alListener3f(AL_POSITION, position.x, position.y, position.z);
	}

	public void setListenerVelocity(Vector3f velocity) {
		alListener3f(AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}

	public void setListenerOrientation(Vector3f at, Vector3f up) {
		float[] orientation = new float[6];
		orientation[0] = at.x;
		orientation[1] = at.y;
		orientation[2] = at.z;
		orientation[3] = up.x;
		orientation[4] = up.y;
		orientation[5] = up.z;
		alListenerfv(AL_ORIENTATION, orientation);
	}

}
