package cz.muni.fi.pv021;

import cz.muni.fi.pv021.functions.HyperbolicTransfer;
import cz.muni.fi.pv021.functions.SigmoidalTransfer;
import cz.muni.fi.pv021.network.MultiLayerPerceptron;
import cz.muni.fi.pv021.reader.MnistLoader;
import cz.muni.fi.pv021.samples.PersonalSamples;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Scanner; 

/**
 * Created by dhanak on 12/7/16.
 */
public class Application {
    
    private static int numberOfTrainSamples;
    private static int numberOfValidationSamples;
    private static int numberOfTestSamples;
    private static int maxNumberOfIterations;
    private static int[] layers;
    private static double learningRate;
    private static double threshold;
    

    public static void main(String[] args) throws IOException, ParseException {

        int action = startSetup();
        
        if (action == 1) {
            testOfOneLearningRateWithoutThreshold(learningRate);
        } else {
            testOfOneLearningRateWithThreshold(learningRate, threshold);
        }
        
    }
    
    private static int startSetup() throws IOException, ParseException {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Hi, this is MLP and dataset is MNIST.");
        System.out.println("Our best result was approached when we use 1 hidden layer of size 50 neurons and learning rate 2.8.");
        
        System.out.println("Start setup...");
        setNumberOfHiddenLayers();
        
        setNumberOfTVSamples(true);
        setNumberOfTestSamples();

        setLearningRate();
        
        int action = setAction();
        
        if (action == 2) {
            setThreshold();
        }
        
        setMaxNumberOfIterations(action);
        return action;
        
    }
    
    private static void setNumberOfHiddenLayers() {
        Scanner scan = new Scanner(System.in);
        int numberOfHiddenLayers = 0;
        System.out.print("The number of hidden layers: ");
        try {
            numberOfHiddenLayers = scan.nextInt();
        } catch(Exception e) {
            System.out.println("Something gone wrong. Try again.");
            setNumberOfHiddenLayers();
        }
            
        layers = new int[numberOfHiddenLayers+2];
        layers[0] = 784;
        for (int i = 0; i < numberOfHiddenLayers; i++) {
            setNumberOfNeuronsInLayer(i);
        }
        layers[numberOfHiddenLayers+1] = 10;
    }
    
    private static void setNumberOfNeuronsInLayer(int i) {
        Scanner scan = new Scanner(System.in);
        System.out.print("The number of neurons in the " + (i+1) + ". hidden layer: ");
        try {
            layers[i+1] = scan.nextInt();
        } catch(Exception e) {
            System.out.println("Something gone wrong. Try again.");
            setNumberOfNeuronsInLayer(i);
        }
    }
    
    private static void setNumberOfTVSamples(boolean first) {
        Scanner scan = new Scanner(System.in);
        numberOfTrainSamples = 60000;
        numberOfValidationSamples = 1;
        while (numberOfTrainSamples + numberOfValidationSamples > 60000) {
            System.out.print("The number of training and validation samples cannot be more than 60 000.");
            if (first) {
                System.out.print(" (We recommend to have three times more training samples as validation samples)");
            }
            System.out.println();
            
            first = false;
            
            try {
                System.out.print("Set the number of training samples: ");
                numberOfTrainSamples = scan.nextInt();
            
                System.out.print("Set the number of validation samples: ");
                numberOfValidationSamples = scan.nextInt();
            } catch(Exception e) {
                System.out.println("Something gone wrong. Try again.");
                setNumberOfTVSamples(false);
            }
            
        }
    }
    
    private static void setNumberOfTestSamples() {
        Scanner scan = new Scanner(System.in);
        numberOfTestSamples = 10001;
        while (numberOfTestSamples > 10000) {
            System.out.println("The maximal number of test samples is 10 000.");
            
            System.out.print("Set the number of train samples: ");
            try {
                numberOfTestSamples = scan.nextInt();
            } catch(Exception e) {
                System.out.println("Something gone wrong. Try again.");
                setNumberOfTestSamples();
            }
        }
    }
    
    private static void setLearningRate() {
        Scanner scan = new Scanner(System.in);
        
        System.out.print("Set the learning rate: ");

        try {
            learningRate = scan.nextDouble();
        } catch(Exception e) {
            System.out.println("Something gone wrong. Try again.");
            setLearningRate();
        }
    }
    
    private static void setThreshold() {
        Scanner scan = new Scanner(System.in);
        
        System.out.print("Set the limit of correct tests of validation tests when you want to stop learning (%): ");

        try {
            threshold = scan.nextDouble();
        } catch(Exception e) {
            System.out.println("Something gone wrong. Try again.");
            setThreshold();
        }
    }
    
