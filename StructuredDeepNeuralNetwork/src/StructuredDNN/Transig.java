package StructuredDNN;

public class Transig implements TransferFunction {

	@Override
	public Double execute(Double sum) {
		// These formulas are equivalent
		// return (Math.exp(sum) - Math.exp(-sum)) / (Math.exp(sum) +
		// Math.exp(-sum));
		
		// this is a tanh function
		// return 1 - 2 / (Math.exp(2 * sum) + 1);
		
		// hyperbolic tangent sigmoid transfer function
		return 2 / (1 + Math.exp(-2 * sum)) - 1;
	}

	@Override
	public Double executeDerivative(Double sum) {
		double tanh = this.execute(sum);

		return 1 - tanh * tanh;
		// return tanh;
		// return 1 - this.execute(sum) * this.execute(sum);
	}

}