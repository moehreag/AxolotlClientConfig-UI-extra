package io.github.axolotlclient.AxolotlClientConfig.impl.util;

public class MathUtil {

	public static int clamp(int val, int min, int max){
		return val < min ? min : (Math.min(val, max));
	}

	public static double clamp(double val, double min, double max){
		return val < min ? min : (Math.min(val, max));
	}

	public static float clamp(float val, float min, float max){
		return val < min ? min : (Math.min(val, max));
	}

	public static int signum(int scroll) {
		return (int) Math.signum(scroll);
	}
}
