package StructuredDNN;

import java.util.ArrayList;

public class Layer {

    private static final double weightInitialMin = -.5;
    private static final double weightInitialMax = .5;

    private int neuronCount;
    private double learningRate;
    private double momentum;
    private TransferFunction transferFunction;

    private Layer previousLayer;

    private ArrayList<ArrayList<Double>> weights;

    private ArrayList<ArrayList<Double>> momentumOfWeights;

    public int weakConnection = 0, totalweights = 0;

    // used for Batch learning
    private ArrayList<ArrayList<Double>> weightChanges;

    private Layer(LayerDescription description, Layer previousLayer) {
        this.neuronCount = description.getNeuronCount();
        this.learningRate = description.getLearningRate();
        this.momentum = description.getMomentum();
        this.transferFunction = description.getTransferFunction();

        this.previousLayer = previousLayer;

        if (!isInputLayer()) {
            initWeights();
        }
    }

    public boolean isInputLayer() {
        return previousLayer == null;
    }

    public Layer(ArrayList<ArrayList<Double>> weights, Layer previousLayer) {
        this.weights = weights;
        this.previousLayer = previousLayer;

        this.neuronCount = weights.get(0).size();
    }

    public void setWeights(ArrayList<ArrayList<Double>> weights) {
        this.weights = weights;
    }

    private void initWeights() {

        RandomWeightGenerator.setParameters(weightInitialMin, weightInitialMax);

        weights = new ArrayList<ArrayList<Double>>(
                previousLayer.neuronCount + 1);
        // structured weight initialization
        if (this.neuronCount == 10 && previousLayer.neuronCount == 15) {
            for (int i = 0; i < previousLayer.neuronCount + 1; ++i) {
                ArrayList<Double> oneToMany = new ArrayList<Double>(
                        this.neuronCount);

                for (int j = 0; j < this.neuronCount; ++j) {
                    // /*
                    switch (j) {
                        case 0:
                            if (i == 0 || i == 4 || i == 5 || i == 7)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 1:
                            if (i == 0 || i == 1 || i == 2 || i == 4 || i == 7 || i == 12)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 2:
                            if (i == 0 || i == 1 || i == 2 || i == 8 || i == 10 || i == 11 || i == 12)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 3:
                            if (i == 0 || i == 5 || i == 6)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 4:
                            if (i == 0 || i == 8 || i == 10 || i == 11)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 5:
                            if (i == 0 || i == 1 || i == 2 || i == 3)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 6:
                            if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 7:
                            if (i == 0 || i == 9 || i == 13 || i == 15)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 8:
                            if (i == 0 || i == 6 || i == 9 || i == 13 || i == 14 || i == 15)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 9:
                            if (i == 0 || i == 1 || i == 2 || i == 12)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;


                        default:
                            oneToMany.add(0.);
                            break;
                    }
                    // */

                    // oneToMany.add(RandomWeightGenerator.getRandomValue());
                }

                weights.add(oneToMany);
            }
        }

        if (this.neuronCount == 7 && previousLayer.neuronCount == 10) {
            for (int i = 0; i < previousLayer.neuronCount + 1; ++i) {
                ArrayList<Double> oneToMany = new ArrayList<Double>(
                        this.neuronCount);

                for (int j = 0; j < this.neuronCount; ++j) {
                    // /*
                    switch (j) {
                        case 0:
                            if (i == 0 || i == 7 || i == 9)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 1:
                            if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 7)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 2:
                            if (i == 0 || i == 5 || i == 8)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 3:
                            if (i == 0 || i == 1 || i == 2 || i == 3 || i == 7)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 4:
                            if (i == 0 || i == 9)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 5:
                            if (i == 0 || i == 1 || i == 2 || i == 3 || i == 6 || i == 10)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;

                        case 6:
                            if (i == 0 || i == 8 || i == 9)
                                oneToMany.add(RandomWeightGenerator
                                        .getRandomValue());
                            else
                                oneToMany.add(0.);
                            break;


                        default:
                            oneToMany.add(0.);
                            break;
                    }

                }

                weights.add(oneToMany);
            }
        }

        if (this.neuronCount == 1) {
            for (int i = 0; i < previousLayer.neuronCount + 1; ++i) {
                ArrayList<Double> oneToMany = new ArrayList<Double>(
                        this.neuronCount);

                for (int j = 0; j < this.neuronCount; ++j) {
                    oneToMany.add(RandomWeightGenerator.getRandomValue());
                }

                weights.add(oneToMany);
            }
        }

