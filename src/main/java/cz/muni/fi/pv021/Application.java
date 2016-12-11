package cz.muni.fi.pv021;

import cz.muni.fi.pv021.functions.HyperbolicTransfer;
import cz.muni.fi.pv021.functions.SigmoidalTransfer;
import cz.muni.fi.pv021.network.MultiLayerPerceptron;
import cz.muni.fi.pv021.reader.MnistLoader;
import cz.muni.fi.pv021.samples.PersonalSamples;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by dhanak on 12/7/16.
 */
public class Application {
    
    private static int numberOfTrainSamples;
    private static int numberOfValidationSamples;
    private static int numberOfTestSamples;
    private static int maxNumberOfIterations;
    private static int[] layers;
    

    public static void main(String[] args) throws IOException, ParseException {
        
        // main app logic here
        numberOfTrainSamples = 4500;
        numberOfValidationSamples = 1500;
        numberOfTestSamples = 1000;
        maxNumberOfIterations = 250;
        
        layers = new int[]{ 784, 50, 10 };
        
        //testOfMoreLearningRatesAndThresholds(0.1, 5.1, 0.5, 90.0, 92.0, 1.0);
        //testOfMoreLearningRatesWithoutThresholds(0.1, 5.1, 0.5);
        //testOfOneLearningRateWithoutThreshold(2.00);
        //testOfOneLearningRateWithThreshold(2.00, 93.00);
        
        /*testOfOneLearningRateWithoutThreshold(0.70);
        testOfOneLearningRateWithoutThreshold(0.90);
        testOfOneLearningRateWithoutThreshold(1.10);
        testOfOneLearningRateWithoutThreshold(1.30);
        testOfOneLearningRateWithoutThreshold(1.50);
        
        testOfOneLearningRateWithoutThreshold(2.30);
        testOfOneLearningRateWithoutThreshold(2.50);
        testOfOneLearningRateWithoutThreshold(2.70);
        testOfOneLearningRateWithoutThreshold(2.80);*/
        
        testOfMoreLearningRatesAndThresholds(0.6, 5.1, 0.5, 90.0, 92.0, 1.0);
        
        /*testOfOneLearningRateWithoutThreshold(2.50);
        testOfOneLearningRateWithoutThreshold(2.70);
        testOfOneLearningRateWithoutThreshold(2.80);*/
    }
    
    
    private static void testOfMoreLearningRatesAndThresholds(
            double minLearningRate, double maxLearningRate, double incOfLearningRate,
            double minThreshold, double maxThreshold, double incOfThreshold) throws IOException, ParseException {
        
        System.out.println("numberOfTrainSamples&"+numberOfTrainSamples);
        System.out.println("numberOfValidationSamples&"+numberOfValidationSamples);
        System.out.println("numberOfTestSamples&"+numberOfTestSamples);
        System.out.println("maxNumberOfIterations&"+maxNumberOfIterations);
        
        System.out.println("Leayers&" + Arrays.toString(layers));
        
        System.out.println("LearningRate&Threshold(%)&NumberOfIterations&CorrectionOfTests(%)");
        
        MultiLayerPerceptron net;
        for(double learningRate = minLearningRate; learningRate <= maxLearningRate; learningRate += incOfLearningRate) {
            for(double threshold = minThreshold; threshold <= maxThreshold; threshold += incOfThreshold) {
        
                MnistLoader reader = new MnistLoader("src/main/resources/train-images.idx3-ubyte", "src/main/resources/train-labels.idx1-ubyte", "src/main/resources/t10k-images.idx3-ubyte", "src/main/resources/t10k-labels.idx1-ubyte", numberOfTrainSamples+numberOfValidationSamples, numberOfTestSamples);
                

                net = new MultiLayerPerceptron(layers, learningRate, new SigmoidalTransfer());
                double percentageOfCorrect = 0;
                int numberOfIterations = 0;
                while ((percentageOfCorrect < threshold) && ((numberOfIterations < maxNumberOfIterations) || (maxNumberOfIterations == 0))) {
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
                }
                System.out.print(learningRate + "&" + threshold + "&" + numberOfIterations + "&");
                
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
                }
                percentageOfCorrect = ((double)correct)/((double)numberOfTestSamples)*100;
                System.out.println(percentageOfCorrect);

            }
        }
        

