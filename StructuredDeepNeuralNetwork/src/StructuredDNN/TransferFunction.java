package StructuredDNN;

public interface TransferFunction {
	Double execute(Double sum);

	Double executeDerivative(Double sum);
}