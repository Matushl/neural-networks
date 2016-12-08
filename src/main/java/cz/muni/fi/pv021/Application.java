package cz.muni.fi.pv021;

import cz.muni.fi.pv021.functions.HyperbolicTransfer;
import cz.muni.fi.pv021.network.MultiLayerPerceptron;
import cz.muni.fi.pv021.reader.MnistLoader;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by dhanak on 12/7/16.
 */
public class Application {

    public static void main(String[] args) throws IOException, ParseException {

       // main app logic here
        int numberOfTrainSamples = 20000;
        int numberOfTestSamples = 10;
        MnistLoader reader = new MnistLoader("src/main/resources/train-images.idx3-ubyte", "src/main/resources/train-labels.idx1-ubyte", "src/main/resources/t10k-images.idx3-ubyte", "src/main/resources/t10k-labels.idx1-ubyte", numberOfTrainSamples, numberOfTestSamples);
        int[] layers = new int[]{ 784, 30, 10 };

        MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.6, new HyperbolicTransfer());
        for(int i = 0; i < numberOfTrainSamples; i++){
            net.backPropagate(reader.trainImages[i], reader.trainLabels[i]);
        }
        double[] pom = new double[10];

            pom = net.execute(reader.testImages[0]);


        for(int j = 0; j < 10; j++){
            System.out.println(pom[j]);
        }
        for(int k = 0; k < 10; k++){
            System.out.println(reader.testLabels[0][k]);
        }
    }
}
