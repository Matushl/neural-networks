package cz.muni.fi.pv021.network;

import java.util.List;
import java.util.Random;

/**
 * Created by dhanak on 11/11/16.
 */
public class NeuralNetwork {

    private final float lambda = 1;

    /**
     *
     */
    private float[] weights;

    /**
     *
     */
    private float c = 0.01f;

    /**
     *
     */
    private float y = 0;

    /**
     * Inputs of the network
     */
    private List<NeuralNetwork> inputs;

    /**
     * Outputs of the network
     */
    private List<NeuralNetwork> outputs;

    /**
     *
     */
    private double Ek_yr = 0;

    /**
     * Number of layer we are in
     */
    private int layer;

    /**
     * Position in layer of neural network
     */
    private int positionInLayer;

    public NeuralNetwork(List<NeuralNetwork> inputs, int layer, int positionInLayer) {
        this.layer = layer;
        this.positionInLayer = positionInLayer;
        this.inputs = null;
        this.outputs = null;

        if (inputs == null) {
            // This means it is an input layer of network
        } else {
            this.inputs = inputs;
            weights = new float[inputs.size() + 1];
            Random rand = new Random();
            for (int i = 0; i < weights.length; i++) {
                weights[i] = rand.nextFloat() * 2 - 1;
            }
        }
    }

    /**
     * Activation function.
     *
     * @param sum
     * @return
     */
    public int activate(double sum) {
        if (sum > 0) return 1;
        else return -1;
    }
}
