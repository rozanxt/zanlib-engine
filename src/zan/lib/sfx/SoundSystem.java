package zan.lib.sfx;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;

public final class SoundSystem {

	private static long device;
	private static long context;

	private SoundSystem() {

	}

	public static void init() {
		device = ALC10.alcOpenDevice(ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER));
		context = ALC10.alcCreateContext(device, new int[] { 0 });
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(ALC.createCapabilities(device));
	}

	public static void exit() {
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}

	public static void setDistanceModel(int model) {
		AL10.alDistanceModel(model);
	}

	public static void setDopplerFactor(float factor) {
		AL10.alDopplerFactor(factor);
	}

	public static void setSoundSpeed(float speed) {
		AL11.alSpeedOfSound(speed);
	}

	public static void setListenerPosition(Vector3f position) {
		AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
	}

	public static void setListenerVelocity(Vector3f velocity) {
		AL10.alListener3f(AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}

	public static void setListenerOrientation(Vector3f at, Vector3f up) {
		float[] orientation = new float[6];
		orientation[0] = at.x;
		orientation[1] = at.y;
		orientation[2] = at.z;
		orientation[3] = up.x;
		orientation[4] = up.y;
		orientation[5] = up.z;
		AL10.alListenerfv(AL10.AL_ORIENTATION, orientation);
	}

}
