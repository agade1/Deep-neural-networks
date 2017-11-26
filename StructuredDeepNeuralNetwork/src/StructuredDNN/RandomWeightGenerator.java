package StructuredDNN;

import java.util.Random;

public class RandomWeightGenerator {

	private static Random generator = new Random(0);
	private static double rangeMin = -1;
	private static double rangeMax = 1;

	public static void setParameters(double min, double max) {
		rangeMin = min;
		rangeMax = max;
	}

	public static double getRandomValue() {
		return (rangeMax - rangeMin) * generator.nextDouble() + rangeMin;
	}
}