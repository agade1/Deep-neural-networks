package StructuredDNN;

public class LayerDescription {
	private final int neuronCount;
	private final double learningRate;
	private final double momentum;	
	private final TransferFunction transferFunction;

	public int getNeuronCount() {
		return neuronCount;
	}
	public Integer getNeuronCountAsInteger() { 
		return this.neuronCount;
	}
	public double getLearningRate() {
		return learningRate;
	}
	public double getMomentum() {
		return momentum;
	}
	public TransferFunction getTransferFunction() {
		return transferFunction;
	}
	
	public LayerDescription(int neuronCount) {
		this(neuronCount, 0, 0, new LinearFunction());
	}
	
	public LayerDescription(int neuronCount, double learningRate, double momentum, TransferFunction transferFunction) {
		this.neuronCount = neuronCount;
		this.learningRate = learningRate;
		this.momentum = momentum;
		this.transferFunction = transferFunction;
	}
}
