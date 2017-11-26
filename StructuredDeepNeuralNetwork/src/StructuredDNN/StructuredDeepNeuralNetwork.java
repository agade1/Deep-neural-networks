package StructuredDNN;

/**
 * Structured Deep neural neural network for predictive analytics
 * Developed by:
 * Amol Gade
 * Computer and information science department
 * University of Massachusetts Dartmouth
 * USA
 * <p>
 * Development year: 2015-2016
 * <p>
 * Use case: House selling price prediction
 * ABSTRACT: With the rapid growth of global data, predictive analytics has become ever important.
 * Predictive analytics can help people in many different ways including making better decisions,
 * understanding market trends, and improving productivity. Conventional machine learning techniques
 * used for predictive analytics, such as regression techniques, are typically not sufficient to handle
 * complex data associated with structured knowledge. Deep learning, also called deep structured learning,
 * has received great attentions in recent years for modeling high-level abstractions in data with complex
 * structures. In this thesis, we propose a systematic approach to deriving a layered knowledge structure
 * and designing a structured deep neural network based on it. Neurons in a structured deep neural network
 * are structurally connected, which makes the network time and space efficient, and also requires fewer
 * data points for training. Furthermore, the proposed model can significantly reduce chances of overfitting,
 * which has been one of the most common and difficult to solve problems in machine learning. The structured
 * deep neural network model has been designed to learn from the most recently captured data points; therefore,
 * it allows the model to adapt to the latest market trends. To demonstrate the effectiveness of the proposed
 * approach for predictive analytics, we use a case study of predicting house selling prices.
 * A deep neural network has been designed to match with a knowledge structure for house price prediction,
 * with a significantly reduced number of connections comparing to a fully connected neural network.
 * Our experimental results show that a specialized structured deep neural network may outperform conventional
 * multivariate linear regression models, as well as fully connected deep neural networks.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class StructuredDeepNeuralNetwork {

    // Restriction constants
    public static int MAX_NEURONS = 1000;
    public static int MIN_LAYERS = 2;
    public static int MAX_LAYERS = 5;

    // Configuration constants
    public static int EPOCHS = 10;

    // Each LayerDescription includes:
    // Neuron Count, Learning Rate, Momentum, Transfer Function
    // First short description initializes default values for input layer
    public static LayerDescription[] STRUCTURE = {new LayerDescription(15),
            new LayerDescription(10, 0.01, .01, new Transig()),
            new LayerDescription(7, 0.01, 0.9, new Transig()),
            new LayerDescription(1)};

    private ArrayList<LayerDescription> structure;
    private ArrayList<Pattern> patterns;
    private ArrayList<Layer> layers;
    // 'expectedOutputs' is used only for a current pattern while learning
    private ArrayList<Double> expectedOutputs;
    private ArrayList<Double> learningCurve;
    stats stats = new stats();
    ReadPatterns readPatterns = new ReadPatterns();

    public StructuredDeepNeuralNetwork(LayerDescription[] structure) throws Exception {

        // Check neural network structure - number of layers and number of
        // neurons in each layer
        this.structure = new ArrayList<LayerDescription>(
                Arrays.asList(structure));

        if (structure.length < MIN_LAYERS || structure.length > MAX_LAYERS) {
            throw new Exception("Illegal structure!");
        }

        // Initialize structure and weights
        this.patterns = new ArrayList<Pattern>();
        this.layers = new ArrayList<Layer>(structure.length);
        this.learningCurve = new ArrayList<Double>();

        Layer layer = null;

        for (int i = 0; i < structure.length; i++) {
            checkLayerDescription(structure[i]);
            // Initialize layers
            layer = Layer.createLayer(structure[i], layer);
            this.layers.add(layer);
        }
    }

    private void checkLayerDescription(LayerDescription description)
            throws Exception {
        if (description.getNeuronCount() < 1
                || description.getNeuronCount() > MAX_NEURONS
                || description.getLearningRate() < 0.0
                || description.getLearningRate() > 1.0
                || description.getMomentum() < 0.0
                || description.getMomentum() > 1.0
                || description.getTransferFunction() == null) {
            throw new Exception("Illegal structure!");
        }
    }

    public ArrayList<Pattern> getPatterns() {
        return patterns;
    }

    public int getSize() {
        return this.structure.size();
    }

    double predictionError = 0.;
    int intSoldDate = 0, predictedSellingPrice, testSetSize = 0;
    ArrayList<Double> percentPredictionErrorDNN = new ArrayList<Double>();
    ArrayList<Double> percentPredictionErrorSQFT = new ArrayList<Double>();

    double numberOfWeakConnections = 0., totalConnections = 0.;
    // Prints results to a file
    PrintWriter outputPrinter = new PrintWriter("nallData/results", "UTF-8");
    // prints error % to a file
    PrintWriter errorPrinter = new PrintWriter("nallData/predictionError", "UTF-8");

    // The main method - Program entry point
    public static void main(String[] args) throws Exception {

        StructuredDeepNeuralNetwork mlp = new StructuredDeepNeuralNetwork(STRUCTURE);

        // reads training data
        mlp.readTrainingPatterns("Data/TrainingDataset");


        long start = System.nanoTime();

        // Enters testing phase (training method is called inside testing method)
        mlp.testing();

        // Prints neural network connection weights to the console
        mlp.printWeights();

        // Prints results statistics to the console
        mlp.statistics();

        mlp.getConnections();
        mlp.getTotalWeights();
        // Time taken for the training and testing
        long elapsedTime = System.nanoTime() - start;
        //System.out.println(" Time taken" + elapsedTime / 1000000000 + " Seconds");
    }

    // testing
    public void testing() throws NumberFormatException,
            IOException {
        Pattern testPattern;

        // Reads test set from the file
        BufferedReader br = new BufferedReader(new FileReader(
                "Data/TestingDataset"));

        String line;
        String[] buff;
        int i;
        try {
            // read pattern lines
            while ((line = br.readLine()) != null) {
                double value = 0.;
                buff = line.split(",");

                testPattern = readPatterns.readPattern(buff);

                /** train the neural network iteratively before testing each new
                 data point
                 @return: price per square feet rate of recently sold houses in the database
                 */
                int priceSqft = this.startTraining();

                //System.out.println("Mean Price per SQFT " + priceSqft);

                // check if outlier
                if ((priceSqft - (int) (Double.valueOf(buff[0]) / Double.valueOf(buff[3]))) < .20 * priceSqft
                        && (priceSqft - (int) (Double.valueOf(buff[0]) / Double.valueOf(buff[3]))) > -.20
                        * priceSqft) {

                    // test the data point, here predictedSellingPrice is the predicted price
                    // by the neural network
                    predictedSellingPrice = this.propagateAndSave(testPattern.x);

                    intSoldDate = Integer.valueOf(buff[7]);
                    testSetSize++;

                    // print result of the current prediction to the console
                    this.printResults(buff, priceSqft);

                    // add this newly tested data point to the training data set
                    // for real-time self adaptive training
                    this.patterns.add(testPattern);

                }
            }

        } finally {
            br.close();
            outputPrinter.close();
            errorPrinter.close();
        }
    }

    // prints results
    public void printResults(String[] buff,
                             int priceSqft) throws FileNotFoundException,
            UnsupportedEncodingException {

        int expectedOutputs = Integer.valueOf(buff[0]);
        System.out.println("Input is : " + Arrays.toString(buff));
        System.out.println("Expected Output is: " + expectedOutputs / 1000
                + "K");
        outputPrinter.write(String.valueOf(expectedOutputs / 1000));
        outputPrinter.write(",");
        System.out.println("Price/SQFT Output is=> "
                + (int) (Double.valueOf(buff[3]) * priceSqft / 1000) + "K");
        outputPrinter.write(String.valueOf((int) (Double.valueOf(buff[3]) * priceSqft / 1000)));
        outputPrinter.write(",");
        double predictionErrorSQFT = (((Double.valueOf(buff[0]) - Double.valueOf(buff[3]) * priceSqft) * 100) / Double.valueOf(buff[0]));
        if (predictionErrorSQFT < 0) {
            predictionErrorSQFT = predictionErrorSQFT * -1;
        }
        System.out.println("NN Predicted Output is=> " + predictedSellingPrice / 1000 + "K");
        outputPrinter.write(String.valueOf(predictedSellingPrice / 1000) + "\n");
        predictionError = Math.sqrt(Math.pow(expectedOutputs - predictedSellingPrice, 2));
        System.out.println("Difference ==>"
                + Math.round((expectedOutputs - predictedSellingPrice) / 1000) + "K");
        predictionError = (predictionError / expectedOutputs) * 100;

        errorPrinter.write(predictionError + "\n");
        percentPredictionErrorSQFT.add(predictionErrorSQFT);

        System.out.println("% Difference>" + Math.round(predictionError) + "%\n");
        percentPredictionErrorDNN.add(predictionError);

    }

    // prints statistics to the console
    public void statistics() {

        stats.printStats(percentPredictionErrorSQFT, percentPredictionErrorDNN, testSetSize);
    }

    // start training the neural network with each pattern

    public int startTraining() {
        ArrayList<Double> deltas;
        boolean flag = true;
        Double currentSoldDate;
        int windowSize = 90;

        // remove old data points from the training data set
        for (int i = 0; i < this.patterns.size(); i++) {
            Pattern pattern = this.patterns.get(i);
            currentSoldDate = pattern.z.get(0);

            if (intSoldDate - currentSoldDate > windowSize) {
                this.patterns.remove(pattern);
                i--;
            }
        }

        // get average price per SQFT
        int priceSqft = 0;
        for (int i = 0; i < this.patterns.size(); i++) {
            Pattern pattern = this.patterns.get(i);
            currentSoldDate = (pattern.y.get(0) * (1500000 - 52500) + 52500)
                    / (pattern.x.get(2) * (5000 - 300) + 300);
            priceSqft += currentSoldDate;
        }

        priceSqft = priceSqft / this.patterns.size();

        // training
        for (int j = 0; j < EPOCHS; j++) {
            while (flag) {
                for (Pattern pattern : this.patterns) {

                    this.expectedOutputs = pattern.y;

                    deltas = training(pattern.x, 1);

                    // change the weights between the input and the first hidden
                    // layer
                    if (deltas != null) {
                        changeWeights(pattern.x, deltas, 1);
                    }
                }

                // shuffle patterns to avoid cycled training
                Collections.shuffle(this.patterns);

                // calculate avg error
                double avg = 0;
                for (int i1 = 0, ii = this.learningCurve.size(); i1 < ii; i1++) {
                    avg += this.learningCurve.get(i1);
                }

                avg = avg / this.learningCurve.size();
                System.out.println("Learning..");

                // threshold
                if (avg < 11) {
                    flag = false;
                }

                this.learningCurve.clear();
            }
        }

        this.expectedOutputs = null;
        return priceSqft;
    }

    // recursive function, returns ArrayList of deltas
    private ArrayList<Double> training(ArrayList<Double> inputs,
                                       int layerIndex) {
        ArrayList<Double> outputs;
        ArrayList<Double> deltas, nextLayerDeltas;

        // calculate outputs of the neurons in the layer
        outputs = layers.get(layerIndex).propagateLayer(inputs);

        // check if hidden layer
        if (layerIndex < this.getSize() - 1) {
            // recursively call the function to get deltas of the next layer
            nextLayerDeltas = training(outputs, layerIndex + 1);

            // calculate deltas of the current layer
            if (nextLayerDeltas != null) {
                deltas = calculateDeltas(outputs, layerIndex + 1,
                        nextLayerDeltas);

                // change the weights between this layer and the next one
                changeWeights(outputs, nextLayerDeltas, layerIndex + 1);
            } else {
                deltas = null;
            }

        } else {
            // if it's the predictedSellingPrice layer just calculate delta
            // delta is error*derivative of predicted predictedSellingPrice

            deltas = calculateDeltas(outputs);
        }

        return deltas;
    }

    // calculate deltas for the predictedSellingPrice layer
    private ArrayList<Double> calculateDeltas(ArrayList<Double> outputs) {
        ArrayList<Double> deltas = new ArrayList<Double>(outputs.size());
        double error, errorSum = 0, delta;
        int outputLayerIndex = this.getSize() - 2;
        // System.out.println(outputs.size());

        for (int i = 0, ii = outputs.size(); i < ii; i++) {
            error = this.expectedOutputs.get(i) - outputs.get(i);
            delta = error
                    * layers.get(outputLayerIndex + 1).getTransferFunction()
                    .executeDerivative(outputs.get(i));
            errorSum = Math.sqrt(Math.pow(error / this.expectedOutputs.get(i),
                    2)) * 100;
            deltas.add(delta);
        }
        // this.learningCurve.add(errorSum / 2);
        // System.out.println(errorSum);

        // if(errorSum>20&&epis>500000){

        // deltas =null;}
        // else {
        this.learningCurve.add(errorSum);
        // }
        return deltas;
    }

    // calculate deltas for the hidden layers
    private ArrayList<Double> calculateDeltas(ArrayList<Double> outputs,
                                              int layerIndex, ArrayList<Double> nextLayerDeltas) {
        ArrayList<Double> deltas = new ArrayList<Double>(outputs.size());
        Layer nextLayer = layers.get(layerIndex);
        double nextLayerDeltaWeightedSum, delta;

        for (int i = 0, ii = outputs.size(); i < ii; i++) {
            nextLayerDeltaWeightedSum = 0;

            for (int k = 0, kk = nextLayerDeltas.size(); k < kk; k++) {
                nextLayerDeltaWeightedSum += nextLayer.getWeight(i + 1, k)
                        * nextLayerDeltas.get(k);
            }

            delta = nextLayerDeltaWeightedSum
                    * nextLayer.getPreviousLayer().getTransferFunction()
                    .executeDerivative(outputs.get(i));

            deltas.add(delta);
        }

        return deltas;
    }

    private void changeWeights(ArrayList<Double> inputArray,
                               ArrayList<Double> deltas, int layerIndex) {
        double difference;

        // change BIAS-weights
        for (int output = 0; output < deltas.size(); output++) {
            difference = layers.get(layerIndex).getWeightDifference(1,
                    deltas.get(output));
            layers.get(layerIndex).changeWeight(0, output, difference);
        }

        // change the rest of the weights
        for (int input = 1; input <= inputArray.size(); input++) {
            for (int output = 0; output < deltas.size(); output++) {
                difference = layers.get(layerIndex).getWeightDifference(
                        inputArray.get(input - 1), deltas.get(output));
                layers.get(layerIndex).changeWeight(input, output, difference);
            }
        }
    }

    // pass an input ArrayList through the network to receive predictedSellingPrice of the house
    public ArrayList<Double> propagate(ArrayList<Double> inputs) {

        ArrayList<Double> outputs = inputs;

        // calculate outputs of the neurons in the layer
        for (int layerIndex = 1; layerIndex < this.getSize(); ++layerIndex) {
            outputs = layers.get(layerIndex).propagateLayer(outputs);
        }

        return outputs;
    }

    // propagate an input ArrayList through the network and save the predictedSellingPrice to a file
    public int propagateAndSave(ArrayList<Double> inputs)
            throws FileNotFoundException, UnsupportedEncodingException {

        ArrayList<Double> outputs = this.propagate(inputs);
        Double[] dsf = new Double[outputs.size()];
        outputs.toArray(dsf);
        return (int) (dsf[0] * (1500000 - 52500) + 52500);
    }

    public void readTrainingPatterns(String fileName) throws IOException {

        this.patterns = readPatterns.readPatterns(fileName);
    }

    public void printPatterns() {
        Pattern pattern;
        int i, k;

        for (i = 0; i < this.patterns.size(); i++) {
            pattern = this.patterns.get(i);
            System.out.print("Pattern " + (i + 1) + ": X{");
            for (k = 0; k < pattern.x.size() - 1; k++) {
                System.out.print(pattern.x.get(k) + ", ");
            }
            System.out.print(pattern.x.get(k) + "}; Y(");
            for (k = 0; k < pattern.y.size() - 1; k++) {
                System.out.print(pattern.y.get(k) + ", ");
            }
            System.out.print(pattern.y.get(k) + "};\n");
        }
    }

    public void printWeights() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1, ii = this.layers.size(); i < ii; i++) {
            sb.append("\nWeights from layer " + (i) + " to " + (i + 1) + ":\n")
                    .append(this.layers.get(i).toString()).append("\n");
        }
        System.out.print(sb.toString());
    }

    public void getConnections() {
        for (int i = 1, ii = this.layers.size(); i < ii; i++) {
            numberOfWeakConnections += this.layers.get(i).connections();
        }
        System.out.println("weak connections " + numberOfWeakConnections);


    }

    public void getTotalWeights() {

        for (int i = 1, ii = this.layers.size(); i < ii; i++) {
            totalConnections += this.layers.get(i).methodTotalWeights();
        }
        System.out.println("total Connections " + totalConnections);

        System.out.println("percent weak connections " + (numberOfWeakConnections / totalConnections) * 100);
    }
}