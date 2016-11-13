package loader.MnistLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Pavel on 12-Nov-16.
 */
public class MnistLoader {
    int[] trainLabels;
    int[] testLabels;
    int[][][] trainImages;
    int[][][] testImages;

    public MnistLoader(String trainImages, String trainLabels, String testImages, String testLabels){
        this.trainLabels = loadLabels(trainLabels);
        this.testLabels = loadLabels(testLabels);
        //this.trainImages = loadImages(trainImages);
        this.testImages = loadImages(testImages);
    }

    private int[][][] loadImages(String fileName){
        FileInputStream labelStream = null;
        int[][][] imageMatrices = null;
        try{
            labelStream = new FileInputStream(fileName);
            int magicNumber = this.readInt(labelStream);
            int numberOfImages = this.readInt(labelStream);
            int numberOfRows = this.readInt(labelStream);
            int numberOfColumns = this.readInt(labelStream);
            imageMatrices = new int[numberOfImages][numberOfRows][numberOfColumns];
            int c;
            int imageIndex = 0;
            int rowIndex = 0;
            int columnIndex = 0;
            while ((c = labelStream.read()) != -1) {
                imageMatrices[imageIndex][rowIndex][columnIndex] = c;
                columnIndex++;
                if(columnIndex == numberOfColumns){
                    rowIndex++;
                    columnIndex = 0;
                }
                if(rowIndex == numberOfRows){
                    rowIndex = 0;
                    imageIndex++;
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

    private int[] loadLabels(String fileName){
        FileInputStream labelStream = null;
        int[] labels = null;
        try{
            labelStream = new FileInputStream(fileName);
            int magicNumber = this.readInt(labelStream);
            int numberOfLabels = this.readInt(labelStream);
            labels = new int[numberOfLabels];
            int c;
            int labelIndex = 0;
            while ((c = labelStream.read()) != -1) {
                labels[labelIndex] = c;
                labelIndex++;
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
