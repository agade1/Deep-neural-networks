package StructuredDNN;

import java.util.Random;

/** Generate random integers in a certain range. */
public final class RandomRange {

	public static final void main(String... aArgs) {
		int binaryMask;
		Random random = new Random();
		long range = (long) 1 - (long) 0 + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * random.nextDouble());
		binaryMask = (int) (fraction + 0);
		System.out.print(binaryMask);
	}

}
