package cz.muni.fi.pv021;

import cz.muni.fi.pv021.functions.HyperbolicTransfer;
import cz.muni.fi.pv021.functions.SigmoidalTransfer;
import cz.muni.fi.pv021.network.MultiLayerPerceptron;
import cz.muni.fi.pv021.reader.MnistLoader;
import cz.muni.fi.pv021.samples.PersonalSamples;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by dhanak on 12/7/16.
 */
public class Application {

    public static void main(String[] args) throws IOException, ParseException {

       // main app logic here
        int numberOfTrainSamples = 45000;
        int numberOfValidationSamples = 15000;
        int numberOfTestSamples = 10000;
        double threshold = 95.00;           //treshold when net stops trainnig
        int maxNumberOfIterations = 50;     //maximal number of iterations of learning
        double learningRate = 2.00;
        MnistLoader reader = new MnistLoader("src/main/resources/train-images.idx3-ubyte", "src/main/resources/train-labels.idx1-ubyte", "src/main/resources/t10k-images.idx3-ubyte", "src/main/resources/t10k-labels.idx1-ubyte", numberOfTrainSamples+numberOfValidationSamples, numberOfTestSamples);
        int[] layers = new int[]{ 784, 30, 10 };

        
        MultiLayerPerceptron net = new MultiLayerPerceptron(layers, learningRate, new SigmoidalTransfer());
        double percentageOfCorrect = 0;
        int numberOfIterations = 0;
        while ((percentageOfCorrect < threshold) && (numberOfIterations < maxNumberOfIterations)) {
            numberOfIterations += 1;
            
            for(int i = 0; i < numberOfTrainSamples; i++){
                net.backPropagate(reader.trainImages[i], reader.trainLabels[i]);
            }

            double[][] results = new double[numberOfValidationSamples][];
            for (int i = 0; i < numberOfValidationSamples; i++){
                results[i] = net.execute(reader.trainImages[numberOfTrainSamples+i]);
            }

            int correct = 0;
            for (int i = 0; i < numberOfValidationSamples; i++){
                int maxIndex = 0;
                double maxResult = results[i][0];
                for(int j = 0; j < 10; j++){
                    if(results[i][j] > maxResult){
                        maxIndex = j;
                        maxResult = results[i][j];
                    }
                }
                if (reader.trainLabels[numberOfTrainSamples+i][maxIndex] == 1){
                    correct++;
                }
            }
            percentageOfCorrect = ((double)correct)/((double)numberOfValidationSamples)*100;
            System.out.println("NumberOfIterations: " + numberOfIterations + " Success: " + percentageOfCorrect + " %");
        }
 
        
        double[][] results = new double[numberOfTestSamples][];
        for (int i = 0; i < numberOfTestSamples; i++){
            results[i] = net.execute(reader.testImages[i]);
        }
        
        int correct = 0;
        for (int i = 0; i < numberOfTestSamples; i++){
            int maxIndex = 0;
            double maxResult = results[i][0];
            for(int j = 0; j < 10; j++){
                if(results[i][j] > maxResult){
                    maxIndex = j;
                    maxResult = results[i][j];
                }
            }
            if (reader.testLabels[i][maxIndex] == 1){
                correct++;
            }
            /*for(int j = 0; j < 10; j++){
                if(results[i][j] > threshold){
                    if (reader.testLabels[i][j] == 1){
                        correct++;
                        break;
                    }
                }
            }*/
        }
        percentageOfCorrect = ((double)correct)/((double)numberOfTestSamples)*100;
        System.out.println(percentageOfCorrect + " %");

        PersonalSamples pavel = new PersonalSamples("src/main/resources/pavel/");
        double[][] personalResults = new double[10][];
        for (int i = 0; i < 10; i++){
            personalResults[i] = net.execute(pavel.parsedImage[i]);
        }

    }
}
