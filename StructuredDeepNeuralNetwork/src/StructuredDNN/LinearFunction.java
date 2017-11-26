package StructuredDNN;

public class LinearFunction implements TransferFunction {

	@Override
	public Double execute(Double sum) {
		return sum;
	}

	@Override
	public Double executeDerivative(Double sum) {
		//return sum;
		return 1.0;
	}

}
