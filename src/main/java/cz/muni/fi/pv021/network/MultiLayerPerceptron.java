package cz.muni.fi.pv021.network;

import cz.muni.fi.pv021.functions.Function;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 *
 * Created by dhanak on 12/7/16.
 */
public class MultiLayerPerceptron {

    protected double learningRate = 0.6;
    protected Layer[] layers;
    protected Function transferFunction;

    /**
     * Creates a MLP neural network
     *
     * @param layers
     * @param learningRate learning constant
     * @param transferFunction fucntion for transfer
     */
    public MultiLayerPerceptron(int[] layers, double learningRate, Function transferFunction) {
        this.learningRate = learningRate;
        this.transferFunction = transferFunction;

        this.layers = new Layer[layers.length];

        for(int i = 0; i < layers.length; i++)
        {
            if(i != 0)
            {
                this.layers[i] = new Layer(layers[i], layers[i - 1]);
            }
            else
            {
                this.layers[i] = new Layer(layers[i], 0);
            }
        }
    }

    /**
     * Runs the network.
     * Activation fucntion.
     *
     * @param input Input values
     * @return Output values returned from the network
     */
    public double[] execute(double[] input)
    {
        int i;
        int j;
        int k;
        double newValue;

        double output[] = new double[layers[layers.length - 1].getNumberOfUnits()];

        // Put input
        for(i = 0; i < layers[0].getNumberOfUnits(); i++)
        {
            layers[0].neurons[i].value = input[i];
        }

        // Execute - hiddens + output
        for(k = 1; k < layers.length; k++)
        {
            for(i = 0; i < layers[k].getNumberOfUnits(); i++)
            {
                newValue = 0.0;
                for(j = 0; j < layers[k - 1].getNumberOfUnits(); j++)
                    newValue += layers[k].neurons[i].weights[j] * layers[k - 1].neurons[j].value;

                newValue += layers[k].neurons[i].bias;

                layers[k].neurons[i].value = transferFunction.evaluate(newValue);
            }
        }


        // Get output
        for(i = 0; i < layers[layers.length - 1].getNumberOfUnits(); i++)
        {
            output[i] = layers[layers.length - 1].neurons[i].value;
        }

        return output;
    }


    /**
     * Backpropagation algorhitm for assisted learning.
     *
     * @param input Input values scaled between 0 - 1
     * @param output Expected output values scaled between 0 - 1
     * @return Delta generated error between output and expected output
     */
    public double backPropagate(double[] input, double[] output) {
        double newOutput[] = execute(input);
        double error;
        int i;
        int j;
        int k;

        // Calculate the output error
        for(i = 0; i < layers[layers.length - 1].getNumberOfUnits(); i++)
        {
            error = output[i] - newOutput[i];
            layers[layers.length - 1].neurons[i].delta = error * transferFunction.evaluateDerivate(newOutput[i]);
        }


        for(k = layers.length - 2; k >= 0; k--)
        {
            // Calculate the current layer error and recalculate the delta
            for(i = 0; i < layers[k].getNumberOfUnits(); i++)
            {
                error = 0.0;
                for(j = 0; j < layers[k + 1].getNumberOfUnits(); j++)
                    error += layers[k + 1].neurons[j].delta * layers[k + 1].neurons[j].weights[i];

                layers[k].neurons[i].delta = error * transferFunction.evaluateDerivate(layers[k].neurons[i].value);
            }

            // Update the weights of the next layer
            for(i = 0; i < layers[k + 1].getNumberOfUnits(); i++)
            {
                for(j = 0; j < layers[k].getNumberOfUnits(); j++)
                    layers[k + 1].neurons[i].weights[j] += learningRate * layers[k + 1].neurons[i].delta *
                            layers[k].neurons[j].value;
                layers[k + 1].neurons[i].bias += learningRate * layers[k + 1].neurons[i].delta;
            }
        }

        // Error calculation
        error = 0.0;

        for(i = 0; i < output.length; i++)
        {
            error += Math.abs(newOutput[i] - output[i]);
        }

        error = error / output.length;
        return error;
    }

    /**
     * Save a MLP network to file.
     *
     * @param path Path for saving MLP network
     * @return true If the network is stored properly.
     */
    public boolean save(String path) {
        try
        {
            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            oos.close();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * Loads the MLP network from a file.
     *
     * @param path Path to a file with MLP network.
     * @return MLP network loaded from file or null
     */
    public static MultiLayerPerceptron load(String path)
    {
        try
        {
            MultiLayerPerceptron net;

            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream oos = new ObjectInputStream(fin);
            net = (MultiLayerPerceptron) oos.readObject();
            oos.close();

            return net;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }

    public Function getTransferFunction() {
        return transferFunction;
    }

    public void setTransferFunction(Function transferFunction) {
        this.transferFunction = transferFunction;
    }

    public int getInputLayerSize()
    {
        return layers[0].getNumberOfUnits();
    }

    public int getOutputLayerSize()
    {
        return layers[layers.length - 1].getNumberOfUnits();
    }
}