        initMomentumOfWeights();
    }

    public void initWeightChanges() {
        this.weightChanges = new ArrayList<ArrayList<Double>>(
                previousLayer.neuronCount + 1);

        for (int i = 0; i < previousLayer.neuronCount + 1; ++i) {
            ArrayList<Double> oneToMany = new ArrayList<Double>(
                    this.neuronCount);

            for (int j = 0; j < this.neuronCount; ++j) {
                oneToMany.add(0.);
            }

            weightChanges.add(oneToMany);
        }

        initMomentumOfWeights();
    }

    private void initMomentumOfWeights() {
        this.momentumOfWeights = new ArrayList<ArrayList<Double>>(
                previousLayer.neuronCount + 1);

        for (int i = 0; i < previousLayer.neuronCount + 1; ++i) {
            ArrayList<Double> oneToMany = new ArrayList<Double>(
                    this.neuronCount);

            for (int j = 0; j < this.neuronCount; ++j) {
                oneToMany.add(0.);
            }

            momentumOfWeights.add(oneToMany);
        }
    }

    public Layer getPreviousLayer() {
        return this.previousLayer;
    }

    public int getNeuronCount() {
        return neuronCount;
    }

    public double getWeight(int i, int o) {
        return weights.get(i).get(o);
    }

    public void changeWeight(int i, int o, double difference) {
        Double currentWeight = weights.get(i).get(o);
        //weight change
        if (currentWeight != 0) {
            Double previousMomentum = momentumOfWeights.get(i).get(o);
            currentWeight += difference + previousMomentum;

            weights.get(i).set(o, currentWeight);

            momentumOfWeights.get(i).set(o, momentum * difference);
        }
    }

    public double getLearningRate() {
        return this.learningRate;
    }

    public double getMomentum() {
        return this.momentum;
    }

    public TransferFunction getTransferFunction() {
        return this.transferFunction;
    }

    public static Layer createInputLayer(LayerDescription description) {
        return new Layer(description, null);
    }

    public static Layer createLayer(LayerDescription description,
                                    Layer previousLayer) {
        return new Layer(description, previousLayer);
    }


    public ArrayList<Double> propagateLayer(ArrayList<Double> inputArray) {
        ArrayList<Double> outputArray = new ArrayList<Double>(this.neuronCount);

        // System.out.println(inputArray.size());
        for (int output = 0; output < this.neuronCount; ++output) {
            // add bias to sum
            double sum = this.getWeight(0, output);
            // input array doesn't contain bias value, where the weight array
            // does
            for (int input = 0; input < inputArray.size(); ++input) {
                sum += this.getWeight(input + 1, output)
                        * inputArray.get(input);
            }
            // System.out.println(sum);
            // outputArray.add(sum);

            outputArray.add(this.transferFunction.execute(sum));
        }

        return outputArray;
    }

    // calculate the weight difference
    public double getWeightDifference(double prevNeuronOutput,
                                      double nextNeuronDelta) {
        return this.learningRate * nextNeuronDelta * prevNeuronOutput;
    }

    @Override
    public String toString() {
        if (this.isInputLayer()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        ArrayList<Double> neuronWeights;

        for (int input = 0; input < this.weights.size(); input++) {
            neuronWeights = this.weights.get(input);
            for (int output = 0; output < neuronWeights.size(); output++) {
                sb.append("(i=" + input + ",o=" + (output + 1) + ")="
                        + String.format("%.4f", neuronWeights.get(output))
                        + "\t");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public double connections() {
        if (this.isInputLayer()) {
            return 0.;
        }

        ArrayList<Double> neuronWeights;

        for (int input = 0; input < this.weights.size(); input++) {
            neuronWeights = this.weights.get(input);
            for (int output = 0; output < neuronWeights.size(); output++) {

                if (neuronWeights.get(output) > 0. && neuronWeights.get(output) * neuronWeights.get(output) < .1 * .1) {
                    weakConnection++;
                }
            }
        }
        return weakConnection;
    }

    public double methodTotalWeights() {
        if (this.isInputLayer()) {
            return 0.;
        }

        ArrayList<Double> neuronWeights;

        for (int input = 0; input < this.weights.size(); input++) {
            neuronWeights = this.weights.get(input);
            for (int output = 0; output < neuronWeights.size(); output++) {
                if (neuronWeights.get(output) != 0.)
                    totalweights++;
            }
        }
        return totalweights;
    }

}