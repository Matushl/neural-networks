package cz.muni.fi.pv021.network;

/**
 * Layer class. Represents number of units in a layer.
 *
 * Created by dhanak on 11/7/16.
 */
public class Layer {

    protected Neuron[] neurons;
    private int numberOfUnits;

    public Layer(int neurons) {
        this.numberOfUnits = neurons;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public Layer(int layerSize, int prevLayerSize) {
        numberOfUnits = layerSize;
        neurons = new Neuron[layerSize];

        for(int j = 0; j < numberOfUnits; j++)
            neurons[j] = new Neuron(prevLayerSize);
    }

    @Override
    public String toString() {
        return "Layer {" +
                "numberOfUnits=" + numberOfUnits +
                '}';
    }
}
