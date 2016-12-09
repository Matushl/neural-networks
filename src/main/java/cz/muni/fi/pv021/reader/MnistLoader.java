package cz.muni.fi.pv021.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Pavel on 12-Nov-16.
 */
public class MnistLoader {
    public double[][] trainLabels;
    public double[][] testLabels;
    public double[][] trainImages;
    public double[][] testImages;
    int numberOfTrainSamples;
    int numberOfTestSamples;

    public MnistLoader(String trainImages, String trainLabels, String testImages, String testLabels, int numberOfTrainSamples, int numberOfTestSamples){
        this.numberOfTrainSamples = numberOfTrainSamples;
        this.numberOfTestSamples = numberOfTestSamples;
        this.trainLabels = loadLabels(trainLabels, this.numberOfTrainSamples);
        this.testLabels = loadLabels(testLabels, this.numberOfTestSamples);
        this.trainImages = loadImages(trainImages, this.numberOfTrainSamples);
        this.testImages = loadImages(testImages, this.numberOfTestSamples);
    }

    private double[][] loadImages(String fileName, int numberOfSamples){
        FileInputStream labelStream = null;
        double[][] imageMatrices = null;
        try{
            labelStream = new FileInputStream(fileName);
            int magicNumber = this.readInt(labelStream);
            int numberOfImages = this.readInt(labelStream);
            int numberOfRows = this.readInt(labelStream);
            int numberOfColumns = this.readInt(labelStream);
            imageMatrices = new double[numberOfImages][numberOfRows*numberOfColumns];
            int c;
            int imageIndex = 0;
            int bitIndex = 0;

            while ((c = labelStream.read()) != -1) {
                imageMatrices[imageIndex][bitIndex] = (double)c/1000;
                bitIndex++;
                if(bitIndex == 784){
                    bitIndex = 0;
                    imageIndex++;
                }
                if(imageIndex == numberOfSamples) {
                    break;
                }
            }

        }catch (FileNotFoundException error){
            error.printStackTrace();
        }catch (IOException error){
            error.printStackTrace();
        }finally {
            if (labelStream != null) {
                try {
                    labelStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageMatrices;
    }

    private double[][] loadLabels(String fileName, int numberOfSamples){
        FileInputStream labelStream = null;
        double[][] labels = null;
        try{
            labelStream = new FileInputStream(fileName);
            int magicNumber = this.readInt(labelStream);
            int numberOfLabels = this.readInt(labelStream);
            labels = new double[numberOfLabels][10];
            int c;
            int labelIndex = 0;
            while ((c = labelStream.read()) != -1) {
                labels[labelIndex][c] = 1;
                labelIndex++;
                if(labelIndex == numberOfSamples) {
                    break;
                }
            }

        }catch (FileNotFoundException error){
            error.printStackTrace();
        }catch (IOException error){
            error.printStackTrace();
        }finally {
            if (labelStream != null) {
                try {
                    labelStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return labels;

    }

    private int readInt(FileInputStream fileStream){
        int value = -1;
        try {
            value = (fileStream.read() << 24) | (fileStream.read() << 16) | (fileStream.read() << 8) | (fileStream.read());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
