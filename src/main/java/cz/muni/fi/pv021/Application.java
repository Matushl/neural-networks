package cz.muni.fi.pv021;

import cz.muni.fi.pv021.functions.HyperbolicTransfer;
import cz.muni.fi.pv021.functions.SigmoidalTransfer;
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
        int numberOfTrainSamples = 30000;
        int numberOfTestSamples = 1000;
        double threshold = 0.8;
        MnistLoader reader = new MnistLoader("src/main/resources/train-images.idx3-ubyte", "src/main/resources/train-labels.idx1-ubyte", "src/main/resources/t10k-images.idx3-ubyte", "src/main/resources/t10k-labels.idx1-ubyte", numberOfTrainSamples, numberOfTestSamples);
        int[] layers = new int[]{ 784, 30, 10 };

        MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 3.0, new SigmoidalTransfer());
        for(int i = 0; i < numberOfTrainSamples; i++){
            net.backPropagate(reader.trainImages[i], reader.trainLabels[i]);
        }
        double[][] results = new double[numberOfTestSamples][];
        for (int i = 0; i < numberOfTestSamples; i++){
            results[i] = net.execute(reader.testImages[i]);
        }
        int correct = 0;
        for (int i = 0; i < numberOfTestSamples; i++){
            for(int j = 0; j < 10; j++){
                if(results[i][j] > threshold){
                    if (reader.testLabels[i][j] == 1){
                        correct++;
                        break;
                    }
                }
            }
        }
        double percentageOfCorrect = ((double)correct)/((double)numberOfTestSamples)*100;
        System.out.println(percentageOfCorrect + " %");
    }
}