    private static void setMaxNumberOfIterations(int action) {
        Scanner scan = new Scanner(System.in);
        if (action == 1) {
            System.out.print("The number of iterations of learning: ");
        } else {
            System.out.print("The maximal number of iterations of learning (for sure): ");
        }
        
        try {
            maxNumberOfIterations = scan.nextInt();
        } catch(Exception e) {
            System.out.println("Something gone wrong. Try again.");
            setMaxNumberOfIterations(action);
        }
    }
    
    private static int setAction() {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Do you want to let this network learn for explicit number of iterations (1) or ");
        System.out.println("do you want to let it learn until it reach some percentage of correctness on validation data(2)? 1/2");
        
        int action = 0;
        boolean first = true;
        while ((action != 1) && (action != 2)) {
            if (!(first)) {
                System.out.println("Answer is '1' for the first option or '2' for the second.");
            }
            first = false;
            
            try {
                action = scan.nextInt();
            } catch(Exception e) {
                System.out.println("Something gone wrong. Try again." + e.toString());
                setAction();
            }
        }
        
        return action;
        
    }
    
    private static void testOfMoreLearningRatesAndThresholds(
            double minLearningRate, double maxLearningRate, double incOfLearningRate,
            double minThreshold, double maxThreshold, double incOfThreshold) throws IOException, ParseException {
        
        System.out.println("numberOfTrainingSamples&"+numberOfTrainSamples);
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
    }
    
    private static void testOfMoreLearningRatesWithoutThresholds(
            double minLearningRate, double maxLearningRate, double incOfLearningRate) throws IOException, ParseException {

        double threshold = 100.00;           //treshold when net stops trainnig

        System.out.println("numberOfTrainingSamples&"+numberOfTrainSamples);
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
    }
    
    private static void testOfOneLearningRateWithoutThreshold(double learningRate) throws IOException, ParseException {

        double threshold = 100.00;           //treshold when net stops trainnig
        
        
        System.out.println("numberOfTrainingSamples&"+numberOfTrainSamples);
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
    }
    
    private static void testOfOneLearningRateWithThreshold(double learningRate, double threshold) throws IOException, ParseException {        
        
        System.out.println("numberOfTrainingSamples&"+numberOfTrainSamples);
        System.out.println("numberOfValidationSamples&"+numberOfValidationSamples);
        System.out.println("numberOfTestSamples&"+numberOfTestSamples);
        System.out.println("maxNumberOfIterations&"+maxNumberOfIterations);
        
        System.out.println("Leayers&" + Arrays.toString(layers));
        
        MultiLayerPerceptron net;
        
        MnistLoader reader = new MnistLoader("src/main/resources/train-images.idx3-ubyte", "src/main/resources/train-labels.idx1-ubyte", "src/main/resources/t10k-images.idx3-ubyte", "src/main/resources/t10k-labels.idx1-ubyte", numberOfTrainSamples+numberOfValidationSamples, numberOfTestSamples);

        System.out.println("LearningRate&"+learningRate);
        System.out.println("Threshold&"+threshold);
        System.out.println("NumberOfIteration&CorrectionOfValidation(%)");    //when you want to see progress of teaching

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
            System.out.println(numberOfIterations + "&" + percentageOfCorrect);   //when you want to see progress of teaching
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
        
        

    }
    
    private static void testOwnNumbers(String path) throws IOException, ParseException {
        
        MultiLayerPerceptron net;
        
        net = new MultiLayerPerceptron(layers, 1.0, new SigmoidalTransfer());
        
        net.load(path);
        
        
        PersonalSamples pavel = new PersonalSamples("src/main/resources/pavel/");
        double[][] personalResults = new double[10][];
        for (int i = 0; i < 10; i++){
            personalResults[i] = net.execute(pavel.parsedImage[i]);
        }
        
        //correct = 0;
        for (int i = 0; i < 10; i++){
            int maxIndex = 0;
            double maxResult = personalResults[i][0];
            for(int j = 0; j < 10; j++){
                if(personalResults[i][j] > maxResult){
                    maxIndex = j;
                    maxResult = personalResults[i][j];
                }
            }
           /* if (i == maxIndex){
                correct++;
            }*/
            System.out.println("Number: " + i + " Result: " + maxIndex);
        }
        //percentageOfCorrect = ((double)correct)/((double)10)*100;
        //System.out.println("PercentageOfCorrectTests(%)&" + percentageOfCorrect);

        
    }
}