        /*PersonalSamples pavel = new PersonalSamples("src/main/resources/pavel/");
        double[][] personalResults = new double[10][];
        for (int i = 0; i < 10; i++){
            personalResults[i] = net.execute(pavel.parsedImage[i]);
        }*/

    }
    
    private static void testOfMoreLearningRatesWithoutThresholds(
            double minLearningRate, double maxLearningRate, double incOfLearningRate) throws IOException, ParseException {

        double threshold = 100.00;           //treshold when net stops trainnig

        System.out.println("numberOfTrainSamples&"+numberOfTrainSamples);
        System.out.println("numberOfValidationSamples&"+numberOfValidationSamples);
        System.out.println("numberOfTestSamples&"+numberOfTestSamples);
        System.out.println("maxNumberOfIterations&"+maxNumberOfIterations);
        
        System.out.println("Leayers&" + Arrays.toString(layers));
        
        MultiLayerPerceptron net;
        for(double learningRate = minLearningRate; learningRate <= maxLearningRate; learningRate += incOfLearningRate) {
            System.out.println("LearningRate&"+learningRate);
            System.out.println("NumberOfIteration&CorrectionOfValidation(%)");

            MnistLoader reader = new MnistLoader("src/main/resources/train-images.idx3-ubyte", "src/main/resources/train-labels.idx1-ubyte", "src/main/resources/t10k-images.idx3-ubyte", "src/main/resources/t10k-labels.idx1-ubyte", numberOfTrainSamples+numberOfValidationSamples, numberOfTestSamples);


            net = new MultiLayerPerceptron(layers, learningRate, new SigmoidalTransfer());
            double percentageOfCorrect = 0;
            int numberOfIterations = 0;
            while ((percentageOfCorrect < threshold) && ((numberOfIterations < maxNumberOfIterations) || (maxNumberOfIterations == 0))) {
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
                System.out.println(numberOfIterations + "&" + percentageOfCorrect);
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
            }
            percentageOfCorrect = ((double)correct)/((double)numberOfTestSamples)*100;
            System.out.println("PercentageOfCorrectTests(%)&" + percentageOfCorrect);
        }
        

        /*PersonalSamples pavel = new PersonalSamples("src/main/resources/pavel/");
        double[][] personalResults = new double[10][];
        for (int i = 0; i < 10; i++){
            personalResults[i] = net.execute(pavel.parsedImage[i]);
        }*/

    }
    
    private static void testOfOneLearningRateWithoutThreshold(double learningRate) throws IOException, ParseException {

        double threshold = 100.00;           //treshold when net stops trainnig
        
        
        System.out.println("numberOfTrainSamples&"+numberOfTrainSamples);
        System.out.println("numberOfValidationSamples&"+numberOfValidationSamples);
        System.out.println("numberOfTestSamples&"+numberOfTestSamples);
        System.out.println("maxNumberOfIterations&"+maxNumberOfIterations);
        
        System.out.println("Leayers&" + Arrays.toString(layers));
        
        MultiLayerPerceptron net;
        
        MnistLoader reader = new MnistLoader("src/main/resources/train-images.idx3-ubyte", "src/main/resources/train-labels.idx1-ubyte", "src/main/resources/t10k-images.idx3-ubyte", "src/main/resources/t10k-labels.idx1-ubyte", numberOfTrainSamples+numberOfValidationSamples, numberOfTestSamples);

        System.out.println("LearningRate&"+learningRate);
        System.out.println("NumberOfIteration&CorrectionOfValidation(%)");
        
        net = new MultiLayerPerceptron(layers, learningRate, new SigmoidalTransfer());
        double percentageOfCorrect = 0;
        int numberOfIterations = 0;
        while ((percentageOfCorrect < threshold) && ((numberOfIterations < maxNumberOfIterations) || (maxNumberOfIterations == 0))) {
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
            System.out.println(numberOfIterations + "&" + percentageOfCorrect);
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
        }
        percentageOfCorrect = ((double)correct)/((double)numberOfTestSamples)*100;
        System.out.println("PercentageOfCorrectTests(%)&" + percentageOfCorrect);
        

        /*PersonalSamples pavel = new PersonalSamples("src/main/resources/pavel/");
        double[][] personalResults = new double[10][];
        for (int i = 0; i < 10; i++){
            personalResults[i] = net.execute(pavel.parsedImage[i]);
        }*/

    }
    
    private static void testOfOneLearningRateWithThreshold(double learningRate, double threshold) throws IOException, ParseException {        
        
        System.out.println("numberOfTrainSamples&"+numberOfTrainSamples);
        System.out.println("numberOfValidationSamples&"+numberOfValidationSamples);
        System.out.println("numberOfTestSamples&"+numberOfTestSamples);
        System.out.println("maxNumberOfIterations&"+maxNumberOfIterations);
        
        System.out.println("Leayers&" + Arrays.toString(layers));
        
        MultiLayerPerceptron net;
        
        MnistLoader reader = new MnistLoader("src/main/resources/train-images.idx3-ubyte", "src/main/resources/train-labels.idx1-ubyte", "src/main/resources/t10k-images.idx3-ubyte", "src/main/resources/t10k-labels.idx1-ubyte", numberOfTrainSamples+numberOfValidationSamples, numberOfTestSamples);

        System.out.println("LearningRate&"+learningRate);
        System.out.println("Threshold&"+threshold);
        //System.out.println("NumberOfIteration&CorrectionOfValidation(%)");    //when you want to see progress of teaching

        net = new MultiLayerPerceptron(layers, learningRate, new SigmoidalTransfer());
        double percentageOfCorrect = 0;
        int numberOfIterations = 0;
        while ((percentageOfCorrect < threshold) && ((numberOfIterations < maxNumberOfIterations) || (maxNumberOfIterations == 0))) {
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
            //System.out.println(numberOfIterations + "&" + percentageOfValidation);   //when you want to see progress of teaching
        }
        System.out.println("NumberOfIterations&" + numberOfIterations);
        System.out.println("CorrectionOfValidation(%)&" + percentageOfCorrect);

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
        }
        percentageOfCorrect = ((double)correct)/((double)numberOfTestSamples)*100;
        System.out.println("PercentageOfCorrectTests(%)&" + percentageOfCorrect);
        

        /*PersonalSamples pavel = new PersonalSamples("src/main/resources/pavel/");
        double[][] personalResults = new double[10][];
        for (int i = 0; i < 10; i++){
            personalResults[i] = net.execute(pavel.parsedImage[i]);
        }*/

    }
}
