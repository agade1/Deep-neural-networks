package StructuredDNN;

import java.util.ArrayList;

public class Pattern {
	public ArrayList<Double> x;
	public ArrayList<Double> y;
	public ArrayList<Double> z;
	
	public Pattern() {
		x = new ArrayList<Double>(StructuredDeepNeuralNetwork.MAX_NEURONS);
		y = new ArrayList<Double>(StructuredDeepNeuralNetwork.MAX_NEURONS);
		z = new ArrayList<Double>(StructuredDeepNeuralNetwork.MAX_NEURONS);
	}
	public int getInputSize() {
		return this.x.size();
	}
	
	public int getOutputSize(){
		return this.y.size();
	}
}