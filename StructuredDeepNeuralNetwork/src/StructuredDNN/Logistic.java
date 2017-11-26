package StructuredDNN;

public class Logistic implements TransferFunction {

	@Override
	public Double execute(Double sum) {
		return 1 / (1.0 + Math.exp(-sum));
	}

	@Override
	public Double executeDerivative(Double sum) {
		double logistic = this.execute(sum);

		return logistic * (1.0 - logistic);

		// return this.execute(sum) * (1.0 - this.execute(sum));
	}
}
